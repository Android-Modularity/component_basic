package com.zfy.component.basic.mvx.mvvm.app;

import com.zfy.component.basic.app.AppDialogFragment;
import com.zfy.component.basic.mvx.mvvm.IMvvmView;

/**
 * CreateAt : 2018/9/11
 * Describe :
 *
 * @author chendong
 */
public abstract class MvvmDialogFragment
        extends AppDialogFragment
        implements IMvvmView {

    private MvvmDelegate mDelegate = new MvvmDelegate();

    @Override
    public MvvmDelegate getViewDelegate() {
        return mDelegate;
    }
}
