package com.zfy.component.basic.foundation.api.observer;


import com.zfy.component.basic.foundation.api.Api;
import com.zfy.component.basic.foundation.api.IApiAnchor;
import com.zfy.component.basic.foundation.api.config.ReqConfig;
import com.zfy.component.basic.foundation.api.exception.ApiException;

import java.lang.ref.WeakReference;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * CreateAt : 2017/7/5
 * Describe : 观察类
 *
 * @author chendong
 */
public class ApiObserver<D> implements Observer<D> {

    public static final String TAG = ApiObserver.class.getSimpleName();

    protected Disposable             disposable;
    protected Consumer<D>            nextConsumer;
    protected Consumer<ApiException> errorConsumer;
    protected Action                 finishAction;

    protected ReqConfig requestConfig;

    private boolean isDispose;

    private WeakReference<IApiAnchor> anchor;

    public ApiObserver(IApiAnchor host) {
        this.anchor = new WeakReference<>(host);
        this.requestConfig = ReqConfig.create();
    }


    @Override
    public void onSubscribe(Disposable d) {
        disposable = new Disposable() {
            @Override
            public void dispose() {
                d.dispose();
                isDispose = true;
            }

            @Override
            public boolean isDisposed() {
                return d.isDisposed();
            }
        };
        Api.queue().addRequest(anchor.get(), disposable);
    }

    @Override
    public void onNext(@NonNull D t) {
        if (isDispose) {
            return;
        }
        if (interceptNext(t)) {
            return;
        }
        if (nextConsumer != null) {
            try {
                nextConsumer.accept(t);
            } catch (Exception e) {
                onError(e);
            }
        }
    }

    // 截断返回
    public boolean interceptNext(@NonNull D d) {
        return false;
    }

    @Override
    public void onError(@NonNull Throwable e) {
        if (isDispose) {
            return;
        }
        ApiException ex = ApiException.parseApiException(e);
        if (ex.code == ApiException.CODE_INTERCEPT) {
            onFinish();
            return;
        }
        if (errorConsumer != null) {
            try {
                errorConsumer.accept(ex);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        onFinish();
    }

    @Override
    public void onComplete() {
        if (isDispose) {
            return;
        }
        onFinish();
    }

    // onError or onComplete
    protected void onFinish() {
        Api.queue().removeRequest(anchor.get(), disposable);
        if (isDispose) {
            return;
        }
        if (finishAction != null) {
            try {
                finishAction.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setNextConsumer(Consumer<D> nextConsumer) {
        this.nextConsumer = nextConsumer;
    }

    public void setErrorConsumer(Consumer<ApiException> errorConsumer) {
        this.errorConsumer = errorConsumer;
    }

    public void setFinishAction(Action finishAction) {
        this.finishAction = finishAction;
    }

    public void setRequestConfig(ReqConfig requestConfig) {
        this.requestConfig = requestConfig;
    }
}
