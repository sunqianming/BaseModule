package com.base.module.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.util.Base64;
import android.view.View;

import com.base.module.base.BaseApplication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


/***
 * @author SQM
 * @Description: 图片处理工具类
 */
public class BitmapHelper {

    public static Bitmap clearBitmap(Bitmap bitmap) {
        try {
            Bitmap scaledBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(scaledBitmap);
            canvas.drawBitmap(bitmap, 0, 0, new Paint(Paint.FILTER_BITMAP_FLAG));
            return scaledBitmap;
        } catch (Throwable e) {
            e.printStackTrace();
            return bitmap;
        }
    }

    /**
     * 本地保存图片
     *
     * @param outPath 保存路径
     */
    public static boolean savePic(Bitmap bitmap, String outPath, Bitmap.CompressFormat format) {
        return savePic(bitmap, outPath, format, 100);
    }


    /**
     * 本地保存图片
     *
     * @param outPath 保存路径
     */
    public static boolean savePic(Bitmap bitmap, String outPath, Bitmap.CompressFormat format, int quanlity) {
        if (bitmap == null) {
            return false;
        }
        File myCaptureFile = new File(outPath);
        if (!myCaptureFile.getParentFile().exists()) {
            myCaptureFile.getParentFile().mkdirs();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myCaptureFile);
            bitmap.compress(format, quanlity, fos);
            fos.flush();
            fos.close();
        } catch (Throwable e) {
            return false;
        }
        return true;
    }

    /**
     * 图片按比例大小压缩方法
     *
     * @param bitmap （根据Bitmap图片压缩）
     * @return
     */
    public static Bitmap compressScale(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        // 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
        if (baos.toByteArray().length / 1024 > 1024) {
            baos.reset();// 重置baos即清空baos
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);// 这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;// 这里设置高度为800f
        float ww = 480f;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) { // 如果高度高的话根据高度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be; // 设置缩放比例
        // newOpts.inPreferredConfig = Config.RGB_565;//降低图片从ARGB888到RGB565
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return bitmap;
    }

    /**
     * 质量压缩方法
     *
     * @param bitmap
     * @param size   (kb)
     * @return
     */
    public static String compressImage(Bitmap bitmap, int size) {
        if (bitmap == null) {
            return "";
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 90;
        while (Base64.encodeToString(baos.toByteArray(), Base64.URL_SAFE).getBytes().length / 1024 > size) { // 循环判断如果压缩后图片是否大于size(kb),大于继续压缩
            baos.reset(); // 重置baos即清空baos
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 1;// 每次都减少1
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        String imgPath = EnvironmentHelper.getAppTmpDir() + File.separator + TimeHelper.getCurrentStamp(7) + ".png";
        if (BitmapHelper.savePic(bitmap, imgPath, Bitmap.CompressFormat.JPEG, options)) {
            bitmap.recycle();
            return imgPath;
        }
        return "";
    }

    /**
     * 创建按比例缩放的bitmap
     *
     * @param srcBitmap
     * @param ratio
     * @return
     */
    public static Bitmap getScaledBitmap(Bitmap srcBitmap, float ratio) {
        int srcWidth = srcBitmap.getWidth();
        int srcHeight = srcBitmap.getHeight();
        int dstWidth = (int) (srcWidth * ratio), dstHeight = (int) (srcHeight * ratio);
        return Bitmap.createScaledBitmap(srcBitmap, dstWidth, dstHeight, true);
    }

    /**
     * 质量压缩（压缩图片到指定大小,单位为kb）
     *
     * @param imageBytes
     * @param size
     * @return
     */
    public static Bitmap bytes2Bitmap(byte[] imageBytes, int size) {
        Bitmap bm = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 90;
        while (baos.toByteArray().length / 1024 > size && options > 0) {
            baos.reset();
            bm.compress(Bitmap.CompressFormat.JPEG, options, baos);
            options -= 10;
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
        bm.recycle();
        return bitmap;
    }

    /**
     * 压缩图片（maxkb为图片的最大大小，单位kb）
     *
     * @param bitmap
     * @param maxkb
     * @param needRecycle
     * @return
     */
    public static byte[] bitmap2Bytes(Bitmap bitmap, int maxkb, boolean needRecycle) {
        if (bitmap == null)
            return new byte[]{};
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bitmap = Bitmap.createScaledBitmap(bitmap, 400, 200, true);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
        int options = 100;
        while (Base64.encodeToString(output.toByteArray(), Base64.URL_SAFE).getBytes().length / 1024 > maxkb && options > 0) {
            output.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, output);
            options -= 1;
        }
        if (needRecycle)
            bitmap.recycle();
        if (Base64.encodeToString(output.toByteArray(), Base64.URL_SAFE).getBytes().length / 1024 > maxkb) {
            return new byte[]{};
        }
        return output.toByteArray();
    }

    public static String bitmapToBase64(Bitmap bitmap, int maxkb) {
        if (bitmap == null)
            return "";
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bitmap = Bitmap.createScaledBitmap(bitmap, 400, 200, true);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
        int options = 100;
        while (Base64.encodeToString(output.toByteArray(), Base64.URL_SAFE).getBytes().length / 1024 > maxkb && options > 0) {
            output.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, output);
            options -= 1;
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(output.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, null);

        String tmp = EnvironmentHelper.getAppTmpDir().getAbsolutePath() + TimeHelper.getCurrentStamp(7) + ".png";
        if (BitmapHelper.savePic(bitmap, tmp, Bitmap.CompressFormat.JPEG, options)) {
            return tmp;
        }
        return "";
    }

    /**
     * 读取照片exif信息中的旋转角度
     *
     * @param path 照片路径
     * @return角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            // e.printStackTrace();
            return degree;
        }
        return degree;
    }

    /***
     * 将图片转换为base64数据
     *
     * @param filePath
     * @return
     */
    public static String imgToBase64(String filePath) {
        ByteArrayOutputStream out = null;
        Bitmap bitMap = null;
        try {
            bitMap = BitmapFactory.decodeFile(filePath);
            if (bitMap == null) {
                return null;
            }
            out = new ByteArrayOutputStream();
            bitMap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            byte[] imgBytes = out.toByteArray();
            return Base64.encodeToString(imgBytes, Base64.DEFAULT);
        } catch (Throwable e) {
            e.printStackTrace();
            System.gc();
            return null;
        } finally {
            if (bitMap != null) {
                bitMap.recycle();
            }
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /***
     * 将图片转换为base64数据
     *
     * @param filePath
     * @return
     */
    public static String pngImgToBase64(String filePath) {
        ByteArrayOutputStream out = null;
        Bitmap bitMap = null;
        try {
            bitMap = BitmapFactory.decodeFile(filePath);
            if (bitMap == null) {
                return null;
            }
            out = new ByteArrayOutputStream();
            bitMap.compress(Bitmap.CompressFormat.PNG, 100, out);
            byte[] imgBytes = out.toByteArray();
            return Base64.encodeToString(imgBytes, Base64.DEFAULT);
        } catch (Throwable e) {
            e.printStackTrace();
            System.gc();
            return null;
        } finally {
            if (bitMap != null) {
                bitMap.recycle();
            }
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
            }
        }
    }

    /***
     * 生成圆角图片
     *
     * @param bitmap
     * @param pixels
     * @return
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;

    }

    /*****
     * 将view转换为图片
     */
    public static Bitmap getBitmapFromView(View v, boolean hashBackground) {
        boolean willNotCache = v.willNotCacheDrawing();
        v.setWillNotCacheDrawing(false);
        int color = 0;
        if (!hashBackground) {
            color = v.getDrawingCacheBackgroundColor();
            v.setDrawingCacheBackgroundColor(0);
            if (color != 0) {
                v.destroyDrawingCache();
            }
        }
        v.buildDrawingCache();
        Bitmap cacheBitmap = v.getDrawingCache();
        v.destroyDrawingCache();
        v.setWillNotCacheDrawing(willNotCache);
        if (!hashBackground) {
            v.setDrawingCacheBackgroundColor(color);
        }
        if (cacheBitmap == null) {

        }
        return cacheBitmap;
    }

    /***
     * 从资源中获取图片
     *
     * @param resId
     * @return
     */
    public static Drawable getDrawableFromRes(int resId) {
        return BaseApplication.getInstance().getResources().getDrawable(resId);
    }

    /**
     * 从asset获取缩放图片
     *
     * @param picName
     * @param scaleSize
     * @return
     */
    public static Bitmap zoomBitmapFromAsset(String picName, float scaleSize) {
        Bitmap oldbmp = null;
        InputStream is = null;
        try {
            is = BaseApplication.getInstance().getAssets().open("image/" + picName);
            oldbmp = BitmapFactory.decodeStream(is);
            Matrix matrix = new Matrix();
            matrix.postScale(scaleSize, scaleSize);
            int width = oldbmp.getWidth();
            int height = oldbmp.getHeight();
            return Bitmap.createBitmap(oldbmp, 0, 0, width, height, matrix, true);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (oldbmp != null) {
                oldbmp.recycle();
                oldbmp = null;
            }
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 从asset获取缩放图片
     *
     * @param picName
     * @param scaleSize
     * @return
     */
    public static Drawable zoomDrawableFromAsset(String picName, float scaleSize) {
        Bitmap oldbmp = null;
        InputStream v_IS = null;
        try {
            v_IS = BaseApplication.getInstance().getAssets().open("image/" + picName);
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inPreferredConfig = Config.ARGB_8888;
            o.inPurgeable = true;// 允许可清除
            o.inInputShareable = true;// 以上options的两个属性必须联合使用才会有效果
            oldbmp = BitmapFactory.decodeStream(v_IS, null, o);
            int width = oldbmp.getWidth();
            int height = oldbmp.getHeight();
            Matrix matrix = new Matrix();
            matrix.postScale(scaleSize, scaleSize);
            return new BitmapDrawable(Bitmap.createBitmap(oldbmp, 0, 0, (int) (width), (int) (height), matrix, true));
        } catch (Exception e) {
            System.gc();
            try {
                v_IS = BaseApplication.getInstance().getAssets().open("image/" + picName);
                BitmapFactory.Options o = new BitmapFactory.Options();
                o.inPreferredConfig = Config.RGB_565;
                o.inPurgeable = true;
                o.inInputShareable = true;
                oldbmp = BitmapFactory.decodeStream(v_IS, null, o);
                int width = oldbmp.getWidth();
                int height = oldbmp.getHeight();
                Matrix matrix = new Matrix();
                matrix.postScale(scaleSize, scaleSize);
                return new BitmapDrawable(Bitmap.createBitmap(oldbmp, 0, 0, (int) (width), (int) (height), matrix, true));
            } catch (Exception ee) {
                e.printStackTrace();
                return null;
            }
        } finally {
            if (oldbmp != null) {
                oldbmp.recycle();
                oldbmp = null;
            }
            if (v_IS != null) {
                try {
                    v_IS.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /***
     * 从Asset中获取图片
     *
     * @param picName
     * @return
     */
    public static Drawable getDrawableFromAsset(String picName) {
        InputStream v_IS = null;
        try {
            v_IS = BaseApplication.getInstance().getAssets().open("image/" + picName);
            return new BitmapDrawable(BitmapFactory.decodeStream(v_IS));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (v_IS != null) {
                try {
                    v_IS.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Bitmap縮放
     *
     * @param
     * @param
     * @param
     * @return
     */
    public static Bitmap zoomBitMap(Bitmap oldbmp, int newWidth, int newHeight) {
        try {
            int width = oldbmp.getWidth();
            int height = oldbmp.getHeight();
            float scaleWidth = ((float) newWidth) / width;
            float scaleHeight = ((float) newHeight) / height;
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            Bitmap resizedBitmap = Bitmap.createBitmap(oldbmp, 0, 0, width, height, matrix, true);
            oldbmp.recycle();
            return resizedBitmap;
        } finally {
            System.gc();
        }
    }

    /**
     * 缩放
     *
     * @param drawable
     * @param scaleX
     * @param scaleY
     * @return
     */
    public static Drawable zoomDrawable(Drawable drawable, float scaleX, float scaleY) {
        try {
            int width = drawable.getIntrinsicWidth();
            int height = drawable.getIntrinsicHeight();
            Bitmap oldbmp = drawableToBitmap(drawable);
            Matrix matrix = new Matrix();
            matrix.postScale(scaleX, scaleY);
            Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height, matrix, true);
            oldbmp.recycle();
            return new BitmapDrawable(newbmp);
        } finally {
            drawable = null;
            System.gc();
        }
    }

    /**
     * 缩放
     *
     * @param oldbmp
     * @param scaleX
     * @param scaleY
     * @return
     */
    public static Bitmap zoomBitmap(Bitmap oldbmp, float scaleX, float scaleY) {
        Matrix matrix = new Matrix();
        matrix.postScale(scaleX, scaleY);
        int width = oldbmp.getWidth();
        int height = oldbmp.getHeight();
        return Bitmap.createBitmap(oldbmp, 0, 0, width, height, matrix, true);
    }

    /**
     * 同等比例缩放
     *
     * @param oldbmp
     * @param scaleSize
     * @return
     */
    public static Bitmap zoomBitMap(Bitmap oldbmp, float scaleSize) {
        return zoomBitmap(oldbmp, scaleSize, scaleSize);
    }

    /**
     * 同等比例缩放
     *
     * @param resId
     * @param scaleSize
     * @return
     */
    public static Bitmap zoomBitMapFromResource(int resId, float scaleSize) {
        Bitmap oldbmp = getBitMapFromResource(resId);
        return zoomBitmap(oldbmp, scaleSize, scaleSize);
    }

    /**
     * 同等比例缩放
     *
     * @param resId
     * @param scaleSize
     * @return
     */
    public static Drawable zoomDrawableFromResource(int resId, float scaleSize) {
        return zoomDrawable(getDrawableFromRes(resId), scaleSize, scaleSize);
    }

    /**
     * 同等比例缩放图片
     *
     * @param drawable
     * @param scaleSize
     * @return
     */
    public static Drawable zoomDrawable(Drawable drawable, float scaleSize) {
        return zoomDrawable(drawable, scaleSize, scaleSize);
    }

    /**
     * Drawable縮放
     *
     * @param drawable
     * @param w
     * @param h
     * @return
     */
    public static Drawable zoomDrawable(Drawable drawable, int w, int h) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        // drawable转换成bitmap
        Bitmap oldbmp = drawableToBitmap(drawable);
        // 创建操作图片用的Matrix对象
        Matrix matrix = new Matrix();
        // 计算缩放比例
        float sx = ((float) w / width);
        float sy = ((float) h / height);
        // 设置缩放比例
        matrix.postScale(sx, sy);
        // 建立新的bitmap，其内容是对原bitmap的缩放后的图
        Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height, matrix, true);
        oldbmp.recycle();
        return new BitmapDrawable(newbmp);
    }

    /***
     * 获取倒影图片
     *
     * @param bitmap
     * @return
     */
    public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
        final int reflectionGap = 4;
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);

        Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, h / 2, w, h / 2, matrix, false);

        Bitmap bitmapWithReflection = Bitmap.createBitmap(w, (h + h / 2), Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmapWithReflection);
        canvas.drawBitmap(bitmap, 0, 0, null);
        Paint deafalutPaint = new Paint();
        canvas.drawRect(0, h, w, h + reflectionGap, deafalutPaint);

        canvas.drawBitmap(reflectionImage, 0, h + reflectionGap, null);

        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0, bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff, 0x00ffffff, TileMode.CLAMP);
        paint.setShader(shader);
        // Set the Transfer mode to be porter duff and destination in
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        // Draw a rectangle using the paint with our linear gradient
        canvas.drawRect(0, h, w, bitmapWithReflection.getHeight() + reflectionGap, paint);

        return bitmapWithReflection;
    }

    /**
     * 获取圆角图片
     *
     * @param bitmap
     * @param roundPx
     * @return
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Bitmap output = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, w, h);
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * 將drawable转换为Bitmap
     *
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        // 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        // 取 drawable 的颜色格式
        Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888 : Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }

    /***
     * 获取图片
     *
     * @param
     * @param resId
     * @return
     */
    public static Bitmap getBitMapFromResource(int resId) {
        return BitmapFactory.decodeResource(BaseApplication.getInstance().getResources(), resId);
    }

    /***
     * 获取图片
     *
     * @param
     * @param resId
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap getBitmapFromResource(int resId, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPurgeable = true;
        BitmapFactory.decodeResource(BaseApplication.getInstance().getResources(), resId, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        try {
            return BitmapFactory.decodeResource(BaseApplication.getInstance().getResources(), resId, options);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    /***
     * 获取图片
     *
     * @param res
     * @param resId
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap getBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPurgeable = true;
        BitmapFactory.decodeResource(res, resId, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        try {
            return BitmapFactory.decodeResource(res, resId, options);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    /***
     * 获取图片
     *
     * @param fileDescriptor
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap getBitmapFromDescriptor(FileDescriptor fileDescriptor, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPurgeable = true;
        BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        try {
            return BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    /***
     * 获取图片
     *
     * @param data
     * @param offset
     * @param length
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap getBitmapFromByteArray(byte[] data, int offset, int length, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPurgeable = true;
        BitmapFactory.decodeByteArray(data, offset, length, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(data, offset, length, options);
    }

    /***
     * 计算图片大小
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
            final float totalPixels = width * height;
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;
            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }


    /**
     * Bottom|Center_Hori添加两行文字
     *
     * @param src
     * @param text1 第一行
     * @param text2 第二行
     * @return
     */
    public static Bitmap addText(Bitmap src, String text1, String text2, int textColor, float textSize) {
        int width = src.getWidth();
        int height = src.getHeight();

        Bitmap bm = null;
        bm = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        Paint p = new Paint();
        p.setStyle(Paint.Style.FILL_AND_STROKE);
        p.setAntiAlias(true);
        p.setColor(textColor);
        p.setStrokeWidth(4.0f);
        p.setTextSize(textSize);
        float textWidth1 = p.measureText(text1);
        float textHeight = textWidth1 / text1.length();
        float textWidth2 = p.measureText(text2);
        Paint.FontMetrics m = p.getFontMetrics();
//		float scale1=width/(textWidth1>textWidth2?textWidth1:textWidth2);
//		float scale2=(textWidth1>textWidth2?textWidth1:textWidth2)/width;
//		if(scale2>1){
//			textSize=(int)(1.0f*textSize/scale2*2/3);
//		}
//		if(scale1>2){
//			textSize=(int)(textSize*scale1*1/2);
//			textSize=textSize>40?40:textSize;
//			p.setTextSize(textSize);
//			 textWidth1 = p.measureText(text1);
//			 textHeight = textWidth1 / text1.length();
//			 textWidth2 = p.measureText(text2);
//		}

        canvas.drawBitmap(src, 0, 0, p);
        float angle = -30;
        float maxW = (Math.max(textWidth1, textWidth2)) * 0.5f + 40;
        float maxH = 1.0f * height / (1.0f * width / maxW) + 10;
        float fontHeight = (float) (Math.ceil(m.descent - m.top) + 2) * 1.1f;
        for (int x = 20, index = 0; x < width; x += maxW, index++) {
            drawText(canvas, text2, x + index * maxW, height - 20 - index * maxH, p, angle);
            drawText(canvas, text1, x + index * maxW, height - 66 - fontHeight - index * maxH, p, angle);
        }

//		for(int x=20,index=0;x<width;x+=maxW,index++){
//			drawText(canvas,text2,x+index*maxW,height*0.7f-20-index*maxH,p,angle);
//			drawText(canvas,text1,x+index*maxW,height*0.7f-50-fontHeight-index*maxH , p,angle);
//		}

        for (int x = 20, index = 0; x < width; x += maxW, index++) {
            drawText(canvas, text2, x + index * maxW, height * 0.5f - 20 - index * maxH, p, angle);
            drawText(canvas, text1, x + index * maxW, height * 0.5f - 60 - fontHeight - index * maxH, p, angle);
        }


//		for(int x=20,index=0;x<width;x+=maxW,index++){
//			drawText(canvas,text2,x+index*maxW,height*0.2f-20-index*maxH,p,angle);
//			drawText(canvas,text1,x+index*maxW,height*0.2f-50-fontHeight-index*maxH , p,angle);
//		}


        for (float x = width * 0.7f, index = 0; x < width; x += maxW, index++) {
            drawText(canvas, text2, x + index * maxW, height - 20 - index * maxH, p, angle);
            drawText(canvas, text1, x + index * maxW, height - 40 - fontHeight - index * maxH, p, angle);
        }

        canvas.save();
        return bm;
    }

    private static void drawText(Canvas canvas, String text, float x, float y, Paint p, float angle) {
        if (angle != 0) {
            canvas.rotate(angle, x, y);
        }
        canvas.drawText(text, x, y, p);
        if (angle != 0) {
            canvas.rotate(-angle, x, y);
        }
    }

    /**
     * Bitmap转byte[]
     *
     * @param bmp
     * @param needRecycle
     * @return
     */
    public static byte[] bmpToByteArray(final Bitmap bmp, boolean needRecycle) {
        if (bmp == null)
            return null;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }
        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
