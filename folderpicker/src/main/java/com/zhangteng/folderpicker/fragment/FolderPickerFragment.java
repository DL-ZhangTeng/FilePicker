package com.zhangteng.folderpicker.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.zhangteng.baselibrary.base.BaseFragment;
import com.zhangteng.baselibrary.callback.IHandlerCallBack;
import com.zhangteng.folderpicker.R;
import com.zhangteng.folderpicker.adapter.FolderPickerAdapter;
import com.zhangteng.folderpicker.config.FolderPickerConfig;
import com.zhangteng.searchfilelibrary.FileService;
import com.zhangteng.searchfilelibrary.config.SearchCofig;
import com.zhangteng.searchfilelibrary.entity.MediaEntity;
import com.zhangteng.searchfilelibrary.utils.MediaStoreUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class FolderPickerFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private RecyclerView mRecyclerViewImageList;
    private TextView mTextViewSelected;
    private TextView mTextViewUpload;
    private Context mContext;

    private ArrayList<MediaEntity> imageInfos;

    private File currentFile = new File(SearchCofig.BASE_SD_PATH);

    private FolderPickerAdapter folderPickerAdapter;
    private FolderPickerConfig folderPickerConfig;
    private IHandlerCallBack iHandlerCallBack;
    private List<String> selectFolder;

    public FolderPickerFragment() {

    }


    /**
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FolderPickerFragment.
     */
    public static FolderPickerFragment newInstance(String param1, String param2) {
        FolderPickerFragment fragment = new FolderPickerFragment();
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
        return R.layout.fragment_folder_picker;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    @Override
    protected void initView(View view) {
        mRecyclerViewImageList = (RecyclerView) view.findViewById(R.id.folder_picker_rv_list);
        mRecyclerViewImageList.setLayoutManager(new LinearLayoutManager(getContext()));
        mTextViewSelected = (TextView) view.findViewById(R.id.folder_picker_tv_selected);
        mTextViewUpload = (TextView) view.findViewById(R.id.folder_picker_tv_upload);
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
        folderPickerConfig = FolderPickerConfig.getInstance();
        selectFolder = folderPickerConfig.getPathList();
        iHandlerCallBack = folderPickerConfig.getiHandlerCallBack();
        iHandlerCallBack.onStart();
        mContext = getContext();
        imageInfos = new ArrayList<>();

        mTextViewSelected.setText(mContext.getString(R.string.folder_picker_selected, 0));

        mTextViewSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!SearchCofig.BASE_SD_PATH.equals(currentFile.getAbsolutePath())) {
                    mTextViewSelected.setText(currentFile.getParentFile().getName());
                    FileService.getInstance().getFileList(currentFile.getParent());
                    currentFile = currentFile.getParentFile();
                }
            }
        });
        folderPickerAdapter = new FolderPickerAdapter(mContext, imageInfos);
        folderPickerAdapter.setHasPreviousBtn(true);
        folderPickerAdapter.setOnItemClickListener(new FolderPickerAdapter.OnItemClickListener() {
            @Override
            public void onImageClick(List<String> selectImage) {
                mTextViewSelected.setText(mContext.getString(R.string.folder_picker_selected, selectImage.size()));
                iHandlerCallBack.onSuccess(selectImage);
                FolderPickerFragment.this.selectFolder = selectImage;
            }

            @Override
            public void onNextFolder(final String nextPath) {
                currentFile = new File(nextPath);
                mTextViewSelected.setText(currentFile.getName());
                FileService.getInstance().getFileList(nextPath);
            }

            @Override
            public void onPreviousFolder(String previousPaht) {
                if (!SearchCofig.BASE_SD_PATH.equals(currentFile.getAbsolutePath())) {
                    FileService.getInstance().getFileList(currentFile.getParent());
                    currentFile = currentFile.getParentFile();
                }
            }
        });
        mRecyclerViewImageList.setAdapter(folderPickerAdapter);
        getActivity().startService(new Intent(getContext(), FileService.class));
        FileService.getInstance().getFileList(null);
        MediaStoreUtil.setListener(new MediaStoreUtil.FolderListener() {

            @Override
            public void onFolderChange(int imageCount, List<MediaEntity> folders) {
                imageInfos.clear();
                imageInfos.addAll(folders);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        folderPickerAdapter.setFolderInfoList(imageInfos);
                    }
                });
            }
        });
    }

}
