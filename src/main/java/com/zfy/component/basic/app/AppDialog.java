package com.zfy.component.basic.app;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;

import com.zfy.component.basic.ComponentX;
import com.zfy.component.basic.R;
import com.zfy.component.basic.app.data.DialogAttr;
import com.zfy.component.basic.foundation.X;

import butterknife.ButterKnife;

/**
 * CreateAt : 16/8/15
 * Describe : dialog基类
 * 第1次 ： 绑定视图和事件 -> initOnCreate() -> show() -> onCreate() -> setAttr() -> initShow()
 * 第2次 ： show() -> initOnShow()
 *
 * 仅绑定一次可在 initOnCreate() 进行
 * 每次数据更新都进行数据绑定可在 initOnShow() 进行
 * @author chendong
 */
public abstract class AppDialog extends AppCompatDialog {

    private Bundle mArguments;

    public AppDialog(Context context, Bundle arguments) {
        this(context, R.style.dialog_theme, arguments);
    }

    public AppDialog(Context context, int theme, Bundle arguments) {
        super(context, theme);
        this.mArguments = arguments;
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        X.registerEvent(this);
    }

    // 仅在第一次 show 时调用
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        X.setDialogAttributes(this, getAttr());

        ComponentX.inject(this);


        initOnCreate();
    }

    // 构造方法里面调用，此时 View/Event 已经绑定
    protected abstract void initOnCreate();

    // 每次 show 都会调用
    protected abstract void initOnShow();

    // 获取布局文件
    protected abstract int getLayoutId();

    // 获取弹窗属性
    protected abstract DialogAttr getAttr();

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        X.unRegisterEvent(this);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    public Bundle getArguments() {
        if (mArguments == null) {
            mArguments = new Bundle();
        }
        return mArguments;
    }

    @Override
    public void show() {
        super.show();
        initOnShow();
    }

    // 更新数据后显示
    public void show(Bundle arguments) {
        this.mArguments = arguments;
        show();
    }
}
