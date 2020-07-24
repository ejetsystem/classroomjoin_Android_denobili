package com.denobili.app.landing_page;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.denobili.app.R;
import com.denobili.app.splashPage.LanguageSelectionActivity;
import com.denobili.app.utils.SharedPreferencesData;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class SlideActivity extends AppCompatActivity {
    private static ViewPager mPager;
    private Button started;
    TextView skip;
    private boolean isLastPageSwiped=false;
    private int counterPageScroll;
    Timer swipeTimer;
    SharedPreferencesData sharedPreferencesData;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private static final Integer[] IMAGES= {R.drawable.artboard6,R.drawable.artboard3,R.drawable.artboard2,R.drawable.artboard1,R.drawable.artboard4};
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide);
         swipeTimer = new Timer();
        sharedPreferencesData=new SharedPreferencesData(this);
        sharedPreferencesData.welCome("isLaunch");
        started = (Button) findViewById(R.id.started);
        skip = (TextView) findViewById(R.id.skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sharedPreferencesData.getUser_languafe() == null || sharedPreferencesData.getUser_languafe().equals("")) {
                    Intent intent=new Intent(SlideActivity.this, LanguageSelectionActivity.class);
                    intent.putExtra("is_from" ,true);
                    startActivity(intent);
                    finish();
                }
            }
        });
        started.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sharedPreferencesData.getUser_languafe() == null || sharedPreferencesData.getUser_languafe().equals("")) {
                    Intent intent=new Intent(SlideActivity.this, LanguageSelectionActivity.class);
                    intent.putExtra("is_from" ,true);
                    startActivity(intent);
                    finish();
                }
            }
        });
        init();
    }

    private void init() {
        for(int i=0;i<IMAGES.length;i++)
            ImagesArray.add(IMAGES[i]);

        mPager = (ViewPager) findViewById(R.id.pager);


        mPager.setAdapter(new SlidingImage_Adapter(SlideActivity.this,ImagesArray));


        CirclePageIndicator indicator = (CirclePageIndicator)
                findViewById(R.id.indicator);

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        indicator.setRadius(5 * density);

        NUM_PAGES =IMAGES.length;

        // Auto start of viewpager
        if (isLastPageSwiped){
            swipeTimer.cancel();
        }
        else {
            final Handler handler = new Handler();
            final Runnable Update = new Runnable() {
                public void run() {
                    if (currentPage == NUM_PAGES) {
                        currentPage = 0;
                        started.setVisibility(View.GONE);
                        skip.setVisibility(View.VISIBLE);
                    }
                    else if (currentPage==4){
                        started.setVisibility(View.VISIBLE);
                        skip.setVisibility(View.GONE);
                    }
                    else {

                    }

                    mPager.setCurrentItem(currentPage++, true);
                }
            };

            swipeTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(Update);
                }
            }, 3000, 3000);
        }


        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {
                if (pos == 3 && arg1 == 0 && !isLastPageSwiped){

                        isLastPageSwiped=true;
                       swipeTimer.cancel();
                        //Next Activity here

                    counterPageScroll++;
                }else{
                    isLastPageSwiped=false;
                    counterPageScroll=0;
                }
            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }

}