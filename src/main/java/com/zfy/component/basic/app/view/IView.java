package com.zfy.component.basic.app.view;

import com.zfy.component.basic.app.IDelegate;

/**
 * CreateAt : 2018/10/11
 * Describe :
 *
 * @author chendong
 */
public interface IView {

    ViewOpts getViewOpts();

    IDelegate getViewDelegate();
}
