package manager.frame.admin;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import manager.dao.UserDao;
import manager.pojo.Users;

public class AdminManagePanel extends JInternalFrame {
    private JTextField searchField = new JTextField(10);
    private JTable table = new JTable() {
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    public AdminManagePanel() {
        super("管理员管理", true, true, true, true);
        this.setVisible(true);
        this.setBackground(Color.white);
        // 标题
        JLabel titleLabel = new JLabel("管理员管理");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 22));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(titleLabel, BorderLayout.NORTH);
        // 顶部操作区
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 12));
        topPanel.setBackground(Color.white);
        JLabel searchLabel = new JLabel("姓名：");
        searchLabel.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        topPanel.add(searchLabel);
        topPanel.add(searchField);
        JButton searchBtn = new JButton("查询");
        JButton addBtn = new JButton("添加");
        JButton editBtn = new JButton("修改");
        JButton delBtn = new JButton("删除");
        // 按钮美化
        Font btnFont = new Font("微软雅黑", Font.PLAIN, 15);
        searchBtn.setFont(btnFont);
        addBtn.setFont(btnFont);
        editBtn.setFont(btnFont);
        delBtn.setFont(btnFont);
        addBtn.setBackground(new Color(66, 133, 244));
        addBtn.setForeground(Color.white);
        addBtn.setFocusPainted(false);
        searchBtn.setBackground(new Color(240,240,240));
        editBtn.setBackground(new Color(240,240,240));
        delBtn.setBackground(new Color(240,240,240));
        searchBtn.setFocusPainted(false);
        editBtn.setFocusPainted(false);
        delBtn.setFocusPainted(false);
        topPanel.add(searchBtn);
        topPanel.add(addBtn);
        topPanel.add(editBtn);
        topPanel.add(delBtn);
        this.add(topPanel, BorderLayout.SOUTH);
        // 表格区
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.white);
        this.add(panel, BorderLayout.CENTER);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220,220,220), 1));
        panel.add(scrollPane, BorderLayout.CENTER);
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setFont(new Font("微软雅黑", Font.BOLD, 15));
        table.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        table.setRowHeight(28);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        search();
        searchBtn.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                search();
            }
        });
        addBtn.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                showEditDialog(null);
            }
        });
        editBtn.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row < 0) return;
                Users user = getUserFromTable(row);
                showEditDialog(user);
            }
        });
        delBtn.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row < 0) return;
                int id = (int) table.getValueAt(row, 0);
                int r = JOptionPane.showConfirmDialog(AdminManagePanel.this, "确定删除该管理员？", "提示", JOptionPane.YES_NO_OPTION);
                if (r == JOptionPane.YES_OPTION) {
                    UserDao.deleteUser(id);
                    search();
                }
            }
        });
    }

    private void search() {
        table.setModel(new DefaultTableModel(UserDao.queryAllUsers(searchField.getText(), true), UserDao.columnNames));
    }

    private Users getUserFromTable(int row) {
        Users u = new Users();
        u.setUserId((Integer) table.getValueAt(row, 0));
        u.setUsername((String) table.getValueAt(row, 1));
        u.setUname((String) table.getValueAt(row, 2));
        u.setTel((String) table.getValueAt(row, 3));
        u.setPassword((String) table.getValueAt(row, 4));
        return u;
    }

    private void showEditDialog(Users user) {
        JTextField usernameField = new JTextField(user == null ? "" : user.getUsername(), 10);
        JTextField unameField = new JTextField(user == null ? "" : user.getUname(), 10);
        JTextField telField = new JTextField(user == null ? "" : user.getTel(), 10);
        JTextField pwdField = new JTextField(user == null ? "" : user.getPassword(), 10);
        JPanel p = new JPanel();
        p.setLayout(new GridLayout(0, 2));
        p.add(new JLabel("账号:"));
        p.add(usernameField);
        p.add(new JLabel("姓名:"));
        p.add(unameField);
        p.add(new JLabel("电话:"));
        p.add(telField);
        p.add(new JLabel("密码:"));
        p.add(pwdField);
        int r = JOptionPane.showConfirmDialog(this, p, user == null ? "添加管理员" : "修改管理员", JOptionPane.OK_CANCEL_OPTION);
        if (r == JOptionPane.OK_OPTION) {
            Users u = new Users();
            if (user != null) u.setUserId(user.getUserId());
            u.setUsername(usernameField.getText());
            u.setUname(unameField.getText());
            u.setTel(telField.getText());
            u.setPassword(pwdField.getText());
            if (user == null) {
                UserDao.addUser(u, true);
            } else {
                UserDao.updateUser(u);
            }
            search();
        }
    }
}
