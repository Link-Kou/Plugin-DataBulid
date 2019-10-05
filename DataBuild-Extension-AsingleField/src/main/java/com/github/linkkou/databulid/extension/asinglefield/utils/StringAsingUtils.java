package com.github.linkkou.databulid.extension.asinglefield.utils;

import java.util.List;

public class StringAsingUtils {

    /**
     * 首字母转大写
     */
    public static String toUpperCaseFirstOne(String s) {
        if (Character.isUpperCase(s.charAt(0))) {
            return s;
        } else {
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }

    /**
     * 首字母转小写
     */
    public static String toLowerCaseFirstOne(String s) {
        if (Character.isLowerCase(s.charAt(0))) {
            return s;
        } else {
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }


    /**
     * 字段排除
     *
     * @param name
     * @param excludeVarOrMethods
     * @return true 字段排除 false 不排除
     */
    public static boolean excludeVarOrMethods(String name, List<String> excludeVarOrMethods) {
        for (String vm : excludeVarOrMethods) {
            if (name.equals(vm)) {
                return true;
            }
        }
        return false;
    }


}
