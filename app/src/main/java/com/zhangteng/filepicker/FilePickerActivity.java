package com.zhangteng.filepicker;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.zhangteng.androidpermission.AndroidPermission;
import com.zhangteng.androidpermission.Permission;
import com.zhangteng.androidpermission.callback.Callback;
import com.zhangteng.audiopicker.fragment.AudioPickerFragment;
import com.zhangteng.base.widget.MyTabLayout;
import com.zhangteng.documentpicker.fragment.DocumentPickerFragment;
import com.zhangteng.filepicker.adapter.FilePickerAdapter;
import com.zhangteng.folderpicker.fragment.FolderPickerFragment;
import com.zhangteng.imagepicker.fragment.ImagePickerFragment;
import com.zhangteng.rarpicker.fragment.RarPickerFragment;
import com.zhangteng.videopicker.fragment.VideoPickerFragment;

import java.util.ArrayList;

public class FilePickerActivity extends AppCompatActivity {
    private final ArrayList<Fragment> fragments = new ArrayList<Fragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyTabLayout myTabLayout = findViewById(R.id.mytablayout);
        ViewPager viewPager = findViewById(R.id.viewpager);

        fragments.add(new ImagePickerFragment());
        fragments.add(new VideoPickerFragment());
        fragments.add(new AudioPickerFragment());
        fragments.add(new RarPickerFragment());
        fragments.add(new DocumentPickerFragment());
        fragments.add(new FolderPickerFragment());

        AndroidPermission androidPermission = new AndroidPermission.Buidler()
                .with(FilePickerActivity.this)
                .permission(Permission.Group.STORAGE_R)
                .callback(new Callback() {
                    @Override
                    public void success(Activity permissionActivity) {
                        FilePickerAdapter filePickerAdapter = new FilePickerAdapter(getSupportFragmentManager(), fragments);
                        viewPager.setAdapter(filePickerAdapter);
                        viewPager.setOffscreenPageLimit(5);
                        myTabLayout.setupWithViewPager(viewPager);
                    }

                    @Override
                    public void failure(Activity permissionActivity) {
                        Toast.makeText(FilePickerActivity.this, "请开启相机权限！", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void nonExecution(Activity permissionActivity) {
                        //权限已通过，请求未执行
                        success(permissionActivity);
                    }
                })
                .build();
        androidPermission.execute();
    }
}
