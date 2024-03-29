package com.zhangteng.documentpicker.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zhangteng.common.config.FilePickerConfig;
import com.zhangteng.documentpicker.R;
import com.zhangteng.searchfilelibrary.entity.DocumentEntity;
import com.zhangteng.searchfilelibrary.entity.MediaEntity;
import com.zhangteng.utils.DateUtilsKt;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by swing on 2018/4/17.
 */
public class DocumentPickerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context mContext;
    private List<DocumentEntity> documentInfoList;
    private final FilePickerConfig documentPickerConfig = FilePickerConfig.getInstance();
    private List<MediaEntity> selectDocument = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public DocumentPickerAdapter(Context context, ArrayList<DocumentEntity> documentInfoList) {
        this.mContext = context;
        this.documentInfoList = documentInfoList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImageViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_document_picker_document, parent, false));
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        DocumentEntity documentInfo;
        documentInfo = documentInfoList.get(position);
        ((ImageViewHolder) holder).imageView.setImageResource(documentPickerConfig.getIconResources(MediaEntity.MEDIA_DOCUMENT));
        ((ImageViewHolder) holder).name.setText(documentInfo.getFileName());
        ((ImageViewHolder) holder).time.setText(DateUtilsKt.getTimeStr(documentInfo.getUpdateTime(), DateUtilsKt.FORMAT_YMD));//documentInfo.getTime()音频持续时间
        ((ImageViewHolder) holder).size.setText(mContext.getString(R.string.document_picker_document_size, documentInfo.getFileLength() / 1024));
        final DocumentEntity finalDocumentInfo1 = documentInfo;
        holder.itemView.setOnClickListener(view -> {
            if (selectDocument.contains(finalDocumentInfo1)) {
                selectDocument.remove(finalDocumentInfo1);
            } else {
                if (selectDocument.size() < documentPickerConfig.getMaxSize())
                    selectDocument.add(finalDocumentInfo1);
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
        if (null == selectDocument) selectDocument = new ArrayList<>();
        boolean isContains = false;
        for (MediaEntity mediaEntity : selectDocument) {
            if (null != mediaEntity
                    && null != mediaEntity.getFilePath()
                    && mediaEntity.getFilePath().equals(documentInfo.getFilePath())) {
                isContains = true;
                ((ImageViewHolder) holder).checkBox.setVisibility(View.VISIBLE);
                ((ImageViewHolder) holder).mask.setVisibility(View.VISIBLE);
                ((ImageViewHolder) holder).checkBox.setChecked(true);
                ((ImageViewHolder) holder).checkBox.setButtonDrawable(R.mipmap.picker_select_checked);
            }
        }
        if (!isContains) {
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

    @SuppressLint("NotifyDataSetChanged")
    public void setDocumentInfoList(List<DocumentEntity> documentInfoList) {
        this.documentInfoList = documentInfoList;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onImageClick(List<MediaEntity> selectImage);
    }

    private static class ImageViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final View mask;
        private final CheckBox checkBox;
        private final TextView name;
        private final TextView time;
        private final TextView size;


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
