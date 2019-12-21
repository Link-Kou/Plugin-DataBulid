package com.github.linkkou.databulid.extension.asinglefield.parsing;

import com.github.linkkou.databulid.extension.asinglefield.annotation.RegexsEntity;
import com.github.linkkou.databulid.extension.asinglefield.annotation.RegexsParsing;
import com.github.linkkou.databulid.extension.asinglefield.entity.ParametersEntity;
import com.github.linkkou.databulid.extension.asinglefield.utils.StringAsingUtils;
import com.github.linkkou.databulid.impl.DefaultCode;
import com.github.linkkou.databulid.utils.ClassUtils;
import com.github.linkkou.databulid.utils.MethodUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 解析返回
 *
 * @author lk
 * @version 1.0
 * @date 2019/10/5 14:23
 */
public class ParsingReturn {

    private static final String SETTER_FORMAT = "set(.*)";

    private static String SETTER_FIRST_FORMAT = "set";

    private static Pattern SETTER_PATTERN = Pattern.compile(SETTER_FORMAT);


    private ExecutableElement executableElement;

    private ProcessingEnvironment processingEnv;

    private StringBuilder stringBuilder;

    private DefaultCode defaultCode;


    public ParsingReturn(DefaultCode defaultCode, ExecutableElement executableElement, ProcessingEnvironment processingEnv, StringBuilder stringBuilder) {
        this.executableElement = executableElement;
        this.processingEnv = processingEnv;
        this.stringBuilder = stringBuilder;
        this.defaultCode = defaultCode;
    }

    public ParametersEntity parsing() {
        final TypeMirror methodReturn = MethodUtils.getMethodReturn(this.executableElement);
        final String classTypePath = ClassUtils.getClassTypePath(methodReturn);
        defaultCode.getCreateDefaultCode(stringBuilder, "returnlocalvar", classTypePath);
        final RegexsEntity regexs = RegexsParsing.getRegexs(this.executableElement);
        ArrayList<ExecutableElement> classAllMembersByPublicAndName;
        if (null != regexs) {
            SETTER_PATTERN = Pattern.compile(regexs.getMatcher());
        }
        classAllMembersByPublicAndName = ClassUtils.getClassAllMembersByPublicAndName(processingEnv, ClassUtils.getClassType(methodReturn), SETTER_PATTERN);
        ParametersEntity parametersEntity = new ParametersEntity();
        parametersEntity.setName("returnlocalvar");
        final List<ParametersEntity.VariableMethodParameter> collect = classAllMembersByPublicAndName.stream().map(x -> {
            String s = x.getSimpleName().toString();
            String sreplaceFirst = s.replaceFirst(SETTER_FIRST_FORMAT, "");
            sreplaceFirst = StringAsingUtils.toUpperCaseFirstOne(sreplaceFirst);
            if (null != regexs) {
                for (String regex1 : regexs.getReplaceFirst()) {
                    sreplaceFirst = s.replaceFirst(regex1, "");
                }
                for (RegexsEntity.RegexEntity entity : regexs.getReplaceFirstMap()) {
                    if (s.equals(entity.getMethodsName())) {
                        for (String regex2 : entity.getRegex()) {
                            sreplaceFirst = s.replaceFirst(regex2, "");
                        }
                    }
                }
                if (regexs.getReplaceFirstCapital()) {
                    sreplaceFirst = StringAsingUtils.toUpperCaseFirstOne(sreplaceFirst);
                } else {
                    sreplaceFirst = StringAsingUtils.toLowerCaseFirstOne(sreplaceFirst);
                }
            }
            ParametersEntity.VariableMethodParameter parameter = new ParametersEntity.VariableMethodParameter();
            parameter.setOriginalMethodName(s);
            parameter.setMatchingMethodName(sreplaceFirst);
            return parameter;


        }).collect(Collectors.toList());
        parametersEntity.setVariableMethodParameterList(collect);
        return parametersEntity;
    }

}
