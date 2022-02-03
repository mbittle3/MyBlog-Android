package com.demo.myblog.volley;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.demo.myblog.MyBlogApplication;

public class VolleySingleton {
    // reference variable for class
    private static VolleySingleton sInstance = null;

    // create request queue
    private RequestQueue nRequestQueue;

    // this constructor is private because we do not
    // want other classes to create instances.
    private VolleySingleton() {
        // get request queue
        nRequestQueue = getRequestQueue();
    }

    public static VolleySingleton getInstance() {
        // if there is no instance of class
        if (sInstance == null) {
            // create new instance
            sInstance = new VolleySingleton();
        }
        return sInstance;
    }

    private RequestQueue getRequestQueue() {
        // if no request queue exists
        if (nRequestQueue == null) {
            // get application context - single instance of RequestQueue for lifetime of application
            // otherwise you need to create for every activity
            nRequestQueue = Volley.newRequestQueue(MyBlogApplication.getAppContext());
        }
        return nRequestQueue;
    }

    // add request to queue
    public void addRequestQueue(Request request) {
        //add request to queue
        nRequestQueue.add(request);
    }
}
