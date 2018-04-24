package org.hq.framework.helper;


import org.hq.framework.annotation.Inject;

import java.lang.reflect.Field;
import java.util.Map;

/*依赖注入助手类*/
public class IocHelper {
    static {
        Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap();
        if(!beanMap.isEmpty()){
            for(Map.Entry<Class<?>, Object> beanEntry: beanMap.entrySet()){
                Class<?> beanClass = beanEntry.getKey();
                Object beanInstance = beanEntry.getValue();
                Field[] beanFields = beanClass.getDeclaredFields();
               if(beanFields.length>0){
                   for(Field beanField: beanFields){
                       if(beanField.isAnnotationPresent(Inject.class)){
                           Class<?> beanFieldClass = beanField.getType();
                           Object beanFieldInstance = beanMap.get(beanFieldClass);
                           if(beanFieldInstance!= null){
                               org.hq.framework.util.ReflectionUtil.setField(beanInstance, beanField, beanFieldInstance);
                           }
                       }
                   }
               }
            }
        }
    }
}
