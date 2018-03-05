package com.freddon.android.app.nsannotation_api;

/**
 * Created by fred on 2018/3/3.
 */

public interface IViewBinder<T> {

    public void bindView(T target, Object object, IFinder finder) ;

    void unbind(T target);
}
