package com.example.dell.pkuweather;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.bean.TodayWeather;
import com.example.dell.util.NetUtil;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

public class MainActivity extends Activity implements View.OnClickListener {//该类为主界面
    private static final int UPDATE_TODAY_WEATHER = 1;
    private ImageView mUpdateBtn;//更新按钮
    private ImageView mCitySelect;//选择城市
    private TextView cityTv, timeTv, humidityTv, weekTv, pmDataTv, pmQualityTv,
            temperatureTv, climateTv, windTv, city_name_Tv,wenduTv,fengxiangTv;
    private ImageView weatherImg, pmImg;



    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg){
            switch (msg.what){
                case UPDATE_TODAY_WEATHER:
                    updateTodayWeather((TodayWeather)msg.obj);
                    break;
                default:
                    break;
            }
        }

    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);//继承父类方法，savedInstanceState保存当前状态
        setContentView(R.layout.weather_info);//为当前活动引入weather_info布局

        initViewlist();

    //引用布局文件中的title_update_btn
        mUpdateBtn = (ImageView) findViewById(R.id.title_update_btn);
    //监听事件
        mUpdateBtn.setOnClickListener(this);
    //将网络状态显示在控制台以及界面上
        if (NetUtil.getNetworkState(this) != NetUtil.NETWORK_NONE) {
            Log.d("myWeather", "网络OK");
            Toast.makeText(MainActivity.this, "网络OK!!!", Toast.LENGTH_LONG).show();

        } else {
            Log.d("myWeather", "网络挂了");

            Toast.makeText(MainActivity.this, "网络挂了！", Toast.LENGTH_LONG).show();
        }

        mCitySelect=(ImageView)findViewById(R.id.title_city_manager);
        mCitySelect.setOnClickListener(this);


        initView();
    }

    //初始化控件内容
    void initView() {
        city_name_Tv = (TextView) findViewById(R.id.title_city_name);
        cityTv = (TextView) findViewById(R.id.city);
        timeTv = (TextView) findViewById(R.id.time);
        //湿度
        humidityTv = (TextView) findViewById(R.id.humidity);
        weekTv = (TextView) findViewById(R.id.week_today);
        pmDataTv = (TextView) findViewById(R.id.pm_data);
        pmQualityTv = (TextView) findViewById(R.id.pm2_5_quality);
        //初始化pm2_5图片内容
        pmImg = (ImageView) findViewById(R.id.pm2_5_img);
        temperatureTv = (TextView) findViewById(R.id.temperature);
        climateTv = (TextView) findViewById(R.id.climate);
        windTv = (TextView) findViewById(R.id.wind);
        //初始化天气图片内容
        weatherImg = (ImageView) findViewById(R.id.weather_img);
        wenduTv=(TextView)findViewById(R.id.wendu);
        fengxiangTv=(TextView)findViewById(R.id.fengxiang);


        city_name_Tv.setText("N/A");
        cityTv.setText("N/A");
        timeTv.setText("N/A");
        humidityTv.setText("N/A");
        pmDataTv.setText("N/A");
        pmQualityTv.setText("N/A");
        pmImg.setImageDrawable(null);
        weatherImg.setImageDrawable(null);
        weekTv.setText("N/A");
        temperatureTv.setText("N/A");
        climateTv.setText("N/A");
        windTv.setText("N/A");
        wenduTv.setText("N/A");
        fengxiangTv.setText("N/A");


    }

    //编写initViewlist方法实现城市列表的展示
    private void initViewlist(){
        //为mBackBtn设置监听事件

    }

    @Override
    public void onClick(View view) {
    //监听转换城市按钮
        if(view.getId()==R.id.title_city_manager){
            //创建一个意图对象
            Intent i = new Intent(this,SelectCity.class);
            //得到新打开Activity关闭后返回的数据
            startActivityForResult(i,1);
        }
    //监听当前城市数据更新按钮
        if (view.getId() == R.id.title_update_btn) {
        //从SharedPreferences中读取城市ID，第一个参数指定文件，第二个参数指定操作模式（只有当前程序可操作）
            SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
            String cityCode = sharedPreferences.getString("main_city_code", "101010100");
            Log.d("myWeather", cityCode);

            if (NetUtil.getNetworkState(this) != NetUtil.NETWORK_NONE) {
                Log.d("myWeather", "网络ok");
                queryWeatherCode(cityCode);
            } else {
                Log.d("myWeather", "网络false");
                Toast.makeText(MainActivity.this, "网络挂了", Toast.LENGTH_SHORT).show();
            }
        }
    }

