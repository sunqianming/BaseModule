package com.base.module.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;

import com.base.module.zip.ZipEntry;
import com.base.module.zip.ZipFile;
import com.base.module.zip.ZipOutputStream;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Enumeration;
import java.util.List;

/***
 * @author SQM
 * @Description: 文件操作基础类
 */
public class FileHelper {

    public static String getPath(Context context, Uri uri) {
        String path = "";
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = null;
            try {
                if (DocumentsContract.isDocumentUri(context, uri)) {
                    String wholeId = DocumentsContract.getDocumentId(uri);
                    String split = "";
                    if (wholeId.indexOf(":") > 0) {
                        split = ":";
                    }
                    if (wholeId.indexOf("%") > 0) {
                        split = "%";
                    }
                    String id = wholeId.split(split)[1];
                    String sel = MediaStore.Images.Media._ID + "=?";
                    cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, sel, new String[]{id}, null);
                } else {
                    cursor = context.getContentResolver().query(uri, projection, null, null, null);
                }


                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    path = cursor.getString(column_index);

//                    for(int i=0;i<cursor.getColumnCount();i++){
//                        int type=cursor.getType(i);
//                        String name=cursor.getColumnName(i);
//                        if (type==Cursor.FIELD_TYPE_STRING){
//                            Log.d("1","key="+name+",v="+cursor.getString(i));
//                        }else if(type==Cursor.FIELD_TYPE_INTEGER){
//                            Log.d("1","key="+name+",v="+cursor.getInt(i));
//                        }else if(type==Cursor.FIELD_TYPE_NULL){
//
//                        }
//                        else{
//                            Log.d("1","key="+name);
//                        }
//                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            path = uri.getPath();
        }

