package org.hq.framework.helper;

import org.hq.framework.util.ReflectionUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class BeanHelper {
    private static final Map<Class<?>, Object> BEAN_MAP = new HashMap<Class<?>, Object>();
    static{
        Set<Class<?>> beanClassSet = ClassHelper.getBeanClassSet();
        for(Class<?> beanclass: beanClassSet){
            Object obj= ReflectionUtil.newInstance(beanclass);
            BEAN_MAP.put(beanclass, obj);
        }
    }
    /*获取bean映射*/
    public static Map<Class<?>, Object> getBeanMap(){
        return BEAN_MAP;
    }

    /*获取bean实例*/
    public static <T> T getBean(Class<T> cls){
        if(!BEAN_MAP.containsKey(cls)){
            throw new RuntimeException("cannot get bean:"+cls);
        }
        return (T) BEAN_MAP.get(cls);
    }

    public static void setBean(Class<?> cls, Object object){
        BEAN_MAP.put(cls,object);
    }


}
