package cn.snowsoft.iot.module.warning.utils;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class StringToList {

    public static List<Long> toLongList (String str) {
        // 使用split(",")按逗号分割字符串，得到字符串数组
        String[] numbersArray = str.split(",");

        // 使用ArrayList来收集Long类型的元素
        List<Long> numbersList = new ArrayList<>();

        // 遍历字符串数组，将每个字符串元素转换为Long并添加到列表中
        for (String numStr : numbersArray) {
            try {
                numbersList.add(Long.parseLong(numStr));
            } catch (NumberFormatException e) {
                // 处理转换失败的情况，例如，如果字符串中包含非数字字符
                System.err.println("转换错误: " + numStr + " 不是有效的数字");
                // 根据实际需求，可以选择继续处理或抛出异常等
            }
        }

        return numbersList;
    }
}
