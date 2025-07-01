package manager.dao;

import manager.common.BaseDao;
import manager.pojo.Users;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

// 用户登录的验证业务逻辑
public class UserDao {
    //根据用户名和角色查询用户信息  username roleId(1管2普) return 用户对象users/null
    public Users getUserByName(String username, int roleId) {
        Users users = null;
        //构建sql
        StringBuilder sql = new StringBuilder();
        sql.append("select t1.* ");
        sql.append("from users t1,user_role t2 ");
        sql.append("where t1.user_id = t2.user_id and t1.username = ? and t2.role_id = ? ");
        Object[] obj = {username, roleId};
        ResultSet rs = BaseDao.executeDQL(sql.toString(), obj);
        try {
            while (rs.next()) {
                users = new Users(rs.getInt("user_id"), rs.getString("username"),
                        rs.getString("password"), rs.getString("uname"), rs.getString("tel"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    // 修改密码
    public static int updatePassword(String username, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE username = ?";
        Object[] params = {newPassword, username};
        return BaseDao.executeDML(sql, params);
    }

    // 查询所有用户（可按用户名模糊查询）
    public static Object[][] queryAllUsers(String uname) {
        StringBuilder sql = new StringBuilder("SELECT * FROM users");
        List<Object> params = new java.util.ArrayList<>();
        if (uname != null && !uname.isEmpty()) {
            sql.append(" WHERE uname LIKE ?");
            params.add("%" + uname + "%");
        }
        ResultSet rs = BaseDao.executeDQL(sql.toString(), params.toArray());
        java.util.List<Users> list = new java.util.ArrayList<>();
        try {
            while (rs.next()) {
                Users user = new Users(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("uname"),
                        rs.getString("tel")
                );
                list.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // 转为二维数组
        Object[][] arr = new Object[list.size()][5];
        for (int i = 0; i < list.size(); i++) {
            Users u = list.get(i);
            arr[i][0] = u.getUserId();
            arr[i][1] = u.getUsername();
            arr[i][2] = u.getUname();
            arr[i][3] = u.getTel();
            arr[i][4] = u.getPassword();
        }
        return arr;
    }

    // 查询所有用户（可按用户名模糊查询），isAdmin为true时只查管理员
    public static Object[][] queryAllUsers(String uname, boolean isAdmin) {
        StringBuilder sql = new StringBuilder("SELECT u.* FROM users u");
        List<Object> params = new java.util.ArrayList<>();
        if (isAdmin) {
            sql.append(", user_role ur WHERE u.user_id = ur.user_id AND ur.role_id = 1");
            if (uname != null && !uname.isEmpty()) {
                sql.append(" AND u.uname LIKE ?");
                params.add("%" + uname + "%");
            }
        } else {
            if (uname != null && !uname.isEmpty()) {
                sql.append(" WHERE uname LIKE ?");
                params.add("%" + uname + "%");
            }
        }
        ResultSet rs = BaseDao.executeDQL(sql.toString(), params.toArray());
        java.util.List<Users> list = new java.util.ArrayList<>();
        try {
            while (rs.next()) {
                Users user = new Users(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("uname"),
                        rs.getString("tel")
                );
                list.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // 转为二维数组
        Object[][] arr = new Object[list.size()][5];
        for (int i = 0; i < list.size(); i++) {
            Users u = list.get(i);
            arr[i][0] = u.getUserId();
            arr[i][1] = u.getUsername();
            arr[i][2] = u.getUname();
            arr[i][3] = u.getTel();
            arr[i][4] = u.getPassword();
        }
        return arr;
    }

    // 添加用户，isAdmin为true时添加管理员
    public static int addUser(Users user, boolean isAdmin) {
        String sql = "INSERT INTO users(username, password, uname, tel) VALUES (?, ?, ?, ?)";
        Object[] params = {user.getUsername(), user.getPassword(), user.getUname(), user.getTel()};
        int result = BaseDao.executeDML(sql, params);
        if (result > 0) {
            // 新增角色
            String getIdSql = "SELECT user_id FROM users WHERE username = ? ORDER BY user_id DESC LIMIT 1";
            ResultSet rs = BaseDao.executeDQL(getIdSql, new Object[]{user.getUsername()});
            int userId = -1;
            try {
                if (rs.next()) userId = rs.getInt("user_id");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (userId > 0) {
                int roleId = isAdmin ? 1 : 2;
                String addRoleSql = "INSERT INTO user_role(user_id, role_id) VALUES (?, ?)";
                BaseDao.executeDML(addRoleSql, new Object[]{userId, roleId});
            }
        }
        return result;
    }

    // 修改用户
    public static int updateUser(Users user) {
        String sql = "UPDATE users SET username=?, password=?, uname=?, tel=? WHERE user_id=?";
        Object[] params = {user.getUsername(), user.getPassword(), user.getUname(), user.getTel(), user.getUserId()};
        return BaseDao.executeDML(sql, params);
    }

    // 删除用户
    public static int deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE user_id=?";
        Object[] params = {userId};
        return BaseDao.executeDML(sql, params);
    }

    // 用户表头
    public static final Object[] columnNames = {"ID", "账号", "姓名", "电话", "密码"};
}
