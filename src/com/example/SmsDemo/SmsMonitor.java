package com.example.SmsDemo;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by liu on 2014/8/10.
 */
public class SmsMonitor {

    public static String TAG = "SMS_SmsMonitor";
    static SmsMsgListener smsMsgListener;


    /**
     * Starts the service and store the listener to be notified when a new incoming or outgoing sms be processed
     * inside the SMS content provider
     *
     * @param context used to start the service
     * @param smsMsgListener to notify when the sms content provider gets a new sms
     */
    public static void initializeSmsMsgService(Context context, SmsMsgListener smsMsgListener) {
        Log.i(TAG,"initializeSmsMsgService()");
        SmsMonitor.smsMsgListener = smsMsgListener;
        Intent intent = new Intent(context, SmsMsgService.class);
        Log.i(TAG, "initializeSmsMsgService(): start SmsMsgService");
        context.startService(intent);
    }

    /**
     * Stops the service and remove the SmsListener added when the SmsRadar was initialized
     *
     * @param context used to stop the service
     */
    public static void stopSmsMsgService(Context context) {
        SmsMonitor.smsMsgListener = null;
        Intent intent = new Intent(context, SmsMsgService.class);
        context.stopService(intent);
    }
}
