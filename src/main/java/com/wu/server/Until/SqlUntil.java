package com.wu.server.Until;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqlUntil {
    public static Connection GetSqlConnect(){
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/game?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT",
                    "root", "123456");
            return conn;
        } catch (SQLException e) {
            e.printStackTrace();
        }
      return conn;
    }
}
