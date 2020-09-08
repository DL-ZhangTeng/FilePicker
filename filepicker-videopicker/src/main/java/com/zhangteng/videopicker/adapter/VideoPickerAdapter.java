package com.zhangteng.videopicker.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.zhangteng.common.config.FilePickerConfig;
import com.zhangteng.searchfilelibrary.entity.VideoEntity;
import com.zhangteng.searchfilelibrary.utils.ScreenUtils;
import com.zhangteng.videopicker.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by swing on 2018/4/17.
 */
public class VideoPickerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int HEAD = 0;
    private static final int PHOTO = 1;
    private Context mContext;
    private List<VideoEntity> videoInfoList;
    private FilePickerConfig videoPickerConfig = FilePickerConfig.getInstance();
    private List<String> selectVideo = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public VideoPickerAdapter(Context context, ArrayList<VideoEntity> videoInfoList) {
        this.mContext = context;
        this.videoInfoList = videoInfoList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == HEAD) {
            return new CameraViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_video_picker_camera, parent, false));
        } else {
            return new VideoViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_video_picker_photo, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        int heightOrWidth = Math.min(ScreenUtils.getScreenHeight(mContext) / 3, ScreenUtils.getScreenWidth(mContext) / 3);
        layoutParams.height = heightOrWidth;
        layoutParams.width = heightOrWidth;
        holder.itemView.setLayoutParams(layoutParams);
        VideoEntity videoInfo;
        if (videoPickerConfig.isShowCamera()) {
            if (position == 0) {
                ((CameraViewHolder) holder).itemView.setOnClickListener(view -> {
                    if (onItemClickListener != null) {
                        if (videoPickerConfig.isMultiSelect() && selectVideo.size() < videoPickerConfig.getMaxSize()) {
                            onItemClickListener.onCameraClick(selectVideo);
                        } else if (!videoPickerConfig.isMultiSelect() && selectVideo.isEmpty()) {
                            onItemClickListener.onCameraClick(selectVideo);
                        }
                    }
                    notifyDataSetChanged();
                });
            } else {
                videoInfo = videoInfoList.get(position - 1);
                videoPickerConfig.getImageLoader().loadImage(mContext, ((VideoViewHolder) holder).imageView, videoInfo.getFilePath());
                final VideoEntity finalVideoInfo = videoInfo;
                ((VideoViewHolder) holder).imageView.setOnClickListener(view -> {
                    if (selectVideo.contains(finalVideoInfo.getFilePath())) {
                        selectVideo.remove(finalVideoInfo.getFilePath());
                    } else {
                        if (selectVideo.size() < videoPickerConfig.getMaxSize()) {
                            selectVideo.add(finalVideoInfo.getFilePath());
                        }
                    }
                    if (onItemClickListener != null)
                        onItemClickListener.onVideoClick(selectVideo);
                    notifyDataSetChanged();
                });
                initView(holder, videoInfo);
            }
        } else {
            videoInfo = videoInfoList.get(position);
            videoPickerConfig.getImageLoader().loadImage(mContext, ((VideoViewHolder) holder).imageView, videoInfo.getFilePath());
            final VideoEntity finalVideoInfo1 = videoInfo;
            ((VideoViewHolder) holder).imageView.setOnClickListener(view -> {
                if (selectVideo.contains(finalVideoInfo1.getFilePath())) {
                    selectVideo.remove(finalVideoInfo1.getFilePath());
                } else {
                    if (selectVideo.size() < videoPickerConfig.getMaxSize())
                        selectVideo.add(finalVideoInfo1.getFilePath());
                }
                if (onItemClickListener != null)
                    onItemClickListener.onVideoClick(selectVideo);
                notifyDataSetChanged();
            });
            initView(holder, videoInfo);
        }

    }

    private void initView(RecyclerView.ViewHolder holder, VideoEntity videoInfo) {
        if (videoPickerConfig.isMultiSelect()) {
            ((VideoViewHolder) holder).checkBox.setVisibility(View.VISIBLE);
        } else {
            ((VideoViewHolder) holder).checkBox.setVisibility(View.GONE);
        }
        if (selectVideo.contains(videoInfo.getFilePath())) {
            ((VideoViewHolder) holder).checkBox.setVisibility(View.VISIBLE);
            ((VideoViewHolder) holder).mask.setVisibility(View.VISIBLE);
            ((VideoViewHolder) holder).checkBox.setChecked(true);
            ((VideoViewHolder) holder).checkBox.setButtonDrawable(R.mipmap.picker_select_checked);
        } else {
            ((VideoViewHolder) holder).checkBox.setVisibility(View.VISIBLE);
            ((VideoViewHolder) holder).mask.setVisibility(View.GONE);
            ((VideoViewHolder) holder).checkBox.setChecked(false);
            ((VideoViewHolder) holder).checkBox.setButtonDrawable(R.mipmap.picker_select_unchecked);
        }
    }

    @Override
    public int getItemCount() {
        return videoInfoList.isEmpty() ? 0 : videoPickerConfig.isShowCamera() ? videoInfoList.size() + 1 : videoInfoList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            if (videoPickerConfig.isShowCamera()) {
                return HEAD;
            }
        }
        return PHOTO;
    }

    public void setVideoInfoList(List<VideoEntity> videoInfoList) {
        this.videoInfoList = videoInfoList;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onCameraClick(List<String> selectVideo);

        void onVideoClick(List<String> selectVideo);
    }

    private static class VideoViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private View mask;
        private CheckBox checkBox;

        public VideoViewHolder(View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.video_picker_im_photo);
            this.mask = itemView.findViewById(R.id.video_picker_v_photo_mask);
            this.checkBox = itemView.findViewById(R.id.video_picker_cb_select);
        }
    }

    private static class CameraViewHolder extends RecyclerView.ViewHolder {

        public CameraViewHolder(View itemView) {
            super(itemView);
        }
    }
}
