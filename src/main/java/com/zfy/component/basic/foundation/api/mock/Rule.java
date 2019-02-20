package com.zfy.component.basic.foundation.api.mock;

import okhttp3.Request;

/**
 * CreateAt : 2019/1/31
 * Describe :
 *
 * @author chendong
 */
public class Rule {

    String path;

    public static Rule path(String path) {
        Rule rule = new Rule();
        rule.path = path;
        return rule;
    }

    public boolean match(Request request) {
        return request.url().toString().contains(path);
    }
}
