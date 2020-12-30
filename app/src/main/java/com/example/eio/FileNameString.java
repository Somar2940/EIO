package com.example.eio;

import android.app.Application;

public class FileNameString extends Application {

    private static String filetime = "default";
    private static String accountname = "default";
    private static String accountpass = "default";

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

    public String getAccountname() {
        return accountname;
    }

    public String getAccountpass() {
        return accountpass;
    }

    public void setAccount(String name,String pass) {
        accountname = name;
        accountpass = pass;
    }
}
