package com.zhangteng.imagepicker.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.zhangteng.common.config.FilePickerConfig;
import com.zhangteng.imagepicker.R;
import com.zhangteng.searchfilelibrary.entity.ImageEntity;
import com.zhangteng.searchfilelibrary.entity.MediaEntity;
import com.zhangteng.searchfilelibrary.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by swing on 2018/4/17.
 */
public class ImagePickerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int HEAD = 0;
    private static final int PHOTO = 1;
    private Context mContext;
    private List<ImageEntity> imageInfoList;
    private FilePickerConfig imagePickerConfig = FilePickerConfig.getInstance();
    private List<MediaEntity> selectImage = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public ImagePickerAdapter(Context context, ArrayList<ImageEntity> imageInfoList) {
        this.mContext = context;
        this.imageInfoList = imageInfoList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == HEAD) {
            return new CameraViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_image_picker_camera, parent, false));
        } else {
            return new ImageViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_image_picker_photo, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        int heightOrWidth = Math.min(ScreenUtils.getScreenHeight(mContext) / 3, ScreenUtils.getScreenWidth(mContext) / 3);
        layoutParams.height = heightOrWidth;
        layoutParams.width = heightOrWidth;
        holder.itemView.setLayoutParams(layoutParams);
        ImageEntity imageInfo;
        if (imagePickerConfig.isShowCamera()) {
            if (position == 0) {
                ((CameraViewHolder) holder).itemView.setOnClickListener(view -> {
                    if (onItemClickListener != null) {
                        if (imagePickerConfig.isMultiSelect() && selectImage.size() < imagePickerConfig.getMaxSize()) {
                            onItemClickListener.onCameraClick(selectImage);
                        } else if (!imagePickerConfig.isMultiSelect() && selectImage.isEmpty()) {
                            onItemClickListener.onCameraClick(selectImage);
                        }
                    }
                    notifyDataSetChanged();
                });
            } else {
                imageInfo = imageInfoList.get(position - 1);
                imagePickerConfig.getImageLoader().loadImage(mContext, ((ImageViewHolder) holder).imageView, imageInfo.getFilePath());
                final ImageEntity finalImageInfo = imageInfo;
                ((ImageViewHolder) holder).imageView.setOnClickListener(view -> {
                    if (selectImage.contains(finalImageInfo)) {
                        selectImage.remove(finalImageInfo);
                    } else {
                        if (selectImage.size() < imagePickerConfig.getMaxSize()) {
                            selectImage.add(finalImageInfo);
                        }
                    }
                    if (onItemClickListener != null)
                        onItemClickListener.onImageClick(selectImage);
                    notifyDataSetChanged();
                });
                initView(holder, imageInfo);
            }
        } else {
            imageInfo = imageInfoList.get(position);
            imagePickerConfig.getImageLoader().loadImage(mContext, ((ImageViewHolder) holder).imageView, imageInfo.getFilePath());
            final ImageEntity finalImageInfo1 = imageInfo;
            ((ImageViewHolder) holder).imageView.setOnClickListener(view -> {
                if (selectImage.contains(finalImageInfo1)) {
                    selectImage.remove(finalImageInfo1);
                } else {
                    if (selectImage.size() < imagePickerConfig.getMaxSize())
                        selectImage.add(finalImageInfo1);
                }
                if (onItemClickListener != null)
                    onItemClickListener.onImageClick(selectImage);
                notifyDataSetChanged();
            });
            initView(holder, imageInfo);
        }

    }

    private void initView(RecyclerView.ViewHolder holder, ImageEntity imageInfo) {
        if (imagePickerConfig.isMultiSelect()) {
            ((ImageViewHolder) holder).checkBox.setVisibility(View.VISIBLE);
        } else {
            ((ImageViewHolder) holder).checkBox.setVisibility(View.GONE);
        }
        if (null == selectImage) selectImage = new ArrayList<>();
        boolean isContains = false;
        for (MediaEntity mediaEntity : selectImage) {
            if (null != mediaEntity
                    && null != mediaEntity.getFilePath()
                    && mediaEntity.getFilePath().equals(imageInfo.getFilePath())) {
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
        return imageInfoList.isEmpty() ? 0 : imagePickerConfig.isShowCamera() ? imageInfoList.size() + 1 : imageInfoList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            if (imagePickerConfig.isShowCamera()) {
                return HEAD;
            }
        }
        return PHOTO;
    }

    public void setImageInfoList(List<ImageEntity> imageInfoList) {
        this.imageInfoList = imageInfoList;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onCameraClick(List<MediaEntity> selectImage);

        void onImageClick(List<MediaEntity> selectImage);
    }

    private static class ImageViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private View mask;
        private CheckBox checkBox;

        public ImageViewHolder(View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.image_picker_im_photo);
            this.mask = itemView.findViewById(R.id.image_picker_v_photo_mask);
            this.checkBox = itemView.findViewById(R.id.image_picker_cb_select);
        }
    }

    private static class CameraViewHolder extends RecyclerView.ViewHolder {

        public CameraViewHolder(View itemView) {
            super(itemView);
        }
    }
}
