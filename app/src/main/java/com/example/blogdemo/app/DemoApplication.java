package com.example.blogdemo.app;

import android.app.Application;

import com.example.blogdemo.db.DatabaseHelper;

/**
 * Created by Administrator on 2016/11/3.
 */
public class DemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DatabaseHelper.init(this);
    }

}
