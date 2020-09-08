package com.zhangteng.documentpicker.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhangteng.common.config.FilePickerConfig;
import com.zhangteng.documentpicker.R;
import com.zhangteng.searchfilelibrary.entity.DocumentEntity;
import com.zhangteng.searchfilelibrary.entity.MediaEntity;
import com.zhangteng.searchfilelibrary.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by swing on 2018/4/17.
 */
public class DocumentPickerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<DocumentEntity> documentInfoList;
    private FilePickerConfig documentPickerConfig = FilePickerConfig.getInstance();
    private List<String> selectDocument = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public DocumentPickerAdapter(Context context, ArrayList<DocumentEntity> documentInfoList) {
        this.mContext = context;
        this.documentInfoList = documentInfoList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ImageViewHolder imageViewHolder = new ImageViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_document_picker_document, parent, false));
        return imageViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        DocumentEntity documentInfo;
        documentInfo = documentInfoList.get(position);
        ((ImageViewHolder) holder).imageView.setImageResource(documentPickerConfig.getIconResources(MediaEntity.MEDIA_DOCUMENT));
        ((ImageViewHolder) holder).name.setText(documentInfo.getFileName());
        ((ImageViewHolder) holder).time.setText(DateUtils.getTime(documentInfo.getUpdateTime(), DateUtils.FORMAT_YMD));//documentInfo.getTime()音频持续时间
        ((ImageViewHolder) holder).size.setText(mContext.getString(R.string.document_picker_document_size, documentInfo.getFileLength() / 1024));
        final DocumentEntity finalDocumentInfo1 = documentInfo;
        ((ImageViewHolder) holder).itemView.setOnClickListener(view -> {
            if (selectDocument.contains(finalDocumentInfo1.getFilePath())) {
                selectDocument.remove(finalDocumentInfo1.getFilePath());
            } else {
                if (selectDocument.size() < documentPickerConfig.getMaxSize())
                    selectDocument.add(finalDocumentInfo1.getFilePath());
            }
            if (onItemClickListener != null)
                onItemClickListener.onImageClick(selectDocument);
            notifyDataSetChanged();
        });
        initView(holder, documentInfo);
    }

    private void initView(RecyclerView.ViewHolder holder, DocumentEntity documentInfo) {
        if (documentPickerConfig.isMultiSelect()) {
            ((ImageViewHolder) holder).checkBox.setVisibility(View.VISIBLE);
        } else {
            ((ImageViewHolder) holder).checkBox.setVisibility(View.GONE);
        }
        if (selectDocument.contains(documentInfo.getFilePath())) {
            ((ImageViewHolder) holder).checkBox.setVisibility(View.VISIBLE);
            ((ImageViewHolder) holder).mask.setVisibility(View.VISIBLE);
            ((ImageViewHolder) holder).checkBox.setChecked(true);
            ((ImageViewHolder) holder).checkBox.setButtonDrawable(R.mipmap.picker_select_checked);
        } else {
            ((ImageViewHolder) holder).checkBox.setVisibility(View.VISIBLE);
            ((ImageViewHolder) holder).mask.setVisibility(View.GONE);
            ((ImageViewHolder) holder).checkBox.setChecked(false);
            ((ImageViewHolder) holder).checkBox.setButtonDrawable(R.mipmap.picker_select_unchecked);
        }
    }

    @Override
    public int getItemCount() {
        return documentInfoList.isEmpty() ? 0 : documentInfoList.size();
    }

    public void setDocumentInfoList(List<DocumentEntity> documentInfoList) {
        this.documentInfoList = documentInfoList;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onImageClick(List<String> selectImage);
    }

    private static class ImageViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private View mask;
        private CheckBox checkBox;
        private TextView name;
        private TextView time;
        private TextView size;


        public ImageViewHolder(View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.document_picker_iv_document_image);
            this.mask = itemView.findViewById(R.id.document_picker_v_photo_mask);
            this.checkBox = itemView.findViewById(R.id.document_picker_cb_select);
            this.name = itemView.findViewById(R.id.document_picker_tv_document_name);
            this.time = itemView.findViewById(R.id.document_picker_tv_document_time);
            this.size = itemView.findViewById(R.id.document_picker_tv_document_size);
        }
    }
}
