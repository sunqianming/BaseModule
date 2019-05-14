/**
 * *程序功能：SD工具类
 * *内部方法：
 * *编程人员：wells
 * *最后修改日期：2015年4月17日
 */

package com.base.module.util;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;

public class SdCardUtil {

    /***
     * 获取应用存取数据根目录
     *
     * @param context
     * @return
     */
    public static String getAPPRootPath(Context context) {
        if (isSDCardEnable()) {
            String path = getSDCardPath() + "Android" + File.separator + "data" + File.separator + context.getPackageName() + File.separator;
            File f = new File(path);
            if (!f.exists()) {
                f.mkdirs();
            }
            return path;
        }
        return context.getPackageResourcePath() + File.separator;
    }

    /***
     * 获取应用图片目录
     *
     * @param context
     * @return
     */
    public static String getAPPPicPath(Context context) {
        String path = getAPPRootPath(context) + File.separator + "pic" + File.separator;
        File f = new File(path);
        if (!f.exists()) {
            f.mkdirs();
        }
        return path;
    }

    /***
     * 获取其他目录
     *
     * @param context
     * @return
     */
    public static String getAPPOtherPath(Context context) {
        String path = getAPPPicPath(context) + File.separator + "other" + File.separator;
        File f = new File(path);
        if (!f.exists()) {
            f.mkdirs();
        }
        return path;
    }

    /**
     * 判断SDCard是否可用
     *
     * @return
     */
    public static boolean isSDCardEnable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);

    }

    /**
     * 获取SD卡路径
     *
     * @return
     */
    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    }

    /**
     * 获取系统存储路径
     *
     * @return
     */
    public static String getRootDirectoryPath() {
        return Environment.getRootDirectory().getAbsolutePath() + File.separator;
    }


    /**
     * 获取SD卡空闲大小
     *
     * @return
     */
    public static long getSDFreeSize() {
        // 取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        // 获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        // 空闲的数据块的数量
        long freeBlocks = sf.getAvailableBlocks();
        // 返回SD卡空闲大小
        return (freeBlocks * blockSize) / 1024 / 1024; // 单位MB
    }

    /**
     * 获取SD卡存储大小
     *
     * @return
     */
    public static long getSDAllSize() {
        // 取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        // 获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        // 获取所有数据块数
        long allBlocks = sf.getBlockCount();
        // 返回SD卡大小
        return (allBlocks * blockSize) / 1024 / 1024; // 单位MB
    }

}
