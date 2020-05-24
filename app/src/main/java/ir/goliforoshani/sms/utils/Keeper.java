package ir.goliforoshani.sms.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ir.goliforoshani.sms.AppController;

public class Keeper {

    private static final Keeper instance = new Keeper();
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private static final String PREF_KEEPER = "pref_keeper";

    private Keeper() {
        sharedPreferences = AppController.getInstance().getSharedPreferences(PREF_KEEPER, Context.MODE_PRIVATE);
    }

    public static Keeper getInstance() {
        return instance;
    }


    public void save(String key, String value) {
        editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String get(String key) {
        return sharedPreferences.getString(key, null );
    }

    public void remove(String key) {
        editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }
}
