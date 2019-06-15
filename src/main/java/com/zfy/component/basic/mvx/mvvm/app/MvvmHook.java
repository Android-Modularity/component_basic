package com.zfy.component.basic.mvx.mvvm.app;

import android.arch.lifecycle.ViewModelProviders;

import com.zfy.component.basic.mvx.mvvm.MvvmViewModel;

/**
 * CreateAt : 2019/6/15
 * Describe : 辅助生成 ViewModel
 *
 * @author chendong
 */
public class MvvmHook {

    public static <VM extends MvvmViewModel> VM use(MvvmActivity<VM> activity) {
        MvvmDelegate<VM> delegate = activity.getViewDelegate();
        return delegate.viewModel();
    }

    public static <VM extends MvvmViewModel> VM use(MvvmFragment<VM> fragment) {
        MvvmDelegate<VM> delegate = fragment.getViewDelegate();
        return delegate.viewModel();
    }

    public static <VM extends MvvmViewModel> VM use(MvvmDialogFragment<VM> fragment) {
        MvvmDelegate<VM> delegate = fragment.getViewDelegate();
        return delegate.viewModel();
    }

    public static <VM extends MvvmViewModel> VM use(MvvmActivity activity, Class<VM> clazz) {
        VM vm = ViewModelProviders.of(activity).get(clazz);
        vm.onMvvmViewAttach(activity);
        return vm;
    }

    public static <VM extends MvvmViewModel> VM use(MvvmFragment fragment, Class<VM> clazz) {
        VM vm = ViewModelProviders.of(fragment).get(clazz);
        vm.onMvvmViewAttach(fragment);
        return vm;
    }

    public static <VM extends MvvmViewModel> VM use(MvvmDialogFragment fragment, Class<VM> clazz) {
        VM vm = ViewModelProviders.of(fragment).get(clazz);
        vm.onMvvmViewAttach(fragment);
        return vm;
    }

    static <VM extends MvvmViewModel> VM useViewModel(Object host, Class<VM> clazz) {
        VM viewModel = null;
        if (host instanceof MvvmActivity) {
            viewModel = MvvmHook.use((MvvmActivity) host, clazz);
            return viewModel;
        } else if (host instanceof MvvmFragment) {
            viewModel = MvvmHook.use((MvvmFragment) host, clazz);
        } else if (host instanceof MvvmDialogFragment) {
            viewModel = MvvmHook.use((MvvmDialogFragment) host, clazz);
        } else if (host instanceof MvvmFunctionView) {
            viewModel = useViewModel(((MvvmFunctionView) host).getHostView(), clazz);
        }
        return viewModel;
    }

}
