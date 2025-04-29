package com.tourismclient.cultoura.utils;


import android.content.Context;

import java.util.Set;

public class SharedPreferences {

    static android.content.SharedPreferences sharedPreferences;
    static final String prefFile = "com.ayuvya.ayuvya.Pref";
    static final String quesFile = "com.ayuvya.ayuvya.QuesPref";

    public static void setVariableInPreferences(String key, String value, Context context) {
        sharedPreferences = context.getSharedPreferences(prefFile, Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();

    }

    public static String getVariablesInPreferences(String key, Context context) {
        sharedPreferences = context.getSharedPreferences(prefFile, Context.MODE_PRIVATE);
        String value = sharedPreferences.getString(key, null);

        if (value == null) {
            return "";
        }

        return value;
    }

    public static void setQuestionNoInPrefrences(String key, Set value, Context context){
        sharedPreferences=context.getSharedPreferences(quesFile,Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putStringSet(key, value);
        editor.commit();
    }
    public static String getQuestionNoFromPrefrences(String key,Context context){
        sharedPreferences=context.getSharedPreferences(quesFile,Context.MODE_PRIVATE);
        String value=sharedPreferences.getString(key,null);

        if(value==null){
            value="";
        }
        return value;
    }


    public static void setBooleanInPreferences(String key, Boolean value, Context context) {
        sharedPreferences = context.getSharedPreferences(prefFile, Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();

    }

    public static Boolean getBooleanInPreferences(String key, Context context) {
        sharedPreferences = context.getSharedPreferences(prefFile, Context.MODE_PRIVATE);
        Boolean value = sharedPreferences.getBoolean(key, false);
        return value;
    }


}

