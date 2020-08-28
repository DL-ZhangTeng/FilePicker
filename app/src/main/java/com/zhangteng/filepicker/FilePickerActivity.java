package com.zhangteng.filepicker;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.zhangteng.base.widget.MyTabLayout;
import com.zhangteng.filepicker.adapter.FilePickerAdapter;

public class FilePickerActivity extends AppCompatActivity {

    private MyTabLayout myTabLayout;
    private ViewPager viewPager;
    private FilePickerAdapter filePickerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_picker);
        myTabLayout = findViewById(R.id.mytablayout);
        viewPager = findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(6);
        filePickerAdapter = new FilePickerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(filePickerAdapter);
        myTabLayout.setupWithViewPager(viewPager);
    }
}
