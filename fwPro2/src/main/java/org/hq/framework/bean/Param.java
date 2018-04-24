package org.hq.framework.bean;

import java.util.Map;

public class Param {
    private Map<String, Object> paramMap;

    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    public Param(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    public Object getLong(String name){
        return  (Long)paramMap.get(name);
    }
}
