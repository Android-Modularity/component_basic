package com.zfy.component.basic.arch.mvp.app;


import android.support.annotation.NonNull;

import com.zfy.component.basic.arch.mvp.presenter.MvpPresenter;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * CreateAt : 2018/9/12
 * Describe : 注解帮助生成 ViewConfig
 *
 * @author chendong
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface MvpV {

    int layout();

    @NonNull Class p() default MvpPresenter.class;
}