package com.cutesys.bibleapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cutesys.bibleapp.Helperclasses.Config;
import com.cutesys.bibleapp.Helperclasses.HttpOperations;
import com.cutesys.qdlibrary.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by user on 10/21/2016.
 */

public class SplashActivity  extends AppCompatActivity {
    ImageView mIcon;
    FloatingActionButton mSubmit;
    private SharedPreferences sPreferences;
    Animation slideUpAnimation, slideDownAnimation;
    private EditText mInputName;
    Config mConfig;
    TextView mWelcome,mEdit;
    private CardView mInvisiblelayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        sPreferences = getSharedPreferences("Thoughtoftheday", MODE_PRIVATE);
        mEdit=(TextView) findViewById(R.id.edit);
        mIcon=(ImageView)findViewById(R.id.icon);
        mSubmit=(FloatingActionButton) findViewById(R.id.submit);
        mInputName=(EditText)findViewById(R.id.inputname);
        mWelcome=(TextView) findViewById(R.id.welcome);
        mInvisiblelayout=(CardView) findViewById(R.id.Invisiblelayout);
       /* slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up_animation);
        startSlideUpAnimation();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                mInputName.setVisibility(View.VISIBLE);
            }
        }, 7000);*/




        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConfig = new Config(getApplicationContext());
                if (mConfig.isOnline(getApplicationContext())) {
                    SplashActivity.LoadAddUser mLoadAddUser = new LoadAddUser(mInputName.getText().toString().trim()
                            .replace(" ", "%20"));
                    mLoadAddUser.execute((Void) null);
                } else {
                    Toast.makeText(getApplicationContext(),"No Internet",Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.bottom_up,
                        android.R.anim.fade_out);
                finish();
            }
        });
        mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInvisiblelayout.setVisibility(View.VISIBLE);
                mWelcome.setVisibility(View.GONE);
            }
        });


        if(!sPreferences.getString("NewUserId","").equals("")){
            mInvisiblelayout.setVisibility(View.GONE);
            mSubmit.setVisibility(View.VISIBLE);
            mWelcome.setText("Welcome "+sPreferences.getString("UserName",""));
            Log.d("username=",sPreferences.getString("UserName",""));
            mEdit.setVisibility(View.VISIBLE);
        }
        else {
            mInvisiblelayout.setVisibility(View.VISIBLE);
        }
    }
    public class LoadAddUser extends AsyncTask<Void,StringBuilder,StringBuilder> {
        private String mNewUser;
        LoadAddUser(String sharetext){
            this.mNewUser=sharetext;

        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected StringBuilder doInBackground(Void... params) {
            HttpOperations httpOperations = new HttpOperations(getApplicationContext());
            StringBuilder resultt = httpOperations.doAddUser(mNewUser);
            return resultt;
        }

        @Override
        protected void onPostExecute(StringBuilder resultt) {
            super.onPostExecute(resultt);
            try {
                JSONObject jsonObj = new JSONObject(resultt.toString());

                if (jsonObj.has("status")) {
                    if (jsonObj.getString("status").equals(String.valueOf(200))) {

                        if((!jsonObj.getString("id").equals(""))&&(!jsonObj.getString("id").equals("null"))){
                            SharedPreferences.Editor editor = sPreferences.edit();
                            editor.clear();
                            editor.commit();
                            mConfig.savePreferences(getApplicationContext(),"NewUserId",jsonObj.getString("id"));
                            Log.d("xxxxx",sPreferences.getString("NewUserId", ""));
                            mConfig.savePreferences(getApplicationContext(),"UserName",mInputName.getText().toString().trim());
                            Log.d("yyyy",sPreferences.getString("UserName",""));
                        }
                    }else {
                        Toast.makeText(getApplicationContext(),"No data",Toast.LENGTH_SHORT).show();
                        // switcher.showEmptyView();
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Please Try Again",Toast.LENGTH_SHORT).show();
                // switcher.showErrorView("Please Try Again");
            } catch (NullPointerException e) {
                Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_SHORT).show();
                // switcher.showErrorView("No Internet Connection");
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"Please Try Again",Toast.LENGTH_SHORT).show();
                // switcher.showErrorView("Please Try Again");
            }
        }
    }



    public void startSlideUpAnimation() {
        mIcon.startAnimation(slideUpAnimation);
    }


}