package com.github.linkkou.databulid.javacode;

import com.github.linkkou.databulid.impl.DefaultCode;
import com.github.linkkou.databulid.impl.DefaultCodeImpl;
import com.github.linkkou.databulid.spi.DataBuildSpi;
import com.github.linkkou.databulid.utils.AnnotationUtils;
import com.github.linkkou.databulid.utils.MethodUtils;
import com.squareup.javawriter.JavaWriter;
import com.sun.tools.javac.code.Attribute;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static javax.lang.model.element.Modifier.PUBLIC;

public class CodeBulidCode extends CodeBulidExtension {

    private JavaWriter writer;

    private ProcessingEnvironment processingEnv;


    /**
     * @param writer
     * @param processingEnv
     */
    public CodeBulidCode(JavaWriter writer, ProcessingEnvironment processingEnv) {
        this.writer = writer;
        this.processingEnv = processingEnv;
    }


    /**
     * 构建方法内的代码
     *
     * @param executableElement 一个类里面单一的方法
     */
    public void createCode(ExecutableElement executableElement, final List<String> parameterlist) throws IOException, InstantiationException, IllegalAccessException {
        final Attribute.Class methodMapperImpl = AnnotationUtils.getMethodMapperImpl(executableElement);
        String code1 = null;
        DefaultCode defaultCode = createDefaultCode(executableElement, parameterlist);
        if (null != methodMapperImpl) {
            final DataBuildSpi dataBuildSpi = super.getDataBuildSpi(methodMapperImpl);
            if (null != dataBuildSpi) {
                code1 = dataBuildSpi.getCode(defaultCode, executableElement, processingEnv);
            }
        } else {
            final DataBuildSpi dataBuildCustomAnnoSpi = super.getDataBuildCustomAnnoSpi(AnnotationUtils.getMethodAnnotation(executableElement));
            if (null != dataBuildCustomAnnoSpi) {
                code1 = dataBuildCustomAnnoSpi.getCode(defaultCode, executableElement, processingEnv);
            } else {
                writer.emitSingleLineComment("方法上面没有可以用到的@MapperImpl注解或指定实现的注解@MapperConfig");
            }
        }
        if (null == code1 || 1 > code1.length()) {
            if (defaultCode.isDefault()) {
                code1 = defaultCode.getSuper();
            } else {
                code1 = "return null";
            }
        }
        writer.emitStatement(code1);
    }

    /**
     * 创建默认接口的代码
     *
     * @param executableElement 方法
     * @param parameterlist     参数
     */
    private DefaultCode createDefaultCode(ExecutableElement executableElement, final List<String> parameterlist) {
       /* String code = "";
        if (MethodUtils.isDefault(executableElement)) {
            String classname = ((Symbol.MethodSymbol) executableElement).owner.name.toString();
            String parameterarray = IntStream.range(0, parameterlist.size()).filter(x -> {
                //是奇数
                return (x & 1) == 1;
            }).mapToObj(x -> parameterlist.get(x)).collect(Collectors.joining(","));
            code = String.format("%s.super.%s(%s);", classname, executableElement.getSimpleName().toString(), parameterarray);
        }
        return code;*/
        return new DefaultCodeImpl(executableElement, parameterlist);
    }
}
