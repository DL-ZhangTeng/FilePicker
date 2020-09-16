package com.zhangteng.audiopicker.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zhangteng.androidpermission.AndroidPermission;
import com.zhangteng.androidpermission.Permission;
import com.zhangteng.androidpermission.callback.Callback;
import com.zhangteng.audiopicker.R;
import com.zhangteng.audiopicker.adapter.AudioPickerAdapter;
import com.zhangteng.common.callback.IHandlerCallBack;
import com.zhangteng.common.config.FilePickerConfig;
import com.zhangteng.searchfilelibrary.FileService;
import com.zhangteng.searchfilelibrary.entity.AudioEntity;
import com.zhangteng.searchfilelibrary.entity.MediaEntity;
import com.zhangteng.searchfilelibrary.utils.FileUtils;
import com.zhangteng.searchfilelibrary.utils.MediaStoreUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * 音频选择器
 */
public class AudioPickerFragment extends Fragment {
    private RecyclerView mRecyclerViewImageList;
    private TextView mTextViewPreview;
    private TextView mTextViewSelected;
    private TextView mTextViewUpload;
    private Context mContext;
    private ArrayList<AudioEntity> imageInfos;
    private AudioPickerAdapter audioPickerAdapter;
    private int REQUEST_CODE = 100;
    private File recordTempFile;
    private FilePickerConfig audioPickerConfig;
    private IHandlerCallBack iHandlerCallBack;
    private List<String> selectAudio;

    public AudioPickerFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_audio_picker, container, false);
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
        mRecyclerViewImageList = view.findViewById(R.id.audio_picker_rv_list);
        mRecyclerViewImageList.setLayoutManager(new LinearLayoutManager(getContext()));
        mTextViewPreview = view.findViewById(R.id.file_picker_tv_preview);
        mTextViewSelected = view.findViewById(R.id.file_picker_tv_selected);
        mTextViewUpload = view.findViewById(R.id.file_picker_tv_upload);
        mTextViewPreview.setOnClickListener(v -> iHandlerCallBack.onPreview(selectAudio));
        mTextViewSelected.setOnClickListener(view1 -> iHandlerCallBack.onSuccess(selectAudio));
        mTextViewUpload.setOnClickListener(view12 -> {
            iHandlerCallBack.onSuccess(selectAudio);
            iHandlerCallBack.onFinish();
            if (null != getActivity()) {
                getActivity().finish();
            }
        });
    }

    public void initData() {
        audioPickerConfig = new FilePickerConfig(new FilePickerConfig.Builder());
        selectAudio = audioPickerConfig.getPathList();
        iHandlerCallBack = audioPickerConfig.getiHandlerCallBack();
        iHandlerCallBack.onStart();
        if (audioPickerConfig.isOpenCamera()) {
            startRecord();
        }
        mContext = getContext();
        imageInfos = new ArrayList<>();
        mTextViewSelected.setText(mContext.getString(R.string.audio_picker_selected, 0));
        audioPickerAdapter = new AudioPickerAdapter(mContext, imageInfos);
        audioPickerAdapter.setOnItemClickListener(new AudioPickerAdapter.OnItemClickListener() {
            @Override
            public void onRecordClick(List<String> selectImage) {
                AndroidPermission androidPermission = new AndroidPermission.Buidler()
                        .with(AudioPickerFragment.this)
                        .permission(Permission.RECORD_AUDIO)
                        .callback(new Callback() {
                            @Override
                            public void success() {
                                startRecord();
                            }

                            @Override
                            public void failure() {
                                Toast.makeText(mContext, "请开启录音权限！", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void nonExecution() {
                                //权限已通过，请求未执行
                                startRecord();
                            }
                        })
                        .build();
                androidPermission.excute();

                AudioPickerFragment.this.selectAudio = selectImage;
            }

            @Override
            public void onImageClick(List<String> selectImage) {
                mTextViewSelected.setText(mContext.getString(R.string.audio_picker_selected, selectImage.size()));
                iHandlerCallBack.onSuccess(selectImage);
                AudioPickerFragment.this.selectAudio = selectImage;
            }
        });
        mRecyclerViewImageList.setAdapter(audioPickerAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            AndroidPermission androidPermission = new AndroidPermission.Buidler()
                    .with(this)
                    .permission(Permission.READ_EXTERNAL_STORAGE,
                            Permission.WRITE_EXTERNAL_STORAGE)
                    .callback(new Callback() {
                        @Override
                        public void success() {
                            searchFile();
                        }

                        @Override
                        public void failure() {
                            Toast.makeText(mContext, "请开启文件读写权限！", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void nonExecution() {
                            //权限已通过，请求未执行
                            searchFile();
                        }
                    })
                    .build();
            androidPermission.excute();
        }
    }

    private void searchFile() {
        getActivity().startService(new Intent(getActivity(), FileService.class));
        FileService.getInstance().getMediaList(MediaEntity.MEDIA_AUDIO, getActivity());
        MediaStoreUtil.setListener(new MediaStoreUtil.AudioListener() {

            @Override
            public void onAudioChange(int imageCount, List<MediaEntity> audios) {
                for (MediaEntity audioEntity : audios) {
                    imageInfos.add((AudioEntity) audioEntity);
                }
                if (getActivity() == null) {
                    return;
                }
                getActivity().runOnUiThread(() -> audioPickerAdapter.notifyDataSetChanged());
            }
        });
    }

    private void startRecord() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        recordTempFile = FileUtils.createTmpFile(getContext(), audioPickerConfig.getFilePath());
        String provider = audioPickerConfig.getProvider();
        Uri imageUri = FileProvider.getUriForFile(mContext, provider, recordTempFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (recordTempFile != null) {
                    if (!audioPickerConfig.isMultiSelect()) {
                        selectAudio.clear();
                    }
                    selectAudio.add(recordTempFile.getAbsolutePath());
                    // 通知系统扫描该文件夹
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri uri = Uri.fromFile(new File(FileUtils.getFilesDir(mContext) + audioPickerConfig.getFilePath()));
                    intent.setData(uri);
                    getActivity().sendBroadcast(intent);
                    iHandlerCallBack.onSuccess(selectAudio);
                    FileService.getInstance().getMediaList(MediaEntity.MEDIA_IMAGE, getContext());
                }
            } else {
                if (recordTempFile != null && recordTempFile.exists()) {
                    recordTempFile.delete();
                }
            }
        }
    }
}
