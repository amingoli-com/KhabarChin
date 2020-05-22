package ir.goliforoshani.sms.service.sendSmsToUser;

import android.content.Context;
import android.os.AsyncTask;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ir.goliforoshani.sms.utils.Keeper;
import ir.goliforoshani.sms.utils.amount;

public class SendSMSToUser extends AsyncTask<itemSendSms, Void , Void> {
    private String TAG = "amin-SendSMSToUser";

    private Context context;

    public SendSMSToUser(Context context) {
        this.context = context;
    }


    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Void doInBackground(itemSendSms... params) {
        for (itemSendSms p : params) {
            try {
                SmsManager smsManager = SmsManager.getDefault();
                if (Keeper.getInstance().get(amount.LIST_NUMBER_MUST_GET_SMS) == null){
                    smsManager.sendTextMessage(Keeper.getInstance().get(amount.NUMBER_PHONE), null,
                            p.number+"\n"+p.massage, null, null);
                }else {
                    Gson gson = new Gson();
                    String json = Keeper.getInstance().get(amount.LIST_NUMBER_MUST_GET_SMS);
                    if (json != null) {
                        Type type = new TypeToken<List<String>>() {
                        }.getType();
                        List<String> arrPackageData = gson.fromJson(json, type);
                        for(String data:arrPackageData) {
                            String[] multi_number = {data,"+98"+data,"+980"+data,"+"+data};
                            for (String number:multi_number){
                                if (p.number.equals(number)){
                                    smsManager.sendTextMessage(Keeper.getInstance().get(amount.NUMBER_PHONE), null,
                                            p.number+"\n"+p.massage, null, null);
                                }else {
                                }
                            }
                        }
                    }
                }
                Thread.sleep(10000);
            } catch (Exception e) {
                Log.e(TAG, "doInBackground: ",e );
            }
        }
        return null;
    }


    private List<String> getList(){
        final List<String> list = new ArrayList<>();
        Gson gson = new Gson();
        String json = Keeper.getInstance().get(amount.LIST_NUMBER_MUST_GET_SMS);
        if (json != null) {
            Type type = new TypeToken<List<String>>() {
            }.getType();
            List<String> arrPackageData = gson.fromJson(json, type);
            list.addAll(arrPackageData);
        }
        return list;
    }
}
