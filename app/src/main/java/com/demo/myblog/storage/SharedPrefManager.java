package com.demo.myblog.storage;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    // shared preference name
    private static final String PREF_NAME = "MY_BLOG_PREF";
    // variable to store data
    private String KEY_EMAIL = "pref_email";


    // constructor
    public SharedPrefManager(){

    }

    // save email to shared preferences
    public void saveEmail(Context context, String data){
        SharedPreferences preferences;
        SharedPreferences.Editor editor; // to edit data
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE); // initialize preference class
        editor = preferences.edit();
        editor.putString(KEY_EMAIL, data);
        editor.apply(); // save email to shared preferences

    }

    // retrieve email from shared preferences
    public String getEmail(Context context){
        String data;
        SharedPreferences preferences;
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        data = preferences.getString(KEY_EMAIL, "");

        return data;
    }

}


