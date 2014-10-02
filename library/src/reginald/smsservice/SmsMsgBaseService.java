package reginald.smsservice;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import java.util.Date;

/**
 * Created by liu on 2014/8/10.
 */
public abstract class SmsMsgBaseService extends Service {


    public static String TAG = "SMS_SmsMsgService";
    private static final String CONTENT_SMS_URI = "content://sms";
    private static final int ONE_SECOND = 1000;
    /**
     * Highest Priority
     */
    private static final int SMSMSG_RECEIVER_PRIORITY = 2147483647;
    private boolean initialized;

    private SmsMsgReceiver mSmsMsgReceiver;


    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate()");
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

        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                SmsMsg smsMsg = (SmsMsg) bundle.getSerializable("msg");
                if (smsMsg != null) {
                    if (smsMsg.getType() == SmsMsgType.RECEIVED) {
                        onReceiveSmsMsg(smsMsg);
                    }
                }
            }
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind(Intent intent)");
        return new Binder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "onUnbind(Intent intent)");
        return true;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.i(TAG, "onRebind(Intent intent)");
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy()");
        super.onDestroy();
        finService();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.i(TAG, "onTaskRemoved()");
        super.onTaskRemoved(rootIntent);
        restartService();
    }

    void initService() {
        Log.i(TAG, "initService()");
    }

    void finService() {
        Log.i(TAG, "finService()");
        unregisterReceivers();
    }

    void restartService() {
        Log.i(TAG, "restartService()");
        Intent intent = new Intent(this, SmsMsgBaseService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, 0);

        long now = new Date().getTime();
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, now + ONE_SECOND, pendingIntent);

    }

    void registerReceivers() {
        registerSmsReceiver();
    }

    public void registerSmsReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        filter.setPriority(SMSMSG_RECEIVER_PRIORITY);
        if (mSmsMsgReceiver == null) {

            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("class", SmsMonitor.getSmsStorage(this).getServiceClass());
            intent.putExtras(bundle);

            mSmsMsgReceiver = new SmsMsgReceiver();
            registerReceiver(mSmsMsgReceiver, filter);
        }
    }

    void unregisterReceivers() {
        if (mSmsMsgReceiver != null) {
            unregisterReceiver(mSmsMsgReceiver);
        }

    }


    protected abstract void onReceiveSmsMsg(SmsMsg smsMsg);


}
