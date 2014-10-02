package reginald.smsservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by liu on 2014/8/10.
 */
public class SmsMsgReceiver extends BroadcastReceiver {

    public static String TAG = "SMS_SmsMsgReceiver";
    private Class<? extends SmsMsgBaseService> serviceClass;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "receiverId = " + this + ", onReceive() ");

        serviceClass = SmsMonitor.getSmsStorage(context).getServiceClass();
        SmsMsgType smsMsgType = null;

        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            smsMsgType = SmsMsgType.RECEIVED;
        } else {
            smsMsgType = SmsMsgType.UNKNOWN;
        }

        Bundle bundle = intent.getExtras();
        SmsMsg[] smsMsgs = null;
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            smsMsgs = SmsMsg.getSmsMsgsFromPdus(pdus, smsMsgType);
        }
        Intent serviceIntent = new Intent(context, serviceClass);
        Bundle serviceBundle = new Bundle();
        serviceBundle.putSerializable("msg", smsMsgs[0]);
        serviceIntent.putExtras(serviceBundle);
        context.startService(serviceIntent);

        //stop broadcast:
        //abortBroadcast();
    }


}
