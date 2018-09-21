package com.zhangteng.videopicker.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.zhangteng.baselibrary.base.BaseFragment;
import com.zhangteng.baselibrary.callback.IHandlerCallBack;
import com.zhangteng.baselibrary.utils.FileUtils;
import com.zhangteng.searchfilelibrary.FileService;
import com.zhangteng.searchfilelibrary.entity.MediaEntity;
import com.zhangteng.searchfilelibrary.entity.VideoEntity;
import com.zhangteng.searchfilelibrary.utils.MediaStoreUtil;
import com.zhangteng.videopicker.R;
import com.zhangteng.videopicker.adapter.VideoPickerAdapter;
import com.zhangteng.videopicker.config.VideoPickerConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 *
 */
public class VideoPickerFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private RecyclerView mRecyclerViewVideoList;
    private TextView mTextViewSelected;
    private TextView mTextViewUpload;
    private Context mContext;
    private ArrayList<VideoEntity> videoInfos;
    private VideoPickerAdapter videoPickerAdapter;
    private int REQUEST_CODE = 100;
    private File cameraTempFile;
    private VideoPickerConfig videoPickerConfig;
    private IHandlerCallBack iHandlerCallBack;
    private List<String> selectVideo;

    public VideoPickerFragment() {

    }

    /**
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VideoPickerFragment.
     */
    public static VideoPickerFragment newInstance(String param1, String param2) {
        VideoPickerFragment fragment = new VideoPickerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    protected int getResourceId() {
        return R.layout.fragment_video_picker;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    @Override
    protected void initView(View view) {
        mRecyclerViewVideoList = (RecyclerView) view.findViewById(R.id.video_picker_rv_list);
        mRecyclerViewVideoList.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mTextViewSelected = (TextView) view.findViewById(R.id.video_picker_tv_selected);
        mTextViewUpload = (TextView) view.findViewById(R.id.video_picker_tv_upload);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    @Override
    public void initData() {
        super.initData();
        videoPickerConfig = VideoPickerConfig.getInstance();
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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        videoPickerAdapter.notifyDataSetChanged();
                    }
                });
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
                    Uri uri = Uri.fromFile(new File(FileUtils.getFilePath(mContext) + videoPickerConfig.getFilePath()));
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
