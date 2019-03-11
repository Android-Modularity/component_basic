package com.zfy.component.basic.app;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.march.common.x.LogX;
import com.zfy.component.basic.R;
import com.zfy.component.basic.foundation.X;

import butterknife.ButterKnife;

/**
 * CreateAt : 16/8/15
 * Describe : dialog基类
 * 第1次 ： 父类构造方法 -> 子类构造方法 -> initOnCreate -> show() -> onCreated() -> initViewOnCreate()
 * 第2次 ： show()
 *
 * View/Event
 * initOnCreate
 *
 * @author chendong
 */
@Deprecated
public abstract class AppDialog2 extends AppCompatDialog {

    protected int MATCH = ViewGroup.LayoutParams.MATCH_PARENT;
    protected int WRAP  = ViewGroup.LayoutParams.WRAP_CONTENT;

    public AppDialog2(Context context) {
        this(context, R.style.dialog_theme);
    }

    public AppDialog2(Context context, int theme) {
        super(context, theme);
        LogX.e("chendong", "super 构造方法");
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        X.registerEvent(this);
        LogX.e("chendong", "super initOnCreate");
        initOnConstruct();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewOnCreate();
        setWindowParams();
    }

    protected abstract void initViewOnCreate();

    protected void initOnConstruct() {

    }

    protected abstract int getLayoutId();

    protected abstract void setWindowParams();

    /* 设置从底部到中间的动画 */
    protected void setAnimationBottomToCenter() {
        Window window = getWindow();
        if (window == null) {
            return;
        }
        window.setWindowAnimations(R.style.dialog_anim_bottom_center);
    }

    /* 全部参数设置属性 */
    protected void setDialogAttributes(int width, int height, float alpha, float dim, int gravity) {
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        Window window = getWindow();
        if (window == null) {
            return;
        }
        WindowManager.LayoutParams params = window.getAttributes();
        // 设置布局的透明度，0为透明，1为实际颜色,该透明度会使layout里的所有空间都有透明度，不仅仅是布局最底层的view
        params.alpha = alpha;
        // 窗口的背景，0为透明，1为全黑
        params.dimAmount = dim;
        params.width = width;
        params.height = height;
        params.gravity = gravity;
        window.setAttributes(params);
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    /**
     * alpha=1;
     * dim=0.6;
     *
     * @param width   弹窗的宽度
     * @param height  弹窗的高度
     * @param gravity 弹窗的位置
     */
    protected void setDialogAttributes(int width, int height, int gravity) {
        setDialogAttributes(width, height, 1f, .6f, gravity);
    }

    /**
     * 宽度 march;
     * 高度 wrap;
     * alpha=1;
     * dim=0.6;
     * gravity=center;
     */
    protected void setDialogAttributes() {
        setDialogAttributes(MATCH, WRAP, 1f, .6f, Gravity.CENTER);
    }

    protected <V extends View> V getView(int id) {
        return findViewById(id);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        X.unRegisterEvent(this);
    }
}
