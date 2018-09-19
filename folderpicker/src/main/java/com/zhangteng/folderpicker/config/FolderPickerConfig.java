package com.zhangteng.folderpicker.config;

import com.zhangteng.baselibrary.callback.HandlerCallBack;
import com.zhangteng.baselibrary.callback.IHandlerCallBack;
import com.zhangteng.baselibrary.imageloader.GlideImageLoader;
import com.zhangteng.baselibrary.imageloader.ImageLoader;
import com.zhangteng.folderpicker.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by swing on 2018/4/18.
 */
public class FolderPickerConfig {
    private static FolderPickerConfig folderPickerConfig;
    private ImageLoader imageLoader;    // 图片加载器
    private IHandlerCallBack iHandlerCallBack;
    private boolean multiSelect;        // 是否开启多选  默认 ： false
    private int maxSize;                // 配置开启多选时 最大可选择的图片数量。   默认：9
    private String provider;            // 兼容android 7.0 设置
    private String filePath;            // 录制后 存放的位置。    默认：/FolderPicker/Pictures
    private ArrayList<String> pathList;      // 已选择音频的路径
    private Builder builder;

    public FolderPickerConfig(Builder builder) {
        setBuilder(builder);
    }

    public static FolderPickerConfig getInstance() {
        if (folderPickerConfig == null) {
            synchronized (FolderPickerConfig.class) {
                if (folderPickerConfig == null) {
                    folderPickerConfig = new FolderPickerConfig(new Builder());
                }
                return folderPickerConfig;
            }
        } else {
            return folderPickerConfig;
        }
    }

    private void setBuilder(Builder builder) {
        this.imageLoader = builder.imageLoader;
        this.multiSelect = builder.multiSelect;
        this.maxSize = builder.maxSize;
        this.pathList = builder.pathList;
        this.filePath = builder.filePath;
        this.provider = builder.provider;
        this.iHandlerCallBack = builder.iHandlerCallBack;
        this.builder = builder;
        FolderPickerConfig.folderPickerConfig = this;
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

    public String getProvider() {
        return provider;
    }

    public String getFilePath() {
        return filePath;
    }

    public ArrayList<String> getPathList() {
        return pathList;
    }

    public static class Builder implements Serializable {

        private static FolderPickerConfig folderPickerConfig;

        private ImageLoader imageLoader = new GlideImageLoader().placeholder(R.mipmap.repository_folder_ico);
        private IHandlerCallBack iHandlerCallBack = new HandlerCallBack();

        private boolean multiSelect = true;
        private int maxSize = 9;
        private String filePath = "/FolderPicker/FolderPickerPictures";

        private String provider = "com.zhangteng.baselibrary.fileprovider";

        private ArrayList<String> pathList = new ArrayList<>();

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

        public Builder filePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public Builder pathList(List<String> pathList) {
            this.pathList.clear();
            this.pathList.addAll(pathList);
            return this;
        }

        public FolderPickerConfig build() {
            if (folderPickerConfig == null) {
                folderPickerConfig = new FolderPickerConfig(this);
            } else {
                folderPickerConfig.setBuilder(this);
            }
            return folderPickerConfig;
        }

    }
}