//为了得到intent重新回传的数据，需要重写onActivity方法
//requestCode 请求码，即调用startActivityForResult()传递过去的值
// resultCode 结果码，结果码用于标识返回数据来自哪个新Activity
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        if(requestCode == 1 && resultCode ==RESULT_OK){
            String newCityCode = data.getStringExtra("cityCode");
            Log.d("myWeather","选择的城市代码为"+newCityCode);
            if(NetUtil.getNetworkState(this)!=NetUtil.NETWORK_NONE){
                Log.d("myWeather","网络OK");
                queryWeatherCode(newCityCode);
            }else {
                Log.d("myWeather","网络挂了");
                Toast.makeText(MainActivity.this,"网络挂了",Toast.LENGTH_LONG).show();
            }
        }
    }
    


    /**
     * @param cityCode 关于cityCode的参数说明
     */



//获取网络数据
    private void queryWeatherCode(String cityCode) {
        final String address = "http://wthrcdn.etouch.cn/WeatherApi?citykey=101010100";

        Log.d("myWeather", address);
    //子线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection con = null;
                TodayWeather todayWeather = null;
                try {
                //请求相应流程begin
                    //读取初始URL
                    URL url = new URL(address);
                    //打开URL
                    con = (HttpURLConnection) url.openConnection();
                    //链接设置
                    con.setRequestMethod("GET");
                    con.setConnectTimeout(8000);
                    con.setReadTimeout(8000);
                    //读取网络内容
                    InputStream in = con.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String str;
                    while ((str = reader.readLine()) != null) {
                        response.append(str);
                        Log.d("myWeather", str);
                    }
                    String responseStr = response.toString();
                    Log.d("myWeather", responseStr);
                    //调用parseXML
                    todayWeather = parseXML(responseStr);
                    if (todayWeather != null) {
                        Log.d("myWeather", todayWeather.toString());

                        Message msg = new Message();
                        msg.what = UPDATE_TODAY_WEATHER;
                        msg.obj = todayWeather;
                        mHandler.sendMessage(msg);
                    }
                    //parseXML(responseStr);
                } catch (Exception e) {
                //打印异常信息
                    e.printStackTrace();
                } finally {
                    if (con != null) {
                        con.disconnect();
                    }
                }
            }
        }).start();
    }

