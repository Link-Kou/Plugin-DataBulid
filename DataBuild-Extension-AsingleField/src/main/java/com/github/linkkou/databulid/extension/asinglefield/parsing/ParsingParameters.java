package com.github.linkkou.databulid.extension.asinglefield.parsing;

import com.github.linkkou.databulid.extension.asinglefield.annotation.RegexsEntity;
import com.github.linkkou.databulid.extension.asinglefield.annotation.RegexsParsing;
import com.github.linkkou.databulid.extension.asinglefield.entity.ParametersEntity;
import com.github.linkkou.databulid.extension.asinglefield.utils.StringAsingUtils;
import com.github.linkkou.databulid.utils.ClassUtils;
import com.github.linkkou.databulid.utils.MethodUtils;
import com.github.linkkou.databulid.utils.ParametersUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author lk
 * @version 1.0
 * @date 2019/10/5 14:25
 */
public class ParsingParameters {

    private ExecutableElement executableElement;

    private ProcessingEnvironment processingEnv;

    private static final String GETTER_FORMAT = "(get|is)(.*)";

    private static final Pattern GETTER_PATTERN = Pattern.compile(GETTER_FORMAT);

    public ParsingParameters(ExecutableElement executableElement, ProcessingEnvironment processingEnv) {
        this.executableElement = executableElement;
        this.processingEnv = processingEnv;
    }


    public ParametersEntity parsing() {
        final List<? extends VariableElement> methodParameters = MethodUtils.getMethodParameters(this.executableElement);
        ParametersEntity parametersEntity = new ParametersEntity();
        for (VariableElement methodParameter : methodParameters) {
            final RegexsEntity regexs = RegexsParsing.getRegexs(methodParameter);
            final ArrayList<ExecutableElement> classAllMembersByPublicAndName = ClassUtils.getClassAllMembersByPublicAndName(processingEnv, ClassUtils.getClassType(ParametersUtils.getParametersType(methodParameter)), GETTER_PATTERN);
            final List<ParametersEntity.VariableMethodParameter> collect = classAllMembersByPublicAndName.stream().map(x -> {
                ParametersEntity.VariableMethodParameter parameter = new ParametersEntity.VariableMethodParameter();
                String s1 = x.getSimpleName().toString();
                parameter.setOriginalMethodName(s1);
                boolean replaceFirst = true;
                for (RegexsEntity.RegexEntity regexEntity : regexs.getReplaceFirstMap()) {
                    if (s1.equals(regexEntity.getMethodsName())) {
                        replaceFirst = false;
                        for (String s : regexEntity.getRegex()) {
                            s1 = s1.replaceAll(s, "");
                        }
                    }
                }
                if (replaceFirst) {
                    for (String s : regexs.getReplaceFirst()) {
                        s1 = s1.replaceAll(s, "");
                    }
                }
                if (regexs.getReplaceFirstCapital()) {
                    s1 = StringAsingUtils.toUpperCaseFirstOne(s1);
                } else {
                    s1 = StringAsingUtils.toLowerCaseFirstOne(s1);
                }
                parameter.setMatchingMethodName(s1);
                return parameter;
            }).collect(Collectors.toList());
            parametersEntity.setName(ParametersUtils.getParametersName(methodParameter));
            parametersEntity.setVariableMethodParameterList(collect);
        }
        return parametersEntity;
    }


}
