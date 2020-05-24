package ir.goliforoshani.sms.ui.intro;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;

import java.util.ArrayList;
import java.util.List;

import ir.goliforoshani.sms.R;
import ir.goliforoshani.sms.ui.main.MainActivity;
import ir.goliforoshani.sms.utils.Keeper;
import ir.goliforoshani.sms.utils.amount;
import me.relex.circleindicator.CircleIndicator2;

public class Intro extends AppCompatActivity {
    private static String TAG = "amingoli-intro";

    List<IntroModel> itemIntroList;
    RecyclerViewPager recyclerView;
    IntroAdapter adaptorIntro;
    LinearLayoutManager layout;

    View bg_dots;
    TextView next,skip,start;
    ImageView img_next;
    CircleIndicator2 indicator;

    String bg_from = "#ffffff";
    String bg_to = "#ffffff";
    String color_primary = "#333333";
    String color_secondary = "#333333";

    @Override
    protected void onStart() {
        super.onStart();
        if (Keeper.getInstance().get(amount.INTRO_IS_STARTED)!=null){
            startMainActivity();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onWindowFocusChanged(true);
        setContentView(R.layout.activity_intro);

        bg_dots   = findViewById(R.id.bg_dots);
        indicator = findViewById(R.id.indicator);
        next      = findViewById(R.id.next);
        skip      = findViewById(R.id.skip);
        start     = findViewById(R.id.start);
        img_next  = findViewById(R.id.image_next);

        final String lang_start = getString(R.string.getStarted);

        itemIntroList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerViewPager_intro);
        adaptorIntro  = new IntroAdapter(this,itemIntroList);
        recyclerView.setAdapter(adaptorIntro);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(recyclerView);
        layout= new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layout);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//      Dots
        indicator.attachToRecyclerView(recyclerView, pagerSnapHelper);
//      optional
        adaptorIntro.registerAdapterDataObserver(indicator.getAdapterDataObserver());

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (getPage() == itemIntroList.size()-1){
                    next.setText(lang_start);
                    start.setText(lang_start);
                    img_next.setTag("start");
                    img_next.animate().alpha(0).setDuration(150);
                    indicator.animate().alpha(0).setDuration(150);
                    start.animate().alpha(1).setDuration(100);
                    start.setEnabled(true);
                }else {
                    if (Keeper.getInstance().get(amount.STATUS_SERVICE) == null)
                        Keeper.getInstance().save(amount.STATUS_SERVICE,"on");
                    img_next.setTag("next");
                    img_next.animate().alpha(1).setDuration(150);
                    indicator.animate().alpha(1).setDuration(150);
                    start.animate().alpha(0).setDuration(100);
                    start.setEnabled(false);
                }

                if (getPage() ==0){
                    skip.setTag("skip");
                }else {
                    skip.setTag("back");
                }
            }
        });

        getJson();
    }
    int getPage(){
        return recyclerView.getCurrentPosition();
    }

    public void btn_intro(View view) {
        int page = getPage();
        String tag = view.getTag().toString();
        switch (tag){
            case "next":
                recyclerView.smoothScrollToPosition(page+1);
                break;
            case "back":
                recyclerView.smoothScrollToPosition(page-1);
                break;
            default:
                startMainActivity();
                break;
        }
    }

    private void startMainActivity(){
        if (permission()){
            Keeper.getInstance().save(amount.INTRO_IS_STARTED,"started");
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }


    private void getJson(){
        itemIntroList.add(new IntroModel("سلام عزیزم",null, "من خبرچین هستم! دستیار پیامکی شما",bg_from,bg_to,color_primary,color_secondary));
        itemIntroList.add(new IntroModel("قراره چیکار کنم؟",null, "معلومه پیامک ها رو برای یه نفر دیگه هم بفرستم",bg_from,bg_to,color_primary,color_secondary));
        itemIntroList.add(new IntroModel("راستی!",null, "کلا دسترسی من به اینترنت قطعه، پس نگران چیزی نباش!",bg_from,bg_to,color_primary,color_secondary));
        itemIntroList.add(new IntroModel("و صد البته!",null, "با اجازه شما شروع میکنم",bg_from,bg_to,color_primary,color_secondary));
        recyclerView.getAdapter();
        adaptorIntro.notifyDataSetChanged();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_IMMERSIVE | View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }

    /**
     * check Permission for SMS
     * */
    private boolean dialogIsRun = false;
    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private boolean sentToSettings = false;
    private boolean permission(){
        final String[] a = {Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS};

        SharedPreferences permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_SMS) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECEIVE_SMS)) {
                //Show Information about why you need the permission
                dialogIsRun = true;
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.desc_need_permission);
                builder.setPositiveButton(R.string.ok_permission, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogIsRun = false;
                        dialog.cancel();
                        ActivityCompat.requestPermissions(Intro.this, a, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
                    }
                });
                builder.setNegativeButton(R.string.exit_app, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogIsRun = false;
                        finish();
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (permissionStatus.getBoolean(Manifest.permission.SEND_SMS,false) ||
                    permissionStatus.getBoolean(Manifest.permission.READ_SMS,false) ||
                    permissionStatus.getBoolean(Manifest.permission.RECEIVE_SMS,false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                dialogIsRun = true;
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.desc_need_permission);
                builder.setPositiveButton(R.string.ok_permission, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogIsRun = false;
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                    }
                });
                builder.setNegativeButton(R.string.exit_app, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogIsRun = false;
                        finish();
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                //just request the permission
                ActivityCompat.requestPermissions(this, a, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
            }

            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(Manifest.permission.SEND_SMS,true);
            editor.putBoolean(Manifest.permission.READ_SMS,true);
            editor.putBoolean(Manifest.permission.RECEIVE_SMS,true);
            editor.apply();
            return false;

        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case EXTERNAL_STORAGE_PERMISSION_CONSTANT: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startMainActivity();
                    // call your method
                } else {
                    permission();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
