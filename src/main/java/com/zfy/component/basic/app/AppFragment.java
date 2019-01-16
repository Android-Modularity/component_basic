package com.zfy.component.basic.app;

import android.arch.lifecycle.Lifecycle;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.march.common.exts.LogX;
import com.march.common.funcs.Action;
import com.zfy.component.basic.app.view.IBaseView;
import com.zfy.component.basic.app.view.IElegantView;
import com.zfy.component.basic.app.view.IInitFlow;
import com.zfy.component.basic.app.view.IViewConfig;
import com.zfy.component.basic.app.view.ViewConfig;
import com.zfy.component.basic.foundation.Exts;

/**
 * CreateAt : 2018/10/11
 * Describe : Fragment 基类
 *
 * @author chendong
 */
public abstract class AppFragment extends Fragment implements IElegantView, IViewConfig, IBaseView, IInitFlow {

    protected View       mContentView;
    protected LazyLoader mLazyLoader;

    private LazyLoader getLazyLoader() {
        if (mLazyLoader == null) {
            mLazyLoader = new LazyLoader(this, this::lazyLoad);
        }
        return mLazyLoader;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        getLazyLoader().setUserVisibleHint(isVisibleToUser);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(preViewAttach()) {
            return null;
        }
        mContentView = getAppDelegate().bindFragment(this, inflater, container);
        getLazyLoader().onCreateView(inflater, container, savedInstanceState);
        preInit();
        init();
        getAppDelegate().onHostInit();
        return mContentView;
    }


    @Override
    public boolean preViewAttach() {
        return false;
    }

    @Override
    public void preInit() {

    }

    @Override
    public ViewConfig getViewConfig() {
        return null;
    }

    // 懒加载触发
    public void lazyLoad() {
    }

    // elegant view

    @Override
    public void startPage(Intent data, int requestCode) {
        if (requestCode == 0) {
            startActivity(data);
        } else {
            startActivityForResult(data, requestCode);
        }
    }

    @Override
    public @NonNull
    Bundle getData() {
        return getAppDelegate().getBundle();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getAppDelegate() != null) {
            getAppDelegate().onDestroy();
        }
    }


    @Override
    public void finishPage(Intent intent, int code) {
        Exts.finishPage(getActivity(), intent, code);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getAppDelegate().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        getAppDelegate().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    // 负责完成懒加载逻辑
    public static class LazyLoader {

        private boolean  mCanLazyLoad;
        private boolean  mIsPrepared;
        private Fragment mFragment;
        private Action   mLazyLoadAction;

        public LazyLoader(Fragment fragment, Action lazyLoadAction) {
            mFragment = fragment;
            mLazyLoadAction = lazyLoadAction;
        }

        public void setUserVisibleHint(boolean isVisibleToUser) {
            if (isVisibleToUser) {
                lazyLoadInternal();
            }
        }

        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            lazyLoadInternal();
            mIsPrepared = true;
            mCanLazyLoad = true;
            return null;
        }

        private void lazyLoadInternal() {
            if (mFragment.getUserVisibleHint() && mIsPrepared && mCanLazyLoad) {
                mLazyLoadAction.run();
                mCanLazyLoad = false;
            }
        }

        // 设置是否可以懒加载，默认是 true，加载一次后置为 false
        // 如果还需要开启懒加载，手动调用开启
        public void setLazyLoadEnable(boolean enable) {
            mCanLazyLoad = !enable;
        }
    }
}
