package com.github.linkkou.databulid.javacode;

import com.github.linkkou.databulid.spi.DataBuildSpi;
import com.github.linkkou.databulid.utils.AnnotationUtils;
import com.squareup.javawriter.JavaWriter;
import com.sun.tools.javac.code.Attribute;
import com.sun.tools.javac.code.Type;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import static javax.lang.model.element.Modifier.PUBLIC;

public class CodeBulidMethod extends CodeBulidExtension {

    /**
     * 所有符合要求的方法
     */
    private List<ExecutableElement> mapperMethods;

    private JavaWriter writer;

    private ProcessingEnvironment processingEnv;


    /**
     * @param mapperMethods 需要导入的方法
     * @param writer
     * @param processingEnv
     */
    public CodeBulidMethod(List<ExecutableElement> mapperMethods, JavaWriter writer, ProcessingEnvironment processingEnv) {
        this.mapperMethods = mapperMethods;
        this.writer = writer;
        this.processingEnv = processingEnv;
    }

    /**
     * 创建方法
     */
    public void createMethod() {
        for (ExecutableElement executableElement : mapperMethods) {
            try {
                //获取到所有的方法的参数(自动会导入指定的架包)
                List<? extends VariableElement> parameters = executableElement.getParameters();
                List<String> parameterlist = new ArrayList<>();
                //HashMap<String, String> parametermap = new HashMap<>();
                for (VariableElement variableElement : parameters) {
                    if (variableElement.asType() instanceof Type.AnnotatedType) {
                        //类型
                        parameterlist.add(((Type.AnnotatedType) variableElement.asType()).tsym.toString());
                    } else {
                        //类型
                        parameterlist.add(variableElement.asType().toString());
                    }
                    //名称
                    parameterlist.add(variableElement.getSimpleName().toString());
                    //parametermap.put(variableElement.getSimpleName().toString(), variableElement.asType().toString());
                }
                String[] parameterarray = new String[parameterlist.size()];
                writer.emitAnnotation(Override.class);
                writer.beginMethod(executableElement.getReturnType().toString(),
                        executableElement.getSimpleName().toString(),
                        EnumSet.of(PUBLIC),
                        //单数 参数类型 双数 参数名称
                        parameterlist.toArray(parameterarray));
                //构建方法内的代码
                createCode(executableElement);
                writer.endMethod();
                writer.emitEmptyLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 构建方法内的代码
     *
     * @param executableElement 一个类里面单一的方法
     */
    private void createCode(ExecutableElement executableElement) {
        final Attribute.Class methodMapperImpl = AnnotationUtils.getMethodMapperImpl(executableElement);
        String code1 = null;
        try {
            if (null != methodMapperImpl) {
                final DataBuildSpi dataBuildSpi = super.getDataBuildSpi(methodMapperImpl);
                if (null != dataBuildSpi) {
                    code1 = dataBuildSpi.getCode(executableElement, processingEnv);
                }
            } else {
                writer.emitSingleLineComment("方法上面没有可以用到的MapperImpl注解");
            }
            if (null == code1 || 1 > code1.length()) {
                code1 = "return null";
            }
            writer.emitStatement(code1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
