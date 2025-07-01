package manager.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import manager.common.BaseDao;
import manager.pojo.CheckResult;

public class CheckResultDao {
    // 查询某次预约的所有体检结果
    public static List<CheckResult> listByOrderId(int orderId) {
        String sql = "SELECT * FROM check_result WHERE order_id = ?";
        ResultSet rs = BaseDao.executeDQL(sql, new Object[]{orderId});
        List<CheckResult> list = new ArrayList<>();
        try {
            while (rs.next()) {
                CheckResult r = new CheckResult();
                r.setResultId(rs.getInt("result_id"));
                r.setOrderId(rs.getInt("order_id"));
                r.setUserId(rs.getInt("user_id"));
                r.setGid(rs.getString("gid"));
                r.setCid(rs.getInt("cid"));
                r.setValue(rs.getString("value"));
                r.setResultDate(rs.getTimestamp("result_date"));
                r.setInputUser(rs.getString("input_user"));
                list.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 保存或更新体检结果（管理员录入）
    public static void saveOrUpdateResults(List<CheckResult> results) {
        for (CheckResult r : results) {
            // 先查有无此条
            String checkSql = "SELECT result_id FROM check_result WHERE order_id=? AND cid=?";
            ResultSet rs = BaseDao.executeDQL(checkSql, new Object[]{r.getOrderId(), r.getCid()});
            try {
                if (rs.next()) {
                    // update
                    String sql = "UPDATE check_result SET value=?, result_date=NOW(), input_user=? WHERE result_id=?";
                    BaseDao.executeDML(sql, new Object[]{r.getValue(), r.getInputUser(), rs.getInt("result_id")});
                } else {
                    // insert
                    String sql = "INSERT INTO check_result(order_id, user_id, gid, cid, value, result_date, input_user) VALUES (?, ?, ?, ?, ?, NOW(), ?)";
                    BaseDao.executeDML(sql, new Object[]{r.getOrderId(), r.getUserId(), r.getGid(), r.getCid(), r.getValue(), r.getInputUser()});
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
