package com.github.linkkou.databulid.spi;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;

/**
 * @author lk
 * @version 1.0
 * @date 2019/10/2 17:57
 */
public interface DataBuildSpi {

    public String getCode(ExecutableElement executableElement, ProcessingEnvironment processingEnv);
}
