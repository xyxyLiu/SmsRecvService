package com.example.SmsDemo;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by liu on 2014/8/10.
 */
public class SmsMonitor {

    public static String TAG = "SMS_SmsMonitor";
    public static Class<? extends BaseSmsMsgService> serviceClass;


    /**
     * Starts the service and store the listener to be notified when a new incoming or outgoing sms be processed
     * inside the SMS content provider
     *
     * @param context used to start the service
     * @param c to notify when the sms content provider gets a new sms
     */
    public static void initializeSmsMsgService(Context context, Class<? extends BaseSmsMsgService> c) {
        Log.i(TAG,"initializeSmsMsgService()");
        SmsMonitor.serviceClass = c;
        Log.i(TAG,"SmsMonitor.serviceClass.getName() = " + SmsMonitor.serviceClass.getName());
        Intent intent = new Intent(context, SmsMonitor.serviceClass);
        Log.i(TAG, "initializeSmsMsgService(): start SmsMsgService");
        context.startService(intent);
    }

    /**
     * Stops the service and remove the SmsListener added when the SmsRadar was initialized
     *
     * @param context used to stop the service
     */
    public static void stopSmsMsgService(Context context) {
        //SmsMonitor.smsMsgListener = null;
        Intent intent = new Intent(context, SmsMonitor.serviceClass);
        context.stopService(intent);
    }
}
