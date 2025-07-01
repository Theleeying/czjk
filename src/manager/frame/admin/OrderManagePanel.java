package manager.frame.admin;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import manager.dao.OrderDao;
import manager.pojo.Order;

public class OrderManagePanel extends JInternalFrame {
    private JTable orderTable;
    private JButton refreshBtn;
    private JButton cancelBtn;
    private JButton arriveBtn;
    private JButton inputResultBtn;

    public OrderManagePanel() {
        super("预约管理", true, true, true, true);
        setSize(800, 500);
        setVisible(true);
        setLayout(new BorderLayout());
        // 按钮区
        JPanel topPanel = new JPanel();
        refreshBtn = new JButton("刷新");
        cancelBtn = new JButton("取消预约");
        arriveBtn = new JButton("到诊登记");
        inputResultBtn = new JButton("录入体检结果");
        topPanel.add(refreshBtn);
        topPanel.add(cancelBtn);
        topPanel.add(arriveBtn);
        topPanel.add(inputResultBtn);
        // 新增：查看体检结果按钮
        JButton viewResultBtn = new JButton("查看体检结果");
        topPanel.add(viewResultBtn);
        add(topPanel, BorderLayout.NORTH);
        // 表格区
        orderTable = new JTable();
        JScrollPane scroll = new JScrollPane(orderTable);
        add(scroll, BorderLayout.CENTER);
        // 事件
        refreshBtn.addActionListener(e -> refreshTable());
        cancelBtn.addActionListener(e -> cancelOrder());
        arriveBtn.addActionListener(e -> markArrived());
        inputResultBtn.addActionListener(e -> inputResult());
        viewResultBtn.addActionListener(e -> viewResult());
        refreshTable();
    }

    private void refreshTable() {
        List<Order> orders = OrderDao.listAllOrders();
        String[] colNames = {"预约ID", "用户ID", "组ID", "姓名", "性别", "电话", "日期", "方式", "状态"};
        Object[][] data = new Object[orders.size()][9];
        for (int i = 0; i < orders.size(); i++) {
            Order o = orders.get(i);
            data[i][0] = o.getId();
            data[i][1] = o.getUserId();
            data[i][2] = o.getGid();
            data[i][3] = o.getName();
            data[i][4] = o.getSex();
            data[i][5] = o.getTelephone();
            data[i][6] = o.getOrderDate();
            data[i][7] = "0".equals(o.getType()) ? "微信" : "电脑";
            data[i][8] = "0".equals(o.getStatus()) ? "未到诊" : "到诊";
        }
        orderTable.setModel(new javax.swing.table.DefaultTableModel(data, colNames));
    }

