package com.zfy.component.basic.mvx.mvvm.app;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zfy.component.basic.Const;
import com.zfy.component.basic.app.AppDelegate;
import com.zfy.component.basic.app.AppFunctionView;
import com.zfy.mantis.api.Mantis;

/**
 * CreateAt : 2018/9/12
 * Describe :
 *
 * @author chendong
 */
public class MvvmDelegate extends AppDelegate {

    public static final String TAG = MvvmDelegate.class.getSimpleName();

    @Override
    public void onAttachHost(Object host) {
        Mantis.inject(Const.VM, host);
        if (host instanceof Fragment) {
            Mantis.inject(Const.ACTIVITY_VM, host);
        }
    }

    @Override
    public View onBindFragment(Fragment owner, LayoutInflater inflater, ViewGroup container) {
        View inflate = inflater.inflate(mViewOpts.getLayout(), container, false);
        bindView(mHost, inflate);
        bindEvent();
        init();
        return inflate;
    }

    @Override
    public void onBindActivity(Activity owner) {
        owner.setContentView(mViewOpts.getLayout());
        bindView(mHost, null);
        bindEvent();
        init();
    }


    @Override
    public void onBindFunctionView(AppFunctionView view, Object host) {
        if (!(view instanceof MvvmFunctionView)) {
            throw new IllegalArgumentException("view must be MvvmFunctionView");
        }
        bindView(mHost, host);
        bindEvent();
        init();
    }

    private void init() {

    }
}
