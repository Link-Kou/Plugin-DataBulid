package com.github.linkkou.databulid.impl;

import com.github.linkkou.databulid.utils.MethodUtils;
import com.sun.tools.javac.code.Symbol;

import javax.lang.model.element.ExecutableElement;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 创建默认接口代码
 *
 * @author lk
 * @version 1.0
 * @date 2019/12/20 17:02
 */
public class DefaultCodeImpl implements DefaultCode {

    private ExecutableElement executableElement;

    private List<String> parameterlist;

    public DefaultCodeImpl(ExecutableElement executableElement, List<String> parameterlist) {
        this.executableElement = executableElement;
        this.parameterlist = parameterlist;
    }

    @Override
    public void getCreateDefaultCode(StringBuilder stringBuilder, String varName, String classTypePath) {
        if (this.isDefault()) {
            String classname = ((Symbol.MethodSymbol) executableElement).owner.name.toString();
            String parameterarray = IntStream.range(0, parameterlist.size()).filter(x -> {
                //是奇数
                return (x & 1) == 1;
            }).mapToObj(x -> parameterlist.get(x)).collect(Collectors.joining(","));
            String code = String.format("%s.super.%s(%s);", classname, executableElement.getSimpleName().toString(), parameterarray);
            stringBuilder.append(String.format("%s %s = %s", classTypePath, varName, code));
            stringBuilder.append(String.format("%s = %s == null ?  new %s() : %s;", varName, varName, classTypePath, varName));
        } else {
            stringBuilder.append(String.format("%s returnlocalvar = new %s();", classTypePath, classTypePath));
        }
    }

    @Override
    public boolean isDefault() {
        return MethodUtils.isDefault(executableElement);
    }

    @Override
    public String getSuper() {
        String classname = ((Symbol.MethodSymbol) executableElement).owner.name.toString();
        String parameterarray = IntStream.range(0, parameterlist.size()).filter(x -> {
            //是奇数
            return (x & 1) == 1;
        }).mapToObj(x -> parameterlist.get(x)).collect(Collectors.joining(","));
        return String.format("return %s.super.%s(%s)", classname, executableElement.getSimpleName().toString(), parameterarray);
    }


}
