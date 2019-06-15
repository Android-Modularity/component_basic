package com.zfy.component.basic.mvx.mvvm.app;

import com.zfy.component.basic.app.AppActivity;
import com.zfy.component.basic.mvx.mvvm.IMvvmView;
import com.zfy.component.basic.mvx.mvvm.MvvmViewModel;

/**
 * CreateAt : 2018/9/11
 * Describe : Mvvm Binding Activity
 *
 * @author chendong
 */
public abstract class MvvmActivity<VM extends MvvmViewModel>
        extends AppActivity implements IMvvmView<VM> {

    protected MvvmDelegate<VM> mDelegate = new MvvmDelegate<>();

    @Override
    public MvvmDelegate<VM> getViewDelegate() {
        return mDelegate;
    }

    @Override
    public VM viewModel() {
        return mDelegate.viewModel();
    }
}
