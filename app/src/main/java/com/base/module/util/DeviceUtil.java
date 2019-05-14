/**
 * *程序功能：获取手机信息
 * *内部方法：
 * *编程人员：韦国旺
 * *最后修改日期：2014年12月11日
 */
package com.base.module.util;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

public class DeviceUtil {
    /**
     * 手机宽度
     */
    public static int displayWidth = 0;
    /**
     * 手机高度
     */
    public static int displayHeight = 0;
    /**
     * density
     */
    public static float displaydensity = 0;
    /**
     * Dpi
     */
    public static int displayDpi = 0;

    /**
     * 返回屏幕分辨率的宽度  px
     *
     * @param act
     * @return
     */
    public static int getDisplayWidth(Context act) {
        if (displayWidth == 0) {
            DisplayMetrics dm = new DisplayMetrics();
            ((Activity) act).getWindowManager().getDefaultDisplay().getMetrics(dm);
            displayWidth = dm.widthPixels;
        }
        return displayWidth;
    }

    /**
     * 返回屏幕分辨率的高度  px
     *
     * @param act
     * @return
     */
    public static int getDisplayHeight(Context act) {
        if (displayHeight == 0) {
            DisplayMetrics dm = new DisplayMetrics();
            ((Activity) act).getWindowManager().getDefaultDisplay().getMetrics(dm);
            displayHeight = dm.heightPixels;
        }
        return displayHeight;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(float dpValue, Context act) {
        if (displaydensity == 0) {
            DisplayMetrics dm = new DisplayMetrics();
            ((Activity) act).getWindowManager().getDefaultDisplay().getMetrics(dm);
            displaydensity = dm.density;
        }
        return (int) (dpValue * displaydensity + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(float pxValue, Context act) {
        if (displaydensity == 0) {
            DisplayMetrics dm = new DisplayMetrics();
            ((Activity) act).getWindowManager().getDefaultDisplay().getMetrics(dm);
            displaydensity = dm.density;
        }
        return (int) (pxValue / displaydensity + 0.5f);
    }

    /**
     * 取得手机串号
     *
     * @param context
     * @return
     */
    public static String getIMEI(Context context) {
        TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = telManager.getDeviceId(); //手机上的IMEI号
        if (deviceId == null || "".equals(deviceId)) {
            return "";
        } else {
            return deviceId;
        }
    }

    /***
     *
     * @Title: getIMSI
     * @Description: 获取IMSI
     * @param @param context
     * @param @return 参数
     * @return String 返回类型
     * @throws
     * @author huangyc
     * @date 2014-10-16 下午12:09:58
     */
    public static String getIMSI(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getSubscriberId();
    }

    /***
     *
     * @Title: getSimSerialNumber
     * @Description: 获取sim卡序列号
     * @param @param context
     * @param @return 参数
     * @return String 返回类型
     * @author huangyc
     * @date 2014-10-16 下午12:10:10
     */
    public static String getSimSerialNumber(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getSimSerialNumber();
    }

    /**
     * @param @param  context
     * @param @return 参数
     * @return String 返回类型
     * @Title: getPhoneNum
     * @Description: 获取电话号码
     * @author huangyc
     * @date 2014-10-16 下午12:10:28
     */
    public static String getPhoneNum(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getLine1Number();
    }

    /**
     * GPS是否开启
     *
     * @param paramContext
     * @return
     */
    public static boolean isGpsEnabled(Context paramContext) {
        ConnectivityManager mgrConn = (ConnectivityManager) paramContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        LocationManager locationManager = (LocationManager) paramContext.getSystemService(Context.LOCATION_SERVICE);
        return ((mgrConn.getActiveNetworkInfo() != null && mgrConn.getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED)
                && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER));
    }
}
