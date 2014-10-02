package reginald.smsservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by tony.lxy on 2014/8/12.
 */
public class SmsBootReceiver extends BroadcastReceiver {

    public static String TAG = "SMS_SmsBootReceiver";
    private Class<? extends SmsMsgBaseService> serviceClass;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (SmsMonitor.getSmsStorage(context).getIsBoot()) {
            serviceClass = SmsMonitor.getSmsStorage(context).getServiceClass();
            Intent serviceIntent = new Intent(context, serviceClass);
            context.startService(serviceIntent);
        }

    }
}
