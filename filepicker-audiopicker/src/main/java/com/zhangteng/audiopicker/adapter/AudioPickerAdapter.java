package com.zhangteng.audiopicker.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhangteng.audiopicker.R;
import com.zhangteng.common.config.FilePickerConfig;
import com.zhangteng.searchfilelibrary.entity.AudioEntity;
import com.zhangteng.searchfilelibrary.entity.MediaEntity;
import com.zhangteng.searchfilelibrary.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by swing on 2018/4/17.
 */
public class AudioPickerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int HEAD = 0;
    private static final int PHOTO = 1;
    private Context mContext;
    private List<AudioEntity> audioInfoList;
    private FilePickerConfig audioPickerConfig = FilePickerConfig.getInstance();
    private List<MediaEntity> selectAudio = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public AudioPickerAdapter(Context context, ArrayList<AudioEntity> audioInfoList) {
        this.mContext = context;
        this.audioInfoList = audioInfoList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == HEAD) {
            return new RecordViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_audio_picker_record, parent, false));
        } else {
            return new ImageViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_audio_picker_audio, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
//        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
//        int heightOrWidth = Math.min(ScreenUtils.getScreenHeight(mContext) / 3, ScreenUtils.getScreenWidth(mContext) / 3);
//        layoutParams.height = heightOrWidth;
//        layoutParams.width = heightOrWidth;
//        holder.itemView.setLayoutParams(layoutParams);
        AudioEntity audioInfo;
        if (audioPickerConfig.isShowCamera()) {
            if (position == 0) {
                ((RecordViewHolder) holder).itemView.setOnClickListener(view -> {
                    if (onItemClickListener != null) {
                        if (audioPickerConfig.isMultiSelect() && selectAudio.size() < audioPickerConfig.getMaxSize()) {
                            onItemClickListener.onRecordClick(selectAudio);
                        } else if (!audioPickerConfig.isMultiSelect() && selectAudio.isEmpty()) {
                            onItemClickListener.onRecordClick(selectAudio);
                        }
                    }
                    notifyDataSetChanged();
                });
            } else {
                audioInfo = audioInfoList.get(position - 1);
                ((ImageViewHolder) holder).imageView.setImageResource(audioPickerConfig.getIconResources(MediaEntity.MEDIA_AUDIO));
                ((ImageViewHolder) holder).name.setText(audioInfo.getFileName());
                ((ImageViewHolder) holder).time.setText(DateUtils.getTime(audioInfo.getUpdateTime(), DateUtils.FORMAT_YMD));//audioInfo.getTime()音频持续时间
                ((ImageViewHolder) holder).size.setText(mContext.getString(R.string.audio_picker_audio_size, audioInfo.getFileLength() / 1024));
                final AudioEntity finalAudioInfo = audioInfo;
                ((ImageViewHolder) holder).itemView.setOnClickListener(view -> {
                    if (selectAudio.contains(finalAudioInfo)) {
                        selectAudio.remove(finalAudioInfo);
                    } else {
                        if (selectAudio.size() < audioPickerConfig.getMaxSize()) {
                            selectAudio.add(finalAudioInfo);
                        }
                    }
                    if (onItemClickListener != null)
                        onItemClickListener.onImageClick(selectAudio);
                    notifyDataSetChanged();
                });
                initView(holder, audioInfo);
            }
        } else {
            audioInfo = audioInfoList.get(position);
            ((ImageViewHolder) holder).imageView.setImageResource(audioPickerConfig.getIconResources(MediaEntity.MEDIA_AUDIO));
            ((ImageViewHolder) holder).name.setText(audioInfo.getFileName());
            ((ImageViewHolder) holder).time.setText(DateUtils.getTime(audioInfo.getUpdateTime(), DateUtils.FORMAT_YMD));//audioInfo.getTime()音频持续时间
            ((ImageViewHolder) holder).size.setText(mContext.getString(R.string.audio_picker_audio_size, audioInfo.getFileLength() / 1024));
            final AudioEntity finalAudioInfo1 = audioInfo;
            ((ImageViewHolder) holder).itemView.setOnClickListener(view -> {
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

    }

    private void initView(RecyclerView.ViewHolder holder, AudioEntity audioInfo) {
        if (audioPickerConfig.isMultiSelect()) {
            ((ImageViewHolder) holder).checkBox.setVisibility(View.VISIBLE);
        } else {
            ((ImageViewHolder) holder).checkBox.setVisibility(View.GONE);
        }
        if (selectAudio.contains(audioInfo)) {
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
        return audioInfoList.isEmpty() ? 0 : audioPickerConfig.isShowCamera() ? audioInfoList.size() + 1 : audioInfoList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            if (audioPickerConfig.isShowCamera()) {
                return HEAD;
            }
        }
        return PHOTO;
    }

    public void setAudioInfoList(List<AudioEntity> audioInfoList) {
        this.audioInfoList = audioInfoList;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onRecordClick(List<MediaEntity> selectImage);

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
            this.imageView = itemView.findViewById(R.id.audio_picker_iv_audio_image);
            this.mask = itemView.findViewById(R.id.audio_picker_v_photo_mask);
            this.checkBox = itemView.findViewById(R.id.audio_picker_cb_select);
            this.name = itemView.findViewById(R.id.audio_picker_tv_audio_name);
            this.time = itemView.findViewById(R.id.audio_picker_tv_audio_time);
            this.size = itemView.findViewById(R.id.audio_picker_tv_audio_size);
        }
    }

    private static class RecordViewHolder extends RecyclerView.ViewHolder {

        public RecordViewHolder(View itemView) {
            super(itemView);
        }
    }
}
