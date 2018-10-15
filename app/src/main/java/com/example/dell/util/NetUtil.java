package com.example.dell.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class NetUtil {
    public static final int NETWORK_NONE = 0;
    public static final int NETWORK_WIFI = 1;
    public static final int NETWORK_MOBILE = 2;
    public static int getNetworkState(Context context){
        ConnectivityManager conManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();//出错 解决方案：在AndroidManifest
        //文件中添加用户访问网络权限 <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
//https://blog.csdn.net/hack8/article/details/28038541
        if (networkInfo == null){
            return NETWORK_NONE;
        }

        int nType = networkInfo.getType();
        if(nType == ConnectivityManager.TYPE_MOBILE){
            return NETWORK_WIFI;
        }else if (nType==ConnectivityManager.TYPE_WIFI){
            return NETWORK_WIFI;
        }
        return NETWORK_NONE;
    }
}
