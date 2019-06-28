package com.zfy.component.basic.mvx.mvp.app;

import android.app.Activity;
import android.app.Service;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.march.common.x.LogX;
import com.zfy.component.basic.ComponentX;
import com.zfy.component.basic.app.AppDelegate;
import com.zfy.component.basic.app.AppFunctionView;
import com.zfy.component.basic.app.view.ViewOpts;
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
    public void onAttachHost(Object host) {
        MvpV annotation = mHost.getClass().getAnnotation(MvpV.class);
        if (annotation != null) {
            int layout = annotation.layout();
            Class pClazz = annotation.p();
            if (layout != 0) {
                mViewOpts = ViewOpts.makeMvp(layout, pClazz);
            }
        }
    }

    @Override
    protected View onBindFragment(android.support.v4.app.Fragment owner, LayoutInflater inflater, ViewGroup container) {
        View inflate = inflater.inflate(mViewOpts.getLayout(), container, false);
        bindView(mHost, inflate);
        init();
        return inflate;
    }

    @Override
    public void onBindActivity(Activity owner) {
        try {
            owner.setContentView(mViewOpts.getLayout());
            bindView(mHost, null);
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onBindService(Service owner) {
        init();
    }

    @Override
    public void onBindFunctionView(AppFunctionView view, Object host) {
        if (!(view instanceof MvpFunctionView)) {
            throw new IllegalArgumentException("view must be AppFunctionView");
        }
        bindView(mHost, host);
        init();
    }

    @SuppressWarnings("unchecked")
    private void init() {
        if (mHost instanceof IMvpView) {
            Class pClazz = mViewOpts.getpClazz();
            try {
                if(pClazz !=null) {
                    mPresenter = (P) pClazz.newInstance();
                    addObserver(mPresenter);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (getPresenter() != null && getPresenter() instanceof MvpPresenter) {
                MvpPresenter presenter = (MvpPresenter) getPresenter();
                presenter.attachView((IMvpView) mHost);
                ComponentX.inject(presenter);
                presenter.init();
            }
        } else {
            LogX.e(TAG, "Host not IMvpView");
        }
    }

    @Override
    public void onHostInit() {
        super.onHostInit();
        if (getPresenter() != null) {
            getPresenter().onViewInit();
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
