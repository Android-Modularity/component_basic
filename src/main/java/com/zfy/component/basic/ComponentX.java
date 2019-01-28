package com.zfy.component.basic;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.CheckResult;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.launcher.ARouter;
import com.march.common.Common;
import com.march.common.funcs.Consumer;
import com.march.common.x.EmptyX;
import com.zfy.component.basic.foundation.JsonAdapterImpl;
import com.zfy.component.basic.service.IComponentService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * CreateAt : 2019/1/24
 * Describe :
 *
 * @author chendong
 */
public class ComponentX {


    private static boolean  DEBUG         = false;
    private static String[] SERVICE_NAMES = new String[]{};

    // 重定向到组件主页
    public static boolean redirect(Context context, String entry) {
        if (TextUtils.isEmpty(entry)) {
            return false;
        }
        List<IComponentService> componentServices = getComponentServices();
        if (EmptyX.isEmpty(componentServices)) {
            return false;
        }
        IComponentService result = null;
        for (IComponentService service : componentServices) {
            String name = service.name();
            if (entry.equals(name)) {
                result = service;
                break;
            }
        }
        if (result == null) {
            return false;
        }
        result.redirect(context);
        return true;
    }


    // 获取组件初始化服务
    private static List<IComponentService> getComponentServices() {
        List<IComponentService> list = new ArrayList<>();
        for (String serviceName : SERVICE_NAMES) {
            Object service = service(serviceName);
            if (service instanceof IComponentService) {
                list.add((IComponentService) service);
            }
        }
        Collections.sort(list, (o1, o2) -> o1.priority() - o2.priority());
        return list;
    }


    /**
     * 初始化
     */
    public static void init(Application app, boolean debug, String[] serviceNames) {
        if (Common.exports.jsonParser == null) {
            Common.exports.jsonParser = new JsonAdapterImpl();
        }
        if (debug) {
            DEBUG = true;
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(app);
        SERVICE_NAMES = serviceNames;
        List<IComponentService> services = getComponentServices();
        for (IComponentService service : services) {
            service.init(app);
        }
    }

    /**
     * 依赖注入
     */
    public static void inject(Object object) {
        ARouter.getInstance().inject(object);
    }

    /**
     * 发现服务1
     */
    @Nullable
    public static <T> T service(Class<T> clazz) {
        return ARouter.getInstance().navigation(clazz);
    }

    /**
     * 发现服务
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
     * 发现服务1
     */
    @Nullable
    public static <T> void service(Class<T> clazz, Consumer<T> consumer) {
        T service = ARouter.getInstance().navigation(clazz);
        if (service != null) {
            consumer.accept(service);
        }
    }

    /**
     * 发现服务
     */
    @Nullable
    public static <T> void service(String path, Class<T> clazz, Consumer<T> consumer) {
        Object service = ARouter.getInstance().build(path).navigation();
        if (service != null) {
            consumer.accept((T) service);
        }
    }


    /**
     * 路由1
     */
    @CheckResult
    public static Postcard route(String url) {
        return ARouter.getInstance().build(url);
    }

    /**
     * 路由2
     */
    @CheckResult
    public static Postcard route(Uri uri) {
        return ARouter.getInstance().build(uri);
    }

    /**
     * 路由3
     */
    public static void go(Context context, String url) {
        ARouter.getInstance().build(url).navigation(context);
    }


    public static boolean extraSign(int extra, int index) {
        return ((extra >> index) & 1) == 1;
    }
}
