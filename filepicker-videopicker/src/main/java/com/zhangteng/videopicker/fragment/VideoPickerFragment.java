package com.zhangteng.videopicker.fragment;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zhangteng.androidpermission.AndroidPermission;
import com.zhangteng.androidpermission.Permission;
import com.zhangteng.androidpermission.callback.Callback;
import com.zhangteng.common.callback.IHandlerCallBack;
import com.zhangteng.common.config.FilePickerConfig;
import com.zhangteng.searchfilelibrary.FileService;
import com.zhangteng.searchfilelibrary.entity.ImageEntity;
import com.zhangteng.searchfilelibrary.entity.MediaEntity;
import com.zhangteng.searchfilelibrary.entity.VideoEntity;
import com.zhangteng.searchfilelibrary.utils.MediaStoreUtil;
import com.zhangteng.utils.FileUtilsKt;
import com.zhangteng.videopicker.R;
import com.zhangteng.videopicker.adapter.VideoPickerAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private final int REQUEST_CODE = 100;
    private File cameraTempFile;
    private FilePickerConfig videoPickerConfig;
    private IHandlerCallBack iHandlerCallBack;
    private List<MediaEntity> selectVideo;

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
            iHandlerCallBack.onFinish(selectVideo);
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
            public void onCameraClick(List<MediaEntity> selectVideo) {
                AndroidPermission androidPermission = new AndroidPermission.Buidler()
                        .with(VideoPickerFragment.this)
                        .permission(Permission.CAMERA,
                                Permission.RECORD_AUDIO)
                        .callback(new Callback() {
                            @Override
                            public void success(Activity permissionActivity) {
                                startCamera();
                            }

                            @Override
                            public void failure(Activity permissionActivity) {
                                Toast.makeText(mContext, "请开启相机与录音权限！", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void nonExecution(Activity permissionActivity) {
                                //权限已通过，请求未执行
                                startCamera();
                            }
                        })
                        .build();
                androidPermission.execute();
                VideoPickerFragment.this.selectVideo = selectVideo;
            }

            @Override
            public void onVideoClick(List<MediaEntity> selectVideo) {
                mTextViewSelected.setText(mContext.getString(R.string.video_picker_selected, selectVideo.size()));
                iHandlerCallBack.onSuccess(selectVideo);
                VideoPickerFragment.this.selectVideo = selectVideo;
            }
        });
        mRecyclerViewVideoList.setAdapter(videoPickerAdapter);

        AndroidPermission androidPermission = new AndroidPermission.Buidler()
                .with(this)
                .permission(Permission.READ_EXTERNAL_STORAGE,
                        Permission.WRITE_EXTERNAL_STORAGE)
                .callback(new Callback() {
                    @Override
                    public void success(Activity permissionActivity) {
                        searchFile();
                    }

                    @Override
                    public void failure(Activity permissionActivity) {
                        Toast.makeText(mContext, "请开启文件读写权限！", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void nonExecution(Activity permissionActivity) {
                        //权限已通过，请求未执行
                        searchFile();
                    }
                })
                .build();
        androidPermission.execute();
    }

    private void searchFile() {
        MediaStoreUtil.setListener(MediaEntity.MEDIA_VIDEO,new MediaStoreUtil.VideoListener() {

            @SuppressLint("NotifyDataSetChanged")
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
        cameraTempFile = FileUtilsKt.createImageFile(getContext(), Objects.requireNonNull(FileUtilsKt.getFilesDir(getContext())), videoPickerConfig.getFilePath());
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
                    MediaEntity mediaEntity = new ImageEntity(cameraTempFile.getName(), cameraTempFile.getAbsolutePath(), cameraTempFile.length(), MediaEntity.MEDIA_VIDEO, cameraTempFile.lastModified());
                    selectVideo.add(mediaEntity);
                    // 通知系统扫描该文件夹
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri uri = Uri.fromFile(new File(FileUtilsKt.getFilesDir(mContext) + videoPickerConfig.getFilePath()));
                    intent.setData(uri);
                    requireActivity().sendBroadcast(intent);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MediaStoreUtil.removeListener(MediaEntity.MEDIA_VIDEO);
    }
}
