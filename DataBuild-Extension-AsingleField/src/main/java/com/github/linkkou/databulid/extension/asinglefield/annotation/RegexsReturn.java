package com.github.linkkou.databulid.extension.asinglefield.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解为核心注解,默认可以不写
 * 不写将采用默认
 *
 * @author lk
 * @version 1.0
 * @date 2019/10/2 15:01
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.SOURCE)
public @interface RegexsReturn {

    /**
     * 返回参数,进行所有方法进行首字母去除
     * <p>
     * 不写 Regexs 注解情况下，参数默认采用(set)开头
     *
     * @return |
     */
    String[] replaceFirst() default {};

    /**
     * 单独实现字段名称处理
     * 如果方法写入而且匹配到了,replaceFirst将不再执行
     * 与 replaceFirst 是二选一的关系
     * @return |
     */
    Regex[] replaceFirstMap() default {};

    /**
     * 是否对替换后的剩余名称首字母进行大写或小写处理
     * true:进行大写处理（默认）
     * false:不进行大写处理
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
         * 匹配的方法名称
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


