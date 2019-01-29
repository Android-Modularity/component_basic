package com.zfy.component.basic.foundation.api;

import android.util.SparseArray;

import com.march.common.mgrs.IMgr;

import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.ListCompositeDisposable;

/**
 * CreateAt : 2018/9/27
 * Describe : 请求队列统一管理
 *
 * @author chendong
 */
public class ApiQueueMgr implements IMgr {

    private SparseArray<ListCompositeDisposable> mDisposableMap;

    ApiQueueMgr() {
        mDisposableMap = new SparseArray<>();
    }

    private boolean checkAnchor(IApiAnchor anchor, Disposable disposable) {
        if (disposable == null) {
            return true;
        }
        if (anchor == null) {
            disposable.dispose();
            return true;
        }
        return false;
    }

    public void addRequest(IApiAnchor anchor, Disposable disposable) {
        if (checkAnchor(anchor, disposable)) {
            return;
        }
        if (mDisposableMap.size() == 0) {
            return;
        }
        int key = anchor.uniqueKey();
        ListCompositeDisposable disposableContainer = mDisposableMap.get(key);
        if (disposableContainer == null) {
            disposableContainer = new ListCompositeDisposable();
            mDisposableMap.put(key, disposableContainer);
        }
        disposableContainer.add(disposable);
    }


    public void removeRequest(IApiAnchor anchor, Disposable disposable) {
        if (checkAnchor(anchor, disposable)) {
            return;
        }
        if (mDisposableMap.size() == 0) {
            return;
        }
        int key = anchor.uniqueKey();
        if (!disposable.isDisposed()) {
            disposable.dispose();
        }
        ListCompositeDisposable disposableContainer = mDisposableMap.get(key);
        if (disposableContainer != null) {
            disposableContainer.delete(disposable);
        }
    }

    // 取消指定 tag 的请求
    public void cancelRequest(IApiAnchor anchor) {
        if (mDisposableMap.size() == 0) {
            return;
        }
        int key = anchor.uniqueKey();
        ListCompositeDisposable disposableContainer = mDisposableMap.get(key);
        if (disposableContainer != null) {
            if (!disposableContainer.isDisposed()) {
                disposableContainer.dispose();
            }
            mDisposableMap.remove(key);
        }
    }

    // 取消所有请求
    public void cancelAllRequest() {
        if (mDisposableMap.size() == 0) {
            return;
        }
        for (int i = 0; i < mDisposableMap.size(); i++) {
            int key = mDisposableMap.keyAt(i);
            ListCompositeDisposable disposableContainer = mDisposableMap.get(key);
            if (disposableContainer != null) {
                if (!disposableContainer.isDisposed()) {
                    disposableContainer.dispose();
                }
                mDisposableMap.remove(key);
            }
        }
    }

    @Override
    public void recycle() {
        cancelAllRequest();
    }

    @Override
    public boolean isRecycled() {
        return mDisposableMap.size() == 0;
    }
}
