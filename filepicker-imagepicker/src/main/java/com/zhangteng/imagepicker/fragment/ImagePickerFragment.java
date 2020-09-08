package com.zhangteng.imagepicker.fragment;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhangteng.base.base.BaseFragment;
import com.zhangteng.base.utils.FileUtils;
import com.zhangteng.common.callback.IHandlerCallBack;
import com.zhangteng.imagepicker.R;
import com.zhangteng.imagepicker.adapter.ImagePickerAdapter;
import com.zhangteng.imagepicker.config.ImagePickerConfig;
import com.zhangteng.searchfilelibrary.FileService;
import com.zhangteng.searchfilelibrary.entity.ImageEntity;
import com.zhangteng.searchfilelibrary.entity.MediaEntity;
import com.zhangteng.searchfilelibrary.utils.MediaStoreUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 *
 */
public class ImagePickerFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private RecyclerView mRecyclerViewImageList;
    private TextView mTextViewSelected;
    private TextView mTextViewUpload;
    private Context mContext;
    private ArrayList<ImageEntity> imageInfos;
    private ImagePickerAdapter imagePickerAdapter;
    private int REQUEST_CODE = 100;
    private File cameraTempFile;
    private ImagePickerConfig imagePickerConfig;
    private IHandlerCallBack iHandlerCallBack;
    private List<String> selectImage;

    public ImagePickerFragment() {

    }

    /**
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ImagePickerFragment.
     */
    public static ImagePickerFragment newInstance(String param1, String param2) {
        ImagePickerFragment fragment = new ImagePickerFragment();
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
        return inflater.inflate(R.layout.fragment_image_picker, container, false);
    }

    @Override
    protected void initView(View view) {
        mRecyclerViewImageList = (RecyclerView) view.findViewById(R.id.image_picker_rv_list);
        mRecyclerViewImageList.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mTextViewSelected = (TextView) view.findViewById(R.id.image_picker_tv_selected);
        mTextViewUpload = (TextView) view.findViewById(R.id.image_picker_tv_upload);
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
        imagePickerConfig = ImagePickerConfig.getInstance();
        selectImage = imagePickerConfig.getPathList();
        iHandlerCallBack = imagePickerConfig.getiHandlerCallBack();
        iHandlerCallBack.onStart();
        if (imagePickerConfig.isOpenCamera()) {
            startCamera();
        }
        mContext = getContext();
        imageInfos = new ArrayList<>();
        mTextViewSelected.setText(mContext.getString(R.string.image_picker_selected, 0));
        imagePickerAdapter = new ImagePickerAdapter(mContext, imageInfos);
        imagePickerAdapter.setOnItemClickListener(new ImagePickerAdapter.OnItemClickListener() {
            @Override
            public void onCameraClick(List<String> selectImage) {
                startCamera();
                ImagePickerFragment.this.selectImage = selectImage;
            }

            @Override
            public void onImageClick(List<String> selectImage) {
                mTextViewSelected.setText(mContext.getString(R.string.image_picker_selected, selectImage.size()));
                iHandlerCallBack.onSuccess(selectImage);
                ImagePickerFragment.this.selectImage = selectImage;
            }
        });
        mRecyclerViewImageList.setAdapter(imagePickerAdapter);
        getActivity().startService(new Intent(getActivity(), FileService.class));
        FileService.getInstance().getMediaList(MediaEntity.MEDIA_IMAGE, getActivity());
        MediaStoreUtil.setListener(new MediaStoreUtil.ImageListener() {

            @Override
            public void onImageChange(int imageCount, List<MediaEntity> images) {
                for (MediaEntity imageEntity : images) {
                    imageInfos.add((ImageEntity) imageEntity);
                }
                if (getActivity() == null) {
                    return;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imagePickerAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    private void startCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraTempFile = FileUtils.createTmpFile(getContext(), imagePickerConfig.getFilePath());
        String provider = imagePickerConfig.getProvider();
        Uri imageUri = FileProvider.getUriForFile(mContext, provider, cameraTempFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (cameraTempFile != null) {
                    if (!imagePickerConfig.isMultiSelect()) {
                        selectImage.clear();
                    }
                    selectImage.add(cameraTempFile.getAbsolutePath());
                    // 通知系统扫描该文件夹
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri uri = Uri.fromFile(new File(FileUtils.getFilesDir(mContext) + imagePickerConfig.getFilePath()));
                    intent.setData(uri);
                    getActivity().sendBroadcast(intent);
                    iHandlerCallBack.onSuccess(selectImage);
                    FileService.getInstance().getMediaList(MediaEntity.MEDIA_IMAGE, getContext());
                }
            } else {
                if (cameraTempFile != null && cameraTempFile.exists()) {
                    cameraTempFile.delete();
                }
                if (imagePickerConfig.isOpenCamera()) {

                }
            }
        }
    }
}
