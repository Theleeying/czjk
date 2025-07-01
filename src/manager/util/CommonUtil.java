package manager.util;

import java.lang.reflect.Field;
import java.util.Collection;

public class CommonUtil {
    //用户将数据封装成JTable展示的格式
    //二维数组就是多个一维数组（用于JTable展示）
    public static Object[][] toArray(Collection<?> collection) {
        // 检查集合是否为空
        if (collection == null || collection.isEmpty()) {
            return new Object[0][0];
        }
        // 获取集合中的第一个元素，用于确定字段数量
        Object firstElement = collection.iterator().next();
        Class<?> clazz = firstElement.getClass(); // 获取元素的类
        Field[] fields = clazz.getDeclaredFields();// 获取所有字段
        // 创建二维数组，行数为集合的大小，列数为字段数量
        Object[][] result = new Object[collection.size()][fields.length];
        int rowIndex = 0;
        // 遍历集合
        for (Object item : collection) {
            // 遍历字段
            for (int colIndex = 0; colIndex < fields.length; colIndex++) {
                Field field = fields[colIndex];
                field.setAccessible(true); // 确保可以访问私有字段
                try {
                    result[rowIndex][colIndex] = field.get(item); // 获取字段值
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            rowIndex++;
        }
        return result;
    }

    //非空校验
    public static boolean isNotEmpty(String text) {
        if (text == null || text.trim().length() == 0) {
            return false;
        }
        return true;
    }
}
