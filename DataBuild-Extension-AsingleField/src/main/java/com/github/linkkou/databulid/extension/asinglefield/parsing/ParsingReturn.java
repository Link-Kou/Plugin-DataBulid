package com.github.linkkou.databulid.extension.asinglefield.parsing;

import com.github.linkkou.databulid.extension.asinglefield.entity.ParametersEntity;
import com.github.linkkou.databulid.utils.ClassUtils;
import com.github.linkkou.databulid.utils.MethodUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author lk
 * @version 1.0
 * @date 2019/10/5 14:23
 */
public class ParsingReturn {

    private static final String SETTER_FORMAT = "set(.*)";

    private static final String SETTER_FIRST_FORMAT = "set";

    private static final Pattern SETTER_PATTERN = Pattern.compile(SETTER_FORMAT);


    private ExecutableElement executableElement;

    private ProcessingEnvironment processingEnv;

    private StringBuilder stringBuilder;


    public ParsingReturn(ExecutableElement executableElement, ProcessingEnvironment processingEnv, StringBuilder stringBuilder) {
        this.executableElement = executableElement;
        this.processingEnv = processingEnv;
        this.stringBuilder = stringBuilder;
    }

    public ParametersEntity parsing() {
        final TypeMirror methodReturn = MethodUtils.getMethodReturn(this.executableElement);
        final String classTypePath = ClassUtils.getClassTypePath(methodReturn);
        stringBuilder.append(String.format("%s returnlocalvar = new %s();", classTypePath, classTypePath));
        final ArrayList<ExecutableElement> classAllMembersByPublicAndName = ClassUtils.getClassAllMembersByPublicAndName(processingEnv, ClassUtils.getClassType(methodReturn), SETTER_PATTERN);
        ParametersEntity parametersEntity = new ParametersEntity();
        parametersEntity.setName("returnlocalvar");
        final List<ParametersEntity.VariableMethodParameter> collect = classAllMembersByPublicAndName.stream().map(x -> {
            final String s = x.getSimpleName().toString();
            ParametersEntity.VariableMethodParameter parameter = new ParametersEntity.VariableMethodParameter();
            parameter.setOriginalMethodName(s);
            parameter.setMatchingMethodName(s.replaceFirst(SETTER_FIRST_FORMAT, ""));
            return parameter;
        }).collect(Collectors.toList());
        parametersEntity.setVariableMethodParameterList(collect);
        return parametersEntity;
    }

}
