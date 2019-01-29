package com.zfy.component.basic.foundation.api;

import android.util.LruCache;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.march.common.funcs.Consumer;
import com.march.common.funcs.Function;
import com.zfy.component.basic.foundation.api.config.ApiOptions;
import com.zfy.component.basic.foundation.api.converts.StringConvertFactory;
import com.zfy.component.basic.foundation.api.interceptors.HeaderInterceptor;
import com.zfy.component.basic.foundation.api.interceptors.NetWorkInterceptor;
import com.zfy.component.basic.foundation.api.observers.ApiObserver;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.schedulers.Schedulers;
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

    private static Api                      sInst;
    private        LruCache<String, Object> mServiceMap; // 服务缓存
    private        OkHttpClient             mOkHttpClient; // client
    private        Retrofit                 mRetrofit; // retrofit
    private        ApiOptions               mApiConfig; // config
    private        ApiQueueMgr              mApiQueueMgr; // queue

    private Consumer<OkHttpClient.Builder> mOkHttpInitConsumer;
    private Consumer<Retrofit.Builder>     mRetrofitConsumer;
    private Function<Object, ApiObserver>  mObserverMaker;

    private Api(ApiOptions apiConfig) {
        mApiConfig = apiConfig;
        mServiceMap = new LruCache<>(10);
        mApiQueueMgr = new ApiQueueMgr();
    }

    public static Api getInst() {
        return sInst;
    }

    public static void init(ApiOptions apiConfig) {
        sInst = new Api(apiConfig);
    }

    public static ApiOptions config() {
        return getInst().mApiConfig;
    }

    public static ApiQueueMgr queue() {
        return getInst().mApiQueueMgr;
    }

    public static void cancelSelfRequest(int code) {
        Api inst = getInst();
        if (inst != null && queue() != null) {
            queue().cancelRequest(code);
        }
    }


    @SuppressWarnings("unchecked")
    public static <S> S use(Class<S> serviceClz) {
        try {
            Api inst = getInst();
            inst.ensureInitClient();
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

    public void setOkHttpInitConsumer(Consumer<OkHttpClient.Builder> okHttpInitConsumer) {
        mOkHttpInitConsumer = okHttpInitConsumer;
    }

    public void setRetrofitConsumer(Consumer<Retrofit.Builder> retrofitConsumer) {
        mRetrofitConsumer = retrofitConsumer;
    }

    public Function<Object, ApiObserver> getObserverMaker() {
        return mObserverMaker;
    }

    public void setObserverMaker(Function<Object, ApiObserver> observerMaker) {
        mObserverMaker = observerMaker;
    }

    private void ensureInitClient() {
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
        builder.addInterceptor(new NetWorkInterceptor());
        // 动态 base url
        // builder.addInterceptor(new BaseUrlInterceptor());
        // 用来添加全局 Header
        builder.addInterceptor(new HeaderInterceptor());
        if (mOkHttpInitConsumer != null) {
            mOkHttpInitConsumer.accept(builder);
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
        if (mRetrofitConsumer != null) {
            mRetrofitConsumer.accept(builder);
        }
        TypeToken.getParameterized(Map.class, String.class, String.class);
        return builder.build();
    }

}
