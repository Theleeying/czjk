package manager.dao;

import java.sql.*;
import java.util.*;
import javax.swing.JOptionPane;
import manager.common.BaseDao;

public class CheckGroupDao {
    // 查询所有检查组，支持按组名、编号、价格模糊查询
    public static List<Map<String, Object>> queryAll(String gName, String gNo, String price) {
        StringBuilder sql = new StringBuilder("SELECT * FROM checkgroup WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (gName != null && !gName.isEmpty()) {
            sql.append(" AND g_name LIKE ?");
            params.add("%" + gName + "%");
        }
        if (gNo != null && !gNo.isEmpty()) {
            sql.append(" AND g_no LIKE ?");
            params.add("%" + gNo + "%");
        }
        if (price != null && !price.isEmpty()) {
            sql.append(" AND price LIKE ?");
            params.add("%" + price + "%");
        }
        ResultSet rs = null;
        PreparedStatement st = null;
        Connection conn = null;
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            conn = manager.common.DBConn.getConn();
            st = conn.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                st.setObject(i + 1, params.get(i));
            }
            rs = st.executeQuery();
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("gid", rs.getString("gid"));
                map.put("g_no", rs.getString("g_no"));
                map.put("g_name", rs.getString("g_name"));
                map.put("price", rs.getBigDecimal("price"));
                map.put("create_date", rs.getDate("create_date"));
                list.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeAll(rs, st, conn);
        }
        return list;
    }

    // 新增检查组，返回新插入的gid
    public static String addGroup(String gid, String gNo, String gName, String price) {
        String sql = "INSERT INTO checkgroup(gid, g_no, g_name, price, create_date) VALUES (?, ?, ?, ?, NOW())";
        Object[] params = {gid, gNo, gName, price};
        try {
            int result = BaseDao.executeDML(sql, params);
            if (result > 0) {
                return gid;
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "添加检查组异常：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    // 删除检查组
    public static int deleteGroup(String gid) {
        return BaseDao.executeDML("DELETE FROM checkgroup WHERE gid=?", new Object[]{gid});
    }

    // 修改检查组
    public static int updateGroup(String gid, String gNo, String gName, String price) {
        return BaseDao.executeDML("UPDATE checkgroup SET g_no=?, g_name=?, price=? WHERE gid=?", new Object[]{gNo, gName, price, gid});
    }

    // 检查g_no或g_name是否已存在
    // 新增检查组与检查项的关联（使用 check_group 表）
    public static void addGroupItems(String gid, List<Integer> cids) {
        String sql = "INSERT INTO check_group(cgid, gid, cid) VALUES (?, ?, ?)";
        for (Integer cid : cids) {
            String cgid = java.util.UUID.randomUUID().toString();
            Object[] params = {cgid, gid, cid};
            BaseDao.executeDML(sql, params);
        }
    }

    // 删除某组的所有检查项关联（使用 check_group 表）
    public static void deleteGroupItems(String gid) {
        String sql = "DELETE FROM check_group WHERE gid = ?";
        BaseDao.executeDML(sql, new Object[]{gid});
    }

    // 查询某组下所有检查项ID（使用 check_group 表）
    public static List<Integer> getGroupItems(String gid) {
        String sql = "SELECT cid FROM check_group WHERE gid = ?";
        ResultSet rs = null;
        PreparedStatement st = null;
        Connection conn = null;
        List<Integer> cids = new ArrayList<>();
        try {
            conn = manager.common.DBConn.getConn();
            st = conn.prepareStatement(sql);
            st.setObject(1, gid);
            rs = st.executeQuery();
            while (rs.next()) {
                cids.add(rs.getInt("cid"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeAll(rs, st, conn);
        }
        return cids;
    }

    // 查询某组下所有CheckItem对象（使用 check_group 表）
    public static List<manager.pojo.CheckItem> getCheckItemsByGroup(String gid) {
        String sql = "SELECT c.* FROM checkitem c JOIN check_group cg ON c.cid = cg.cid WHERE cg.gid = ?";
        ResultSet rs = null;
        PreparedStatement st = null;
        Connection conn = null;
        List<manager.pojo.CheckItem> list = new ArrayList<>();
        try {
            conn = manager.common.DBConn.getConn();
            st = conn.prepareStatement(sql);
            st.setObject(1, gid);
            rs = st.executeQuery();
            while (rs.next()) {
                manager.pojo.CheckItem checkItem = new manager.pojo.CheckItem(
                        rs.getInt("cid"),
                        rs.getString("c_no"),
                        rs.getString("c_name"),
                        rs.getBigDecimal("price") == null ? null : rs.getBigDecimal("price").toPlainString(),
                        rs.getString("unit"),
                        rs.getString("remark")
                );
                list.add(checkItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeAll(rs, st, conn);
        }
        return list;
    }

    // 关联查询三种机制
    // 1. 按组查项
    public static List<Map<String, Object>> queryItemsByGroup(String gid) {
        String sql = "SELECT c.* FROM checkitem c JOIN check_group cg ON c.cid = cg.cid WHERE cg.gid = ?";
        ResultSet rs = null;
        PreparedStatement st = null;
        Connection conn = null;
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            conn = manager.common.DBConn.getConn();
            st = conn.prepareStatement(sql);
            st.setObject(1, gid);
            rs = st.executeQuery();
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("cid", rs.getInt("cid"));
                map.put("ccode", rs.getString("ccode"));
                map.put("cname", rs.getString("cname"));
                map.put("refer_val", rs.getString("refer_val"));
                map.put("unit", rs.getString("unit"));
                list.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeAll(rs, st, conn);
        }
        return list;
    }

    // 2. 按项查组
    public static List<Map<String, Object>> queryGroupsByItem(int cid) {
        String sql = "SELECT g.* FROM checkgroup g JOIN check_group cg ON g.gid = cg.gid WHERE cg.cid = ?";
        ResultSet rs = null;
        PreparedStatement st = null;
        Connection conn = null;
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            conn = manager.common.DBConn.getConn();
            st = conn.prepareStatement(sql);
            st.setObject(1, cid);
            rs = st.executeQuery();
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("gid", rs.getString("gid"));
                map.put("g_no", rs.getString("g_no"));
                map.put("g_name", rs.getString("g_name"));
                map.put("price", rs.getBigDecimal("price"));
                map.put("create_date", rs.getDate("create_date"));
                list.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeAll(rs, st, conn);
        }
        return list;
    }

    // 3. 搜索查询（组名/项名模糊查）
    public static List<Map<String, Object>> searchGroupsAndItems(String keyword) {
        String sql = "SELECT g.*, c.* FROM checkgroup g " +
                "JOIN check_group cg ON g.gid = cg.gid " +
                "JOIN checkitem c ON cg.cid = c.cid " +
                "WHERE g.g_name LIKE ? OR c.c_name LIKE ?";
        ResultSet rs = null;
        PreparedStatement st = null;
        Connection conn = null;
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            conn = manager.common.DBConn.getConn();
            st = conn.prepareStatement(sql);
            st.setObject(1, "%" + keyword + "%");
            st.setObject(2, "%" + keyword + "%");
            rs = st.executeQuery();
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("gid", rs.getString("gid"));
                map.put("g_name", rs.getString("g_name"));
                map.put("g_no", rs.getString("g_no"));
                java.math.BigDecimal groupPrice = rs.getBigDecimal("price");
                map.put("price", groupPrice == null ? null : groupPrice.toPlainString());
                map.put("create_date", rs.getDate("create_date"));
                map.put("cid", rs.getInt("cid"));
                map.put("c_name", rs.getString("c_name"));
                map.put("c_no", rs.getString("c_no"));
                java.math.BigDecimal itemPrice = rs.getBigDecimal("price");
                map.put("c_price", itemPrice == null ? null : itemPrice.toPlainString());
                map.put("unit", rs.getString("unit"));
                map.put("remark", rs.getString("remark"));
                list.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeAll(rs, st, conn);
        }
        return list;
    }

    public static boolean existsGroupNoOrName(String gNo, String gName) {
        String sql = "SELECT g_no, g_name FROM checkgroup WHERE g_no = ? OR g_name = ?";
        ResultSet rs = null;
        PreparedStatement st = null;
        Connection conn = null;
        try {
            conn = manager.common.DBConn.getConn();
            st = conn.prepareStatement(sql);
            st.setObject(1, gNo);
            st.setObject(2, gName);
            rs = st.executeQuery();
            while (rs.next()) {
                String existingNo = rs.getString("g_no");
                String existingName = rs.getString("g_name");
                if (gNo.equals(existingNo)) {
                    JOptionPane.showMessageDialog(null, "编号 " + gNo + " 已存在！", "提示", JOptionPane.WARNING_MESSAGE);
                    return true;
                    // 弹窗后立即关闭资源并返回，防止界面卡死
                }
                if (gName.equals(existingName)) {
                    JOptionPane.showMessageDialog(null, "组名 " + gName + " 已存在！", "提示", JOptionPane.WARNING_MESSAGE);
                    return true;
                    // 弹窗后立即关闭资源并返回，防止界面卡死
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeAll(rs, st, conn);
        }
        return false;
    }
}
