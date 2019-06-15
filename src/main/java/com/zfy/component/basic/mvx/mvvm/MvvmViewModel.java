package com.zfy.component.basic.mvx.mvvm;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LifecycleObserver;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.zfy.component.basic.app.view.IView;
import com.zfy.component.basic.foundation.X;
import com.zfy.component.basic.foundation.api.Api;
import com.zfy.component.basic.foundation.api.IApiAnchor;

/**
 * CreateAt : 2018/9/11
 * Describe : ViewModel 基类
 *
 * @author chendong
 */
public abstract class MvvmViewModel extends AndroidViewModel implements LifecycleObserver, IApiAnchor {

    private Bundle bundle;

    public MvvmViewModel(@NonNull Application application) {
        super(application);
        X.registerEvent(this);
        // 第一次创建时调用 init
        init();
    }

    public void onMvvmViewAttach(IMvvmView view) {
        if (bundle != null) {
            return;
        }
        if (view instanceof IView) {
            ((IView) view).getViewDelegate().addObserver(this);
        }
        bundle = view.getData();
    }

    public @NonNull
    Bundle getData() {
        return bundle;
    }


    /**
     * View 层会在 ViewModel 创建后调用 init() 方法
     * init() 在构造方法之后调用
     */
    protected abstract void init();

    @Override
    public int uniqueKey() {
        return hashCode();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        X.unRegisterEvent(this);

        // 取消 view model 里面的网络请求
        Api.queue().cancelRequest(this);
    }


}
