package com.zfy.component.basic.arch.mvp;

import com.march.common.able.Destroyable;

/**
 * CreateAt : 2018/10/11
 * Describe : mvp presenter
 *
 * @author chendong
 */
public interface IMvpPresenter<V extends IMvpView> extends Destroyable {

    void setView(V view);

    void init();
}