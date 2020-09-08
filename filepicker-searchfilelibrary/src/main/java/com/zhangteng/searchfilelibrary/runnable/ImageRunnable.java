package com.zhangteng.searchfilelibrary.runnable;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;

import com.zhangteng.searchfilelibrary.callback.GetListCallbak;
import com.zhangteng.searchfilelibrary.entity.ImageEntity;
import com.zhangteng.searchfilelibrary.entity.MediaEntity;
import com.zhangteng.searchfilelibrary.utils.MediaStoreUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by swing on 2018/8/27.
 */
public class ImageRunnable implements Runnable {
    private Context context;
    private Handler handler;

    public ImageRunnable(Context context) {
        this.context = context;
    }

    public ImageRunnable(Handler handler) {
        this.handler = handler;
    }

    /**
     * 根据图片的ID得到缩略图
     *
     * @param cr
     * @param imageId
     * @return
     */
    public static Bitmap getThumbnailsFromImageId(ContentResolver cr, String imageId) {
        if (imageId == null || imageId.equals(""))
            return null;

        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inDither = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        long imageIdLong = Long.parseLong(imageId);
        //via imageid get the bimap type thumbnail in thumbnail table.
        bitmap = MediaStore.Images.Thumbnails.getThumbnail(cr, imageIdLong, MediaStore.Images.Thumbnails.MINI_KIND, options);

        return bitmap;
    }

    @Override
    public void run() {
        if (handler != null) {
            getImageNames(new GetListCallbak<MediaEntity>() {
                @Override
                public void onSuccess(List<MediaEntity> list) {
                    Message message = new Message();
                    message.what = GetListCallbak.SUCCESS;
                    message.obj = list;
                    handler.sendMessage(message);
                }

                @Override
                public void onFailed(String msg) {
                    handler.sendEmptyMessage(GetListCallbak.FAILED);
                }
            });
            return;
        }
        if (!MediaStoreUtil.getImage().isEmpty()) {
            MediaStoreUtil.clearImage();
        }
        getImageNames(new GetListCallbak<MediaEntity>() {
            @Override
            public void onSuccess(List<MediaEntity> list) {
                MediaStoreUtil.addImage(list);
            }

            @Override
            public void onFailed(String msg) {
                MediaStoreUtil.clearImage();
            }
        });
    }

    /**
     * 查询图片文件名称
     *
     * @return
     */
    public void getImageNames(final GetListCallbak<MediaEntity> callBack) {
        List<MediaEntity> list = new ArrayList<MediaEntity>();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.TITLE,
                        MediaStore.Images.Media.MIME_TYPE, MediaStore.Images.Media.SIZE, MediaStore.Images.Media.DATA}, null,
                new String[]{}, null);
        while (cursor.moveToNext()) {
            Log.i("ImageRunnable", "filePath==" + MediaStore.Images.Media.DATA);
            String filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            String fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
            long fileSize = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.SIZE));
            MediaEntity entity = new ImageEntity(fileName, filePath, fileSize, MediaEntity.MEDIA_IMAGE);
            Log.i("ImageRunnable", "==查询的图片==" + entity.toString());
            list.add(entity);
        }
        callBack.onSuccess(list);
    }
}
