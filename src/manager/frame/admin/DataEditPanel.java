package manager.frame.admin;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import manager.dao.CheckItemDao;
import manager.frame.MainFrame;
import manager.pojo.CheckItem;
import manager.util.SystemVerifier;

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
        // 表单区
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.white);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 18, 10, 18);
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0;
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("代号："), gbc);
        gbc.gridy++;
        formPanel.add(new JLabel("名称："), gbc);
        gbc.gridy++;
        formPanel.add(new JLabel("参考值："), gbc);
        gbc.gridy++;
        formPanel.add(new JLabel("单位："), gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1;
        JTextField field1 = new JTextField(18);
        field1.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        field1.setInputVerifier(SystemVerifier.emptyVerify("代号", 2, null));
        formPanel.add(field1, gbc);
        gbc.gridy++;
        JTextField field2 = new JTextField(18);
        field2.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        formPanel.add(field2, gbc);
        gbc.gridy++;
        JTextField field3 = new JTextField(18);
        field3.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        formPanel.add(field3, gbc);
        gbc.gridy++;
        JTextField field4 = new JTextField(18);
        field4.setFont(new Font("微软雅黑", Font.PLAIN, 15));
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
                if (!field1.getInputVerifier().verify(field1)) {
                    return;
                }
                CheckItem checkItem = new CheckItem(id, field1.getText(), field2.getText(), field3.getText(), field4.getText(), MainFrame.users.getUname());
                if (id == null) {
                    CheckItem checkItem1 = CheckItemDao.queryCheckItemById(field1.getText());
                    if (checkItem1 != null) {
                        JOptionPane.showMessageDialog(btn.getParent(), "检查项编号重复，无法添加", "系统提示！", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    int i = CheckItemDao.addCheckItem(checkItem);
                    if (i > 0) {
                        JOptionPane.showMessageDialog(btn.getParent(), "添加成功！", "系统提示！", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(btn.getParent(), "检查项添加失败", "系统提示！", JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    CheckItem checkItem1 = CheckItemDao.queryCheckItemById(field1.getText());
                    if (checkItem1 != null) {
                        JOptionPane.showMessageDialog(btn.getParent(), "检查项编号重复，无法修改", "系统提示！", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    int i = CheckItemDao.updateCheckItem(checkItem);
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
