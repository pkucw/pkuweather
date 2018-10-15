package com.example.dell.pkuweather;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class MainActivity extends Activity implements View.OnClickListener{
    private ImageView mUpdateBtn;
    private TextView cityTv,timeTv,humidityTv,weekTv,pmDataTv,pmQualityTv,
            temperatureTv,climateTv,windTv,city_name_Tv;
    private ImageView weatherImg,pmImg;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_info);

        mUpdateBtn = (ImageView)findViewById(R.id.title_update_btn);//获取相应按钮
        mUpdateBtn.setOnClickListener(this);

        if (NetUtil.getNetworkState(this)!=NetUtil.NETWORK_NONE){
            Log.d("myWeather","网络OK");
            Toast.makeText(MainActivity.this,"网络OK!!!",Toast.LENGTH_LONG).show();

        }else{
            Log.d("myWeather","网络挂了");
            Toast.makeText(MainActivity.this,"网络挂了！",Toast.LENGTH_LONG).show();
        }

        initView();
    }

    void initView(){
        city_name_Tv=(TextView)findViewById(R.id.title_city_name);
        cityTv=(TextView)findViewById(R.id.city);
        timeTv=(TextView)findViewById(R.id.time);
        humidityTv=(TextView)findViewById(R.id.humidity);
        weekTv=(TextView)findViewById(R.id.week_today);
        pmDataTv=(TextView)findViewById(R.id.pm_data);
        pmQualityTv=(TextView)findViewById(R.id.pm2_5_quality);
        pmImg=(ImageView) findViewById(R.id.pm2_5_img);
        temperatureTv=(TextView)findViewById(R.id.temperature);
        climateTv=(TextView)findViewById(R.id.climate);
        windTv=(TextView)findViewById(R.id.wind);
        weatherImg=(ImageView)findViewById(R.id.weather_img);

        city_name_Tv.setText("N/A");
        cityTv.setText("N/A");
        timeTv.setText("N/A");
        humidityTv.setText("N/A");
        pmDataTv.setText("N/A");
        pmQualityTv.setText("N/A");
        weekTv.setText("N/A");
        temperatureTv.setText("N/A");
        climateTv.setText("N/A");
        windTv.setText("N/A");

    }

    @Override
    public void onClick(View view){
        if (view.getId()==R.id.title_update_btn){
            SharedPreferences sharedPreferences = getSharedPreferences("config",MODE_PRIVATE);
            String cityCode = sharedPreferences.getString("main_city_code","101010100");
            Log.d("myWeather",cityCode);

            if(NetUtil.getNetworkState(this)!=NetUtil.NETWORK_NONE){
                Log.d("myWeather","网络ok");
                queryWeatherCode(cityCode);
            }else{
                Log.d("myWeather","网络false");
                Toast.makeText(MainActivity.this, "网络挂了", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * @param cityCode 关于cityCode的参数说明
     *
     *
     */

//

    //"https://wthrcdn.etouch.cn/WeatherApi?citykey=" + cityCode
    //https://www.weather.com.cn/data/cityinfo/"+cityCode+".html
    private void queryWeatherCode(String cityCode){
        final String address="http://wthrcdn.etouch.cn/WeatherApi?citykey=101010100";
     /*   HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier(){
            public boolean verify(String string,SSLSession ssls) {
                return true;
            }
        });*/

        Log.d( "myWeather",address);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection con = null;
                try{
                    URL url = new URL(address);
                    con = (HttpURLConnection)url.openConnection();
                    con.setRequestMethod("GET");
                    con.setConnectTimeout(8000);
                    con.setReadTimeout(8000);
                    InputStream in = con.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String str;
                    while ((str=reader.readLine())!=null){
                        response.append(str);
                        Log.d("myWeather",str);
                    }
                    String responseStr = response.toString();
                    Log.d("myWeather",responseStr);
                    parseXML(responseStr);
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if(con !=null){
                        con.disconnect();
                    }
                }
            }
        }).start();
    }

    //解析城市名称已经更新时间信息

    private void parseXML(String xmldata){
        int fengxiangCount = 0;
        int fengliCount = 0;
        int dateCount = 0;
        int highCount = 0;
        int lowCount = 0;
        int typeCount = 0;
        try{
            XmlPullParserFactory fac = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser=fac.newPullParser();
            xmlPullParser.setInput(new StringReader(xmldata));
            int eventType = xmlPullParser.getEventType();
            Log.d("myWeather","parseXML");
            while (eventType != XmlPullParser.END_DOCUMENT){
                switch (eventType){
    //判断当前事件是否为文档开始事件
                    case XmlPullParser.START_DOCUMENT:
                        break;
    //判断当前事件是否为标签元素开始事件
                    case XmlPullParser.START_TAG:
                        if (xmlPullParser.getName().equals("city")){
                            eventType = xmlPullParser.next();
                            Log.d("myWeather","city:     "+xmlPullParser.getText());

                        }else if(xmlPullParser.getName().equals("updatetime")){
                             eventType = xmlPullParser.next();
                            Log.d("myWeather","updatetime      "+xmlPullParser.getText());
                        }else if(xmlPullParser.getName().equals("shidu")){
                            eventType = xmlPullParser.next();
                            Log.d("myWeather","shidu:    "+xmlPullParser.getText());
                        }else if(xmlPullParser.getName().equals("wendu")){
                            eventType=xmlPullParser.next();
                            Log.d("myWeather","wendu:   "+xmlPullParser.getText());
                        }else if(xmlPullParser.getName().equals("pm25")){
                            eventType=xmlPullParser.next();
                            Log.d("myWeather","pm25:   "+xmlPullParser.getText());
                        }else if(xmlPullParser.getName().equals("quality")){
                            eventType=xmlPullParser.next();
                            Log.d("myWeather","quality:   "+xmlPullParser.getText());
                        }else if(xmlPullParser.getName().equals("fengxiang")&&fengxiangCount==0){
                            eventType = xmlPullParser.next();
                            Log.d("myWeather","fengxiang:    "+xmlPullParser.getText());
                            fengxiangCount++;
                        }else if(xmlPullParser.getName().equals("fengli")&&fengliCount==0){
                            eventType = xmlPullParser.next();
                            Log.d("myWeather","fengli:   "+xmlPullParser.getText());
                            fengliCount++;
                        }else if(xmlPullParser.getName().equals("data")&&dateCount==0){
                            eventType = xmlPullParser.next();
                            Log.d("myWeather","data:  "+xmlPullParser.getText());
                            dateCount++;
                        }else if(xmlPullParser.getName().equals("high")&&highCount==0){
                            eventType = xmlPullParser.next();
                            Log.d("myWeather","high:  "+xmlPullParser.getText());
                            highCount++;
                        }else if (xmlPullParser.getName().equals("low")&&lowCount==0){
                            eventType = xmlPullParser.next();
                            Log.d("myWeather","low:   "+xmlPullParser.getText());
                            lowCount++;
                        }else if (xmlPullParser.getName().equals("type")&typeCount==0){
                            eventType = xmlPullParser.next();
                            Log.d("myWeather","type:"+xmlPullParser.getText());
                            typeCount++;
                        }
                        break;
                    //判断当前事件是否为标签元素结束事件
                    case XmlPullParser.END_TAG:
                        break;
                }
                //进入下一个元素并触发相应事件
                eventType = xmlPullParser.next();
            }
        }catch (XmlPullParserException e){
             e.printStackTrace();
         }catch (IOException e){
         e.printStackTrace();
     }
   }





}
