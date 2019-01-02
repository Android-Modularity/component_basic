package com.zfy.component.basic.app.data;

import android.view.Gravity;
import android.view.ViewGroup;

/**
 * CreateAt : 2018/12/29
 * Describe :
 *
 * @author chendong
 */
public class DialogAttr {

    public static final int MATCH = ViewGroup.LayoutParams.MATCH_PARENT;
    public static final int WRAP  = ViewGroup.LayoutParams.WRAP_CONTENT;

    public int     width                  = MATCH; // 宽度
    public int     height                 = WRAP; // 高度
    public float   alpha                  = 1f; // 设置布局的透明度，0为透明，1为实际颜色,该透明度会使 layout 里的所有空间都有透明度，不仅仅是布局最底层的 view
    public float   dim                    = .6f; // 背景颜色深度
    public int     gravity                = Gravity.CENTER; // 位置
    public int     animStyle; // 动画效果
    public boolean b2cAnim                = false; // 从底部出现
    public boolean cancelable             = true;
    public boolean canceledOnTouchOutside = true;

    public DialogAttr() {
    }

    public DialogAttr(int width, int height, int gravity) {
        this.width = width;
        this.height = height;
        this.gravity = gravity;
    }
}
