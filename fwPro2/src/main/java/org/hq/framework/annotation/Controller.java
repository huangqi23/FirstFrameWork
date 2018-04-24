package org.hq.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)/*限制注解可以使用的类型*/
@Retention(RetentionPolicy.RUNTIME)
public @interface Controller {
}
