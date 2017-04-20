package com.cutesys.bibleapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by user on 10/21/2016.
 */

public class SplashActivity extends AppCompatActivity {
    long Delay=3000;
    ImageView splashlogo;
    TextView appname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        startzoomOutAnimation();
        Timer RunSplash = new Timer();
        // Task to do when the timer ends
        TimerTask ShowSplash = new TimerTask() {
            @Override
            public void run() {
                // Close SplashScreenActivity.class

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        finish();

                        // Start HomeActivity.class
                        Intent myIntent = new Intent(SplashActivity.this,HomeActivity.class);
                        startActivity(myIntent);
                    }
                });
            }
        };
        // Start the timer
        RunSplash.schedule(ShowSplash, Delay);
    }
    public void startzoomOutAnimation()
    {
        Animation zoomOutAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_out_animation);
        Animation textMove = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.text_anim);
        splashlogo=(ImageView)findViewById(R.id.spalshlogo);
        splashlogo.startAnimation(zoomOutAnimation);
        appname=(TextView)findViewById(R.id.appname);
        appname.startAnimation(textMove);
    }

}
