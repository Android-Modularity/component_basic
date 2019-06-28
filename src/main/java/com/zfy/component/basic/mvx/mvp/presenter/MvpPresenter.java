package com.zfy.component.basic.mvx.mvp.presenter;

import com.march.common.x.LogX;
import com.zfy.component.basic.ComponentX;
import com.zfy.component.basic.app.view.IView;
import com.zfy.component.basic.foundation.X;
import com.zfy.component.basic.mvx.model.IRepository;
import com.zfy.component.basic.mvx.mvp.IMvpPresenter;
import com.zfy.component.basic.mvx.mvp.IMvpView;
import com.zfy.component.basic.mvx.mvvm.IMvvmView;

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
        mRepo = newRepo();
        X.registerEvent(this);
    }

    public void attachView(V view) {
        mView = view;
    }

    public void onViewAttach(V view) {
        mView = view;
        if (view instanceof IView) {
            ((IView) view).getViewDelegate().addObserver(this);
        }
        // 注入 repo
        ComponentX.inject(this);
        init();
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

    protected R newRepo() {
        return makeRepo();
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
