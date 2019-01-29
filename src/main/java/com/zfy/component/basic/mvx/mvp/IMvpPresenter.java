package com.zfy.component.basic.mvx.mvp;

import android.arch.lifecycle.LifecycleObserver;

import com.march.common.able.Destroyable;
import com.zfy.component.basic.foundation.api.IApiAnchor;

/**
 * CreateAt : 2018/10/11
 * Describe : mvp presenter
 *
 * @author chendong
 */
public interface IMvpPresenter extends Destroyable, LifecycleObserver, IApiAnchor {

    /**
     * 将会在 UI 和 事件 绑定完成后触发
     */
    void init();

    /**
     * 当 View 曾初始化完成
     */
    void onViewInit();
}
