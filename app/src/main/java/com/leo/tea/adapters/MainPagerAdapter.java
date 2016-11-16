package com.leo.tea.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by my on 2016/11/11.
 */
public class MainPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList;

    public MainPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList=fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList!=null?fragmentList.size():0;
    }
    private String[] titles={"头条","百科","资讯","经营","数据"};
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
