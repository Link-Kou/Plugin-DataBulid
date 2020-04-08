package com.github.linkkou.databulid.javacode;


import com.github.linkkou.databulid.annotation.MapperConfig;
import com.github.linkkou.databulid.spi.DataBuildSpi;
import com.sun.tools.javac.code.Attribute;
import org.reflections.Reflections;

import javax.lang.model.element.AnnotationMirror;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

abstract class CodeBulidExtension {
    /**
     * 谷歌元反射
     */
    private static Reflections reflections = new Reflections();

    protected DataBuildSpi getDataBuildSpi(Attribute.Class type) {
        final Set<Class<? extends DataBuildSpi>> subTypesOf = reflections.getSubTypesOf(DataBuildSpi.class);
        for (Class<? extends DataBuildSpi> aClass : subTypesOf) {
            if (aClass.getName().equals(type.classType.toString())) {
                try {
                    final DataBuildSpi dataBuildSpi = aClass.newInstance();
                    return dataBuildSpi;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    protected DataBuildSpi getDataBuildCustomAnnoSpi(HashMap<String, AnnotationMirror> v) {
        final Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(MapperConfig.class);
        final Iterator<Class<?>> iterator = typesAnnotatedWith.iterator();
        while (iterator.hasNext()) {
            final Class<?> next = iterator.next();
            final MapperConfig annotation = next.getAnnotation(MapperConfig.class);
            if (null != annotation) {
                if (v.containsKey(annotation.value().getName())) {
                    try {
                        final DataBuildSpi dataBuildSpi = (DataBuildSpi) next.newInstance();
                        return dataBuildSpi;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }


}
