package manager.frame;

import manager.pojo.Users;
import manager.util.SystemConstants;

import javax.swing.*;
import java.awt.*;

public class MainFrame {
    /*将JFrame定义成常亮，因为我们要在点击登录时，
     * 跳转到主页面，然后点击退出时要跳转回登录页
     * 要在别的类中去拿到这个JFrame然后替换他
     * 所以定义成常亮*/
    // 全局静态主窗口实例，保证单例访问
    public static final JFrame frame = new JFrame("管理系统！");

    public static Users users;

    public static void main(String[] args) {
        // 设置窗口基本属性
        frame.setSize(SystemConstants.FRAME_WIDTH, SystemConstants.FRAME_HEIGHT);//设置窗口的大小
        frame.setContentPane(new LoginPanel());//初始显示登录面板 将菜单加入画框中,进入系统显示登录页
        frame.setAlwaysOnTop(true);//设置窗口始终置顶
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//设置关闭窗口时退出程序，点击x号就关闭程序
        frame.setLocationRelativeTo(null);//设置窗口的初始位置居中显示
        frame.setVisible(true);//窗口显示--尽快调用paint方法绘制窗口
    }

    //定义一个方法，去修改JFrame中的内容--会有很多个页面要去切换
    public static void setContent(JPanel jPanel) {
        frame.setContentPane(jPanel);// 设置窗口的内容面板
    }

    // 支持JInternalFrame的切换
    public static void setContent(JInternalFrame jInternalFrame) {
        JPanel panel = new JPanel(new BorderLayout());
        jInternalFrame.setSize(SystemConstants.FRAME_WIDTH, SystemConstants.FRAME_HEIGHT);
        panel.add(jInternalFrame, BorderLayout.CENTER);
        frame.setContentPane(panel);
        frame.revalidate();
        frame.repaint();
    }
}
