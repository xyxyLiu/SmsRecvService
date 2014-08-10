package com.example.SmsDemo;



/**
 * Created by liu on 2014/8/10.
 */
public interface SmsMsgListener {


    /**
     * Invoked when an outgoing sms is intercepted.
     *
     * @param sms
     */
    public void onSmsMsgReceived(SmsMsg sms);
}
