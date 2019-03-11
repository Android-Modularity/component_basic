package com.zfy.component.basic.mvx.mvp.app;

import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.march.common.able.Destroyable;
import com.zfy.component.basic.app.view.IBaseView;
import com.zfy.component.basic.app.view.IOnResultView;
import com.zfy.component.basic.foundation.api.IApiAnchor;
import com.zfy.component.basic.mvx.mvp.IExtendsMvpView;
import com.zfy.component.basic.mvx.mvp.IMvpPresenter;
import com.zfy.component.basic.mvx.mvp.IMvpView;

/**
 * CreateAt : 2018/10/9
 * Describe : 没有 布局的 View 层，用来做 View 层抽离
 * V 宿主范型
 * P Presenter 范型
 *
 * @author chendong
 */
public class MvpPluginView<HOST extends IMvpView, P extends IMvpPresenter>
        implements IExtendsMvpView<P>, Destroyable, IOnResultView, IBaseView, IApiAnchor {

    protected MvpDelegate<P> mDelegate = new MvpDelegate<>();

    protected HOST mHostView;

    public MvpPluginView(HOST view) {
        mHostView = view;
        mDelegate.bindPluginView(this, mHostView);
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mHostView.getLifecycle();
    }

    @Override
    public Context getContext() {
        return mHostView.getContext();
    }

    @Override
    public Activity getActivity() {
        return mHostView.getActivity();
    }

    @NonNull
    @Override
    public Bundle getData() {
        return mDelegate.getBundle();
    }

    @Override
    public P getPresenter() {
        return mDelegate.getPresenter();
    }

    @Override
    public void onDestroy() {
        mDelegate.onDestroy();
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
        return null;
    }

    @Override
    public int uniqueKey() {
        if (mHostView instanceof IApiAnchor) {
            return ((IApiAnchor) mHostView).uniqueKey();
        }
        return hashCode();
    }
}
