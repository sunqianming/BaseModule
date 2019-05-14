package com.base.module.db;

import android.content.Context;
import android.content.SharedPreferences;

import com.base.module.base.BaseApplication;

import java.util.Set;

/**
 * Created by Administrator on 2016/4/20.
 */
public class SharedPreferencesHelper {

    SharedPreferences preferences;

    private SharedPreferencesHelper() {
        preferences = BaseApplication.getInstance().getSharedPreferences("easylifdata", Context.MODE_PRIVATE);
    }

    private static SharedPreferencesHelper instance = new SharedPreferencesHelper();

    //获取SharedPreferences
    public static SharedPreferencesHelper getInstance() {
        return instance;
    }


    /***
     * 保存数据
     * @param key
     * @param value
     */
    public void put(String key, boolean value) {
        preferences.edit().putBoolean(key, value).commit();
    }

    /***
     * 保存数据
     * @param key
     * @param value
     */
    public void put(String key, String value) {
        preferences.edit().putString(key, value).commit();
    }

    /**
     * 保存数据
     *
     * @param key
     * @param value
     */
    public void put(String key, long value) {
        preferences.edit().putLong(key, value).commit();
    }

    /***
     * 保存数据
     * @param key
     * @param value
     */
    public void put(String key, float value) {
        preferences.edit().putFloat(key, value).commit();
    }

    /***
     * 保存数据
     * @param key
     * @param value
     */
    public void put(String key, Set<String> value) {
        preferences.edit().putStringSet(key, value).commit();
    }

    /**
     * 获取数据
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        return preferences.getBoolean(key, defaultValue);
    }

    /**
     * 获取数据
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public String getString(String key, String defaultValue) {
        return preferences.getString(key, defaultValue);
    }

    /***
     * 获取数据
     * @param key
     * @param defaultValue
     * @return
     */
    public float getFloat(String key, float defaultValue) {
        return preferences.getFloat(key, defaultValue);
    }

    /***
     * 获取数据
     * @param key
     * @param defaultValue
     * @return
     */
    public long getLong(String key, long defaultValue) {
        return preferences.getLong(key, defaultValue);
    }

    /***
     * 获取数据
     * @param key
     * @param defaultValue
     * @return
     */
    public Set<String> getSet(String key, Set<String> defaultValue) {
        return preferences.getStringSet(key, defaultValue);
    }

    /**
     * 删除数据
     *
     * @param key
     */
    public void clear(String key) {
        preferences.edit().remove(key).commit();
    }

    /**
     * 删除全部数据
     */
    public void clearAll() {
        preferences.edit().clear().commit();
    }

    /**
     * 是否存在数据
     *
     * @param key
     * @return
     */
    public boolean hasKey(String key) {
        return preferences.contains(key);
    }
}
