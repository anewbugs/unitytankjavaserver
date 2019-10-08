package com.wu.server.bean;

import com.wu.server.proto.MsgBattleResult;
import com.wu.server.proto.MsgEnterBattle;
import com.wu.server.proto.MsgGetRoomInfo;
import com.wu.server.proto.MsgLeaveBattle;
import com.wu.server.proto.base.MsgBase;
import com.wu.server.proto.base.PlayerInfo;
import com.wu.server.proto.base.TankInfo;
import com.wu.server.service.PlayerService;
import com.wu.server.service.RoomService;

import java.util.Date;
import java.util.HashMap;

public  class Room {
    //id
    public int id = 0;
    //最大玩家数
    public int maxPlayer = 6;
    //玩家列表
    public HashMap<String, Boolean> playerIds = new HashMap<String, Boolean>();
    //房主id
    public String ownerId = "";
    //状态
//    public enum Status {
//        PREPARE, FIGHT
//    }
    public int status = Status.PREPARE;
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
        Player player = PlayerService.GetPlayer(id);
        if(player == null){
            System.out.println(new Date() +"Room AddPlayer: room.AddPlayer fail, player is null");
            return false;
        }
        //房间人数
        if(playerIds.size() >= maxPlayer){
            System.out.println(new Date() +"Room AddPlayer: room.AddPlayer fail, reach maxPlayer");
            return false;
        }
        //准备状态才能加人
        if(status != Status.PREPARE){
            System.out.println(new Date() +"Room AddPlayer: room.AddPlayer fail, not PREPARE");
            return false;
        }
        //已经在房间里
        if(playerIds.containsKey(id)){
            System.out.println(new Date() +"Room AddPlayer: room.AddPlayer fail, already in this room");
            return false;
        }
        //加入列表
        playerIds.put(id,true);
        //设置玩家数据
        player.setCamp(SwitchCamp());
        player.setRoomId( this.id);
        //设置房主
        if(ownerId.equals("") ){
            ownerId = player.getId();
        }
        //广播
        Broadcast(ToMsg());
        return true;
    }

    //分配阵营
    public int SwitchCamp() {
        //计数
        int count1 = 0;
        int count2 = 0;
        for( String id : playerIds.keySet() ){
            Player player = PlayerService.GetPlayer(id);
            if(player.getCamp() == 1) {count1++;}
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
    public boolean isOwner(Player player){
        return player.getId().equals(ownerId);
    }

    //删除玩家
    public boolean RemovePlayer(String id) {
        //获取玩家
        Player player = PlayerService.GetPlayer(id);
        if(player == null){
            System.out.println( new Date() + "Room RemovePlayer: room.RemovePlayer fail, player is null");
            return false;
        }
        //没有在房间里
        if(!playerIds.containsKey(id)){
            System.out.println( new Date() + "Room RemovePlayer: room.RemovePlayer fail, not in this room");
            return false;
        }
        //删除列表
        playerIds.remove(id);
        //设置玩家数据
        player.setCamp(0);
        player.setRoomId(-1);
        //设置房主
        if(ownerId.equals(player.getId()) ){
            ownerId = SwitchOwner();
        }
        //战斗状态退出
        if(status == Status.FIGHT){
            player.getData().setLost(player.getData().getLost() + 1);
            MsgLeaveBattle msg = new MsgLeaveBattle();
            msg.id = player.getId();
            Broadcast(msg);
        }
        //房间为空
        if(playerIds.size() == 0){
            RoomService.RemoveRoom(this.id);
        }
        //广播
        Broadcast(ToMsg());
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
            Player player = PlayerService.GetPlayer(id);
            player.send(msg);
        }
    }

    //生成MsgGetRoomInfo协议
    public MsgBase ToMsg(){
        MsgGetRoomInfo msg = new MsgGetRoomInfo();
        int count = playerIds.size();
        msg.players = new PlayerInfo[count];
        //players
        int i = 0;
        for(String id : playerIds.keySet()){
            Player player = PlayerService.GetPlayer(id);
            PlayerInfo playerInfo = new PlayerInfo();
            //赋值
            playerInfo.id = player.getId();
            playerInfo.camp = player.getCamp();
            playerInfo.win = player.getData().getWin();
            playerInfo.lost = player.getData().getLost();
            playerInfo.isOwner = 0;
            if(isOwner(player)){
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
            Player player = PlayerService.GetPlayer(id);
            if(player.getCamp() == 1){ count1++; }
            else { count2++; }
        }
        //每个队伍至少要有1名玩家
        if (count1 < 1 || count2 < 1){
            return false;
        }
        return true;
    }

    //初始化位置
    private void SetBirthPos(Player player, int index){
        int camp = player.getCamp();

        player.setX( birthConfig[camp-1] [index][0]);
        player.setY( birthConfig[camp-1][ index][1]);
        player.setZ( birthConfig[camp-1][ index][2]);
        player.setEx(birthConfig[camp-1][ index][3]);
        player.setEy(birthConfig[camp-1][ index][4]);
        player.setEz(birthConfig[camp-1][ index][5]);
    }

    //玩家数据转成TankInfo
    public TankInfo PlayerToTankInfo(Player player){
        TankInfo tankInfo = new TankInfo();
        tankInfo.camp = player.getCamp();
        tankInfo.id = player.getId();
        tankInfo.hp = player.getHp();

        tankInfo.x  = player.getX();
        tankInfo.y  = player.getY();
        tankInfo.z  = player.getZ();
        tankInfo.ex = player.getEx();
        tankInfo.ey = player.getEy();
        tankInfo.ez = player.getEz();

        return tankInfo;
    }

    //重置玩家战斗属性
    private void ResetPlayers(){
        //位置和旋转
        int count1 = 0;
        int count2 = 0;
        for(String id : playerIds.keySet()) {
            Player player = PlayerService.GetPlayer(id);
            if(player.getCamp() == 1){
                SetBirthPos(player, count1);
                count1++;
            }
            else {
                SetBirthPos(player, count2);
                count2++;
            }
        }
        //生命值
        for(String id : playerIds.keySet()) {
            Player player = PlayerService.GetPlayer(id);
            player.setHp(100);
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
            Player player = PlayerService.GetPlayer(id);
            msg.tanks[i] = PlayerToTankInfo(player);
            i++;
        }
        Broadcast(msg);
        return true;
    }


    //是否死亡
    public boolean IsDie(Player player){
        return player.getHp()<= 0;
    }


    //定时更新
    public void Update(){
        //状态判断
        if(status != Status.FIGHT){
            return;
        }
        //时间判断
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
            Player player = PlayerService.GetPlayer(id);
            if(player.getCamp() == winCamp){

                player.getData().setWin(player.getData().getWin() + 1);}
            else{ player.getData().setLost(player.getData().getLost() + 1);}
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
            Player player = PlayerService.GetPlayer(id);
            if(!IsDie(player)){
                if(player.getCamp() == 1){count1++;};
                if(player.getCamp() == 2){count2++;};
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
