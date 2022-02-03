package com.demo.myblog;

import android.app.Application;
import android.content.Context;

public class MyBlogApplication extends Application {
    // private static instance of class
    private static MyBlogApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        // when application starts, create instance of MyBlogApplication class
        sInstance = this;
    }

    public static MyBlogApplication getInstance() {
        // returning instance of class
        return sInstance;
    }

    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }
}
