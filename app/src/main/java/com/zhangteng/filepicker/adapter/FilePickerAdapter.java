package com.zhangteng.filepicker.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Swing on 2017/11/24.
 */

public class FilePickerAdapter extends FragmentPagerAdapter {
    private final String[] titles = {"图片", "视频", "音频", "压缩包", "文档", "文件"};
    private final ArrayList<Fragment> fragments;

    public FilePickerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return titles.length;
    }
}
