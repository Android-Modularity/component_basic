package com.zfy.component.basic.mvx.mvp.app;

import com.zfy.component.basic.app.AppActivity;
import com.zfy.component.basic.app.IDelegate;
import com.zfy.component.basic.mvx.mvp.IExtendsMvpView;
import com.zfy.component.basic.mvx.mvp.IMvpPresenter;

/**
 * CreateAt : 2018/10/11
 * Describe :
 *
 * @author chendong
 */
public abstract class MvpActivity<P extends IMvpPresenter> extends AppActivity implements IExtendsMvpView<P> {

    protected MvpDelegate<P> mDelegate = new MvpDelegate<>();

    @Override
    public IDelegate getViewDelegate() {
        return mDelegate;
    }

    @Override
    public P getPresenter() {
        return mDelegate.getPresenter();
    }
}
