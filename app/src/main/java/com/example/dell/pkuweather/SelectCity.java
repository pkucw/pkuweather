package com.example.dell.pkuweather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.strictmode.CleartextNetworkViolation;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.app.Myapplication;
import com.example.dell.bean.City;

import java.util.ArrayList;
import java.util.List;

public class SelectCity extends Activity implements View.OnClickListener{
    private ImageView mBackBtn;
    private ListView mList = null;
    private TextView text_search = null;
    private List<City> listcity = Myapplication.getInstance().getCityList();
    private int listSize = listcity.size();
    private String updatecitycode="-1";


    private String[] city= new String[listSize];

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_city);

        mBackBtn = (ImageView) findViewById(R.id.title_back);
        mBackBtn.setOnClickListener(this);

        text_search = (EditText)findViewById(R.id.search_text);
        Log.i("city",listcity.get(1).getCity());
        for(int i=0;i<listSize;i++){
            city[i]=listcity.get(i).getCity();
            Log.d("city",city[i]);
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_single_choice,city);

        mList = (ListView)findViewById(R.id.title_list);
        mList.setAdapter(arrayAdapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(SelectCity.this,"你已选择："+city[i],
                        Toast.LENGTH_SHORT).show();
                text_search.setText("当前城市："+city[i]);
                //传递城市代码
                updatecitycode =listcity.get(i).getNumber();
            }
        });
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.title_back:
                Intent i = new Intent(this,MainActivity.class);
                i.putExtra("cityCode",updatecitycode);
                setResult(RESULT_OK,i);
                //startActivity(i);//??????????/写成这个就不对了必须写finish
                //Log.d("onClickselect",updatecitycode);
                finish();
                break;
            default:
                break;
        }
    }

    }

