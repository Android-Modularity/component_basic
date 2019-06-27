package com.zfy.component.basic.foundation;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.alibaba.android.arouter.facade.template.IProvider;
import com.march.common.x.EmptyX;
import com.zfy.component.basic.Const;
import com.zfy.component.basic.app.AppDialog;
import com.zfy.component.basic.app.view.IElegantView;
import com.zfy.component.basic.mvx.model.AppRepository;
import com.zfy.component.basic.mvx.mvp.IMvpView;
import com.zfy.component.basic.mvx.mvp.presenter.MvpPresenter;
import com.zfy.component.basic.mvx.mvvm.MvvmViewModel;
import com.zfy.component.basic.mvx.mvvm.app.MvvmActivity;
import com.zfy.component.basic.mvx.mvvm.app.MvvmDialogFragment;
import com.zfy.component.basic.mvx.mvvm.app.MvvmFragment;
import com.zfy.component.basic.mvx.mvvm.app.MvvmFunctionView;
import com.zfy.component.basic.mvx.mvvm.app.MvvmHook;
import com.zfy.component.basic.route.Router;
import com.zfy.mantis.annotation.LookupOpts;
import com.zfy.mantis.api.provider.BundleProvider;
import com.zfy.mantis.api.provider.IDataProvider;
import com.zfy.mantis.api.provider.IDataProviderFactory;
import com.zfy.mantis.api.provider.IObjProvider;

/**
 * CreateAt : 2019/1/30
 * Describe :
 *
 * @author chendong
 */
public class MantisProvider {

    public static class IDataProviderImpl implements IDataProviderFactory {

        BundleProvider provider = new BundleProvider();

        @Override
        public IDataProvider create(Object target) {
            if (target instanceof IElegantView) {
                return provider.reset(((IElegantView) target).getData());
            }
            if (target instanceof MvpPresenter) {
                IMvpView view = ((MvpPresenter) target).getView();
                return provider.reset(view.getData());
            }
            if (target instanceof AppDialog) {
                return provider.reset(((AppDialog) target).getArguments());
            }
            if (target instanceof MvvmViewModel) {
                return provider.reset(((MvvmViewModel) target).getData());
            }
            provider.reset(target);
            return provider;
        }
    }

    public static class IObjProviderImpl implements IObjProvider {

        @Override
        public Object getObject(LookupOpts opts) {
            // 获取 class
            Class<?> clazz = opts.clazz != null ? opts.clazz : opts.fieldClazz;
            int group = opts.group;
            Object target = opts.target;
            // 生成 repo
            if (group == Const.REPO && AppRepository.class.isAssignableFrom(clazz)) {
                return X.newInst(clazz);
            }
            // class + target 指定在哪里生成什么样子的 viewModel
            if (group == Const.VM && MvvmViewModel.class.isAssignableFrom(clazz)) {
                return useViewModel(target, clazz);
            }
            // class + target 对应的 Activity 指定在哪里生成什么样子的 viewModel
            if (group == Const.ACTIVITY_VM && MvvmViewModel.class.isAssignableFrom(clazz)) {
                if (target instanceof Fragment) {
                    FragmentActivity activity = ((Fragment) target).getActivity();
                    return useViewModel(activity, clazz);
                } else {
                    throw new IllegalStateException("请不要在非 Fragment 中使用 ACTIVITY_VM");
                }
            }
            // 生成 ARouter Service
            if (IProvider.class.isAssignableFrom(clazz)) {
                return provideARouterService(target, opts.key, clazz);
            }
            return null;
        }


        private MvvmViewModel useViewModel(Object host, Class clazz) {
            MvvmViewModel viewModel = null;
            if (host instanceof MvvmActivity) {
                viewModel = MvvmHook.use((MvvmActivity) host, clazz);
            } else if (host instanceof MvvmFragment) {
                viewModel = MvvmHook.use((MvvmFragment) host, clazz);
            } else if (host instanceof MvvmDialogFragment) {
                viewModel = MvvmHook.use((MvvmDialogFragment) host, clazz);
            } else if (host instanceof MvvmFunctionView) {
                viewModel = useViewModel(((MvvmFunctionView) host).getHostView(), clazz);
            }
            return viewModel;
        }

        // 帮助发现服务
        private IProvider provideARouterService(Object target, String key, Class<?> clazz) {
            IProvider provider;
            if (EmptyX.isEmpty(key)) {
                provider = (IProvider) Router.service(clazz);
            } else {
                provider = Router.service(key);
            }
            if (provider != null) {
                if (target instanceof Context) {
                    provider.init((Context) target);
                } else if (target instanceof MvpPresenter) {
                    provider.init(((MvpPresenter) target).getView().getContext());
                }
            }
            return provider;
        }
    }


}
