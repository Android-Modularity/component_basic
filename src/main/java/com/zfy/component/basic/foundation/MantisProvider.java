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
import com.zfy.component.basic.mvx.mvp.IMvpPresenter;
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
            String key = opts.key;
            Object target = opts.target;
            // 生成 repo
            if (Const.REPO.equals(key) && AppRepository.class.isAssignableFrom(clazz)) {
                return X.newInst(clazz);
            }
            // 生成 mvp presenter
            if (IMvpPresenter.class.isAssignableFrom(clazz)) {
                return provideMvpPresenter(target, opts.clazz, opts.fieldClazz);
            }
            // 生成 mvp view
            if (IMvpView.class.isAssignableFrom(clazz)) {
                return provideMvpView(target, clazz);
            }
            // 从当前类获取 ViewModel
            if (Const.VM.equals(key) && MvvmViewModel.class.isAssignableFrom(clazz)) {
                return provideViewModel(target, clazz);
            }
            // 获取宿主 Activity 的 ViewModel
            if (Const.VM_ACT.equals(key) && MvvmViewModel.class.isAssignableFrom(clazz)) {
                if (target instanceof Fragment) {
                    FragmentActivity activity = ((Fragment) target).getActivity();
                    return provideViewModel(activity, clazz);
                } else {
                    throw new IllegalStateException("请不要在非 Fragment 中使用 ACTIVITY_VM");
                }
            }
            // 生成 ARouter Service
            if (IProvider.class.isAssignableFrom(clazz)) {
                return provideARouterService(target, key, clazz);
            }
            return null;
        }

        // 创建 MvpView
        private IMvpView provideMvpView(Object host, Class<?> clazz) {
            if (!(host instanceof MvpPresenter)) {
                throw new IllegalStateException("Only MvpPresenter Can Inject View !!!");
            }
            MvpPresenter presenter = (MvpPresenter) host;
            Object o = presenter.getView();
            if (!clazz.isInstance(o)) {
                throw new IllegalStateException("The View Must Be Impl Interface !!!");
            }
            return presenter.getView();
        }

        // 创建 MvpPresenter
        private MvpPresenter provideMvpPresenter(Object host, Class<?> clazz, Class<?> fieldClazz) {
            if (clazz == null || fieldClazz == null) {
                throw new IllegalStateException("Class Must Not Be Null !!!");
            }
            if (!(host instanceof IMvpView)) {
                throw new IllegalStateException("Only MvpView Can Inject Presenter !!!");
            }
            if (!fieldClazz.isInterface()) {
                // 属性声明，需要是接口
                throw new IllegalStateException("Field Type Must Be Contract Interface !!!");
            }
            if (!fieldClazz.isAssignableFrom(clazz)) {
                // 实现类需要实现该接口
                throw new IllegalStateException("Field Type Must Be Impl Contract Interface !!!");
            }
            IMvpView mvpView = (IMvpView) host;
            MvpPresenter o = (MvpPresenter) X.newInst(clazz);
            if (o != null) {
                o.onViewAttach(mvpView);
            }
            return o;
        }

        // 创建 ViewModel
        private MvvmViewModel provideViewModel(Object host, Class clazz) {
            MvvmViewModel viewModel = null;
            if (host instanceof MvvmActivity) {
                viewModel = MvvmHook.use((MvvmActivity) host, clazz);
            } else if (host instanceof MvvmFragment) {
                viewModel = MvvmHook.use((MvvmFragment) host, clazz);
            } else if (host instanceof MvvmDialogFragment) {
                viewModel = MvvmHook.use((MvvmDialogFragment) host, clazz);
            } else if (host instanceof MvvmFunctionView) {
                viewModel = provideViewModel(((MvvmFunctionView) host).getHostView(), clazz);
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
