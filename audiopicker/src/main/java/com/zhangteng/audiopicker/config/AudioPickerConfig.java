package com.zhangteng.audiopicker.config;

import com.zhangteng.audiopicker.R;
import com.zhangteng.common.callback.HandlerCallBack;
import com.zhangteng.common.callback.IHandlerCallBack;
import com.zhangteng.common.imageloader.GlideImageLoader;
import com.zhangteng.common.imageloader.ImageLoader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by swing on 2018/4/18.
 */
public class AudioPickerConfig {
    private static AudioPickerConfig audioPickerConfig;
    private ImageLoader imageLoader;    // 图片加载器
    private IHandlerCallBack iHandlerCallBack;
    private boolean multiSelect;        // 是否开启多选  默认 ： false
    private int maxSize;                // 配置开启多选时 最大可选择的图片数量。   默认：9
    private boolean isShowRecord;       // 是否开启录制 默认：true
    private String provider;            // 兼容android 7.0 设置
    private String filePath;            // 录制后 存放的位置。    默认：/audioPicker/Pictures
    private ArrayList<String> pathList;      // 已选择音频的路径
    private boolean isOpenRecord;             // 是否直接开启录制    默认：false
    private Builder builder;

    public AudioPickerConfig(Builder builder) {
        setBuilder(builder);
    }

    public static AudioPickerConfig getInstance() {
        if (audioPickerConfig == null) {
            synchronized (AudioPickerConfig.class) {
                if (audioPickerConfig == null) {
                    audioPickerConfig = new AudioPickerConfig(new Builder());
                }
                return audioPickerConfig;
            }
        } else {
            return audioPickerConfig;
        }
    }

    private void setBuilder(Builder builder) {
        this.imageLoader = builder.imageLoader;
        this.multiSelect = builder.multiSelect;
        this.maxSize = builder.maxSize;
        this.isShowRecord = builder.isShowRecord;
        this.pathList = builder.pathList;
        this.filePath = builder.filePath;
        this.isOpenRecord = builder.isOpenRecord;
        this.provider = builder.provider;
        this.iHandlerCallBack = builder.iHandlerCallBack;
        this.builder = builder;
        AudioPickerConfig.audioPickerConfig = this;
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

    public boolean isShowRecord() {
        return isShowRecord;
    }

    public String getProvider() {
        return provider;
    }

    public String getFilePath() {
        return filePath;
    }

    public ArrayList<String> getPathList() {
        return pathList;
    }

    public boolean isOpenRecord() {
        return isOpenRecord;
    }

    public void setOpenRecord(boolean openRecord) {
        isOpenRecord = openRecord;
    }

    public static class Builder implements Serializable {

        private static AudioPickerConfig audioPickerConfig;

        private ImageLoader imageLoader = new GlideImageLoader().placeholder(R.mipmap.repository_audio_ico);
        private IHandlerCallBack iHandlerCallBack = new HandlerCallBack();

        private boolean multiSelect = true;
        private int maxSize = 9;
        private boolean isShowRecord = false;
        private String filePath = "/audioPicker/AudioPickerPictures";

        private String provider = "com.zhangteng.base.fileprovider";

        private ArrayList<String> pathList = new ArrayList<>();

        private boolean isOpenRecord = false;

        public Builder provider(String provider) {
            this.provider = provider;
            return this;
        }

        public Builder iHandlerCallBack(IHandlerCallBack iHandlerCallBack) {
            this.iHandlerCallBack = iHandlerCallBack;
            return this;
        }

        public Builder imageLoader(ImageLoader imageLoader) {
            this.imageLoader = imageLoader;
            return this;
        }


        public Builder multiSelect(boolean multiSelect) {
            this.multiSelect = multiSelect;
            return this;
        }

        public Builder multiSelect(boolean multiSelect, int maxSize) {
            this.multiSelect = multiSelect;
            this.maxSize = maxSize;
            return this;
        }

        public Builder maxSize(int maxSize) {
            this.maxSize = maxSize;
            return this;
        }

        public Builder isShowRecord(boolean isShowRecord) {
            this.isShowRecord = isShowRecord;
            return this;
        }

        public Builder filePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public Builder isOpenRecord(boolean isOpenRecord) {
            this.isOpenRecord = isOpenRecord;
            return this;
        }


        public Builder pathList(List<String> pathList) {
            this.pathList.clear();
            this.pathList.addAll(pathList);
            return this;
        }

        public AudioPickerConfig build() {
            if (audioPickerConfig == null) {
                audioPickerConfig = new AudioPickerConfig(this);
            } else {
                audioPickerConfig.setBuilder(this);
            }
            return audioPickerConfig;
        }

    }
}
