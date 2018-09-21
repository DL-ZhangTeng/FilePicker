package com.zhangteng.searchfilelibrary.entity;

import android.graphics.Bitmap;

/**
 * 视频类型
 * Created by swing on 2018/8/27.
 */
public class VideoEntity implements MediaEntity {

    String fileName;
    String filePath;
    long fileLength;
    int type;
    Bitmap thumbPath;
    long updateTime;

    public VideoEntity(String fileName, String filePath, long fileLength, int type, long updateTime) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileLength = fileLength;
        this.type = type;
        this.updateTime = updateTime;
    }

    public VideoEntity(String fileName, String filePath, long fileLength, int type, Bitmap thumbPath) {
        this.thumbPath = thumbPath;
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileLength = fileLength;
        this.type = type;
    }

    public VideoEntity() {
    }

    @Override
    public Bitmap getThumPath() {
        return thumbPath;
    }

    @Override
    public void setThumPath(Bitmap thumbPath) {
        this.thumbPath = thumbPath;
    }

    @Override
    public String getAlbum() {
        return null;
    }

    @Override
    public void setAlbum(String album) {

    }

    @Override
    public int getTime() {
        return 0;
    }

    @Override
    public void setTime(int time) {

    }

    @Override
    public String getArtist() {
        return null;
    }

    @Override
    public void setArtist(String artist) {

    }

    @Override
    public long getUpdateTime() {
        return updateTime;
    }

    @Override
    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public int getMediaType() {
        return type;
    }

    @Override
    public void setMediaType(int type) {
        this.type = type;
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String getFilePath() {
        return filePath;
    }

    @Override
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public long getFileLength() {
        return fileLength;
    }

    @Override
    public void setFileLength(long fileLength) {
        this.fileLength = fileLength;
    }

    @Override
    public String toString() {
        return "ImageEntity{" +
                "fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", fileLength=" + fileLength +
                '}';
    }
}
