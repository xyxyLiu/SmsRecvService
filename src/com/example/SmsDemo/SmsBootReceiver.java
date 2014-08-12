package com.example.SmsDemo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
/**
 * Created by tony.lxy on 2014/8/12.
 */
public class SmsBootReceiver extends BroadcastReceiver {

	public static String TAG = "SMS_SmsBootReceiver";
	private Class<? extends  BaseSmsMsgService> serviceClass;

	public SmsBootReceiver(Intent intent)
	{
		if(intent != null)
		{
			serviceClass = (Class<? extends BaseSmsMsgService>)intent.getExtras().getSerializable("class");
		}
		else
		{
			serviceClass = null;
		}
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG,"onReceive()");
		Intent serviceIntent = new Intent(context, serviceClass);
		context.startService(serviceIntent);

	}
}
