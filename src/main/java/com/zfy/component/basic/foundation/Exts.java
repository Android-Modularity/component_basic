package com.zfy.component.basic.foundation;

import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.view.WindowManager;

import com.zfy.component.basic.R;
import com.zfy.component.basic.app.AppActivity;
import com.zfy.component.basic.app.data.DialogAttr;

import org.greenrobot.eventbus.EventBus;

/**
 * CreateAt : 2018/10/12
 * Describe :
 *
 * @author chendong
 */
public class Exts {

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


    public static void finishPage(FragmentActivity activity, Intent intent, int code) {
        if (activity != null) {
            if (activity instanceof AppActivity) {
                ((AppActivity) activity).finishPage(intent, code);
            } else {
                if (intent != null) {
                    activity.setResult(code, intent);
                }
                activity.finish();
            }
        }
    }
}
