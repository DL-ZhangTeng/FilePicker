package com.zhangteng.searchfilelibrary.runnable;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;

import com.zhangteng.searchfilelibrary.callback.GetListCallbak;
import com.zhangteng.searchfilelibrary.entity.AudioEntity;
import com.zhangteng.searchfilelibrary.entity.MediaEntity;
import com.zhangteng.searchfilelibrary.utils.MediaStoreUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by swing on 2018/8/27.
 */
public class AudioRunnable implements Runnable {
    private Context context;
    private Handler handler;

    public AudioRunnable(Context context) {
        this.context = context;
    }

    public AudioRunnable(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        if (handler != null) {
            getAllAudio(new GetListCallbak<MediaEntity>() {
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
        if (!MediaStoreUtil.getAudio().isEmpty()) {
            MediaStoreUtil.clearAudio();
        }
        getAllAudio(new GetListCallbak<MediaEntity>() {
            @Override
            public void onSuccess(List<MediaEntity> list) {
                MediaStoreUtil.addAudio(list);
            }

            @Override
            public void onFailed(String msg) {
                MediaStoreUtil.clearAudio();
            }
        });
    }

    public void getAllAudio(final GetListCallbak<MediaEntity> callBack) {
        List<MediaEntity> audios = new ArrayList<>();
        ContentResolver mContentResolver = context.getContentResolver();
        String[] projection = new String[]{MediaStore.Audio.AudioColumns._ID,
                MediaStore.Audio.AudioColumns.DATA,
                MediaStore.Audio.AudioColumns.DURATION,
                MediaStore.Audio.AudioColumns.SIZE,
                MediaStore.Audio.AudioColumns.ARTIST,
                MediaStore.Audio.AudioColumns.ALBUM,
                MediaStore.Audio.AudioColumns.DISPLAY_NAME,
                MediaStore.Audio.AudioColumns.DATE_MODIFIED};
        Cursor cursor = mContentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, null, null, MediaStore.Audio.AudioColumns.DATE_MODIFIED + " desc");
        while (cursor.moveToNext()) {
            String fileId = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns._ID));
            String fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DISPLAY_NAME));
            String filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA));
            long fileSize = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.SIZE));
            int time = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION));
            String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM));
            String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST));
            //方法1：歌曲文件的修改时间 （注意：精确到秒，所以还要*1000）：MediaStore.Audio.Media.DATE_MODIFIED
            long updateTime = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATE_MODIFIED));
            updateTime = updateTime * 1000;
            //大于50kb的视频在显示
            if (fileSize / 1024 >= 50) {
                MediaEntity fileItem = new AudioEntity(fileName, filePath, fileSize, MediaEntity.MEDIA_AUDIO, time, artist, album, updateTime);
                Log.i("AudioRunnable", "===检索的音频文件===" + fileItem.toString());
                audios.add(fileItem);
            }
        }
        cursor.close();
        callBack.onSuccess(audios);
    }
}