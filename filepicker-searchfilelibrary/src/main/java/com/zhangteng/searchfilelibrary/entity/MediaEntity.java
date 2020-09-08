package com.zhangteng.searchfilelibrary.entity;

import android.graphics.Bitmap;

/**
 * Created by swing on 2018/8/27.
 */
public interface MediaEntity {
    /**
     * 图片类型
     */
    public static final int MEDIA_IMAGE = 0;
    /**
     * 音频类型
     */
    public static final int MEDIA_AUDIO = 1;
    /**
     * 视频类型
     */
    public static final int MEDIA_VIDEO = 2;
    /**
     * 文档类型
     */
    public static final int MEDIA_DOCUMENT = 3;
    /**
     * 压缩类型
     */
    public static final int MEDIA_ZIP = 4;
    /**
     * pdf类型
     */
    public static final int MEDIA_PDF = 5;
    /**
     * word类型
     */
    public static final int MEDIA_DOC = 6;
    /**
     * PPT类型
     */
    public static final int MEDIA_PPT = 7;
    /**
     * EXCEL类型
     */
    public static final int MEDIA_EXCEL = 8;
    /**
     * TXT类型
     */
    public static final int MEDIA_TXT = 9;
    /**
     * APK类型
     */
    public static final int MEDIA_APK = 10;
    /**
     * 文件夹类型
     */
    public static final int MEDIA_FOLDER = 99;
    /**
     * 未知类型
     */
    public static final int MEDIA_UNKNOWN = 100;


    public static String MEDIA_TYPE = "MEDIA_TYPE";

    /**
     * 获取文件的类型
     */
    int getMediaType();

    /**
     * 设置文件的类型
     */
    void setMediaType(int type);

    /***
     * 获取文件的名字
     *
     * @return
     */
    String getFileName();

    /***
     * 设置文件的名字
     *
     * @param fileName
     */

    void setFileName(String fileName);

    /***
     * 获取文件的路径
     *
     * @return
     */
    String getFilePath();

    /***
     * 设置文件的路径
     *
     * @param filePath
     */
    void setFilePath(String filePath);

    /***
     * 获取文件的大小
     *
     * @return
     */
    long getFileLength();

    /***
     * 设置文件的大小
     *
     * @param fileLength
     */
    void setFileLength(long fileLength);

    /***
     * 获取缩略图的路径
     *
     * @return
     */
    Bitmap getThumPath();

    /***
     * 设置缩略图的路径
     *
     * @param bitmap
     */
    void setThumPath(Bitmap bitmap);

    String getAlbum();

    void setAlbum(String album);

    int getTime();

    void setTime(int time);

    String getArtist();

    void setArtist(String artist);

    long getUpdateTime();

    void setUpdateTime(long updateTime);
}
