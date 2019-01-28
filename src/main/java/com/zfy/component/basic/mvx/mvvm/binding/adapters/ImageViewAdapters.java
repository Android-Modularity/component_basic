package com.zfy.component.basic.mvx.mvvm.binding.adapters;

import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.request.RequestOptions;


/**
 * CreateAt : 2018/9/10
 * Describe : ImageView
 * bindUri
 * bindOpts
 *
 * @author chendong
 */
public final class ImageViewAdapters {

    @BindingAdapter(value = {"bindUri"})
    public static void bindUri(ImageView imageView, String uri) {
        if (!TextUtils.isEmpty(uri)) {
            // BuildInService.image().load(imageView.getContext(), uri, imageView);
        }
    }

    @BindingAdapter(value = {"bindUri", "bindOpts"}, requireAll = false)
    public static void bindUri(final ImageView imageView, String uri, RequestOptions opts) {
        if (TextUtils.isEmpty(uri)) {
            return;
        }
        if (opts == null) {
            opts = new RequestOptions();
        }
        // BuildInService.image().loadGlide(imageView.getContext(), uri, imageView, opts);
    }

}