    private void cancelOrder() {
        int row = orderTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "请选择要取消的预约！");
            return;
        }
        int id = (int) orderTable.getValueAt(row, 0);
        int r = JOptionPane.showConfirmDialog(this, "确定取消该预约？", "提示", JOptionPane.YES_NO_OPTION);
        if (r == JOptionPane.YES_OPTION) {
            int result = OrderDao.deleteOrder(id);
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "取消成功！");
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "取消失败！");
            }
        }
    }

    private void markArrived() {
        int row = orderTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "请选择要登记到诊的预约！");
            return;
        }
        int id = (int) orderTable.getValueAt(row, 0);
        int result = OrderDao.updateOrderStatus(id, "1");
        if (result > 0) {
            JOptionPane.showMessageDialog(this, "登记成功！");
            refreshTable();
        } else {
            JOptionPane.showMessageDialog(this, "登记失败！");
        }
    }

    private void inputResult() {
        int row = orderTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "请选择要录入结果的预约！");
            return;
        }
        int orderId = (int) orderTable.getValueAt(row, 0);
        int userId = (int) orderTable.getValueAt(row, 1);
        String gid = (String) orderTable.getValueAt(row, 2);
        // 查询该组下所有项目
        java.util.List<java.util.Map<String, Object>> items = manager.dao.CheckGroupDao.queryItemsByGroup(gid);
        java.util.List<manager.pojo.CheckResult> results = manager.dao.CheckResultDao.listByOrderId(orderId);
        java.util.Map<Integer, manager.pojo.CheckResult> resultMap = new java.util.HashMap<>();
        for (manager.pojo.CheckResult r : results) {
            resultMap.put(r.getCid(), r);
        }
        JPanel panel = new JPanel(new GridLayout(items.size(), 2));
        java.util.List<JTextField> fields = new java.util.ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            java.util.Map<String, Object> m = items.get(i);
            int cid = (int) m.get("cid");
            String label = m.get("cname") + "(" + m.get("ccode") + ")";
            panel.add(new JLabel(label));
            JTextField field = new JTextField(10);
            if (resultMap.containsKey(cid)) {
                field.setText(resultMap.get(cid).getValue());
            }
            fields.add(field);
            panel.add(field);
        }
        int r = JOptionPane.showConfirmDialog(this, panel, "录入体检结果", JOptionPane.OK_CANCEL_OPTION);
        if (r == JOptionPane.OK_OPTION) {
            java.util.List<manager.pojo.CheckResult> saveList = new java.util.ArrayList<>();
            for (int i = 0; i < items.size(); i++) {
                java.util.Map<String, Object> m = items.get(i);
                int cid = (int) m.get("cid");
                String value = fields.get(i).getText().trim();
                manager.pojo.CheckResult cr = new manager.pojo.CheckResult();
                cr.setOrderId(orderId);
                cr.setUserId(userId);
                cr.setGid(gid);
                cr.setCid(cid);
                cr.setValue(value);
                cr.setInputUser("admin"); // 可替换为当前管理员
                saveList.add(cr);
            }
            manager.dao.CheckResultDao.saveOrUpdateResults(saveList);
            JOptionPane.showMessageDialog(this, "保存成功！");
            // 新增：录入后自动弹窗显示结果
            showResultDetail(orderId, gid);
        }
    }

    // 新增：录入后自动弹窗显示结果
    private void showResultDetail(int orderId, String gid) {
        java.util.List<java.util.Map<String, Object>> items = manager.dao.CheckGroupDao.queryItemsByGroup(gid);
        java.util.List<manager.pojo.CheckResult> results = manager.dao.CheckResultDao.listByOrderId(orderId);
        java.util.Map<Integer, String> valueMap = new java.util.HashMap<>();
        for (manager.pojo.CheckResult r : results) {
            valueMap.put(r.getCid(), r.getValue());
        }
        String[] colNames = {"项目编号", "项目名称", "参考值", "测量值", "单位"};
        Object[][] data = new Object[items.size()][5];
        for (int i = 0; i < items.size(); i++) {
            java.util.Map<String, Object> m = items.get(i);
            int cid = (int) m.get("cid");
            data[i][0] = m.get("ccode");
            data[i][1] = m.get("cname");
            data[i][2] = m.get("refer_val");
            data[i][3] = valueMap.getOrDefault(cid, "待录入");
            data[i][4] = m.get("unit");
        }
        JTable table = new JTable(data, colNames);
        table.setEnabled(false);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(500, 250));
        JOptionPane.showMessageDialog(this, scroll, "体检结果明细", JOptionPane.INFORMATION_MESSAGE);
    }

    private void viewResult() {
        int row = orderTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "请选择要查看的预约！");
            return;
        }
        int orderId = (int) orderTable.getValueAt(row, 0);
        String gid = (String) orderTable.getValueAt(row, 2);
        // 查询该组下所有项目
        java.util.List<java.util.Map<String, Object>> items = manager.dao.CheckGroupDao.queryItemsByGroup(gid);
        java.util.List<manager.pojo.CheckResult> results = manager.dao.CheckResultDao.listByOrderId(orderId);
        java.util.Map<Integer, String> valueMap = new java.util.HashMap<>();
        for (manager.pojo.CheckResult r : results) {
            valueMap.put(r.getCid(), r.getValue());
        }
        String[] colNames = {"项目编号", "项目名称", "参考值", "测量值", "单位"};
        Object[][] data = new Object[items.size()][5];
        for (int i = 0; i < items.size(); i++) {
            java.util.Map<String, Object> m = items.get(i);
            int cid = (int) m.get("cid");
            data[i][0] = m.get("ccode");
            data[i][1] = m.get("cname");
            data[i][2] = m.get("refer_val");
            data[i][3] = valueMap.getOrDefault(cid, "待录入");
            data[i][4] = m.get("unit");
        }
        JTable table = new JTable(data, colNames);
        table.setEnabled(false);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(500, 250));
        JOptionPane.showMessageDialog(this, scroll, "体检结果明细", JOptionPane.INFORMATION_MESSAGE);
    }
}
