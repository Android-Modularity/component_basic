package com.zfy.component.basic.app;

import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.march.common.able.Destroyable;
import com.zfy.component.basic.app.view.IOnResultView;
import com.zfy.component.basic.foundation.api.IApiAnchor;
import com.zfy.component.basic.mvx.mvp.app.MvpPluginView;

import io.reactivex.disposables.Disposable;

/**
 * CreateAt : 2018/10/11
 * Describe :
 *
 * @author chendong
 */
public interface IDelegate extends Destroyable, LifecycleOwner, IOnResultView, IApiAnchor {

    void clickView(View.OnClickListener listener, int... viewIds);

    void addObserver(@NonNull LifecycleObserver observer);

    void addOnResultView(IOnResultView view);

    void addDisposable(Disposable disposable);

    void addDestroyable(Destroyable destroyable);

    void onHostInit();

    View bindFragment(Fragment appFragment, LayoutInflater inflater, ViewGroup container);

    void bindService(AppService appService);

    void bindActivity(AppActivity appActivity);

    void bindPluginView(MvpPluginView noLayoutMvpView, Object host);

    void onAttachHost(Object host);

    @NonNull
    Bundle getBundle();

    Handler post(Runnable runnable, long delay);
}
