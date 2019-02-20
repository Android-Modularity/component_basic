package com.zfy.component.basic.foundation.api.interceptor;

import android.support.annotation.NonNull;

import com.march.common.x.EmptyX;
import com.zfy.component.basic.foundation.api.Api;
import com.zfy.component.basic.foundation.api.mock.RespProvider;
import com.zfy.component.basic.foundation.api.mock.Rule;

import java.io.IOException;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * CreateAt : 2019/1/31
 * Describe :
 *
 * @author chendong
 */
public class MockInterceptor extends AbstractInterceptor {
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        Map<Rule, RespProvider> map = Api.mock().getMockRespMap();
        for (Rule rule : map.keySet()) {
            if (rule.match(request)) {
                RespProvider provider = map.get(rule);
                if (provider != null) {
                    String content = provider.provide(request);
                    if (!EmptyX.isEmpty(content)) {
                        return new Response.Builder()
                                .code(200)
                                .message("")
                                .request(chain.request())
                                .protocol(Protocol.HTTP_1_0)
                                .body(ResponseBody.create(MediaType.parse("application/json"), content))
                                .addHeader("content-type", "application/json")
                                .build();
                    }
                }
            }
        }
        return super.intercept(chain);

    }
}
