package com.zhangteng.filepicker;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.zhangteng.filepicker.adapter.MainAdapter;
import com.zhangteng.filepicker.widget.MyTabLayout;

public class MainActivity extends AppCompatActivity {

    private MyTabLayout myTabLayout;
    private ViewPager viewPager;
    private MainAdapter mainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myTabLayout = findViewById(R.id.mytablayout);
        viewPager = findViewById(R.id.viewpager);
        mainAdapter = new MainAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mainAdapter);
        myTabLayout.setupWithViewPager(viewPager);
    }
}
