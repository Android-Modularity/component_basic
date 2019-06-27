package com.zfy.component.basic.app;

import android.support.annotation.LayoutRes;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * CreateAt : 2019/6/27
 * Describe :
 *
 * @author chendong
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface Layout {
    @LayoutRes
    int value();
}
