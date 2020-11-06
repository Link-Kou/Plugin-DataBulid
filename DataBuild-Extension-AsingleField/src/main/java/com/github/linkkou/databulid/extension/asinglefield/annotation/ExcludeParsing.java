package com.github.linkkou.databulid.extension.asinglefield.annotation;

import com.github.linkkou.databulid.utils.AnnotationUtils;
import com.sun.tools.javac.code.Attribute;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.VariableElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 排除相关方法
 * @author lk
 * @version 1.0
 * @date 2020/9/15 08:51
 */
public class ExcludeParsing {

    public static List<String> getExclude(VariableElement methodParameter) {
        final HashMap<String, AnnotationMirror> parametersAnnotation = AnnotationUtils.getParametersAnnotation(methodParameter);
        final AnnotationMirror annotationMirror = parametersAnnotation.get(Exclude.class.getName());
        return getExclude(annotationMirror);
    }

    /**
     * 注解解析
     *
     * @param annotationMirror 注解对象
     * @return list
     */
    private static List<String> getExclude(final AnnotationMirror annotationMirror) {
        if (null != annotationMirror) {
            final HashMap<String, AnnotationValue> annotationParameters1 = AnnotationUtils.getAnnotationParameters(annotationMirror);
            for (Map.Entry<String, AnnotationValue> valueEntry : annotationParameters1.entrySet()) {
                if ("methodsName".equals(valueEntry.getKey())) {
                    final List<Attribute> annotationValueForArray = AnnotationUtils.getAnnotationValueForArray(valueEntry.getValue());
                    final List<String> collect = annotationValueForArray.stream().map((x) -> {
                        return (String) (((Attribute.Constant) x).value);
                    }).collect(Collectors.toList());
                    return collect;
                }
            }
        }
        return new ArrayList<>();
    }
}
