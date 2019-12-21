package com.github.linkkou.databulid.utils;

import com.sun.tools.javac.code.Type;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * 获取参数名称以及类型
     *
     * @param parameters
     * @return
     */
    public static List<String> getParameters(List<? extends VariableElement> parameters) {
        List<String> parameterlist = new ArrayList<>();
        //HashMap<String, String> parametermap = new HashMap<>();
        for (VariableElement variableElement : parameters) {
            if (variableElement.asType() instanceof Type.AnnotatedType) {
                //类型
                parameterlist.add(((Type.AnnotatedType) variableElement.asType()).tsym.toString());
            } else {
                //类型
                parameterlist.add(variableElement.asType().toString());
            }
            //名称
            parameterlist.add(variableElement.getSimpleName().toString());
        }
        return parameterlist;
    }


}
