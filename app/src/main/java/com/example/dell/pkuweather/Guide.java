package com.example.dell.pkuweather;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import java.util.ArrayList;
import java.util.List;

public class Guide extends Activity {
    private Button benter;
    private List<View> pagerview;
    private ViewPageAdapter viewPageAdapter;
    private ViewPager viewPager;
    @Override
    protected void  onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide);
        initpagerview();
        benter=(Button)pagerview.get(2).findViewById(R.id.enter);
        benter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击后跳转至MainActivity事件
                Intent intent = new Intent(Guide.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    private void initpagerview(){
        //实例化xml文件
        LayoutInflater lf = LayoutInflater.from(this);
        pagerview = new ArrayList<View>();
        //通过LayoutInflater.inflate动态加载xml文件
        pagerview.add(lf.inflate(R.layout.guide_1,null));
        pagerview.add(lf.inflate(R.layout.guide_2,null));
        pagerview.add(lf.inflate(R.layout.guide_3,null));
        viewPageAdapter = new ViewPageAdapter(pagerview,this);
        viewPager = (ViewPager)findViewById(R.id.viewpager);
        viewPager.setAdapter(viewPageAdapter);
    }


}
