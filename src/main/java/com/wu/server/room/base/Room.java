package com.wu.server.room.base;

import com.wu.server.Until.LogUntil;
import com.wu.server.bean.Status;
import com.wu.server.bean.User;
import com.wu.server.netty.NetManage;
import com.wu.server.proto.net.MsgBattleResult;
import com.wu.server.proto.net.MsgEnterBattle;
import com.wu.server.proto.net.MsgGetRoomInfo;
import com.wu.server.proto.net.MsgLeaveBattle;
import com.wu.server.proto.base.MsgBase;
import com.wu.server.proto.base.PlayerInfo;
import com.wu.server.proto.base.TankInfo;
import com.wu.server.room.manage.work.RoomWorker;
import com.wu.server.status.DataManage;

import java.lang.reflect.Member;
import java.util.Date;
import java.util.HashMap;

public class Room {
    public Room(int roomId ,String ownerId) {
        this.roomId = roomId;
        this.ownerId = ownerId;
        AddPlayer(ownerId);
    }

    //房间id
    public int roomId = 0;
    //最大玩家数
    public int maxPlayer = 6;
    //玩家列表
    public HashMap<String, RoomMember> playerIds = new HashMap<String, RoomMember>();
    //房主id
    public String ownerId = "";
    //状态
//    public enum Status {
//        PREPARE, FIGHT
//    }
    public volatile int status = Status.PREPARE;
    //出生点位置配置
    static float[][][] birthConfig =  {
            //阵营1出生点
            {
                    { 5f, 0f, -10f,     0f, 0f,0f},//出生点1
                    { 0f, 0f, -10f,     0f, 0f,0f},//出生点2
                    {-5f, 0f, -10f,     0f, 0f,0f},//出生点3
            },
            //阵营2出生点
            {
                    { 5f, 0f, 30f,     0f, 0f,0f},//出生点1
                    { 0f, 0f, 30f,     0f, 0f,0f},//出生点2
                    {-5f, 0f, 30f,     0f, 0f,0f},//出生点3
            },
    };
    //上一次判断结果的时间
    private long lastjudgeTime = 0;

    //添加玩家
    public boolean AddPlayer(String id){
        //获取玩家
        User user = DataManage.INSTANCE.onLineUser.get(id);
        if(user == null){
            LogUntil.logger.warn("Room AddPlayer: room.AddPlayer fail, player is null");
            return false;
        }
        //房间人数
        if(playerIds.size() >= maxPlayer){
            LogUntil.logger.warn("Room AddPlayer: room.AddPlayer fail, reach maxPlayer");
            return false;
        }
        //准备状态才能加人
        if(status != Status.PREPARE){
            LogUntil.logger.warn("Room AddPlayer: room.AddPlayer fail, not PREPARE");
            return false;
        }
        //已经在房间里
        if(playerIds.containsKey(id)){
            LogUntil.logger.warn("Room AddPlayer: room.AddPlayer fail, already in this room");
            return false;
        }
        //加入列表
        playerIds.put(id,new RoomMember(SwitchCamp(),id));
        //设置玩家数据
       user.roomId = this.roomId;
       //todo  player.setRoomId( this.id);
        //设置房主
        if(ownerId.equals("") ){
            ownerId = user.getId();
        }
        //广播
        Broadcast(ToMsg(new MsgGetRoomInfo()));
        return true;
    }

    //分配阵营
    public int SwitchCamp() {
        //计数
        int count1 = 0;
        int count2 = 0;
        for( String id : playerIds.keySet() ){
            RoomMember player = playerIds.get(id);
            if(player.getCamp()== 1) {count1++;}
            if(player.getCamp() == 2) {count2++;}
        }

        //选择
        if (count1 <= count2){
            return 1;
        }
        else{
            return 2;
        }
    }

    //是不是房主
    public boolean isOwner(String id){
        return id.equals(ownerId);
    }

    //删除玩家
    public boolean RemovePlayer(String id) {
        //获取玩家
         User user = DataManage.INSTANCE.onLineUser.get(id);
        if(user == null){
            LogUntil.logger.info("Room RemovePlayer: room.RemovePlayer fail, player is null");
            return false;
        }
        //没有在房间里
        if(!playerIds.containsKey(id)){
            LogUntil.logger.warn("Room RemovePlayer: room.RemovePlayer fail, not in this room");
            return false;
        }

        //战斗状态不可退出
        if(status == Status.FIGHT){
            LogUntil.logger.warn("Room RemovePlayer: room.RemovePlayer fail, room‘s  status is FIGHT");
            return false;
        }

        //删除列表
        playerIds.remove(id);
        //设置玩家数据
        user.roomId = -1 ;
        //设置房主
        if(ownerId.equals(user.getId()) ){
            ownerId = SwitchOwner();
        }

        //房间非空
        if(playerIds.size() != 0){
            //广播
            Broadcast(ToMsg(new MsgGetRoomInfo()));
        }

        return true;
    }

    //选择房主
    public String SwitchOwner() {
        //选择第一个玩家

        for(String id : playerIds.keySet()) {
            return id;
        }
        //房间没人
        return "";
    }


    //广播消息
    public void Broadcast(MsgBase msg){
        for(String id : playerIds.keySet()) {
            NetManage.send(id,msg);
        }
    }

