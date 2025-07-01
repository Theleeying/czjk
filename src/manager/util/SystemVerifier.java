package manager.util;

import javax.swing.*;
import javax.swing.text.JTextComponent;

public class SystemVerifier { //校验空和长度
    //InputVerifier 可以被用来检查用户输入是否有效   参数：字段名，最小长度，最大长度
    public static InputVerifier emptyVerify(String fieldName, Integer minLen, Integer maxLen) {
        return new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                input.setVerifyInputWhenFocusTarget(false);//校验空和长度
                String text = ((JTextComponent) input).getText();//校验空和长度
                int len = text.trim().length();// 计算文本长度（去除空格）
                if (len == 0) {// 空值验证
                    JOptionPane.showMessageDialog(null, fieldName + "不能为空", "系统提示",
                            JOptionPane.WARNING_MESSAGE);
                    return false;
                }
                if (minLen != null && len < minLen) {
                    JOptionPane.showMessageDialog(null, fieldName + "长度不能小于" + minLen, "系统提示",
                            JOptionPane.WARNING_MESSAGE);
                    return false;
                }
                if (maxLen != null && len > maxLen) {
                    JOptionPane.showMessageDialog(null, fieldName + "长度不能大于" + maxLen, "系统提示",
                            JOptionPane.WARNING_MESSAGE);
                    return false;
                }
                return true;
            }
        };
    }
}