package com.zfy.component.basic.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.march.common.able.Destroyable;
import com.zfy.component.basic.R;
import com.zfy.component.basic.app.data.DialogAttr;
import com.zfy.component.basic.app.view.IBaseView;
import com.zfy.component.basic.app.view.IElegantView;
import com.zfy.component.basic.app.view.IInitFlow;
import com.zfy.component.basic.app.view.IView;
import com.zfy.component.basic.app.view.ViewOpts;
import com.zfy.component.basic.foundation.X;
import com.zfy.component.basic.foundation.api.IApiAnchor;

/**
 * CreateAt : 16/8/15
 * Describe : dialog fragment 基类
 *
 * @author chendong
 */
public abstract class AppDialogFragment extends DialogFragment
        implements IElegantView, IView, IBaseView, IInitFlow, IApiAnchor, Destroyable {

    protected View mContentView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.dialog_theme);
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
    public ViewOpts getViewOpts() {
        return null;
    }

    // elegant view

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getViewDelegate().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        getViewDelegate().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private boolean mAlreadyAddToFt; // 是否已添加到 FragmentTransaction
    private boolean mShowAsDialog; // 是否以 Dialog 显示，不会每次销毁

    public void showAsDialog(FragmentManager fragmentManager) {
        mShowAsDialog = true;
        if (mAlreadyAddToFt) {
            if (!getDialog().isShowing()) {
                getDialog().show();
            }
        } else {
            show(fragmentManager, "dialog" + hashCode());
            mAlreadyAddToFt = true;
        }
    }

    private boolean mNotFirstStart;

    @Override
    public void onStart() {
        boolean showing = this.getDialog().isShowing();
        super.onStart();
        X.setDialogAttributes(getDialog(), getAttr());
        if (mNotFirstStart && !showing) {
            getDialog().dismiss();
        }
        mNotFirstStart = true;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (mShowAsDialog) {
            dialog.dismiss();
        } else {
            super.onDismiss(dialog);
        }
    }

    public void setShowView(IView view) {
        view.getViewDelegate().addDestroyable(this);
    }
}
