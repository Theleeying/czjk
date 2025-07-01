package manager.frame;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import manager.dao.UserDao;
import manager.pojo.Users;

public class ChangePasswordPanel extends JInternalFrame {
    public ChangePasswordPanel() {
        super("修改密码", true, true, true, true);
        this.setVisible(true);
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.white);
        this.add(panel);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(16, 24, 16, 24);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel titleLabel = new JLabel("修改密码");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 22));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel, gbc);
        gbc.gridy++;
        gbc.gridwidth = 1;
        // 旧密码
        JLabel oldPwdLabel = new JLabel("旧密码：");
        oldPwdLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        panel.add(oldPwdLabel, gbc);
        gbc.gridx = 1;
        JPasswordField oldPwdField = new JPasswordField(14);
        oldPwdField.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        panel.add(oldPwdField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        // 新密码
        JLabel newPwdLabel = new JLabel("新密码：");
        newPwdLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        panel.add(newPwdLabel, gbc);
        gbc.gridx = 1;
        JPasswordField newPwdField = new JPasswordField(14);
        newPwdField.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        panel.add(newPwdField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        // 确认新密码
        JLabel confirmPwdLabel = new JLabel("确认新密码：");
        confirmPwdLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        panel.add(confirmPwdLabel, gbc);
        gbc.gridx = 1;
        JPasswordField confirmPwdField = new JPasswordField(14);
        confirmPwdField.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        panel.add(confirmPwdField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        // 按钮
        JButton btn = new JButton("提交");
        btn.setFont(new Font("微软雅黑", Font.BOLD, 18));
        btn.setBackground(new Color(66, 133, 244));
        btn.setForeground(Color.white);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(120, 40));
        panel.add(btn, gbc);
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String oldPwd = new String(oldPwdField.getPassword());
                String newPwd = new String(newPwdField.getPassword());
                String confirmPwd = new String(confirmPwdField.getPassword());
                if (oldPwd.isEmpty() || newPwd.isEmpty() || confirmPwd.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "所有字段不能为空！", "提示", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (!newPwd.equals(confirmPwd)) {
                    JOptionPane.showMessageDialog(panel, "两次输入的新密码不一致！", "提示", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                Users user = MainFrame.users;
                if (!user.getPassword().equals(oldPwd)) {
                    JOptionPane.showMessageDialog(panel, "旧密码错误！", "提示", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                int result = UserDao.updatePassword(user.getUsername(), newPwd);
                if (result > 0) {
                    user.setPassword(newPwd);
                    JOptionPane.showMessageDialog(panel, "密码修改成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(panel, "密码修改失败！", "提示", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }
}

