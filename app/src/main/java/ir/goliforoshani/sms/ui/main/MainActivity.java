package ir.goliforoshani.sms.ui.main;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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
import ir.goliforoshani.sms.ui.intro.Intro;
import ir.goliforoshani.sms.utils.Keeper;
import ir.goliforoshani.sms.utils.amount;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "amingoli-main";

    @Override
    protected void onStart() {
        super.onStart();
        itemStatus();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Keeper.getInstance().get(amount.NUMBER_PHONE) == null){
            saveNumber(this);
        }
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
                saveNumber(this);
                break;
            case "status":
                if (keeperStatusIsOn()){
                    changeStatus(false);
                }else {
                    changeStatus(true);
                }
                break;
            default:
                startActivity(new Intent(this, Intro.class));
                break;
        }
    }

    /**
     * Save Number
     * */
    private void saveNumber(Activity activity){
        boolean cancelable = true;
        if (Keeper.getInstance().get(amount.NUMBER_PHONE) == null){
            cancelable = false;
        }
        AddNumberDialog.listener listener = new AddNumberDialog.listener() {
            @Override
            public void result(String number) {
                Keeper.getInstance().save(amount.NUMBER_PHONE,number);
                if (Keeper.getInstance().get(amount.STATUS_SERVICE) == null)
                    Keeper.getInstance().save(amount.STATUS_SERVICE,"on");
                itemStatus();
            }
        };
        AddNumberDialog add_number = new AddNumberDialog(activity,listener,cancelable);
        add_number.show();
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
        if (keeperStatusIsOn()){
            changeStatus(true);
        }else {
            changeStatus(false);
        }
    }
    private boolean keeperStatusIsOn(){
        String ke = Keeper.getInstance().get(amount.STATUS_SERVICE);
        return ke != null && ke.equals("on");
    }
    void changeStatus(boolean on){
        if (on){
            status.setText(getString(R.string.on));
            status.setTextColor(getResources().getColor(R.color.green));
            Keeper.getInstance().save(amount.STATUS_SERVICE,"on");
        }else {
            status.setText(getString(R.string.off));
            status.setTextColor(getResources().getColor(R.color.red));
            Keeper.getInstance().save(amount.STATUS_SERVICE,"off");
        }
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


}