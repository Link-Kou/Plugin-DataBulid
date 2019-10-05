package com.github.linkkou.databulid.utils;

import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * 类工具
 *
 * @author lk
 * @version 1.0
 * @date 2019/10/2 18:08Regex
 */
public class ClassUtils {


    /**
     * 获取class类型
     *
     * @param typeMirror 方法对象
     * @return Type.ClassType
     */
    public static Type.ClassType getClassType(TypeMirror typeMirror) {
        Type.ClassType returnType = (Type.ClassType) typeMirror;
        return returnType;
    }

    /**
     * 获取class的完整路径
     *
     * @param typeMirror 方法对象
     * @return Type.ClassType
     */
    public static String getClassTypePath(TypeMirror typeMirror) {
        final Type.ClassType classType = getClassType(typeMirror);
        return classType.tsym.type.toString();
    }

    /**
     * 获取泛型类
     *
     * @param typeMirror 方法对象
     */
    public static List<Type> getClassGenerics(TypeMirror typeMirror) {
        final Type.ClassType classType = getClassType(typeMirror);
        final List<Type> typeArguments = classType.getTypeArguments();
        return typeArguments;
    }

    /**
     * 是否为泛型
     *
     * @param typeMirror 方法对象
     */
    public static boolean isClassGenerics(TypeMirror typeMirror) {
        final List<Type> classGenerics = getClassGenerics(typeMirror);
        return classGenerics.size() > 0;
    }


    /**
     * 获取到类的所有的公开的方法
     *
     * @param processingEnv 解析
     * @param classType     类
     * @return
     */
    public static ArrayList<ExecutableElement> getClassAllMembersByPublic(ProcessingEnvironment processingEnv, Type.ClassType classType) {
        final ArrayList<ExecutableElement> executableElements = new ArrayList<ExecutableElement>();
        final TypeElement typeElement = processingEnv.getElementUtils().getTypeElement(classType.tsym.toString());
        if (typeElement != null) {
            java.util.List<? extends Element> allMembers = processingEnv.getElementUtils().getAllMembers(typeElement);
            java.util.List<? extends Element> methods = ElementFilter.methodsIn(allMembers);
            for (Element method : methods) {
                ExecutableElement executableElement = (ExecutableElement) method;
                if (executableElement.getModifiers().contains(Modifier.PUBLIC)) {
                    executableElements.add(executableElement);
                }
            }
        }
        return executableElements;
    }

    /**
     * 获取到类的所有的方法
     *
     * @param processingEnv 解析
     * @param classType     类
     * @return
     */
    public static ArrayList<ExecutableElement> getClassAllMembers(ProcessingEnvironment processingEnv, Type.ClassType classType) {
        final ArrayList<ExecutableElement> executableElements = new ArrayList<ExecutableElement>();
        final TypeElement typeElement = processingEnv.getElementUtils().getTypeElement(classType.tsym.toString());
        if (typeElement != null) {
            java.util.List<? extends Element> allMembers = processingEnv.getElementUtils().getAllMembers(typeElement);
            java.util.List<? extends Element> methods = ElementFilter.methodsIn(allMembers);
            for (Element method : methods) {
                ExecutableElement executableElement = (ExecutableElement) method;
                executableElements.add(executableElement);
            }
        }
        return executableElements;
    }


    /**
     * 获取到类的所有的公开的方法并且更具正则表达式匹配
     *
     * @param processingEnv 解析
     * @param classType     类型
     * @param pattern       表达式
     * @return
     */
    public static ArrayList<ExecutableElement> getClassAllMembersByPublicAndName(ProcessingEnvironment processingEnv, Type.ClassType classType, Pattern pattern) {
        final ArrayList<ExecutableElement> executableElements = new ArrayList<ExecutableElement>();
        final TypeElement typeElement = processingEnv.getElementUtils().getTypeElement(classType.tsym.toString());
        if (typeElement != null) {
            java.util.List<? extends Element> allMembers = processingEnv.getElementUtils().getAllMembers(typeElement);
            java.util.List<? extends Element> methods = ElementFilter.methodsIn(allMembers);
            for (Element method : methods) {
                ExecutableElement executableElement = (ExecutableElement) method;
                if (executableElement.getModifiers().contains(Modifier.PUBLIC)) {
                    //判断方法的参数数量 executableElement.getParameters().size() == parameters
                    if (pattern.matcher(executableElement.getSimpleName()).matches()) {
                        executableElements.add(executableElement);
                    }
                }
            }
        }
        return executableElements;
    }


}
