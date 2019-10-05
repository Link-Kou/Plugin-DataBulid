package com.github.linkkou.databulid.extension.asinglefield.annotation;

/**
 * @author lk
 * @version 1.0
 * @date 2019/10/5 15:27
 */
public class RegexsEntity {

    /**
     * 对所有字段进行首字母去除
     */
    String[] replaceFirst;

    /**
     * 单独实现字段名称直接对应,匹配上优先执行
     */
    RegexEntity[] replaceFirstMap;

    /**
     * 是否对替换后的剩余名称首字母进行大写或小写处理
     * true:进行大写处理（默认）
     * false:进行大写处理
     */
    Boolean replaceFirstCapital;

    public String[] getReplaceFirst() {
        return replaceFirst;
    }

    public RegexsEntity setReplaceFirst(String[] replaceFirst) {
        this.replaceFirst = replaceFirst;
        return this;
    }

    public RegexEntity[] getReplaceFirstMap() {
        return replaceFirstMap;
    }

    public RegexsEntity setReplaceFirstMap(RegexEntity[] replaceFirstMap) {
        this.replaceFirstMap = replaceFirstMap;
        return this;
    }

    public Boolean getReplaceFirstCapital() {
        return replaceFirstCapital;
    }

    public RegexsEntity setReplaceFirstCapital(Boolean replaceFirstCapital) {
        this.replaceFirstCapital = replaceFirstCapital;
        return this;
    }

    public static class RegexEntity {
        /**
         * 排除变量或方法
         *
         * @return
         */
        String methodsName;

        /**
         * 执行替换
         *
         * @return
         */
        String[] regex;

        public String getMethodsName() {
            return methodsName;
        }

        public RegexEntity setMethodsName(String methodsName) {
            this.methodsName = methodsName;
            return this;
        }

        public String[] getRegex() {
            return regex;
        }

        public RegexEntity setRegex(String[] regex) {
            this.regex = regex;
            return this;
        }
    }
}