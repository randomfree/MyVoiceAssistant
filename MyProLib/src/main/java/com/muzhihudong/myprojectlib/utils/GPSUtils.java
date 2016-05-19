package com.muzhihudong.myprojectlib.utils;

import android.content.Context;
import android.location.LocationManager;

/**
 * Created by sonic on 2016/4/11.
 */
public class GPSUtils {
    /**
     * 判断Use GPS satellites.是否勾选
     * @return true 表示开启
     */
    public static boolean isGPSProviderAvaliable( Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            return gps;
    }

    /**
     *  判断Use wireless networks 是否勾选
     * @return true 表示开启
     */
    public static boolean isWIFIProviderAvaliable( Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return network;
    }

    public static boolean isGPSOpen(Context context)
    {
        return isGPSProviderAvaliable(context) && isWIFIProviderAvaliable(context);
    }
}
