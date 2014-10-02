package reginald.smsservice.demo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import reginald.smsservice.R;
import reginald.smsservice.SmsMonitor;
import reginald.smsservice.SmsMsg;

public class DemoActivity extends Activity {
    public static String TAG = "SMS_DemoActivity";
    private Button startService;
    private Button stopService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mapGui();
        hookListeners();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshView();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy()");
    }

    private void mapGui() {
        startService = (Button) findViewById(R.id.bt_start_service);
        stopService = (Button) findViewById(R.id.bt_stop_service);
    }

    private void hookListeners() {
        startService.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "start SmsRecvService");
                initializeSmsRadarService();
                refreshView();
            }
        });

        stopService.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "stop SmsRecvService");
                stopSmsRadarService();
                refreshView();
            }
        });
    }

    private void refreshView() {
        boolean isRunning = SmsMonitor.isSmsServiceRunning(DemoActivity.this);
        if (isRunning) {
            startService.setText("started");
            stopService.setText("stop");
        } else {
            startService.setText("start");
            stopService.setText("stopped");
        }
    }

    private void initializeSmsRadarService() {
        SmsMonitor.initializeSmsMsgService(this, SmsMsgService.class, true);
    }

    private void stopSmsRadarService() {
        SmsMonitor.stopSmsMsgService(this);
    }

    private void showSmsToast(SmsMsg msg) {
        Toast.makeText(this, msg.toString(), Toast.LENGTH_LONG).show();
    }


}
