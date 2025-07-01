package manager.frame.admin;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import manager.dao.CheckItemDao;

public class DataTablePanel extends JInternalFrame {
    private JTextField field1 = new JTextField(28);
    private JTextField field2 = new JTextField(28);
    private JTable table = new JTable() {
        public boolean isCellEditable(int row, int column) { return false; }
    };

    public DataTablePanel() {
        super("检查项管理", true, true, true, true);
        this.setVisible(true);
        setBackground(Color.white);
        setLayout(new BorderLayout());
        // 标题
        JLabel titleLabel = new JLabel("检查项管理");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 22));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(18, 0, 8, 0));
        add(titleLabel, BorderLayout.NORTH);
        // 顶部操作区
        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBackground(Color.white);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0;
        gbc.gridx = 0;
        topPanel.add(new JLabel("名称："), gbc); gbc.gridx++;
        field1.setPreferredSize(new Dimension(220, 32));
        gbc.weightx = 1;
        topPanel.add(field1, gbc); gbc.gridx++;
        gbc.weightx = 0;
        topPanel.add(new JLabel("编号："), gbc); gbc.gridx++;
        field2.setPreferredSize(new Dimension(220, 32));
        gbc.weightx = 1;
        topPanel.add(field2, gbc); gbc.gridx++;
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
        // 初始加载数据
        search();
        // 查询按钮
        searchBtn.addActionListener(e -> search());
        // 添加
        addBtn.addActionListener(e -> AdminPanel.setContent(new DataEditPanel(null)));
        // 修改
        editBtn.addActionListener(e -> {
            int rowNum = table.getSelectedRow();
            if (rowNum <= -1) return;
            AdminPanel.setContent(new DataEditPanel((Integer) table.getValueAt(rowNum, 0)));
        });
        // 删除
        delBtn.addActionListener(e -> {
            int rowNum = table.getSelectedRow();
            if (rowNum <= -1) return;
            CheckItemDao.deleteCheckItem((int) table.getValueAt(rowNum, 0));
            search();
        });
    }

    private void search() {
        DefaultTableModel tableModel = new DefaultTableModel(
                CheckItemDao.querryAllCheckItem(field1.getText(), field2.getText()), CheckItemDao.columnNames
        );
        table.setModel(tableModel);
    }
}
