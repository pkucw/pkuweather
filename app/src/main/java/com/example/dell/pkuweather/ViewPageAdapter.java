package com.example.dell.pkuweather;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewGroupCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

public class ViewPageAdapter extends PagerAdapter {
    private List<View> pagerview;
    private Context context;
    public ViewPageAdapter(List<View> pagerview,Context context){
        this.context = context;
        this.pagerview = pagerview;
    }

    //返回滑动页面页码
    @Override
    public Object instantiateItem(ViewGroup container,int position){
        container.addView(pagerview.get(position));
        return pagerview.get(position);
    }

    //返回滑动页面大小
    @Override
    public int getCount(){
        return pagerview.size();
    }

    @Override
    public void destroyItem(ViewGroup container,int position,Object object){
        container.removeViewAt(position);
    }

    @Override
    public boolean isViewFromObject(View view,Object object){
        return (view == object);
    }
}
