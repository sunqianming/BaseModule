package com.base.module.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.base.module.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * 二维码工具类
 * Created by SQM on 2018/11/7.
 */

public class QRCodeUtil {

    private static final int BLACK = 0xff000000;
    private static final int PADDING_SIZE_MIN = 5; // 最小留白长度,单位: px

    /**
     * 生成二维码图片
     *
     * @param content
     * @param width
     * @param height
     * @return
     */
    public static Bitmap generateBitmap(String content, int width, int height) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, String> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        try {
            BitMatrix encode = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (encode.get(j, i)) {
                        pixels[i * width + j] = 0x00000000;
                    } else {
                        pixels[i * width + j] = 0xffffffff;
                    }
                }
            }
            return Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成二维码图片（需裁剪周围的空白区域）
     *
     * @param context
     * @param str
     * @param widthAndHeight
     * @return
     */
    public static Bitmap createQRCode1(Context context, String str, int widthAndHeight) {
        try {
            Hashtable hints = new Hashtable();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            BitMatrix matrix = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, widthAndHeight, widthAndHeight, hints);
            int width = matrix.getWidth();
            int height = matrix.getHeight();
            int[] pixels = new int[width * height];
            boolean isFirstBlackPoint = false;
            int startX = 0;
            int startY = 0;
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (matrix.get(x, y)) {
                        if (isFirstBlackPoint == false) {
                            isFirstBlackPoint = true;
                            startX = x;
                            startY = y;
                        }
                        pixels[y * width + x] = BLACK;
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height); // 剪切中间的二维码区域，减少padding区域
            if (startX <= PADDING_SIZE_MIN)
                return bitmap;
            int x1 = startX - PADDING_SIZE_MIN;
            int y1 = startY - PADDING_SIZE_MIN;
            if (x1 < 0 || y1 < 0) return bitmap;
            int w1 = width - x1 * 2;
            int h1 = height - y1 * 2;
            Bitmap bitmapQRCode = Bitmap.createBitmap(bitmap, x1, y1, w1, h1);
            Bitmap bitmapLogo = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher_round);
            return addLogo(bitmapQRCode, bitmapLogo);
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 给二维码图片中间添加logo图片
     *
     * @param bitmapQRCode 原二维码
     * @param bitmapLogo   添加的logo
     * @return
     */
    public static Bitmap addLogo(Bitmap bitmapQRCode, Bitmap bitmapLogo) {
        if (bitmapQRCode == null) {
            return null;
        }
        if (bitmapQRCode == null || bitmapLogo == null) {
            return bitmapQRCode;
        }
        //这里得到原二维码bitmap的数据
        int srcWidth = bitmapQRCode.getWidth();
        int srcHeight = bitmapQRCode.getHeight();
        //logo的Width和Height
        int logoWidth = bitmapLogo.getWidth();
        int logoHeight = bitmapLogo.getHeight();
        if (srcWidth == 0 || srcHeight == 0) {
            return null;
        }
        if (logoWidth == 0 || logoHeight == 0) {
            return bitmapQRCode;
        }
        //logo大小为二维码整体大小的1/5，也可以自定义多大，越小越好
        //二维码有一定的纠错功能，中间图片越小，越容易纠错
        float scaleFactor = srcWidth * 1.0f / 5 / logoWidth;
        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
        try {
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(bitmapQRCode, 0, 0, null);
            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
            canvas.drawBitmap(bitmapLogo, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);
            canvas.save();
            canvas.restore();
        } catch (Exception e) {
            bitmap = null;
            e.getStackTrace();
        }
        return bitmap;
    }
}

