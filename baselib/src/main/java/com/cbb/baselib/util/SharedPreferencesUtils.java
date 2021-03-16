package com.cbb.baselib.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.cbb.baselib.base.BaseApplication;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SharedPreferencesUtils {
    private static SharedPreferences mSharedPreferences;

    /**
     * @param key   本地数据对应的key
     * @param value 本地数据对应的value
     */
    public static void addData(String key, String value) {
        init();

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void addData(String key, boolean value) {
        init();

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static void addData(String key, int value) {
        init();
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void addStringSetData(String key, Set<String> value) {
        init();
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putStringSet(key, value);
        editor.commit();
    }

    public static void addStringListData(String key, List<String> value) {
        init();
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        String s = new Gson().toJson(value);
        editor.putString(key, s);
        editor.commit();
    }

    public static List<String> readStringListData(String key) {
        init();
        String string = mSharedPreferences.getString(key, "");
        List<String> list = getObjectList(string, String.class);
        return list;
    }

    public static <T> void addListData(String key, List<T> value) {
        init();
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        String s = new Gson().toJson(value);
        editor.putString(key, s);
        editor.commit();
    }
    public static <T> void addObjData(String key, T value) {
        init();
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        String s = new Gson().toJson(value);
        editor.putString(key, s);
        editor.commit();
    }
    public static <T> T readObjData(String key,  Class<T> cls) {
        init();
        String string = mSharedPreferences.getString(key, "");
        return new Gson().fromJson(string,cls);
    }

    public static <T> List<T> readListData(String key , Class<T> cls) {
        init();
        String string = mSharedPreferences.getString(key, "");
        List<T> list = getObjectList(string, cls);
        return list;
    }

    public static <T> List<T> getObjectList(String jsonString, Class<T> cls) {
        List<T> list = new ArrayList<T>();
        try {
            Gson gson = new Gson();
            JsonArray arry = new JsonParser().parse(jsonString).getAsJsonArray();
            for (JsonElement jsonElement : arry) {
                list.add(gson.fromJson(jsonElement, cls));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    private static void init() {
        if (mSharedPreferences == null) {
            mSharedPreferences = BaseApplication.instance.getSharedPreferences(BaseApplication.instance.getPackageName(), Context.MODE_PRIVATE);
        }
    }

    public static Set<String> readStringSerData(String key) {
        init();
        return mSharedPreferences.getStringSet(key, new HashSet<String>());
    }

    public static String readData(String key) {
        return readData(key, "");
    }

    public static boolean readBooleanData(String key, Boolean defaultStr) {
        init();
        return mSharedPreferences.getBoolean(key, defaultStr);
    }

    public static int readIntData(String key, int defaultStr) {
        init();
        return mSharedPreferences.getInt(key, defaultStr);
    }

    public static String readData(String key, String defaultStr) {
        init();

        return mSharedPreferences.getString(key, defaultStr);
    }

    public static SharedPreferences getInstance() {
        init();

        return mSharedPreferences;
    }

    public static void clearShared() {
        init();

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

}
