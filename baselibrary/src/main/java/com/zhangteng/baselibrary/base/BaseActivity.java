package com.zhangteng.baselibrary.base;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.zhangteng.baselibrary.R;
import com.zhangteng.baselibrary.utils.ActivityHelper;
import com.zhangteng.baselibrary.utils.AppManager;


/**
 * Created by swing on 2017/11/23.
 */

public abstract class BaseActivity extends AppCompatActivity {

    public LinearLayout rootLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 经测试在代码里直接声明透明状态栏更有效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        super.setContentView(R.layout.activity_base);
        AppManager.addActivity(this);
        setContentView(getResourceId());
        initView();
        initData();
    }

    protected abstract int getResourceId();

    protected abstract void initView();

    protected abstract void initData();

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        setContentView(View.inflate(this, layoutResID, null));
    }

    @Override
    public void setContentView(View view) {
        rootLayout = (LinearLayout) findViewById(R.id.root_layout);
        if (rootLayout == null) {
            return;
        }
        //if(this instanceof SplashActivity || this instanceof LoginActivity || this instanceof NavigationActivity){
        //    rootLayout.removeViewAt(0);
        //}
        rootLayout.addView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
    }

    public void buttonClick(View v) {
        int i = v.getId();
        if (i == R.id.titlebar_left_back) {
            goBack();
        } else {
            localButtonClick(v);
        }
    }


    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void showToast(int mesageId) {
        Toast.makeText(this, mesageId, Toast.LENGTH_SHORT).show();
    }

    public void localButtonClick(View v) {

    }

    @Override
    public void startActivity(Intent intent) {
        try {
            super.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            showToast("未找到相应应用");
        }
    }


    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        try {
            super.startActivityForResult(intent, requestCode);
        } catch (ActivityNotFoundException e) {
            showToast("未找到相应应用");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            goBack();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    public void goBack() {
        ActivityHelper.setActivityAnimClose(this);
    }
}

