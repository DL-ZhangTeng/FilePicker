package com.zhangteng.rarpicker.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zhangteng.androidpermission.AndroidPermission;
import com.zhangteng.androidpermission.Permission;
import com.zhangteng.androidpermission.callback.Callback;
import com.zhangteng.common.callback.IHandlerCallBack;
import com.zhangteng.common.config.FilePickerConfig;
import com.zhangteng.rarpicker.R;
import com.zhangteng.rarpicker.adapter.RarPickerAdapter;
import com.zhangteng.searchfilelibrary.FileService;
import com.zhangteng.searchfilelibrary.entity.MediaEntity;
import com.zhangteng.searchfilelibrary.entity.RarEntity;
import com.zhangteng.searchfilelibrary.utils.MediaStoreUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 压缩包选择器
 */
public class RarPickerFragment extends Fragment {

    private RecyclerView mRecyclerViewImageList;
    private TextView mTextViewPreview;
    private TextView mTextViewSelected;
    private TextView mTextViewUpload;
    private Context mContext;
    private ArrayList<RarEntity> imageInfos;
    private RarPickerAdapter rarPickerAdapter;
    private FilePickerConfig rarPickerConfig;
    private IHandlerCallBack iHandlerCallBack;
    private List<MediaEntity> selectRar;

    public RarPickerFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rar_picker, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initData();
    }

    protected void initView(View view) {
        mRecyclerViewImageList = view.findViewById(R.id.rar_picker_rv_list);
        mRecyclerViewImageList.setLayoutManager(new LinearLayoutManager(getContext()));
        mTextViewPreview = view.findViewById(R.id.file_picker_tv_preview);
        mTextViewSelected = view.findViewById(R.id.file_picker_tv_selected);
        mTextViewUpload = view.findViewById(R.id.file_picker_tv_upload);
        mTextViewPreview.setOnClickListener(v -> iHandlerCallBack.onPreview(selectRar));
        mTextViewSelected.setOnClickListener(view1 -> iHandlerCallBack.onSuccess(selectRar));
        mTextViewUpload.setOnClickListener(view12 -> {
            iHandlerCallBack.onSuccess(selectRar);
            iHandlerCallBack.onFinish(selectRar);
            if (null != getActivity()) {
                getActivity().finish();
            }
        });
    }

    public void initData() {
        rarPickerConfig = FilePickerConfig.getInstance();
        selectRar = rarPickerConfig.getPathList();
        iHandlerCallBack = rarPickerConfig.getiHandlerCallBack();
        iHandlerCallBack.onStart();
        mContext = getContext();
        imageInfos = new ArrayList<>();
        mTextViewSelected.setText(mContext.getString(R.string.rar_picker_selected, 0));
        rarPickerAdapter = new RarPickerAdapter(mContext, imageInfos);
        rarPickerAdapter.setOnItemClickListener(selectImage -> {
            mTextViewSelected.setText(mContext.getString(R.string.rar_picker_selected, selectImage.size()));
            iHandlerCallBack.onSuccess(selectImage);
            RarPickerFragment.this.selectRar = selectImage;
        });
        mRecyclerViewImageList.setAdapter(rarPickerAdapter);

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
        MediaStoreUtil.setListener(MediaEntity.MEDIA_ZIP, new MediaStoreUtil.RarListener() {

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onRarChange(int imageCount, List<MediaEntity> rars) {
                for (MediaEntity rarEntity : rars) {
                    imageInfos.add((RarEntity) rarEntity);
                }
                if (getActivity() == null) {
                    return;
                }
                getActivity().runOnUiThread(() -> rarPickerAdapter.notifyDataSetChanged());
            }
        });
        FileService.getInstance().getMediaList(MediaEntity.MEDIA_ZIP, getContext());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MediaStoreUtil.removeListener(MediaEntity.MEDIA_ZIP);
    }
}
