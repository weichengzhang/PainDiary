package com.example.mobilepaindiary.sp;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceUtil {
    private static SharedPreferenceUtil sharedPreferenceUtil;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences userSP;


    private final static String KEY = "sp_general";

    private final static String KEY_USER = "sp_user";
    private final static String KEY_USER_NAME = "sp_user_name";
    private final static String KEY_USER_DOB = "sp_user_dob";


    private SharedPreferenceUtil(Context context) {
        sharedPreferences = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        userSP = context.getSharedPreferences(KEY_USER, Context.MODE_PRIVATE);
    }

    public static SharedPreferenceUtil getInstance(Context context) {
        if (sharedPreferenceUtil == null) {
            sharedPreferenceUtil = new SharedPreferenceUtil(context);
        }
        return sharedPreferenceUtil;
    }

    /**
     * 设置Usr name, String类型值
     *
     * @param value
     */
    public void putUserName(String value) {
        SharedPreferences.Editor editor = userSP.edit();
        editor.putString(KEY_USER_NAME, value);
        editor.apply();
    }
    public String getUserName() {
        return getString(KEY_USER_NAME, "");
    }

    public void putUserDOB(String value) {
        SharedPreferences.Editor editor = userSP.edit();
        editor.putString(KEY_USER_DOB, value);
        editor.apply();
    }
    /**
     * 设置String类型值
     *
     * @param key
     * @param value
     */
    public void putString(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * 设置long类型值
     *
     * @param key
     * @param value
     */
    public void putLong(String key, long value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    /**
     * 设置int类型值
     *
     * @param key
     * @param value
     */
    public void putInt(String key, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    /**
     * 设置Boolean类型值
     *
     * @param key
     * @param value
     */
    public void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    /**
     * 设置Float类型值
     *
     * @param key
     * @param value
     */
    public void putFloat(String key, float value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    /**
     * 获取key相对应的value，如果不设默认参数，默认值为""
     *
     * @param key
     * @return
     */
    public String getString(String key) {
        return getString(key, "");
    }

    /**
     * 获取key相对应的value，如果不设默认参数，默认值为""
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public String getString(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    /**
     * 获取key相对应的value，如果不设默认参数，默认值为false
     *
     * @param key
     * @return
     */
    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    /**
     * 获取key相对应的value，如果不设默认参数，默认值为false
     *
     * @param key
     * @param defalutValue
     * @return
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    /**
     * 获取key相对应的value，如果不设默认参数，默认值为0
     *
     * @param key
     * @return
     */
    public int getInt(String key) {
        return getInt(key, 0);
    }

    /**
     * 获取key相对应的value，如果不设默认参数，默认值为0
     *
     * @param key
     * @return
     */
    public int getInt(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    /**
     * 获取key相对应的value，如果不设默认参数，默认值为0
     *
     * @param key
     * @return
     */
    public long getLong(String key) {
        return getLong(key, 0L);
    }

    /**
     * 获取key相对应的value，如果不设默认参数，默认值为0
     *
     * @param key
     * @param defalutValue
     * @return
     */
    public long getLong(String key, Long defaultValue) {
        return sharedPreferences.getLong(key, defaultValue);
    }

    /**
     * 获取key相对应的value，如果不设默认参数，默认值为0
     *
     * @param key
     * @return
     */
    public float getFloat(String key) {
        return getFloat(key, 0f);
    }

    /**
     * 获取key相对应的value，如果不设默认参数，默认值为0
     *
     * @param key
     * @param defalutValue
     * @return
     */
    public float getFloat(String key, Float defaultValue) {
        return sharedPreferences.getFloat(key, defaultValue);
    }

    /**
     * 判断是否存在此字段
     */
    public boolean contains(String key) {
        return sharedPreferences.contains(key);
    }

    /**
     * 判断是否存在此字段
     */
    public boolean has(String key) {
        return sharedPreferences.contains(key);
    }

    /**
     * 删除sharedPreferences文件中对应的Key和value
     */
    public boolean remove(String key) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        return editor.commit();
    }

    /**
     * 删除所有SP数据，一般是logout时候激活
     * @return
     */
    public boolean removeAll() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        return editor.commit();
    }
}
