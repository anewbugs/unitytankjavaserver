package com.wu.server.service;

import com.wu.server.bean.Player;
import com.wu.server.dao.UserDao;

import java.util.HashMap;

 class PlayerService {
    private static HashMap<String, Player> players = new HashMap<String, Player>();
    /**
     * 确认用户
     * @param id 账号
     * @param pw 密码
     * @return
     */
    public static boolean CheckedPlayer(String id,String pw){
        return UserDao.selectToLogin(id, pw);
    }
    /**
     * 判断玩家是否在线
     * @param id
     * @return
     */
    public static boolean IsOnline(String id){
        return players.containsKey(id);
    }
    /**
     * 获取玩家
     * @param id
     * @return
     */
    public static Player GetPlayer(String id) {
        if(players.containsKey(id)){
            return players.get(id);
        }
        return null;
    }

    /**
     * 添加玩家
     * @param id
     * @param player
     */
    public static void  AddPlayer(String id , Player player){
        players.put(id,player);
    }

    /**
     * 删除玩家
     * @param id
     */
    public static void RemovePlayer(String id){
        players.remove(id);
    }
}
