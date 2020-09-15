package com.github.linkkou.databulid.utils;

import com.github.linkkou.databulid.annotation.MapperImpl;
import com.sun.tools.javac.code.Attribute;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import java.util.*;

/**
 * 方法工具
 *
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
     * 是否为Void返回
     *
     * @param typeMirror 对象
     * @return boolean
     */
    public static boolean isVoid(TypeMirror typeMirror) {
        return typeMirror instanceof Type.JCVoidType;
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

    /**
     * 方法是否是默认接口
     *
     * @return
     */
    public static boolean isDefault(ExecutableElement executableElement) {
        return executableElement.getModifiers().contains(Modifier.DEFAULT);
    }

    /**
     * 过滤 ABSTRACT DEFAULT 类型方法
     *
     * @return
     */
    public static List<ExecutableElement> getMethodModifiers(ProcessingEnvironment processingEnv, TypeElement classtypeElement) {
        //过滤所有的系统的Methods
        List<? extends Element> allMembers = processingEnv.getElementUtils().getAllMembers(classtypeElement);
        List<? extends Element> methods = ElementFilter.methodsIn(allMembers);
        List<ExecutableElement> elements = new ArrayList<>();
        //所有的方法
        for (Element method : methods) {
            ExecutableElement executableElement = (ExecutableElement) method;
            final Set<Modifier> modifiers = executableElement.getModifiers();
            if (modifiers.contains(Modifier.ABSTRACT) || modifiers.contains(Modifier.DEFAULT)) {
                //方法名称是存在重复的情况。由ide工具会做出限制，所有不会相同方法名称并且参数一样的方法
                elements.add(executableElement);
            }
        }
        return elements;
    }


}
