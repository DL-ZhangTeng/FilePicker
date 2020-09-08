package com.zhangteng.searchfilelibrary;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.zhangteng.searchfilelibrary.config.SearchCofig;
import com.zhangteng.searchfilelibrary.entity.MediaEntity;
import com.zhangteng.searchfilelibrary.runnable.ApkRunnable;
import com.zhangteng.searchfilelibrary.runnable.AudioRunnable;
import com.zhangteng.searchfilelibrary.runnable.DocumentRunnable;
import com.zhangteng.searchfilelibrary.runnable.FolderRunnable;
import com.zhangteng.searchfilelibrary.runnable.ImageRunnable;
import com.zhangteng.searchfilelibrary.runnable.RarRunnable;
import com.zhangteng.searchfilelibrary.runnable.VideoRunnable;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by swing on 2018/8/27.
 */
public class FileService extends Service {
    public static final String TAG = "FileService";
    public static final String UPDATE_MAIN_UI_BROADRECEIVER = "com.zhangteng.searchfilelibarary.service.UPDATE_MAIN_UI_BROADRECEIVER";
    private static FileService instance;
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i(TAG, "===================SD==" + action);
            if (action.equals(Intent.ACTION_MEDIA_EJECT)) {
                SearchCofig.EXTERNAL_SD = "";
                context.sendBroadcast(new Intent(SearchCofig.SDCARD_OUT_BROADCAST));
                Log.i(TAG, "===================SD____out");
            } else if (action.equals(Intent.ACTION_MEDIA_MOUNTED)) {
                context.sendBroadcast(new Intent(SearchCofig.SDCARD_IN_BROADCAST));
                Log.i(TAG, "===================SD____in");
            } else if (action.equals(UsbManager.ACTION_USB_DEVICE_ATTACHED)) {
                context.sendBroadcast(new Intent(SearchCofig.SDCARD_IN_BROADCAST));
                Log.i(TAG, "===================USB____in");
            } else if (action.equals(UsbManager.ACTION_USB_DEVICE_DETACHED)) {
                context.sendBroadcast(new Intent(SearchCofig.SDCARD_OUT_BROADCAST));
                Log.i(TAG, "===================USB____out");
            }
        }
    };
    Timer timer;
    TimerTask task;
    private ExecutorService executor = Executors.newFixedThreadPool(10);

    public static FileService getInstance() {
        if (instance == null) {
            instance = new FileService();
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (instance == null) {
            instance = this;
        }
        initReceiver();
    }

    private void initReceiver() {
        IntentFilter filter = new IntentFilter();
        //sd插入
        filter.addAction(Intent.ACTION_MEDIA_EJECT);
        //sd拔出
        filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        filter.addDataScheme("file");
        registerReceiver(receiver, filter);

        IntentFilter filter_usb = new IntentFilter();
        //usb插入
        filter_usb.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        //usb拔出
        filter_usb.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        registerReceiver(receiver, filter_usb);
    }

    /***
     * 传入路径,检索目录下的文件夹和文件
     * @return
     */

    public void getFileList(String path) {
        getFolder(path);
    }

    public void getFileList(Handler handler, String path) {
        searchFolder(handler, path);
    }


    /**
     * public static final int MEDIA_IMAGE = 0;  //图片类型
     * public static final int MEDIA_AUDIO = 1;  //音频类型
     * public static final int MEDIA_VIDEO = 2;  //视频类型
     * public static final int MEDIA_DOCUMENT = 3;  //文档类型
     * public static final int MEDIA_ZIP = 4;  //压缩类型
     * public static final int MEDIA_APK = 10;  //APK类型
     */
    public void getMediaList(int searchId, Handler handler) {
        if (searchId == MediaEntity.MEDIA_AUDIO) {
            searchAudio(handler);
        } else if (searchId == MediaEntity.MEDIA_VIDEO) {
            searchVideo(handler);
        } else if (searchId == MediaEntity.MEDIA_IMAGE) {
            searchImage(handler);
        } else if (searchId == MediaEntity.MEDIA_DOCUMENT) {
            searchDocument(handler);
        } else if (searchId == MediaEntity.MEDIA_ZIP) {
            searchZip(handler);
        } else if (searchId == MediaEntity.MEDIA_APK) {
            searchApk(handler);
        }
    }

    /***
     * 刷新文件
     * @param context
     * @param  fileModel
     * 需要刷新文件的类型
     * -1 是刷所有的文件
     * public static final int MEDIA_IMAGE = 0;  //图片类型
     * public static final int MEDIA_AUDIO = 1;  //音频类型
     * public static final int MEDIA_VIDEO = 2;  //视频类型
     * public static final int MEDIA_DOCUMENT = 3;  //文档类型
     * public static final int MEDIA_ZIP = 4;  //压缩类型
     * public static final int MEDIA_APK = 10;  //APK类型
     */
    public void getMediaList(int fileModel, Context context) {
        try {
            if (fileModel == 0) {
                getPic(context);
            } else if (fileModel == 1) {
                getAudio(context);
            } else if (fileModel == 2) {
                getVideo(context);
            } else if (fileModel == 3) {
                getDucoment(context);
            } else if (fileModel == 4) {
                getZip(context);
            } else if (fileModel == 10) {
                getApk(context);
            } else if (fileModel == -1) {
                getPic(context);
                getAudio(context);
                getVideo(context);
                getZip(context);
                getApk(context);
                getDucoment(context);
            }
        } catch (Exception e) {
        }

        if (timer != null) {
            timer.cancel();
        }
        if (task != null) {
            task.cancel();
        }
        timer = new Timer(true);
        task = new MyTask(context);
        timer.schedule(task, 500);
    }


    //===================线程相关代码====================================================

    /***
     * 获取当前图片
     */
    private void getPic(Context context) {
        Runnable runnable = new ImageRunnable(context);
        executor.execute(runnable);
    }

    /***
     * 获取当前音乐
     */
    private void getAudio(Context context) {
        Runnable runnable = new AudioRunnable(context);
        executor.execute(runnable);
    }

    /***
     * 获取当前图片
     */
    private void getVideo(Context context) {
        Runnable runnable = new VideoRunnable(context);
        executor.execute(runnable);
    }

    /**
     * 获取zip的
     */
    private void getZip(Context context) {
        Runnable runnable = new RarRunnable(context);
        executor.execute(runnable);
    }

    private void getApk(Context context) {
        Runnable runnable = new ApkRunnable(context);
        executor.execute(runnable);
    }

    private void getDucoment(Context context) {
        Runnable runnable = new DocumentRunnable(context);
        executor.execute(runnable);
    }

    /**
     * 遍历所有文件
     */

    private void getFolder(String path) {
        Runnable runnable = new FolderRunnable(path);
        executor.execute(runnable);
    }

    private void searchApk(Handler handler) {
        executor.execute(new ApkRunnable(handler));
    }

    private void searchZip(Handler handler) {
        executor.execute(new RarRunnable(handler));
    }

    private void searchDocument(Handler handler) {
        executor.execute(new DocumentRunnable(handler));
    }

    private void searchImage(Handler handler) {
        executor.execute(new ImageRunnable(handler));
    }

    private void searchVideo(Handler handler) {
        executor.execute(new VideoRunnable(handler));
    }

    private void searchAudio(Handler handler) {
        executor.execute(new AudioRunnable(handler));
    }

    private void searchFolder(Handler handler, String path) {
        executor.execute(new FolderRunnable(handler, path));
    }

    public void execute(Runnable runnable) {
        executor.execute(runnable);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new FileBinder();
    }

    public class FileBinder extends Binder {
        public FileService getFileService() {
            return FileService.this;
        }
    }

    public class MyTask extends TimerTask {
        Context context;

        public MyTask(Context context) {
            this.context = context;
        }

        @Override
        public void run() {
            Intent intent = new Intent();
            intent.setAction(UPDATE_MAIN_UI_BROADRECEIVER);
            context.sendBroadcast(intent);
        }
    }
}
