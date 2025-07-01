package manager.util;

import javax.swing.*;
import javax.swing.text.JTextComponent;

public class SystemVerifier { //校验空和长度
    //InputVerifier 可以被用来检查用户输入是否有效   参数：字段名，最小长度，最大长度
    public static InputVerifier emptyVerify(String fieldName, Integer minLen, Integer maxLen) {
        return new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                String text = ((JTextComponent) input).getText();
                int len = text.trim().length();
                if (len == 0) {
                    return false;
                }
                if (minLen != null && len < minLen) {
                    return false;
                }
                if (maxLen != null && len > maxLen) {
                    return false;
                }
                return true;
            }
        };
    }
}