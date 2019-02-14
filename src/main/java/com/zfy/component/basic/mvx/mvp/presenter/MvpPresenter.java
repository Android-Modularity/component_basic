package com.zfy.component.basic.mvx.mvp.presenter;

import com.march.common.x.LogX;
import com.zfy.component.basic.foundation.X;
import com.zfy.component.basic.mvx.model.IRepository;
import com.zfy.component.basic.mvx.mvp.IMvpPresenter;
import com.zfy.component.basic.mvx.mvp.IMvpView;

/**
 * CreateAt : 2018/10/11
 * Describe : Presenter Impl
 *
 * @author chendong
 */
public abstract class MvpPresenter<R extends IRepository, V extends IMvpView> implements IMvpPresenter {

    protected V mView;
    protected R mRepo;

    public MvpPresenter() {
        mRepo = makeRepo();
        X.registerEvent(this);
    }

    public void attachView(V view) {
        mView = view;
    }

    @Override
    public void onViewInit() {

    }

    @SuppressWarnings("unchecked")
    private R makeRepo() {
        R repo = null;
        try {
            MvpP annotation = getClass().getAnnotation(MvpP.class);
            if (annotation != null) {
                Class<R> repoClazz = annotation.repo();
                repo = X.newInst(repoClazz);
            }
        } catch (Exception e) {
            LogX.e("no repo presenter");
            e.printStackTrace();
        }
        return repo;
    }

    @Override
    public int uniqueKey() {
        return mView.uniqueKey();
    }

    @Override
    public void onDestroy() {
        X.unRegisterEvent(this);
    }

    public V getView() {
        return mView;
    }
}
