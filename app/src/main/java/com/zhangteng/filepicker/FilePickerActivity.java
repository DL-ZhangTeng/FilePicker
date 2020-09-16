package com.zhangteng.filepicker;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.zhangteng.filepicker.adapter.FilePickerAdapter;
import com.zhangteng.filepicker.widget.MyTabLayout;

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
