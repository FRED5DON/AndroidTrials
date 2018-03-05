package com.freddon.android.app.nsannotation_api;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by fred on 2018/3/3.
 */
public class FNViewBinder {

   private static Finder Finder=new Finder();

   static Map<String,IViewBinder> caches=new TreeMap<>();

    public static void bind(Object target){
        bind(target,target);
    }

    public static void bind(Object target,Object object){
        bind(target,object,Finder);
    }


    private static void bind(Object target,Object object,Finder finder){
        //获取注解所在的类
        final String annotatedClassName = target.getClass().getName();

        //获取生成的类
        //先从缓存中获取
        IViewBinder binder = caches.get(annotatedClassName);
        if (binder==null){
            try {
                Class<?> clazz = Class.forName(annotatedClassName + "$$IViewBinder");
                binder= (IViewBinder) clazz.newInstance();
                caches.put(annotatedClassName,binder);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
        if (binder!=null){
            binder.bindView(target,object,finder);
        }

    }

    public static void unBind(Object host) {
        final String className = host.getClass().getName();
        IViewBinder binder = caches.get(className);
        if (binder != null) {
            binder.unbind(host);
        }
        caches.remove(className);
    }

}
