package com.zfy.component.basic.service.impl;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.service.SerializationService;
import com.march.common.Common;
import com.zfy.component.basic.service.BasicServices;

import java.lang.reflect.Type;

/**
 * CreateAt : 2019/1/27
 * Describe : 实现 json 解析服务
 *
 * @author chendong
 */
@Route(path = BasicServices.SERIALIZATION_SERVICE)
public class SerializationServiceImpl implements SerializationService {

    @Override
    @Deprecated
    public <T> T json2Object(String input, Class<T> clazz) {
        return Common.exports.jsonParser.toObj(input, clazz);
    }

    @Override
    public String object2Json(Object instance) {
        return Common.exports.jsonParser.toJson(instance);
    }

    @Override
    public <T> T parseObject(String input, Type type) {
        return Common.exports.jsonParser.toObj(input, type);
    }

    @Override
    public void init(Context context) {

    }
}
