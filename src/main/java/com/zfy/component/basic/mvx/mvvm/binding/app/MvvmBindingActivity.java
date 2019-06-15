package com.zfy.component.basic.mvx.mvvm.binding.app;

import android.databinding.ViewDataBinding;

import com.zfy.component.basic.app.AppActivity;
import com.zfy.component.basic.app.IDelegate;
import com.zfy.component.basic.mvx.mvvm.binding.MvvmBindingViewModel;
import com.zfy.component.basic.mvx.mvvm.binding.IMvvmBindingView;

/**
 * CreateAt : 2018/9/11
 * Describe : Mvvm Binding Activity
 *
 * @author chendong
 */
public abstract class MvvmBindingActivity<VM extends MvvmBindingViewModel, VDB extends ViewDataBinding>
        extends AppActivity
        implements IMvvmBindingView<VM, VDB> {

    protected MvvmBindingDelegate<VM, VDB> mDelegate = new MvvmBindingDelegate<>();

    @Override
    public VDB binding() {
        return mDelegate.binding();
    }

    @Override
    public VM viewModel() {
        return mDelegate.viewModel();
    }

    @Override
    public <E extends MvvmBindingViewModel> E provideViewModel(Class<E> clazz) {
        return mDelegate.provideViewModel(clazz);
    }

    @Override
    public IDelegate getViewDelegate() {
        return mDelegate;
    }

}
