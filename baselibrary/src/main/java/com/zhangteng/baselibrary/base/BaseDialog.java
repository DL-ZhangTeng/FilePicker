package com.zhangteng.baselibrary.base;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.zhangteng.baselibrary.R;

/**
 * Created by swing on 2018/9/6.
 */
public abstract class BaseDialog extends Dialog {
    protected LinearLayout clTitle;
    protected ConstraintLayout clContent;
    protected LinearLayout clButton;

    protected OnCancelClickListener onCancelClickListener;
    protected OnConfirmClickListener onConfirmClickListener;

    public BaseDialog(@NonNull Context context) {
        this(context, R.style.SelfDialog);
    }

    public BaseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.self_base_dialog, null);
        clTitle = view.findViewById(R.id.self_base_dialog_title);
        clContent = view.findViewById(R.id.self_base_dialog_content);
        clButton = view.findViewById(R.id.self_base_dialog_button);
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
        setContentView(view);
    }

    public abstract int getSelfTitleView();

    public abstract int getSelfContentView();

    public abstract int getSelfButtonView();

    public abstract void initView(View view);

    public void setClTitle(View view) {
        clTitle.removeAllViews();
        if (view != null) {
            clTitle.addView(view);
        }
    }

    public void setClContent(View view) {
        clContent.removeAllViews();
        if (view != null) {
            clContent.addView(view);
        }
    }

    public void setClButton(View view) {
        clButton.removeAllViews();
        if (view != null) {
            clButton.addView(view);
        }
    }

    public void setOnCancelClickListener(OnCancelClickListener onCancelClickListener) {
        this.onCancelClickListener = onCancelClickListener;
        if (clButton.getChildCount() > 0 && clButton.getChildAt(0) instanceof ViewGroup) {
            if (((ViewGroup) clButton.getChildAt(0)).getChildAt(0) != null) {
                ((ViewGroup) clButton.getChildAt(0)).getChildAt(0).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (BaseDialog.this.onCancelClickListener != null) {
                            BaseDialog.this.onCancelClickListener.onCancel(v);
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
                        if (BaseDialog.this.onConfirmClickListener != null) {
                            BaseDialog.this.onConfirmClickListener.onConfirm(v);
                        }
                    }
                });
            }
        }
    }

    public interface OnCancelClickListener {
        void onCancel(View view);
    }

    public interface OnConfirmClickListener {
        void onConfirm(View view);
    }
}
