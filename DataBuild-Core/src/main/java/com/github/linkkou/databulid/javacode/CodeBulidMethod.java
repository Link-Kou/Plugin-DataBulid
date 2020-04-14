package com.github.linkkou.databulid.javacode;

import com.github.linkkou.databulid.spi.DataBuildSpi;
import com.github.linkkou.databulid.utils.AnnotationUtils;
import com.github.linkkou.databulid.utils.MethodUtils;
import com.github.linkkou.databulid.utils.ParametersUtils;
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
    public void createMethod() throws IOException, IllegalAccessException, InstantiationException {
        for (ExecutableElement executableElement : mapperMethods) {
            //获取到所有的方法的参数(自动会导入指定的架包)
            List<? extends VariableElement> parameters = executableElement.getParameters();
            List<String> parameterlist = ParametersUtils.getParameters(parameters);
            String[] parameterarray = new String[parameterlist.size()];
            writer.emitAnnotation(Override.class);
            writer.beginMethod(executableElement.getReturnType().toString(),
                    executableElement.getSimpleName().toString(),
                    EnumSet.of(PUBLIC),
                    //单数 参数类型 双数 参数名称
                    parameterlist.toArray(parameterarray));
            //构建方法内的代码
            CodeBulidCode codeBulidCode = new CodeBulidCode(writer, this.processingEnv);
            codeBulidCode.createCode(executableElement, parameterlist);
            //createCode(executableElement);
            writer.endMethod();
            writer.emitEmptyLine();
        }
    }
}
