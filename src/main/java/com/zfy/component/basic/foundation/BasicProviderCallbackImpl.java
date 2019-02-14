package com.zfy.component.basic.foundation;

import android.content.Context;

import com.alibaba.android.arouter.facade.template.IProvider;
import com.march.common.x.EmptyX;
import com.zfy.component.basic.ComponentX;
import com.zfy.component.basic.mvx.mvp.IMvpView;
import com.zfy.component.basic.mvx.mvp.presenter.MvpPresenter;
import com.zfy.mantis.api.provider.BundleProvider;
import com.zfy.mantis.api.provider.IDataProvider;
import com.zfy.mantis.api.provider.IObjProvider;
import com.zfy.mantis.api.provider.ProviderCallbackImpl;

/**
 * CreateAt : 2019/1/30
 * Describe :
 *
 * @author chendong
 */
public class BasicProviderCallbackImpl extends ProviderCallbackImpl {
    @Override
    public IDataProvider getDataProvider(Object target) {
        if (target instanceof MvpPresenter) {
            IMvpView view = ((MvpPresenter) target).getView();
            return BundleProvider.use(view.getData());
        }
        return super.getDataProvider(target);
    }

    @Override
    public IObjProvider getObjProvider(Object target, IDataProvider dataProvider) {
        return (key, clazz) -> {
            if (IProvider.class.isAssignableFrom(clazz)) {
                return provideARouterService(target, key, clazz);
            }
            return null;
        };
    }


    // 帮助发现服务
    private IProvider provideARouterService(Object target, String key, Class<?> clazz) {
        IProvider provider = null;
        if (EmptyX.isEmpty(key)) {
            provider = (IProvider) ComponentX.service(clazz);
        } else {
            provider = ComponentX.service(key);
        }
        if (provider != null) {
            if (target instanceof Context) {
                provider.init((Context) target);
            } else if (target instanceof MvpPresenter) {
                provider.init(((MvpPresenter) target).getView().getContext());
            }
        }
        return provider;
    }
}
