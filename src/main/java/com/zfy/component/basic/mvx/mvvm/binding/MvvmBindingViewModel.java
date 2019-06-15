package com.zfy.component.basic.mvx.mvvm.binding;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LifecycleObserver;
import android.os.Bundle;
import android.support.annotation.NonNull;

import org.greenrobot.eventbus.EventBus;

/**
 * CreateAt : 2018/9/11
 * Describe : ViewModel 基类
 *
 * @author chendong
 */
public abstract class MvvmBindingViewModel extends AndroidViewModel implements LifecycleObserver {


    public MvvmBindingViewModel(@NonNull Application application) {
        super(application);
        EventBus.getDefault().register(this);
    }

    /**
     * View 层会在 ViewModel 创建后调用 init() 方法
     * init() 在构造方法之后调用
     */
    public void init() {

    }

    @Override
    protected void onCleared() {
        super.onCleared();
        EventBus.getDefault().unregister(this);
    }


}
