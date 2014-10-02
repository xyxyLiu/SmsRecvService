package reginald.smsservice.demo;

import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.PowerManager;
import android.view.WindowManager;
import reginald.smsservice.SmsMsg;
import reginald.smsservice.SmsMsgBaseService;
import android.R;

/**
 * Created by liu on 2014/8/12.
 */
public class SmsMsgService extends SmsMsgBaseService {


    @Override
    protected void onReceiveSmsMsg(SmsMsg smsMsg) {

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

        final PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
        final KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("");


        Intent notificationIntent = new Intent(this, DemoActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        Notification notification = new Notification(R.drawable.ic_dialog_alert, "new incoming sms!",
                System.currentTimeMillis());
        notification.setLatestEventInfo(this, "lastest sms from " + smsMsg.getAddress(),
                smsMsg.getMsg(), pendingIntent);
        startForeground(5751, notification);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("smsRecvService");
        builder.setMessage(smsMsg.getMsg());
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                wakeLock.release();
                Intent intent = new Intent(SmsMsgService.this, DemoActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                SmsMsgService.this.getApplicationContext().startActivity(intent);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);//use alert.

        wakeLock.acquire();
        keyguardLock.disableKeyguard();
        dialog.show();
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Notification notification = new Notification(R.drawable.ic_dialog_alert, "SmsRecvService started ...",
                System.currentTimeMillis());
        Intent notificationIntent = new Intent(this, DemoActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        notification.setLatestEventInfo(this, "smsRecvService",
                "started", pendingIntent);
        startForeground(5751, notification);
    }

}
