package com.wu.server.dao;

import com.wu.server.Until.SqlUntil;

import java.sql.*;

public class UserDao
{
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
        return false;
    }

    public static void InsertToSign(String iduser, String password, String email, String tele) {
        Connection conn=null;

        try {

            conn = SqlUntil.GetSqlConnect();
            String sql="insert into user(iduser,password) values (?,?)";
            if (conn != null) {
                PreparedStatement preStmt=conn.prepareStatement(sql);
                preStmt.setString(1, iduser);
                preStmt.setString(2, password);
                preStmt.executeUpdate();
                preStmt.close();
            }


        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {

            try {

                conn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }
}
