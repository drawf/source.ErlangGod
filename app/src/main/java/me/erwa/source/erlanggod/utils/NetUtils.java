package me.erwa.source.erlanggod.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import me.erwa.source.erlanggod.MainApplication;

/**
 * Created by drawf on 2017/5/19.
 * ------------------------------
 */
public class NetUtils {

    public static final String TYPE_NONE = "none";
    public static final String TYPE_WIFI = "wifi";
    public static final String TYPE_CELLULAR = "cellular";
    private static final Context sContext = MainApplication.getContext();

    public static boolean isConnected() {
        try {
            //获取连接管理对象
            ConnectivityManager connectivity = (ConnectivityManager) sContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                //获取活动的网络连接
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return false;
    }

    public static int getIntType() {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) sContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
            return mNetworkInfo.getType();
        }
        return -1;
    }

    public static String getType() {
        String type = TYPE_NONE;
        ConnectivityManager mConnectivityManager = (ConnectivityManager) sContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = mConnectivityManager.getActiveNetworkInfo();
        if (info == null)
            return type;
        switch (info.getType()) {
            case ConnectivityManager.TYPE_WIFI:
            case ConnectivityManager.TYPE_WIMAX:
            case ConnectivityManager.TYPE_ETHERNET:
                type = TYPE_WIFI;
                break;
            case ConnectivityManager.TYPE_MOBILE:
                type = TYPE_CELLULAR;
                switch (info.getSubtype()) {
                    case TelephonyManager.NETWORK_TYPE_LTE:  // 4G
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS: // 3G
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                        break;
                    case TelephonyManager.NETWORK_TYPE_GPRS: // 2G
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                        break;
                }
                break;
            default:
                type = TYPE_NONE;
        }
        return type;
    }
}
