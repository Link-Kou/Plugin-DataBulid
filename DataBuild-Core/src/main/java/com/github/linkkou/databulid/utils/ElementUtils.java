package com.github.linkkou.databulid.utils;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author lk
 * @version 1.0
 * @date 2019/12/20 14:34
 */
public class ElementUtils {

    /**
     * 查询所有带有指定注解的接口}
     *
     * @param roundEnv
     * @return
     */
    public static List<Element> findAnnoationElement(RoundEnvironment roundEnv,Class<? extends Annotation> a) {
        List<Element> targetClassMap = new ArrayList<>();
        //找到所有跟AnDataCollect注解相关元素
        Collection<? extends Element> anLogSet = roundEnv.getElementsAnnotatedWith(a);
        //遍历所有元素
        for (Element e : anLogSet) {
            //接口类型
            if (e.getKind() != ElementKind.INTERFACE) {
                continue;
            }
            //对类做一个缓存
            targetClassMap.add(e);
        }
        return targetClassMap;
    }

}
