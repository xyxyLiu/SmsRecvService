package com.example.SmsDemo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.*;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;


import java.util.Date;

/**
 * Created by liu on 2014/8/10.
 */
public class SmsMsgService extends Service {


    public static String TAG = "SMS_SmsMsgService";
    private static final String CONTENT_SMS_URI = "content://sms";
    private static final int ONE_SECOND = 1000;
    /**
     *
     */
    private static final int SMSMSG_RECEIVER_PRIORITY = 2147483647;

    private ContentResolver contentResolver;
    private boolean initialized;

    private SmsMsgReceiver mSmsMsgReceiver;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        registerReceivers();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand()");
        if (!initialized) {
            Log.i(TAG, "onStartCommand(): initializeService");
            initService();
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onStartCommand()");
        super.onDestroy();
        finService();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.i(TAG, "onTaskRemoved()");
        super.onTaskRemoved(rootIntent);
        restartService();
    }

    void initService()
    {
        Log.i(TAG,"initService()");

    }

    void finService()
    {
        Log.i(TAG,"finService()");
        unregisterReceivers();
    }

    void restartService()
    {
        Log.i(TAG,"restartService()");
        Intent intent = new Intent(this, SmsMsgService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, 0);

        long now = new Date().getTime();
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, now + ONE_SECOND, pendingIntent);

    }

    void registerReceivers()
    {
        IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        filter.setPriority(SMSMSG_RECEIVER_PRIORITY);
        mSmsMsgReceiver = new SmsMsgReceiver();
        registerReceiver(mSmsMsgReceiver, filter);
    }

    void unregisterReceivers()
    {
        unregisterReceiver(mSmsMsgReceiver);
    }

}
