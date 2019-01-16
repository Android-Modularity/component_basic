package com.zfy.component.basic.app;

import android.app.Activity;
import android.app.Service;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.march.common.able.Destroyable;
import com.march.common.exts.EmptyX;
import com.march.common.exts.ListX;
import com.zfy.component.basic.ComponentX;
import com.zfy.component.basic.app.view.IOnResultView;
import com.zfy.component.basic.app.view.IViewConfig;
import com.zfy.component.basic.app.view.ViewConfig;
import com.zfy.component.basic.foundation.Exts;
import com.zfy.component.basic.mvx.mvp.IMvpView;
import com.zfy.component.basic.mvx.mvp.app.NoLayoutMvpView;

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
public abstract class AppDelegate implements Destroyable, LifecycleOwner, IOnResultView {

    // 数据
    protected Bundle            mBundle;
    // 声明周期管理
    protected LifecycleOwner    mLifecycleOwner;

    protected Object     mHost;
    protected ViewConfig mViewConfig;
    private   Unbinder   mUnBinder;

    private List<Destroyable>   mDestroyableList;
    private List<Disposable>    mDisposables;
    private List<IOnResultView> mOnResultViews;

    public void addObserver(@NonNull LifecycleObserver observer) {
        getLifecycle().addObserver(observer);
    }

    public void addOnResultView(IOnResultView view) {
        if (mOnResultViews == null) {
            mOnResultViews = new ArrayList<>();
        }
        mOnResultViews.add(view);
    }

    public void addDisposable(Disposable disposable) {
        if (mDisposables == null) {
            mDisposables = new ArrayList<>();
        }
        mDisposables.add(disposable);
    }

    public void addDestroyable(Destroyable destroyable) {
        if (mDestroyableList == null) {
            mDestroyableList = new ArrayList<>();
        }
        mDestroyableList.add(destroyable);
    }

    public void onHostInit() {

    }

    public View bindFragment(Fragment appFragment, LayoutInflater inflater, ViewGroup container) {
        mBundle = appFragment.getArguments();
        attachHost(appFragment);
        return onBindFragment(appFragment, inflater, container);
    }

    public void bindService(AppService appService) {
        attachHost(appService);
        onBindService(appService);
    }

    public void bindActivity(AppActivity appActivity) {
        mBundle = appActivity.getIntent().getExtras();
        attachHost(appActivity);
        onBindActivity(appActivity);
    }

    public void bindNoLayoutView(NoLayoutMvpView noLayoutMvpView, Object host) {
        if (host instanceof IMvpView) {
            mBundle = ((IMvpView) host).getData();
        }
        // NoLayoutView 绑定到 Host 生命周期等
        AppDelegate hostDelegate = null;
        if (host instanceof AppActivity) {
            hostDelegate = ((AppActivity) host).getAppDelegate();
        } else if (host instanceof AppFragment) {
            hostDelegate = ((AppFragment) host).getAppDelegate();
        }
        if (hostDelegate != null) {
            hostDelegate.addDestroyable(noLayoutMvpView);
            hostDelegate.addOnResultView(noLayoutMvpView);
        }
        attachHost(noLayoutMvpView);
        onBindNoLayout(noLayoutMvpView, host);
    }

    protected void onAttachHost(Object host) {

    }

    protected View onBindFragment(Fragment owner, LayoutInflater inflater, ViewGroup container) {
        return null;
    }

    protected void onBindActivity(Activity owner) {

    }

    public void onBindNoLayout(LifecycleOwner owner, Object host) {

    }

    protected void onBindService(Service owner) {

    }

    public Bundle getBundle() {
        if (mBundle == null) {
            mBundle = new Bundle();
        }
        return mBundle;
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
        } else if (host instanceof NoLayoutMvpView) {
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
        Exts.registerEvent(mHost);
    }

    @Override
    public void onDestroy() {
        Exts.unRegisterEvent(mHost);
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

    private <T extends LifecycleOwner> void attachHost(T host) {
        ComponentX.inject(host);
        mHost = host;
        mLifecycleOwner = host;
//        Lifecycle lifecycle = host.getLifecycle();
//        if (lifecycle instanceof LifecycleRegistry) {
//            mLifecycleRegistry = (LifecycleRegistry) lifecycle;
//        } else {
//            mLifecycleRegistry = new LifecycleRegistry(host);
//        }
        if (host instanceof IViewConfig && ((IViewConfig) host).getViewConfig() != null) {
            mViewConfig = ((IViewConfig) host).getViewConfig();
        }
        onAttachHost(host);
        if (mViewConfig == null) {
            throw new IllegalStateException("require ViewConfig");
        }
    }
}
