package com.zhangteng.searchfilelibrary.utils;

import androidx.annotation.NonNull;

import com.zhangteng.searchfilelibrary.entity.MediaEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * Created by swing on 2018/8/27.
 */
public class MediaStoreUtil {
    private static int apk_count = 0;
    private static int audio_count = 0;
    private static int document_count = 0;
    private static int image_count = 0;
    private static int rar_count = 0;
    private static int video_count = 0;
    private static int folder_count = 0;

    private static final List<MediaEntity> apk = new ArrayList<>();
    private static final List<MediaEntity> audio = new ArrayList<>();
    private static final List<MediaEntity> document = new ArrayList<>();
    private static final List<MediaEntity> image = new ArrayList<>();
    private static final List<MediaEntity> rar = new ArrayList<>();
    private static final List<MediaEntity> video = new ArrayList<>();
    private static final List<MediaEntity> folder = new ArrayList<>();

    private static final List<MediaStoreListener> listeners = new CopyOnWriteArrayList<>();

    public static void addApk(@NonNull List<MediaEntity> list) {
        MediaStoreUtil.apk.addAll(list);
        MediaStoreUtil.apk_count += list.size();
        for (MediaStoreListener listener : listeners) {
            listener.onApkChange(apk_count, apk);
        }
    }

    public static void addAudio(@NonNull List<MediaEntity> list) {
        MediaStoreUtil.audio.addAll(list);
        MediaStoreUtil.audio_count += list.size();
        for (MediaStoreListener listener : listeners) {
            listener.onAudioChange(audio_count, audio);
        }
    }

    public static void addDocument(@NonNull List<MediaEntity> list) {
        MediaStoreUtil.document.addAll(list);
        MediaStoreUtil.document_count += list.size();
        for (MediaStoreListener listener : listeners) {
            listener.onDocumentChange(document_count, document);
        }
    }

    public static void addImage(@NonNull List<MediaEntity> list) {
        MediaStoreUtil.image.addAll(list);
        MediaStoreUtil.image_count += list.size();
        for (MediaStoreListener listener : listeners) {
            listener.onImageChange(image_count, image);
        }
    }

    public static void addRar(@NonNull List<MediaEntity> list) {
        MediaStoreUtil.rar.addAll(list);
        MediaStoreUtil.rar_count += list.size();
        for (MediaStoreListener listener : listeners) {
            listener.onRarChange(rar_count, rar);
        }
    }

    public static void addVideo(@NonNull List<MediaEntity> list) {
        MediaStoreUtil.video.addAll(list);
        MediaStoreUtil.video_count += list.size();
        for (MediaStoreListener listener : listeners) {
            listener.onVideoChange(video_count, video);
        }
    }

    public static void addFolder(@NonNull List<MediaEntity> list) {
        MediaStoreUtil.folder.addAll(list);
        MediaStoreUtil.folder_count += list.size();
        for (MediaStoreListener listener : listeners) {
            listener.onFolderChange(folder_count, folder);
        }
    }

    public static void clearApk() {
        MediaStoreUtil.apk.clear();
        MediaStoreUtil.apk_count = 0;
        for (MediaStoreListener listener : listeners) {
            listener.onApkChange(apk_count, apk);
        }
    }

    public static void clearAudio() {
        MediaStoreUtil.audio.clear();
        MediaStoreUtil.audio_count = 0;
        for (MediaStoreListener listener : listeners) {
            listener.onAudioChange(audio_count, audio);
        }
    }

    public static void clearDocument() {
        MediaStoreUtil.document.clear();
        MediaStoreUtil.document_count = 0;
        for (MediaStoreListener listener : listeners) {
            listener.onDocumentChange(document_count, document);
        }
    }

    public static void clearImage() {
        MediaStoreUtil.image.clear();
        MediaStoreUtil.image_count = 0;
        for (MediaStoreListener listener : listeners) {
            listener.onImageChange(image_count, image);
        }
    }

    public static void clearRar() {
        MediaStoreUtil.rar.clear();
        MediaStoreUtil.rar_count = 0;
        for (MediaStoreListener listener : listeners) {
            listener.onRarChange(rar_count, rar);
        }
    }

    public static void clearVideo() {
        MediaStoreUtil.video.clear();
        MediaStoreUtil.video_count = 0;
        for (MediaStoreListener listener : listeners) {
            listener.onVideoChange(video_count, video);
        }
    }

    public static void clearFolder() {
        MediaStoreUtil.folder.clear();
        MediaStoreUtil.folder_count = 0;
        for (MediaStoreListener listener : listeners) {
            listener.onFolderChange(folder_count, folder);
        }
    }

