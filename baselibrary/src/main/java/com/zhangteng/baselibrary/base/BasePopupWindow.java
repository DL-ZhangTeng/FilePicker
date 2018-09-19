package com.zhangteng.baselibrary.base;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.zhangteng.baselibrary.R;

/**
 * Created by swing on 2018/9/6.
 */
public abstract class BasePopupWindow extends PopupWindow {
    protected LinearLayout clTitle;
    protected ConstraintLayout clContent;
    protected LinearLayout clButton;

    protected OnCancelClickListener onCancelClickListener;
    protected OnConfirmClickListener onConfirmClickListener;

    public BasePopupWindow(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.self_base_popupwindow, null);
        clTitle = view.findViewById(R.id.self_base_popupwindow_title);
        clContent = view.findViewById(R.id.self_base_popupwindow_content);
        clButton = view.findViewById(R.id.self_base_popupwindow_button);

        if (getSelfTitleView() != 0) {
            LayoutInflater.from(context).inflate(getSelfTitleView(), clTitle, true);
        }

        if (getSelfContentView() != 0) {
            LayoutInflater.from(context).inflate(getSelfContentView(), clContent, true);
        }

        if (getSelfButtonView() != 0) {
            LayoutInflater.from(context).inflate(getSelfButtonView(), clButton, true);
        }
        initView(view);
        this.setContentView(view);

        //设置高
        this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        //设置宽
        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置PopupWindow可触摸
        this.setTouchable(true);
        //设置非PopupWindow区域是否可触摸
        this.setOutsideTouchable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        this.setAnimationStyle(R.style.showAsDropDown);
        //防止被虚拟导航栏阻挡
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    public abstract int getSelfTitleView();

    public abstract int getSelfContentView();

    public abstract int getSelfButtonView();

    public abstract void initView(View view);

    public void setOnCancelClickListener(OnCancelClickListener onCancelClickListener) {
        this.onCancelClickListener = onCancelClickListener;
        if (clButton.getChildCount() > 0 && clButton.getChildAt(0) instanceof ViewGroup) {
            if (((ViewGroup) clButton.getChildAt(0)).getChildAt(0) != null) {
                ((ViewGroup) clButton.getChildAt(0)).getChildAt(0).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (BasePopupWindow.this.onCancelClickListener != null) {
                            BasePopupWindow.this.onCancelClickListener.onCancel(v);
                        }
                    }
                });
            }
        }
    }

    public void setOnConfirmClickListener(OnConfirmClickListener onConfirmClickListener) {
        this.onConfirmClickListener = onConfirmClickListener;
        if (clButton.getChildCount() > 0 && clButton.getChildAt(0) instanceof ViewGroup) {
            if (((ViewGroup) clButton.getChildAt(0)).getChildAt(((ViewGroup) clButton.getChildAt(0)).getChildCount() - 1) != null) {
                ((ViewGroup) clButton.getChildAt(0)).getChildAt(((ViewGroup) clButton.getChildAt(0)).getChildCount() - 1).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (BasePopupWindow.this.onConfirmClickListener != null) {
                            BasePopupWindow.this.onConfirmClickListener.onConfirm(v);
                        }
                    }
                });
            }
        }
    }

    @Override
    public View getContentView() {
        return super.getContentView();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
    }

    @Override
    public void setAnimationStyle(int animationStyle) {
        super.setAnimationStyle(animationStyle);
    }

    @Override
    public void setBackgroundDrawable(Drawable background) {
        super.setBackgroundDrawable(background);
    }

    @Override
    public void showAsDropDown(View view) {
        super.showAsDropDown(view);
    }

    public interface OnCancelClickListener {
        void onCancel(View view);
    }

    public interface OnConfirmClickListener {
        void onConfirm(View view);
    }
}
