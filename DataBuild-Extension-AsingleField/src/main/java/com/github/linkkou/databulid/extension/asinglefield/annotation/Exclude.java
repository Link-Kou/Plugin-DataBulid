package com.github.linkkou.databulid.extension.asinglefield.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 排除
 *
 * @author lk
 * @version 1.0
 * @date 2020/9/15 08:49
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.SOURCE)
public @interface Exclude {
    /**
     * 排除的方法
     *
     * @return 数组
     */
    String[] methodsName() default "";
}
