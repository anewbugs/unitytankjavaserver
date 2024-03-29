package com.wu.server.dao;

import com.wu.server.Until.LogUntil;
import com.wu.server.Until.SqlUntil;
import com.wu.server.bean.PlayerData;
import com.wu.server.bean.User;

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
                sql = "select * FROM playerdata where binary iduser='"+id+"'";
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
            LogUntil.logger.error(e.toString());
        } finally {

            try {
                rs.close();
                stmt.close();
                conn.close();
            } catch (SQLException e) {
              LogUntil.logger.error(e.toString());
            }

        }
        return null;
    }

    /**
     * 创建玩家数据
     * @param iduser
     * @return
     */
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
            LogUntil.logger.error(e.toString());
            return false;
        } finally {

            try {

                conn.close();
            } catch (SQLException e) {
                LogUntil.logger.error(e.toString());
            }

        }
        return false;
    }

    /**
     * 更新玩家数据
     * @param user
     * @return
     */
    public static void UpdatePlayerData(User user) {
        Connection conn=null;

        try {

            conn = SqlUntil.GetSqlConnect();
            String sql="update playerdata set coin = ?,text = ?,win = ?,lost= ? where iduser = ?;";
            if (conn != null) {
                PreparedStatement preStmt=conn.prepareStatement(sql);
                preStmt.setInt(1,user.playerData.getCoin());
                preStmt.setString(2,user.playerData.getText());
                preStmt.setInt(3,user.playerData.getWin());
                preStmt.setInt(4,user.playerData.getLost());
                preStmt.setString(5,user.getId());
                System.out.println(preStmt.toString());
                //preStmt.setString(2, password);
                int count =preStmt.executeUpdate();
                preStmt.close();
                if(count == 1){
                    return;
                }
                else{
                    LogUntil.logger.error("Failed execute :" +sql);
                    return;
                }

            }


        } catch (SQLException e) {
            LogUntil.logger.error("Failed :" + e.toString());
            return;
        } finally {

            try {

                conn.close();
            } catch (SQLException e) {
                LogUntil.logger.error(e.toString());
            }

        }
    }
}
