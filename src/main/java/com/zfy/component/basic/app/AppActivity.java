package com.zfy.component.basic.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.zfy.component.basic.app.view.IBaseView;
import com.zfy.component.basic.app.view.IElegantView;
import com.zfy.component.basic.app.view.IInitFlow;
import com.zfy.component.basic.app.view.IOnResultView;
import com.zfy.component.basic.app.view.IView;
import com.zfy.component.basic.app.view.ViewOpts;
import com.zfy.component.basic.foundation.api.IApiAnchor;

/**
 * CreateAt : 2018/10/11
 * Describe :
 *
 * @author chendong
 */
public abstract class AppActivity extends AppCompatActivity
        implements IElegantView, IView, IBaseView, IInitFlow, IOnResultView, IApiAnchor {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (preViewAttach()) {
            return;
        }
        getViewDelegate().bindActivity(this);
        preInit();
        init();
        getViewDelegate().onHostInit();
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

    // elegant view

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public AppActivity getActivity() {
        return this;
    }


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
    protected void onDestroy() {
        super.onDestroy();
        getViewDelegate().onDestroy();
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
}
