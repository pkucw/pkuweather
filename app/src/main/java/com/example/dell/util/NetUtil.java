package com.example.dell.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class NetUtil {
    public static final int NETWORK_NONE = 0;
    public static final int NETWORK_WIFI = 1;
    public static final int NETWORK_MOBILE = 2;

    public static int getNetworkState(Context context){
        //ConnectivityManager为系统服务，用来管理网络状态链接信息
        ConnectivityManager conManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);//通过网络链接管理者获取管理对象
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();// 获取正链接的网络信息

        if (networkInfo == null){
            return NETWORK_NONE;
        }
        int nType = networkInfo.getType();
        if(nType == ConnectivityManager.TYPE_MOBILE){
            return NETWORK_MOBILE;
        }else if (nType==ConnectivityManager.TYPE_WIFI){
            return NETWORK_WIFI;
        }
        return NETWORK_NONE;
    }
}
