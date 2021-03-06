package com.zfy.component.basic.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zfy.component.basic.app.view.IBaseView;
import com.zfy.component.basic.app.view.IElegantView;
import com.zfy.component.basic.app.view.IInitFlow;
import com.zfy.component.basic.app.view.IView;
import com.zfy.component.basic.app.view.ViewOpts;
import com.zfy.component.basic.foundation.api.IApiAnchor;

/**
 * CreateAt : 2018/10/11
 * Describe : Fragment 基类
 *
 * @author chendong
 */
public abstract class AppFragment extends Fragment
        implements IElegantView, IView, IBaseView, IInitFlow,IApiAnchor {

    protected View       mContentView;
    protected LazyLoader mLazyLoader;

    protected LazyLoader getLazyLoader() {
        if (mLazyLoader == null) {
            mLazyLoader = new LazyLoader(this);
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
        mContentView = getViewDelegate().bindFragment(this, inflater, container);
        preInit();
        init();
        getViewDelegate().onHostInit();
        LazyLoader lazyLoader = getLazyLoader();
        if (enableLazyLoad() && lazyLoader.mCanLazyLoadNow) {
            lazyLoader.onCreateView(inflater, container, savedInstanceState);
        }
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
    public ViewOpts getViewOpts() {
        return null;
    }

    // 懒加载触发
    public void lazyLoad() {
    }

    // 是否开启懒加载
    public boolean enableLazyLoad() {
        return false;
    }

    // elegant view

    @Override
    public @NonNull
    Bundle getData() {
        return getViewDelegate().getBundle();
    }

    @Override
    public int uniqueKey() {
        return hashCode();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getViewDelegate() != null) {
            getViewDelegate().onDestroy();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getViewDelegate().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        getViewDelegate().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public <T extends View> T findViewById(int id) {
        return mContentView.findViewById(id);
    }

    // 负责完成懒加载逻辑
    public static class LazyLoader {

        private boolean     mIsPrepared;
        private boolean     mCanLazyLoadNow = true;
        private AppFragment mFragment;

        public LazyLoader(AppFragment fragment) {
            mFragment = fragment;
        }

        public void setUserVisibleHint(boolean isVisibleToUser) {
            if (isVisibleToUser) {
                lazyLoadInternal();
            }
        }

        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            mIsPrepared = true;
            lazyLoadInternal();
            return null;
        }

        private void lazyLoadInternal() {
            if (mFragment.getUserVisibleHint() && mIsPrepared && mFragment.enableLazyLoad() && mCanLazyLoadNow) {
                mFragment.lazyLoad();
            }
        }

        public void setCanLazyLoadNow(boolean canLazyLoadNow) {
            mCanLazyLoadNow = canLazyLoadNow;
        }
    }
}
