package com.zfy.component.basic.foundation.api.config;

import com.march.common.funcs.Consumer;
import com.march.common.funcs.Function;
import com.march.common.x.EmptyX;
import com.zfy.component.basic.foundation.api.IApiAnchor;
import com.zfy.component.basic.foundation.api.observer.ApiObserver;

import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * CreateAt : 2018/1/12
 * Describe :
 * - 多 baseUrl 支持
 * - common Header 支持
 *
 * @author chendong
 */
public class ApiOptions {

    private String              baseUrl;
    private String              host;
    private Map<String, String> baseUrlMap;
    private Map<String, String> headers;

    private boolean saveCookie;
    private boolean debug;
    private boolean useCache;

    private Consumer<OkHttpClient.Builder>    okHttpRewriter;
    private Consumer<Retrofit.Builder>        retrofitRewriter;
    private Function<IApiAnchor, ApiObserver> observerFactory;

    private ApiOptions() {
    }

    public String getHost() {
        return host;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public Map<String, String> getBaseUrlMap() {
        return baseUrlMap;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Consumer<OkHttpClient.Builder> getOkHttpRewriter() {
        return okHttpRewriter;
    }

    public Consumer<Retrofit.Builder> getRetrofitRewriter() {
        return retrofitRewriter;
    }

    public Function<IApiAnchor, ApiObserver> getObserverFactory() {
        return observerFactory;
    }

    public boolean isSaveCookie() {
        return saveCookie;
    }

    public boolean isDebug() {
        return debug;
    }

    public boolean isUseCache() {
        return useCache;
    }

    public static class Builder {

        private String              baseUrl; // base url
        private String              host; // host
        private Map<String, String> baseUrlMap = new HashMap<>(); // 多 url 映射
        private Map<String, String> headers    = new HashMap<>(); // 公共 header
        private boolean             saveCookie; // 是否使用 cookie
        private boolean             debug; // 是否为调试模式
        private boolean             useCache; // 是否使用缓存

        private Consumer<OkHttpClient.Builder>    okHttpRewriter; // 重写 okhttp
        private Consumer<Retrofit.Builder>        retrofitRewriter; // 重写 retrofit
        private Function<IApiAnchor, ApiObserver> observerFactory; // 生成 Observer


        public ApiOptions build() {
            ApiOptions options = new ApiOptions();
            options.baseUrl = baseUrl;
            options.host = host;
            options.headers = headers;
            options.baseUrlMap = baseUrlMap;
            options.debug = debug;
            options.useCache = useCache;
            options.saveCookie = saveCookie;
            options.okHttpRewriter = okHttpRewriter;
            options.retrofitRewriter = retrofitRewriter;
            options.observerFactory = observerFactory;
            return options;
        }

        public Builder(String baseUrl) {
            this.baseUrl = baseUrl;
        }

        public Builder host(String host) {
            this.host = host;
            return this;
        }

        public Builder setSaveCookie(boolean saveCookie) {
            this.saveCookie = saveCookie;
            return this;
        }

        public Builder setDebug(boolean debug) {
            this.debug = debug;
            return this;
        }

        public Builder setUseCache(boolean useCache) {
            this.useCache = useCache;
            return this;
        }

        // 添加多 baseUrl,使用 domain 区分
        public Builder addBaseUrl(String domain, String baseUrl) {
            if (!EmptyX.isAnyEmpty(domain, baseUrl)) {
                this.baseUrlMap.put(domain, baseUrl);
            }
            return this;
        }

        // 添加通用 header
        public Builder addHeader(String key, String value) {
            if (!EmptyX.isEmpty(key)) {
                this.headers.put(key, value);
            }
            return this;
        }

        public Builder rewriteOkHttp(Consumer<OkHttpClient.Builder> okHttpRewriter) {
            this.okHttpRewriter = okHttpRewriter;
            return this;
        }

        public Builder rewriteRetrofit(Consumer<Retrofit.Builder> retrofitRewriter) {
            this.retrofitRewriter = retrofitRewriter;
            return this;
        }

        public Builder observerFactory(Function<IApiAnchor, ApiObserver> observerFactory) {
            this.observerFactory = observerFactory;
            return this;
        }
    }

}
