package com.zfy.component.basic.mvx.mvvm.app;

import com.zfy.component.basic.app.AppFunctionView;
import com.zfy.component.basic.mvx.mvvm.IMvvmView;
import com.zfy.component.basic.mvx.mvvm.MvvmViewModel;

/**
 * CreateAt : 2018/9/11
 * Describe :
 *
 * @author chendong
 */
public abstract class MvvmFunctionView<HOST, VM extends MvvmViewModel>
        extends AppFunctionView<HOST>
        implements IMvvmView<VM> {

    private MvvmDelegate<VM> mDelegate = new MvvmDelegate<>();

    public MvvmFunctionView(HOST view) {
        super(view);
    }

    @Override
    public VM viewModel() {
        return mDelegate.viewModel();
    }

    @Override
    public MvvmDelegate<VM> getViewDelegate() {
        return mDelegate;
    }
}
