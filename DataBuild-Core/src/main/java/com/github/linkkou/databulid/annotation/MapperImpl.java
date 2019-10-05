package com.github.linkkou.databulid.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lk
 * @version 1.0
 * @date 2019/10/2 18:09
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.SOURCE)
public @interface MapperImpl {
    Class value();
}
