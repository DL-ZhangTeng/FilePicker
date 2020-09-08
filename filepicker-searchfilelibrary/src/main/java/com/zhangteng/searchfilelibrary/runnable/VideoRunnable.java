package com.zhangteng.searchfilelibrary.runnable;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;

import com.zhangteng.searchfilelibrary.callback.GetListCallbak;
import com.zhangteng.searchfilelibrary.entity.MediaEntity;
import com.zhangteng.searchfilelibrary.entity.VideoEntity;
import com.zhangteng.searchfilelibrary.utils.MediaStoreUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by swing on 2018/8/27.
 */
public class VideoRunnable implements Runnable {
    private Context context;
    private Handler handler;

    public VideoRunnable(Context context) {
        this.context = context;
    }

    public VideoRunnable(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        if (handler != null) {
            getVideoInfo(new GetListCallbak<MediaEntity>() {
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
        if (!MediaStoreUtil.getVideo().isEmpty()) {
            MediaStoreUtil.clearVideo();
        }
        getVideoInfo(new GetListCallbak<MediaEntity>() {
            @Override
            public void onSuccess(List<MediaEntity> list) {
                MediaStoreUtil.addVideo(list);
            }

            @Override
            public void onFailed(String msg) {
                MediaStoreUtil.clearVideo();
            }
        });
    }

    /**
     * 获得所有视频文件
     *
     * @param
     */
    public void getVideoInfo(final GetListCallbak<MediaEntity> callBack) {
        List<MediaEntity> videoList = new ArrayList<MediaEntity>();
        String[] mediaColumns = new String[]{
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.MIME_TYPE};
        //首先检索SDcard上所有的video
        Cursor cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, mediaColumns, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                String mimeType = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                long fileSize = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
                //获取当前Video对应的Id，然后根据该ID获取其Thumb
                String id = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
                Bitmap thumPath = getVideoThumbnail(context, id);
                Log.i("VideoRunnable", "====图片的路径===" + thumPath);
                MediaEntity info = new VideoEntity(title, filePath, fileSize, MediaEntity.MEDIA_VIDEO, thumPath);
                videoList.add(info);
            } while (cursor.moveToNext());
        }
        callBack.onSuccess(videoList);
    }

    // 获取视频缩略图
    public Bitmap getVideoThumbnail(String filePath) {
        Bitmap b = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            b = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();

        } finally {
            try {
                retriever.release();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return b;
    }

    public Bitmap getVideoThumbnail(Context context, String id) {
        if (id == null || id.equals(""))
            return null;
        long videoId = Long.parseLong(id);
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inDither = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        bitmap = MediaStore.Video.Thumbnails.getThumbnail(context.getContentResolver(), videoId, MediaStore.Video.Thumbnails.MINI_KIND, options);
        return bitmap;
    }

    public String getVideoThumbnail(Context context, int id) {
        String thumpath = null;
        // MediaStore.Video.Thumbnails.DATA:视频缩略图的文件路径
        String[] thumbColumns = {MediaStore.Video.Thumbnails.DATA,
                MediaStore.Video.Thumbnails.VIDEO_ID};

        Cursor thumbCursor = context.getContentResolver().query(
                MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
                thumbColumns, MediaStore.Video.Thumbnails.VIDEO_ID
                        + "=" + id, null, null);
        if (thumbCursor.moveToFirst()) {
            thumpath = thumbCursor.getString(thumbCursor
                    .getColumnIndex(MediaStore.Video.Thumbnails.DATA));
        }
        return thumpath;
    }
}