    //生成MsgGetRoomInfo协议
    public MsgBase ToMsg(MsgGetRoomInfo msg){
        int count = playerIds.size();
        msg.players = new PlayerInfo[count];
        //players
        int i = 0;
        for(String id : playerIds.keySet()){
            RoomMember roomMember = playerIds.get(id);
            PlayerInfo playerInfo = new PlayerInfo();
            User user = DataManage.INSTANCE.onLineUser.get(id);
            //赋值
            playerInfo.id = roomMember.getId();
            playerInfo.camp = roomMember.getCamp();
            playerInfo.win = user.playerData.getWin();
            playerInfo.lost = user.playerData.getLost();
            playerInfo.isOwner = 0;
            if(isOwner(roomMember.getId())){
                playerInfo.isOwner = 1;
            }

            msg.players[i] = playerInfo;
            i++;
        }
        return msg;
    }

    //能否开战
    public boolean CanStartBattle() {
        //已经是战斗状态
        if (status != Status.PREPARE){
            return false;
        }
        //统计每个队伍的玩家数
        int count1 = 0;
        int count2 = 0;
        for(String id : playerIds.keySet()) {
            RoomMember roomMember = playerIds.get(id);
            if(roomMember.getCamp() == 1){ count1++; }
            else { count2++; }
        }
        //每个队伍至少要有1名玩家
        if (count1 < 1 || count2 < 1){
            return false;
        }
        return true;
    }

    //初始化位置
    private void SetBirthPos(RoomMember roomMember, int index){
        int camp = roomMember.getCamp();
        roomMember.setPosition(
                birthConfig[camp-1] [index][0],
                birthConfig[camp-1][ index][1],
                birthConfig[camp-1][ index][2],
                birthConfig[camp-1][ index][3],
                birthConfig[camp-1][ index][4],
                birthConfig[camp-1][ index][5]);


    }

    //玩家数据转成TankInfo
    public TankInfo PlayerToTankInfo(RoomMember roomMember){
        TankInfo tankInfo = new TankInfo(
                roomMember.getId(),
                roomMember.getCamp(),
                roomMember.getHp(),
                roomMember.getX(),
                roomMember.getY(),
                roomMember.getZ(),
                roomMember.getEx(),
                roomMember.getEy(),
                roomMember.getEz());


        return tankInfo;
    }

    //重置玩家战斗属性
    private void ResetPlayers(){
        //位置和旋转
        int count1 = 0;
        int count2 = 0;
        for(String id : playerIds.keySet()) {
            RoomMember roomMember = playerIds.get(id);
            if(roomMember.getCamp() == 1){
                SetBirthPos(roomMember, count1);
                count1++;
            }
            else {
                SetBirthPos(roomMember, count2);
                count2++;
            }
        }
        //生命值
        for(String id : playerIds.keySet()) {
            RoomMember roomMember = playerIds.get(id);
            roomMember.setHp(100);
        }
    }

    //开战
    public boolean StartBattle() {
        if(!CanStartBattle()){
            return false;
        }
        //状态
        status = Status.FIGHT;
        //玩家战斗属性
        ResetPlayers();
        //返回数据
        MsgEnterBattle msg = new MsgEnterBattle();
        msg.mapId = 1;
        msg.tanks = new TankInfo[playerIds.size()];

        int i=0;
        for(String id : playerIds.keySet()) {
            RoomMember roomMember = playerIds.get(id);
            msg.tanks[i] = PlayerToTankInfo(roomMember);
            i++;
        }
        Broadcast(msg);
        return true;
    }


    //是否死亡
    public boolean IsDie(RoomMember roomMember){
        return roomMember.getHp()<= 0;
    }


    //定时更新
    public void Update(){
        //状态判断
        if(status != Status.FIGHT){
            return;
        }
//        //时间判断
//        if(NetManager.GetTimeStamp() - lastjudgeTime < 10f){
//            return;
//        }
//        lastjudgeTime = NetManager.GetTimeStamp();
        //胜负判断
        int winCamp = Judgment();
        //尚未分出胜负
        if(winCamp == 0){
            return;
        }
        //某一方胜利，结束战斗
        status = Status.PREPARE;
        //统计信息
        for(String id : playerIds.keySet()) {
            RoomMember roomMember = playerIds.get(id);
            User user = DataManage.INSTANCE.onLineUser.get(id);
            if(roomMember.getCamp() == winCamp){
                user.playerData.setWin(user.playerData.getWin() + 1);
            }
            else{ user.playerData.setLost(user.playerData.getLost() + 1);}
        }
        //发送Result
        MsgBattleResult msg = new MsgBattleResult();
        msg.winCamp = winCamp;
        Broadcast(msg);
    }

    //胜负判断
    public int Judgment(){
        //存活人数
        int count1 = 0;
        int count2 = 0;
        for(String id : playerIds.keySet()) {
            RoomMember roomMember = playerIds.get(id);
            if(!IsDie(roomMember)){
                if(roomMember.getCamp() == 1){count1++;};
                if(roomMember.getCamp() == 2){count2++;};
            }
        }
        //判断
        if(count1 <= 0){
            return 2;
        }
        else if(count2 <= 0){
            return 1;
        }
        return 0;
    }
}
