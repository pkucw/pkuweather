package com.example.dell.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.dell.bean.City;

import java.util.ArrayList;
import java.util.List;

public class CityDB {
    public static final String CITY_DB_NAME ="city.db";
    private static final String CITY_TABLE_NAME ="city";
    //创建数据库
    private SQLiteDatabase db;

    public CityDB(Context context,String path){
        //创建新的数据库语句
        db = context.openOrCreateDatabase(path,Context.MODE_PRIVATE,null);
    }
//泛型，存放城市的所有相关信息
    public List<City> getAllCity(){
        List<City> list = new ArrayList<City>();
        //rawQuery查询信息
        Cursor c=db.rawQuery("SELECT * from " + CITY_TABLE_NAME,null);
        //遍历查询
        while (c.moveToNext()){
            String province = c.getString(c.getColumnIndex("province"));
            String city = c.getString(c.getColumnIndex("city"));
            String number = c.getString(c.getColumnIndex("number"));
            String allPY = c.getString(c.getColumnIndex("allpy"));
            String allFirstPY = c.getString(c.getColumnIndex("allfirstpy"));
            String firstPY = c.getString(c.getColumnIndex("firstpy"));
            City item = new City(province,city,number,firstPY,allFirstPY,allPY);
            list.add(item);
        }
        return list;
    }
}
