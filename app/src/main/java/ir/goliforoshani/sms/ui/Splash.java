package ir.goliforoshani.sms.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import ir.goliforoshani.sms.R;
import ir.goliforoshani.sms.ui.intro.Intro;
import ir.goliforoshani.sms.ui.main.AddNumberDialog;
import ir.goliforoshani.sms.ui.main.MainActivity;
import ir.goliforoshani.sms.utils.Keeper;
import ir.goliforoshani.sms.utils.amount;

public class Splash extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        permisssion();
    }

    @Override
    public void onClick(View view) {
        if (permisssion()){
            startActivity(new Intent(Splash.this, MainActivity.class));
            finish();
        }
    }

    /**
     * check Permission for SMS
     * */
    private boolean dialogIsRun = false;
    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private boolean sentToSettings = false;
    private SharedPreferences permissionStatus;
    private boolean permisssion(){
        final String[] a = {Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS};

        permissionStatus = getSharedPreferences("permissionStatus",MODE_PRIVATE);

        if (ActivityCompat.checkSelfPermission(Splash.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(Splash.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(Splash.this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Splash.this, Manifest.permission.SEND_SMS) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(Splash.this, Manifest.permission.READ_SMS) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(Splash.this, Manifest.permission.RECEIVE_SMS)) {
                //Show Information about why you need the permission
                dialogIsRun = true;
                AlertDialog.Builder builder = new AlertDialog.Builder(Splash.this);
                builder.setMessage(R.string.desc_need_permission);
                builder.setPositiveButton(R.string.ok_permission, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogIsRun = false;
                        dialog.cancel();
                        ActivityCompat.requestPermissions(Splash.this, a, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(Splash.this);
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
                        finish();
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
                ActivityCompat.requestPermissions(Splash.this, a, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
            }

            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(Manifest.permission.SEND_SMS,true);
            editor.putBoolean(Manifest.permission.READ_SMS,true);
            editor.putBoolean(Manifest.permission.RECEIVE_SMS,true);
            editor.apply();
            return false;

        } else {
            startActivity(new Intent(Splash.this, MainActivity.class));
            finish();
            return true;
        }
    }
}
