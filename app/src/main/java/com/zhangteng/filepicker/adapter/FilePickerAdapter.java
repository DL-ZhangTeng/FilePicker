package com.zhangteng.filepicker.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.zhangteng.audiopicker.fragment.AudioPickerFragment;
import com.zhangteng.documentpicker.fragment.DocumentPickerFragment;
import com.zhangteng.folderpicker.fragment.FolderPickerFragment;
import com.zhangteng.imagepicker.fragment.ImagePickerFragment;
import com.zhangteng.rarpicker.fragment.RarPickerFragment;
import com.zhangteng.videopicker.fragment.VideoPickerFragment;

import java.util.ArrayList;

/**
 * Created by Lanxumit on 2017/11/24.
 */

public class FilePickerAdapter extends FragmentPagerAdapter {
    private final String[] titles = {"image", "video", "audio", "rar", "document", "folder"};
    private final ArrayList<Fragment> fragmentlist = new ArrayList<Fragment>();

    public FilePickerAdapter(FragmentManager fm) {
        super(fm);
    }

    public ArrayList<Fragment> getFragmentList() {
        return fragmentlist;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new ImagePickerFragment();
                break;
            case 1:
                fragment = new VideoPickerFragment();
                break;
            case 2:
                fragment = new AudioPickerFragment();
                break;
            case 3:
                fragment = new RarPickerFragment();
                break;
            case 4:
                fragment = new DocumentPickerFragment();
                break;
            case 5:
                fragment = new FolderPickerFragment();
                break;
            default:
                break;
        }
        fragmentlist.add(fragment);
        return fragment;
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
