package com.zhangteng.common.config;

import android.util.SparseIntArray;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import com.zhangteng.common.R;
import com.zhangteng.common.callback.HandlerCallBack;
import com.zhangteng.common.callback.IHandlerCallBack;
import com.zhangteng.common.imageloader.GlideImageLoader;
import com.zhangteng.common.imageloader.ImageLoader;
import com.zhangteng.searchfilelibrary.entity.MediaEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by swing on 2018/4/18.
 */
public class FilePickerConfig {
    private static FilePickerConfig filePickerConfig;
    private ImageLoader imageLoader;    // 图片加载器
    private IHandlerCallBack iHandlerCallBack;
    private boolean multiSelect;        // 是否开启多选  默认 ： false
    private int maxSize;                // 配置开启多选时 最大可选择的图片数量。   默认：9
    private boolean isShowCamera;       // 是否开启相机 默认：true
    private String provider;            // 兼容android 7.0 设置
    private String filePath;            // 拍照以及截图后 存放的位置。    默认：/imagePicker/Pictures
    private List<MediaEntity> pathList;      // 已选择照片的集合
    private boolean isOpenCamera;             // 是否直接开启相机    默认：false
    private SparseIntArray iconResources; //icon资源

    public static FilePickerConfig getInstance() {
        if (filePickerConfig == null) {
            synchronized (FilePickerConfig.class) {
                if (filePickerConfig == null) {
                    filePickerConfig = new FilePickerConfig()
                            .imageLoader(new GlideImageLoader().placeholder(R.mipmap.repository_picture_ico))
                            .iHandlerCallBack(new HandlerCallBack())
                            .multiSelect(true)
                            .maxSize(9)
                            .isShowCamera(true)
                            .filePath("/filePicker/FilePicker")
                            .provider("com.zhangteng.searchfilelibrary.fileprovider")
                            .pathList(new ArrayList<>())
                            .isOpenCamera(false)
                            .iconResources(new SparseIntArray());
                }
                return filePickerConfig;
            }
        } else {
            return filePickerConfig;
        }
    }

