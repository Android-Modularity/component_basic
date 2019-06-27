package com.zfy.component.basic.mvx.mvvm;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

/**
 * CreateAt : 2019/3/8
 * Describe :
 *
 * @author chendong
 */
public class ExLiveData<T> extends MutableLiveData<T> {

    private T defaultValue;

    /**
     * @param initValue 初始值
     */
    public ExLiveData(T initValue) {
        this.setValue(initValue);
    }

    /**
     * @param initValue    初始值
     * @param defaultValue 默认值，当 value 为空时返回该值
     */
    public ExLiveData(T initValue, @NonNull T defaultValue) {
        this.setValue(initValue);
        this.defaultValue = defaultValue;
    }

    public interface NonNullObserver<T> {
        void onChanged(T t);
    }

    public void observeNonNull(@NonNull LifecycleOwner owner, @NonNull NonNullObserver<T> observer) {
        super.observe(owner, data -> {
            if (data != null) {
                observer.onChanged(data);
            }
        });
    }

    /**
     * 相当于 getValue()
     *
     * @return ss
     */
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

    /**
     * 如果不想等，则设置
     * @param value value 2 set
     */
    public void setValueNoEqual(T value) {
        updateValue(value, false, true, false, false);
    }

    /**
     * 如果不想等，则设置
     * @param value value 2 set
     */
    public void postValueNoEqual(T value) {
        updateValue(value, true, true, false, false);
    }


    /**
     * 设置数据
     * @param value 数据
     * @param post 是否在主线程
     * @param equalNotSet 是否相等跳过更新
     * @param noObserverNotSet 是否没有监听时跳过更新
     * @param noActiveObserverNotSet 是否没有激活的监听时，跳过更新
     */
    public void updateValue(
            @NonNull T value, /* 数据 */
            boolean post, /* 是否发送到主线程 */
            boolean equalNotSet, /* 相等时不触发更新 */
            boolean noObserverNotSet, /* 没有监听者时不触发更新 */
            boolean noActiveObserverNotSet /* 没有活跃的监听者时，不触发更新*/
    ) {
        // 相等不设置
        if (equalNotSet && value.equals(getValue())) {
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
