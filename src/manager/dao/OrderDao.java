package manager.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import manager.common.BaseDao;
import manager.pojo.Order;

public class OrderDao {
    // 新增预约
    public static int addOrder(Order order) {
        String sql = "INSERT INTO `order` (user_id, gid, name, sex, telephone, order_date, type, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Object[] params = {
                order.getUserId(), order.getGid(), order.getName(), order.getSex(),
                order.getTelephone(), order.getOrderDate(), order.getType(), order.getStatus()
        };
        return BaseDao.executeDML(sql, params);
    }

    // 查询当前用户所有预约
    public static List<Order> listOrdersByUser(int userId) {
        String sql = "SELECT * FROM `order` WHERE user_id = ? ORDER BY order_date DESC";
        ResultSet rs = BaseDao.executeDQL(sql, new Object[]{userId});
        List<Order> list = new ArrayList<>();
        try {
            while (rs.next()) {
                Order o = new Order();
                o.setId(rs.getInt("id"));
                o.setUserId(rs.getInt("user_id"));
                o.setGid(rs.getString("gid"));
                o.setName(rs.getString("name"));
                o.setSex(rs.getString("sex"));
                o.setTelephone(rs.getString("telephone"));
                o.setOrderDate(rs.getDate("order_date"));
                o.setType(rs.getString("type"));
                o.setStatus(rs.getString("status"));
                list.add(o);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 查询所有预约（管理员用）
    public static List<Order> listAllOrders() {
        String sql = "SELECT * FROM `order` ORDER BY order_date DESC";
        ResultSet rs = BaseDao.executeDQL(sql, new Object[]{});
        List<Order> list = new ArrayList<>();
        try {
            while (rs.next()) {
                Order o = new Order();
                o.setId(rs.getInt("id"));
                o.setUserId(rs.getInt("user_id"));
                o.setGid(rs.getString("gid"));
                o.setName(rs.getString("name"));
                o.setSex(rs.getString("sex"));
                o.setTelephone(rs.getString("telephone"));
                o.setOrderDate(rs.getDate("order_date"));
                o.setType(rs.getString("type"));
                o.setStatus(rs.getString("status"));
                list.add(o);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 更新预约状态（如到诊）
    public static int updateOrderStatus(int id, String status) {
        String sql = "UPDATE `order` SET status = ? WHERE id = ?";
        return BaseDao.executeDML(sql, new Object[]{status, id});
    }

    // 删除预约
    public static int deleteOrder(int id) {
        String sql = "DELETE FROM `order` WHERE id = ?";
        return BaseDao.executeDML(sql, new Object[]{id});
    }
}
