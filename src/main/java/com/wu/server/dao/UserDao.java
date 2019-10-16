package com.wu.server.dao;

import com.wu.server.Until.LogUntil;
import com.wu.server.Until.SqlUntil;

import java.sql.*;

/**
 * user表调用类
 */
public class UserDao {
    /**
     * 查询账号
     * @param iduser
     * @return
     */
    public static boolean SelectById(String iduser) {
        ResultSet rs=null;
        Connection conn=null;
        java.sql.Statement stmt = null;

        try {
            //链接
            conn = SqlUntil.GetSqlConnect();

            if (conn != null) {
                stmt = conn.createStatement();
                String sql;
                sql = "select * FROM user where iduser='"+iduser+"'";
                rs = stmt.executeQuery(sql);
                return rs.next();

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
        return false;
    }

    /**
     * 查询数据库，确认用户正确
     * @param iduser
     * @param password
     * @return
     */
    public static boolean selectToLogin(String iduser, String password) {
        ResultSet rs=null;
        Connection conn=null;
        java.sql.Statement stmt = null;

        try {
            //链接
            conn = SqlUntil.GetSqlConnect();

            if (conn != null) {
                stmt = conn.createStatement();
                String sql;
                sql = "select * FROM user where iduser='"+iduser+"' and password ='"+password+"'";
                rs = stmt.executeQuery(sql);
                return rs.next();

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
        return false;
    }

    /**
     * 注册账号
     * @param iduser
     * @param password
     * @return
     */
    public static boolean Register(String iduser, String password) {
        Connection conn=null;

        try {

            conn = SqlUntil.GetSqlConnect();
            String sql="insert into user(iduser,password) values (?,?);";
            if (conn != null) {
                PreparedStatement preStmt=conn.prepareStatement(sql);
                preStmt.setString(1, iduser);
                preStmt.setString(2, password);
                int count =preStmt.executeUpdate();
                preStmt.close();
//                System.out.println(count);
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
}
