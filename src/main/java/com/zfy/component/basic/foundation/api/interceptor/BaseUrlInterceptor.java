package com.zfy.component.basic.foundation.api.interceptor;

import com.march.common.x.EmptyX;
import com.zfy.component.basic.foundation.api.Api;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Request;

/**
 * CreateAt : 2018/1/12
 * Describe : 动态 baseUrl
 *
 * @author chendong
 */
public class BaseUrlInterceptor extends AbstractInterceptor {

    @Override
    protected Request proceedRequest(Chain chain, Request request) throws IOException {
        String header = request.header(Api.DOMAIN_KEY);
        if (EmptyX.isEmpty(header)) {
            return super.proceedRequest(chain, request);
        }
        String baseUrl = Api.config().getBaseUrlMap().get(header);
        if (EmptyX.isEmpty(baseUrl)) {
            return super.proceedRequest(chain, request);
        }
        HttpUrl mapUrl = HttpUrl.parse(baseUrl);
        if (mapUrl == null) {
            return super.proceedRequest(chain, request);
        }
        HttpUrl httpUrl = request.url().newBuilder()
                .scheme(mapUrl.scheme())
                .host(mapUrl.host())
                .port(mapUrl.port())
                .build();
        return request.newBuilder().url(httpUrl).build();
    }
}
