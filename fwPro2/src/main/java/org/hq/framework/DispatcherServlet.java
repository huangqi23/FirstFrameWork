package org.hq.framework;


import org.hq.framework.bean.Data;
import org.hq.framework.bean.Handler;
import org.hq.framework.bean.Param;
import org.hq.framework.bean.View;
import org.hq.framework.helper.BeanHelper;
import org.hq.framework.helper.ConfigHelper;
import org.hq.framework.helper.ControllerHelper;
import org.hq.framework.util.*;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class DispatcherServlet extends HttpServlet{
    @Override
    public void init(ServletConfig config) throws ServletException {
        //super.init(config);
        HelperLoader.init();
        ServletContext servletContext = config.getServletContext();
        ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
        jspServlet.addMapping(ConfigHelper.getAppJspPath()+"*");
        ServletRegistration defaultServlet = servletContext.getServletRegistration("default");
        defaultServlet.addMapping(ConfigHelper.getAppAssetPath()+"*");

    }

    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        //super.service(req, res);
        String requestMethod = req.getMethod().toLowerCase();
        String requestPath = req.getRequestURI();//req.getPathInfo();
        Handler handler = ControllerHelper.getHandler(requestMethod,requestPath);/*根据请求的路径找到handler*/
        if(handler!= null){
            Class controllerClass = handler.getControllerClass();/*获取控制类*/
            Object controllerBean = BeanHelper.getBean(controllerClass);/*获取控制类对象*/
            Map<String, Object> paramMap = new HashMap<String, Object>();
            /*获取request中的参数*/
            Enumeration<String> paramNames = req.getParameterNames();
            while (paramNames.hasMoreElements()){
                String paramName = paramNames.nextElement();
                String paramValue = req.getParameter(paramName);
                paramMap.put(paramName, paramValue);
            }
            /*获取URL后面的参数*/

            String body = CodeUtil.decodeURL(StreamUtil.getString(req.getInputStream()));
            if (StringUtil.isNotEmpty(body)){
                String[] params = body.split("&");
                if(ArrayUtil.isNotEmpty(params)){
                    for(String param: params){
                        String[] array = param.split("=");
                        if (ArrayUtil.isNotEmpty(array) && array.length ==2 ){
                            String paramname = array[0];
                            String paramvalue = array[1];
                            paramMap.put(paramname,paramvalue);
                        }
                    }
                }
            }

            Param param = new Param(paramMap);
            Method method = handler.getActionMethod();
            Object result = ReflectionUtil.invokeMethod(controllerBean, method, param);
            if(result instanceof View){//如果返回结果是view的一个实例
                View view = (View) result;
                String path = view.getPath();
                if(StringUtil.isNotEmpty(path)){
                    if(path.startsWith("/")){
                        res.sendRedirect(req.getContextPath()+path);
                    }else{
                        Map<String, Object> model = view.getModel();
                        for (Map.Entry<String,Object> entry:model.entrySet()){
                            req.setAttribute(entry.getKey(), entry.getValue());
                        }
                        req.getRequestDispatcher(ConfigHelper.getAppJspPath()+path).forward(req, res);
                    }
                }

            }else if(result instanceof Data){
                /*返回json*/
                Data data = (Data) result;
                Object model = data.getModel();
                if(model!= null){
                    res.setContentType("application/json");
                    res.setCharacterEncoding("UTF-8");
                    PrintWriter writer = res.getWriter();
                    String json = JsonUtil.toJson(model);
                    writer.write(json);
                    writer.flush();
                    writer.close();
                }
            }




        }

    }
}
