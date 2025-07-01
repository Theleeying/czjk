package manager.frame.admin;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import manager.dao.CheckGroupDao;
import manager.dao.CheckItemDao;
import manager.pojo.CheckItem;

public class CheckGroupPanel extends JInternalFrame {
    private final JTextField nameField = new JTextField(28);
    private final JTextField noField = new JTextField(28);
    private final JTextField priceField = new JTextField(28);
    private final JTable table = new JTable() {
        @Override
        public boolean isCellEditable(int row, int column) { return false; }
    };

    public CheckGroupPanel() {
        super("检查组管理", true, true, true, true);
        setVisible(true);
        setBackground(Color.white);
        setLayout(new BorderLayout());
        // 标题
        JLabel titleLabel = new JLabel("检查组管理");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 22));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(18, 0, 8, 0));
        add(titleLabel, BorderLayout.NORTH);
        // 操作区
        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBackground(Color.white);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0;
        gbc.gridx = 0;
        topPanel.add(new JLabel("组名："), gbc); gbc.gridx++;
        nameField.setPreferredSize(new Dimension(220, 32));
        gbc.weightx = 1;
        topPanel.add(nameField, gbc); gbc.gridx++;
        gbc.weightx = 0;
        topPanel.add(new JLabel("编号："), gbc); gbc.gridx++;
        noField.setPreferredSize(new Dimension(220, 32));
        gbc.weightx = 1;
        topPanel.add(noField, gbc); gbc.gridx++;
        gbc.weightx = 0;
        topPanel.add(new JLabel("价格："), gbc); gbc.gridx++;
        priceField.setPreferredSize(new Dimension(220, 32));
        gbc.weightx = 1;
        topPanel.add(priceField, gbc); gbc.gridx++;
        gbc.weightx = 0;
        JButton searchBtn = new JButton("查询");
        JButton addBtn = new JButton("添加");
        JButton editBtn = new JButton("修改");
        JButton delBtn = new JButton("删除");
        Font btnFont = new Font("微软雅黑", Font.PLAIN, 15);
        for (JButton btn : new JButton[]{searchBtn, addBtn, editBtn, delBtn}) {
            btn.setFont(btnFont);
            btn.setFocusPainted(false);
        }
        addBtn.setBackground(new Color(66, 133, 244)); addBtn.setForeground(Color.white);
        searchBtn.setBackground(new Color(240,240,240));
        editBtn.setBackground(new Color(240,240,240));
        delBtn.setBackground(new Color(240,240,240));
        topPanel.add(searchBtn, gbc); gbc.gridx++;
        topPanel.add(addBtn, gbc); gbc.gridx++;
        topPanel.add(editBtn, gbc); gbc.gridx++;
        topPanel.add(delBtn, gbc);
        add(topPanel, BorderLayout.PAGE_START);
        // 表格区
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.white);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220,220,220), 1));
        panel.add(scrollPane, BorderLayout.CENTER);
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setFont(new Font("微软雅黑", Font.BOLD, 15));
        table.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        table.setRowHeight(28);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(panel, BorderLayout.CENTER);
        // 事件绑定
        searchBtn.addActionListener(e -> search());
        addBtn.addActionListener(e -> showEditDialog(null));
        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {JOptionPane.showMessageDialog(this, "请选择要修改的检查组！"); return;}
            Map<String, Object> group = getGroupFromTable(row);
            showEditDialog(group);
        });
        delBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {JOptionPane.showMessageDialog(this, "请选择要删除的检查组！"); return;}
            String gid = String.valueOf(table.getValueAt(row, 0));
            int r = JOptionPane.showConfirmDialog(this, "确定删除该检查组？", "提示", JOptionPane.YES_NO_OPTION);
            if (r == JOptionPane.YES_OPTION) {
                CheckGroupDao.deleteGroup(gid);
                search();
            }
        });
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0 && e.getClickCount() == 2) {
                    String gid = String.valueOf(table.getValueAt(row, 0));
                    String gName = String.valueOf(table.getValueAt(row, 2));
                    showGroupItemsDialog(gid, gName);
                }
            }
        });
        search();
    }

    private void showEditDialog(Map<String, Object> group) {
        JTextField noFieldLocal = new JTextField(group == null ? "" : String.valueOf(group.get("g_no")), 14);
        JTextField nameFieldLocal = new JTextField(group == null ? "" : String.valueOf(group.get("g_name")), 14);
        String priceStr = "";
        if (group != null) {
            Object priceObj = group.get("price");
            if (priceObj != null) {
                priceStr = priceObj instanceof java.math.BigDecimal ? ((java.math.BigDecimal) priceObj).toPlainString() : priceObj.toString();
            }
        }
        JTextField priceFieldLocal = new JTextField(priceStr, 14);
        java.util.List<CheckItem> allItems = CheckItemDao.listAllCheckItems();
        java.util.Set<Integer> groupCids = new java.util.HashSet<>();
        if (group != null) {
            String gid = String.valueOf(group.get("gid"));
            groupCids.addAll(CheckGroupDao.getGroupItems(gid));
        }
        String[] columnNames = {"选择", "编号", "名称", "参考值", "单位"};
        Object[][] tableData = new Object[allItems.size()][5];
        for (int i = 0; i < allItems.size(); i++) {
            CheckItem item = allItems.get(i);
            tableData[i][0] = groupCids.contains(item.getCid());
            tableData[i][1] = item.getCcode();
            tableData[i][2] = item.getCname();
            tableData[i][3] = item.getReferVal();
            tableData[i][4] = item.getUnit();
        }
        JTable itemTable = new JTable(new DefaultTableModel(tableData, columnNames) {
            @Override
            public Class<?> getColumnClass(int columnIndex) { return columnIndex == 0 ? Boolean.class : String.class; }
            @Override
            public boolean isCellEditable(int row, int column) { return column == 0; }
        });
        itemTable.setPreferredScrollableViewportSize(new Dimension(420, Math.min(200, 26 * Math.max(5, Math.min(10, allItems.size())))));
        itemTable.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 14));
        itemTable.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        itemTable.setRowHeight(26);
        JScrollPane itemScroll = new JScrollPane(itemTable);
        itemScroll.setPreferredSize(new Dimension(420, 180));
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1;
        p.add(new JLabel("编号:"), gbc);
        gbc.gridx = 1;
        p.add(noFieldLocal, gbc);
        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 1;
        p.add(new JLabel("组名:"), gbc);
        gbc.gridx = 1;
        p.add(nameFieldLocal, gbc);
        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 1;
        p.add(new JLabel("价格:"), gbc);
        gbc.gridx = 1;
        p.add(priceFieldLocal, gbc);
        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        p.add(new JLabel("选择检查项:"), gbc);
        gbc.gridy++;
        p.add(itemScroll, gbc);
        int r = JOptionPane.showConfirmDialog(this, p, group == null ? "添加检查组" : "修改检查组", JOptionPane.OK_CANCEL_OPTION);
        if (r == JOptionPane.OK_OPTION) {
            String gNo = noFieldLocal.getText().trim();
            String gName = nameFieldLocal.getText().trim();
            String price = priceFieldLocal.getText().trim();
            java.util.List<Integer> selectedCids = new java.util.ArrayList<>();
            for (int i = 0; i < itemTable.getRowCount(); i++) {
                Boolean checked = (Boolean) itemTable.getValueAt(i, 0);
                if (checked != null && checked) {
                    selectedCids.add(allItems.get(i).getCid());
                }
            }
            if (gNo.isEmpty() || gName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "编号和组名不能为空且不能为全空格！", "输入错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (selectedCids.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请至少选择一个检查项！", "输入错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (group == null) {
                if (CheckGroupDao.existsGroupNoOrName(gNo, gName)) {
                    JOptionPane.showMessageDialog(this, "编号或组名已存在，不能重复添加！", "输入错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String gid = java.util.UUID.randomUUID().toString();
                String newGid = CheckGroupDao.addGroup(gid, gNo, gName, price);
                if (newGid == null) {
                    JOptionPane.showMessageDialog(this, "添加检查组失败！", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                CheckGroupDao.addGroupItems(gid, selectedCids);
            } else {
                String gid = String.valueOf(group.get("gid"));
                CheckGroupDao.updateGroup(gid, gNo, gName, price);
                CheckGroupDao.deleteGroupItems(gid);
                CheckGroupDao.addGroupItems(gid, selectedCids);
            }
            search();
        }
    }

    private void showGroupItemsDialog(String gid, String gName) {
        java.util.List<java.util.Map<String, Object>> items = CheckGroupDao.queryItemsByGroup(gid);
        if (items.isEmpty()) {
            JOptionPane.showMessageDialog(this, "该组下没有检查项！", "提示", JOptionPane.INFORMATION_MESSAGE);
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
        table.setEnabled(false);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(400, 200));
        JOptionPane.showMessageDialog(this, scroll, "组【" + gName + "】的检查项", JOptionPane.INFORMATION_MESSAGE);
    }

    private void search() {
        List<Map<String, Object>> list = CheckGroupDao.queryAll(nameField.getText(), noField.getText(), priceField.getText());
        Object[][] data = new Object[list.size()][5];
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> m = list.get(i);
            data[i][0] = m.get("gid");
            data[i][1] = m.get("g_no");
            data[i][2] = m.get("g_name");
            data[i][3] = m.get("price");
            data[i][4] = m.get("create_date");
        }
        table.setModel(new DefaultTableModel(data, new Object[]{"ID", "编号", "组名", "价格", "创建时间"}));
    }

    private Map<String, Object> getGroupFromTable(int row) {
        Map<String, Object> m = new HashMap<>();
        m.put("gid", String.valueOf(table.getValueAt(row, 0)));
        m.put("g_no", table.getValueAt(row, 1));
        m.put("g_name", table.getValueAt(row, 2));
        m.put("price", table.getValueAt(row, 3));
        m.put("create_date", table.getValueAt(row, 4));
        return m;
    }
}
