package com.zfy.component.basic.mvx.mvp.presenter;

import com.zfy.component.basic.app.view.IView;
import com.zfy.component.basic.mvx.model.IRepository;
import com.zfy.component.basic.mvx.mvp.IMvpView;

/**
 * CreateAt : 2018/10/11
 * Describe : Presenter Plugin Impl
 *
 * @author chendong
 */
public abstract class MvpPluginPresenter<R extends IRepository, V extends IMvpView>
        extends MvpPresenter<R, V> {

    public void attachViewOnPlugin(V view) {
        try {
            if (view instanceof IView) {
                ((IView) view).getViewDelegate().addObserver(this);
            }
            attachView(view);
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
