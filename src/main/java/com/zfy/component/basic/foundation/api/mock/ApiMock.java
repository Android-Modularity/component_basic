package com.zfy.component.basic.foundation.api.mock;

import java.util.HashMap;
import java.util.Map;

/**
 * CreateAt : 2019/1/31
 * Describe :
 *
 * @author chendong
 */
public class ApiMock {

    private Map<Rule, RespProvider> mMockRespMap = new HashMap<>();

    public void addMock(Rule rule, RespProvider provider) {
        mMockRespMap.put(rule, provider);
    }

    public Map<Rule, RespProvider> getMockRespMap() {
        return mMockRespMap;
    }
}
