package com.zhangteng.searchfilelibrary.runnable;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.zhangteng.searchfilelibrary.callback.GetListCallbak;
import com.zhangteng.searchfilelibrary.config.SearchCofig;
import com.zhangteng.searchfilelibrary.entity.MediaEntity;
import com.zhangteng.searchfilelibrary.utils.FileErgodicUtil;
import com.zhangteng.searchfilelibrary.utils.MediaStoreUtil;

import java.util.List;

/**
 * Created by swing on 2018/9/11.
 */
public class FolderRunnable implements Runnable {
    private Context context;
    private Handler handler;
    private String path;

    public FolderRunnable(String path) {
        this.path = path;
    }

    public FolderRunnable(Handler handler, String path) {
        this.handler = handler;
        this.path = path;
    }

    @Override
    public void run() {
        String[] endNamed = new String[]{
                "txt", "pdf",
                "doc", "docx",
                "xls", "xlsx",
                "ppt", "pptx",
                "rar", "zip", "apk",
                "jpg", "png", "jpeg", "gif",
                "mp3", "wave", "wma", "mpeg",
                "mp4", "wmv", "m3u8", "avi", "flv", "3gp"
        };

        if (handler != null) {
            getAllFiles(path == null ? SearchCofig.BASE_SD_PATH : path, endNamed, new GetListCallbak<MediaEntity>() {
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
        if (!MediaStoreUtil.getFolder().isEmpty()) {
            MediaStoreUtil.clearFolder();
        }
        getAllFiles(path == null ? SearchCofig.BASE_SD_PATH : path, endNamed, new GetListCallbak<MediaEntity>() {
            @Override
            public void onSuccess(List<MediaEntity> list) {
                MediaStoreUtil.addFolder(list);
            }

            @Override
            public void onFailed(String msg) {
                MediaStoreUtil.clearFolder();
            }
        });
    }

    //  遍历相应的文件
    public List<MediaEntity> getAllFiles(String filePath, String[] endName, GetListCallbak<MediaEntity> callBack) {
        List<MediaEntity> file_lists = null;
        try {
            file_lists = FileErgodicUtil.getFileList(filePath, endName);
        } catch (Exception e) {
            if (callBack != null) {
                callBack.onFailed(e.toString());
            }
        }
        if (callBack != null && !file_lists.isEmpty()) {
            callBack.onSuccess(file_lists);
        }
        Log.i("ApkRunnable", "====检索的文件的个数====" + file_lists.size());
        return file_lists;
    }
}
