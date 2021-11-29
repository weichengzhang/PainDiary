package com.example.mobilepaindiary;


import android.app.Application;
import android.content.Context;

import com.example.mobilepaindiary.roomdb.PainRecord;

import java.util.ArrayList;
import java.util.List;

public class MyApplication extends Application {
    private static Context context;
    private static MyApplication instance;
    private static List<PainRecord> painRecordList;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        instance = this;
        painRecordList = new ArrayList<>();
    }

    public static Context getContext() {
        return context;
    }

    public static Application getApplication() {
        return instance;
    }

    public static void setPainRecordList(List<PainRecord> painRecordList) {
        MyApplication.painRecordList = painRecordList;
    }

    public static List<PainRecord> getPainRecordList() {
        return painRecordList;
    }
}
