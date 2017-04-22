package com.cutesys.bibleapp.SubClasses;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.cutesys.bibleapp.AdapterClasses.ReviewAdapter;
import com.cutesys.bibleapp.Helperclasses.Config;
import com.cutesys.bibleapp.Helperclasses.HttpOperations;
import com.cutesys.bibleapp.Helperclasses.ListItem;
import com.cutesys.bibleapp.HomeActivity;
import com.cutesys.bibleapp.R;
import com.cutesys.qdlibrary.Switcher.Switcher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Kris on 4/19/2017.
 */

public class ViewReview extends AppCompatActivity implements View.OnClickListener{

    private Switcher switcher;
    SharedPreferences sPreferences;
    private RecyclerView mrecyclerview;
    private ImageButton btn_send;
    private ImageView close;
    private TextView error_label_retry, empty_label_retry;

    private ReviewAdapter mNotificationAdapter;
    ArrayList<ListItem> dataItem;
    int start = 0;

    EditText thought_text;
    String THOUGHTID;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_view);
        sPreferences = getSharedPreferences("Thoughtoftheday", MODE_PRIVATE);

        Intent i = getIntent();
        THOUGHTID = i.getExtras().getString("THOUGHTID");
        InitIDView();

    }

    private void InitIDView(){

        switcher = new Switcher.Builder(getApplicationContext())
                .addContentView(findViewById(R.id.mrecyclerview))
                .addErrorView(findViewById(R.id.error_view))
                .addProgressView(findViewById(R.id.progress_view))
                .setErrorLabel((TextView) findViewById(R.id.error_label))
                .setEmptyLabel((TextView) findViewById(R.id.empty_label))
                .addEmptyView(findViewById(R.id.empty_view))
                .build();

        close = ((ImageView) findViewById(R.id.close));
        error_label_retry = ((TextView) findViewById(R.id.error_label_retry));
        empty_label_retry = ((TextView)findViewById(R.id.empty_label_retry));
        error_label_retry.setOnClickListener(this);
        empty_label_retry.setOnClickListener(this);
        close.setOnClickListener(this);

        thought_text = (EditText)findViewById(R.id.thought_text);
        thought_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(thought_text.getText().toString().trim().length() > 0){

                    btn_send.setClickable(true);
                }else {
                    btn_send.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        btn_send = (ImageButton)findViewById(R.id.btn_send);
        btn_send.setOnClickListener(this);
        mrecyclerview = ((RecyclerView)findViewById(R.id.mrecyclerview));
        mrecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    @Override
    public void onStart() {
        super.onStart();

        InitGetData();
    }

    private void InitGetData(){
        Config mConfig = new Config(getApplicationContext());
        if(mConfig.isOnline(getApplicationContext())){
            LoadThoughtNotificationInitiate mLoadThoughtNotificationInitiate =
                    new LoadThoughtNotificationInitiate();
            mLoadThoughtNotificationInitiate.execute((Void) null);
        }else {
            switcher.showErrorView("No Internet Connection");
        }
    }

    @Override
    public void onClick(View view) {

        int buttonid = view.getId();
        switch(buttonid){
            case R.id.btn_send:
                Config mConfig = new Config(getApplicationContext());
                if(mConfig.isOnline(getApplicationContext())){
                    LoadAddReview mLoadAddReview =
                            new LoadAddReview(THOUGHTID,
                                    thought_text.getText().toString().trim().replace(" ", "%20")
                                    ,sPreferences.getString("NewUserId", ""));
                    mLoadAddReview.execute((Void) null);
                }else {
                    //switcher.showErrorView("No Internet Connection");
                }
                break;

            case R.id.error_label_retry:
                InitGetData();
                break;
            case R.id.empty_label_retry:
                InitGetData();
                break;
            case R.id.close:
                Intent intent = new Intent(ViewReview.this,HomeActivity.class);
//intent.putExtra("PAGE","COUNTRY");
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,
                        R.anim.bottom_down);
                finish();
                break;
        }
    }

    public class LoadAddReview extends AsyncTask<Void, StringBuilder, StringBuilder> {

        private String mquotes_id, mreview, muser;

        LoadAddReview(String quotes_id,String review,
                      String user) {
            mquotes_id = quotes_id;
            mreview = review;
            muser = user;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected StringBuilder doInBackground(Void... params) {

            HttpOperations httpOperations = new HttpOperations(getApplicationContext());
            StringBuilder result = httpOperations.doAddReview(mquotes_id, mreview, muser);
            return result;
        }
        @Override
        protected void onPostExecute(StringBuilder result) {
            super.onPostExecute(result);

            try {
                JSONObject jsonObj = new JSONObject(result.toString());

                if (jsonObj.has("status")) {
                    if (jsonObj.getString("status").equals(String.valueOf(200))) {
                        thought_text.setText("");

                        LoadThoughtNotificationInitiate mLoadThoughtNotificationInitiate =
                                new LoadThoughtNotificationInitiate();
                        mLoadThoughtNotificationInitiate.execute((Void) null);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();

            } catch (NullPointerException e) {

            } catch (Exception e) {

            }
        }
    }

    public class LoadThoughtNotificationInitiate extends AsyncTask<Void, StringBuilder, StringBuilder> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dataItem = new ArrayList<>();
            switcher.showProgressView();
        }

        @Override
        protected StringBuilder doInBackground(Void... params) {

            HttpOperations httpOperations = new HttpOperations(getApplicationContext());
            StringBuilder result = httpOperations.doReview_List(sPreferences.getString("NewUserId", "")+"");
            return result;
        }

        @Override
        protected void onPostExecute(StringBuilder result) {
            super.onPostExecute(result);

            try {
                JSONObject jsonObj = new JSONObject(result.toString());

                if (jsonObj.has("status")) {
                    if (jsonObj.getString("status").equals(String.valueOf(200))) {
                        switcher.showContentView();
                        JSONArray feedArray = jsonObj.getJSONArray("review_list");
                        for (int i = 0; i < feedArray.length(); i++) {

                            ListItem item = new ListItem();
                            JSONObject feedObj = (JSONObject) feedArray.get(i);

                            if((!feedObj.getString("review").equals(""))
                                    && (!feedObj.getString("review").equals("null"))) {
                                item.set_name(feedObj.getString("review"));

                                dataItem.add(item);
                            }
                        }
                        start = dataItem.size();
                        mNotificationAdapter = new ReviewAdapter(getApplicationContext(), dataItem);
                        mrecyclerview.setAdapter(mNotificationAdapter);

                    }else {
                        switcher.showEmptyView();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                switcher.showErrorView("Please Try Again");
            } catch (NullPointerException e) {
                switcher.showErrorView("No Internet Connection");
            } catch (Exception e) {
                switcher.showErrorView("Please Try Again");
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(i);
        overridePendingTransition(android.R.anim.fade_in,
                R.anim.bottom_down);
        finish();

    }
}
