package com.base.module.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/***
 *@author SQM
 * @Description: 时间格式化工具类
 */
public class TimeHelper {

    // 时间戳格式化
    public static final SimpleDateFormat m_Stamp1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat m_Stamp2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    public static final SimpleDateFormat m_Stamp3 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static final SimpleDateFormat m_Stamp4 = new SimpleDateFormat("yyyy/MM/dd HH:mm");
    public static final SimpleDateFormat m_Stamp5 = new SimpleDateFormat("yyyy年MM月dd HH时mm分ss秒");
    public static final SimpleDateFormat m_Stamp6 = new SimpleDateFormat("yyyy年MM月dd HH时mm分");
    public static final SimpleDateFormat m_Stamp7 = new SimpleDateFormat("yyyyMMddHHmmss");
    public static final SimpleDateFormat m_Stamp8 = new SimpleDateFormat("MM月dd日 HH:mm");
    public static final SimpleDateFormat m_Stamp9 = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
    public static final SimpleDateFormat m_Stamp10 = new SimpleDateFormat("MM-dd HH:mm");

    // 日期格式
    public static final SimpleDateFormat m_Date1 = new SimpleDateFormat("yyyy-MM--dd");
    public static final SimpleDateFormat m_Date2 = new SimpleDateFormat("yyyy/MM/dd");
    public static final SimpleDateFormat m_Date3 = new SimpleDateFormat("yyyy年MM月dd日");
    public static final SimpleDateFormat m_Date4 = new SimpleDateFormat("yyyyMMdd");
    public static final SimpleDateFormat m_Date5 = new SimpleDateFormat("yyyy");

    // 时间格式化
    public static final SimpleDateFormat m_time1 = new SimpleDateFormat("HH:mm:ss");
    public static final SimpleDateFormat m_time2 = new SimpleDateFormat("HH时mm分ss秒");
    public static final SimpleDateFormat m_time3 = new SimpleDateFormat("HHmmss");

    /***
     *
     * @Title: getStamp
     * @Description: 获取时间戳
     * @param @param type 1,yyyy-MM--dd HH:mm:ss;2,yyyy/MM/dd
     *        HH:mm:ss;3,yyyy-MM--dd HH:mm;4,yyyy/MM/dd HH:mm;5,yyyy年MM月dd
     *        HH时mm分ss秒;6,yyyy年MM月dd HH是mm分;7,yyyyMMddHHmmss
     * @param @param date date对象
     * @return String 时间戳
     */
    public static String getStamp(int type, Date date) {
        switch (type) {
            case 1:
                return m_Stamp1.format(date);
            case 2:
                return m_Stamp2.format(date);
            case 3:
                return m_Stamp3.format(date);
            case 4:
                return m_Stamp4.format(date);
            case 5:
                return m_Stamp5.format(date);
            case 6:
                return m_Stamp6.format(date);
            case 7:
                return m_Stamp7.format(date);
            case 8:
                return m_Stamp8.format(date);
            case 9:
                return m_Stamp9.format(date);
            case 10:
                return m_Stamp10.format(date);
            default:
                return "";
        }
    }

    /****
     *
     *
     * @Title: getDate
     * @Description: 获取日期
     * @param @param type ,1,yyyy-MM--dd;2,yyyy/MM/dd;3,yyyy年MM月dd日;4,yyyyMMdd
     * @param @param date date对象
     * @return String 返回类型
     */
    public static String getDate(int type, Date date) {
        switch (type) {
            case 1:
                return m_Date1.format(date);
            case 2:
                return m_Date2.format(date);
            case 3:
                return m_Date3.format(date);
            case 4:
                return m_Date4.format(date);
            case 5:
                return m_Date5.format(date);
            default:
                return "";
        }
    }

    /***
     *
     * @Title: getTime
     * @Description: 获取时间
     * @param @param type,1,HH:mm:ss;2,HH时mm分ss秒;3,HHmmss
     * @param @param date date对象
     * @return String 返回类型
     */
    public static String getTime(int type, Date date) {
        switch (type) {
            case 1:
                return m_time1.format(date);
            case 2:
                return m_time2.format(date);
            case 3:
                return m_time3.format(date);
            default:
                return "";
        }
    }

    /***
     *
     * @Title: getCurrentDate
     * @Description: 获取当前时间戳
     * @param @param type 1,yyyy-MM--dd HH:mm:ss;2,yyyy/MM/dd
     *        HH:mm:ss;3,yyyy-MM--dd HH:mm;4,yyyy/MM/dd HH:mm;5,yyyy年MM月dd
     *        HH时mm分ss秒;6,yyyy年MM月dd HH是mm分;7,yyyyMMddHHmmss
     * @return String 时间戳
     */
    public static String getCurrentDate(int type) {
        return getDate(type, new Date());
    }

