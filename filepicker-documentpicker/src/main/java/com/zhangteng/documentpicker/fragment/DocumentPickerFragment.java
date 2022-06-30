package com.zhangteng.documentpicker.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.zhangteng.documentpicker.R;
import com.zhangteng.documentpicker.adapter.DocumentPickerAdapter;
import com.zhangteng.searchfilelibrary.FileService;
import com.zhangteng.searchfilelibrary.entity.DocumentEntity;
import com.zhangteng.searchfilelibrary.entity.MediaEntity;
import com.zhangteng.searchfilelibrary.utils.MediaStoreUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 文档选择器
 */
public class DocumentPickerFragment extends Fragment {
    private RecyclerView mRecyclerViewImageList;
    private TextView mTextViewPreview;
    private TextView mTextViewSelected;
    private TextView mTextViewUpload;
    private Context mContext;
    private ArrayList<DocumentEntity> imageInfos;
    private DocumentPickerAdapter documentPickerAdapter;
    private FilePickerConfig documentPickerConfig;
    private IHandlerCallBack iHandlerCallBack;
    private List<MediaEntity> selectDocument;

    public DocumentPickerFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_document_picker, container, false);
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
        mRecyclerViewImageList = view.findViewById(R.id.document_picker_rv_list);
        mRecyclerViewImageList.setLayoutManager(new LinearLayoutManager(getContext()));
        mTextViewPreview = view.findViewById(R.id.file_picker_tv_preview);
        mTextViewSelected = view.findViewById(R.id.file_picker_tv_selected);
        mTextViewUpload = view.findViewById(R.id.file_picker_tv_upload);
        mTextViewPreview.setOnClickListener(v -> iHandlerCallBack.onPreview(selectDocument));
        mTextViewSelected.setOnClickListener(view1 -> iHandlerCallBack.onSuccess(selectDocument));
        mTextViewUpload.setOnClickListener(view12 -> {
            iHandlerCallBack.onSuccess(selectDocument);
            iHandlerCallBack.onFinish(selectDocument);
            if (null != getActivity()) {
                getActivity().finish();
            }
        });
    }

    public void initData() {
        documentPickerConfig = FilePickerConfig.getInstance();
        selectDocument = documentPickerConfig.getPathList();
        iHandlerCallBack = documentPickerConfig.getiHandlerCallBack();
        iHandlerCallBack.onStart();
        mContext = getContext();
        imageInfos = new ArrayList<>();
        mTextViewSelected.setText(mContext.getString(R.string.document_picker_selected, 0));
        documentPickerAdapter = new DocumentPickerAdapter(mContext, imageInfos);
        documentPickerAdapter.setOnItemClickListener(selectImage -> {
            mTextViewSelected.setText(mContext.getString(R.string.document_picker_selected, selectImage.size()));
            iHandlerCallBack.onSuccess(selectImage);
            DocumentPickerFragment.this.selectDocument = selectImage;
        });
        mRecyclerViewImageList.setAdapter(documentPickerAdapter);
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
        getActivity().startService(new Intent(getContext(), FileService.class));
        FileService.getInstance().getMediaList(MediaEntity.MEDIA_DOCUMENT, getContext());
        MediaStoreUtil.setListener(new MediaStoreUtil.DocumentListener() {

            @Override
            public void onDocumentChange(int imageCount, List<MediaEntity> documents) {
                for (MediaEntity documentEntity : documents) {
                    imageInfos.add((DocumentEntity) documentEntity);
                }
                if (getActivity() == null) {
                    return;
                }
                getActivity().runOnUiThread(() -> documentPickerAdapter.notifyDataSetChanged());
            }
        });
    }
}
