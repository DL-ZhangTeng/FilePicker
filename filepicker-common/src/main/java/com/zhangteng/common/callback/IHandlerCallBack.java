package com.zhangteng.common.callback;

import com.zhangteng.searchfilelibrary.entity.MediaEntity;

import java.util.List;

/**
 * Created by swing on 2018/4/18.
 */
public interface IHandlerCallBack {

    void onStart();

    void onSuccess(List<MediaEntity> selectAudio);

    void onCancel();

    void onFinish(List<MediaEntity> selectAudio);

    void onError();

    void onPreview(List<MediaEntity> selectAudio);
}
