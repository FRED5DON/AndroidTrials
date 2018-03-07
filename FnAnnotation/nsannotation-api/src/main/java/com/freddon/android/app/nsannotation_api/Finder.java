package com.freddon.android.app.nsannotation_api;

import android.app.Dialog;
import android.view.View;
import android.view.Window;

/**
 * Created by fred on 2018/3/3.
 */

final class Finder implements IFinder {

    @Override
    public View findView(Object object, int resId) {
        if (object instanceof View){
            return ((View)object).findViewById(resId);
        }
        if (object instanceof Dialog){
            return ((Dialog)object).findViewById(resId);
        }
        if (object instanceof Window){
            return ((Window)object).findViewById(resId);
        }
        return null;
    }


    private View findView(Window object, int resId){
        return object.findViewById(resId);
    }

    private  View findView(Dialog object, int resId){
        return object.findViewById(resId);
    }

    private View findView(View object, int resId){
        return object.findViewById(resId);
    }

}