package com.zfy.component.basic.mvx.mvp.app;

import com.zfy.component.basic.app.AppDelegate;
import com.zfy.component.basic.app.AppDialogFragment;
import com.zfy.component.basic.mvx.mvp.IMvpPresenter;
import com.zfy.component.basic.mvx.mvp.IExtendsMvpView;

/**
 * CreateAt : 2018/10/11
 * Describe :
 *
 * @author chendong
 */
public abstract class MvpDialogFragment<P extends IMvpPresenter> extends AppDialogFragment
        implements IExtendsMvpView {

    protected MvpDelegate<P> mDelegate = new MvpDelegate<>();

    @Override
    public AppDelegate getAppDelegate() {
        return mDelegate;
    }

    @Override
    public P getPresenter() {
        return mDelegate.getPresenter();
    }
}
