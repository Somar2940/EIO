package com.example.eio;

import android.app.Application;

public class FileNameString extends Application {

    private String filetime = "default";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public String getfiletime() {
        return filetime;
    }

    public void setfiletime(String str) {
        filetime = str;
    }
}
