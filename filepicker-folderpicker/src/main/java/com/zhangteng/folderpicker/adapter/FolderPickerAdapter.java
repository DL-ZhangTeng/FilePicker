package com.zhangteng.folderpicker.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhangteng.common.config.FilePickerConfig;
import com.zhangteng.folderpicker.R;
import com.zhangteng.searchfilelibrary.config.SearchCofig;
import com.zhangteng.searchfilelibrary.entity.MediaEntity;
import com.zhangteng.searchfilelibrary.utils.DateUtils;
import com.zhangteng.searchfilelibrary.utils.DensityUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by swing on 2018/4/17.
 */
public class FolderPickerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private String previousPath = SearchCofig.BASE_SD_PATH;
    private List<MediaEntity> folderInfoList;
    private FilePickerConfig folderPickerConfig = FilePickerConfig.getInstance();
    private List<String> selectFolder = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    private boolean hasPreviousBtn = false;

    public FolderPickerAdapter(Context context, ArrayList<MediaEntity> folderInfoList) {
        this.mContext = context;
        this.folderInfoList = folderInfoList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImageViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_folder_picker_document, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        MediaEntity folderInfo;
        if (hasPreviousBtn && position == 0) {
            ((ImageViewHolder) holder).name.setText("返回上一级");
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ((ImageViewHolder) holder).name.getLayoutParams();
            layoutParams.setMargins(0, DensityUtil.dp2px(((ImageViewHolder) holder).name.getContext(), 40), 0, 0);
            ((ImageViewHolder) holder).name.setLayoutParams(layoutParams);
            ((ImageViewHolder) holder).imageView.setImageResource(FilePickerConfig.getInstance().getIconResources(MediaEntity.MEDIA_FOLDER));
            ((ImageViewHolder) holder).time.setVisibility(View.GONE);
            ((ImageViewHolder) holder).size.setVisibility(View.GONE);
            ((ImageViewHolder) holder).checkBox.setVisibility(View.GONE);
            ((ImageViewHolder) holder).mask.setVisibility(View.GONE);
            holder.itemView.setOnClickListener(view -> {
                if (onItemClickListener != null && null != previousPath && !previousPath.isEmpty()) {
                    File previousFile = new File(previousPath);
                    if (previousFile.exists()) {
                        onItemClickListener.onPreviousFolder(previousPath);
                        if (!SearchCofig.BASE_SD_PATH.equals(previousPath)) {
                            previousPath = previousFile.getParent();
                        }
                    }
                }
            });
            return;
        }
        folderInfo = folderInfoList.get(hasPreviousBtn ? position - 1 : position);
        ((ImageViewHolder) holder).name.setText(folderInfo.getFileName());
        ((ImageViewHolder) holder).time.setText(DateUtils.getTime(folderInfo.getUpdateTime(), DateUtils.FORMAT_YMD));//folderInfo.getTime()音频持续时间
        ((ImageViewHolder) holder).size.setText(mContext.getString(R.string.folder_picker_folder_size, folderInfo.getFileLength() / 1024));
        switch (folderInfo.getMediaType()) {
            case MediaEntity.MEDIA_APK:
            case MediaEntity.MEDIA_ZIP:
                initView(holder, folderInfo);
                initClick(((ImageViewHolder) holder).itemView, folderInfo);
                ((ImageViewHolder) holder).imageView.setImageResource(FilePickerConfig.getInstance().getIconResources(MediaEntity.MEDIA_ZIP));
                break;
            case MediaEntity.MEDIA_PDF:
            case MediaEntity.MEDIA_DOC:
            case MediaEntity.MEDIA_PPT:
            case MediaEntity.MEDIA_EXCEL:
            case MediaEntity.MEDIA_TXT:
            case MediaEntity.MEDIA_DOCUMENT:
                initView(holder, folderInfo);
                initClick(((ImageViewHolder) holder).itemView, folderInfo);
                ((ImageViewHolder) holder).imageView.setImageResource(FilePickerConfig.getInstance().getIconResources(MediaEntity.MEDIA_DOCUMENT));
                break;
            case MediaEntity.MEDIA_AUDIO:
                initView(holder, folderInfo);
                initClick(((ImageViewHolder) holder).itemView, folderInfo);
                ((ImageViewHolder) holder).imageView.setImageResource(FilePickerConfig.getInstance().getIconResources(MediaEntity.MEDIA_AUDIO));
                break;
            case MediaEntity.MEDIA_IMAGE:
                initView(holder, folderInfo);
                initClick(((ImageViewHolder) holder).itemView, folderInfo);
                ((ImageViewHolder) holder).imageView.setImageResource(FilePickerConfig.getInstance().getIconResources(MediaEntity.MEDIA_IMAGE));
                break;
            case MediaEntity.MEDIA_VIDEO:
                initView(holder, folderInfo);
                initClick(((ImageViewHolder) holder).itemView, folderInfo);
                ((ImageViewHolder) holder).imageView.setImageResource(FilePickerConfig.getInstance().getIconResources(MediaEntity.MEDIA_VIDEO));
                break;
            case MediaEntity.MEDIA_FOLDER:
                initView(holder, null);
                initClick(((ImageViewHolder) holder).itemView, folderInfo);
                ((ImageViewHolder) holder).imageView.setImageResource(FilePickerConfig.getInstance().getIconResources(MediaEntity.MEDIA_FOLDER));
                break;
            case MediaEntity.MEDIA_UNKNOWN:
                initView(holder, null);
                initClick(((ImageViewHolder) holder).itemView, null);
                ((ImageViewHolder) holder).imageView.setImageResource(FilePickerConfig.getInstance().getIconResources(MediaEntity.MEDIA_UNKNOWN));
                break;
            default:
                initView(holder, null);
                initClick(((ImageViewHolder) holder).itemView, null);
                folderPickerConfig.getImageLoader().loadImage(mContext, ((ImageViewHolder) holder).imageView, folderInfo.getThumPath());
                break;
        }

    }

    private void initClick(View itemView, final MediaEntity finalFolderInfo) {
        itemView.setOnClickListener(view -> {
            if (finalFolderInfo != null) {
                if (finalFolderInfo.getMediaType() == MediaEntity.MEDIA_FOLDER) {
                    if (onItemClickListener != null) {
                        previousPath = new File(finalFolderInfo.getFilePath()).getParent();
                        onItemClickListener.onNextFolder(finalFolderInfo.getFilePath());
                    }
                } else {
                    if (selectFolder.contains(finalFolderInfo.getFilePath())) {
                        selectFolder.remove(finalFolderInfo.getFilePath());
                    } else {
                        if (selectFolder.size() < folderPickerConfig.getMaxSize())
                            selectFolder.add(finalFolderInfo.getFilePath());
                    }
                    if (onItemClickListener != null)
                        onItemClickListener.onImageClick(selectFolder);
                }
                notifyDataSetChanged();
            }
        });
    }

    private void initView(RecyclerView.ViewHolder holder, MediaEntity folderInfo) {
        if (folderPickerConfig.isMultiSelect()) {
            ((ImageViewHolder) holder).checkBox.setVisibility(View.VISIBLE);
        } else {
            ((ImageViewHolder) holder).checkBox.setVisibility(View.GONE);
        }
        if (folderInfo != null) {
            if (selectFolder.contains(folderInfo.getFilePath())) {
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
        } else {
            ((ImageViewHolder) holder).checkBox.setVisibility(View.GONE);
            ((ImageViewHolder) holder).mask.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if (hasPreviousBtn) {
            return folderInfoList.isEmpty() ? 1 : folderInfoList.size() + 1;
        }
        return folderInfoList.isEmpty() ? 0 : folderInfoList.size();
    }

    public void setFolderInfoList(List<MediaEntity> folderInfoList) {
        this.folderInfoList = folderInfoList;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        notifyDataSetChanged();
    }

    public void setHasPreviousBtn(boolean hasPreviousBtn) {
        this.hasPreviousBtn = hasPreviousBtn;
    }

    public interface OnItemClickListener {
        void onImageClick(List<String> selectImage);

        void onNextFolder(String nextPath);

        void onPreviousFolder(String previousPaht);
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
            this.imageView = itemView.findViewById(R.id.folder_picker_iv_folder_image);
            this.mask = itemView.findViewById(R.id.folder_picker_v_photo_mask);
            this.checkBox = itemView.findViewById(R.id.folder_picker_cb_select);
            this.name = itemView.findViewById(R.id.folder_picker_tv_folder_name);
            this.time = itemView.findViewById(R.id.folder_picker_tv_folder_time);
            this.size = itemView.findViewById(R.id.folder_picker_tv_folder_size);
        }
    }
}
