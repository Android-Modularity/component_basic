package com.zfy.component.basic.app.view;

/**
 * CreateAt : 2018/11/28
 * Describe :
 *
 * @author chendong
 */
public interface IInitFlow {

    /**
     * View 没有创建之前
     *
     * @return 结束 View 装载
     */
     boolean preViewAttach();
    /**
     * 数据初始化之前
     */
    void preInit();

    /**
     * 数据初始化
     */
    void init();
}
