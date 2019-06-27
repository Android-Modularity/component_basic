package com.zfy.component.basic.mvx.mvvm.app;

import com.zfy.component.basic.app.AppActivity;
import com.zfy.component.basic.mvx.mvvm.IMvvmView;

/**
 * CreateAt : 2018/9/11
 * Describe : Mvvm Binding Activity
 *
 * @author chendong
 */
public abstract class MvvmActivity extends AppActivity implements IMvvmView {

    protected MvvmDelegate mDelegate = new MvvmDelegate();

    @Override
    public MvvmDelegate getViewDelegate() {
        return mDelegate;
    }

}
