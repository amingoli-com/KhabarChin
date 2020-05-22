package ir.goliforoshani.sms;

/**
 * Created by Ahmad on 3/7/2018.
 */

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.Api;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Locale;
import java.util.Objects;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;


public class AppController extends Application implements Application.ActivityLifecycleCallbacks {

    public static final String TAG = AppController.class.getSimpleName();

    private RequestQueue mRequestQueue;

    private static AppController mInstance;

    public String getStrings(int id) {
        return AppController.getInstance().getApplicationContext().getResources().getString(id);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        handleSSL();

    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }


    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    private HurlStack hurlStack;

    public void handleSSL() {

        try {

            /* To understand the concept about Self-Signed Certificates, Custom TrustManagers, etc.
            CertificateFactory:  https://docs.oracle.com/javase/7/docs/api/java/security/cert/CertificateFactory.html
            Certificate:         https://docs.oracle.com/javase/7/docs/api/java/security/cert/Certificate.html
            KeyStore:            https://docs.oracle.com/javase/7/docs/api/java/security/KeyStore.html
            KeyManagerFactory:   https://docs.oracle.com/javase/7/docs/api/javax/net/ssl/KeyManagerFactory.html
            TrustManagerFactory: http://docs.oracle.com/javase/7/docs/api/javax/net/ssl/TrustManagerFactory.html
            SSLContext:          https://docs.oracle.com/javase/7/docs/api/javax/net/ssl/SSLContext.html
            SSLSocketFactory:    https://docs.oracle.com/javase/7/docs/api/javax/net/ssl/SSLSocketFactory.html
            */

            Context myContext = getApplicationContext();
            InputStream inStream = myContext.getResources().openRawResource(R.raw.tdmssl);
            // Use the .crt file obtained via this script: https://gist.github.com/ivanlmj/a6a93dd142fb623d01262303d5bd8074
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            Certificate ca = cf.generateCertificate(inStream);
            inStream.close();

            String keyStoreType = KeyStore.getDefaultType();
            KeyStore ks = KeyStore.getInstance(keyStoreType);
            ks.load(null, null);
            ks.setCertificateEntry("ca", ca);

            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
            tmf.init(ks);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);

            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            hurlStack = new HurlStack(null, sslSocketFactory);

        } catch (Exception e) {
            Log.d("my_app", "LoginScreen.java - SSL Exception: " + e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated() called with: activity = [" + activity + "], savedInstanceState = [" + savedInstanceState + "]");
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Log.d(TAG, "onActivityStarted() called with: activity = [" + activity + "]");
    }

    @Override
    public void onActivityResumed(Activity activity) {
        Log.d(TAG, "onActivityResumed() called with: activity = [" + activity + "]");
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Log.d(TAG, "onActivityPaused() called with: activity = [" + activity + "]");
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Log.d(TAG, "onActivityStopped() called with: activity = [" + activity + "]");
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        Log.d(TAG, "onActivitySaveInstanceState() called with: activity = [" + activity + "], outState = [" + outState + "]");
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Log.d(TAG, "onActivityDestroyed() called with: activity = [" + activity + "]");
    }

}