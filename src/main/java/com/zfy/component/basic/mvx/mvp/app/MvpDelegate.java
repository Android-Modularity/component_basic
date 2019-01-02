package com.zfy.component.basic.mvx.mvp.app;

import android.app.Activity;
import android.app.Fragment;
import android.app.Service;
import android.arch.lifecycle.LifecycleOwner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.march.common.exts.LogX;
import com.zfy.component.basic.app.AppDelegate;
import com.zfy.component.basic.app.view.ViewConfig;
import com.zfy.component.basic.mvx.mvp.IMvpPresenter;
import com.zfy.component.basic.mvx.mvp.IMvpView;
import com.zfy.component.basic.mvx.mvp.presenter.MvpPresenter;

/**
 * CreateAt : 2018/9/12
 * Describe :
 *
 * @author chendong
 */
public class MvpDelegate<P extends IMvpPresenter> extends AppDelegate {

    public static final String TAG = MvpDelegate.class.getSimpleName();

    private P mPresenter;

    @Override
    protected void onAttachHost(Object host) {
        if (mViewConfig == null) {
            MvpV annotation = mHost.getClass().getAnnotation(MvpV.class);
            if (annotation != null) {
                int layout = annotation.layout();
                Class pClazz = annotation.p();
                if (layout != 0) {
                    mViewConfig = ViewConfig.makeMvp(layout, pClazz);
                }
            }
        }
    }

    @Override
    protected View onBindFragment(android.support.v4.app.Fragment owner, LayoutInflater inflater, ViewGroup container) {
        View inflate = inflater.inflate(mViewConfig.getLayout(), container, false);
        bindView(mHost, inflate);
        bindEvent();
        init();
        return inflate;
    }

    @Override
    public void onBindActivity(Activity owner) {
        ((Activity) owner).setContentView(mViewConfig.getLayout());
        bindView(mHost, null);
        bindEvent();
        init();
    }

    @Override
    protected void onBindService(Service owner) {
        bindEvent();
        init();
    }

    @Override
    public void onBindNoLayout(LifecycleOwner owner, Object binder) {
        if (!(owner instanceof NoLayoutMvpView)) {
            throw new IllegalArgumentException("owner must be no layout view");
        }
        bindView(mHost, binder);
        bindEvent();
        init();
    }

    @SuppressWarnings("unchecked")
    private void init() {
        if (mHost instanceof IMvpView) {
            Class pClazz = mViewConfig.getpClazz();
            try {
                if(pClazz !=null) {
                    mPresenter = (P) pClazz.newInstance();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (getPresenter() != null && getPresenter() instanceof MvpPresenter) {
                MvpPresenter presenter = (MvpPresenter) getPresenter();
                presenter.attachView((IMvpView) mHost);
                presenter.init();
            }
        } else {
            LogX.e(TAG, "Host not IMvpView");
        }
    }

    public P getPresenter() {
        return mPresenter;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getPresenter() != null) {
            getPresenter().onDestroy();
        }
    }


    public static class NoPresenter extends MvpPresenter {

        @Override
        public void init() {

        }
    }
}
