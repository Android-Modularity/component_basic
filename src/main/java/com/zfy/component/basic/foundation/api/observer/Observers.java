package com.zfy.component.basic.foundation.api.observer;

import com.zfy.component.basic.foundation.api.Api;
import com.zfy.component.basic.foundation.api.IApiAnchor;
import com.zfy.component.basic.foundation.api.config.ReqConfig;
import com.zfy.component.basic.foundation.api.exception.ApiException;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * CreateAt : 2018/9/29
 * Describe : 工厂创建 Observer
 *
 * @author chendong
 */
public class Observers {

    @SuppressWarnings("unchecked")
    public static <D> ApiObserver<D> make(IApiAnchor host) {
        return Api.config().getObserverFactory().apply(host);
    }

    public static <D> ApiObserver<D> make(IApiAnchor host, Consumer<D> nextConsumer) {
        ApiObserver<D> observer = make(host);
        observer.setNextConsumer(nextConsumer);
        return observer;
    }


    public static <D> ApiObserver<D> make(IApiAnchor host, Consumer<D> nextConsumer, Consumer<ApiException> errorConsumer) {
        ApiObserver<D> observer = make(host);
        observer.setNextConsumer(nextConsumer);
        observer.setErrorConsumer(errorConsumer);
        return observer;
    }

    public static <D> ApiObserver<D> make(IApiAnchor host, Consumer<D> nextConsumer, Consumer<ApiException> errorConsumer, Action finishAction) {
        ApiObserver<D> observer = make(host);
        observer.setNextConsumer(nextConsumer);
        observer.setErrorConsumer(errorConsumer);
        observer.setFinishAction(finishAction);
        return observer;
    }

    public static <D> ApiObserver<D> make(IApiAnchor host, ReqConfig reqConfig, Consumer<D> nextConsumer) {
        ApiObserver<D> observer = make(host);
        observer.setNextConsumer(nextConsumer);
        observer.setRequestConfig(reqConfig);
        return observer;
    }

    public static <D> ApiObserver<D> make(IApiAnchor host, ReqConfig reqConfig, Consumer<D> nextConsumer, Consumer<ApiException> errorConsumer) {
        ApiObserver<D> observer = make(host);
        observer.setNextConsumer(nextConsumer);
        observer.setErrorConsumer(errorConsumer);
        observer.setRequestConfig(reqConfig);
        return observer;
    }

    public static <D> ApiObserver<D> make(IApiAnchor host, ReqConfig reqConfig, Consumer<D> nextConsumer, Consumer<ApiException> errorConsumer, Action finishAction) {
        ApiObserver<D> observer = make(host);
        observer.setNextConsumer(nextConsumer);
        observer.setErrorConsumer(errorConsumer);
        observer.setFinishAction(finishAction);
        observer.setRequestConfig(reqConfig);
        return observer;
    }

}
