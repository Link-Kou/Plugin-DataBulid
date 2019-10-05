package com.github.linkkou.databulid.javacode;


import com.github.linkkou.databulid.spi.DataBuildSpi;
import com.sun.tools.javac.code.Attribute;
import org.reflections.Reflections;

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


}
