package manager.frame.admin;

import manager.dao.CheckItemDao;
import manager.frame.MainFrame;
import manager.pojo.CheckItem;
import manager.util.SystemVerifier;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DataEditPanel extends JInternalFrame {
    //添加和修改数据 都用这个页面  添加数据时id为null 修改数据时传递id
    public DataEditPanel(Integer id) {
        super("数据列表", true, true, true, true);
        this.setVisible(true);//显示内部窗口
        JPanel panel = new JPanel();//创建主面板
        panel.setBackground(Color.white);
        this.add(panel);
        Box box = Box.createVerticalBox();// 创建垂直布局容器
        panel.add(box);// 添加垂直容器到面板
        box.add(Box.createVerticalStrut(55));// 添加垂直间距（55像素）
        //编号输入行
        Box box1 = Box.createHorizontalBox();
        box1.add(new JLabel("代号："));// 添加编号标签
        JTextField field1 = new JTextField(10);// 创建编号输入框
        // 设置输入验证器（非空，最小长度2）
        field1.setInputVerifier(SystemVerifier.emptyVerify("代号", 2, null));
        box1.add(field1); // 添加输入框到容器
        box.add(box1);// 添加行容器到垂直容器
        box.add(Box.createVerticalStrut(5));// 添加垂直间距（5像素）
        // 名称输入行
        Box box2 = Box.createHorizontalBox();
        box2.add(new JLabel("名称：")); // 添加名称标签
        JTextField field2 = new JTextField(10);// 创建名称输入框
        box2.add(field2);// 添加输入框到容器
        box.add(box2);// 添加行容器到垂直容器
        box.add(Box.createVerticalStrut(5));// 添加垂直间距（5像素）
        // 参考值输入行
        Box box3 = Box.createHorizontalBox();
        box3.add(new JLabel("参考值："));// 添加参考值标签
        JTextField field3 = new JTextField(10); // 创建参考值输入框
        box3.add(field3);
        box.add(box3);
        box.add(Box.createVerticalStrut(5));
        // 单位输入行
        Box box4 = Box.createHorizontalBox();
        box4.add(new JLabel("单位："));// 添加单位标签
        JTextField field4 = new JTextField(10); // 创建单位输入框
        box4.add(field4);
        box.add(box4);
        box.add(Box.createVerticalStrut(5));
        // 提交按钮容器
        JButton btn = new JButton("提交");
        Box boxBtn = Box.createHorizontalBox();
        boxBtn.add(Box.createHorizontalStrut(40)); // 添加水平间距（40像素）
        boxBtn.add(btn);
        box.add(boxBtn);

        //修改 将原本的数据进行回显到页面上
        if (id != null) {
            CheckItem checkItem = CheckItemDao.queryCheckItemById(id);
            field1.setText(checkItem.getCcode());
            field2.setText(checkItem.getCname());
            field3.setText(checkItem.getReferVal());
            field4.setText(checkItem.getUnit());
        }
        //给提交按钮添加监听器
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //当点击时触发校验判断 如果不成功就不能添加
                if (!field1.getInputVerifier().verify(field1)) {
                    return;//验证失败不提交
                }
                //验证成功准备增加数据 先初始化
                CheckItem checkItem = new CheckItem(id, field1.getText(), field2.getText(), field3.getText(), field4.getText(), MainFrame.users.getUname());

                if (id == null) {//添加
                    //先判断添加的数据的ccode编号是否已经存在
                    CheckItem checkItem1 = CheckItemDao.queryCheckItemById(field1.getText());
                    if (checkItem1 != null) {
                        JOptionPane.showMessageDialog(btn.getParent(), "检查项编号重复，无法添加", "系统提示！", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    //调用增加的业务方法
                    int i = CheckItemDao.addCheckItem(checkItem);
                    if (i > 0) {
                        JOptionPane.showMessageDialog(btn.getParent(), "添加成功！", "系统提示！", JOptionPane.INFORMATION_MESSAGE);
                        // 不自动跳转
                    } else {
                        JOptionPane.showMessageDialog(btn.getParent(), "检查项添加失败", "系统提示！", JOptionPane.WARNING_MESSAGE);
                    }
                } else {//修改
                    //先判断添加的数据的ccode编号是否已经存在
                    CheckItem checkItem1 = CheckItemDao.queryCheckItemById(field1.getText());
                    if (checkItem1 != null) {
                        JOptionPane.showMessageDialog(btn.getParent(), "检查项编号重复，无法修改", "系统提示！", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    int i = CheckItemDao.updateCheckItem(checkItem);
                    if (i > 0) {
                        JOptionPane.showMessageDialog(btn.getParent(), "修改成功！", "系统提示！", JOptionPane.INFORMATION_MESSAGE);
                        // 不自动跳转
                    } else {
                        JOptionPane.showMessageDialog(btn.getParent(), "检查项修改失败", "系统提示！", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        });

    }

}
