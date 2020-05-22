package ir.goliforoshani.sms.ui.intro;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
//        AppManager.get(getApplication()).save_splash(2);

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
        Keeper.getInstance().save(amount.INTRO_IS_STARTED,"started");
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    private void getJson(){
        itemIntroList.add(new IntroModel("title","subtitle", "desc",bg_from,bg_to,color_primary,color_secondary));
        itemIntroList.add(new IntroModel("title","subtitle", "desc",bg_from,bg_to,color_primary,color_secondary));
        itemIntroList.add(new IntroModel("title","subtitle", "desc",bg_from,bg_to,color_primary,color_secondary));
        itemIntroList.add(new IntroModel("title","subtitle", "desc",bg_from,bg_to,color_primary,color_secondary));
        adaptorIntro.notifyDataSetChanged();
        recyclerView.getAdapter();
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
}
