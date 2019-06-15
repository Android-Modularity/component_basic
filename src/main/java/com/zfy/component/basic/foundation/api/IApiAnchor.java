package com.zfy.component.basic.foundation.api;

/**
 * CreateAt : 2019/1/28
 * Describe :
 *
 * @author chendong
 */
public interface IApiAnchor {

    IApiAnchor EMPTY = () -> -1;


    // 返回唯一标示
    int uniqueKey();
}
