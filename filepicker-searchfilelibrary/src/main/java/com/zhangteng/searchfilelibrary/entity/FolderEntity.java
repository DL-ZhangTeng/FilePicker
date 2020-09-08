package com.zhangteng.searchfilelibrary.entity;

import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by swing on 2018/9/11.
 */
public class FolderEntity implements MediaEntity {

    String fileName;
    String filePath;
    int type;
    long updateTime;
    List<MediaEntity> children;
    FolderEntity parent;

    public FolderEntity(String fileName, String filePath, int type) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.type = type;
    }

    public FolderEntity(String fileName, String filePath, int type, long updateTime) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.type = type;
        this.updateTime = updateTime;
    }

    public FolderEntity getParent() {
        return parent;
    }

    public void setParent(FolderEntity parent) {
        this.parent = parent;
    }

    public List<MediaEntity> getChildren() {
        return children;
    }

    public void setChildren(List<MediaEntity> children) {
        this.children = children;
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
        return 0;
    }

    @Override
    public void setFileLength(long fileLength) {

    }

    @Override
    public Bitmap getThumPath() {
        return null;
    }

    @Override
    public void setThumPath(Bitmap bitmap) {

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
}
