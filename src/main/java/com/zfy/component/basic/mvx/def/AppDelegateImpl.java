package com.zfy.component.basic.mvx.def;

import android.app.Activity;
import android.app.Service;
import android.arch.lifecycle.LifecycleOwner;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zfy.component.basic.app.AppDelegate;
import com.zfy.component.basic.mvx.mvp.app.NoLayoutMvpView;

/**
 * CreateAt : 2018/9/12
 * Describe :
 *
 * @author chendong
 */
public class AppDelegateImpl extends AppDelegate {

    public static final String TAG = AppDelegateImpl.class.getSimpleName();

    @Override
    protected void onAttachHost(Object host) {

    }

    @Override
    protected View onBindFragment(Fragment owner, LayoutInflater inflater, ViewGroup container) {
        View inflate = inflater.inflate(mViewConfig.getLayout(), container, false);
        bindView(mHost, inflate);
        bindEvent();
        return inflate;
    }

    @Override
    public void onBindActivity(Activity owner) {
        owner.setContentView(mViewConfig.getLayout());
        bindView(mHost, null);
        bindEvent();
    }

    @Override
    protected void onBindService(Service owner) {
        bindEvent();
    }

    @Override
    public void onBindNoLayout(LifecycleOwner owner, Object binder) {
        if (!(owner instanceof NoLayoutMvpView)) {
            throw new IllegalArgumentException("owner must be no layout view");
        }
        bindView(mHost, binder);
        bindEvent();
    }

}
