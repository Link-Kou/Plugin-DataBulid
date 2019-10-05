package com.github.linkkou.databulid.utils;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

/**
 * 参数工具类
 *
 * @author lk
 * @version 1.0
 * @date 2019/10/2 18:08Regex
 */
public class ParametersUtils {


    /**
     * 获取到参数的类型
     *
     * @param variableElement 方法对象
     * @return Type.ClassType
     */
    public static TypeMirror getParametersType(VariableElement variableElement) {
        return variableElement.asType();
    }


    /**
     * 获取参数的名称
     *
     * @param variableElement 方法对象
     * @return Type.ClassType
     */
    public static String getParametersName(VariableElement variableElement) {
        return variableElement.getSimpleName().toString();
    }


}
