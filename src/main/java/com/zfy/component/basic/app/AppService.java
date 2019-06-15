package com.zfy.component.basic.app;

import android.app.Activity;
import android.app.Service;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.march.common.mgrs.ActivityMgr;
import com.zfy.component.basic.app.view.IElegantView;
import com.zfy.component.basic.app.view.IView;
import com.zfy.component.basic.app.view.ViewOpts;
import com.zfy.component.basic.foundation.api.IApiAnchor;

/**
 * CreateAt : 2018/10/11
 * Describe : Service
 *
 * @author chendong
 */
public abstract class AppService extends Service implements IElegantView, IView, LifecycleOwner, IApiAnchor {

    private LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);

    protected abstract void init();

    @Override
    public void onCreate() {
        super.onCreate();
        getViewDelegate().bindService(this);
        init();
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
        getViewDelegate().onHostInit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getViewDelegate().onDestroy();
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public Activity getActivity() {
        return ActivityMgr.getInst().getTopActivity();
    }

    @NonNull
    @Override
    public Bundle getData() {
        return new Bundle();
    }

    @Override
    public ViewOpts getViewOpts() {
        return ViewOpts.makeEmpty();
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mLifecycleRegistry;
    }

    @Override
    public int uniqueKey() {
        return hashCode();
    }

}
