package com.wu.server.dao;

import com.wu.server.Until.SqlUntil;
import com.wu.server.bean.PlayerData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * playerdate表调用基类
 */
public class PlayerDataDao {
    /**
     * 获取玩家数据
     * @param id
     * @return
     */
    public static PlayerData GetPlayerData(String id){
        ResultSet rs=null;
        Connection conn=null;
        java.sql.Statement stmt = null;

        try {
            //链接
            conn = SqlUntil.GetSqlConnect();

            if (conn != null) {
                stmt = conn.createStatement();
                String sql;
                sql = "select * FROM playerdata where iduser='"+id+"'";
                rs = stmt.executeQuery(sql);
                if(rs != null && rs.next()){
                    PlayerData playerData = new PlayerData();
                    playerData.setCoin(rs.getInt("coin"));
                    playerData.setText(rs.getString("text"));
                    playerData.setWin(rs.getInt("win"));
                    playerData.setLost(rs.getInt("lost"));
                    return playerData;
                }


            }


        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {

            try {
                rs.close();
                stmt.close();
                conn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        return null;
    }

    public static boolean CreatePlayer(String iduser) {
        Connection conn=null;

        try {

            conn = SqlUntil.GetSqlConnect();
            String sql="insert into playerdata(iduser) values (?)";
            if (conn != null) {
                PreparedStatement preStmt=conn.prepareStatement(sql);
                preStmt.setString(1, iduser);
                //preStmt.setString(2, password);
                int count =preStmt.executeUpdate();
                preStmt.close();
                if(count == 1)
                    return  true;
                else
                    return false;
            }


        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } finally {

            try {

                conn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        return false;
    }
}
