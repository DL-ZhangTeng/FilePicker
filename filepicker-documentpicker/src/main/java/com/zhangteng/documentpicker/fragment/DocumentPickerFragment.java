package com.zhangteng.documentpicker.fragment;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhangteng.base.base.BaseFragment;
import com.zhangteng.common.callback.IHandlerCallBack;
import com.zhangteng.documentpicker.R;
import com.zhangteng.documentpicker.adapter.DocumentPickerAdapter;
import com.zhangteng.documentpicker.config.DocumentPickerConfig;
import com.zhangteng.searchfilelibrary.FileService;
import com.zhangteng.searchfilelibrary.entity.DocumentEntity;
import com.zhangteng.searchfilelibrary.entity.MediaEntity;
import com.zhangteng.searchfilelibrary.utils.MediaStoreUtil;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class DocumentPickerFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private RecyclerView mRecyclerViewImageList;
    private TextView mTextViewSelected;
    private TextView mTextViewUpload;
    private Context mContext;
    private ArrayList<DocumentEntity> imageInfos;
    private DocumentPickerAdapter documentPickerAdapter;
    private DocumentPickerConfig documentPickerConfig;
    private IHandlerCallBack iHandlerCallBack;
    private List<String> selectDocument;

    public DocumentPickerFragment() {

    }

    /**
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DocumentPickerFragment.
     */
    public static DocumentPickerFragment newInstance(String param1, String param2) {
        DocumentPickerFragment fragment = new DocumentPickerFragment();
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
        return inflater.inflate(R.layout.fragment_document_picker, container, false);
    }

    @Override
    protected void initView(View view) {
        mRecyclerViewImageList = (RecyclerView) view.findViewById(R.id.document_picker_rv_list);
        mRecyclerViewImageList.setLayoutManager(new LinearLayoutManager(getContext()));
        mTextViewSelected = (TextView) view.findViewById(R.id.document_picker_tv_selected);
        mTextViewUpload = (TextView) view.findViewById(R.id.document_picker_tv_upload);
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
        documentPickerConfig = DocumentPickerConfig.getInstance();
        selectDocument = documentPickerConfig.getPathList();
        iHandlerCallBack = documentPickerConfig.getiHandlerCallBack();
        iHandlerCallBack.onStart();
        mContext = getContext();
        imageInfos = new ArrayList<>();
        mTextViewSelected.setText(mContext.getString(R.string.document_picker_selected, 0));
        documentPickerAdapter = new DocumentPickerAdapter(mContext, imageInfos);
        documentPickerAdapter.setOnItemClickListener(new DocumentPickerAdapter.OnItemClickListener() {
            @Override
            public void onImageClick(List<String> selectImage) {
                mTextViewSelected.setText(mContext.getString(R.string.document_picker_selected, selectImage.size()));
                iHandlerCallBack.onSuccess(selectImage);
                DocumentPickerFragment.this.selectDocument = selectImage;
            }
        });
        mRecyclerViewImageList.setAdapter(documentPickerAdapter);
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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        documentPickerAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

}
