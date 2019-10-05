package com.github.linkkou.databulid.extension.asinglefield;

import com.github.linkkou.databulid.extension.asinglefield.entity.ParametersEntity;
import com.github.linkkou.databulid.extension.asinglefield.parsing.ParsingParameters;
import com.github.linkkou.databulid.extension.asinglefield.parsing.ParsingReturn;
import com.github.linkkou.databulid.spi.DataBuildSpi;
import com.github.linkkou.databulid.utils.AnnotationUtils;
import com.github.linkkou.databulid.utils.ClassUtils;
import com.github.linkkou.databulid.utils.MethodUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author lk
 * @version 1.0
 * @date 2019/10/2 15:01
 */
public class BulidAsingleField implements DataBuildSpi {


    private StringBuilder stringBuilder = new StringBuilder();

    private ExecutableElement executableElement;

    private ProcessingEnvironment processingEnv;


    private static final String SETTER_FORMAT = "set(.*)";

    private static final Pattern SETTER_PATTERN = Pattern.compile(SETTER_FORMAT);

    @Override
    public String getCode(ExecutableElement executableElement, ProcessingEnvironment processingEnv) {
        this.executableElement = executableElement;
        this.processingEnv = processingEnv;
        final ParametersEntity parametersEntity = parsingReturn();
        final ParametersEntity parametersEntity1 = parsingParameters();
        parametersEntity.getVariableMethodParameterList().stream().forEach(x -> {
            parametersEntity1.getVariableMethodParameterList().stream().forEach(x1 -> {
                if (x.getMatchingMethodName().equals(x1.getMatchingMethodName())) {
                    stringBuilder.append(String.format("%s.%s(%s.%s());", parametersEntity.getName(), x.getOriginalMethodName(), parametersEntity1.getName(), x1.getOriginalMethodName()));
                }
            });
        });
        stringBuilder.append(String.format("return %s", parametersEntity.getName()));
        return stringBuilder.toString();
    }


    /**
     * 获取方法返回的对象
     */
    private ParametersEntity parsingReturn() {
        ParsingReturn parsingReturn = new ParsingReturn(executableElement, processingEnv, stringBuilder);
        return parsingReturn.parsing();
    }


    private ParametersEntity parsingParameters() {
        ParsingParameters parsingParameters = new ParsingParameters(executableElement, processingEnv);
        return parsingParameters.parsing();
    }

}
