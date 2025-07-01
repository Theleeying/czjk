package manager.frame;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import manager.dao.UserDao;
import manager.pojo.Users;

public class RegisterPanel extends JInternalFrame {
    public RegisterPanel() {
        super("注册", true, true, true, true);
        this.setVisible(true);
        JPanel panel = new JPanel();
        panel.setBackground(Color.white);
        panel.setLayout(new GridBagLayout());
        this.add(panel);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 18, 12, 18); // 增加间距
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        JLabel titleLabel = new JLabel("用户注册");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 22));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);
        gbc.gridy++;
        gbc.gridwidth = 1;
        // 账号
        JLabel userLabel = new JLabel("账号：");
        userLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        panel.add(userLabel, gbc);
        gbc.gridx = 1;
        JTextField usernameField = new JTextField(14);
        usernameField.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        panel.add(usernameField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        // 密码
        JLabel pwdLabel = new JLabel("密码：");
        pwdLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        panel.add(pwdLabel, gbc);
        gbc.gridx = 1;
        JPasswordField pwdField = new JPasswordField(14);
        pwdField.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        panel.add(pwdField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        // 姓名
        JLabel unameLabel = new JLabel("姓名：");
        unameLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        panel.add(unameLabel, gbc);
        gbc.gridx = 1;
        JTextField unameField = new JTextField(14);
        unameField.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        panel.add(unameField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        // 电话
        JLabel telLabel = new JLabel("电话：");
        telLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        panel.add(telLabel, gbc);
        gbc.gridx = 1;
        JTextField telField = new JTextField(14);
        telField.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        panel.add(telField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        // 注册类型
        JLabel typeLabel = new JLabel("注册类型：");
        typeLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        panel.add(typeLabel, gbc);
        gbc.gridx = 1;
        JComboBox<String> typeBox = new JComboBox<>(new String[]{"用户", "管理员"});
        typeBox.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        panel.add(typeBox, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        // 注册按钮
        JButton btn = new JButton("注册");
        btn.setFont(new Font("微软雅黑", Font.BOLD, 18));
        btn.setBackground(new Color(66, 133, 244));
        btn.setForeground(Color.white);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(120, 40));
        gbc.gridwidth = 2;
        panel.add(btn, gbc);
        gbc.gridy++;
        // 返回按钮
        JButton backBtn = new JButton("返回");
        backBtn.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        backBtn.setPreferredSize(new Dimension(120, 36));
        backBtn.setBackground(new Color(240, 240, 240));
        backBtn.setFocusPainted(false);
        panel.add(backBtn, gbc);
        // 事件绑定
        backBtn.addActionListener(e -> {
            manager.frame.MainFrame.setContent(new manager.frame.LoginPanel());
        });
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String username = usernameField.getText().trim();
                String pwd = new String(pwdField.getPassword());
                String uname = unameField.getText().trim();
                String tel = telField.getText().trim();
                boolean isAdmin = typeBox.getSelectedIndex() == 1;
                if (username.isEmpty() || pwd.isEmpty() || uname.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "账号、密码、姓名不能为空！", "提示", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                Users user = new Users();
                user.setUsername(username);
                user.setPassword(pwd);
                user.setUname(uname);
                user.setTel(tel);
                int result = UserDao.addUser(user, isAdmin);
                if (result > 0) {
                    JOptionPane.showMessageDialog(panel, "注册成功！请返回登录。", "提示", JOptionPane.INFORMATION_MESSAGE);
                    manager.frame.MainFrame.setContent(new manager.frame.LoginPanel());
                } else {
                    JOptionPane.showMessageDialog(panel, "注册失败，账号可能已存在。", "提示", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }
}
