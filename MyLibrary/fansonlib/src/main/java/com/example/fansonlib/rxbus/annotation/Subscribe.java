package com.example.fansonlib.rxbus.annotation;

import com.example.fansonlib.rxbus.event.EventThread;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
* @author Created by：fanson
* Created on：2017/12/1 10:52
* Description：订阅注解
*/
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Subscribe {
    int eventTag() default -1;

    EventThread threadMode() default EventThread.CURRENT_THREAD;
}
