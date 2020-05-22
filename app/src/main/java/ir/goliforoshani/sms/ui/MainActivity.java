package ir.goliforoshani.sms.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.airbnb.lottie.LottieAnimationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import ir.goliforoshani.sms.R;
import ir.goliforoshani.sms.utils.Keeper;
import ir.goliforoshani.sms.utils.amount;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "amingoli-main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        smsPermission_isOK();
        itemStatus();
        itemList();
    }

    /**
     * On Click
     * */
    @Override
    public void onClick(View view) {
        switch (view.getTag().toString()){
            case "add_item":
                AddNumberDialog add_item = new AddNumberDialog(this, new AddNumberDialog.listener() {
                    @Override
                    public void result(String number) {
                        addItem(number);
                    }
                });
                add_item.show();
                break;
            case "number_phone":
                final AddNumberDialog number_phone = new AddNumberDialog(this, new AddNumberDialog.listener() {
                    @Override
                    public void result(String number) {
                        if (number.length() >= 7){
                            Keeper.getInstance().save(amount.NUMBER_PHONE,number);
                        }else {
                            Toast.makeText(MainActivity.this, getResources()
                                                    .getString(R.string.warm_number_length_less_then_two_digits,"هفت"),
                                            Toast.LENGTH_SHORT).show();
                        }
                        itemStatus();
                    }
                });
                number_phone.show();
                break;
            case "on":
                Keeper.getInstance().save(amount.STATUS_SERVICE,"off");
                itemStatus();
                break;
            case "off":
                Keeper.getInstance().save(amount.STATUS_SERVICE,"on");
                itemStatus();
                break;
            default:
                break;
        }
    }

    /**
     * View Status
     * */
    View item_status;
    TextView status;
    private void itemStatus(){
        item_status = findViewById(R.id.view_item_status);
        status = item_status.findViewById(R.id.status);
        TextView number = item_status.findViewById(R.id.number_phone);
        number.setText(Keeper.getInstance().get(amount.NUMBER_PHONE));

        if (status_service()){
            status.setText(getString(R.string.on));
            status.setTextColor(getResources().getColor(R.color.green));
            status.setTag("on");
        }else {
            status.setText(getString(R.string.off));
            status.setTextColor(getResources().getColor(R.color.red));
            status.setTag("off");
        }
        Keeper.getInstance().save(amount.STATUS_SERVICE,status.getTag().toString());
    }

    private boolean status_service(){
        String tag = Keeper.getInstance().get(amount.STATUS_SERVICE);
        if (tag!=null && tag.equals("on") ){
            return true;
        }
        return false;
    }

    /**
     * View List
     * */
    private List<String> list_main = null;
    private AdapterListNumber adapter;
    private LottieAnimationView animationView;
    private void itemList(){
        View item_list = findViewById(R.id.view_item_list);
        animationView = item_list.findViewById(R.id.animation_view);
        RecyclerView recyclerView = item_list.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        list_main = getList();
        adapter = new AdapterListNumber(list_main, new AdapterListNumber.listener() {
            @Override
            public void result(int pos) {
                removeItem(pos);
            }
        }, this);
        recyclerView.setAdapter(adapter);
    }

    private void addItem(String value){
        if (animationView.getVisibility() == View.VISIBLE){
            animationView.setVisibility(View.GONE);
        }
        list_main.add(value);
        saveList(list_main);
        adapter.notifyDataSetChanged();
    }
    private void removeItem(int pos){
        list_main.remove(pos);
        saveList(list_main);
        if (pos==0) getList();
        adapter.notifyDataSetChanged();
    }
    private void saveList(List<String> list){
        String json = null;
        if (list.size() != 0){
            Gson gson = new Gson();
            json = gson.toJson(list);
        }
        Keeper.getInstance().save(amount.LIST_NUMBER_MUST_GET_SMS,json);
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
        }else {
            animationView.setVisibility(View.VISIBLE);
            animationView.playAnimation();
        }
        return list;
    }

    /**
     * check Permission for SMS
     * */
    public Boolean smsPermission_isOK(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
                        != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                        != PackageManager.PERMISSION_GRANTED )
        {
            ActivityCompat.requestPermissions(this,new String[]
                    {
                            Manifest.permission.SEND_SMS,
                            Manifest.permission.READ_SMS,
                            Manifest.permission.RECEIVE_SMS,
                            Manifest.permission.READ_PHONE_STATE
                    }, 1);
            return false;

        }
        else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED )
        {
            ActivityCompat.requestPermissions(this,new String[]
                    {
                            Manifest.permission.READ_PHONE_STATE
                    }, 1);
            return false;

        }
        else{
            return true;
        }
    }
}