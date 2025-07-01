package manager.frame.user;

import manager.dao.CheckGroupDao;
import manager.dao.OrderDao;
import manager.pojo.Order;
import manager.pojo.Users;
import manager.util.SystemConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.List;

public class OrderPanel extends JInternalFrame {
    private Users currentUser;
    private JTable orderTable;
    private JComboBox<String> groupBox;
    private JComboBox<String> typeBox;
    private JButton orderBtn;

    public OrderPanel(Users user) {
        super("体检预约与跟踪", true, true, true, true);
        this.currentUser = user;
        setLayout(new BorderLayout());
        this.setSize(700, 400);
        this.setVisible(true);
        // 顶部预约区
        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBackground(Color.white);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridy = 0; gbc.gridx = 0;
        topPanel.add(new JLabel("选择体检组："), gbc); gbc.gridx++;
        List<java.util.Map<String, Object>> groups = CheckGroupDao.queryAll(null, null, null);
        String[] groupNames = new String[groups.size()];
        String[] groupIds = new String[groups.size()];
        for (int i = 0; i < groups.size(); i++) {
            groupNames[i] = groups.get(i).get("g_name") + "(" + groups.get(i).get("g_no") + ")";
            groupIds[i] = String.valueOf(groups.get(i).get("gid"));
        }
        groupBox = new JComboBox<>(groupNames);
        groupBox.setPreferredSize(new Dimension(180, 32));
        topPanel.add(groupBox, gbc); gbc.gridx++;
        topPanel.add(new JLabel("体检方式："), gbc); gbc.gridx++;
        typeBox = new JComboBox<>(new String[]{"微信", "电脑"});
        typeBox.setPreferredSize(new Dimension(120, 32));
        topPanel.add(typeBox, gbc); gbc.gridx++;
        orderBtn = new JButton("预约体检");
        orderBtn.setFont(new Font("微软雅黑", Font.BOLD, 15));
        orderBtn.setBackground(new Color(66, 133, 244));
        orderBtn.setForeground(Color.white);
        orderBtn.setFocusPainted(false);
        orderBtn.setPreferredSize(new Dimension(120, 36));
        topPanel.add(orderBtn, gbc); gbc.gridx++;
        JButton viewGroupBtn = new JButton("查看组内项目");
        viewGroupBtn.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        viewGroupBtn.setBackground(new Color(240,240,240));
        viewGroupBtn.setFocusPainted(false);
        viewGroupBtn.setPreferredSize(new Dimension(120, 36));
        topPanel.add(viewGroupBtn, gbc); gbc.gridx++;
        JButton viewResultBtn = new JButton("查看体检结果");
        viewResultBtn.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        viewResultBtn.setBackground(new Color(240,240,240));
        viewResultBtn.setFocusPainted(false);
        viewResultBtn.setPreferredSize(new Dimension(120, 36));
        topPanel.add(viewResultBtn, gbc);
        add(topPanel, BorderLayout.NORTH);
        // 预约记录表
        orderTable = new JTable();
        orderTable.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 15));
        orderTable.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        orderTable.setRowHeight(28);
        orderTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(orderTable);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(220,220,220), 1));
        add(scroll, BorderLayout.CENTER);
        // 预约按钮事件
        orderBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int idx = groupBox.getSelectedIndex();
                if (idx < 0) {
                    JOptionPane.showMessageDialog(OrderPanel.this, "请选择体检组！");
                    return;
                }
                String gid = groupIds[idx];
                String gName = groupNames[idx];
                String type = typeBox.getSelectedIndex() == 0 ? "0" : "1";
                Order order = new Order();
                order.setUserId(currentUser.getUserId());
                order.setGid(gid);
                order.setName(currentUser.getUname());
                order.setSex(currentUser.getSex());
                order.setTelephone(currentUser.getTel());
                order.setOrderDate(new Date(System.currentTimeMillis()));
                order.setType(type);
                order.setStatus("0");
                int result = OrderDao.addOrder(order);
                if (result > 0) {
                    JOptionPane.showMessageDialog(OrderPanel.this, "预约成功！");
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(OrderPanel.this, "预约失败！");
                }
            }
        });
        viewGroupBtn.addActionListener(e -> {
            int idx = groupBox.getSelectedIndex();
            if (idx < 0) {
                JOptionPane.showMessageDialog(OrderPanel.this, "请选择体检组！");
                return;
            }
            String gid = groupIds[idx];
            List<java.util.Map<String, Object>> items = manager.dao.CheckGroupDao.queryItemsByGroup(gid);
            if (items.isEmpty()) {
                JOptionPane.showMessageDialog(OrderPanel.this, "该组下没有检查项！", "提示", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            String[] colNames = {"编号", "名称", "参考值", "单位"};
            Object[][] data = new Object[items.size()][4];
            for (int i = 0; i < items.size(); i++) {
                java.util.Map<String, Object> m = items.get(i);
                data[i][0] = m.get("ccode");
                data[i][1] = m.get("cname");
                data[i][2] = m.get("refer_val");
                data[i][3] = m.get("unit");
            }
            JTable table = new JTable(data, colNames);
            table.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 14));
            table.setFont(new Font("微软雅黑", Font.PLAIN, 14));
            table.setRowHeight(26);
            table.setEnabled(false);
            JScrollPane scroll2 = new JScrollPane(table);
            scroll2.setPreferredSize(new Dimension(400, 200));
            JOptionPane.showMessageDialog(OrderPanel.this, scroll2, "组内检查项", JOptionPane.INFORMATION_MESSAGE);
        });
        // 新增：查看体检结果按钮事件
        viewResultBtn.addActionListener(e -> viewResult());
        refreshTable();
    }

    private void refreshTable() {
        List<Order> orders = OrderDao.listOrdersByUser(currentUser.getUserId());
        String[] colNames = {"预约ID", "组ID", "姓名", "性别", "电话", "日期", "方式", "状态"};
        Object[][] data = new Object[orders.size()][8];
        for (int i = 0; i < orders.size(); i++) {
            Order o = orders.get(i);
            data[i][0] = o.getId();
            data[i][1] = o.getGid();
            data[i][2] = o.getName();
            data[i][3] = o.getSex();
            data[i][4] = o.getTelephone();
            data[i][5] = o.getOrderDate();
            data[i][6] = "0".equals(o.getType()) ? "微信" : "电脑";
            data[i][7] = "0".equals(o.getStatus()) ? "未到诊" : "到诊";
        }
        orderTable.setModel(new javax.swing.table.DefaultTableModel(data, colNames));
    }

    private void viewResult() {
        int row = orderTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "请选择要查看的预约！");
            return;
        }
        int orderId = (int) orderTable.getValueAt(row, 0);
        String gid = (String) orderTable.getValueAt(row, 1);
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
        table.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 14));
        table.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        table.setRowHeight(26);
        table.setEnabled(false);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(500, 250));
        JOptionPane.showMessageDialog(this, scroll, "体检结果明细", JOptionPane.INFORMATION_MESSAGE);
    }
}
