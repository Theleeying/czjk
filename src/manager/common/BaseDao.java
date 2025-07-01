package manager.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BaseDao {
    //更新操作(增删改)
    public static int executeDML(String sql, Object... params) {
        Connection conn = DBConn.getConn();//获得连接
        PreparedStatement st = null;//预编译语句对象
        int r = 0;//影响行数
        try {
            st = conn.prepareStatement(sql);//创建预编译语句对象
            //?设置值
            for (int i = 0; i < params.length; i++) {
                st.setObject(i + 1, params[i]);
            }
            r = st.executeUpdate();//执行更新 返回影响行数
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (st != null) st.close();//关闭连接
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return r;
    }

    //查询   全表、条件查询
    public static ResultSet executeDQL(String sql, Object[] params) {
        Connection conn = DBConn.getConn();//获得连接
        PreparedStatement st = null;//预编译语句对象
        ResultSet rs = null;//查询结果集对象
        try {
            st = conn.prepareStatement(sql);//创建预编译语句对象
            if (params != null) {
                //?设置值
                for (int i = 0; i < params.length; i++) {
                    st.setObject(i + 1, params[i]);
                }
            }
            rs = st.executeQuery();//执行查询语句，返回结果集对象
            // 这里不能关闭st和conn，否则rs失效。调用者用完rs后必须关闭rs、st、conn。
            // 建议后续重构为不直接返回ResultSet。
            rs.getStatement().getConnection(); // 强制不被GC
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    // 新增工具方法，关闭ResultSet、Statement、Connection
    public static void closeAll(ResultSet rs, PreparedStatement st, Connection conn) {
        try {
            if (rs != null) rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (st != null) st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
