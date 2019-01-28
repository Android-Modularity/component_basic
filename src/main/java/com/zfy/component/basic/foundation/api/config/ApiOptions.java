package com.zfy.component.basic.foundation.api.config;

import com.march.common.x.EmptyX;

import java.util.HashMap;
import java.util.Map;

/**
 * CreateAt : 2018/1/12
 * Describe :
 * - 多 baseUrl 支持
 * - common Header 支持
 *
 * @author chendong
 */
public class ApiOptions {

    private String baseUrl;
    private String host;
    private Map<String, String> baseUrlMap = new HashMap<>();
    private Map<String, String> headers    = new HashMap<>();
    private Map<String, String> config     = new HashMap<>();

    private ApiOptions(String baseUrl) {
        if (EmptyX.isEmpty(baseUrl)) {
            throw new IllegalArgumentException();
        }
        this.baseUrl = baseUrl;
    }

    public static ApiOptions create(String baseUrl) {
        return new ApiOptions(baseUrl);
    }

    public ApiOptions setHost(String host) {
        this.host = host;
        return this;
    }

    public String getHost() {
        return host;
    }

    // 添加多 baseUrl,使用 domain 区分
    public ApiOptions addBaseUrl(String domain, String baseUrl) {
        if (!EmptyX.isAnyEmpty(domain, baseUrl)) {
            this.baseUrlMap.put(domain, baseUrl);
        }
        return this;
    }

    // 添加通用 header
    public ApiOptions addHeader(String key, String value) {
        if (!EmptyX.isEmpty(key)) {
            this.headers.put(key, value);
        }
        return this;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public Map<String, String> getBaseUrlMap() {
        return baseUrlMap;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Map<String, String> getConfig() {
        if (config == null) {
            config = new HashMap<>();
        }
        return config;
    }

    public void setConfig(Map<String, String> config) {
        this.config = config;
    }
}
