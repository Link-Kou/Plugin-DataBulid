package com.github.linkkou.databulid.extension.asinglefield.annotation;

import com.github.linkkou.databulid.utils.AnnotationUtils;
import com.sun.tools.javac.code.Attribute;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.VariableElement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lk
 * @version 1.0
 * @date 2019/10/5 16:03
 */
public class RegexsParsing {

    /**
     * 解析{@link Regexs}注解
     *
     * @param methodParameter
     */
    public static RegexsEntity getRegexs(VariableElement methodParameter) {
        final HashMap<String, AnnotationMirror> parametersAnnotation = AnnotationUtils.getParametersAnnotation(methodParameter);
        final AnnotationMirror annotationMirror = parametersAnnotation.get(Regexs.class.getName());
        if (null != annotationMirror) {
            RegexsEntity regexsEntity = new RegexsEntity();
            final HashMap<String, AnnotationValue> annotationParameters1 = AnnotationUtils.getAnnotationParameters(annotationMirror);
            for (Map.Entry<String, AnnotationValue> valueEntry : annotationParameters1.entrySet()) {
                if ("replaceFirst".equals(valueEntry.getKey())) {
                    final List<Attribute> annotationValueForArray = AnnotationUtils.getAnnotationValueForArray(valueEntry.getValue());
                    final String[] strings = annotationValueForArray.stream().map((x) -> {
                        return (String) (((Attribute.Constant) x).value);
                    }).toArray(String[]::new);
                    regexsEntity.setReplaceFirst(strings);
                }
                if ("replaceFirstMap".equals(valueEntry.getKey())) {
                    final List<Attribute> annotationValueForArray = AnnotationUtils.getAnnotationValueForArray(valueEntry.getValue());
                    final RegexsEntity.RegexEntity[] regexEntities = annotationValueForArray.stream().map(x -> {
                        final AnnotationMirror annotationValueForAnnotation = AnnotationUtils.getAnnotationValueForAnnotation((AnnotationValue) x.getValue());
                        final HashMap<String, AnnotationValue> annotationParameters = AnnotationUtils.getAnnotationParameters(annotationValueForAnnotation);
                        RegexsEntity.RegexEntity regexEntity = new RegexsEntity.RegexEntity();
                        for (Map.Entry<String, AnnotationValue> stringAnnotationValueEntry : annotationParameters.entrySet()) {
                            if ("methodsName".equals(stringAnnotationValueEntry.getKey())) {
                                regexEntity.setMethodsName(AnnotationUtils.getAnnotationValueForString(stringAnnotationValueEntry.getValue()));
                            }
                            if ("regex".equals(stringAnnotationValueEntry.getKey())) {
                                final List<Attribute> annotationValueForArray1 = AnnotationUtils.getAnnotationValueForArray(stringAnnotationValueEntry.getValue());
                                final String[] strings = annotationValueForArray1.stream().map((x1) -> {
                                    return (String) (((Attribute.Constant) x1).value);
                                }).toArray(String[]::new);
                                regexEntity.setRegex(strings);
                            }
                        }
                        return regexEntity;
                    }).toArray(RegexsEntity.RegexEntity[]::new);
                    regexsEntity.setReplaceFirstMap(regexEntities);
                }
                if ("replaceFirstCapital".equals(valueEntry.getKey())) {
                    final Boolean annotationValueForBool = AnnotationUtils.getAnnotationValueForBool(valueEntry.getValue());
                    regexsEntity.setReplaceFirstCapital(annotationValueForBool);
                }
            }
            return regexsEntity;
        }
        return null;
    }
}
