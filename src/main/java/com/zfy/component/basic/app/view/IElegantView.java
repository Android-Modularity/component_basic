package com.zfy.component.basic.app.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;

/**
 * CreateAt : 2017/12/7
 * Describe : View 层统一的实现的方法
 *
 * @author chendong
 */
public interface IElegantView {

    Context getContext();

    Activity getActivity();

    void startPage(Intent data, int code);

    @NonNull
    Bundle getData();

    void finishPage(Intent intent, int code);

    default void clickView(int[] ids, View.OnClickListener listener) {
        if (this instanceof IViewConfig) {
            ((IViewConfig) this).getViewDelegate().clickView(listener, ids);
        }
    }
    default Handler post(Runnable runnable, int delay) {
        if (this instanceof IViewConfig) {
            return ((IViewConfig) this).getViewDelegate().post(runnable, delay);
        }
        return null;
    }
}
