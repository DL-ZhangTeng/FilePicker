package com.zhangteng.searchfilelibrary.entity;

import android.graphics.Bitmap;

/**
 * 音频类型
 * Created by swing on 2018/8/27.
 */
public class AudioEntity implements MediaEntity {

    String fileName;
    String filePath;
    long fileLength;
    int type;
    int time;
    String artist;
    String album;
    long updateTime;

    public AudioEntity() {
    }

    public AudioEntity(String fileName, String filePath, long fileLength, int type, long updateTime) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileLength = fileLength;
        this.type = type;
        this.updateTime = updateTime;
    }

    public AudioEntity(String fileName, String filePath, long fileLength, int type, int time, String artist, String album, long updateTime) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileLength = fileLength;
        this.type = type;
        this.time = time;
        this.artist = artist;
        this.album = album;
        this.updateTime = updateTime;
    }

    @Override
    public String getArtist() {
        return artist;
    }

    @Override
    public void setArtist(String artist) {
        this.artist = artist;
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
    public String getAlbum() {
        return album;
    }

    @Override
    public void setAlbum(String album) {
        this.album = album;
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
    public int getTime() {
        return time;
    }

    @Override
    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public Bitmap getThumPath() {
        return null;
    }

    @Override
    public void setThumPath(Bitmap bitmap) {

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
