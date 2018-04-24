package org.hq.framework.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)/*限制注解可以使用的类型*/
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {

    Class<? extends Annotation> value();
}
