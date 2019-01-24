package com.zfy.component.basic.mvx.mvp.app;

import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.march.common.able.Destroyable;
import com.zfy.component.basic.app.view.IOnResultView;
import com.zfy.component.basic.mvx.mvp.IMvpPresenter;
import com.zfy.component.basic.mvx.mvp.IMvpView;
import com.zfy.component.basic.mvx.mvp.IExtendsMvpView;

/**
 * CreateAt : 2018/10/9
 * Describe : 没有 布局的 View 层，用来做 View 层抽离
 * V 宿主范型
 * P Presenter 范型
 *
 * @author chendong
 */
public class MvpPluginView<HOST extends IMvpView, P extends IMvpPresenter> implements IExtendsMvpView<P>, Destroyable, IOnResultView {

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

    @Override
    public void startPage(Intent data, int code) {
        mHostView.startPage(data, code);
    }

    @NonNull
    @Override
    public Bundle getData() {
        return mDelegate.getBundle();
    }

    @Override
    public void finishPage(Intent intent, int code) {
        mHostView.finishPage(intent, code);
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
}
