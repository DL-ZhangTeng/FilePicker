package com.zhangteng.searchfilelibrary.runnable;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.zhangteng.searchfilelibrary.callback.GetListCallbak;
import com.zhangteng.searchfilelibrary.config.SearchCofig;
import com.zhangteng.searchfilelibrary.entity.DocumentEntity;
import com.zhangteng.searchfilelibrary.entity.MediaEntity;
import com.zhangteng.searchfilelibrary.utils.MediaStoreUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.zhangteng.searchfilelibrary.utils.MediaStoreUtil.MathFile;

/**
 * Created by swing on 2018/8/27.
 */
public class DocumentRunnable implements Runnable {
    private Context context;
    private Handler handler;

    public DocumentRunnable(Context context) {
        this.context = context;
    }

    public DocumentRunnable(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        String[] endNamed = new String[]{
                "txt", "pdf",
                "doc", "docx",
                "xls", "xlsx",
                "ppt", "pptx"
        };
        if (handler != null) {
            getAllFiles(SearchCofig.BASE_SD_PATH, endNamed, new GetListCallbak<MediaEntity>() {
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
        if (!MediaStoreUtil.getDocument().isEmpty()) {
            MediaStoreUtil.clearDocument();
        }
        getAllFiles(SearchCofig.BASE_SD_PATH, endNamed, new GetListCallbak<MediaEntity>() {
            @Override
            public void onSuccess(List<MediaEntity> list) {
                MediaStoreUtil.addDocument(list);
            }

            @Override
            public void onFailed(String msg) {
                MediaStoreUtil.clearDocument();
            }
        });
    }

    //  遍历相应的文件
    public List<MediaEntity> getAllFiles(String filePath, String[] endName, GetListCallbak<MediaEntity> callBack) {
        List<MediaEntity> file_lists = new ArrayList<MediaEntity>();
        try {
            File root = new File(filePath);
            File files[] = root.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.isDirectory()) {
                        //扫描结束后才会调用回调
                        file_lists.addAll(getAllFiles(f.getAbsolutePath(), endName, null));
                        // 每扫描一个文件夹都会调用回调，但是必须修改 MediaStoreUtil.addRar()的逻辑，
                        // 不然每次回调都会返回已查总数（MediaStoreUtil记录的是总数且listener返回的也是总数）
//                        getAllFiles(f.getAbsolutePath(), endName, callBack);
                    } else {
                        for (int i = 0; i < endName.length; i++) {
                            if (f.getName().endsWith(endName[i])) {

                                String fileName = f.getName();
                                String fPath = f.getAbsolutePath();
                                long fileLength = f.length();
                                int fileType = MathFile(fileName);
                                long updateTime = f.lastModified();
                                if (fileLength > 10) {
                                    MediaEntity entity = new DocumentEntity(fileName, fPath, fileLength, fileType, updateTime);
                                    Log.i("DocumentRunnable", "====检索的文件的====" + entity.toString());
                                    file_lists.add(entity);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            if (callBack != null) {
                callBack.onFailed(e.toString());
            }
        }
        if (callBack != null && !file_lists.isEmpty()) {
            callBack.onSuccess(file_lists);
        }
        Log.i("DocumentRunnable", "====检索的文件的个数====" + file_lists.size());
        return file_lists;
    }

}