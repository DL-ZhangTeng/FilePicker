package com.zhangteng.videopicker.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhangteng.common.callback.IHandlerCallBack;
import com.zhangteng.common.config.FilePickerConfig;
import com.zhangteng.searchfilelibrary.FileService;
import com.zhangteng.searchfilelibrary.entity.MediaEntity;
import com.zhangteng.searchfilelibrary.entity.VideoEntity;
import com.zhangteng.searchfilelibrary.utils.FileUtils;
import com.zhangteng.searchfilelibrary.utils.MediaStoreUtil;
import com.zhangteng.videopicker.R;
import com.zhangteng.videopicker.adapter.VideoPickerAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * 视频选择器
 */
public class VideoPickerFragment extends Fragment {
    private RecyclerView mRecyclerViewVideoList;
    private TextView mTextViewPreview;
    private TextView mTextViewSelected;
    private TextView mTextViewUpload;
    private Context mContext;
    private ArrayList<VideoEntity> videoInfos;
    private VideoPickerAdapter videoPickerAdapter;
    private int REQUEST_CODE = 100;
    private File cameraTempFile;
    private FilePickerConfig videoPickerConfig;
    private IHandlerCallBack iHandlerCallBack;
    private List<String> selectVideo;

    public VideoPickerFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video_picker, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    protected void initView(View view) {
        mRecyclerViewVideoList = view.findViewById(R.id.video_picker_rv_list);
        mRecyclerViewVideoList.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mTextViewPreview = view.findViewById(R.id.file_picker_tv_preview);
        mTextViewSelected = view.findViewById(R.id.file_picker_tv_selected);
        mTextViewUpload = view.findViewById(R.id.file_picker_tv_upload);
        mTextViewPreview.setOnClickListener(v -> iHandlerCallBack.onPreview(selectVideo));
        mTextViewSelected.setOnClickListener(view1 -> iHandlerCallBack.onSuccess(selectVideo));
        mTextViewUpload.setOnClickListener(view12 -> {
            iHandlerCallBack.onSuccess(selectVideo);
            iHandlerCallBack.onFinish();
            if (null != getActivity()) {
                getActivity().finish();
            }
        });
    }

    public void initData() {
        videoPickerConfig = FilePickerConfig.getInstance();
        selectVideo = videoPickerConfig.getPathList();
        iHandlerCallBack = videoPickerConfig.getiHandlerCallBack();
        iHandlerCallBack.onStart();
        if (videoPickerConfig.isOpenCamera()) {
            startCamera();
        }
        mContext = getContext();
        videoInfos = new ArrayList<>();
        mTextViewSelected.setText(mContext.getString(R.string.video_picker_selected, 0));
        videoPickerAdapter = new VideoPickerAdapter(mContext, videoInfos);
        videoPickerAdapter.setOnItemClickListener(new VideoPickerAdapter.OnItemClickListener() {
            @Override
            public void onCameraClick(List<String> selectVideo) {
                startCamera();
                VideoPickerFragment.this.selectVideo = selectVideo;
            }

            @Override
            public void onVideoClick(List<String> selectVideo) {
                mTextViewSelected.setText(mContext.getString(R.string.video_picker_selected, selectVideo.size()));
                iHandlerCallBack.onSuccess(selectVideo);
                VideoPickerFragment.this.selectVideo = selectVideo;
            }
        });
        mRecyclerViewVideoList.setAdapter(videoPickerAdapter);

        getActivity().startService(new Intent(getContext(), FileService.class));
        MediaStoreUtil.setListener(new MediaStoreUtil.VideoListener() {

            @Override
            public void onVideoChange(int videoCount, List<MediaEntity> videos) {
                for (MediaEntity videoEntity : videos) {
                    videoInfos.add((VideoEntity) videoEntity);
                }
                if (getActivity() == null) {
                    return;
                }
                getActivity().runOnUiThread(() -> videoPickerAdapter.notifyDataSetChanged());
            }
        });
        FileService.getInstance().getMediaList(MediaEntity.MEDIA_VIDEO, getContext());
    }

    private void startCamera() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        cameraTempFile = FileUtils.createTmpFile(getContext(), videoPickerConfig.getFilePath());
        String provider = videoPickerConfig.getProvider();
        Uri videoUri = FileProvider.getUriForFile(mContext, provider, cameraTempFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (cameraTempFile != null) {
                    if (!videoPickerConfig.isMultiSelect()) {
                        selectVideo.clear();
                    }
                    selectVideo.add(cameraTempFile.getAbsolutePath());
                    // 通知系统扫描该文件夹
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri uri = Uri.fromFile(new File(FileUtils.getFilesDir(mContext) + videoPickerConfig.getFilePath()));
                    intent.setData(uri);
                    getActivity().sendBroadcast(intent);
                    iHandlerCallBack.onSuccess(selectVideo);
                    FileService.getInstance().getMediaList(MediaEntity.MEDIA_VIDEO, getContext());
                }
            } else {
                if (cameraTempFile != null && cameraTempFile.exists()) {
                    cameraTempFile.delete();
                }
                if (videoPickerConfig.isOpenCamera()) {
                }
            }
        }
    }
}
