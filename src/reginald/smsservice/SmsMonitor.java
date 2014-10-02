package reginald.smsservice;

import android.app.ActivityManager;
import android.content.*;

/**
 * Created by liu on 2014/8/10.
 */
public final class SmsMonitor {

    public static String TAG = "SMS_SmsMonitor";

    public static void initializeSmsMsgService(Context context, Class<? extends SmsMsgBaseService> c, boolean isBoot) {
        getSmsStorage(context).setServiceClass(c);
        getSmsStorage(context).setIsBoot(isBoot);

        Intent intent = new Intent(context, getSmsStorage(context).getServiceClass());
        context.startService(intent);
    }


    public static void stopSmsMsgService(Context context) {
        Intent intent = new Intent(context, getSmsStorage(context).getServiceClass());
        context.stopService(intent);
    }


    public static boolean isSmsServiceRunning(Context context) {
        Class<?> serviceClass = getSmsStorage(context).getServiceClass();
        if(serviceClass == null)
            return false;

        String serviceClassName = serviceClass.getName();

        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClassName.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;

    }


    public static SmsSharedPreference getSmsStorage(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("sms_preferences", Context.MODE_PRIVATE);
        SmsSharedPreference smsStorage = SmsSharedPreference.getInstance(preferences);
        return smsStorage;
    }


}
