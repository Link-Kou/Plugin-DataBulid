package com.github.linkkou.databulid;

import com.github.linkkou.databulid.annotation.Mappers;
import com.github.linkkou.databulid.javacode.CodeBulidClass;
import com.github.linkkou.databulid.utils.ElementUtils;
import com.github.linkkou.databulid.utils.MethodUtils;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * @author LK
 * @date 2018-05-31 10:46
 */
@SupportedAnnotationTypes({"com.github.linkkou.databulid.annotation.Mappers"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class DataBulidProcessor extends AbstractProcessor {


    /**
     * 方法名称
     * 方法对象
     */
    private final HashMap<String, List<ExecutableElement>> remainingMapperTypes = new HashMap<String, List<ExecutableElement>>();

    /**
     * 初始化
     *
     * @param processingEnv
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    /**
     * 依据相关注解解析
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(Mappers.class.getCanonicalName());
    }

    /**
     * {@inheritDoc}
     *
     * @param annotations
     * @param roundEnv
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (annotations.size() > 0) {
            //从注解中找到所有的类,以及对应的注解的方法
            findMethodsElement(roundEnv);
        }
        return true;
    }


    /**
     * 查询所有带有 {@link Mappers 接口里面的方法}
     *
     * @param roundEnv
     */
    private void findMethodsElement(RoundEnvironment roundEnv) {
        //从注解中找到所有的类,以及对应的注解的方法
        List<Element> targetClassMap = ElementUtils.findAnnoationElement(roundEnv, Mappers.class);
        for (Element item : targetClassMap) {
            TypeElement classtypeElement = (TypeElement) item;
            List<ExecutableElement> elements = MethodUtils.getMethodModifiers(processingEnv, classtypeElement);
            new CodeBulidClass(processingEnv, classtypeElement, elements).createClass();
        }
    }

}