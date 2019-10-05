package com.github.linkkou.databulid.extension.asinglefield.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解
 *
 * @author lk
 * @version 1.0
 * @date 2019/10/2 15:01
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.SOURCE)
public @interface Regexs {

    /**
     * 对所有字段进行首字母去除
     *
     * @return |
     */
    String[] replaceFirst() default {"(get|is)", "^F"};

    /**
     * 单独实现字段名称直接对应,匹配上优先执行
     *
     * @return |
     */
    Regex[] replaceFirstMap() default {};

    /**
     * 是否对替换后的剩余名称首字母进行大写或小写处理
     * true:进行大写处理（默认）
     * false:进行大写处理
     *
     * @return |
     */
    boolean replaceFirstCapital() default true;

    /**
     * 注解
     *
     * @author lk
     * @version 1.0
     * @date 2019/10/2 15:01
     */
    public @interface Regex {

        /**
         * 排除变量或方法
         *
         * @return
         */
        String methodsName();

        /**
         * 执行替换
         *
         * @return
         */
        String[] regex();
    }

}


