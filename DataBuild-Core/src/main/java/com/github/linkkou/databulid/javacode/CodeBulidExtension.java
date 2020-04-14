package com.github.linkkou.databulid.javacode;


import com.github.linkkou.databulid.annotation.MapperConfig;
import com.github.linkkou.databulid.spi.DataBuildSpi;
import com.sun.tools.javac.code.Attribute;
import org.reflections.Reflections;

import javax.lang.model.element.AnnotationMirror;
import java.util.HashMap;
import java.util.Set;

abstract class CodeBulidExtension {

    //TODO 异常打包报错的问题,不影响编译
    private static Reflections reflections = new Reflections();

    protected synchronized DataBuildSpi getDataBuildSpi(Attribute.Class type) throws IllegalAccessException, InstantiationException {

        final Set<Class<? extends DataBuildSpi>> subTypesOf = reflections.getSubTypesOf(DataBuildSpi.class);
        for (Class<? extends DataBuildSpi> aClass : subTypesOf) {
            if (aClass.getName().equals(type.classType.toString())) {
                final DataBuildSpi dataBuildSpi = aClass.newInstance();
                return dataBuildSpi;
            }
        }
        return null;
    }

    protected synchronized DataBuildSpi getDataBuildCustomAnnoSpi(final HashMap<String, AnnotationMirror> v) throws IllegalAccessException, InstantiationException {
        if (v.size() > 0) {
            final Set<Class<? extends DataBuildSpi>> subTypesOf = reflections.getSubTypesOf(DataBuildSpi.class);
            for (Class<? extends DataBuildSpi> aClass : subTypesOf) {
                final MapperConfig annotation = aClass.getAnnotation(MapperConfig.class);
                if (null != annotation) {
                    if (v.containsKey(annotation.value().getName())) {
                        final DataBuildSpi dataBuildSpi = (DataBuildSpi) aClass.newInstance();
                        return dataBuildSpi;
                    }
                }
            }
        }
        return null;
    }


}
