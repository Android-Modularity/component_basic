package com.zfy.component.basic.foundation.api.interceptor;

import android.support.annotation.NonNull;

import okhttp3.Response;

/**
 * CreateAt : 2019/1/31
 * Describe : Network 为 resp 增加缓存字段，使 Cache 可以识别并缓存数据
 *
 * @author chendong
 */
public class CacheResponseInterceptor extends AbstractInterceptor {

    public static final String CACHE_CONTROL = "Cache-Control";

    @NonNull
    @Override
    protected Response proceedResponse(Chain chain, Response response) {
        // 所有 get 请求缓存 5 分钟
        int maxAge = 60 * 5;
        return response.newBuilder()
                .removeHeader("Pragma")
                .removeHeader(CACHE_CONTROL)
                .header(CACHE_CONTROL, "public, max-age=" + maxAge)
                .build();
    }
}
