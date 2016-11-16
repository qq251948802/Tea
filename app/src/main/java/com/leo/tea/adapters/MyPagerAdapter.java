package com.leo.tea.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by my on 2016/11/14.
 */
public class MyPagerAdapter extends PagerAdapter {
    private List<ImageView> headerImg;
    public MyPagerAdapter(List<ImageView> headerImg, Context context) {

        this.headerImg=headerImg;

    }

    @Override
    public int getCount() {
        //return headerImg!=null?headerImg.size():0;
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if(headerImg.size()!=0){
            Log.d("flag", "----------->得到的数据为instantiateItem:11"+position);

                ImageView child = headerImg.get(position % 3);
            ViewGroup parent = (ViewGroup) child.getParent();
            if(parent !=null){
                parent.removeView(child);
            }
            container.addView(child);

            return headerImg.get(position%3);
        }
        return null;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if(headerImg.size()!=0){

            container.removeView(headerImg.get(position%3));
        }
    }
}
