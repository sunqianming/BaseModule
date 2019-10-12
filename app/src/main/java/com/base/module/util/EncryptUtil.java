package com.base.module.util;

import android.content.Context;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Administrator on 2016/6/21.
 */
public class EncryptUtil {


    // MD5加密
    public static String encryptMD5(String str) {
        try {
            byte[] bytes = MessageDigest.getInstance("MD5").digest(
                    str.getBytes(Charset.defaultCharset()));
            return CommonUtil.hexEncode(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // SHA-256加密
    public static String encryptSHA256(String str) {
        try {
            byte[] bytes = MessageDigest.getInstance("SHA-256").digest(
                    str.getBytes(Charset.defaultCharset()));
            return CommonUtil.hexEncode(bytes).toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static byte[] get3desKey(Context context){
        byte [] keys=new byte[24];
        String bb=DeviceUtil.getIMEI(context)+"01234567890asdfghjklqwertyuioasdf";
        byte[] v_keys = bb.getBytes();
        System.arraycopy(v_keys, 0, keys, 0, 24);
        return keys;
    }


    public static String encode3DES(Context context, String data){
        try {
            byte[] ret = encryptDESede(get3desKey(context), data.getBytes("utf-8"));
            return CommonUtil.hexEncode(ret);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String decode3DES(Context context, String data){
        try {
            byte[] ret = decryptDESede(get3desKey(context), CommonUtil.hexDecode(data));
            return new String(ret,"utf-8");
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public static byte[] encryptDESede(byte[] keybyte, byte[] src)
    {
        try
        {
            SecretKey deskey = new SecretKeySpec(keybyte, "DESede");

            Cipher c1 = Cipher.getInstance("DESede/CBC/PKCS5Padding");
            IvParameterSpec ivSpec = new IvParameterSpec(new byte[8]);
            c1.init(1, deskey, ivSpec);
            return c1.doFinal(src);
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }
    public static byte[] decryptDESede(byte[] keybyte, byte[] src)
    {
        try
        {
            SecretKey deskey = new SecretKeySpec(keybyte, "DESede");

            Cipher c1 = Cipher.getInstance("DESede/CBC/PKCS5Padding");
            IvParameterSpec ivSpec = new IvParameterSpec(new byte[8]);
            c1.init(2, deskey, ivSpec);
            return c1.doFinal(src);
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }
}
