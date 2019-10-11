package test;



import com.wu.server.Until.LogUntil;
import jdk.nashorn.internal.runtime.Debug;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;


public class testJdbc {
//    public static void main(String[] args) {
//              ResultSet rs=null;
//
//
//        java.sql.Statement stmt = null;
//        // 加载驱动类
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//          Connection  conn = DriverManager.getConnection(
//                    "jdbc:mysql://localhost:3306/game?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=UTC",
//                    "root", "123456");
//
//            System.out.println(conn);
//
//
//        } catch (SQLException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } finally {
//
//        }
//
//
//    }




    /**
     * @param args
     */
    public static void main(String[] args) {
        // System.out.println("This is println message.");

        // 记录debug级别的信息
        LogUntil.logger.debug("This is debug message.");
        // 记录info级别的信息
        LogUntil.logger.info("This is info message.");
        // 记录error级别的信息
        LogUntil.logger.error("This is error message.");


    }

}