//解析城市名称已经的更新时间信息
    private TodayWeather parseXML(String xmldata) {
        TodayWeather todayWeather = null;
        int fengxiangCount = 0;
        int fengliCount = 0;
        int dateCount = 0;
        int highCount = 0;
        int lowCount = 0;
        int typeCount = 0;
        try {
            //获取XmlPullParser对象
            XmlPullParserFactory fac = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = fac.newPullParser();
            //设置解析器的输入
            xmlPullParser.setInput(new StringReader(xmldata));
            //开始解析
            int eventType = xmlPullParser.getEventType();
            Log.d("myWeather", "parseXML");
            //当文档没有结束的时候
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    //判断当前事件是否为文档开始事件
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    //判断当前事件是否为标签元素开始事件
                    case XmlPullParser.START_TAG:
                        if (xmlPullParser.getName().equals("resp")) {
                            todayWeather = new TodayWeather();
                        }
                        if (todayWeather != null) {

                            if (xmlPullParser.getName().equals("city")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setCity(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("updatetime")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setUpdatetime(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("shidu")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setShidu(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("wendu")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setWendu(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("pm25")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setPm25(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("quality")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setQuality(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("fengxiang") && fengxiangCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFengxiang(xmlPullParser.getText());
                                fengxiangCount++;
                            } else if (xmlPullParser.getName().equals("fengli") && fengliCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFengli(xmlPullParser.getText());
                                fengliCount++;
                            } else if (xmlPullParser.getName().equals("data") && dateCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setDate(xmlPullParser.getText());
                                dateCount++;
                            } else if (xmlPullParser.getName().equals("high") && highCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setHigh(xmlPullParser.getText().substring(2).trim());
                                highCount++;
                            } else if (xmlPullParser.getName().equals("low") && lowCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setLow(xmlPullParser.getText().substring(2).trim());
                                lowCount++;
                            } else if (xmlPullParser.getName().equals("type") & typeCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setType(xmlPullParser.getText());
                                typeCount++;
                            }
                        }
                        break;
                    //判断当前事件是否为标签元素结束事件
                    case XmlPullParser.END_TAG:
                        break;
                }
                //进入下一个元素并触发相应事件
                eventType = xmlPullParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return todayWeather;
    }
//更新UI中的控件
    void updateTodayWeather(TodayWeather todayWeather) {
        city_name_Tv.setText(todayWeather.getCity() + "天气");
        cityTv.setText(todayWeather.getCity());
        timeTv.setText(todayWeather.getUpdatetime() + "发布");
        humidityTv.setText("湿度：" + todayWeather.getShidu());
        pmDataTv.setText(todayWeather.getPm25());
        pmQualityTv.setText(todayWeather.getQuality());
        weekTv.setText(todayWeather.getDate());
        temperatureTv.setText(todayWeather.getHigh() + "~" + todayWeather.getLow());
        climateTv.setText(todayWeather.getType());
        windTv.setText("风力：" + todayWeather.getFengli());
        wenduTv.setText("当前温度："+ todayWeather.getWendu()+"℃");
        fengxiangTv.setText("风向："+ todayWeather.getFengxiang());
        //String numpm251=todayWeather.getPm25();
        //强制类型转换
        int numpm25 = Integer.valueOf(todayWeather.getPm25());
        if(numpm25>=0&&numpm25<=50)
            pmImg.setImageResource(R.drawable.biz_plugin_weather_0_50);
        else if(numpm25>50&&numpm25<=100)
            pmImg.setImageResource(R.drawable.biz_plugin_weather_51_100);
        else if(numpm25>100&&numpm25<=150)
            pmImg.setImageResource(R.drawable.biz_plugin_weather_101_150);
        else if(numpm25>150&&numpm25<=200)
            pmImg.setImageResource(R.drawable.biz_plugin_weather_151_200);
        else if(numpm25>200&&numpm25<=300)
            pmImg.setImageResource(R.drawable.biz_plugin_weather_201_300);
        else if(numpm25>300)
            pmImg.setImageResource(R.drawable.biz_plugin_weather_greater_300);


        switch (todayWeather.getType()){
            case "晴":{
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_qing);
            }
            case "暴雪":{
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_baoxue);
            }
            case "暴雨":{
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_baoyu);
            }
            case "大暴雨":{
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_dabaoyu);
            }
            case "大雪":{
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_daxue);
            }
            case "大雨":{
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_dayu);
            }
            case "多云":{
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_duoyun);
            }
            case "雷阵雨":{
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_leizhenyu);
            }
            case "雷阵雨冰雹":{
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_leizhenyubingbao);
            }
            case "沙尘暴":{
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_shachenbao);
            }
            case "特大暴雨":{
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_tedabaoyu);
            }
            case "雾":{
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_wu);
            }
            case "小雪":{
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_xiaoxue);
            }
            case "小雨":{
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_xiaoyu);
            }
            case "阴":{
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_yin);
            }
            case "雨夹雪":{
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_yujiaxue);
            }
            case "阵雪":{
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhenxue);
            }
            case "阵雨":{
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhenyu);
            }
            case "中雪":{
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhongxue);
            }
            case "中雨":{
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhongyu);
            }
        }


        Toast.makeText(MainActivity.this, "更新成功！", Toast.LENGTH_SHORT).show();
    }


}
