package com.zhangteng.baselibrary.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Created by swing on 2018/4/18.
 */
public interface ImageLoader {
    void loadImage(Context context, ImageView imageView, Bitmap uri);

    void loadImage(Context context, ImageView imageView, String path);
}
