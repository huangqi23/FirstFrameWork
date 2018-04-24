package org.hq.framework.util;

import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
/*反射工具类*/
public final class ReflectionUtil {
    private static final Logger LOGGER =  Logger.getLogger(ReflectionUtil.class.getName());
    /*创建对象*/
    public static Object newInstance(Class<?> cls){
        Object instance = null;

        try {
            instance = cls.newInstance();
        }catch (Exception e){

        }
        return instance;
    }

    /*调用方法：对象、方法、参数*/
    public static Object invokeMethod(Object obj, Method method, Object...args){
        Object result;
        try {
            method.setAccessible(true);/*设置为private*/
            result = method.invoke(obj, args);
        }catch (Exception e){
            LOGGER.error("调用method错误",e);
            throw new RuntimeException(e);
        }
        return result;

    }

    /*设置成员变量的值*/
    public static void setField(Object obj, Field field, Object value){
        try {
            field.setAccessible(true);
            field.set(obj, value);
        }catch (Exception e){
            LOGGER.error("设置field错误",e);
            throw new RuntimeException(e);
        }

    }

}
