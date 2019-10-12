package com.base.module.util;

import android.os.Environment;
import com.base.module.base.BaseApplication;
import java.io.File;

public class EnvironmentHelper {

    private static String appHomeDir = ".ecp/";
    private static String dataBaseDir = "databases/";
    private static String fileDir = "files/";
    private static String picDir = "pics/";

    /***
     *
     * @Title: getAppDatabaseDir
     * @Description: 获取数据库目录
     * @param @return 参数
     * @return File 返回类型
     * @author huangyc
     * @date 2014-9-25 下午6:35:23
     */
    public static File getAppDatabaseDir() {
        File localFile = new File(getAppHomeDir(), dataBaseDir);
        if (!localFile.exists()) {
            localFile.mkdirs();
        }
        return localFile;
    }

    public static File getAnyofficeLog() {
        File localFile = new File(getAppHomeDir(), "anyofficeLog");
        if (!localFile.exists()) {
            localFile.mkdirs();
        }
        return localFile;
    }

    /***
     *
     * @return
     */
    public static File getAppTmpDir() {
        File localFile = new File(getAppHomeDir(), "tmpdatadir168");
        if (!localFile.exists()) {
            localFile.mkdirs();
        }
        return localFile;
    }

    /***
     *
     * @Title: getAppDatabaseFile
     * @Description: 获取数据目录文件
     * @param @param paramString
     * @param @return 参数
     * @return File 返回类型
     * @author huangyc
     * @date 2014-9-25 下午6:34:58
     */
    public static File getAppDatabaseFile(String paramString) {
        if (paramString == null) {
            paramString = "";
        }
        return new File(getAppDatabaseDir(), paramString);
    }

    /***
     *
     * @Title: getAppFile
     * @Description: 获取文件工作目录下某个文件
     * @param @param paramString
     * @param @return 参数
     * @return File 返回类型
     * @author huangyc
     * @date 2014-9-25 下午6:34:09
     */
    public static File getAppFile(String paramString) {
        if (paramString == null) {
            paramString = "";
        }
        return new File(getAppFileDir(), paramString);
    }

    /***
     *
     * @Title: getAppFileDir
     * @Description: 获取应用工作目录下文件目录
     * @param @return 参数
     * @return File 返回类型
     * @author huangyc
     * @date 2014-9-25 下午6:33:21
     */
    public static File getAppFileDir() {
        File localFile = new File(getAppHomeDir(), fileDir);
        if (!localFile.exists()) {
            localFile.mkdirs();
        }
        return localFile;
    }

    /**
     * @param @return 参数
     * @return File 返回类型
     * @Title: getAppHomeDir
     * @Description: 获取工作目录
     * @author huangyc
     * @date 2014-9-25 下午6:32:15
     */
    public static File getAppHomeDir() {
        File localFile = new File(getSdcardDir() + File.separator + "Android" + File.separator + "data" + File.separator + BaseApplication.getInstance().getPackageName() + File.separator);
        if (!localFile.exists()) {
            localFile.mkdirs();
        }
        return localFile;
    }

    /***
     *
     * @Title: getAppHomeSubDir
     * @Description: 获取应用工作目录下子目录
     * @param @param paramString
     * @param @return 参数
     * @return File 返回类型
     * @author huangyc
     * @date 2014-9-25 下午6:31:12
     */
    public static File getAppHomeSubDir(String paramString) {
        if (paramString == null) {
            paramString = "";
        }
        File localFile = new File(getAppHomeDir(), paramString);
        if (!localFile.exists()) {
            localFile.mkdirs();
        }
        return localFile;
    }

    /***
     *
     * @Title: getAppHomeSubFile
     * @Description: 获取应用程序工作目录下某个文件
     * @param @param paramString
     * @param @return 参数
     * @return File 返回类型
     * @author huangyc
     * @date 2014-9-25 下午6:30:29
     */
    public static File getAppHomeSubFile(String paramString) {
        if (paramString == null)
            paramString = "";
        return new File(getAppHomeDir(), paramString);
    }

    /***
     *
     * @Title: getAppPicDir
     * @Description: 获取图片目录
     * @param @return 参数
     * @return File 返回类型
     * @throws
     * @author huangyc
     * @date 2014-9-25 下午6:29:29
     */
    public static File getAppPicDir() {
        File localFile = new File(getAppHomeDir(), picDir);
        if (!localFile.exists()) {
            localFile.mkdirs();
        }
        return localFile;
    }

    /***
     *
     * @Title: getAppPicFile
     * @Description: 获取程序图片目录文件
     * @param @param paramString
     * @param @return 参数
     * @return File 返回类型
     * @author huangyc
     * @date 2014-9-25 下午6:28:49
     */
    public static File getAppPicFile(String paramString) {
        if (paramString == null) {
            paramString = "";
        }
        return new File(getAppPicDir(), paramString);
    }

    /***
     *
     * @Title: getSdcardDir
     * @Description: 获取SD卡目录
     * @param @return 参数
     * @return File 返回类型
     * @throws
     * @author huangyc
     * @date 2014-9-25 下午6:28:28
     */
    public static File getSdcardDir() {
        if (sdcardIsEnable()) {
            return Environment.getExternalStorageDirectory();
        } else {
            return BaseApplication.getInstance().getCacheDir();
        }
    }

    /***
     *
     * @Title: getSdcardDir
     * @Description: 获取SD卡下目录
     * @param @param paramString
     * @param @return 参数
     * @return File 返回类型
     * @throws
     * @author huangyc
     * @date 2014-9-25 下午6:27:52
     */
    public static File getSdcardDir(String paramString) {
        if (paramString == null) {
            paramString = "";
        }
        File localFile = new File(getSdcardDir(), paramString);
        if (!localFile.exists()) {
            localFile.mkdirs();
        }
        return localFile;
    }

    /***
     *
     * @Title: getSdcardFile
     * @Description: 获取SD卡某个文件
     * @param @param paramString
     * @param @return 参数
     * @return File 返回类型
     * @throws
     * @author huangyc
     * @date 2014-9-25 下午6:26:26
     */
    public static File getSdcardFile(String paramString) {
        if (paramString == null) {
            paramString = "";
        }
        return new File(getSdcardDir(), paramString);
    }

    /****
     *
     * @Title: sdcardIsEnable
     * @Description: SD卡是否能够使用
     * @param @return 参数
     * @return boolean 返回类型
     * @author huangyc
     * @date 2014-9-25 下午6:25:52
     */
    public static boolean sdcardIsEnable() {
        return (Environment.getExternalStorageState().equals("mounted")) && (!Environment.getExternalStorageState().equals("shared"));
    }

    /***
     *
     * @Title: sdcardIsMounted
     * @Description: 是否有SD卡
     * @param @return 参数
     * @return boolean 返回类型
     * @author huangyc
     * @date 2014-9-25 下午6:25:35
     */
    public static boolean sdcardIsMounted() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    /***
     *
     * @Title: setAppHomeDir
     * @Description: 设置应用目录
     * @param @param paramString 参数
     * @return void 返回类型
     * @author huangyc
     * @date 2014-9-25 下午6:25:03
     */
    public static void setAppHomeDir(String paramString) {
        if (CommonUtil.checkNull(paramString)) {
            return;
        }
        appHomeDir = paramString;
    }
}
