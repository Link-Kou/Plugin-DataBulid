package com.github.linkkou.databulid.annotation;


import org.springframework.stereotype.Repository;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
/**
 * @author LK
 * @version 1.0
 * @data 2017-12-27 21:35
 */
@Repository
public @interface Mappers {

}
