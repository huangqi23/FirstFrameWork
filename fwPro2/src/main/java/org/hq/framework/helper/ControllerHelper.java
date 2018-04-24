package org.hq.framework.helper;

import org.hq.framework.annotation.Action;
import org.hq.framework.bean.Handler;
import org.hq.framework.bean.Request;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/*控制器助手类,把所有action映射的方法路径，和对应的类、方法 添加到map中*/
public class ControllerHelper {
    private static final Map<Request, Handler> ACTION_MAP = new HashMap<Request, Handler>();
    static {
        Set<Class<?>> classSet = ClassHelper.getControllerClassSet();
        if(!classSet.isEmpty()){
            for(Class<?> controllerClass: classSet){
                Method[] methods = controllerClass.getDeclaredMethods();
                if(methods.length>0){
                    for(Method method: methods){
                        if(method.isAnnotationPresent(Action.class)){
                            Action action = method.getAnnotation(Action.class);
                            String mapping = action.value();
                            String method_ = action.method();
                            if(true){//(mapping.matches("\\w+:/\\w*")){
                                //String[] array = mapping.split(":");
                                /*if (array.length==2){
                                    String requestmethod = array[0];
                                    String requestpath = array[1];
                                    Request request = new Request(requestmethod, requestpath);
                                    Handler handler = new Handler(controllerClass, method);
                                    ACTION_MAP.put(request, handler);
                                }*/
                                Request request = new Request(method_.toLowerCase(), mapping);
                                Handler handler = new Handler(controllerClass, method);
                                ACTION_MAP.put(request, handler);
                            }
                        }
                    }
                }
            }
        }
    }

    public static Handler getHandler(String requestMethod, String requestPath){
        Request request = new Request(requestMethod,requestPath);
        return ACTION_MAP.get(request);
    }
}
