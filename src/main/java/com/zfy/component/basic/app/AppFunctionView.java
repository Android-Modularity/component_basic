package com.zfy.component.basic.app;

import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.march.common.able.Destroyable;
import com.zfy.component.basic.app.view.IBaseView;
import com.zfy.component.basic.app.view.IElegantView;
import com.zfy.component.basic.app.view.IOnResultView;
import com.zfy.component.basic.app.view.IView;
import com.zfy.component.basic.app.view.ViewOpts;
import com.zfy.component.basic.foundation.api.IApiAnchor;

/**
 * CreateAt : 2018/10/9
 * Describe : 没有 布局的 View 层，用来做 View 层抽离
 * V 宿主范型
 * P Presenter 范型
 *
 * @author chendong
 */
public abstract class AppFunctionView<HOST>
        implements LifecycleOwner, IElegantView, Destroyable, IOnResultView, IBaseView, IApiAnchor, IView {

    protected HOST mHostView;

    public AppFunctionView(HOST view) {
        mHostView = view;
        getViewDelegate().bindFunctionView(this, view);
        init();
    }

    public abstract void init();

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        if (mHostView instanceof LifecycleOwner) {
            return ((LifecycleOwner) mHostView).getLifecycle();
        }
        return new LifecycleRegistry(this);
    }

    @Override
    public Context getContext() {
        if (mHostView instanceof IElegantView) {
            return ((IElegantView) mHostView).getContext();
        }
        throw new IllegalStateException("HOST should impl IElegantView to getContext()");
    }

    @Override
    public Activity getActivity() {
        if (mHostView instanceof IElegantView) {
            return ((IElegantView) mHostView).getActivity();
        }
        throw new IllegalStateException("HOST should impl IElegantView to getActivity()");
    }

    @NonNull
    @Override
    public Bundle getData() {
        return getViewDelegate().getBundle();
    }

    @Override
    public void onDestroy() {
        mHostView = null;
        getViewDelegate().onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    }

    @Override
    public <T extends View> T findViewById(int id) {
        if (mHostView instanceof IBaseView) {
            return ((IBaseView) mHostView).findViewById(id);
        }
        throw new IllegalStateException("HOST should impl IBaseView to findViewById()");
    }

    @Override
    public int uniqueKey() {
        if (mHostView instanceof IApiAnchor) {
            return ((IApiAnchor) mHostView).uniqueKey();
        }
        return hashCode();
    }

    @Override
    public ViewOpts getViewOpts() {
        return null;
    }

    public HOST getHostView() {
        return mHostView;
    }

}
