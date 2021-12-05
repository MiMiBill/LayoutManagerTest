package com.bill.example.layoutmanagergroup;

import android.app.Application;
import android.content.Context;


/**
 * Created by Bill
 * github: https://github.com/MiMiBill
 *
 */

public class MyApplication extends Application{
    public static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
    }
}