    public static int getApkCount() {
        return apk_count;
    }

    public static int getAudioCount() {
        return audio_count;
    }

    public static int getDocumentCount() {
        return document_count;
    }

    public static int getImageCount() {
        return image_count;
    }

    public static int getRarCount() {
        return rar_count;
    }

    public static int getVideoCount() {
        return video_count;
    }

    public static int getFolderCount() {
        return folder_count;
    }

    public static List<MediaEntity> getApk() {
        return apk;
    }

    public static List<MediaEntity> getAudio() {
        return audio;
    }

    public static List<MediaEntity> getDocument() {
        return document;
    }

    public static List<MediaEntity> getImage() {
        return image;
    }

    public static List<MediaEntity> getRar() {
        return rar;
    }

    public static List<MediaEntity> getVideo() {
        return video;
    }

    public static List<MediaEntity> getFolder() {
        return folder;
    }

    public static void setListener(MediaStoreListener listener) {
        MediaStoreUtil.listeners.add(listener);
    }

    public static void removeListener(MediaStoreListener listener) {
        for (MediaStoreListener mediaStoreListener : listeners) {
            if (mediaStoreListener == listener) {
                listeners.remove(listener);
            }
        }
    }

    public static void removeListener(int index) {
        listeners.remove(index);
    }

    /**
     * 根据文件名判断文件是那种文档类型
     * 只判断文档文件与压缩文件
     *
     * @param fileName 文件名
     */
    public static int MathFile(String fileName) {
        if (fileName.endsWith("apk")) {
            return MediaEntity.MEDIA_APK;
        } else if (fileName.endsWith("txt")) {
            return MediaEntity.MEDIA_TXT;
        } else if (fileName.endsWith("pdf")) {
            return MediaEntity.MEDIA_PDF;
        } else if (fileName.endsWith("doc") || fileName.endsWith("docx")) {
            return MediaEntity.MEDIA_DOC;
        } else if (fileName.endsWith("xls") || fileName.endsWith("xlsx")) {
            return MediaEntity.MEDIA_EXCEL;
        } else if (fileName.endsWith("ppt") || fileName.endsWith("pptx")) {
            return MediaEntity.MEDIA_PPT;
        } else if (fileName.endsWith("zip") || fileName.endsWith("rar")) {
            return MediaEntity.MEDIA_ZIP;
        } else {
            return MediaEntity.MEDIA_DOCUMENT;
        }
    }

    /**
     * 根据文件后缀判断文件类型
     * 所有文件包含媒体文件
     *
     * @param type 文件类型
     */
    public static int MathFileByType(String type) {
        if (type.equals("apk")) {
            return MediaEntity.MEDIA_APK;
        } else if (type.equals("txt")) {
            return MediaEntity.MEDIA_TXT;
        } else if (type.equals("pdf")) {
            return MediaEntity.MEDIA_PDF;
        } else if (type.equals("doc") || type.equals("docx")) {
            return MediaEntity.MEDIA_DOC;
        } else if (type.equals("xls") || type.equals("xlsx")) {
            return MediaEntity.MEDIA_EXCEL;
        } else if (type.equals("ppt") || type.equals("pptx")) {
            return MediaEntity.MEDIA_PPT;
        } else if (type.equals("zip") || type.equals("rar")) {
            return MediaEntity.MEDIA_ZIP;
        } else if (type.equals("jpg") || type.equals("png")
                || type.equals("jpeg") || type.equals("gif")) {
            return MediaEntity.MEDIA_IMAGE;
        } else if (type.equals("mp3") || type.equals("wave")
                || type.equals("wma") || type.equals("mpeg")) {
            return MediaEntity.MEDIA_AUDIO;
        } else if (type.equals("mp4") || type.equals("wmv")
                || type.equals("m3u8") || type.equals("avi")
                || type.equals("flv") || type.equals("3gp")) {
            return MediaEntity.MEDIA_VIDEO;
        } else {
            return MediaEntity.MEDIA_UNKNOWN;
        }
    }

    public interface MediaStoreListener {
        void onApkChange(int apkCount, List<MediaEntity> apks);

        void onAudioChange(int audioCount, List<MediaEntity> audios);

        void onDocumentChange(int documentCount, List<MediaEntity> documents);

        void onImageChange(int imageCount, List<MediaEntity> images);

        void onRarChange(int rarCount, List<MediaEntity> rars);

        void onVideoChange(int videoCount, List<MediaEntity> videos);

