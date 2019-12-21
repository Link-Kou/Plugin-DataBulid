package com.github.linkkou.databulid.spi;

import com.github.linkkou.databulid.impl.DefaultCode;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;

/**
 * 扩展机制
 * @author lk
 * @version 1.0
 * @date 2019/10/2 17:57
 */
public interface DataBuildSpi {

    public String getCode(DefaultCode defaultCode, ExecutableElement executableElement, ProcessingEnvironment processingEnv);
}
