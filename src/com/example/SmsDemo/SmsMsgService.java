package com.example.SmsDemo;

import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.PowerManager;
import android.os.Vibrator;
import android.util.Log;
import android.view.WindowManager;

/**
 * Created by liu on 2014/8/12.
 */
public class SmsMsgService extends BaseSmsMsgService {


    @Override
    protected void onReceiveSmsMsg(SmsMsg smsMsg)
    {


        //获取电源的服务
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        //获取系统服务
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);

        final PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
        final KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("");


        final MediaPlayer mp = MediaPlayer.create(this, R.raw.alarm);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("AlertDialog");
        builder.setMessage(smsMsg.getMsg());
        builder.setPositiveButton("Sure", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                mp.stop();
                //keyguardLock.reenableKeyguard();
                wakeLock.release();

                Intent intent = new Intent(SmsMsgService.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                SmsMsgService.this.getApplicationContext().startActivity(intent);
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

        Vibrator vibrator = (Vibrator)getSystemService(Service.VIBRATOR_SERVICE);
        vibrator.vibrate(3000);

    }


}
