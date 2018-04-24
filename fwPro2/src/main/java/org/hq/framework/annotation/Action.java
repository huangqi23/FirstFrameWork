package org.hq.framework.annotation;

import javax.print.DocFlavor;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)/*限制注解可以使用的类型*/
@Retention(RetentionPolicy.RUNTIME)
public @interface Action {
    String value();
    String method();
}
