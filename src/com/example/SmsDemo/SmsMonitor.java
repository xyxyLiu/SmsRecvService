package com.example.SmsDemo;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by liu on 2014/8/10.
 */
public final class SmsMonitor {

    public static String TAG = "SMS_SmsMonitor";
    //public static Class<? extends BaseSmsMsgService> serviceClass;

	static
	{
		Log.i(TAG,"create SmsMonitor");
	}


    public static void initializeSmsMsgService(Context context, Class<? extends BaseSmsMsgService> c, boolean isBoot) {
        Log.i(TAG,"initializeSmsMsgService()");

	    getSmsStorage(context).setServiceClass(c);
	    getSmsStorage(context).setIsBoot(isBoot);

        Log.i(TAG,"SmsMonitor.serviceClass.getName() = " + getSmsStorage(context).getServiceClass());
        Intent intent = new Intent(context, getSmsStorage(context).getServiceClass());
        Log.i(TAG, "initializeSmsMsgService(): start SmsMsgService");
        context.startService(intent);
    }


    public static void stopSmsMsgService(Context context) {
        //SmsMonitor.smsMsgListener = null;
        Intent intent = new Intent(context, getSmsStorage(context).getServiceClass());
        context.stopService(intent);
    }

	public static SmsSharedPreference getSmsStorage(Context context)
	{
		SharedPreferences preferences = context.getSharedPreferences("sms_preferences", Context.MODE_PRIVATE);
		SmsSharedPreference smsStorage = new SmsSharedPreference(preferences);
		return smsStorage;
	}



}
