package com.zhangteng.filepicker;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.zhangteng.base.widget.MyTabLayout;
import com.zhangteng.filepicker.adapter.FilePickerAdapter;

public class FilePickerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyTabLayout myTabLayout = findViewById(R.id.mytablayout);
        ViewPager viewPager = findViewById(R.id.viewpager);
        FilePickerAdapter filePickerAdapter = new FilePickerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(filePickerAdapter);
        myTabLayout.setupWithViewPager(viewPager);
    }
}
