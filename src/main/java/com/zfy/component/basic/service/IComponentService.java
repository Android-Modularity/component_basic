package com.zfy.component.basic.service;

import android.content.Context;

/**
 * CreateAt : 2019/1/25
 * Describe :
 *
 * @author chendong
 */
public interface IComponentService extends IService {

    /**
     * 组件优先级别
     *
     * @return 优先级别
     */
    int priority();


    /**
     * 按照 priority 进行初始化
     *
     * @param context
     */
    void initOrderly(Context context);

}
