package com.zfy.component.basic.foundation.api.exception;

import android.net.ParseException;

import com.google.gson.JsonParseException;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import javax.net.ssl.SSLHandshakeException;

import retrofit2.HttpException;


/**
 * CreateAt : 2018/9/27
 * Describe : 异常
 *
 * @author chendong
 */
public class ApiException extends IllegalStateException {

    public static final int CODE_OK            = 0; // 没有错误
    public static final int CODE_UNKNOW_ERROR  = 1; // 主动打断请求
    public static final int CODE_INTERCEPT     = 2; // 主动打断请求
    public static final int CODE_NETWORK_ERROR = 3; // 网络没有链连接
    public static final int CODE_HTTP_ERROR    = 4; // http 错误
    public static final int CODE_PARSE_ERROR   = 5; // 解析错误
    public static final int CODE_CONNECT_ERROR = 6; // 连接错误
    public static final int CODE_SSL_ERROR     = 7; // 握手错误
    public static final int CODE_TIMEOUT_ERROR = 8; // 超时
    public static final int CODE_DATA_NULL = 9; // data null

    public int       code;
    public String    msg;
    public Throwable error;


    public ApiException() {
    }

    public ApiException(String message) {
        super(message);
    }

    public ApiException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ApiException(int errorCode) {
        code = errorCode;
        switch (code) {
            case CODE_NETWORK_ERROR:
                msg = "网络未连接";
                break;
            case CODE_PARSE_ERROR:
                msg = "数据处理错误";
                break;
            default:
                break;
        }
    }

    public static ApiException parseApiException(Throwable e) {
        if (e instanceof ApiException) {
            return (ApiException) e;
        }
        ApiException ex = new ApiException();
        ex.error = e;
        if (e instanceof HttpException) {
            ex.code = CODE_HTTP_ERROR;
            ex.msg = "Http Error";
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            ex.code = CODE_PARSE_ERROR;
            ex.msg = "解析错误";
        } else if (e instanceof ConnectException) {
            ex.code = CODE_CONNECT_ERROR;
            ex.msg = "连接错误";
        } else if (e instanceof SSLHandshakeException) {
            ex.code = CODE_SSL_ERROR;
            ex.msg = "SSL 握手失败，证书错误";
        } else if (e instanceof ConnectTimeoutException || e instanceof SocketTimeoutException) {
            ex.code = CODE_TIMEOUT_ERROR;
            ex.msg = "连接超时";
        } else {
            ex.code = CODE_UNKNOW_ERROR;
            ex.msg = "未知错误";
        }
        return ex;
    }

}
