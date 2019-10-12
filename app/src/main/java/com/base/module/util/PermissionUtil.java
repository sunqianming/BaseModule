package com.base.module.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SQM
 * @Description 权限处理类
 */

public class PermissionUtil {

    public static final int CODE_STORAGE = 0x11;
    public static final int CODE_LOCATION = 0x12;
    public static final int CODE_CALENDAR = 0x13;
    public static final int CODE_CONTACTS = 0x14;
    public static final int CODE_SENSORS = 0x15;
    public static final int CODE_AUDIO = 0x16;
    public static final int CODE_PHONE = 0x17;
    public static final int CODE_SMS = 0x18;
    public static final int CODE_CAMERA = 0x19;

    /**
     * 判断权限
     *
     * @param context
     */
    public static void checkPermission(Activity context) {
        if (Build.VERSION.SDK_INT < 23) {
            return;
        }
        List<String> list = checkPermissions(context, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,  //读写
                Manifest.permission.ACCESS_COARSE_LOCATION, //位置
                Manifest.permission.READ_CALENDAR,  //日程信息
                Manifest.permission.READ_CONTACTS,  //联系人
                Manifest.permission.BODY_SENSORS,  //传感器
                Manifest.permission.RECORD_AUDIO, //音视频
                Manifest.permission.CALL_PHONE,  //电话
                Manifest.permission.READ_SMS,//短讯服务
                Manifest.permission.CAMERA,  //相机
        });
        if (list != null) {
            requestPermissions(context, list);
        }
    }

    /***
     * 检测权限
     * @param context
     * @param permissions
     */
    public static List<String> checkPermissions(Activity context, String[] permissions) {
        List<String> list = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                list.add(permission);
            }
        }
        if (list.size() > 0) {
            return list;
        }
        return null;
    }

    /***
     * 申请权限组
     * @param context
     * @param permissions
     */
    public static void requestPermissions(Activity context, List<String> permissions) {
        if (permissions == null || permissions.size() == 0) {
            return;
        }
        ActivityCompat.requestPermissions(context, permissions.toArray(new String[]{}), 1);
    }

    /***
     * 申请权限
     * @param context
     * @param permission
     */
    public static void requestPermission(Activity context, String permission) {
        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, new String[]{permission}, 1);
        }
    }

}
