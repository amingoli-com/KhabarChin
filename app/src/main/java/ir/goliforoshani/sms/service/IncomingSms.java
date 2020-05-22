package ir.goliforoshani.sms.service;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import ir.goliforoshani.sms.service.sendSmsToUser.SendSMSToUser;
import ir.goliforoshani.sms.service.sendSmsToUser.itemSendSms;
import ir.goliforoshani.sms.utils.Keeper;
import ir.goliforoshani.sms.utils.amount;

public class IncomingSms extends BroadcastReceiver {
    private static String TAG = "amingoli-IncomingSms";

    public void onReceive(final Context context, Intent intent) {
        String statusService = Keeper.getInstance().get(amount.STATUS_SERVICE);
        if (statusService !=null && statusService.equals("on")){
            if(Objects.equals(intent.getAction(), "android.provider.Telephony.SMS_RECEIVED")){
                Bundle bundle = intent.getExtras();
                SmsMessage[] msgs = null;
                String numberSMS = null
                        ,textSMS = ""
                        ,timeSMS = null;
                if (bundle != null){
                    //---retrieve the SMS message received---
                    try{
                        Object[] pdus = (Object[]) bundle.get("pdus");
                        msgs = new SmsMessage[pdus.length];
                        for(int i=0; i<msgs.length; i++){
                            msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                            if (i == 0){
                                numberSMS = msgs[i].getOriginatingAddress();
                                @SuppressLint("SimpleDateFormat")
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                timeSMS = simpleDateFormat.format(new Date());
                            }
                            textSMS = textSMS + msgs[i].getMessageBody();
                            itemSendSms item = new itemSendSms(numberSMS,textSMS,timeSMS);
                            new SendSMSToUser(context).execute(item);
                        }
                    }catch(Exception e){
                    }
                }
            }
        }
    }

}