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
 * 解析方法的参数
 *
 * @author lk
 * @version 1.0
 * @date 2019/10/5 14:25
 */
public class ParsingParameters {

    private ExecutableElement executableElement;

    private ProcessingEnvironment processingEnv;

    private static final String GETTER_FORMAT = "(get|is|to)(.*)";

    private static final Pattern GETTER_PATTERN = Pattern.compile(GETTER_FORMAT);

    private static final String[] GETTER_FIRST_FORMAT = new String[]{"(get|is|to)", "^F"};

    public ParsingParameters(ExecutableElement executableElement, ProcessingEnvironment processingEnv) {
        this.executableElement = executableElement;
        this.processingEnv = processingEnv;
    }


    public List<ParametersEntity> parsing() {
        final List<? extends VariableElement> methodParameters = MethodUtils.getMethodParameters(this.executableElement);
        List<ParametersEntity> parametersEntitys = new ArrayList<ParametersEntity>();
        for (VariableElement methodParameter : methodParameters) {
            final RegexsEntity regexs = RegexsParsing.getRegexs(methodParameter);
            final ArrayList<ExecutableElement> classAllMembersByPublicAndName = ClassUtils.getClassAllMembersByPublicAndName(processingEnv, ClassUtils.getClassType(ParametersUtils.getParametersType(methodParameter)), GETTER_PATTERN);
            final List<ParametersEntity.VariableMethodParameter> collect = classAllMembersByPublicAndName.stream().map(x -> {
                String s1 = x.getSimpleName().toString();
                String sreplaceFirst1 = s1;
                for (String s : GETTER_FIRST_FORMAT) {
                    sreplaceFirst1 = sreplaceFirst1.replaceFirst(s, "");
                }
                if (null != regexs) {
                    sreplaceFirst1 = s1;
                    for (String s : regexs.getReplaceFirst()) {
                        sreplaceFirst1 = sreplaceFirst1.replaceFirst(s, "");
                    }
                    for (RegexsEntity.RegexEntity regexEntity : regexs.getReplaceFirstMap()) {
                        if (s1.equals(regexEntity.getMethodsName())) {
                            String sreplaceFirstuser = s1;
                            for (String s : regexEntity.getRegex()) {
                                sreplaceFirstuser = sreplaceFirstuser.replaceFirst(s, "");
                            }
                            if (!"".equals(regexEntity.getRename().trim())) {
                                sreplaceFirstuser = sreplaceFirstuser.replaceAll(sreplaceFirstuser, regexEntity.getRename());
                            }
                            sreplaceFirst1 = sreplaceFirstuser;
                        }
                    }
                    if (regexs.getReplaceFirstCapital()) {
                        sreplaceFirst1 = StringAsingUtils.toUpperCaseFirstOne(sreplaceFirst1);
                    } else {
                        sreplaceFirst1 = StringAsingUtils.toLowerCaseFirstOne(sreplaceFirst1);
                    }
                } else {
                    sreplaceFirst1 = StringAsingUtils.toUpperCaseFirstOne(sreplaceFirst1);
                }
                ParametersEntity.VariableMethodParameter parameter = new ParametersEntity.VariableMethodParameter();
                parameter.setOriginalMethodName(s1);
                parameter.setMatchingMethodName(sreplaceFirst1);
                return parameter;
            }).collect(Collectors.toList());
            ParametersEntity parametersEntity = new ParametersEntity();
            parametersEntity.setName(ParametersUtils.getParametersName(methodParameter));
            parametersEntity.setVariableMethodParameterList(collect);
            parametersEntitys.add(parametersEntity);
        }
        return parametersEntitys;
    }


}
