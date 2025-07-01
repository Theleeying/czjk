package manager.frame;

import manager.dao.UserDao;
import manager.frame.admin.AdminPanel;
import manager.frame.user.UserPanel;
import manager.pojo.Users;
import manager.util.SystemConstants;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class LoginPanel extends JPanel {
    //读取图片路径的文件夹---会有很多个图片，定义成全局的
    private static final String DIR = LoginPanel.class.getClassLoader().getResource("manager/images").getPath();

    //初始化登录页 设置面板尺寸和布局
    public LoginPanel() {
        //设置x，y，宽度和高度
        this.setBounds(0, 0, SystemConstants.FRAME_WIDTH, SystemConstants.FRAME_HEIGHT);
        this.setLayout(null);// 使用绝对定位布局 为空
        //添加登录面板
        JPanel panel = new JPanel() {
            protected void paintComponent(Graphics g) {
                //将画笔g转为平面画笔
                Graphics2D g2 = (Graphics2D) g;
                //绘制图片的坐标，x，y，宽高，null没有意义
                g2.drawImage(new ImageIcon(DIR + "/arg.png").getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };
        panel.setBounds(200, 100, 500, 300);
        this.add(panel);
        //登录表单的大盒子
        Box box = Box.createVerticalBox();//创建垂直盒子
        panel.add(box);//添加盒子
        box.add(Box.createVerticalStrut(15));//添加间隔

        //标题
        Box box0 = Box.createHorizontalBox();
        JLabel title = new JLabel("管理系统");
        title.setFont(new Font("微软雅黑", Font.BOLD, 30));
        box0.add(title);
        box.add(box0);
        box.add(Box.createVerticalStrut(15));//添加间隔

        //设置字体的通用格式
        Font font = new Font("微软雅黑", Font.BOLD, 20); //字体样式
        Border border = BorderFactory.createLoweredSoftBevelBorder();//边框样式

        // 用户名输入行容器（水平布局）
        Box box1 = Box.createHorizontalBox();
        JLabel nameLabel = new JLabel("账 号：");// 创建用户名标签
        nameLabel.setFont(font);
        box1.add(nameLabel);
        // 创建用户名输入框（宽度15字符）
        JTextField nameField = new JTextField(15);//参数是宽度
        nameField.setBorder(border);
        box1.add(nameField);
        box.add(box1);
        box.add(Box.createVerticalStrut(15));//间隔

        // 密码输入行容器（水平布局）
        Box box2 = Box.createHorizontalBox();
        JLabel pwdLabel = new JLabel("密 码：");
        pwdLabel.setFont(font);
        box2.add(pwdLabel);
        // 创建密码输入框（宽度15字符）
        JPasswordField pwdField = new JPasswordField(15);//参数是宽度
        pwdField.setBorder(border);
        box2.add(pwdField);
        box.add(box2);
        box.add(Box.createVerticalStrut(15));//间隔

        //如果做权限划分就加这个身份按钮
        //----权限按钮开始----
        JRadioButton adminRadio = new JRadioButton("管理员", true);
        JRadioButton commonRadio = new JRadioButton("普通用户");
        adminRadio.setFont(font);//字体
        adminRadio.setOpaque(false);//设置透明的
        adminRadio.setFocusPainted(false);//选中是否有边框
        commonRadio.setFont(font);//字体
        commonRadio.setOpaque(false);
        commonRadio.setFocusPainted(false);
        //实现两个按钮的二选一效果，加入一个分组中
        ButtonGroup group = new ButtonGroup();
        group.add(adminRadio);
        group.add(commonRadio);
        //创建一个水平的盒子，让两个按钮一排显示
        Box box3 = Box.createHorizontalBox();
        box3.add(adminRadio);
        box3.add(commonRadio);
        box.add(box3);
        box.add(Box.createVerticalStrut(15));//间隔
        // 登录按钮容器（水平布局）
        JButton loginBtn = new JButton();
        // 加载登录按钮图片 SCALE_DEFAULT缩放
        Image login = new ImageIcon(DIR + "/log_btn.png").getImage().getScaledInstance(110, 60, Image.SCALE_DEFAULT);
        loginBtn.setIcon(new ImageIcon(login));//将图片加入按钮
        loginBtn.setBorderPainted(false); //false表示不绘制边框
        loginBtn.setOpaque(false);// 设置透明背景
        loginBtn.setContentAreaFilled(false);//false表示内容区域不填充
        loginBtn.setBorder(null);//取消边框

        // 注册按钮
        JButton regBtn = new JButton();
        // 加载注册按钮图片 SCALE_DEFAULT缩放
        Image regImg = new ImageIcon(DIR + "/reg_btn.png").getImage().getScaledInstance(110, 60, Image.SCALE_DEFAULT);
        regBtn.setIcon(new ImageIcon(regImg));//将图片加入按钮
        regBtn.setBorderPainted(false); //不绘制边框
        regBtn.setOpaque(false);// 设置透明背景
        regBtn.setContentAreaFilled(false);//内容区域不填充
        regBtn.setBorder(null);//取消边框
        box.add(Box.createVerticalStrut(10));
        box.add(regBtn);
        // 添加登录、注册按钮到容器
        Box box4 = Box.createHorizontalBox();
        box4.add(loginBtn);
        box4.add(Box.createHorizontalStrut(50));// 添加水平间距（20像素）
        box4.add(regBtn);
        box.add(box4);

        //登录按钮的点击事件
        loginBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //获取用户名和密码
                String username = nameField.getText();
                String password = new String(pwdField.getPassword());
                //判断是管理员还是普通用户
                Users users = null;
                JPanel panel1 = null;//切换面板
                if (adminRadio.isSelected()) {//管理员
                    users = new UserDao().getUserByName(username, 1);
                    panel1 = new AdminPanel();
                } else {//普通用户
                    users = new UserDao().getUserByName(username, 2);
                    panel1 = new UserPanel();
                }
                //验证 用户是否存在并且密码匹配
                if (users == null || !users.getPassword().equals(password)) {
                    JOptionPane.showMessageDialog(loginBtn.getParent(),
                            "用户名或密码错误", "系统提示",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    //登录成功 跳转管理员或者用户的界面
                    System.out.println("登录成功");
                    MainFrame.setContent(panel1);
                    MainFrame.users = users;
                    // 新增：全局记录当前用户
                    manager.util.SystemConstants.currentUser = users;
                }
            }
        });
        regBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                MainFrame.setContent(new RegisterPanel());
            }
        });

    }

    //背景图片直接绘制给JPanel面板
    //paintComponent绘制背景图方法
    @Override
    protected void paintComponent(Graphics g) {
        //将画笔g转为平面画笔
        Graphics2D g2 = (Graphics2D) g;
        //绘制图片的坐标，x，y，宽高，null没有意义
//        g2.drawImage(new ImageIcon(DIR + "/bn.png").getImage(), 0, 0, getWidth(), getHeight(), null);

        /*第二种方式通过ImageIo读取图片*/
        try {
            // 使用ImageIO.read()读取图片
            Image img = ImageIO.read(new File(DIR + "/bn.png"));
            // 绘制图片的坐标，x，y，宽高
            g2.drawImage(img, 0, 0, getWidth(), getHeight(), null);
        } catch (IOException e) {
            e.printStackTrace(); // 如果图片加载失败，打印异常信息//
        }
    }
}