        return path;
    }


    /***
     * 从asset中读取文件
     * @param context
     * @param filePath
     * @return
     */
    public static byte[] readFileFromAsset(Context context, String filePath) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int len = 0;
        byte[] buffer = new byte[1024];
        InputStream is = null;
        try {
            is = context.getAssets().open(filePath);
            while ((len = is.read(buffer)) > 0) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /***
     * 读取文本信息
     * @param path
     * @return
     */
    public static byte[] readFile(String path) {
        File f = new File(path);
        if (!f.exists() || f.isDirectory()) {
            return null;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int len = 0;
        byte[] buffer = new byte[1024];
        FileInputStream is = null;
        try {
            is = new FileInputStream(f);
            while ((len = is.read(buffer)) > 0) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 删除web缓存
     *
     * @param context
     */
    public static void clearWebCache(Context context) {
        CookieManager cm = CookieManager.getInstance();
        try {
            context.deleteDatabase("webview.db");
            context.deleteDatabase("webviewCache.db");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                cm.removeAllCookies(new ValueCallback<Boolean>() {
                    @Override
                    public void onReceiveValue(Boolean value) {

                    }
                });
                cm.flush();
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    cm.removeAllCookie();
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        // 默认缓存目录
        File cachedir = new File(context.getCacheDir().getAbsolutePath());
        if (cachedir.exists()) {
            File vv = new File(cachedir.getParentFile().getAbsoluteFile() + "/app_webview");
            deleteFile(vv);
            deleteFile(cachedir);
        }
    }


    /***
     * unzip
     * @param zipFilePath
     * @param outPath
     * @return
     */
    public static boolean unzipFiles(String zipFilePath, String outPath) {
        File zipFile = new File(zipFilePath);
        if (!zipFile.exists()) {
            return false;
        }
        File outFile = new File(outPath);
        if (!outFile.exists()) {
            outFile.mkdirs();
        }
        try {
            ZipFile z = new ZipFile(zipFile);
            Enumeration<ZipEntry> files = z.getEntries();
            int len = 0;
            byte buffer[] = new byte[1024];
            while (files.hasMoreElements()) {
                ZipEntry entry = files.nextElement();
                File ff = new File(outPath + File.separator + entry.getName());
                if (!ff.getParentFile().exists()) {
                    ff.getParentFile().mkdirs();
                }
                if (entry.isDirectory()) {
                    ff.mkdirs();
                    continue;
                }
                InputStream zis = z.getInputStream(entry);
                FileOutputStream fos = new FileOutputStream(ff);
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.flush();
                fos.close();
            }
            z.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /***
     * 压缩文件
     *
     * @param dir
     * @param outPath
     */
    public static boolean zipFiles(String dir, String outPath) {
        File f = new File(dir);
        if (!f.exists()) {
            return false;
        }
        File ff = new File(outPath);
        if (!ff.getParentFile().exists()) {
            ff.getParentFile().mkdirs();
        }
        if (ff.exists()) {
            ff.delete();
        }
        try {
            FileOutputStream fos = new FileOutputStream(outPath);
            ZipOutputStream zos = new ZipOutputStream(fos);
            zipFile(zos, f, dir);
            zos.flush();
            zos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /*
     * zipfile分割
     * size 分割大小（MB）
     * */
    public void zipFileSplit(String srcFile, int size, String destFile) {
        File file = new File(srcFile);
        if (file.exists()) {
            return;
        }
        long countSize = srcFile.length();
        long fileSize = 1024 * 1024 + size;
        int num = 0;
        if (countSize % fileSize == 0) {
            num = (int) (countSize / fileSize);
        } else {
            num = (int) (countSize / fileSize);
        }
        InputStream is = null;
        try {
            is = new FileInputStream(srcFile);
            BufferedInputStream bis = new BufferedInputStream(is);
            BufferedOutputStream bos = null;
            byte bytes[] = new byte[1024 * 1024];
            int len = -1;
            for (int i = 0; i < num; i++) {
                String newFile = destFile + File.separator + file.getName() + "-" + i;
                bos = new BufferedOutputStream(new FileOutputStream(newFile));
                int count = 0;
                while ((len = bis.read(bytes)) != -1) {
                    bos.write(bytes, 0, len);
                    bos.flush();
                    count += len;
                    if (count >= fileSize) {
                        break;
                    }
                }
                bos.close();
            }
            bis.close();
            is.close();
        } catch (Exception e) {

        }
    }

    private static void zipFile(ZipOutputStream zos, File file, String baseDir) throws IOException {
        if (file.isDirectory()) {
            File[] fs = file.listFiles();
            if (fs != null) {
                for (File f : fs) {
                    zipFile(zos, f, baseDir);
                }
            }
        } else {
            ZipEntry ze = new ZipEntry(file.getAbsolutePath().replaceAll(baseDir + File.separator, ""));
            zos.putNextEntry(ze);
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, len);
            }
            fis.close();
            zos.closeEntry();
        }
    }

    /**
     * 取得文件列表
     *
     * @param realPath
     * @param files
     * @return
     */
    public static List<File> getFiles(String realPath, List<File> files) {
        File realFile = new File(realPath);
        if (realFile.isDirectory()) {
            File[] subFiles = realFile.listFiles();
            if (subFiles != null) {
                for (File file : subFiles) {
                    if (file.isDirectory()) {
                        getFiles(file.getAbsolutePath(), files);
                    } else {
                        files.add(file);
                    }
                }
            }
        }
        return files;
    }

    /**
     * @param dirPath
     * @param fileName
     * @param bytes
     */
    public static boolean bytes2File(String dirPath, String fileName, byte[] bytes) {
        return bytes2File(dirPath, fileName, bytes, true);
    }

    public static boolean bytes2File(String dirPath, String fileName, byte[] bytes, boolean append) {
        FileOutputStream fileOutputStream = null;
        try {
            // 判断有没有文件夹
            File filePath = new File(dirPath);
            if (!filePath.exists()) {
                filePath.mkdirs(); // 没有，创建
            }
            // 写文件
            File file = new File(dirPath, fileName);
            fileOutputStream = new FileOutputStream(file, append);
            fileOutputStream.write(bytes);
            fileOutputStream.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }

            return true;
        }
    }

    /**
     * 创建目录
     *
     * @param dir
     */
    public static boolean createDir(String dir) {
        File file = new File(dir);
        if (!file.exists()) {
            return file.mkdirs();
        }
        return false;
    }

    /**
     * 重命名
     *
     * @param srcFile 源文件
     * @param name    新文件名
     */
    public static boolean renameFile(String srcFile, String name) {
        try {
            File f1 = new File(srcFile);
            String path = srcFile.substring(0, srcFile.lastIndexOf(File.separator) + 1);
            return f1.renameTo(new File(path + name));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /***
     * 重命名文件
     *
     * @param srcFile
     * @param pathName
     * @return
     */
    public static boolean renameFile2(String srcFile, String pathName) {
        File f1 = new File(srcFile);
        if (!f1.exists()) {
            return false;
        }
        File f2 = new File(pathName);
        if (f2.isDirectory()) {
            f2 = new File(pathName + File.separator + f1.getName());
        }
        if (f2.exists()) {
            f2.delete();
        }
        if (f1.getParent().equals(f2.getParent())) {
            return renameFile(srcFile, f2.getName());
        } else {
            return copyFile(srcFile, f2.getAbsolutePath()) && f1.delete();
        }
    }

    /**
     * 递归删除文件
     *
     * @param file
     */
    public static boolean deleteFile(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
                return true;
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                if (files != null) {
                    for (int i = 0; i < files.length; i++) {
                        deleteFile(files[i]);
                    }
                }
                file.delete();
            }
            return true;
        }
        return true;   // 反正都已经没了
    }


    /***
     * @param @param  context
     * @param @param  fileName
     * @param @param  path
     * @param @return 参数
     * @return boolean    返回类型
     * @Title: copyApkFromAssets
     * @Description: 将数据冲asset目录拷贝到其他目录
     * @author huangyc
     * @date 2014-10-22 下午3:40:39
     */
    public static boolean copyApkFromAssets(Context context, String fileName, String path) {
        boolean bRet = false;
        try {
            InputStream is = context.getAssets().open(fileName);
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(file);
            byte[] temp = new byte[1024];
            int i = 0;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
            }
            fos.close();
            is.close();
            bRet = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bRet;
    }


    /**
     * @param @param permission
     * @param @param path    参数
     * @return void    返回类型
     * @Title: chmod
     * @Description: 执行 chmod权限修改命令
     * @author huangyc
     * @date 2014-10-22 下午3:40:52
     */
    public static void chmod(String permission, String path) {
        try {
            String command = "chmod " + permission + " " + path;
            Runtime runtime = Runtime.getRuntime();
            Process p = runtime.exec(command);
            p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * @param @param path 文件路劲
     * @return boolean true,文件存；false，文件不存在
     * @Title: isExist
     * @Description: 文件是否存在
     * @author huangyc
     * @date 2014-6-30 下午2:39:44
     */
    public static boolean isExist(String path) {
        File v_f = new File(path);
        return v_f.exists();
    }

    /***
     * @param @param  path
     * @param @return 参数
     * @return boolean 返回类型
     * @Title: isDir
     * @Description: 是否是目录
     * @author huangyc
     * @date 2014-6-30 下午2:46:07
     */
    public static boolean isDir(String path) {
        File v_f = new File(path);
        return v_f.isDirectory();
    }

    /**
     * @param @param  path
     * @param @return 参数
     * @return long 返回类型
     * @Title: getFileSize
     * @Description: 获取文件大小
     * @author huangyc
     * @date 2014-6-30 下午2:42:27
     */
    public static long getFileSize(String path) {
        File v_f = new File(path);
        if (!v_f.exists() || v_f.isDirectory()) {
            return 0;
        } else {
            return v_f.length();
        }
    }

    public static boolean copyFiles(String resPath, String targetPath) {
        return copyFiles(resPath, targetPath, true);
    }

    //复制文件
    public static boolean copyFiles(String resPath, String targetPath, boolean clearTargetFiles) {
        File file = new File(resPath);
        if (!file.exists()) {
            return false;
        }
        if (file.isFile()) {
            return copyFile(resPath, targetPath);
        } else {
            File targetFile = new File(targetPath);
            if (clearTargetFiles && targetFile.exists()) {
                deleteFile(targetFile);
            }
            targetFile.mkdirs();
            try {
                Process p = Runtime.getRuntime().exec("cp -rf " + resPath + " " + targetPath);
                p.waitFor();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }


    /**
     * @param @param resPath 源文件路径
     * @param @param targetPath 目标文件路径
     * @return void 返回类型
     * @Title: copyFile
     * @Description: 复制文件
     * @author huangyc
     * @date 2014-6-30 下午2:47:02
     */
    public static boolean copyFile(String resPath, String targetPath) {
        try {
            FileInputStream fin = new FileInputStream(resPath);
            File targetFile = new File(targetPath);
            if (!targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdirs();
            }
            FileOutputStream fout = new FileOutputStream(targetPath);
            FileChannel fcin = fin.getChannel();
            FileChannel fcout = fout.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(100);
            while ((fcin.read(buffer)) != -1) {
                buffer.flip();
                fcout.write(buffer);
                buffer.clear();
            }
            fcout.close();
            fcin.close();
            fin.close();
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
