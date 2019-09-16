package com.wu.server.dao;

import com.wu.server.Until.SqlUntil;
import com.wu.server.bean.PlayerData;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerDataDao {
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
}
