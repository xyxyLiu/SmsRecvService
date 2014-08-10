package com.example.SmsDemo;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;

/**
 * Created by liu on 2014/8/10.
 */
public class SmsMsgReceiver extends BroadcastReceiver{

    public static String TAG = "SMS_SmsMsgReceiver";

    static
    {
        Log.i(TAG,"create SmsMsgReceiver");
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.i(TAG, "onReceive() ");

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

            SmsMonitor.smsMsgListener.onSmsMsgReceived(msg);
            Toast.makeText(context,sb.toString(),Toast.LENGTH_LONG).show();

        }
    }

}
