package com.xiaosheng.learnapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

public class SpUtil {

    // 获取 SharedPreferences 对象 (默认使用 AppContext)
    public static SharedPreferences getSp(String spName) {
        return getSp(AppContextUtil.getAppContext(), spName);
    }

    // 获取 SharedPreferences 对象 (允许传入 Context)
    public static SharedPreferences getSp(Context context, String spName) {
        return context.getSharedPreferences(spName, Context.MODE_PRIVATE);
    }

    // 默认保存数据 (使用 apply() 方式，使用 AppContext)
    public static void put(String spName, String key, String value) {
        put(AppContextUtil.getAppContext(), spName, key, value, true);
    }

    // 允许传入自定义 Context 进行保存
    public static void put(Context context, String spName, String key, String value) {
        put(context, spName, key, value, true);
    }

    public static void put(String spName, String key, Long value) {
        put(AppContextUtil.getAppContext(), spName, key, value, true);
    }

    public static void put(Context context, String spName, String key, Long value) {
        put(context, spName, key, value, true);
    }

    public static void put(String spName, String key, Boolean value) {
        put(AppContextUtil.getAppContext(), spName, key, value, true);
    }

    public static void put(Context context, String spName, String key, Boolean value) {
        put(context, spName, key, value, true);
    }

    public static void put(String spName, String key, Integer value) {
        put(AppContextUtil.getAppContext(), spName, key, value, true);
    }

    public static void put(Context context, String spName, String key, Integer value) {
        put(context, spName, key, value, true);
    }

    public static void put(String spName, String key, Float value) {
        put(AppContextUtil.getAppContext(), spName, key, value, true);
    }

    public static void put(Context context, String spName, String key, Float value) {
        put(context, spName, key, value, true);
    }

    public static void put(String spName, String key, Set<String> value) {
        put(AppContextUtil.getAppContext(), spName, key, value, true);
    }

    public static void put(Context context, String spName, String key, Set<String> value) {
        put(context, spName, key, value, true);
    }

    // 重载方法，允许选择是否 apply 或 commit
    public static void put(Context context, String spName, String key, String value, boolean apply) {
        SharedPreferences.Editor editor = getEditor(context, spName);
        editor.putString(key, value);
        save(editor, apply);
    }

    public static void put(Context context, String spName, String key, Long value, boolean apply) {
        SharedPreferences.Editor editor = getEditor(context, spName);
        editor.putLong(key, value);
        save(editor, apply);
    }

    public static void put(Context context, String spName, String key, Boolean value, boolean apply) {
        SharedPreferences.Editor editor = getEditor(context, spName);
        editor.putBoolean(key, value);
        save(editor, apply);
    }

    public static void put(Context context, String spName, String key, Integer value, boolean apply) {
        SharedPreferences.Editor editor = getEditor(context, spName);
        editor.putInt(key, value);
        save(editor, apply);
    }

    public static void put(Context context, String spName, String key, Float value, boolean apply) {
        SharedPreferences.Editor editor = getEditor(context, spName);
        editor.putFloat(key, value);
        save(editor, apply);
    }

    public static void put(Context context, String spName, String key, Set<String> value, boolean apply) {
        SharedPreferences.Editor editor = getEditor(context, spName);
        editor.putStringSet(key, value);
        save(editor, apply);
    }

    // 内部方法，决定使用 apply() 还是 commit()
    private static void save(SharedPreferences.Editor editor, boolean apply) {
        if (apply) {
            editor.apply();  // 异步保存
        } else {
            editor.commit();  // 同步保存
        }
    }

    public static String get(String spName, String key, String defaultValue) {
        return get(AppContextUtil.getAppContext(), spName, key, defaultValue);
    }

    public static String get(Context context, String spName, String key, String defaultValue) {
        SharedPreferences sp = getSp(context, spName);
        return sp.getString(key, defaultValue);
    }

    public static Long get(String spName, String key, Long defaultValue) {
        return get(AppContextUtil.getAppContext(), spName, key, defaultValue);
    }

    public static Long get(Context context, String spName, String key, Long defaultValue) {
        SharedPreferences sp = getSp(context, spName);
        return sp.getLong(key, defaultValue);
    }

    public static Boolean get(String spName, String key, Boolean defaultValue) {
        return get(AppContextUtil.getAppContext(), spName, key, defaultValue);
    }

    public static Boolean get(Context context, String spName, String key, Boolean defaultValue) {
        SharedPreferences sp = getSp(context, spName);
        return sp.getBoolean(key, defaultValue);
    }

    public static Integer get(String spName, String key, Integer defaultValue) {
        return get(AppContextUtil.getAppContext(), spName, key, defaultValue);
    }

    public static Integer get(Context context, String spName, String key, Integer defaultValue) {
        SharedPreferences sp = getSp(context, spName);
        return sp.getInt(key, defaultValue);
    }

    public static Float get(String spName, String key, Float defaultValue) {
        return get(AppContextUtil.getAppContext(), spName, key, defaultValue);
    }

    public static Float get(Context context, String spName, String key, Float defaultValue) {
        SharedPreferences sp = getSp(context, spName);
        return sp.getFloat(key, defaultValue);
    }

    public static Set<String> get(String spName, String key, Set<String> defaultValue) {
        return get(AppContextUtil.getAppContext(), spName, key, defaultValue);
    }

    public static Set<String> get(Context context, String spName, String key, Set<String> defaultValue) {
        SharedPreferences sp = getSp(context, spName);
        return sp.getStringSet(key, defaultValue);
    }

    // 获取 Editor 对象
    private static SharedPreferences.Editor getEditor(Context context, String spName) {
        SharedPreferences sp = getSp(context, spName);
        return sp.edit();
    }
}
