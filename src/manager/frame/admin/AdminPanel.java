package manager.frame.admin;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import manager.frame.LoginPanel;
import manager.frame.MainFrame;
import manager.util.SystemConstants;

public class AdminPanel extends JPanel {
    // 内部窗口容器 用于显示多个菜单对应的内容面板
    private static JDesktopPane contentPanel = new JDesktopPane();

    public AdminPanel() {
        this.setBounds(0, 0, SystemConstants.FRAME_WIDTH, SystemConstants.FRAME_HEIGHT);
        this.setLayout(new BorderLayout());
        this.setVisible(true);
        // 创建菜单栏
        JMenuBar menuBar = new JMenuBar();//菜单栏的容器
        menuBar.setBounds(0, 0, SystemConstants.FRAME_WIDTH, 50);// 设置菜单栏尺寸
        //将菜单栏加入到AdminPanel的上(北)
        this.add(menuBar, BorderLayout.NORTH);
        contentPanel.removeAll();// 清空内容面板
        contentPanel.repaint();// 重绘内容面板
        //将内部窗口设置到AdminPanel的中间
        this.add(contentPanel, BorderLayout.CENTER);
        // 创建数据管理菜单
        JMenu parentMenu = new JMenu("检查项管理");//一级菜单1
        parentMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                setContent(new DataTablePanel());//切换到检查项管理面板
            }
        });
        JMenu adminMenu = new JMenu("管理员管理");//一级菜单2
        adminMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                setContent(new AdminManagePanel());
            }
        });
        JMenu userMenu = new JMenu("用户管理");//一级菜单3
        userMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                setContent(new UserManagePanel());
            }
        });
        JMenu systemMenu = new JMenu("系统管理");//一级菜单4
        //将菜单加入到菜单栏容器中
        menuBar.add(parentMenu);
        menuBar.add(adminMenu);
        menuBar.add(userMenu);
        menuBar.add(systemMenu);
        //创建系统管理的两个二级菜单
        JMenuItem passwordMenu = new JMenuItem("修改密码");
        JMenuItem logoutMenu = new JMenuItem("退出登录");
        //将二级菜单加入系统管理一级菜单中
        systemMenu.add(passwordMenu);
        systemMenu.add(logoutMenu);
        //添加修改密码点击事件
        passwordMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                setContent(new manager.frame.ChangePasswordPanel());
            }
        });
        //添加鼠标点击监听器 退出登录的点击事件
        logoutMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                contentPanel.removeAll();//删除所有组件 清空页面
                contentPanel.repaint();//重绘页面
                MainFrame.setContent(new LoginPanel());
            }
        });
        JMenu checkGroupMenu = new JMenu("检查组管理");
        checkGroupMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                setContent(new CheckGroupPanel());
            }
        });
        menuBar.add(checkGroupMenu);
        JMenu orderMenu = new JMenu("预约管理");
        orderMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                setContent(new OrderManagePanel());
            }
        });
        menuBar.add(orderMenu);
    }

    //切换窗口的方法--多个菜单功能  jInternalFrame内部窗口
    public static void setContent(JInternalFrame internalFrame) {
        internalFrame.setSize(SystemConstants.FRAME_WIDTH - 15, SystemConstants.FRAME_HEIGHT - 60);
        internalFrame.setVisible(true);
        //切换菜单标题时将上一个页面清空 添加新的页面
        contentPanel.removeAll();//删除所有组件 清空页面
        contentPanel.repaint();//重绘页面
        contentPanel.add(internalFrame);//添加内部窗口到内部内容面板

    }


}
