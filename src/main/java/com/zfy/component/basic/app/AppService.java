package com.zfy.component.basic.app;

import android.app.Activity;
import android.app.Service;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.march.common.mgrs.ActivityMgr;
import com.zfy.component.basic.app.view.IElegantView;
import com.zfy.component.basic.app.view.IViewConfig;
import com.zfy.component.basic.app.view.ViewConfig;
import com.zfy.component.basic.foundation.api.Api;
import com.zfy.component.basic.foundation.api.IApiAnchor;

/**
 * CreateAt : 2018/10/11
 * Describe : Service
 *
 * @author chendong
 */
public abstract class AppService extends Service implements IElegantView, IViewConfig, LifecycleOwner, IApiAnchor {

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

    @Override
    public void startPage(Intent data, int code) {
        Activity topActivity = ActivityMgr.getInst().getTopActivity();
        if (topActivity instanceof AppActivity) {
            ((AppActivity) topActivity).startPage(data, code);
        }
    }

    @NonNull
    @Override
    public Bundle getData() {
        return new Bundle();
    }

    @Override
    public void finishPage(Intent intent, int code) {
        throw new UnsupportedOperationException("service unSupport finish UI");
    }

    @Override
    public ViewConfig getViewConfig() {
        return ViewConfig.makeEmpty();
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
