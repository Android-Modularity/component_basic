package com.zfy.component.basic.foundation;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.march.common.adapter.JsonAdapter;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * CreateAt : 2018/4/3
 * Describe :
 *
 * @author chendong
 */
public class JsonAdapterImpl implements JsonAdapter {

    private Gson mGson = new Gson();

    // obj -> json
    @Override
    public String toJson(Object object) {
        return mGson.toJson(object);
    }

    // json -> obj
    @Override
    public <T> T toObj(String json, Class<T> cls) {
        return mGson.fromJson(json, cls);
    }

    // json -> obj, type 扩展
    @Override
    public <T> T toObj(String json, Type type) {
        return mGson.fromJson(json, type);
    }

    // json -> list
    @Override
    public <T> List<T> toList(String json, Class<T> clazz) {
        return mGson.fromJson(json, TypeToken.getParameterized(List.class, clazz).getType());
    }

    // json -> map
    @Override
    public <K, V> Map<K, V> toMap(String json, Class<K> kClazz, Class<V> vClazz) {
        return mGson.fromJson(json, TypeToken.getParameterized(Map.class, kClazz, vClazz).getType());
    }

}
