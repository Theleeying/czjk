package manager.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import manager.common.BaseDao;
import manager.pojo.CheckItem;
import manager.util.CommonUtil;

public class CheckItemDao {
    //表头的列名 数组
    public static final Object[] columnNames = {"ID", "编号", "名称", "参考值", "单位", "创建时间", "更新时间", "删除时间", "创建人", "状态"};

    //全表查询、根据名称模糊查询
    public static Object[][] querryAllCheckItem(String cname, String ccode) {

        List<CheckItem> list = new ArrayList<>();//结果列表
        StringBuilder sql = new StringBuilder();
        sql.append("select * from checkitem ");

        List<Object> params = new ArrayList<>();
        //如果ccode不为空就是模糊查询 否则就是全表查询
        boolean hasCname = cname != null && !cname.isEmpty();//检查名称条件
        boolean hasCcode = ccode != null && !ccode.isEmpty();//检查编号条件
        //添加条件
        if (hasCcode || hasCname) {
            sql.append("where ");
            if (hasCname) {
                sql.append("cname like ? ");
                params.add("%" + cname + "%");
            }
            if (hasCcode) {
                if (hasCname) {
                    sql.append("and ");
                }
                sql.append("ccode like ? ");
                params.add("%" + ccode + "%");
            }
        }
        Object[] obj = params.toArray();//参数转化为数组
        //执行查询
        ResultSet rs = BaseDao.executeDQL(sql.toString(), obj);
        try {
            while (rs.next()) {
                CheckItem checkItem = new CheckItem(
                        rs.getInt("cid"),
                        rs.getString("ccode"),
                        rs.getString("cname"),
                        rs.getString("refer_val"),
                        rs.getString("unit"),
                        rs.getDate("create_date"),
                        rs.getDate("upd_date"),
                        rs.getDate("delete_date"),
                        rs.getString("option_user"),
                        rs.getString("status"));
                list.add(checkItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return CommonUtil.toArray(list);//转换为二维数组
    }

    // 添加检查项功能
    public static int addCheckItem(CheckItem checkItem) {
        //sql语句
        String sql = "INSERT INTO checkitem(ccode,cname,refer_val,unit,create_date,option_user,status) VALUES(?,?,?,?,now(),?,1)";
        Object[] params = {//设置参数
                checkItem.getCcode(),
                checkItem.getCname(),
                checkItem.getReferVal(),
                checkItem.getUnit(),
                checkItem.getOptionUser()
        };
        return BaseDao.executeDML(sql, params);//执行DML语句更新操作
    }

    //根据编号查询用户信息
    public static CheckItem queryCheckItemById(String ccode) {
        String sql = "SELECT * FROM checkitem WHERE ccode = ?";
        Object[] params = {ccode};
        ResultSet rs = BaseDao.executeDQL(sql, params);
        CheckItem checkItem = null;
        try {
            while (rs.next()) {
                checkItem = new CheckItem(
                        rs.getInt("cid"),
                        rs.getString("ccode"),
                        rs.getString("cname"),
                        rs.getString("refer_val"),
                        rs.getString("unit"),
                        rs.getDate("create_date"),
                        rs.getDate("upd_date"),
                        rs.getDate("delete_date"),
                        rs.getString("option_user"),
                        rs.getString("status"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return checkItem;
    }

    //根据主键id查询
    public static CheckItem queryCheckItemById(Integer cid) {
        String sql = "SELECT * FROM checkitem WHERE cid = ?";
        Object[] params = {cid};
        ResultSet rs = BaseDao.executeDQL(sql, params);
        CheckItem checkItem = null;
        try {
            while (rs.next()) {
                checkItem = new CheckItem(
                        rs.getInt("cid"),
                        rs.getString("ccode"),
                        rs.getString("cname"),
                        rs.getString("refer_val"),
                        rs.getString("unit"),
                        rs.getDate("create_date"),
                        rs.getDate("upd_date"),
                        rs.getDate("delete_date"),
                        rs.getString("option_user"),
                        rs.getString("status"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return checkItem;
    }

    //修改检查项
    public static int updateCheckItem(CheckItem checkItem) {
        String sql = "UPDATE checkitem SET ccode = ?,cname = ?,refer_val = ?,unit = ?,upd_date = now() WHERE cid = ?";
        Object[] params = {//设置参数
                checkItem.getCcode(),
                checkItem.getCname(),
                checkItem.getReferVal(),
                checkItem.getUnit(),
                checkItem.getCid()
        };
        return BaseDao.executeDML(sql, params);//执行DML语句更新操作
    }

    //删除检查项
    public static int deleteCheckItem(Integer cid) {
        String sql = "DELETE FROM checkitem WHERE cid = ?";
        Object[] params = {cid};//设置参数
        return BaseDao.executeDML(sql, params);//执行DML语句更新操作 删除
    }

    // 查询所有检查项，返回对象列表
    public static List<CheckItem> listAllCheckItems() {
        List<CheckItem> list = new ArrayList<>();
        String sql = "select * from checkitem";
        ResultSet rs = BaseDao.executeDQL(sql, new Object[]{});
        try {
            while (rs.next()) {
                CheckItem checkItem = new CheckItem(
                        rs.getInt("cid"),
                        rs.getString("ccode"),
                        rs.getString("cname"),
                        rs.getString("refer_val"),
                        rs.getString("unit"),
                        rs.getDate("create_date"),
                        rs.getDate("upd_date"),
                        rs.getDate("delete_date"),
                        rs.getString("option_user"),
                        rs.getString("status"));
                list.add(checkItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

}
