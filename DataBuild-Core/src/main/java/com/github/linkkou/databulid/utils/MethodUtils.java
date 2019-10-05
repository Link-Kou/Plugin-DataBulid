package com.github.linkkou.databulid.utils;

import com.github.linkkou.databulid.annotation.MapperImpl;
import com.sun.tools.javac.code.Attribute;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 方法工具
 * @author lk
 * @version 1.0
 * @date 2019/10/2 18:08Regex
 */
public class MethodUtils {


    /**
     * 获取到方法上的返回对象
     *
     * @param mapperMethods 方法对象
     * @return Type.ClassType
     */
    public static TypeMirror getMethodReturn(ExecutableElement mapperMethods) {
        final TypeMirror returnType = mapperMethods.getReturnType();
        return returnType;
    }

    /**
     * 获取到方法上的参数
     *
     * @param mapperMethods 方法对象
     * @return Attribute.Class | null
     */
    public static List<? extends VariableElement> getMethodParameters(ExecutableElement mapperMethods) {
        List<? extends VariableElement> parameters = mapperMethods.getParameters();
        return parameters;
    }




}
