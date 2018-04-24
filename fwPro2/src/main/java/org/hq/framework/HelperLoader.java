package org.hq.framework;

import org.hq.framework.annotation.Controller;
import org.hq.framework.helper.*;
import org.hq.framework.util.ClassUtil;

public final class HelperLoader {
    public static void init(){
        Class<?>[] classList = {ClassHelper.class, BeanHelper.class, AopHelper.class, IocHelper.class, ControllerHelper.class};
        for(Class<?> cls: classList){
            ClassUtil.loadClass(cls.getName(),true);
        }
    }
}
