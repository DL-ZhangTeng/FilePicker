package com.zhangteng.filepicker.utils;

import android.graphics.Paint;
import android.graphics.Rect;
import android.widget.TextView;

/**
 * Created by swing on 2019/7/31 0031.
 */
public class TextUtil {
    /**
     * 计算textview中文本高度
     */
    public static int getTextHeight(TextView textView) {
        Rect bounds = new Rect();
        Paint mTextPaint = textView.getPaint();
        String mText = textView.getText().toString();
        mTextPaint.getTextBounds(mText, 0, mText.length(), bounds);
        return bounds.height();
    }

    /**
     * 计算textview中文本宽度
     */
    public static float getTextWidthF(TextView textView) {
        float textWidth = textView.getPaint().measureText(textView.getText().toString());
        return textWidth;
    }

    /**
     * 计算textview中文本宽度
     */
    public static int getTextWidth(TextView textView) {
        int textWidth = (int) android.text.Layout.getDesiredWidth(textView.getText(), textView.getPaint());
        return textWidth;
    }
}
