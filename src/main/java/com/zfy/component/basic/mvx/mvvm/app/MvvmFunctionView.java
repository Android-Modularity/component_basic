package com.zfy.component.basic.mvx.mvvm.app;

import com.zfy.component.basic.app.AppFunctionView;
import com.zfy.component.basic.mvx.mvvm.IMvvmView;

/**
 * CreateAt : 2018/9/11
 * Describe :
 *
 * @author chendong
 */
public abstract class MvvmFunctionView<HOST> extends AppFunctionView<HOST>
        implements IMvvmView {

    private MvvmDelegate mDelegate;

    public MvvmFunctionView(HOST view) {
        super(view);
    }

    @Override
    public MvvmDelegate getViewDelegate() {
        if (mDelegate == null) {
            mDelegate = new MvvmDelegate();
        }
        return mDelegate;
    }
}
