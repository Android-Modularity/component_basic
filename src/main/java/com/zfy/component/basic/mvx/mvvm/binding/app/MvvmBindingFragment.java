package com.zfy.component.basic.mvx.mvvm.binding.app;

import android.databinding.ViewDataBinding;

import com.zfy.component.basic.app.AppFragment;
import com.zfy.component.basic.app.IDelegate;
import com.zfy.component.basic.mvx.mvvm.binding.MvvmBindingViewModel;
import com.zfy.component.basic.mvx.mvvm.binding.IMvvmBindingView;

/**
 * CreateAt : 2018/9/11
 * Describe :
 *
 * @author chendong
 */
public abstract class MvvmBindingFragment<VM extends MvvmBindingViewModel, VDB extends ViewDataBinding>
        extends AppFragment
        implements IMvvmBindingView<VM, VDB> {

    private MvvmBindingDelegate<VM, VDB> mDelegate = new MvvmBindingDelegate<>();

    @Override
    public VM viewModel() {
        return mDelegate.viewModel();
    }

    @Override
    public VDB binding() {
        return mDelegate.binding();
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
