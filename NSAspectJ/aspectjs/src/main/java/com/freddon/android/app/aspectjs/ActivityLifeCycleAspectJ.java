package com.freddon.android.app.aspectjs;

import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Created by fred on 2018/3/8.
 */
@Aspect
public class ActivityLifeCycleAspectJ {

    @Pointcut("execution(* android.app.Activity+.on*(..))")
    public void point_method(){}

    @Around("point_method()")
    public Object around_point_method(ProceedingJoinPoint joinPoint) throws Throwable {
        StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append("\n--- Signature -----\n")
                .append(joinPoint.getSignature().toString())
                .append("\n--- Location -----\n")
                .append(joinPoint.getSourceLocation().toString());
        Log.i("********** ",stringBuffer.toString());
        return joinPoint.proceed();
    }


}
