package com.github.linkkou.databulid;

import com.github.linkkou.databulid.javacode.CodeBulidClass;
import com.github.linkkou.databulid.annotation.Mappers;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.util.ElementFilter;
import java.util.*;

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
        List<Element> targetClassMap = findAnnoationElement(roundEnv);
        for (Element item : targetClassMap) {
            TypeElement classtypeElement = (TypeElement) item;
            //过滤所有的系统的Methods
            List<? extends Element> allMembers = processingEnv.getElementUtils().getAllMembers(classtypeElement);
            List<? extends Element> methods = ElementFilter.methodsIn(allMembers);
            List<ExecutableElement> elements = new ArrayList<>();
            //所有的方法
            for (Element method : methods) {
                ExecutableElement executableElement = (ExecutableElement) method;
                if (executableElement.getModifiers().contains(Modifier.ABSTRACT)) {
                    //方法名称是存在重复的情况。ide工具会做出限制，所有不会相同方法名称并且参数一样的方法
                    elements.add(executableElement);
                }
            }
            new CodeBulidClass(processingEnv, classtypeElement, elements).createClass();
        }
    }


    /**
     * 查询所有带有{@link Mappers 的注解的接口}
     *
     * @param roundEnvironment
     * @return
     */
    private List<Element> findAnnoationElement(RoundEnvironment roundEnvironment) {
        List<Element> targetClassMap = new ArrayList<>();
        //找到所有跟AnDataCollect注解相关元素
        Collection<? extends Element> anLogSet = roundEnvironment.getElementsAnnotatedWith(Mappers.class);
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