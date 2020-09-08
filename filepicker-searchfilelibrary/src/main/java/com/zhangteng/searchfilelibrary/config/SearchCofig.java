package com.zhangteng.searchfilelibrary.config;

import android.os.Environment;

/**
 * Created by swing on 2018/8/27.
 */
public class SearchCofig {
    public static final String BASE_SD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static String EXTERNAL_SD = BASE_SD_PATH;
    /**
     * 外置存储弹出
     */
    public static String SDCARD_OUT_BROADCAST = "SEARCH_FILE_LIBRARAY_SDCARD_OUT_BROADCAST";
    /**
     * 外置存储插入
     */
    public static String SDCARD_IN_BROADCAST = "SEARCH_FILE_LIBRARAY_SDCARD_IN_BROADCAST";

}
