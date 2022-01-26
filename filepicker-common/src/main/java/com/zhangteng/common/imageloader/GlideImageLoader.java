package com.zhangteng.common.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;

import com.bumptech.glide.Glide;

/**
 * Created by swing on 2018/4/18.
 */
public class GlideImageLoader implements ImageLoader {
    private int placeHolder;

    public GlideImageLoader placeholder(@DrawableRes int placeHolder) {
        this.placeHolder = placeHolder;
        return this;
    }

    @Override
    public void loadImage(Context context, ImageView imageView, Bitmap uri) {
        Glide.with(context)
                .load(uri)
                .placeholder(placeHolder)
                .centerCrop()
                .into(imageView);
    }

    @Override
    public void loadImage(Context context, ImageView imageView, String path) {
        Glide.with(context)
                .load(path)
                .placeholder(placeHolder)
                .centerCrop()
                .into(imageView);
    }
}
