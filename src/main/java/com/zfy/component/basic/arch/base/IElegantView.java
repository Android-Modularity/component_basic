package com.zfy.component.basic.arch.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

/**
 * CreateAt : 2017/12/7
 * Describe : View 层统一的实现的方法
 *
 * @author chendong
 */
public interface IElegantView {

    Context getContext();

    Activity getActivity();

    void startActivity(Class clz);

    Bundle getData();
}