package com.zfy.component.basic;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.march.common.Common;
import com.march.common.x.EmptyX;
import com.zfy.component.basic.foundation.JsonAdapterImpl;
import com.zfy.component.basic.foundation.MantisProvider;
import com.zfy.component.basic.route.Router;
import com.zfy.component.basic.service.IComponentService;
import com.zfy.mantis.annotation.LookupOpts;
import com.zfy.mantis.api.Mantis;
import com.zfy.mantis.api.provider.IDataProviderFactory;
import com.zfy.mantis.api.provider.IObjProvider;

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
            Object service = Router.service(serviceName);
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
        // init mantis
        Mantis.init(new MantisProvider.IDataProviderImpl(),
                new MantisProvider.IObjProviderImpl());
    }

    /**
     * 依赖注入
     * @param object 需要注入的对象
     */
    public static void inject(Object object) {
        ARouter.getInstance().inject(object);
        Mantis.inject(object);
    }

}
