package manager.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConn {
    private static String url = "jdbc:mysql://localhost:3306/project_db?serverTimezone=UTC";
    private static String username = "root";
    private static String pass = "mysql-secret-password";

    //  静态代码块：类加载时执行。用于注册JDBC驱动
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConn() {
        Connection conn = null;
        try {
            conn = java.sql.DriverManager.getConnection(url, username, pass);
            System.out.println("数据库连接成功");
        } catch (Exception e) {
            System.out.println("数据库连接失败");
            e.printStackTrace();
        }
        return conn;
    }

    public static void main(String[] args) {
        getConn();
    }

}
