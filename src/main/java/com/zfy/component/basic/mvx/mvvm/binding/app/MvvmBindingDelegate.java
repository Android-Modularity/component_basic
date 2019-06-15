package com.zfy.component.basic.mvx.mvvm.binding.app;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.march.common.x.LogX;
import com.zfy.component.basic.app.AppDelegate;
import com.zfy.component.basic.app.view.ViewOpts;
import com.zfy.component.basic.mvx.mvvm.binding.MvvmBindingViewModel;
import com.zfy.component.basic.mvx.mvvm.binding.IMvvmBindingView;

/**
 * CreateAt : 2018/9/12
 * Describe :
 *
 * @author chendong
 */
public class MvvmBindingDelegate<VideoModel extends MvvmBindingViewModel, VDB extends ViewDataBinding> extends AppDelegate implements IMvvmBindingView<VideoModel, VDB> {

    public static final String TAG = MvvmBindingDelegate.class.getSimpleName();

    private VideoModel mViewModel;
    private VDB        mBinding;

    @Override
    public void onAttachHost(Object host) {
        if (mViewOpts == null) {
            VM annotation = mHost.getClass().getAnnotation(VM.class);
            if (annotation != null) {
                int layout = annotation.layout();
                Class vm = annotation.vm();
                int vmId = annotation.vmId();
                if (layout != 0) {
                    mViewOpts = ViewOpts.makeMvvm(layout, vmId, vm);
                }
            }
        }
    }

    @Override
    public View onBindFragment(Fragment owner, LayoutInflater inflater, ViewGroup container) {
        mBinding = DataBindingUtil.inflate(inflater, mViewOpts.getLayout(), container, false);
        bindView(mHost, mBinding.getRoot());
        bindEvent();
        init();
        return mBinding.getRoot();
    }

    @Override
    public void onBindActivity(Activity owner) {
        mBinding = DataBindingUtil.setContentView(((Activity) owner), mViewOpts.getLayout());
        bindView(mHost, null);
        init();
    }

    private void init() {
        // binding 绑定到生命周期
        mBinding.setLifecycleOwner(mLifecycleOwner);
        // 生成 view model
        if (mViewOpts.getVmClazz() != null) {
            mViewModel = makeViewModel(mViewOpts.getVmClazz());
        } else {
            LogX.e(TAG, "viewModel class is null (" + mViewOpts.getVmId() + ")");
        }
        // 绑定到 binding
        if (mViewModel != null && mViewOpts.getVmId() != 0) {
            mBinding.setVariable(mViewOpts.getVmId(), mViewModel);
        } else {
            LogX.e(TAG, "viewModel is null or vmId is 0 (" + mViewOpts.getVmId() + "," + mViewOpts.getVmClazz() + ")");
        }
    }

    // 创建 ViewModel 并绑定到生命周期
    @SuppressWarnings("unchecked")
    private VideoModel makeViewModel(Class clazz) {
        if (mHost instanceof FragmentActivity) {
            FragmentActivity activity = (FragmentActivity) mHost;
            VideoModel viewModel = (VideoModel) ViewModelProviders.of(activity).get(clazz);
            addObserver(viewModel);
            viewModel.init();
            return viewModel;
        }
        return null;
    }


    @Override
    public VideoModel viewModel() {
        return mViewModel;
    }

    @Override
    public VDB binding() {
        return mBinding;
    }

    @Override
    public <E extends MvvmBindingViewModel> E provideViewModel(Class<E> clazz) {
        if (mHost instanceof FragmentActivity) {
            return ViewModelProviders.of((FragmentActivity) mHost).get(clazz);
        } else if (mHost instanceof Fragment) {
            Fragment fragment = (Fragment) mHost;
            if (fragment.getActivity() != null) {
                return ViewModelProviders.of(fragment.getActivity()).get(clazz);
            }
        }
        return null;
    }

    @Override
    public Handler getHandler() {
        return super.getHandler();
    }
}
