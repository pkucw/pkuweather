package com.example.dell.pkuweather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.strictmode.CleartextNetworkViolation;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.dell.app.Myapplication;
import com.example.dell.bean.City;

import java.util.List;

public class SelectCity extends Activity implements View.OnClickListener{
    private ImageView mBackBtn;

    //private Myadapter myadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_city);

       /* ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                SelectCity.this,android.R.layout.simple_list_item_1,list);
        ListView listView = (listView) findViewById(R.id.title_list);
        listView.setAdapter(adapter);*/

        mBackBtn = (ImageView) findViewById(R.id.title_back);
        mBackBtn.setOnClickListener(this);

        //initViews();
    }

   /* private void initViews(){
        mBackBtn = (ImageView)findViewById(R.id.title_back);
        mBackBtn.setOnClickListener(this);

        //mClearEditText = (ClearEditText)findViewById(R.id.search_city);

        mCityList=(ListView)findViewById(R.id.title_list);
        Myapplication myapplication = (Myapplication) getApplication();
        list = myapplication.getCityList();
        for(City city : cityList){
            filterDateList.add(city);
        }

        myadapter = new Myadapter(SelectCity.this,cityList);
        mList.setAdapter(myadapter);
        mList.setOnIterClickListner(new AdapterView.OnClickListener(){
            @Override
            public void onItemClick(AdapterView<?>adapterView,View view,int position,long l){
                City city = filterDataList.get(position);
                Intent i = new Intent();
                i.putExtra("cityCode",city.getNumber());
                setResult(RESULT_OK,i);
                finish();
            }
        });*/


    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.title_back:
                Intent i = new Intent();
                i.putExtra("cityCode","101160101");
                setResult(RESULT_OK,i);
                finish();
                break;
            default:
                break;
        }
    }
}
