package com.zfy.component.basic.app;

import android.arch.lifecycle.Lifecycle;
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
import com.zfy.component.basic.app.view.IViewConfig;
import com.zfy.component.basic.app.view.ViewConfig;
import com.zfy.component.basic.foundation.api.Api;
import com.zfy.component.basic.mvx.def.AppDelegateImpl;

/**
 * CreateAt : 2018/10/11
 * Describe :
 *
 * @author chendong
 */
public abstract class AppActivity extends AppCompatActivity implements IElegantView, IViewConfig, IBaseView, IInitFlow, IOnResultView {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (preViewAttach()) {
            return;
        }
        getAppDelegate().bindActivity(this);
        preInit();
        init();
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
    public Lifecycle getLifecycle() {
        return getAppDelegate().getLifecycle();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        getAppDelegate().onDestroy();
        Api.cancelSelfRequest(hashCode());
    }

    @Override
    public void finishPage(Intent intent, int code) {
        if (intent != null) {
            setResult(code, intent);
        }
        finish();
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
}
