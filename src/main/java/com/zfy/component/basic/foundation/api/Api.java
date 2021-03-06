package com.zfy.component.basic.foundation.api;

import android.util.LruCache;

import com.google.gson.Gson;
import com.march.common.Common;
import com.march.common.x.EmptyX;
import com.zfy.component.basic.foundation.api.config.ApiOptions;
import com.zfy.component.basic.foundation.api.converts.StringConvertFactory;
import com.zfy.component.basic.foundation.api.cookie.PersistentCookieJar;
import com.zfy.component.basic.foundation.api.cookie.cache.SetCookieCache;
import com.zfy.component.basic.foundation.api.cookie.persistence.SharedPrefsCookiePersistor;
import com.zfy.component.basic.foundation.api.interceptor.BaseUrlInterceptor;
import com.zfy.component.basic.foundation.api.interceptor.CacheRequestInterceptor;
import com.zfy.component.basic.foundation.api.interceptor.CacheResponseInterceptor;
import com.zfy.component.basic.foundation.api.interceptor.HeaderInterceptor;
import com.zfy.component.basic.foundation.api.interceptor.MockInterceptor;
import com.zfy.component.basic.foundation.api.mock.ApiMock;

import java.io.File;
import java.util.concurrent.TimeUnit;

import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * CreateAt : 2017/6/30
 * Describe : 单例，API请求管理
 *
 * @author chendong
 */
public class Api {

    public static final String TAG = Api.class.getSimpleName();

    public static final String DOMAIN_KEY  = "api-domain"; // base url domain
    public static final String KEY_AUTH    = "Authorization"; // token
    public static final String KEY_CHANNEL = "Channel"; // 渠道

    private static volatile Api sInst = new Api();

    private LruCache<String, Object> mServiceMap; // 服务缓存
    private OkHttpClient             mOkHttpClient; // client
    private Retrofit                 mRetrofit; // retrofit
    private ApiOptions               mApiConfig; // config
    private ApiQueueMgr              mApiQueueMgr; // queue
    private ApiMock                  mApiMock;


    private Api() {
        mServiceMap = new LruCache<>(10);
        mApiQueueMgr = new ApiQueueMgr();
        mApiMock = new ApiMock();
    }

    public static Api getInst() {
        if (sInst == null) {
            synchronized (Api.class) {
                if (sInst == null) {
                    sInst = new Api();
                }
            }
        }
        return sInst;
    }

    public static void init(ApiOptions apiConfig) {
        sInst.mApiConfig = apiConfig;
    }

    public static ApiOptions config() {
        return getInst().mApiConfig;
    }

    public static ApiQueueMgr queue() {
        return getInst().mApiQueueMgr;
    }

    public static ApiMock mock() {
        return getInst().mApiMock;
    }

    @SuppressWarnings("unchecked")
    public static <S> S use(Class<S> serviceClz) {
        try {
            Api inst = getInst();
            inst.ensureClientInit();
            Object apiService = inst.mServiceMap.get(serviceClz.getCanonicalName());
            if (apiService != null) {
                return (S) apiService;
            }
            S service = inst.mRetrofit.create(serviceClz);
            inst.mServiceMap.put(serviceClz.getCanonicalName(), service);
            return service;
        } catch (Exception e) {
            throw new IllegalStateException();
        }
    }

    private void ensureClientInit() {
        if (mOkHttpClient == null) {
            mOkHttpClient = provideOkHttpClient();
        }
        if (mRetrofit == null) {
            mRetrofit = provideRetrofit(mOkHttpClient);
        }
    }

    // 创建 OkHttpClient
    private OkHttpClient provideOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // 连接超时
        builder.connectTimeout(5 * 1000, TimeUnit.MILLISECONDS);
        // 读超时
        builder.readTimeout(5 * 1000, TimeUnit.MILLISECONDS);
        // 写超时
        builder.writeTimeout(5 * 1000, TimeUnit.MILLISECONDS);
        // 失败后重试
        builder.retryOnConnectionFailure(true);
        // 检查网络
        // builder.addInterceptor(new NetWorkInterceptor());
        // 用来添加全局 Header
        builder.addInterceptor(new HeaderInterceptor());
        // 动态 base url
        if (!EmptyX.isEmpty(config().getBaseUrlMap())) {
            builder.addInterceptor(new BaseUrlInterceptor());
        }
        // mock
        if (config().isDebug()) {
            builder.addInterceptor(new MockInterceptor());
        }
        // 缓存
        if (config().isUseCache()) {
            File httpCacheDirectory = new File(Common.app().getCacheDir(), "okhttp_cache");
            int cacheSize = 10 * 1024 * 1024; // 10 MB
            Cache cache = new Cache(httpCacheDirectory, cacheSize);
            builder.cache(cache);
            builder.addInterceptor(new CacheRequestInterceptor());
            builder.addNetworkInterceptor(new CacheResponseInterceptor());
        }
        // cookie
        if (config().isSaveCookie()) {
            PersistentCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(Common.app()));
            builder.cookieJar(cookieJar);
        }
        if (config().getOkHttpRewriter() != null) {
            config().getOkHttpRewriter().accept(builder);
        }
        // token校验，返回 403 时
        // builder.authenticator(new TokenAuthenticator());
        return builder.build();
    }


    // 创建 retrofit
    private Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        final Retrofit.Builder builder = new Retrofit.Builder();
        // client
        builder.client(okHttpClient);
        // baseUrl
        builder.baseUrl(mApiConfig.getBaseUrl());
        // rxJava 调用 adapter
        builder.addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()));
        // 转换为 String
        builder.addConverterFactory(StringConvertFactory.create());
        // 转换为 Json Model
        builder.addConverterFactory(GsonConverterFactory.create(new Gson()));
        if (config().getRetrofitRewriter() != null) {
            config().getRetrofitRewriter().accept(builder);
        }
        return builder.build();
    }

}
