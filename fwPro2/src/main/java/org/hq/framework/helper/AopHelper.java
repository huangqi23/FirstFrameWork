package org.hq.framework.helper;


import org.apache.log4j.Logger;
import org.hq.framework.annotation.Aspect;
import org.hq.framework.proxy.AspectProxy;
import org.hq.framework.proxy.Proxy;
import org.hq.framework.proxy.ProxyManager;

import java.lang.annotation.Annotation;
import java.util.*;

public class AopHelper {
    private static final Logger LOGGER = Logger.getLogger(AopHelper.class.getName());
    static {
        try {
            Map<Class<?>, Set<Class<?>>> proxyMap = createProxyMap(); /*key是切面类，value是目标类set，每一个切面类有一个目标类集合*/
            Map<Class<?>, List<Proxy>> targetMap = createTargetMap(proxyMap);/*key是目标类，value是切面接口列表*/
            for (Map.Entry<Class<?>, List<Proxy>> targetEntry: targetMap.entrySet()){
                Class<?> targetClass = targetEntry.getKey();/*目标类*/
                List<Proxy> proxyList = targetEntry.getValue();/*切面接口列表*/
                Object proxy = ProxyManager.createProxy(targetClass, proxyList); /*创建代理对象*/
                BeanHelper.setBean(targetClass, proxy);
            }
        } catch (Exception e) {
            LOGGER.error("aop fail",e);
        }
    }

    private static Map<Class<?>, Set<Class<?>>> createProxyMap() throws Exception{
        Map<Class<?>, Set<Class<?>>> proxyMap = new HashMap<Class<?>, Set<Class<?>>>();

        Set<Class<?>> proxyClassSet = ClassHelper.getClassSetbySuper(AspectProxy.class);/*获取扩展了AspectProxy类的所有类*/
        for (Class<?> proxyClass: proxyClassSet){ /*循环这些切面*/
            if (proxyClass.isAnnotationPresent(Aspect.class)){ /*如果有Aspect注解*/
                Aspect aspect = proxyClass.getAnnotation(Aspect.class); /*获取注解*/
                Set<Class<?>> targetClassSet = createTargetClassSet(aspect); /*有某个注解的所有类的集合*/
                proxyMap.put(proxyClass, targetClassSet);
            }
        }
        return proxyMap;
    }

    private static Set<Class<?>> createTargetClassSet(Aspect aspect)throws Exception{
        Set<Class<?>> targetClassSet = new HashSet<Class<?>>();
        Class<? extends Annotation> annotation =  aspect.value();
        if(annotation!= null && !annotation.equals(Aspect.class)){
            targetClassSet.addAll(ClassHelper.getClassSetByAnnotation(annotation));/*获取所有这个注解*/
        }
        return targetClassSet;
    }

    private static Map<Class<?>, List<Proxy>> createTargetMap(Map<Class<?>, Set<Class<?>>> proxyMap) throws Exception{
        Map<Class<?>, List<Proxy>> targetMap = new HashMap<Class<?>, List<Proxy>>();
        for(Map.Entry<Class<?>, Set<Class<?>>> proxyEntry : proxyMap.entrySet()){ /*循环所有切面*/
            Class<?> proxyClass = proxyEntry.getKey();
            Set<Class<?>> targetClassSet = proxyEntry.getValue();
            for(Class<?> targetClass: targetClassSet){ /*循环目标类*/
                Proxy proxy= (Proxy) proxyClass.newInstance(); /*切面对象，转接口*/
                if(targetMap.containsKey(targetClass)){
                    targetMap.get(targetClass).add(proxy);
                }else{
                    List<Proxy> proxyList = new ArrayList<Proxy>();
                    proxyList.add(proxy);
                    targetMap.put(targetClass, proxyList);/*key是目标类，value是切面接口列表*/
                }
            }
        }
        return  targetMap;
    }


}
