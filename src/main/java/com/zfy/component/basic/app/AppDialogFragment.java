package com.zfy.component.basic.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zfy.component.basic.R;
import com.zfy.component.basic.app.data.DialogAttr;
import com.zfy.component.basic.app.view.IBaseView;
import com.zfy.component.basic.app.view.IElegantView;
import com.zfy.component.basic.app.view.IInitFlow;
import com.zfy.component.basic.app.view.IViewConfig;
import com.zfy.component.basic.app.view.ViewConfig;
import com.zfy.component.basic.foundation.X;
import com.zfy.component.basic.foundation.api.IApiAnchor;

/**
 * CreateAt : 16/8/15
 * Describe : dialog fragment 基类
 *
 * @author chendong
 */
public abstract class AppDialogFragment extends DialogFragment
        implements IElegantView, IViewConfig, IBaseView, IInitFlow, IApiAnchor {

    protected View mContentView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.dialog_theme);
    }

    @Override
    public void onStart() {
        super.onStart();
        X.setDialogAttributes(getDialog(), getAttr());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (preViewAttach()) {
            return null;
        }
        mContentView = getViewDelegate().bindFragment(this, inflater, container);
        preInit();
        init();
        getViewDelegate().onHostInit();
        return mContentView;
    }

    @Override
    public <T extends View> T findViewById(int id) {
        return mContentView.findViewById(id);
    }

    // 获取弹窗属性
    protected abstract DialogAttr getAttr();

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
    public void startPage(Intent data, int requestCode) {
        if (requestCode == 0) {
            startActivity(data);
        } else {
            startActivityForResult(data, requestCode);
        }
    }

    @Override
    public int uniqueKey() {
        return hashCode();
    }

    @Override
    public @NonNull
    Bundle getData() {
        return getViewDelegate().getBundle();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getViewDelegate() != null) {
            getViewDelegate().onDestroy();
        }
    }

    @Override
    public void finishPage(Intent intent, int code) {
        X.finishPage(getActivity(), intent, code);
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
