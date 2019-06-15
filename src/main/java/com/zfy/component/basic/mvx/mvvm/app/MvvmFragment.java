package com.zfy.component.basic.mvx.mvvm.app;

import com.zfy.component.basic.app.AppFragment;
import com.zfy.component.basic.app.IDelegate;
import com.zfy.component.basic.mvx.mvvm.IMvvmView;
import com.zfy.component.basic.mvx.mvvm.MvvmViewModel;

/**
 * CreateAt : 2018/9/11
 * Describe :
 *
 * @author chendong
 */
public abstract class MvvmFragment<VM extends MvvmViewModel>
        extends AppFragment
        implements IMvvmView<VM> {

    private MvvmDelegate<VM> mDelegate = new MvvmDelegate<>();

    @Override
    public VM viewModel() {
        return mDelegate.viewModel();
    }

    @Override
    public MvvmDelegate<VM> getViewDelegate() {
        return mDelegate;
    }
}
