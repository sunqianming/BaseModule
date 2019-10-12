package com.base.module.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

/**
 * @author SQM
 * @Description:基础公共类
 */
public class CommonUtil {

    public static int getAppVersionCode(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String getVersionName(Context context) {
        String name = null;
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            name = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return name;
    }

    /***
     *
     * @Title: showShutDownApp
     * @Description:显示关闭程序对话框
     * @param @param context 参数
     * @return void 返回类型
     * @author huangyc
     * @date 2014-11-10 上午11:09:59
     */
    public static void showShutDownApp(Context context) {
        AlertDialog.Builder v_builder = new AlertDialog.Builder(context);
        v_builder.setTitle("退出系统");
        v_builder.setMessage("您确定要退出系统吗？");
        v_builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                IntentHelper.ShutDownAPP();
            }
        });
        v_builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        v_builder.create().show();
    }

    /**
     * @param @param  field
     * @param @param  t
     * @param @return 参数
     * @return T 返回类型
     * @Title: getAnnotation
     * @Description: 获取注解
     * @author huangyc
     * @date 2014-11-7 下午8:48:05
     */
    public static <T> T getAnnotation(Field field, Class<T> t) {
        Annotation v_ans[] = field.getDeclaredAnnotations();
        if (v_ans == null || v_ans.length == 0) {
            return null;
        }
        for (Annotation an : v_ans) {
            Class b = an.annotationType();
            String c = b.getName();
            String d = t.getName();
            if (c.equals(d)) {
                return (T) an;
            }
        }
        return null;
    }

    /**
     * @param @param v_list
     * @param @param classz 参数
     * @return void 返回类型
     * @Title: getField
     * @Description: 查询父类字段
     * @author huangyc
     * @date 2014-11-7 下午8:23:32
     */
    public static void getField(List<Field> v_list, Class<?> classz) {
        Field v_fs[] = classz.getDeclaredFields();
        for (Field f : v_fs) {
            v_list.add(f);
        }
        if (classz.getSuperclass() != Activity.class) {
            getField(v_list, classz.getSuperclass());
        }
    }

    public static Method getMethod(Class<?> classz, String methodName, Class<?> param) {
        Method d = null;
        try {
            d = classz.getDeclaredMethod(methodName, param);
        } catch (Exception e) {
            //e.printStackTrace();
        }
        if (d == null && classz != View.class) {
            return getMethod(classz.getSuperclass(), methodName, param);
        } else {
            return d;
        }
    }

    /***
     *
     * @Title: showKeyBord
     * @Description: 显示软键盘
     * @param @param primaryTextField 参数
     * @return void 返回类型
     * @author huangyc
     * @date 2014-10-22 下午3:43:09
     */
    public static void showKeyBord(final EditText primaryTextField) {
        (new Handler()).postDelayed(new Runnable() {
            public void run() {
                primaryTextField.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0));
                primaryTextField.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0, 0, 0));
                primaryTextField.setSelection(primaryTextField.getText().length());
            }
        }, 100);
    }

    /**
     * @param @param activity
     * @param @param ed 参数
     * @return void 返回类型
     * @Title: hideKeyBord
     * @Description: 关闭软键盘
     * @author huangyc
     * @date 2014-10-22 下午3:43:59
     */
    public static void hideKeyBord(final Context activity, final EditText ed) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(ed.getWindowToken(), 0);
    }

    /**
     * @param @param listView 参数
     * @return void 返回类型
     * @throws
     * @Title: requestLayoutListView
     * @Description: 重新布局listView高度，使其不滚动
     * @author huangyc
     * @date 2014-7-4 下午4:22:35
     */
    public static void requestLayoutListView(ListView listView) {
        ListAdapter mAdapter = listView.getAdapter();
        if (mAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < mAdapter.getCount(); i++) {
            View mView = mAdapter.getView(i, null, listView);
            if (mView == null) {
                continue;
            }
            mView.measure(0, 0);
            totalHeight += mView.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (mAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


    /***
     * 获取颜色
     * @param context
     * @param resId
     * @return
     */
    public static int getColor(Context context, int resId) {
        if (Build.VERSION.SDK_INT >= 23) {
            return context.getResources().getColor(resId, null);
        } else {
            return context.getResources().getColor(resId);
        }
    }

    /**
     * 获取字符串
     *
     * @param context
     * @param strId
     * @return
     */
    public static String getString(Context context, int strId) {
        return context.getString(strId);
    }

    /**
     * 获取字符串
     *
     * @param context
     * @param strId
     * @param params
     * @return
     */
    public static String getString(Context context, int strId, Object... params) {
        return context.getString(strId, params);
    }

    /***
     * 检测字符是否为空
     *
     * @param str
     * @return
     */
    public static boolean checkNull(String str) {
        if (str == null || str.trim().length() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 显示提示消息
     *
     * @param context
     * @param msg
     */
    public static void showToastShort(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示提示消息
     *
     * @param context
     * @param msg
     */
    public static void showToastLong(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    /**
     * 功能描述：
     * <p/>
     * 将金额分转换为元
     *
     * @param @param  i_fen 金额(分)
     * @param @param  i_message 错误消息名
     * @param @param  i_logger 日志对象
     * @param @return 参数对应内容描述
     * @return String 返回对应内容描述
     */
    public static String formatMeoney2Y(String i_fen) {
        try {
            NumberFormat formater = null;
            double num = Double.parseDouble(i_fen) / 100;
            formater = new DecimalFormat("0.00");
            return formater.format(num);
        } catch (Exception e) {
            return i_fen;
        }
    }


    /**
     * 功能描述：
     * <p/>
     * 将金额元转换为分
     *
     * @param @param  i_yuan 金额(元)
     * @param @param  i_logger
     * @param @return 参数对应内容描述
     * @return String 返回对应内容描述
     */
    public static String formatMeoney2F(String i_yuan) {
        try {
            double v_yuan = Double.valueOf(i_yuan);
            double aa = v_yuan * 100;
            DecimalFormat formater = new DecimalFormat("#");
            return formater.format(aa);
        } catch (Exception e) {
            return i_yuan;
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 sp 的单位 转成为 px
     */
    public static int sp2px(Context context, float sp) {
        float f1 = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * f1 + 0.5F);
    }

    /**
     * 根据手机的分辨率从 px 的单位 转成为 sp
     */
    public static int px2sp(Context context, float sp) {
        float f1 = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp / f1 + 0.5F);
    }

    /**
     * HEX编码 将形如0x12 0x2A 0x01 转换为122A01
     *
     * @param data
     * @return
     */
    public static String hexEncode(byte[] data) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            String tmp = Integer.toHexString(data[i] & 0xff);
            if (tmp.length() < 2) {
                buffer.append('0');
            }
            buffer.append(tmp);
        }
        String retStr = buffer.toString().toUpperCase();
        return retStr;

    }

    /**
     * HEX解码 将形如122A01 转换为0x12 0x2A 0x01
     *
     * @param data
     * @return
     */
    public static byte[] hexDecode(String data) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        for (int i = 0; i < data.length(); i += 2) {
            String onebyte = data.substring(i, i + 2);
            int b = Integer.parseInt(onebyte, 16) & 0xff;
            out.write(b);
        }
        return out.toByteArray();
    }


    /**
     * @param view   需要截图的View
     * @param startX inPixel
     * @param startY inPixel
     * @param width  需要剪裁的宽度
     * @param height 需要剪裁的高度
     * @return success?
     */
    public static boolean saveScreenShot(View view, String savePath, int startX, int startY, int width, int height) {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap cache = view.getDrawingCache();
        if (cache == null) return false;
        Bitmap bitmap = Bitmap.createBitmap(cache, startX, startY, width, height);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        File file = new File(savePath);
        if (file.exists()) {
            file.delete();
        }
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        view.setDrawingCacheEnabled(false);
        cache.recycle();
        bitmap.recycle();
        return true;
    }

    /**
     * 拨打电话
     *
     * @param context context
     * @param tel     电话号码
     */
    public static void call(Context context, String tel) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + tel));
        context.startActivity(intent);
    }

    /**
     * 通知相册更新
     *
     * @param context
     * @param filePath
     * @describe 拍照后，为了在相册中能够看到所拍照片，需要刷新
     */
    public static void refreshPhotoAlbum(Context context, String filePath) {
        File file = new File(filePath);
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        context.sendBroadcast(intent);
    }

}
