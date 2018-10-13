package com.zfy.component.basic.arch;

import android.app.Application;

import com.march.common.Common;
import com.zfy.component.basic.ComponentX;

/**
 * CreateAt : 2018/9/11
 * Describe :
 *
 * @author chendong
 */
public abstract class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ComponentX.init(this, true);
        Common.init(this, getBuildConfigClazz());
    }

    public abstract Class getBuildConfigClazz();
}