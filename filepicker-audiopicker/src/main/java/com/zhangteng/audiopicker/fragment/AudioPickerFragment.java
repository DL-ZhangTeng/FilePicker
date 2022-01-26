package com.zhangteng.audiopicker.fragment;

import static android.app.Activity.RESULT_OK;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private final int REQUEST_CODE = 100;
    private File recordTempFile;
    private FilePickerConfig audioPickerConfig;
    private IHandlerCallBack iHandlerCallBack;
    private List<MediaEntity> selectAudio;

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
            iHandlerCallBack.onFinish(selectAudio);
            if (null != getActivity()) {
                getActivity().finish();
            }
        });
    }

    public void initData() {
        audioPickerConfig = FilePickerConfig.getInstance();
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
            public void onRecordClick(List<MediaEntity> selectImage) {
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
            public void onImageClick(List<MediaEntity> selectImage) {
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
                    MediaEntity mediaEntity = new AudioEntity(recordTempFile.getName(), recordTempFile.getAbsolutePath(), recordTempFile.length(), MediaEntity.MEDIA_AUDIO, recordTempFile.lastModified());
                    selectAudio.add(mediaEntity);
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
