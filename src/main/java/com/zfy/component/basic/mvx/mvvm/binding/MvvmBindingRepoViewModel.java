package com.zfy.component.basic.mvx.mvvm.binding;

import android.app.Application;
import android.support.annotation.NonNull;

import com.zfy.component.basic.mvx.model.IRepository;

/**
 * CreateAt : 2018/9/11
 * Describe : ViewModel with Repository
 *
 * @author chendong
 */
public abstract class MvvmBindingRepoViewModel<R extends IRepository> extends MvvmBindingViewModel {

    protected R mRepository;

    public MvvmBindingRepoViewModel(@NonNull Application application) {
        super(application);
        mRepository = getRepository();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mRepository.onDestroy();
    }

    protected abstract R getRepository();

}
