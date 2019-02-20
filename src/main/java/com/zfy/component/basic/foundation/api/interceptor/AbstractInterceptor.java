package com.zfy.component.basic.foundation.api.interceptor;

import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * CreateAt : 2018/1/12
 * Describe : base Interceptor
 *
 * @author chendong
 */
public abstract class AbstractInterceptor implements Interceptor {

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        return proceedResponse(chain, chain.proceed(proceedRequest(chain, chain.request())));
    }


    protected @NonNull
    Request proceedRequest(Chain chain, Request request) throws IOException {
        return request;
    }

    protected @NonNull
    Response proceedResponse(Chain chain, Response response) throws IOException {
        return response;
    }
}
