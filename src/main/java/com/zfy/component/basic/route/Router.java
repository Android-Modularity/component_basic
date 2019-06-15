package com.zfy.component.basic.route;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.launcher.ARouter;
import com.march.common.funcs.Consumer;
import com.zfy.component.basic.R;

/**
 * CreateAt : 2019/1/24
 * Describe :
 * 路由工具
 *
 * @author chendong
 */
public class Router {

    /**
    1. 如何标记？

        public static final int NEED_LOGIN  = 0b00000000001; // 需要登录
        public static final int NEED_TOAST  = 0b00000000010; // 需要提示
        public static final int NEED_TOAST1 = 0b00000000100; // 需要提示1

        public static final int INDEX_LOGIN =  0; // 登录标记索引
        public static final int INDEX_TOAST =  1; // toast 标记索引
        public static final int INDEX_TOAST1 = 2; // toast 标记索引1

    2. 路由协议

        协议标砖:    schema://   host           /path                            ?query
        网络协议:    http://     www.baidu.com  /pic/test                        ?word=android
        终端协议:    schema://   host           /group  /type   /path 	        ?query
        最终实现:    hibros://   app            /ratio  /page   /index/test/ggg   ?tab=1&showtab=true&name=sss

    3. sample

        // 定义 group
        private static final String TEST_GROUP      = "/test";
        // 路由1
        public static final  String TEST_PAGE       = TEST_GROUP + Router.PAGE + "/test";
        // 路由1标记
        public static final  int    TEST_PAGE_EXTRA = NEED_LOGIN + NEED_TOAST;
        // 路由2 fragment
        public static final  String TEST_FRAGMENT   = TEST_GROUP + Router.FRAGMENT + "/goods/test";
        // 路由3 服务
        public static final  String TEST_SERVICE    = TEST_GROUP + Router.SERVICE + "/goods/test";
     */

    public static final String X        = "/";
    public static final String PAGE     = X + "page";
    public static final String FRAGMENT = X + "fragment";
    public static final String SERVICE  = X + "service";

    /**
     * 解析标记位
     *
     * @param extra 标记
     * @param index 标记下标
     * @return 标记位值
     */
    public static boolean check(int extra, int index) {
        return ((extra >> index) & 1) == 1;
    }

    /**
     * 发现服务1
     *
     * @param clazz 服务接口
     * @param <T>   范型
     * @return 服务对象
     */
    @Nullable
    public static <T> T service(Class<T> clazz) {
        return ARouter.getInstance().navigation(clazz);
    }

    /**
     * 发现服务2
     *
     * @param path 路径
     * @param <T>  范型
     * @return 服务对象
     */
    @Nullable
    public static <T> T service(String path) {
        Object service = ARouter.getInstance().build(path).navigation();
        if (service == null) {
            return null;
        }
        return (T) service;
    }

    /**
     * 发现服务3
     *
     * @param clazz    服务接口
     * @param consumer 查找到服务后执行
     * @param <T>      范型
     */
    @Nullable
    public static <T> void service(Class<T> clazz, Consumer<T> consumer) {
        T service = ARouter.getInstance().navigation(clazz);
        if (service != null) {
            consumer.accept(service);
        }
    }

    /**
     * 发现服务4
     *
     * @param path     路径
     * @param clazz    接口
     * @param consumer 当获取到 service 时执行，避免 NPE
     * @param <T>      范型
     */
    @Nullable
    public static <T> void service(String path, Class<T> clazz, Consumer<T> consumer) {
        Object service = ARouter.getInstance().build(path).navigation();
        try {
            if (service != null) {
                consumer.accept((T) service);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取 fragment
     *
     * @param url 路径
     * @return Fragment
     */
    public static Fragment fragment(String url, Bundle bundle) {
        Fragment fragment = (Fragment) ARouter.getInstance().build(url).navigation();
        if (fragment != null) {
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    /**
     * 获取 fragment
     *
     * @param url 路径
     * @return Fragment
     */
    public static Fragment fragment(String url) {
        return (Fragment) ARouter.getInstance().build(url).navigation();
    }


    /**
     * 获取 fragment
     *
     * @param url 路径
     * @return Fragment
     */
    public static Postcard fragmentNav(String url) {
        return ARouter.getInstance().build(url);
    }


    public static Postcard wrap(Postcard postcard) {
        return postcard.withTransition(R.anim.act_translate_in, R.anim.act_no_anim);
    }

    /**
     * 路由1
     *
     * @param url 路径
     */
    public static void open(Context context, String url, Bundle bundle) {
        wrap(ARouter.getInstance().build(url).with(bundle)).navigation(context);
    }

    /**
     * 路由2
     *
     * @param uri uri
     */
    public static void open(Context context, Uri uri, Bundle bundle) {
        wrap(ARouter.getInstance().build(uri).with(bundle)).navigation(context);
    }

    /**
     * 路由1
     *
     * @param url 路径
     */
    public static void open(Context context, String url) {
        wrap(ARouter.getInstance().build(url)).navigation(context);
    }


    /**
     * 路由2
     *
     * @param uri uri
     */
    public static void open(Context context, Uri uri) {
        wrap(ARouter.getInstance().build(uri)).navigation(context);
    }

    /**
     * 路由1
     *
     * @param url 路径
     */
    public static Postcard openNav(String url) {
        return wrap(ARouter.getInstance().build(url));
    }

    /**
     * 路由2
     *
     * @param uri uri
     */
    public static Postcard openNav(Uri uri) {
        return wrap(ARouter.getInstance().build(uri));
    }

}