    public IHandlerCallBack getiHandlerCallBack() {
        return iHandlerCallBack;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    public boolean isMultiSelect() {
        return multiSelect;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public boolean isShowCamera() {
        return isShowCamera;
    }

    public String getProvider() {
        return provider;
    }

    public String getFilePath() {
        return filePath;
    }

    public List<MediaEntity> getPathList() {
        return pathList;
    }

    public boolean isOpenCamera() {
        return isOpenCamera;
    }

    public SparseIntArray getIconResources() {
        return iconResources;
    }

    public int getIconResources(int mediaEntityType) {
        if (mediaEntityType == MediaEntity.MEDIA_IMAGE) {
            return iconResources.get(mediaEntityType, R.mipmap.repository_picture_ico);
        } else if (mediaEntityType == MediaEntity.MEDIA_AUDIO) {
            return iconResources.get(mediaEntityType, R.mipmap.repository_audio_ico);
        } else if (mediaEntityType == MediaEntity.MEDIA_VIDEO) {
            return iconResources.get(mediaEntityType, R.mipmap.repository_video_ico);
        } else if (mediaEntityType == MediaEntity.MEDIA_DOCUMENT) {
            return iconResources.get(mediaEntityType, R.mipmap.repository_manuscripts_ico);
        } else if (mediaEntityType == MediaEntity.MEDIA_ZIP) {
            return iconResources.get(mediaEntityType, R.mipmap.repository_zipfile_ico);
        } else if (mediaEntityType == MediaEntity.MEDIA_PDF) {
            return iconResources.get(mediaEntityType, R.mipmap.repository_manuscripts_ico);
        } else if (mediaEntityType == MediaEntity.MEDIA_DOC) {
            return iconResources.get(mediaEntityType, R.mipmap.repository_manuscripts_ico);
        } else if (mediaEntityType == MediaEntity.MEDIA_PPT) {
            return iconResources.get(mediaEntityType, R.mipmap.repository_manuscripts_ico);
        } else if (mediaEntityType == MediaEntity.MEDIA_EXCEL) {
            return iconResources.get(mediaEntityType, R.mipmap.repository_manuscripts_ico);
        } else if (mediaEntityType == MediaEntity.MEDIA_TXT) {
            return iconResources.get(mediaEntityType, R.mipmap.repository_manuscripts_ico);
        } else if (mediaEntityType == MediaEntity.MEDIA_APK) {
            return iconResources.get(mediaEntityType, R.mipmap.repository_zipfile_ico);
        } else if (mediaEntityType == MediaEntity.MEDIA_FOLDER) {
            return iconResources.get(mediaEntityType, R.mipmap.repository_folder_ico);
        } else if (mediaEntityType == MediaEntity.MEDIA_UNKNOWN) {
            return iconResources.get(mediaEntityType, R.mipmap.repository_unknown_ico);
        }
        return iconResources.get(mediaEntityType, R.mipmap.repository_unknown_ico);
    }

    public int getIconResources(@NonNull MediaEntity mediaEntityType) {
        if (mediaEntityType.getMediaType() == MediaEntity.MEDIA_IMAGE) {
            return iconResources.get(mediaEntityType.getMediaType(), R.mipmap.repository_picture_ico);
        } else if (mediaEntityType.getMediaType() == MediaEntity.MEDIA_AUDIO) {
            return iconResources.get(mediaEntityType.getMediaType(), R.mipmap.repository_audio_ico);
        } else if (mediaEntityType.getMediaType() == MediaEntity.MEDIA_VIDEO) {
            return iconResources.get(mediaEntityType.getMediaType(), R.mipmap.repository_video_ico);
        } else if (mediaEntityType.getMediaType() == MediaEntity.MEDIA_DOCUMENT) {
            return iconResources.get(mediaEntityType.getMediaType(), R.mipmap.repository_manuscripts_ico);
        } else if (mediaEntityType.getMediaType() == MediaEntity.MEDIA_ZIP) {
            return iconResources.get(mediaEntityType.getMediaType(), R.mipmap.repository_zipfile_ico);
        } else if (mediaEntityType.getMediaType() == MediaEntity.MEDIA_PDF) {
            return iconResources.get(mediaEntityType.getMediaType(), R.mipmap.repository_manuscripts_ico);
        } else if (mediaEntityType.getMediaType() == MediaEntity.MEDIA_DOC) {
            return iconResources.get(mediaEntityType.getMediaType(), R.mipmap.repository_manuscripts_ico);
        } else if (mediaEntityType.getMediaType() == MediaEntity.MEDIA_PPT) {
            return iconResources.get(mediaEntityType.getMediaType(), R.mipmap.repository_manuscripts_ico);
        } else if (mediaEntityType.getMediaType() == MediaEntity.MEDIA_EXCEL) {
            return iconResources.get(mediaEntityType.getMediaType(), R.mipmap.repository_manuscripts_ico);
        } else if (mediaEntityType.getMediaType() == MediaEntity.MEDIA_TXT) {
            return iconResources.get(mediaEntityType.getMediaType(), R.mipmap.repository_manuscripts_ico);
        } else if (mediaEntityType.getMediaType() == MediaEntity.MEDIA_APK) {
            return iconResources.get(mediaEntityType.getMediaType(), R.mipmap.repository_zipfile_ico);
        } else if (mediaEntityType.getMediaType() == MediaEntity.MEDIA_FOLDER) {
            return iconResources.get(mediaEntityType.getMediaType(), R.mipmap.repository_folder_ico);
        } else if (mediaEntityType.getMediaType() == MediaEntity.MEDIA_UNKNOWN) {
            return iconResources.get(mediaEntityType.getMediaType(), R.mipmap.repository_unknown_ico);
        }
        return iconResources.get(mediaEntityType.getMediaType(), R.mipmap.repository_unknown_ico);
    }

    public FilePickerConfig provider(String provider) {
        this.provider = provider;
        return this;
    }

    public FilePickerConfig iHandlerCallBack(IHandlerCallBack iHandlerCallBack) {
        this.iHandlerCallBack = iHandlerCallBack;
        return this;
    }

    public FilePickerConfig imageLoader(ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
        return this;
    }


    public FilePickerConfig multiSelect(boolean multiSelect) {
        this.multiSelect = multiSelect;
        return this;
    }

    public FilePickerConfig multiSelect(boolean multiSelect, int maxSize) {
        this.multiSelect = multiSelect;
        this.maxSize = maxSize;
        return this;
    }

    public FilePickerConfig maxSize(int maxSize) {
        this.maxSize = maxSize;
        return this;
    }

    public FilePickerConfig isShowCamera(boolean isShowCamera) {
        this.isShowCamera = isShowCamera;
        return this;
    }

    public FilePickerConfig filePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public FilePickerConfig isOpenCamera(boolean isOpenCamera) {
        this.isOpenCamera = isOpenCamera;
        return this;
    }

    public FilePickerConfig pathList(List<MediaEntity> pathList) {
        this.pathList = pathList;
        return this;
    }

    public FilePickerConfig iconResources(@NonNull MediaEntity mediaEntityType, @DrawableRes int resourcesId) {
        this.iconResources.put(mediaEntityType.getMediaType(), resourcesId);
        return this;
    }

    public FilePickerConfig iconResources(int mediaEntityType, @DrawableRes int resourcesId) {
        this.iconResources.put(mediaEntityType, resourcesId);
        return this;
    }

    public FilePickerConfig iconResources(SparseIntArray iconResources) {
        this.iconResources = iconResources;
        return this;
    }
}
