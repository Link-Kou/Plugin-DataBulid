package com.github.linkkou.databulid;

import com.github.linkkou.databulid.annotation.Mappers;
import com.github.linkkou.databulid.javacode.CodeBulidClass;
import com.github.linkkou.databulid.utils.ElementUtils;
import com.github.linkkou.databulid.utils.MethodUtils;
import com.sun.source.util.Trees;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Name;
import org.reflections.Reflections;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.util.*;

/**
 * @author LK
 * @date 2018-05-31 10:46
 */
@SupportedAnnotationTypes({"com.github.linkkou.databulid.annotation.Mappers"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class DataBulidProcessor extends AbstractProcessor {

    private Trees trees;

    private TreeMaker make;

    private Context context;

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
        trees = Trees.instance(processingEnv);
        context = ((JavacProcessingEnvironment)
                processingEnv).getContext();
        make = TreeMaker.instance(context);
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
            try {
                //从注解中找到所有的类,以及对应的注解的方法
                findMethodsElement(roundEnv);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }


    /**
     * 查询所有带有 {@link Mappers 接口里面的方法}
     *
     * @param roundEnv
     */
    private void findMethodsElement(RoundEnvironment roundEnv) throws IOException, InstantiationException, IllegalAccessException {
        //从注解中找到所有的类,以及对应的注解的方法
        List<Element> targetClassMap = ElementUtils.findAnnoationElement(roundEnv, Mappers.class);
        for (Element item : targetClassMap) {
            TypeElement classtypeElement = (TypeElement) item;
            List<ExecutableElement> elements = MethodUtils.getMethodModifiers(processingEnv, classtypeElement);
            new CodeBulidClass(processingEnv, classtypeElement, elements).createClass();
            JCTree tree = (JCTree) trees.getTree(item);
            TreeTranslator visitor = new Inliner();
            tree.accept(visitor);
        }
    }

    private class Inliner extends TreeTranslator {

        @Override
        public void visitClassDef(JCTree.JCClassDecl jcClassDecl) {
            super.visitClassDef(jcClassDecl);

            List<JCTree.JCAnnotation> jcAnnotations = new ArrayList<>();
            for (JCTree.JCAnnotation annotation : jcClassDecl.mods.annotations) {
                if (!annotation.attribute.type.toString().equals(Mappers.class.getName())) {
                    jcAnnotations.add(annotation);
                }
            }
            //去除@Validated，防止重复的扫描
            jcClassDecl.mods.annotations = com.sun.tools.javac.util.List.from(jcAnnotations);
            final JCTree.JCClassDecl jcClassDecl1 = make.ClassDef(
                    jcClassDecl.getModifiers(),
                    jcClassDecl.getSimpleName(),
                    jcClassDecl.getTypeParameters(),
                    jcClassDecl.getExtendsClause(),
                    jcClassDecl.getImplementsClause(),
                    jcClassDecl.getMembers()
            );
            this.result = jcClassDecl1;
        }

    }

}