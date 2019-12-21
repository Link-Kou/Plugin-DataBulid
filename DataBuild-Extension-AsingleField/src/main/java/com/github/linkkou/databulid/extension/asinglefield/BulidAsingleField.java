package com.github.linkkou.databulid.extension.asinglefield;

import com.github.linkkou.databulid.extension.asinglefield.entity.ParametersEntity;
import com.github.linkkou.databulid.extension.asinglefield.parsing.ParsingParameters;
import com.github.linkkou.databulid.extension.asinglefield.parsing.ParsingReturn;
import com.github.linkkou.databulid.impl.DefaultCode;
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
    public String getCode(DefaultCode defaultCode, ExecutableElement executableElement, ProcessingEnvironment processingEnv) {
        this.executableElement = executableElement;
        this.processingEnv = processingEnv;
        final ParametersEntity parametersEntity = parsingReturn(defaultCode);
        final List<ParametersEntity> parametersEntity1 = parsingParameters();
        parametersEntity.getVariableMethodParameterList().stream().forEach(x -> {
            parametersEntity1.forEach(x1 -> {
                x1.getVariableMethodParameterList().stream().forEach(x2 -> {
                    if (x.getMatchingMethodName().equals(x2.getMatchingMethodName())) {
                        stringBuilder.append(String.format("%s.%s(%s.%s());", parametersEntity.getName(), x.getOriginalMethodName(), x1.getName(), x2.getOriginalMethodName()));
                    }
                });
            });
        });
        stringBuilder.append(String.format("return %s", parametersEntity.getName()));
        return stringBuilder.toString();
    }


    /**
     * 获取方法返回的对象
     */
    private ParametersEntity parsingReturn(DefaultCode defaultCode) {
        ParsingReturn parsingReturn = new ParsingReturn(defaultCode,executableElement, processingEnv, stringBuilder);
        return parsingReturn.parsing();
    }

    /**
     * 获取到参数
     *
     * @return
     */
    private List<ParametersEntity> parsingParameters() {
        ParsingParameters parsingParameters = new ParsingParameters(executableElement, processingEnv);
        return parsingParameters.parsing();
    }

}
