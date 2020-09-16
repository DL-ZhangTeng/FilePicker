package com.zhangteng.rarpicker.adapter;

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
import com.zhangteng.rarpicker.R;
import com.zhangteng.searchfilelibrary.entity.MediaEntity;
import com.zhangteng.searchfilelibrary.entity.RarEntity;
import com.zhangteng.searchfilelibrary.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by swing on 2018/4/17.
 */
public class RarPickerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<RarEntity> rarInfoList;
    private FilePickerConfig rarPickerConfig = FilePickerConfig.getInstance();
    private List<MediaEntity> selectRar = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public RarPickerAdapter(Context context, ArrayList<RarEntity> rarInfoList) {
        this.mContext = context;
        this.rarInfoList = rarInfoList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImageViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_rar_picker_rar, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        RarEntity rarInfo = rarInfoList.get(position);
        rarPickerConfig.getImageLoader().loadImage(mContext, ((ImageViewHolder) holder).imageView, rarInfo.getThumPath());
        ((ImageViewHolder) holder).name.setText(rarInfo.getFileName());
        ((ImageViewHolder) holder).time.setText(DateUtils.getTime(rarInfo.getUpdateTime(), DateUtils.FORMAT_YMD));//rarInfo.getTime()音频持续时间
        ((ImageViewHolder) holder).size.setText(mContext.getString(R.string.rar_picker_rar_size, rarInfo.getFileLength() / 1024));
        final RarEntity finalRarInfo1 = rarInfo;
        ((ImageViewHolder) holder).itemView.setOnClickListener(view -> {
            if (selectRar.contains(finalRarInfo1)) {
                selectRar.remove(finalRarInfo1);
            } else {
                if (selectRar.size() < rarPickerConfig.getMaxSize())
                    selectRar.add(finalRarInfo1);
            }
            if (onItemClickListener != null)
                onItemClickListener.onImageClick(selectRar);
            notifyDataSetChanged();
        });
        initView(holder, rarInfo);
    }

    private void initView(RecyclerView.ViewHolder holder, RarEntity rarInfo) {
        if (rarPickerConfig.isMultiSelect()) {
            ((ImageViewHolder) holder).checkBox.setVisibility(View.VISIBLE);
        } else {
            ((ImageViewHolder) holder).checkBox.setVisibility(View.GONE);
        }
        if (null == selectRar) selectRar = new ArrayList<>();
        boolean isContains = false;
        for (MediaEntity mediaEntity : selectRar) {
            if (null != mediaEntity
                    && null != mediaEntity.getFilePath()
                    && mediaEntity.getFilePath().equals(rarInfo.getFilePath())) {
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
        return rarInfoList.isEmpty() ? 0 : rarInfoList.size();
    }

    public void setRarInfoList(List<RarEntity> rarInfoList) {
        this.rarInfoList = rarInfoList;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onImageClick(List<MediaEntity> selectImage);
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
            this.imageView = itemView.findViewById(R.id.rar_picker_iv_rar_image);
            this.mask = itemView.findViewById(R.id.rar_picker_v_photo_mask);
            this.checkBox = itemView.findViewById(R.id.rar_picker_cb_select);
            this.name = itemView.findViewById(R.id.rar_picker_tv_rar_name);
            this.time = itemView.findViewById(R.id.rar_picker_tv_rar_time);
            this.size = itemView.findViewById(R.id.rar_picker_tv_rar_size);
        }
    }
}
