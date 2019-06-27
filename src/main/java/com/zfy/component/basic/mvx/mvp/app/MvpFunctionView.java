package com.zfy.component.basic.mvx.mvp.app;

import com.march.common.able.Destroyable;
import com.zfy.component.basic.app.AppFunctionView;
import com.zfy.component.basic.app.view.IBaseView;
import com.zfy.component.basic.app.view.IOnResultView;
import com.zfy.component.basic.app.view.IView;
import com.zfy.component.basic.foundation.api.IApiAnchor;
import com.zfy.component.basic.mvx.mvp.IExtendsMvpView;
import com.zfy.component.basic.mvx.mvp.IMvpPresenter;
import com.zfy.component.basic.mvx.mvp.IMvpView;

/**
 * CreateAt : 2018/10/9
 * Describe : 没有 布局的 View 层，用来做 View 层抽离
 * V 宿主范型
 * P Presenter 范型
 *
 * @author chendong
 */
public abstract class MvpFunctionView<HOST extends IMvpView, P extends IMvpPresenter> extends AppFunctionView<HOST>
        implements IExtendsMvpView<P>, Destroyable, IOnResultView, IBaseView, IApiAnchor, IView {

    private MvpDelegate<P> mDelegate;

    public MvpFunctionView(HOST view) {
        super(view);
    }

    @Override
    public void init() {

    }

    @Override
    public MvpDelegate<P> getViewDelegate() {
        if (mDelegate == null) {
            mDelegate = new MvpDelegate<>();
        }
        return mDelegate;
    }

    @Override
    public P getPresenter() {
        return mDelegate.getPresenter();
    }
}
