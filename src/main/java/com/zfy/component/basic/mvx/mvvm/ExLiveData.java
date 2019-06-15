package com.zfy.component.basic.mvx.mvvm;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;

/**
 * CreateAt : 2019/3/8
 * Describe :
 *
 * @author chendong
 */
public class ExLiveData<T> extends MutableLiveData<T> {

    private @NonNull T defaultValue;

    public ExLiveData(@NonNull T defaultValue) {
        this.defaultValue = defaultValue;
        this.setValue(defaultValue);
    }

    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<T> observer) {
        super.observe(owner, observer);
    }

    @Override
    public void observeForever(@NonNull Observer<T> observer) {
        super.observeForever(observer);
    }

    @NonNull
    public T value() {
        return getValue();
    }

    @Override
    @NonNull
    public T getValue() {
        T value = super.getValue();
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    @Override
    public void setValue(T value) {
        super.setValue(value);
    }


    @Override
    public void postValue(T value) {
        super.postValue(value);
    }

    public void setValueNoEqual(T value) {
        updateValue(value, false, true, false, false);
    }

    public void postValueNoEqual(T value) {
        updateValue(value, true, true, false, false);
    }

    public void updateValue(
            T value, /* 数据 */
            boolean post, /* 是否发送到主线程 */
            boolean equalNotSet, /* 相等时不触发更新 */
            boolean noObserverNotSet, /* 没有监听者时不触发更新 */
            boolean noActiveObserverNotSet /* 没有活跃的监听者时，不触发更新*/
    ) {
        // 相等不设置
        if (equalNotSet && getValue().equals(value)) {
            return;
        }
        // 没有监听者不设置
        if (noObserverNotSet && !hasObservers()) {
            return;
        }
        // 没有激活的监听者不设置
        if (noActiveObserverNotSet && !hasActiveObservers()) {
            return;
        }

        if (post) {
            postValue(value);
        } else {
            setValue(value);
        }
    }

}
