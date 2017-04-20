package com.cutesys.bibleapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cutesys.bibleapp.Alarm.Alarm;
import com.cutesys.bibleapp.Alarm.AlarmMe;
import com.cutesys.bibleapp.Alarm.EditAlarm;
import com.cutesys.bibleapp.Alarm.Preferences;

/**
 * Created by user on 4/18/2017.
 */

public class Activity_Alarm extends AppCompatActivity {
    private ImageView settings;
    private FloatingActionButton mAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        settings = (ImageView) findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), Preferences.class);
               // Intent intent = new Intent(getApplicationContext(),AlarmMe.class);
                startActivity(intent);
                overridePendingTransition(R.anim.bottom_up,
                        android.R.anim.fade_out);
                finish();
            }
        });

    }

}
