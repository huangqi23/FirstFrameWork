package org.hq.framework.util;


import org.apache.log4j.Logger;
import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class ClassUtil {
    private static final Logger LOGGER = Logger.getLogger(PropsUtil.class.getName());
    /*
    *获取类加载器
    * */
    public static ClassLoader getClassLoader(){
        return Thread.currentThread().getContextClassLoader();
    }
    /*
    * 加载类
    * */
    public static Class<?> loadClass(String className, boolean isInitialized){
        Class<?> cls = null;

        try {
            cls = Class.forName(className,isInitialized,getClassLoader());
        }catch (ClassNotFoundException e){
            LOGGER.error("load class fail",e);
        }
        return cls;
    }

    public static Set<Class<?>> getClassSet(String packageName){
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        try {
            Enumeration<URL> urls = getClassLoader().getResources(packageName.replace(".","/"));
            while (urls.hasMoreElements()){
                URL url = urls.nextElement();
                if(url!=null){
                    String protocol = url.getProtocol();
                    if(protocol.equals("file")){
                        String packagePath = url.getPath().replace("%20","");
                        addClass(classSet, packagePath,packageName);
                    }else if(protocol.equals("jar")){
                        JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                        if(jarURLConnection!=null){
                            JarFile jarFile = jarURLConnection.getJarFile();
                            if(jarFile!=null){
                                Enumeration<JarEntry> jarEntryEnumeration = jarFile.entries();
                                while (jarEntryEnumeration.hasMoreElements()){
                                    JarEntry jarEntry = jarEntryEnumeration.nextElement();
                                    String jarEntryName = jarEntry.getName();
                                    if(jarEntryName.endsWith(".class")){
                                        String className = jarEntryName.substring(0,jarEntryName.lastIndexOf(".")).replaceAll("/",".");
                                        doAddClass(classSet, className);
                                    }
                                }

                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            LOGGER.error("get class set fail",e);
            throw  new RuntimeException(e);
        }
        return classSet;
    }


    private static void addClass(Set<Class<?>> classSet,String packagePath, String packageName){
        File[] files = new File(packagePath).listFiles(
                new FileFilter() {
                    @Override
                    public boolean accept(File pathname) {
                        return (pathname.isFile()&&pathname.getName().endsWith(".class"))||
                        pathname.isDirectory();
                    }
                }
        );
        /*等待改进*/
        for(File file:files){
           String filename=file.getName();
           if(file.isFile()){
                String className = filename.substring(0,filename.lastIndexOf("."));
                if(StringUtil.isNotEmpty(packageName)) {
                    className = packageName + "." + className;
                }
                doAddClass(classSet,className);
           }else{
               String subPackagePath = filename;
               if(StringUtil.isNotEmpty(packagePath)){
               subPackagePath = packagePath + "/" + subPackagePath;
               }
               String subPackageName = filename;
               if(StringUtil.isNotEmpty(packageName)){
                    subPackageName = packageName + "."+subPackageName;
               }
                addClass(classSet, subPackagePath, subPackageName);
           }
       }

    }


    private static void doAddClass(Set<Class<?>> classSet, String className){
        Class<?> cls = loadClass(className, false);
        classSet.add(cls);
    }








}
