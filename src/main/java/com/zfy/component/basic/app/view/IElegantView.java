package com.zfy.component.basic.app.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

    @NonNull
    Bundle getData();
}
