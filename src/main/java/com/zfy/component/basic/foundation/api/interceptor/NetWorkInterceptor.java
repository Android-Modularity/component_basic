package com.zfy.component.basic.foundation.api.interceptor;


import com.march.common.x.NetX;
import com.zfy.component.basic.foundation.api.exception.ApiException;

import java.io.IOException;

import okhttp3.Request;

/**
 * CreateAt : 2017/7/1
 * Describe : 提前检测网络
 *
 * @author chendong
 */
public class NetWorkInterceptor extends AbstractInterceptor {

    @Override
    protected Request proceedRequest(Chain chain, Request request) throws IOException {
        if (!NetX.isNetworkConnected()) {
            throw new ApiException(ApiException.CODE_NETWORK_ERROR);
        }
        return super.proceedRequest(chain, request);
    }
}