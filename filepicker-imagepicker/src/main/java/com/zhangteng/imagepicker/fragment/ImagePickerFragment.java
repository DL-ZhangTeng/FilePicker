package com.zhangteng.imagepicker.fragment;

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
import com.zhangteng.imagepicker.R;
import com.zhangteng.imagepicker.adapter.ImagePickerAdapter;
import com.zhangteng.searchfilelibrary.FileService;
import com.zhangteng.searchfilelibrary.entity.ImageEntity;
import com.zhangteng.searchfilelibrary.entity.MediaEntity;
import com.zhangteng.searchfilelibrary.utils.MediaStoreUtil;
import com.zhangteng.utils.FileUtilsKt;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 图片选择器（需要更个性的自定义样式可使用：https://github.com/duoluo9/ImagePicker）
 */
public class ImagePickerFragment extends Fragment {
    private RecyclerView mRecyclerViewImageList;
    private TextView mTextViewPreview;
    private TextView mTextViewSelected;
    private TextView mTextViewUpload;
    private Context mContext;
    private ArrayList<ImageEntity> imageInfos;
    private ImagePickerAdapter imagePickerAdapter;
    private final int REQUEST_CODE = 100;
    private File cameraTempFile;
    private FilePickerConfig imagePickerConfig;
    private IHandlerCallBack iHandlerCallBack;
    private List<MediaEntity> selectImage;

    public ImagePickerFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image_picker, container, false);
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
        mRecyclerViewImageList = view.findViewById(R.id.image_picker_rv_list);
        mRecyclerViewImageList.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mTextViewPreview = view.findViewById(R.id.file_picker_tv_preview);
        mTextViewSelected = view.findViewById(R.id.file_picker_tv_selected);
        mTextViewUpload = view.findViewById(R.id.file_picker_tv_upload);
        mTextViewPreview.setOnClickListener(v -> iHandlerCallBack.onPreview(selectImage));
        mTextViewSelected.setOnClickListener(view1 -> iHandlerCallBack.onSuccess(selectImage));
        mTextViewUpload.setOnClickListener(view12 -> {
            iHandlerCallBack.onSuccess(selectImage);
            iHandlerCallBack.onFinish(selectImage);
            if (null != getActivity()) {
                getActivity().finish();
            }
        });
    }

    public void initData() {
        imagePickerConfig = FilePickerConfig.getInstance();
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
            public void onCameraClick(List<MediaEntity> selectImage) {
                AndroidPermission androidPermission = new AndroidPermission.Buidler()
                        .with(ImagePickerFragment.this)
                        .permission(Permission.CAMERA)
                        .callback(new Callback() {
                            @Override
                            public void success(Activity permissionActivity) {
                                startCamera();
                            }

                            @Override
                            public void failure(Activity permissionActivity) {
                                Toast.makeText(mContext, "请开启相机权限！", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void nonExecution(Activity permissionActivity) {
                                //权限已通过，请求未执行
                                startCamera();
                            }
                        })
                        .build();
                androidPermission.execute();

                ImagePickerFragment.this.selectImage = selectImage;
            }

            @Override
            public void onImageClick(List<MediaEntity> selectImage) {
                mTextViewSelected.setText(mContext.getString(R.string.image_picker_selected, selectImage.size()));
                iHandlerCallBack.onSuccess(selectImage);
                ImagePickerFragment.this.selectImage = selectImage;
            }
        });
        mRecyclerViewImageList.setAdapter(imagePickerAdapter);
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
    }

    private void searchFile() {
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
                    @SuppressLint("NotifyDataSetChanged")
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
        cameraTempFile = FileUtilsKt.createImageFile(getContext(), Objects.requireNonNull(FileUtilsKt.getFilesDir(getContext())), imagePickerConfig.getFilePath());
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
                    MediaEntity mediaEntity = new ImageEntity(cameraTempFile.getName(), cameraTempFile.getAbsolutePath(), cameraTempFile.length(), MediaEntity.MEDIA_IMAGE, cameraTempFile.lastModified());
                    selectImage.add(mediaEntity);
                    // 通知系统扫描该文件夹
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri uri = Uri.fromFile(new File(FileUtilsKt.getFilesDir(mContext) + imagePickerConfig.getFilePath()));
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
