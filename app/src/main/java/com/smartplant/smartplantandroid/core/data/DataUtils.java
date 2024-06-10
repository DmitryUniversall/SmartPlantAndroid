package com.smartplant.smartplantandroid.core.data;

import java.util.ArrayList;
import java.util.List;

public class DataUtils {
    public static String convertListToString(List<Integer> list, String delimiter) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < list.size(); i++) {
            stringBuilder.append(list.get(i));
            if (i < list.size() - 1) {
                stringBuilder.append(delimiter);
            }
        }

        return stringBuilder.toString();
    }

    public static List<Integer> convertStringToIntegerList(String str, String delimiter) {
        List<Integer> list = new ArrayList<>();
        if (str != null && !str.isEmpty()) {
            String[] split = str.split(delimiter);
            for (String s : split) {
                list.add(Integer.parseInt(s));
            }
        }
        return list;
    }
}
