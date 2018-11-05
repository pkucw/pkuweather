package com.example.dell.app;

import android.app.Activity;
import android.app.Application;
import android.os.Environment;
import android.util.Log;

import com.example.dell.bean.City;
import com.example.dell.db.CityDB;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

//创建类继承自Activity用于声明全局变量
public class Myapplication extends Application{
    private static final String TAG="MyApp";
    private static Myapplication mApplication;
    private CityDB mCityDB;
    //创建城市信息列表
    private List<City> mCityList;
    @Override
    public void onCreate(){
        super.onCreate();
        Log.d(TAG,"MyApplication->Oncreate");
        mApplication = this;
        //打开数据库
        mCityDB = openCityDB();
        //初始化城市信息列表
        initCityList();
    }
//初始化城市信息列表
    private void initCityList(){
        mCityList = new ArrayList<City>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                prepareCityList();
            }
        }).start();
    }
//向城市信息列表中传值（代码和名称）
    private boolean prepareCityList(){
        mCityList = mCityDB.getAllCity();
        int i=0;
        for(City city :mCityList){
            i++;
            String cityName = city.getCity();
            String cityCode = city.getNumber();
            Log.d(TAG,cityCode+":"+cityName);
        }
        Log.d(TAG,"i="+i);
        return true;
    }

    public List<City> getCityList(){
        return mCityList;
    }

    public static Myapplication getInstance(){
        return mApplication;
    }
//创建打开数据库的方法
    private CityDB openCityDB(){
        String path = "/data"
                + Environment.getDataDirectory().getAbsolutePath()
                + File.separator + getPackageName()
                + File.separator + "databases1"
                + File.separator
                + CityDB.CITY_DB_NAME;
        File db = new File(path);
        Log.d(TAG,path);
        if(!db.exists()){
            String pathfolder = "/data"
                    + Environment.getDataDirectory().getAbsolutePath()
                    + File.separator+getPackageName()
                    + File.separator+"databases1"
                    + File.separator;
            File dirFirstFolder = new File(pathfolder);
            if(!dirFirstFolder.exists()){
                dirFirstFolder.mkdirs();
                Log.i("MyApp","mkdirs");
            }
            Log.i("MyApp","db is not exists");
            try{
                InputStream is = getAssets().open("city.db");
                FileOutputStream fos = new FileOutputStream(db);
                int len = -1;
                byte[] buffer = new byte[1024];
                while ((len = is.read(buffer))!=-1){
                    fos.write(buffer,0,len);
                    fos.flush();
                }
                fos.close();
                is.close();
            }catch (IOException e){
                e.printStackTrace();
                System.exit(0);
            }
        }
        return new CityDB(this,path);

    }



}
