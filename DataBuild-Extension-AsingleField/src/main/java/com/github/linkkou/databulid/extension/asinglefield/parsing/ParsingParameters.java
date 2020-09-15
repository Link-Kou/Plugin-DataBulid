package com.github.linkkou.databulid.extension.asinglefield.parsing;

import com.github.linkkou.databulid.extension.asinglefield.annotation.ExcludeParsing;
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

    /**
     * 解析
     *
     * @return 数组
     */
    public List<ParametersEntity> parsing() {
        final List<? extends VariableElement> methodParameters = MethodUtils.getMethodParameters(this.executableElement);
        List<ParametersEntity> parametersEntitys = new ArrayList<ParametersEntity>();
        //遍历所有参数
        for (VariableElement methodParameter : methodParameters) {
            final RegexsEntity regexs = RegexsParsing.getRegexs(methodParameter);
            //自定义匹配方法
            ArrayList<ExecutableElement> classAllMembersByPublicAndName = getAllMembersByPublicAndName(regexs, methodParameter);
            final List<ParametersEntity.VariableMethodParameter> collect = classAllMembersByPublicAndName.stream().map(x -> {
                String s1 = x.getSimpleName().toString();
                //排除
                final boolean excludeMembers = getExcludeMembers(methodParameter, s1);
                if (excludeMembers) {
                    return new ParametersEntity.VariableMethodParameter();
                }
                String sreplaceFirst1 = s1;
                for (String s : GETTER_FIRST_FORMAT) {
                    sreplaceFirst1 = sreplaceFirst1.replaceFirst(s, "");
                }
                sreplaceFirst1 = StringAsingUtils.toUpperCaseFirstOne(sreplaceFirst1);
                //注解用户自定义参数
                if (null != regexs) {
                    if (regexs.getReplaceFirst().length > 0) {
                        sreplaceFirst1 = s1;
                        for (String s : regexs.getReplaceFirst()) {
                            sreplaceFirst1 = sreplaceFirst1.replaceFirst(s, "");
                        }
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

    /**
     * 获取到所有的方法
     *
     * @param regexs          注解实体
     * @param methodParameter 方法
     * @return 数组
     */
    private ArrayList<ExecutableElement> getAllMembersByPublicAndName(final RegexsEntity regexs, final VariableElement methodParameter) {
        //自定义匹配方法
        ArrayList<ExecutableElement> classAllMembersByPublicAndName = ClassUtils.getClassAllMembersByPublicAndName(
                processingEnv, ClassUtils.getClassType(ParametersUtils.getParametersType(methodParameter)), GETTER_PATTERN);
        if (null != regexs) {
            final String matcher = regexs.getMatcher();
            if (null != matcher && matcher.length() > 0) {
                classAllMembersByPublicAndName = ClassUtils.getClassAllMembersByPublicAndName(
                        processingEnv, ClassUtils.getClassType(ParametersUtils.getParametersType(methodParameter)), Pattern.compile(regexs.getMatcher()));
            }
        }
        return classAllMembersByPublicAndName;
    }

    /**
     * 排除
     *
     * @param methodParameter 对象
     * @param methodName      方法
     * @return boolean true为排除
     */
    private boolean getExcludeMembers(VariableElement methodParameter, String methodName) {
        final List<String> exclude = ExcludeParsing.getExclude(methodParameter);
        final long count = exclude.stream().filter(x -> x.equals(methodName)).count();
        return count > 1;
    }
}
