package com.zfy.component.basic.mvx.mvp;

/**
 * CreateAt : 2018/10/11
 * Describe : Mvp view
 *
 * @author chendong
 */
public interface IExtendsMvpView<P extends IMvpPresenter> extends IMvpView {

    P getPresenter();
}
