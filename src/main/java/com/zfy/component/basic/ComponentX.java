package com.zfy.component.basic;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.march.common.Common;
import com.zfy.component.basic.foundation.JsonAdapterImpl;
import com.zfy.component.basic.foundation.MantisProvider;
import com.zfy.component.basic.route.Router;
import com.zfy.component.basic.service.IComponentService;
import com.zfy.mantis.api.Mantis;

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

    // 获取组件初始化服务
    private static List<IComponentService> getComponentServices(String[] SERVICE_NAMES) {
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
    public static void init(Application app, boolean debug) {
        String[] compServices = BuildConfig.COMP_SERVICES;
        if (Common.exports.jsonParser == null) {
            Common.exports.jsonParser = new JsonAdapterImpl();
        }
        if (debug) {
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(app);
        List<IComponentService> services = getComponentServices(compServices);
        for (IComponentService service : services) {
            service.initOrderly(Common.app());
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
