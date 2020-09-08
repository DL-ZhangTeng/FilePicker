package com.zhangteng.searchfilelibrary.utils;

import android.util.Log;

import com.zhangteng.searchfilelibrary.entity.AudioEntity;
import com.zhangteng.searchfilelibrary.entity.DocumentEntity;
import com.zhangteng.searchfilelibrary.entity.FolderEntity;
import com.zhangteng.searchfilelibrary.entity.ImageEntity;
import com.zhangteng.searchfilelibrary.entity.MediaEntity;
import com.zhangteng.searchfilelibrary.entity.RarEntity;
import com.zhangteng.searchfilelibrary.entity.VideoEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.zhangteng.searchfilelibrary.utils.MediaStoreUtil.MathFileByType;

/**
 * Created by swing on 2018/8/27.
 */
public class FileErgodicUtil {

    public static List<MediaEntity> getFileList(String directoryPath, String[] endNamed) {

        List<MediaEntity> list = new ArrayList<>();
        File directory = new File(directoryPath);
        FolderEntity parent = null;
        if (directory.isDirectory()) {
            parent = new FolderEntity(directory.getName(), directory.getAbsolutePath(), MediaEntity.MEDIA_FOLDER, directory.lastModified());
        } else {
            throw new NullPointerException("未找到路径对应的文件夹");
        }
        for (File file : directory.listFiles()) {
            if (file.isDirectory()) {
                FolderEntity child = new FolderEntity(file.getName(), file.getAbsolutePath(), MediaEntity.MEDIA_FOLDER, file.lastModified());
                child.setParent(parent);
//                child.setChildren(getFileList(child.getFilePath(), endNamed));
                list.add(child);
            } else {
                String path = file.getAbsolutePath();
                for (String type : endNamed) {
                    if (path.endsWith(type)) {
                        String fileName = file.getName();
                        long fileLength = file.length();
                        int fileType = MathFileByType(type);
                        long updateTime = file.lastModified();
                        if (fileLength > 10) {
                            MediaEntity entity = null;
                            switch (fileType) {
                                case MediaEntity.MEDIA_APK:
                                case MediaEntity.MEDIA_ZIP:
                                    entity = new RarEntity(fileName, path, fileLength, fileType, updateTime);
                                    break;
                                case MediaEntity.MEDIA_PDF:
                                case MediaEntity.MEDIA_DOC:
                                case MediaEntity.MEDIA_PPT:
                                case MediaEntity.MEDIA_EXCEL:
                                case MediaEntity.MEDIA_TXT:
                                case MediaEntity.MEDIA_DOCUMENT:
                                    entity = new DocumentEntity(fileName, path, fileLength, fileType, updateTime);
                                    break;
                                case MediaEntity.MEDIA_AUDIO:
                                    entity = new AudioEntity(fileName, path, fileLength, fileType, updateTime);
                                    break;
                                case MediaEntity.MEDIA_IMAGE:
                                    entity = new ImageEntity(fileName, path, fileLength, fileType, updateTime);
                                    break;
                                case MediaEntity.MEDIA_VIDEO:
                                    entity = new VideoEntity(fileName, path, fileLength, fileType, updateTime);
                                    break;
                                case MediaEntity.MEDIA_FOLDER:
                                case MediaEntity.MEDIA_UNKNOWN:
                                    break;
                                default:
                                    break;
                            }

                            Log.i("MediaStoreUtil", "====检索的文件的====" + entity.toString());
                            list.add(entity);
                        }
                        break;
                    }
                }
            }
        }
        return list;
    }
}
