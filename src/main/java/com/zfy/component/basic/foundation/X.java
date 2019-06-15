package com.zfy.component.basic.foundation;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.Window;
import android.view.WindowManager;

import com.march.common.x.ActivityAnimX;
import com.zfy.component.basic.R;
import com.zfy.component.basic.app.IDelegate;
import com.zfy.component.basic.app.data.DialogAttr;
import com.zfy.component.basic.app.view.IView;

import org.greenrobot.eventbus.EventBus;

/**
 * CreateAt : 2018/10/12
 * Describe :
 *
 * @author chendong
 */
public class X {

    /**
     * 注册 EventBus
     *
     * @param host 注册者
     */
    public static void registerEvent(Object host) {
        if (host == null) {
            return;
        }
        if (!EventBus.getDefault().isRegistered(host)) {
            try {
                EventBus.getDefault().register(host);
            } catch (Exception e) {
                // e.printStackTrace();
            }
        }
    }

    /**
     * 取消注册 EventBus
     *
     * @param host 注册着
     */
    public static void unRegisterEvent(Object host) {
        if (host == null) {
            return;
        }
        if (EventBus.getDefault().isRegistered(host)) {
            try {
                EventBus.getDefault().unregister(host);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 创建实例
     *
     * @param clazz class
     * @param <T>   范型
     * @return 实例
     */
    public static <T> T newInst(Class<T> clazz) {
        T t;
        try {
            t = clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("实例创建失败", e);
        }
        return t;
    }


    /**
     * 设置 Dialog Attrs
     * @param dialog dialog
     * @param attr attr
     */
    public static void setDialogAttributes(Dialog dialog, DialogAttr attr) {
        dialog.setCancelable(attr.cancelable);
        dialog.setCanceledOnTouchOutside(attr.canceledOnTouchOutside);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        WindowManager.LayoutParams params = window.getAttributes();
        params.alpha = attr.alpha;
        // 窗口的背景，0为透明，1为全黑
        params.dimAmount = attr.dim;
        params.width = attr.width;
        params.height = attr.height;
        params.gravity = attr.gravity;
        window.setAttributes(params);
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        if (attr.animStyle > 0) {
            window.setWindowAnimations(attr.animStyle);
        } else if (attr.b2cAnim) {
            window.setWindowAnimations(R.style.dialog_anim_bottom_center);
        }
    }

    public static void finish(Activity activity) {
        if (activity != null) {
            activity.finish();
            ActivityAnimX.translateFinish(activity);
        }
    }

    public static void start(Activity activity, Intent intent) {
        if (activity != null) {
            activity.startActivity(intent);
            ActivityAnimX.translateStart(activity);
        }
    }

    public static void post(IView view, Runnable runnable, long delay) {
        IDelegate viewDelegate = view.getViewDelegate();
        if (viewDelegate != null) {
            viewDelegate.post(runnable, delay);
        }
    }

}