    /***
     *
     * @Title: getCurrentTime
     * @Description: 获取当前时间
     * @param @param type,1,HH:mm:ss;2,HH时mm分ss秒;3,HHmmss
     * @return String 当前时间
     */
    public static String getCurrentTime(int type) {
        return getTime(type, new Date());
    }

    /****
     *
     *
     * @Title: getCurrentStamp
     * @Description: 获取当前日期
     * @param @param type ,1,yyyy-MM--dd;2,yyyy/MM/dd;3,yyyy年MM月dd日;4,yyyyMMdd
     * @return String 当前日期
     */
    public static String getCurrentStamp(int type) {
        return getStamp(type, new Date());
    }

    /**
     * @param @param  stampStr 时间戳字符串
     * @param @param  type 1,yyyy-MM--dd HH:mm:ss;2,yyyy/MM/dd
     *                HH:mm:ss;3,yyyy-MM--dd HH:mm;4,yyyy/MM/dd HH:mm;5,yyyy年MM月dd
     *                HH时mm分ss秒;6,yyyy年MM月dd HH是mm分;7,yyyyMMddHHmmss
     * @param @return
     * @param @throws ParseException 参数
     * @return Date 返回类型
     * @Title: parseStamp
     * @Description: 解析时间戳到Date对象
     */
    public static Date parseStamp(String stampStr, int type) throws ParseException {
        switch (type) {
            case 1:
                return m_Stamp1.parse(stampStr);
            case 2:
                return m_Stamp2.parse(stampStr);
            case 3:
                return m_Stamp3.parse(stampStr);
            case 4:
                return m_Stamp4.parse(stampStr);
            case 5:
                return m_Stamp5.parse(stampStr);
            case 6:
                return m_Stamp6.parse(stampStr);
            case 7:
                return m_Stamp7.parse(stampStr);
            default:
                return null;
        }
    }

    /**
     * @param @param yyyyMMddHHmmss 源时间字符串
     * @param @param type 1,yyyy-MM--dd HH:mm:ss;2,yyyy/MM/dd
     *               HH:mm:ss;3,yyyy-MM--dd HH:mm;4,yyyy/MM/dd HH:mm;5,yyyy年MM月dd
     *               HH时mm分ss秒;6,yyyy年MM月dd HH是mm分;7,yyyyMMddHHmmss
     * @return String 返回类型
     * @throws ParseException
     * @Title: formatStamp
     * @Description:格式化时间戳
     */
    public static String formatStamp(String yyyyMMddHHmmss, int type) throws ParseException {
        Date date = m_Stamp7.parse(yyyyMMddHHmmss);
        switch (type) {
            case 1:
                return m_Stamp1.format(date);
            case 2:
                return m_Stamp2.format(date);
            case 3:
                return m_Stamp3.format(date);
            case 4:
                return m_Stamp4.format(date);
            case 5:
                return m_Stamp5.format(date);
            case 6:
                return m_Stamp6.format(date);
            case 7:
                return m_Stamp7.format(date);
            default:
                return "";
        }
    }

    /***
     * 将毫秒转换为分钟显示
     * @param ms
     * @return
     */
    public static String ss2mmss(int ms) {
        String s = "";
        int ss = ms / 1000;
        String strSs = String.valueOf(ss % 60);
        strSs = strSs.length() <= 1 ? "0" + strSs : strSs;
        String strMm = String.valueOf((int) (ss / 60));
        strMm = strMm.length() <= 1 ? "0" + strMm : strMm;
        s = strMm + ":" + strSs;
        return s;
    }

    /**
     * 距离此刻的时间
     *
     * @param time
     * @return
     */
    public static String str2ExpiringDate(String time) {
        String value = null;
        try {
            Long t = m_Stamp1.parse(time).getTime() - new Date().getTime();
            if (t <= 0) {
                value = "";
            } else if (t / 1000 / 60 <= 60) {
                value = "还有" + t / 1000 / 60 + "分钟";
            } else if (t / 1000 / 60 / 60 <= 24) {
                value = "还有" + t / 1000 / 60 / 60 + "小时";
            } else {
                value = "还有" + t / 1000 / 60 / 60 / 24 + "天";
            }
        } catch (ParseException e) {

        }
        return value;
    }

    /**
     * 距离此刻的时间
     *
     * @param time
     * @return
     */
    public static String long2ExpiringDate(Long time) {
        String value;
        Long t = time - new Date().getTime();
        if (t <= 0) {
            value = "";
        } else if (t / 1000 / 60 <= 60) {
            value = "还有" + t / 1000 / 60 + "分";
        } else if (t / 1000 / 60 / 60 <= 24) {
            value = "还有" + t / 1000 / 60 / 60 + "时" + (t - (t / 1000 / 60 / 60) * 60 * 60 * 1000) / 1000 / 60 + "分";
        } else {
            value = "还有" + t / 1000 / 60 / 60 / 24 + "天";
        }
        return value;
    }
}
