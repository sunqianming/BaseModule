package com.base.module.cache;

import android.app.Activity;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * * @author SQM
 * <p>
 * 存放全局临时数据,如果确定此数据不用了需要立即清理
 */
public class GlobalDataHelper {

    public List<Activity> atyList = new LinkedList<>();
    private final static Map<String, String> mData = new ConcurrentHashMap<>(10);

    private GlobalDataHelper() {
    }

    private static GlobalDataHelper mInstance = new GlobalDataHelper();

    /***
     * 获取实例
     *
     * @return
     */
    public static GlobalDataHelper getInstance() {
        return mInstance;
    }

    private boolean isShutDown = false;

    public void setIsShutDown(boolean isShutDown) {
        this.isShutDown = isShutDown;
    }

    /***
     * 存放数据
     *
     * @param key
     * @param value
     */
    public void put(String key, boolean value) {
        mData.put(key, Boolean.toString(value));
    }

    /***
     *
     * @Title: put
     * @Description: 存放数据
     * @param @param key
     * @param @param value 参数
     * @return void 返回类型
     * @author SQM
     * @date 2014-8-27 下午1:49:04
     */
    public void put(String key, long value) {
        mData.put(key, "" + value);
    }

    /***
     *
     * @Title: put
     * @Description: 设置值
     * @param @param key
     * @param @param value 参数
     * @return void 返回类型
     * @author SQM
     * @date 2014-8-27 下午1:50:29
     */
    public void put(String key, String value) {
        if (mData != null) {
            mData.put(key, value);
        }
    }

    /***
     *
     * @Title: put
     * @Description: 存放json, 存储和取出来的不是同一个对象
     * @param @param key
     * @param @param value 参数
     * @return void 返回类型
     * @author SQM
     * @date 2014-10-15 下午3:16:11
     */
    public void put(String key, JSONObject value) {
        mData.put(key, value.toString());
    }

    /***
     *
     * @Title: put
     * @Description: 存放json, 存储和取出来的不是同一个对象
     * @param @param key
     * @param @param value 参数
     * @return void 返回类型
     * @author SQM
     * @date 2014-10-15 下午3:17:03
     */
    public void put(String key, JSONArray value) {
        mData.put(key, value.toString());
    }

    /***
     * 获取数据
     *
     * @param key
     * @return
     */
    public String get(String key) {
        return mData.get(key);
    }

    /**
     * 获取数据带默认值
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public String get(String key, String defaultValue) {
        String data = get(key);
        return data != null ? data : defaultValue;
    }

    /***
     * 获取字符串
     *
     * @param key
     * @return
     */
    public String getString(String key) {
        String v_temp = get(key);
        return v_temp == null ? "" : v_temp.toString();
    }

    /***
     *
     * @Title: getLong
     * @Description: 取long值
     * @param @param key
     * @param @return 参数
     * @return long 返回类型
     * @throws
     * @author SQM
     * @date 2014-8-27 下午2:10:27
     */
    public long getLong(String key) {
        try {
            String v_temp = get(key);
            if (v_temp == null) {
                return 0;
            } else {
                return Long.parseLong(v_temp);
            }
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * @param @param  key
     * @param @param  defaultValue
     * @param @return 参数
     * @return boolean 返回类型
     * @throws
     * @Title: getBoolean
     * @Description: 获取boolean 值
     * @author SQM
     * @date 2014-8-27 下午2:11:57
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        try {
            String v_temp = get(key);
            if (v_temp == null) {
                return defaultValue;
            } else {
                return Boolean.parseBoolean(v_temp);
            }
        } catch (Throwable e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    /**
     * @param @param  key
     * @param @return 参数
     * @return JSONObject 返回类型
     * @throws
     * @Title: getJSONObject
     * @Description: 获取JSONobject
     * @author SQM
     * @date 2014-8-27 下午2:14:58
     */
    public JSONObject getJSONObject(String key) {
        try {
            String v_temp = get(key);
            if (v_temp == null) {
                return null;
            } else {
                return new JSONObject(v_temp);
            }
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param @param  key
     * @param @return 参数
     * @return JSONObject 返回类型
     * @throws
     * @Title: getJSONArray
     * @Description: 获取jsonarray
     * @author SQM
     * @date 2014-8-27 下午2:15:27
     */
    public JSONArray getJSONArray(String key) {
        try {
            String v_temp = get(key);
            if (v_temp == null) {
                return null;
            } else {
                return new JSONArray(v_temp);
            }
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    /***
     * 删除数据
     *
     * @param key
     */
    public void clear(String key) {
        mData.remove(key);
    }

    /***
     * 删除所有数据
     */
    public void clearAll() {
        mData.clear();
        if (atyList != null) {
            atyList.clear();
        }
    }

    /***
     * 检测是否存在对象
     *
     * @param key
     * @return
     */
    public boolean containKey(String key) {
        return mData.containsKey(key);
    }

    /**
     * 检测是否存在对象
     *
     * @param obj
     * @return
     */
    public boolean containsValue(Object obj) {
        return mData.containsValue(obj);
    }

    /****
     *
     * @Title: getDatas
     * @Description: 获取所有数据
     * @param @return 参数
     * @return Map<String                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               ,                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               String> 返回类型
     * @author SQM
     * @date 2014-10-13 上午10:16:27
     */
    public Map<String, String> getDatas() {
        return mData;
    }

    /**
     * @param @param bundle 参数
     * @return void 返回类型
     * @Title: onRestoreInstanceState
     * @Description: 恢复GlobalData, 在activity中onRestoreInstanceState调用
     * @author SQM
     * @date 2014-11-4 上午9:41:18
     */
    public void onRestoreInstanceState(Bundle bundle) {
        if (bundle == null || isShutDown) {
            return;
        }
        synchronized (mData) {
            Set<String> v_keys = bundle.keySet();
            for (String k : v_keys) {
                String v_dataKey = k.substring(3);
                if (k.startsWith("gd_") && !mData.containsKey(v_dataKey)) {
                    mData.put(v_dataKey, bundle.getString(k));
                }
            }
        }
    }

    /**
     * @param @param bundle 参数
     * @return void 返回类型
     * @Title: onSaveInstanceState
     * @Description: 恢复保存, 在activity中onSaveInstanceState调用
     * @author SQM
     * @date 2014-11-4 上午9:42:09
     */
    public void onSaveInstanceState(Bundle bundle) {
        if (bundle == null || isShutDown) {
            return;
        }
        synchronized (mData) {
            Set<String> v_keys = mData.keySet();
            for (String k : v_keys) {
                if (mData.get(k) != null) {
                    bundle.putString("gd_" + k, mData.get(k));
                }
            }
        }
    }


    /**
     * 添加activity
     *
     * @param aty
     */
    public synchronized void addAty(Activity aty) {
        if (!atyList.contains(aty)) {
            atyList.add(aty);
        }
    }

    /**
     * 移除activity
     *
     * @param aty
     */
    public synchronized void removeAty(Activity aty) {
        if (atyList.contains(aty)) {
            atyList.remove(aty);
        }
    }

    public List<Activity> getAtyList() {
        return atyList;
    }
}
