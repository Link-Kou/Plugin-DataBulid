package com.github.linkkou.databulid.impl;

/**
 * @author lk
 * @version 1.0
 * @date 2019/12/20 17:02
 */
public interface DefaultCode {

    /**
     * 创建默认代码
     *
     * @param stringBuilder 字符串构建
     * @param varName 变量
     * @param classTypePath 类路径
     */
    void getCreateDefaultCode(StringBuilder stringBuilder, String varName, String classTypePath);

    /**
     * 是否为 接口默认方法
     *
     * @return boolean
     */
    boolean isDefault();

    /**
     * 获取到基础的父级代码
     *
     * @return 字符串
     */
    String getSuper();
}
