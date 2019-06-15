package com.zfy.component.basic.mvx.mvvm;

import android.app.Application;
import android.support.annotation.NonNull;

import com.zfy.component.basic.foundation.X;
import com.zfy.component.basic.mvx.model.AppRepository;
import com.zfy.component.basic.mvx.model.IRepository;
import com.zfy.component.basic.mvx.mvvm.app.MVVM$VM;

/**
 * CreateAt : 2018/9/11
 * Describe : ViewModel with Repository
 *
 * @author chendong
 */
public abstract class MvvmRepoViewModel<R extends IRepository> extends MvvmViewModel {

    protected R mRepo;

    public MvvmRepoViewModel(@NonNull Application application) {
        super(application);
        mRepo = newRepo();
    }

    private R makeRepo() {
        R repo = null;
        try {
            MVVM$VM annotation = getClass().getAnnotation(MVVM$VM.class);
            if (annotation != null) {
                Class<? extends AppRepository> repoClazz = annotation.repo();
                AppRepository appRepository = X.newInst(repoClazz);
                repo = (R) appRepository;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return repo;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mRepo.onDestroy();
    }

    protected R newRepo() {
        return makeRepo();
    }
}
