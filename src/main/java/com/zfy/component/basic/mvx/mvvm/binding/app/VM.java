package com.zfy.component.basic.mvx.mvvm.binding.app;


import android.support.annotation.NonNull;

import com.zfy.component.basic.mvx.mvvm.binding.MvvmBindingViewModel;

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
public @interface VM {

    int layout();

    @NonNull Class vm() default MvvmBindingViewModel.class;

    int vmId() default 0;
}
