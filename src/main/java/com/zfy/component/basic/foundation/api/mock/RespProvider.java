package com.zfy.component.basic.foundation.api.mock;

import android.content.Context;

import com.march.common.Common;
import com.march.common.funcs.Function;
import com.march.common.model.WeakContext;
import com.march.common.x.EmptyX;
import com.march.common.x.StreamX;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Request;

/**
 * CreateAt : 2019/1/31
 * Describe :
 *
 * @author chendong
 */
public class RespProvider {

    private Function<Request, String> provideFunc;

    public String provide(Request request) {
        if (provideFunc == null) {
            return null;
        }
        return provideFunc.apply(request);
    }

    public static RespProvider assets(String path) {
        RespProvider provider = new RespProvider();
        if (path.startsWith("/")) {
            path = new StringBuilder(path).deleteCharAt(0).toString();
        }
        final String validPath = path;
        WeakContext weakContext = new WeakContext(Common.app());
        provider.provideFunc = request -> {
            String result;
            try {
                Context ctx = weakContext.get();
                if (ctx != null) {
                    InputStream open = ctx.getResources().getAssets().open(validPath);
                    result = StreamX.saveStreamToString(open);
                } else {
                    result = "Context 被回收";
                }
                if (result == null) {
                    result = "无法获取 mock 的数据";
                }
            } catch (IOException e) {
                e.printStackTrace();
                result = "获取 mock 数据时发生异常" + e.getMessage();
            }
            return result;
        };
        return provider;
    }

    public static RespProvider file(File file) {
        RespProvider provider = new RespProvider();
        provider.provideFunc = request -> {
            if (EmptyX.isEmpty(file)) {
                return null;
            }
            String result;
            try {
                InputStream open = new FileInputStream(file);
                result = StreamX.saveStreamToString(open);
                if (result == null) {
                    result = "无法获取 mock 的数据";
                }
            } catch (IOException e) {
                e.printStackTrace();
                result = "获取 mock 数据时发生异常" + e.getMessage();
            }
            return result;
        };
        return provider;
    }

    public static RespProvider content(String content) {
        RespProvider provider = new RespProvider();
        provider.provideFunc = request -> content;
        return provider;
    }

    public static RespProvider obj(Object object) {
        RespProvider provider = new RespProvider();
        provider.provideFunc = request -> Common.exports.jsonParser.toJson(object);
        return provider;
    }
}
