package com.zhangteng.common.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zhangteng.common.R;

/**
 * Created by swing on 2018/4/18.
 */
public class GlideImageLoader implements ImageLoader {
    private int placeHolder = R.mipmap.ic_launcher_round;

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
