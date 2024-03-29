package com.zhangteng.audiopicker.adapter;

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

import com.zhangteng.audiopicker.R;
import com.zhangteng.common.config.FilePickerConfig;
import com.zhangteng.searchfilelibrary.entity.AudioEntity;
import com.zhangteng.searchfilelibrary.entity.MediaEntity;
import com.zhangteng.utils.DateUtilsKt;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by swing on 2018/4/17.
 */
public class AudioPickerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context mContext;
    private List<AudioEntity> audioInfoList;
    private final FilePickerConfig audioPickerConfig = FilePickerConfig.getInstance();
    private List<MediaEntity> selectAudio = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public AudioPickerAdapter(Context context, ArrayList<AudioEntity> audioInfoList) {
        this.mContext = context;
        this.audioInfoList = audioInfoList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImageViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_audio_picker_audio, parent, false));
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        AudioEntity audioInfo = audioInfoList.get(position);
        ((ImageViewHolder) holder).imageView.setImageResource(audioPickerConfig.getIconResources(MediaEntity.MEDIA_AUDIO));
        ((ImageViewHolder) holder).name.setText(audioInfo.getFileName());
        ((ImageViewHolder) holder).time.setText(DateUtilsKt.getTimeStr(audioInfo.getUpdateTime(), DateUtilsKt.FORMAT_YMD));//audioInfo.getTime()音频持续时间
        ((ImageViewHolder) holder).size.setText(mContext.getString(R.string.audio_picker_audio_size, audioInfo.getFileLength() / 1024));
        final AudioEntity finalAudioInfo1 = audioInfo;
        holder.itemView.setOnClickListener(view -> {
            if (selectAudio.contains(finalAudioInfo1)) {
                selectAudio.remove(finalAudioInfo1);
            } else {
                if (selectAudio.size() < audioPickerConfig.getMaxSize())
                    selectAudio.add(finalAudioInfo1);
            }
            if (onItemClickListener != null)
                onItemClickListener.onImageClick(selectAudio);
            notifyDataSetChanged();
        });
        initView(holder, audioInfo);

    }

    private void initView(RecyclerView.ViewHolder holder, AudioEntity audioInfo) {
        if (audioPickerConfig.isMultiSelect()) {
            ((ImageViewHolder) holder).checkBox.setVisibility(View.VISIBLE);
        } else {
            ((ImageViewHolder) holder).checkBox.setVisibility(View.GONE);
        }
        if (null == selectAudio) selectAudio = new ArrayList<>();
        boolean isContains = false;
        for (MediaEntity mediaEntity : selectAudio) {
            if (null != mediaEntity
                    && null != mediaEntity.getFilePath()
                    && mediaEntity.getFilePath().equals(audioInfo.getFilePath())) {
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
        return audioInfoList == null ? 0 : audioInfoList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setAudioInfoList(List<AudioEntity> audioInfoList) {
        this.audioInfoList = audioInfoList;
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
            this.imageView = itemView.findViewById(R.id.audio_picker_iv_audio_image);
            this.mask = itemView.findViewById(R.id.audio_picker_v_photo_mask);
            this.checkBox = itemView.findViewById(R.id.audio_picker_cb_select);
            this.name = itemView.findViewById(R.id.audio_picker_tv_audio_name);
            this.time = itemView.findViewById(R.id.audio_picker_tv_audio_time);
            this.size = itemView.findViewById(R.id.audio_picker_tv_audio_size);
        }
    }
}
