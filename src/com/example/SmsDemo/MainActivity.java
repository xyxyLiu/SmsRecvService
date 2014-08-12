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
    private int test;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG,"onCreate()");
	    printActivityLog();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mapGui();
        hookListeners();
        test = 1;
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
        test = 2;
        System.gc();
        System.gc();
        System.gc();
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
                startService.setText("started");
                stopService.setText("stop");
                initializeSmsRadarService();
            }
        });

        stopService.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "stop SmsRadarService");
                startService.setText("start");
                stopService.setText("stopped");
                stopSmsRadarService();
            }
        });
    }

    private void initializeSmsRadarService() {

        SmsMonitor.initializeSmsMsgService(this,SmsMsgService.class,true);

    }

    private void stopSmsRadarService() {
      
        SmsMonitor.stopSmsMsgService(this);
    }



    private void showSmsToast(SmsMsg msg) {
        Log.i(TAG,"showSmsToast: " + msg.toString() );
        Toast.makeText(this, msg.toString(), Toast.LENGTH_LONG).show();
    }



}
