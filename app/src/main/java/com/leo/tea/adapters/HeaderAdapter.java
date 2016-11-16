package com.leo.tea.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by my on 2016/11/11.
 */
public class HeaderAdapter extends FragmentPagerAdapter {

    List<Fragment> headerViewPagerFragment;

    public HeaderAdapter(FragmentManager fragmentManager, List<Fragment> headerViewPagerFragment) {
        super(fragmentManager);
        this.headerViewPagerFragment=headerViewPagerFragment;
    }

    @Override
    public Fragment getItem(int position) {
        return headerViewPagerFragment.get(position);
    }

    @Override
    public int getCount() {
        return headerViewPagerFragment!=null?headerViewPagerFragment.size():0;
    }

}
