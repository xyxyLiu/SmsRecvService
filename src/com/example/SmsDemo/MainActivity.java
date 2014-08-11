package com.example.SmsDemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
    public static String TAG = "SMS_MainActivity";
    private Button startService;
    private Button stopService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG,"onCreate()");
	    printActivityLog();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mapGui();
        hookListeners();
    }

	@Override
	public void onResume()
	{
		super.onResume();
		Log.i(TAG,"onResume()");
		printActivityLog();
	}

	@Override
	public void onPause()
	{
		super.onPause();
		Log.i(TAG,"onPause()");
		printActivityLog();
	}

	@Override
	public void onStop()
	{
		super.onStop();
		Log.i(TAG,"onStop()");
		printActivityLog();
	}

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"onDestroy()");
	    printActivityLog();
    }

	public void printActivityLog()
	{
		Log.i(TAG,"MainActivity.this = " + MainActivity.this);
	}

    private void mapGui() {
        startService = (Button) findViewById(R.id.bt_start_service);
        stopService = (Button) findViewById(R.id.bt_stop_service);
    }

    private void hookListeners() {
        startService.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "start SmsRadarService");
                initializeSmsRadarService();
            }
        });

        stopService.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "stop SmsRadarService");
                stopSmsRadarService();
            }
        });
    }

    private void initializeSmsRadarService() {

        SmsMonitor.initializeSmsMsgService(this, new SmsMsgListener() {
            @Override
            public void onSmsMsgReceived(SmsMsg msg) {
                Log.i(TAG,"onSmsMsgReceived()");
                showSmsToast(msg);
                Vibrator vibrator = (Vibrator)getSystemService(Service.VIBRATOR_SERVICE);
                vibrator.vibrate(3000);

                /*
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplication().startActivity(intent);
                */
                showAlertDialog(getApplicationContext(),msg);
            }
        });

    }

    private void stopSmsRadarService() {
      
        SmsMonitor.stopSmsMsgService(this);
    }



    private void showSmsToast(SmsMsg msg) {
        Log.i(TAG,"showSmsToast: " + msg.toString() );
        Toast.makeText(this, msg.toString(), Toast.LENGTH_LONG).show();
    }

    private void showAlertDialog(Context context,SmsMsg msg) {

        Log.i(TAG,"MainActivity.this is " + (MainActivity.this  == null?"NULL":"NOT NULL"));

        //获取电源的服务
        PowerManager powerManagerm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        //获取系统服务
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);

        final PowerManager.WakeLock wakeLock = powerManagerm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
        final KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("");


        final MediaPlayer mp = MediaPlayer.create(MainActivity.this, R.raw.alarm);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("AlertDialog");
        builder.setMessage(msg.getMsg());
        builder.setPositiveButton("Sure", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            mp.stop();
            keyguardLock.reenableKeyguard();
            wakeLock.release();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);//use alert.
        // dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY);



        wakeLock.acquire();
        Log.i("Log : ", "------>mKeyguardLock");
        //禁用显示键盘锁定
        keyguardLock.disableKeyguard();


        dialog.show();
        mp.setLooping(true);

        mp.setVolume(1.0f,1.0f);
        mp.start();
    }

}
