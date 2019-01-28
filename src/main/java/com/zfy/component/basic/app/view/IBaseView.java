package com.zfy.component.basic.app.view;

import android.view.View;

/**
 * CreateAt : 2018/10/11
 * Describe :
 *
 * @author chendong
 */
public interface IBaseView {
    <T extends View> T findViewById(int id);
}
