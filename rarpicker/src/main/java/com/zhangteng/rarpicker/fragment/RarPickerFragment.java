package com.zhangteng.rarpicker.fragment;

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
import com.zhangteng.rarpicker.R;
import com.zhangteng.rarpicker.adapter.RarPickerAdapter;
import com.zhangteng.rarpicker.config.RarPickerConfig;
import com.zhangteng.searchfilelibrary.FileService;
import com.zhangteng.searchfilelibrary.entity.MediaEntity;
import com.zhangteng.searchfilelibrary.entity.RarEntity;
import com.zhangteng.searchfilelibrary.utils.MediaStoreUtil;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class RarPickerFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private RecyclerView mRecyclerViewImageList;
    private TextView mTextViewSelected;
    private TextView mTextViewUpload;
    private Context mContext;
    private ArrayList<RarEntity> imageInfos;
    private RarPickerAdapter rarPickerAdapter;
    private RarPickerConfig rarPickerConfig;
    private IHandlerCallBack iHandlerCallBack;
    private List<String> selectRar;

    public RarPickerFragment() {

    }


    /**
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RarPickerFragment.
     */
    public static RarPickerFragment newInstance(String param1, String param2) {
        RarPickerFragment fragment = new RarPickerFragment();
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
        return inflater.inflate(R.layout.fragment_rar_picker, container, false);
    }

    @Override
    protected void initView(View view) {
        mRecyclerViewImageList = (RecyclerView) view.findViewById(R.id.rar_picker_rv_list);
        mRecyclerViewImageList.setLayoutManager(new LinearLayoutManager(getContext()));
        mTextViewSelected = (TextView) view.findViewById(R.id.rar_picker_tv_selected);
        mTextViewUpload = (TextView) view.findViewById(R.id.rar_picker_tv_upload);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    @Override
    public void initData() {
        super.initData();
        rarPickerConfig = RarPickerConfig.getInstance();
        selectRar = rarPickerConfig.getPathList();
        iHandlerCallBack = rarPickerConfig.getiHandlerCallBack();
        iHandlerCallBack.onStart();
        mContext = getContext();
        imageInfos = new ArrayList<>();
        mTextViewSelected.setText(mContext.getString(R.string.rar_picker_selected, 0));
        rarPickerAdapter = new RarPickerAdapter(mContext, imageInfos);
        rarPickerAdapter.setOnItemClickListener(new RarPickerAdapter.OnItemClickListener() {
            @Override
            public void onImageClick(List<String> selectImage) {
                mTextViewSelected.setText(mContext.getString(R.string.rar_picker_selected, selectImage.size()));
                iHandlerCallBack.onSuccess(selectImage);
                RarPickerFragment.this.selectRar = selectImage;
            }
        });
        mRecyclerViewImageList.setAdapter(rarPickerAdapter);
        getActivity().startService(new Intent(getContext(), FileService.class));
        FileService.getInstance().getMediaList(MediaEntity.MEDIA_ZIP, getContext());
        MediaStoreUtil.setListener(new MediaStoreUtil.RarListener() {

            @Override
            public void onRarChange(int imageCount, List<MediaEntity> rars) {
                for (MediaEntity rarEntity : rars) {
                    imageInfos.add((RarEntity) rarEntity);
                }
                if (getActivity() == null) {
                    return;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rarPickerAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }
}
