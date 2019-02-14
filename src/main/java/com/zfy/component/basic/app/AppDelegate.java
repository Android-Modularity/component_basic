package com.zfy.component.basic.app;

import android.app.Activity;
import android.app.Service;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.march.common.able.Destroyable;
import com.march.common.x.EmptyX;
import com.march.common.x.ListX;
import com.march.common.x.RecycleX;
import com.zfy.component.basic.ComponentX;
import com.zfy.component.basic.app.view.IBaseView;
import com.zfy.component.basic.app.view.IOnResultView;
import com.zfy.component.basic.app.view.IViewConfig;
import com.zfy.component.basic.app.view.ViewConfig;
import com.zfy.component.basic.foundation.X;
import com.zfy.component.basic.foundation.api.Api;
import com.zfy.component.basic.foundation.api.IApiAnchor;
import com.zfy.component.basic.mvx.mvp.IMvpView;
import com.zfy.component.basic.mvx.mvp.app.MvpPluginView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;

/**
 * CreateAt : 2018/10/11
 * Describe :
 *
 * @author chendong
 */
public abstract class AppDelegate implements IDelegate {

    // 数据
    protected Bundle            mBundle;
    // 声明周期管理
    protected LifecycleOwner    mLifecycleOwner;

    private Handler mHandler;

    protected Object     mHost;
    protected ViewConfig mViewConfig;
    private   Unbinder   mUnBinder;

    private List<Destroyable>   mDestroyableList;
    private List<Disposable>    mDisposables;
    private List<IOnResultView> mOnResultViews;


    @Override
    public void addObserver(@NonNull LifecycleObserver observer) {
        getLifecycle().addObserver(observer);
    }

    @Override
    public void addOnResultView(IOnResultView view) {
        if (mOnResultViews == null) {
            mOnResultViews = new ArrayList<>();
        }
        mOnResultViews.add(view);
    }

    @Override
    public void addDisposable(Disposable disposable) {
        if (mDisposables == null) {
            mDisposables = new ArrayList<>();
        }
        mDisposables.add(disposable);
    }

    @Override
    public void addDestroyable(Destroyable destroyable) {
        if (mDestroyableList == null) {
            mDestroyableList = new ArrayList<>();
        }
        mDestroyableList.add(destroyable);
    }

    @Override
    public void clickView(View.OnClickListener listener, int... viewIds) {
        for (int viewId : viewIds) {
            View view;
            if (mHost instanceof IBaseView) {
                view = ((IBaseView) mHost).findViewById(viewId);
                if (view != null) {
                    view.setOnClickListener(listener);
                }
            }
        }
    }

    @Override
    public void onHostInit() {

    }

    @Override
    public View bindFragment(Fragment appFragment, LayoutInflater inflater, ViewGroup container) {
        mBundle = appFragment.getArguments();
        attachHost(appFragment);
        return onBindFragment(appFragment, inflater, container);
    }

    @Override
    public void bindService(AppService appService) {
        attachHost(appService);
        onBindService(appService);
    }

    @Override
    public void bindActivity(AppActivity appActivity) {
        mBundle = appActivity.getIntent().getExtras();
        attachHost(appActivity);
        onBindActivity(appActivity);
    }

    @Override
    public void bindPluginView(MvpPluginView mvpPluginView, Object host) {
        if (host instanceof IMvpView) {
            mBundle = ((IMvpView) host).getData();
        }
        // NoLayoutView 绑定到 Host 生命周期等
        IDelegate hostDelegate = null;
        if (host instanceof AppActivity) {
            hostDelegate = ((AppActivity) host).getViewDelegate();
        } else if (host instanceof AppFragment) {
            hostDelegate = ((AppFragment) host).getViewDelegate();
        }
        if (hostDelegate != null) {
            hostDelegate.addDestroyable(mvpPluginView);
            hostDelegate.addOnResultView(mvpPluginView);
        }
        attachHost(mvpPluginView);
        onBindPluginView(mvpPluginView, host);
    }

    @Override
    public void onAttachHost(Object host) {

    }


    @Override
    public Bundle getBundle() {
        if (mBundle == null) {
            mBundle = new Bundle();
        }
        return mBundle;
    }

    @Override
    public void onDestroy() {
        X.unRegisterEvent(mHost);
        if (mUnBinder != null) {
            mUnBinder.unbind();
            mUnBinder = null;
        }
        if (!EmptyX.isEmpty(mDestroyableList)) {
            ListX.foreach(mDestroyableList, Destroyable::onDestroy);
            mDestroyableList.clear();
        }
        if (!EmptyX.isEmpty(mDisposables)) {
            ListX.foreach(mDisposables, Disposable::dispose);
            mDisposables.clear();
        }
        RecycleX.recycle(mDisposables, mDestroyableList, mOnResultViews);
        RecycleX.recycle(mHandler);
        Api.queue().cancelRequest(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        ListX.foreach(mOnResultViews, view -> view.onRequestPermissionsResult(requestCode, permissions, grantResults));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ListX.foreach(mOnResultViews, view -> view.onActivityResult(requestCode, resultCode, data));
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mLifecycleOwner.getLifecycle();
    }


    @Override
    public Handler post(Runnable runnable, long delay) {
        if (mHandler == null) {
            mHandler = new Handler(Looper.getMainLooper());
        }
        if (Looper.getMainLooper() == Looper.myLooper() && delay == 0) {
            runnable.run();
        } else {
            mHandler.postDelayed(runnable, delay);
        }
        return mHandler;
    }

    @Override
    public int uniqueKey() {
        if (mHost instanceof IApiAnchor) {
            return ((IApiAnchor) mHost).uniqueKey();
        } else {
            return mHost.hashCode();
        }
    }

    protected View onBindFragment(Fragment owner, LayoutInflater inflater, ViewGroup container) {
        return null;
    }

    protected void onBindActivity(Activity owner) {

    }

    public void onBindPluginView(LifecycleOwner owner, Object host) {

    }

    protected void onBindService(Service owner) {

    }

    /**
     * @param host   当前需要绑定的 对象
     * @param binder findViewById 对象
     */
    protected void bindView(Object host, Object binder) {
        if (host instanceof AppActivity) {
            mUnBinder = ButterKnife.bind((AppActivity) host);
        } else if (host instanceof AppFragment && binder instanceof View) {
            mUnBinder = ButterKnife.bind(host, (View) binder);
        } else if (host instanceof AppDialogFragment && binder instanceof View) {
            mUnBinder = ButterKnife.bind(host, (View) binder);
        } else if (host instanceof MvpPluginView) {
            if (binder instanceof AppActivity) {
                mUnBinder = ButterKnife.bind(host, (AppActivity) binder);
            } else if (binder instanceof AppFragment) {
                mUnBinder = ButterKnife.bind(host, ((AppFragment) binder).mContentView);
            } else if (binder instanceof AppDialogFragment) {
                mUnBinder = ButterKnife.bind(host, ((AppDialogFragment) binder).mContentView);
            } else if (binder instanceof View) {
                mUnBinder = ButterKnife.bind(host, (View) binder);
            }
        }
    }

    protected void bindEvent() {
        X.registerEvent(mHost);
    }


    private <T extends LifecycleOwner> void attachHost(T host) {
        // host
        ComponentX.inject(host);
        mHost = host;
        mLifecycleOwner = host;
        if (host instanceof IViewConfig && ((IViewConfig) host).getViewConfig() != null) {
            mViewConfig = ((IViewConfig) host).getViewConfig();
        }
        onAttachHost(host);
        if (mViewConfig == null) {
            throw new IllegalStateException("require ViewConfig");
        }
    }


}