        void onFolderChange(int folderCount, List<MediaEntity> folders);
    }

    public abstract static class ApkListener implements MediaStoreListener {

        @Override
        public void onAudioChange(int audioCount, List<MediaEntity> audios) {

        }

        @Override
        public void onDocumentChange(int documentCount, List<MediaEntity> documents) {

        }

        @Override
        public void onImageChange(int imageCount, List<MediaEntity> images) {

        }

        @Override
        public void onRarChange(int rarCount, List<MediaEntity> rars) {

        }

        @Override
        public void onVideoChange(int videoCount, List<MediaEntity> videos) {

        }

        @Override
        public void onFolderChange(int folderCount, List<MediaEntity> folders) {

        }
    }

    public abstract static class AudioListener implements MediaStoreListener {

        @Override
        public void onApkChange(int apkCount, List<MediaEntity> apks) {

        }

        @Override
        public void onDocumentChange(int documentCount, List<MediaEntity> documents) {

        }

        @Override
        public void onImageChange(int imageCount, List<MediaEntity> images) {

        }

        @Override
        public void onRarChange(int rarCount, List<MediaEntity> rars) {

        }

        @Override
        public void onVideoChange(int videoCount, List<MediaEntity> videos) {

        }

        @Override
        public void onFolderChange(int folderCount, List<MediaEntity> folders) {

        }
    }

    public abstract static class DocumentListener implements MediaStoreListener {

        @Override
        public void onApkChange(int apkCount, List<MediaEntity> apks) {

        }

        @Override
        public void onAudioChange(int audioCount, List<MediaEntity> audios) {

        }

        @Override
        public void onImageChange(int imageCount, List<MediaEntity> images) {

        }

        @Override
        public void onRarChange(int rarCount, List<MediaEntity> rars) {

        }

        @Override
        public void onVideoChange(int videoCount, List<MediaEntity> videos) {

        }

        @Override
        public void onFolderChange(int folderCount, List<MediaEntity> folders) {

        }
    }

    public abstract static class ImageListener implements MediaStoreListener {

        @Override
        public void onApkChange(int apkCount, List<MediaEntity> apks) {

        }

        @Override
        public void onAudioChange(int audioCount, List<MediaEntity> audios) {

        }

        @Override
        public void onDocumentChange(int documentCount, List<MediaEntity> documents) {

        }

        @Override
        public void onRarChange(int rarCount, List<MediaEntity> rars) {

        }

        @Override
        public void onVideoChange(int videoCount, List<MediaEntity> videos) {

        }

        @Override
        public void onFolderChange(int folderCount, List<MediaEntity> folders) {

        }
    }

    public abstract static class RarListener implements MediaStoreListener {

        @Override
        public void onApkChange(int apkCount, List<MediaEntity> apks) {

        }

        @Override
        public void onAudioChange(int audioCount, List<MediaEntity> audios) {

        }

        @Override
        public void onDocumentChange(int documentCount, List<MediaEntity> documents) {

        }

        @Override
        public void onImageChange(int imageCount, List<MediaEntity> images) {

        }

        @Override
        public void onVideoChange(int videoCount, List<MediaEntity> videos) {

        }

        @Override
        public void onFolderChange(int folderCount, List<MediaEntity> folders) {

        }
    }

    public abstract static class VideoListener implements MediaStoreListener {

        @Override
        public void onApkChange(int apkCount, List<MediaEntity> apks) {

        }

        @Override
        public void onAudioChange(int audioCount, List<MediaEntity> audios) {

        }

        @Override
        public void onDocumentChange(int documentCount, List<MediaEntity> documents) {

        }

        @Override
        public void onImageChange(int imageCount, List<MediaEntity> images) {

        }

        @Override
        public void onRarChange(int rarCount, List<MediaEntity> rars) {

        }

        @Override
        public void onFolderChange(int folderCount, List<MediaEntity> folders) {

        }
    }

    public abstract static class FolderListener implements MediaStoreListener {
        @Override
        public void onApkChange(int apkCount, List<MediaEntity> apks) {

        }

        @Override
        public void onAudioChange(int audioCount, List<MediaEntity> audios) {

        }

        @Override
        public void onDocumentChange(int documentCount, List<MediaEntity> documents) {

        }

        @Override
        public void onImageChange(int imageCount, List<MediaEntity> images) {

        }

        @Override
        public void onRarChange(int rarCount, List<MediaEntity> rars) {

        }

        @Override
        public void onVideoChange(int videoCount, List<MediaEntity> videos) {

        }
    }
}
