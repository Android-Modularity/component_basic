package com.zfy.component.basic.foundation.api.interceptor;

import android.support.annotation.NonNull;

import com.march.common.x.EmptyX;
import com.march.common.x.NetX;
import com.zfy.component.basic.foundation.api.Api;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Request;
import okhttp3.Response;

/**
 * CreateAt : 2019/1/31
 * Describe : 为 request 添加缓存策略
 * 无网络时强制返回缓存
 * 有网络时，返回一分钟内的缓存
 *
 * @author chendong
 */
public class CacheRequestInterceptor extends AbstractInterceptor {

    public static final String HEADER_RETRY = "header-retry";

    @NonNull
    @Override
    protected Request proceedRequest(Chain chain, Request request) {
        if (!NetX.isNetworkAvailable()) {
            // 没有网络时，因为网络请求无法得到响应，强制使用缓存
            return request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        } else {
            // 有网络时，返回 10s 内的缓存，允许过期 10s，避免频繁请求
            return request.newBuilder()
                    .cacheControl(new CacheControl.Builder()
                            .maxAge(Api.config().getCacheMilliSeconds(), TimeUnit.MILLISECONDS)
                            .maxStale(Api.config().getCacheMilliSeconds(), TimeUnit.MILLISECONDS)
                            .build())
                    .build();
        }
    }

    // 失败后获取缓存
    @NonNull
    @Override
    protected Response proceedResponse(Chain chain, Response response) throws IOException {
        if (!response.isSuccessful()) {
            Request request = response.request();
            if (EmptyX.isEmpty(request.header(HEADER_RETRY))) {
                return chain.proceed(request.newBuilder()
                        .header(HEADER_RETRY, "true")
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build());
            }
            return super.proceedResponse(chain, response);
        }
        return super.proceedResponse(chain, response);

    }
}
