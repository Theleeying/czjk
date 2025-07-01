package manager.frame.admin;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import manager.dao.CheckItemDao;
import manager.pojo.CheckItem;

public class DataEditPanel extends JInternalFrame {
    // 添加和修改数据 都用这个页面  添加数据时id为null 修改数据时传递id
    public DataEditPanel(Integer id) {
        super("检查项编辑", true, true, true, true);
        setVisible(true);
        setBackground(Color.white);
        setLayout(new BorderLayout());
        // 标题
        JLabel titleLabel = new JLabel(id == null ? "添加检查项" : "修改检查项");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 22));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(18, 0, 8, 0));
        add(titleLabel, BorderLayout.NORTH);
        // 表单区重写，保证输入框可选中
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.white);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 18, 10, 18);
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0;
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel label1 = new JLabel("代号：");
        JLabel label2 = new JLabel("名称：");
        JLabel label3 = new JLabel("参考值：");
        JLabel label4 = new JLabel("单位：");
        formPanel.add(label1, gbc);
        gbc.gridy++;
        formPanel.add(label2, gbc);
        gbc.gridy++;
        formPanel.add(label3, gbc);
        gbc.gridy++;
        formPanel.add(label4, gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1;
        JTextField field1 = new JTextField(18);
        field1.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        field1.setEditable(true);
        field1.setFocusable(true);
        formPanel.add(field1, gbc);
        gbc.gridy++;
        JTextField field2 = new JTextField(18);
        field2.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        field2.setEditable(true);
        field2.setFocusable(true);
        formPanel.add(field2, gbc);
        gbc.gridy++;
        JTextField field3 = new JTextField(18);
        field3.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        field3.setEditable(true);
        field3.setFocusable(true);
        formPanel.add(field3, gbc);
        gbc.gridy++;
        JTextField field4 = new JTextField(18);
        field4.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        field4.setEditable(true);
        field4.setFocusable(true);
        formPanel.add(field4, gbc);
        add(formPanel, BorderLayout.CENTER);
        // 按钮区
        JButton btn = new JButton("提交");
        btn.setFont(new Font("微软雅黑", Font.BOLD, 16));
        btn.setBackground(new Color(66, 133, 244));
        btn.setForeground(Color.white);
        btn.setFocusPainted(false);
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(Color.white);
        btnPanel.add(btn);
        add(btnPanel, BorderLayout.SOUTH);
        // 回显
        if (id != null) {
            CheckItem checkItem = CheckItemDao.queryCheckItemById(id);
            field1.setText(checkItem.getCcode());
            field2.setText(checkItem.getCname());
            field3.setText(checkItem.getReferVal());
            field4.setText(checkItem.getUnit());
        }
        // 提交监听
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 校验输入
                String ccode = field1.getText().trim();
                String cname = field2.getText().trim();
                String referVal = field3.getText().trim();
                String unit = field4.getText().trim();
                if (ccode.length() == 0) {
                    JOptionPane.showMessageDialog(btn.getParent(), "代号不能为空", "系统提示", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (ccode.length() < 2) {
                    JOptionPane.showMessageDialog(btn.getParent(), "代号长度不能小于2", "系统提示", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                // 用户名安全获取
                String optionUser = (manager.frame.MainFrame.users != null && manager.frame.MainFrame.users.getUname() != null) ? manager.frame.MainFrame.users.getUname() : "";
                CheckItem checkItem = new CheckItem(id, ccode, cname, referVal, unit, optionUser);
                if (id == null) { // 添加
                    // 检查ccode唯一性
                    CheckItem checkItem1 = manager.dao.CheckItemDao.queryCheckItemById(ccode);
                    if (checkItem1 != null) {
                        JOptionPane.showMessageDialog(btn.getParent(), "检查项编号重复，无法添加", "系统提示！", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    int i = manager.dao.CheckItemDao.addCheckItem(checkItem);
                    if (i > 0) {
                        JOptionPane.showMessageDialog(btn.getParent(), "添加成功！", "系统提示！", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(btn.getParent(), "检查项添加失败", "系统提示！", JOptionPane.WARNING_MESSAGE);
                    }
                } else { // 修改
                    // 检查ccode唯一性（排除本身）
                    CheckItem checkItem1 = manager.dao.CheckItemDao.queryCheckItemById(ccode);
                    if (checkItem1 != null && !checkItem1.getCid().equals(id)) {
                        JOptionPane.showMessageDialog(btn.getParent(), "检查项编号重复，无法修改", "系统提示！", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    int i = manager.dao.CheckItemDao.updateCheckItem(checkItem);
                    if (i > 0) {
                        JOptionPane.showMessageDialog(btn.getParent(), "修改成功！", "系统提示！", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(btn.getParent(), "检查项修改失败", "系统提示！", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        });
    }
}
