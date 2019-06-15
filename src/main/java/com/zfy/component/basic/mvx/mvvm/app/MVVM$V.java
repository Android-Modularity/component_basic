package com.zfy.component.basic.mvx.mvvm.app;


import android.support.annotation.NonNull;

import com.zfy.component.basic.mvx.mvvm.MvvmViewModel;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * CreateAt : 2018/9/12
 * Describe : 注解帮助生成 ViewOpts
 *
 * @author chendong
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface MVVM$V {

    int NO_LAYOUT = -1;

    int layout();

    @NonNull Class<? extends MvvmViewModel> vm() default MvvmViewModel.class;
}
