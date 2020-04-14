package com.github.linkkou.databulid.utils;

import com.github.linkkou.databulid.annotation.MapperImpl;
import com.sun.tools.javac.code.Attribute;
import com.sun.tools.javac.code.Scope;
import com.sun.tools.javac.code.Symbol;

import javax.lang.model.element.*;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 注解工具类
 *
 * @author lk
 * @version 1.0
 * @date 2019/10/2 18:08Regex
 */
public class AnnotationUtils {


    /**
     * 获取到方法上的 {@link com.github.linkkou.databulid.annotation.MapperImpl}
     *
     * @param mapperMethods 方法对象
     * @return Attribute.Class | null
     */
    public static Attribute.Class getMethodMapperImpl(ExecutableElement mapperMethods) {
        if (mapperMethods instanceof Symbol.MethodSymbol) {
            List<? extends AnnotationMirror> annotationMirrors = mapperMethods.getAnnotationMirrors();
            for (AnnotationMirror annotationMirror : annotationMirrors) {
                if (MapperImpl.class.getCanonicalName().equals(annotationMirror.getAnnotationType().asElement().toString())) {
                    Map<? extends ExecutableElement, ? extends AnnotationValue> elementValues = annotationMirror.getElementValues();
                    if (elementValues.size() > 0) {
                        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> meea : elementValues.entrySet()) {
                            if ("value".equals(meea.getKey().getSimpleName().toString())) {
                                if (meea.getValue() instanceof Attribute.Class) {
                                    return ((Attribute.Class) meea.getValue());
                                }
                            }
                        }
                    }
                }
            }
        }
        //默认值
        return null;
    }

    /**
     * 获取方法上所有的注解
     *
     * @param mapperMethods 方法对象
     * @return HashMap<String, AnnotationMirror> 注解名称,注解对象
     */
    public static HashMap<String, AnnotationMirror> getMethodAnnotation(ExecutableElement mapperMethods) {
        HashMap<String, AnnotationMirror> AnnotationMirrorMap = new HashMap<String, AnnotationMirror>(16);
        if (mapperMethods instanceof Symbol.MethodSymbol) {
            List<? extends AnnotationMirror> annotationMirrors = mapperMethods.getAnnotationMirrors();
            for (AnnotationMirror annotationMirror : annotationMirrors) {
                AnnotationMirrorMap.put(annotationMirror.getAnnotationType().asElement().toString(), annotationMirror);
            }
        }
        return AnnotationMirrorMap;
    }


    /**
     * 获取方法的参数上所有的注解
     *
     * @param variableElements 方法对象
     * @return HashMap<String, AnnotationMirror> 注解名称,注解对象
     */
    public static HashMap<String, AnnotationMirror> getParametersAnnotation(VariableElement variableElements) {
        HashMap<String, AnnotationMirror> AnnotationMirrorMap = new HashMap<String, AnnotationMirror>(16);
        List<? extends AnnotationMirror> annotationMirrors = variableElements.getAnnotationMirrors();
        //接口方法上的注解
        for (AnnotationMirror annotationMirror : annotationMirrors) {
            AnnotationMirrorMap.put(annotationMirror.getAnnotationType().asElement().toString(), annotationMirror);
        }
        return AnnotationMirrorMap;
    }


    /**
     * 批量获取所有注解与值（包含默认参数）
     *
     * @param mirrorHashMap
     * @return
     */
    public static List<HashMap<String, AnnotationValue>> getAnnotationParameters(HashMap<String, AnnotationMirror> mirrorHashMap) {
        final ArrayList<HashMap<String, AnnotationValue>> hashMapArrayList = new ArrayList<>();
        for (Map.Entry<String, AnnotationMirror> stringAnnotationMirrorEntry : mirrorHashMap.entrySet()) {
            hashMapArrayList.add(getAnnotationParameters(stringAnnotationMirrorEntry.getValue()));
        }
        return hashMapArrayList;
    }


    /**
     * 获取注解与值（包含默认参数）
     *
     * @param annotationMirror 对象
     * @return HashMap<String, AnnotationValue>  注解名称，注解值
     */
    public static HashMap<String, AnnotationValue> getAnnotationParameters(AnnotationMirror annotationMirror) {
        HashMap<String, AnnotationValue> annotationValueMap = new HashMap<String, AnnotationValue>(16);
        final Element element = annotationMirror.getAnnotationType().asElement();
        final Scope members_field = ((Symbol.ClassSymbol) element).members_field;
        for (Field field : members_field.getClass().getDeclaredFields()) {
            if ("table".equals(field.getName())) {
                field.setAccessible(true);
                try {
                    final Scope.Entry[] entries = (Scope.Entry[]) field.get(members_field);
                    for (Scope.Entry entry : entries) {
                        if (null != entry) {
                            if (entry.sym instanceof Symbol.MethodSymbol) {
                                final Symbol.MethodSymbol sym = (Symbol.MethodSymbol) entry.sym;
                                annotationValueMap.put(sym.name.toString(), sym.defaultValue);
                            }
                        }
                    }
                } catch (Exception e) {

                }
            }
        }
        Map<? extends ExecutableElement, ? extends AnnotationValue> elementValues = annotationMirror.getElementValues();
        if (elementValues.size() > 0) {
            for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> meea : elementValues.entrySet()) {
                final AnnotationValue value = meea.getValue();
                annotationValueMap.put(meea.getKey().getSimpleName().toString(), value);
            }
        }
        return annotationValueMap;
    }


    /**
     * 获取到 Bool
     *
     * @param annotationValue 对象
     * @return 失败 null
     */
    public static Boolean getAnnotationValueForBool(AnnotationValue annotationValue) {
        if (annotationValue.getValue() instanceof Boolean) {
            return (Boolean) annotationValue.getValue();
        }
        return null;
    }

    /**
     * 获取到 Integer
     *
     * @param annotationValue 对象
     * @return 失败 null
     */
    public static Integer getAnnotationValueForInt(AnnotationValue annotationValue) {
        if (annotationValue.getValue() instanceof Integer) {
            return (Integer) annotationValue.getValue();
        }
        return null;
    }


    /**
     * 获取到 Float
     *
     * @param annotationValue 对象
     * @return 失败 null
     */
    public static Float getAnnotationValueForFloat(AnnotationValue annotationValue) {
        if (annotationValue.getValue() instanceof Float) {
            return (Float) annotationValue.getValue();
        }
        return null;
    }

    /**
     * 获取到 byte
     *
     * @param annotationValue 对象
     * @return 失败 null
     */
    public static Byte getAnnotationValueForByte(AnnotationValue annotationValue) {
        if (annotationValue.getValue() instanceof Byte) {
            return (Byte) annotationValue.getValue();
        }
        return null;
    }


    /**
     * 获取到 Double
     *
     * @param annotationValue 对象
     * @return 失败 null
     */
    public static Double getAnnotationValueForDouble(AnnotationValue annotationValue) {
        if (annotationValue.getValue() instanceof Double) {
            return (Double) annotationValue.getValue();
        }
        return null;
    }

    /**
     * 获取到 Long
     *
     * @param annotationValue 对象
     * @return 失败 null
     */
    public static Long getAnnotationValueForLong(AnnotationValue annotationValue) {
        if (annotationValue.getValue() instanceof Long) {
            return (Long) annotationValue.getValue();
        }
        return null;
    }

    /**
     * 获取到 Short
     *
     * @param annotationValue 对象
     * @return 失败 null
     */
    public static Short getAnnotationValueForShort(AnnotationValue annotationValue) {
        if (annotationValue.getValue() instanceof Short) {
            return (Short) annotationValue.getValue();
        }
        return null;
    }

    /**
     * 获取到 String
     *
     * @param annotationValue 对象
     * @return 失败 null
     */
    public static String getAnnotationValueForString(AnnotationValue annotationValue) {
        return annotationValue.getValue().toString();
    }

    /**
     * 获取到注解对象
     *
     * @param annotationValue 对象
     * @return 失败 null
     */
    public static AnnotationMirror getAnnotationValueForAnnotation(AnnotationValue annotationValue) {
        if (annotationValue instanceof Attribute.Compound) {
            final Attribute.Compound value = (Attribute.Compound) annotationValue.getValue();
            return (AnnotationMirror) value;
        }
        return null;
    }


    /**
     * 获取到注解数组
     *
     * @param annotationValue 对象
     * @return 失败 null
     */
    public static List<Attribute> getAnnotationValueForArray(AnnotationValue annotationValue) {
        if (annotationValue instanceof Attribute.Array) {
            final Attribute.Array value = (Attribute.Array) annotationValue;
            return value.getValue();
        }
        return null;
    }

    /**
     * 获取到注解数组
     *
     * @param annotationValue 对象
     * @return 失败 null
     */
    public static Attribute.Class getAnnotationValueForClass(AnnotationValue annotationValue) {
        if (annotationValue instanceof Attribute.Class) {
            //final Symbol.TypeSymbol tsym = value.classType.tsym;
            return (Attribute.Class) annotationValue.getValue();
        }
        return null;
    }

    /**
     * 获取到 枚举
     *
     * @param annotationValue 对象
     * @return 失败 null
     */
    public static String getAnnotationValueForEnum(AnnotationValue annotationValue) {
        if (annotationValue.getValue() instanceof Attribute.Enum) {
            return (((Attribute.Enum) annotationValue.getValue()).value.toString());
        }
        return null;
    }


}
