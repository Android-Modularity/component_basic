package com.zfy.component.basic.app;

import android.app.Activity;
import android.app.Service;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.march.common.mgrs.ActivityMgr;
import com.zfy.component.basic.app.view.IElegantView;
import com.zfy.component.basic.app.view.IViewConfig;
import com.zfy.component.basic.app.view.ViewConfig;
import com.zfy.component.basic.foundation.api.Api;

import org.greenrobot.eventbus.Subscribe;

/**
 * CreateAt : 2018/10/11
 * Describe : Service
 *
 * @author chendong
 */
public abstract class AppService extends Service implements IElegantView, IViewConfig, LifecycleOwner {

    protected abstract void init();

    @Override
    public void onCreate() {
        super.onCreate();
        getAppDelegate().bindService(this);
        init();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getAppDelegate().onDestroy();
        Api.cancelSelfRequest(hashCode());
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
        return getAppDelegate().getLifecycle();
    }
}
