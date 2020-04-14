package com.github.linkkou.databulid.javacode;

import com.squareup.javawriter.JavaWriter;
import com.sun.tools.javac.code.Symbol;
import org.springframework.stereotype.Component;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.*;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.util.EnumSet;
import java.util.List;

import static javax.lang.model.element.Modifier.PUBLIC;

public class CodeBulidClass {

    public static final String GENERATED_BY_SELMA = "GENERATED BY SELMA ";

    /**
     * 解析节点APT类
     */
    private ProcessingEnvironment processingEnv;
    /**
     * 包名称
     */
    private String packageName;
    /**
     * class 路径 - 这里是接口的路径名称
     */
    private String classpath;
    /**
     * class 文件名称 - 这里是类文件名称
     */
    private String classname;
    /**
     * 类的节点
     */
    private TypeElement type;

    /**
     * 该类下的所有方法
     */
    private List<ExecutableElement> elements;

    /**
     * 构建代码
     *
     * @param classtypeElement 接口类
     * @param elements         该类下的所有方法
     */
    public CodeBulidClass(ProcessingEnvironment processingEnv, TypeElement classtypeElement, List<ExecutableElement> elements) {
        //完整的class路,
        String classallpath = ((Symbol.ClassSymbol) classtypeElement).fullname.toString();
        this.processingEnv = processingEnv;
        this.type = processingEnv.getElementUtils().getTypeElement(classallpath);
        //package 路径
        this.packageName = getPackage(type).getQualifiedName().toString();
        //class 路径名称
        this.classpath = getClasspath(type.getQualifiedName().toString(), packageName);
        //class 文件名称
        this.classname = this.classpath.replace(".", "");
        //该类下的所有方法
        this.elements = elements;

    }


    public void createClass() throws IOException, IllegalAccessException, InstantiationException {
        JavaWriter writer = createSourceFile("Impl");
        if (writer != null) {
            createMethod(this.elements, writer, processingEnv);
            try {
                writer.endType();
                writer.close();
            } catch (Exception e) {

            }
        }
    }


    /**
     * 创建 class 输出路径
     *
     * @param newclassnamepex 后缀名称
     * @return
     */
    private JavaWriter createSourceFile(String newclassnamepex) {
        try {
            //重新构建新的class的文件名称
            newclassnamepex = packageName + '.' + classname + newclassnamepex;
            //获取此节点的对应的资源
            JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile(newclassnamepex, type);
            //Mac与widonw的路径不一样，目前测试只有IDEA下
            if (sourceFile.getName().contains("generated-sources" + System.getProperty("file.separator") + "annotations")) {
                JavaWriter writer = new JavaWriter(sourceFile.openWriter());
                return createClass(newclassnamepex, writer);
            }
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * 创建类型
     * * @param writer
     */
    private JavaWriter createClass(String newclassnamepex, JavaWriter writer) {
        try {
            writer.emitSingleLineComment(GENERATED_BY_SELMA);
            writer.emitPackage(this.packageName);
            writer.emitAnnotation(Component.class);
            //创建类
            writer.beginType(newclassnamepex, "class", EnumSet.of(PUBLIC), null, classpath);
            return writer;
        } catch (Exception e) {

        }
        return null;
    }


    /**
     * 创建方法
     *
     * @param mapperMethods
     * @param writer
     * @param processingEnv
     */
    private void createMethod(List<ExecutableElement> mapperMethods, JavaWriter writer, ProcessingEnvironment processingEnv) throws IOException, InstantiationException, IllegalAccessException {
        CodeBulidMethod buildAsbMethod = new CodeBulidMethod(mapperMethods, writer, processingEnv);
        buildAsbMethod.createMethod();
    }

    /**
     * 获取包名称
     *
     * @param type
     * @return
     */
    private PackageElement getPackage(Element type) {
        while (type.getKind() != ElementKind.PACKAGE) {
            type = type.getEnclosingElement();
        }
        return (PackageElement) type;
    }

    /**
     * 获取class路径名称
     *
     * @param type
     * @param packageName
     * @return
     */
    private String getClasspath(String type, String packageName) {
        return type.substring(packageName.isEmpty() ? 0 : packageName.length() + 1);
    }

}
