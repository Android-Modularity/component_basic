package com.zfy.component.basic.mvx.mvvm;

import android.arch.lifecycle.LifecycleOwner;

/**
 * CreateAt : 2019/6/17
 * Describe :
 *
 * @author chendong
 */
public class LiveEvent extends ExLiveData<String> {

    public LiveEvent(String initValue) {
        super(initValue);
    }

    public void subscribe(LifecycleOwner owner, NonNullObserver<String> observer) {
        observeNonNull(owner, observer);
    }
}
