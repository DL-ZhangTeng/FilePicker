package com.zhangteng.audiopicker.fragment;

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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhangteng.audiopicker.R;
import com.zhangteng.audiopicker.adapter.AudioPickerAdapter;
import com.zhangteng.audiopicker.config.AudioPickerConfig;
import com.zhangteng.base.base.BaseFragment;
import com.zhangteng.base.utils.FileUtils;
import com.zhangteng.common.callback.IHandlerCallBack;
import com.zhangteng.searchfilelibrary.FileService;
import com.zhangteng.searchfilelibrary.entity.AudioEntity;
import com.zhangteng.searchfilelibrary.entity.MediaEntity;
import com.zhangteng.searchfilelibrary.utils.MediaStoreUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 *
 */
public class AudioPickerFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private RecyclerView mRecyclerViewImageList;
    private TextView mTextViewSelected;
    private TextView mTextViewUpload;
    private Context mContext;
    private ArrayList<AudioEntity> imageInfos;
    private AudioPickerAdapter audioPickerAdapter;
    private int REQUEST_CODE = 100;
    private File recordTempFile;
    private AudioPickerConfig audioPickerConfig;
    private IHandlerCallBack iHandlerCallBack;
    private List<String> selectAudio;

    public AudioPickerFragment() {

    }

    /**
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AudioPickerFragment.
     */
    public static AudioPickerFragment newInstance(String param1, String param2) {
        AudioPickerFragment fragment = new AudioPickerFragment();
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_audio_picker, container, false);
    }

    @Override
    protected void initView(View view) {
        mRecyclerViewImageList = (RecyclerView) view.findViewById(R.id.audio_picker_rv_list);
        mRecyclerViewImageList.setLayoutManager(new LinearLayoutManager(getContext()));
        mTextViewSelected = (TextView) view.findViewById(R.id.audio_picker_tv_selected);
        mTextViewUpload = (TextView) view.findViewById(R.id.audio_picker_tv_upload);
        mTextViewSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        mTextViewUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iHandlerCallBack.onSuccess(selectAudio);
            }
        });
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
        audioPickerConfig = new AudioPickerConfig(new AudioPickerConfig.Builder());
        selectAudio = audioPickerConfig.getPathList();
        iHandlerCallBack = audioPickerConfig.getiHandlerCallBack();
        iHandlerCallBack.onStart();
        if (audioPickerConfig.isOpenRecord()) {
            startRecord();
        }
        mContext = getContext();
        imageInfos = new ArrayList<>();
        mTextViewSelected.setText(mContext.getString(R.string.audio_picker_selected, 0));
        audioPickerAdapter = new AudioPickerAdapter(mContext, imageInfos);
        audioPickerAdapter.setOnItemClickListener(new AudioPickerAdapter.OnItemClickListener() {
            @Override
            public void onRecordClick(List<String> selectImage) {
                startRecord();
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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        audioPickerAdapter.notifyDataSetChanged();
                    }
                });
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
