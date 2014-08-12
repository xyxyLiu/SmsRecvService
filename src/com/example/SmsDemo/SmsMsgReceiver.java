package com.example.SmsDemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;

/**
 * Created by liu on 2014/8/10.
 */
public class SmsMsgReceiver extends BroadcastReceiver{

    public static String TAG = "SMS_SmsMsgReceiver";
	private Class<? extends  BaseSmsMsgService> serviceClass;

	static
	{
		Log.i(TAG,"create SmsMsgReceiver");
	}

	public SmsMsgReceiver(Intent intent)
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
    public void onReceive(Context context, Intent intent)
    {
        Log.i(TAG, "receiverId = " + this + ", onReceive() ");

        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED"))
        {
            StringBuilder sb = new StringBuilder();
            Bundle bundle = intent.getExtras();
            SmsMsg msg = null;
            if(bundle != null)
            {
                Object[] pdus = (Object[]) bundle.get("pdus");
                SmsMessage[] messages = new SmsMessage[pdus.length];
                for(int i = 0; i< pdus.length; i++)
                {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }

                Log.i(TAG, "pdus.length = " + pdus.length);

                for(SmsMessage message : messages)
                {
                    Log.i(TAG, "SMS Message: " + message.getDisplayMessageBody());
                    String date = null;
                    try
                    {
                        date = new Date(message.getTimestampMillis()).toString();
                    }
                    catch (Exception e)
                    {

                    }
                    msg = new SmsMsg(message.getDisplayOriginatingAddress(),date,message.getDisplayMessageBody(),SmsMsgType.RECEIVED);
                    sb.append("SMS Message:");
                    sb.append(message.getDisplayOriginatingAddress());
                    sb.append(message.getDisplayMessageBody());

                }
            }

            //SmsMonitor.smsMsgListener.onSmsMsgReceived(msg);
	        Log.i(TAG,"Toast");
            Toast.makeText(context,sb.toString(),Toast.LENGTH_LONG).show();

			Log.i(TAG, "context = " + (context == null ? "NULL":context.getPackageName()) + ", SmsMonitor.serviceClass = " + serviceClass);
            Intent serviceIntent = new Intent(context, serviceClass);
            Bundle serviceBundle = new Bundle();



            serviceBundle.putSerializable("msg",msg);
            serviceIntent.putExtras(serviceBundle);
            context.startService(serviceIntent);
        }
    }

}
