package com.github.linkkou.databulid.extension.asinglefield.entity;

import java.util.List;

/**
 * 方法参数的实体
 * @author lk
 * @version 1.0
 * @date 2019/10/5 14:27
 */
public class ParametersEntity {


    /**
     * 变量名称
     */
    private String name;

    /**
     * 所有的方法 名称
     */
    private List<VariableMethodParameter> variableMethodParameterList;

    public String getName() {
        return name;
    }

    public ParametersEntity setName(String name) {
        this.name = name;
        return this;
    }

    public List<VariableMethodParameter> getVariableMethodParameterList() {
        return variableMethodParameterList;
    }

    public ParametersEntity setVariableMethodParameterList(List<VariableMethodParameter> variableMethodParameterList) {
        this.variableMethodParameterList = variableMethodParameterList;
        return this;
    }

    public static class VariableMethodParameter {
        /**
         * 方法名称
         */
        private String originalMethodName;

        /**
         * 处理后的匹配方法名称
         */
        private String matchingMethodName;

        public String getOriginalMethodName() {
            return originalMethodName;
        }

        public VariableMethodParameter setOriginalMethodName(String originalMethodName) {
            this.originalMethodName = originalMethodName;
            return this;
        }

        public String getMatchingMethodName() {
            return matchingMethodName;
        }

        public VariableMethodParameter setMatchingMethodName(String matchingMethodName) {
            this.matchingMethodName = matchingMethodName;
            return this;
        }
    }
}
