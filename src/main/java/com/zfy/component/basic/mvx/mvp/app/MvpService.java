package com.zfy.component.basic.mvx.mvp.app;

import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.zfy.component.basic.app.AppDelegate;
import com.zfy.component.basic.app.AppService;
import com.zfy.component.basic.app.IDelegate;
import com.zfy.component.basic.mvx.mvp.IMvpPresenter;
import com.zfy.component.basic.mvx.mvp.IExtendsMvpView;

/**
 * CreateAt : 2018/11/22
 * Describe :
 *
 * @author chendong
 */
public abstract class MvpService<P extends IMvpPresenter> extends AppService implements IExtendsMvpView<P> {

    private MvpDelegate<P> mAppDelegate = new MvpDelegate<>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public P getPresenter() {
        return mAppDelegate.getPresenter();
    }

    @Override
    public IDelegate getViewDelegate() {
        return mAppDelegate;
    }
}
