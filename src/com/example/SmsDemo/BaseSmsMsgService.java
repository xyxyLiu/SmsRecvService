package com.example.SmsDemo;

import android.app.*;
import android.content.*;
import android.os.*;
import android.util.Log;


import java.util.Date;

/**
 * Created by liu on 2014/8/10.
 */
public abstract class BaseSmsMsgService extends Service {


    public static String TAG = "SMS_SmsMsgService";
    private static final String CONTENT_SMS_URI = "content://sms";
    private static final int ONE_SECOND = 1000;
    /**
     * Highest Priority
     */
    private static final int SMSMSG_RECEIVER_PRIORITY = 2147483647;
	private static final int BOOT_RECEIVER_PRIORITY = 2147483647;

    private ContentResolver contentResolver;
    private boolean initialized;
	private boolean isBoot;

    private SmsMsgReceiver mSmsMsgReceiver;
	private SmsBootReceiver mSmsBootReceiver;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

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

	    if(intent != null) {

		    Bundle bundle = intent.getExtras();
		    if (bundle != null) {
			    SmsMsg smsMsg = (SmsMsg) bundle.getSerializable("msg");
			    if (smsMsg != null) {
				    onReceiveSmsMsg(smsMsg);
			    }
		    }
	    }
        return START_STICKY;
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

    void initService()
    {
        Log.i(TAG,"initService()");
    }

    void finService()
    {
        Log.i(TAG,"finService()");
        unregisterReceivers();
    }

    void restartService()
    {
        Log.i(TAG,"restartService()");
        Intent intent = new Intent(this, BaseSmsMsgService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, 0);

        long now = new Date().getTime();
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, now + ONE_SECOND, pendingIntent);

    }

    void registerReceivers()
    {
	    registerSmsReceiver();
	    if( SmsMonitor.getSmsStorage(this).getIsBoot()) {
		    registerBootReceiver();
	    }
    }

	public void registerSmsReceiver()
	{
		Log.i(TAG,"registerReceivers(): new SmsMsgReceiver(intent) with SmsMonitor.getSmsStorage(this).getServiceClass().getName() = " + SmsMonitor.getSmsStorage(this).getServiceClass().getName());
		IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
		filter.setPriority(SMSMSG_RECEIVER_PRIORITY);
		if( mSmsMsgReceiver == null) {

		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putSerializable("class", SmsMonitor.getSmsStorage(this).getServiceClass());
		intent.putExtras(bundle);

		mSmsMsgReceiver = new SmsMsgReceiver(intent);

		Log.i(TAG, "register receiver: " +  mSmsMsgReceiver);
		registerReceiver( mSmsMsgReceiver, filter);
		}
	}

	public void registerBootReceiver()
	{
		Log.i(TAG,"registerBootReceiver()");
		IntentFilter filter = new IntentFilter("android.intent.action.BOOT_COMPLETED");
		filter.setPriority(BOOT_RECEIVER_PRIORITY);

		Log.i(TAG,"registerBootReceivers...");
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putSerializable("class", SmsMonitor.getSmsStorage(this).getServiceClass());
		intent.putExtras(bundle);

		mSmsBootReceiver = new SmsBootReceiver(intent);

		Log.i(TAG, "register boot receiver: " + mSmsBootReceiver);
		registerReceiver(mSmsBootReceiver, filter);

	}



    void unregisterReceivers()
    {
	    if( mSmsMsgReceiver != null) {
		    unregisterReceiver(mSmsMsgReceiver);
		    Log.i(TAG, "unregister receiver: " + mSmsMsgReceiver);
	    }
    }



    protected abstract void onReceiveSmsMsg(SmsMsg smsMsg);




}
