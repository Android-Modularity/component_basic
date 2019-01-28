package com.zfy.component.basic.foundation;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.march.common.adapter.JsonAdapter;

import java.lang.reflect.Type;
import java.util.ArrayList;
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

    @Override
    public String toJson(Object object) {
        return mGson.toJson(object);
    }

    @Override
    public <T> T toObj(String json, Class<T> cls) {
        return mGson.fromJson(json, cls);
    }

    @Override
    public <T> T toObj(String json, Type type) {
        return mGson.fromJson(json, type);
    }

    @Override
    public <T> List<T> toList(String json, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        try {
            JsonArray array = new JsonParser().parse(json).getAsJsonArray();
            for (final JsonElement elem : array) {
                list.add(mGson.fromJson(elem, clazz));
            }
        } catch (Exception e) {
            return null;
        }
        return list;
    }


    @Override
    public <K, V> Map<K, V> toMap(String json, Class<K> kClazz, Class<V> vClazz) {
        return mGson.fromJson(json, new TypeToken<Map<K, V>>() {
        }.getType());
    }

}
