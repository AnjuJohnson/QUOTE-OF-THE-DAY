package com.cutesys.bibleapp.SubClasses;

import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cutesys.bibleapp.AdapterClasses.ViewPagerAdapter;
import com.cutesys.bibleapp.Helperclasses.Config;
import com.cutesys.bibleapp.Helperclasses.HttpOperations;
import com.cutesys.bibleapp.Helperclasses.ListItem;
import com.cutesys.bibleapp.Helperclasses.SqliteHelper;
import com.cutesys.bibleapp.R;
import com.cutesys.qdlibrary.FloatingActionButton;
import com.cutesys.qdlibrary.Switcher.Switcher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by user on 4/18/2017.
 */

public class HistoryFragment extends Fragment implements View.OnClickListener{

    private SqliteHelper helper;

    private Calendar cal;
    private int day, year, month;
    private String mDate, mday, mmonth;

    private ViewPagerAdapter mViewpagerAdapter;
    private TextView error_label_retry, empty_label_retry;
    private Switcher switcher;
    private FloatingActionButton setdate;
    private ImageView[] dots;
    private ViewPager mViewpager;
    private LinearLayout pager_indicator;
    private TextView imgGenerated;

    ArrayList<ListItem> dataItem;

    private int[] mImageResources = {
            R.drawable.img1,
            R.drawable.img2,
            R.drawable.img3,
            R.drawable.img4,
            R.drawable.img5,
    };

    private int dotsCount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.history_fragment, container, false);

        helper = new SqliteHelper(getActivity(), "Thoughtfortheday", null, 1);

        InitIdView(rootView);
        return rootView;
    }

    private void InitIdView(View rooView){

        setdate = (FloatingActionButton)rooView.findViewById(R.id.setdate);
        mViewpager=(ViewPager)rooView.findViewById(R.id.viewpager);
        imgGenerated = (TextView)rooView.findViewById(R.id.imgGenerated);
        pager_indicator = (LinearLayout)rooView.findViewById(R.id.viewPagerCountDots);

        setdate.setOnClickListener(this);

        cal = Calendar.getInstance();
        day = cal.get(java.util.Calendar.DAY_OF_MONTH);
        month = cal.get(java.util.Calendar.MONTH);
        year = cal.get(java.util.Calendar.YEAR);
        mday = String.valueOf(day);
        mmonth = String.valueOf(month+1);
        if(mday.length()==1){
            mday="0"+String.valueOf(mday);
        }
        if(mmonth.length()==1){
            mmonth="0"+String.valueOf(mmonth);
        }

        mDate = year + "-" + mmonth + "-" + mday;
        imgGenerated.setText(""+mday+"-"+mmonth+"-"+year);

        switcher = new Switcher.Builder(getActivity())
                .addContentView(rooView.findViewById(R.id.contentview))
                 .addErrorView(rooView.findViewById(R.id.error_view))
                .addProgressView(rooView.findViewById(R.id.progress_view))
                .setErrorLabel((TextView) rooView.findViewById(R.id.error_label))
                .setEmptyLabel((TextView) rooView.findViewById(R.id.empty_label))
                 .addEmptyView(rooView.findViewById(R.id.empty_view))
                .build();
        error_label_retry = ((TextView) rooView.findViewById(R.id.error_label_retry));
        empty_label_retry = ((TextView)rooView.findViewById(R.id.empty_label_retry));
        error_label_retry.setOnClickListener(this);
        empty_label_retry.setOnClickListener(this);
        final List<HashMap<String, String>> Data_Item;
        Data_Item = helper.getthoughtdetails();

        if((Data_Item.size() > 0) && ((Data_Item.get(0).get("date")).equals(mDate))){
            try {
                switcher.showProgressView();
                dataItem = new ArrayList<>();
                for (int i = 0 ; i < Data_Item.size() ; i++){
                    ListItem item = new ListItem();

                    item.set_id(Data_Item.get(i).get("thought_id"));
                    item.set_name(Data_Item.get(i).get("thought"));

                    dataItem.add(item);
                }
                switcher.showContentView();
                LoadToViewpager();
            } catch (Exception e) {
            }
        } else {
            InitDataView(mDate);
        }
    }

    private void InitDataView(String date){
        Config mConfig = new Config(getActivity());
        if(mConfig.isOnline(getActivity())){
            LoadThoughtInitiate mLoadThoughtInitiate = new LoadThoughtInitiate(date);
            mLoadThoughtInitiate.execute((Void) null);
        }else {
             switcher.showErrorView("No Internet Connection");
        }
    }

    @Override
    public void onClick(View view) {
        int buttonId = view.getId();

        switch (buttonId){
            case R.id.error_label_retry:
                InitDataView(mDate);
                break;
            case R.id.empty_label_retry:
                InitDataView(mDate);
                break;
            case R.id.setdate:
                Calendar mCurrentDate = Calendar.getInstance();
                int mYear = mCurrentDate.get(Calendar.YEAR);
                int mMonth = mCurrentDate.get(Calendar.MONTH);
                int mDay = mCurrentDate.get(Calendar.DAY_OF_MONTH);

                final DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedYear, int selectedMonth, int selectedDay) {

                        Calendar c = Calendar.getInstance();
                        c.set(Calendar.DAY_OF_MONTH, selectedDay);
                        c.set(Calendar.MONTH, selectedMonth);
                        c.set(Calendar.YEAR, selectedYear);

                        long selectedTimeInMillis = c.getTimeInMillis();
                        long systemTimeInMillis = Calendar.getInstance().getTimeInMillis();
                        if(selectedTimeInMillis<=systemTimeInMillis) {

                            mday = String.valueOf(selectedDay);
                            mmonth = String.valueOf(selectedMonth + 1);
                            if (mday.length() == 1) {
                                mday = "0" + String.valueOf(mday);
                            }
                            if (mmonth.length() == 1) {
                                mmonth = "0" + String.valueOf(mmonth);
                            }

                            mDate = selectedYear + "-" + mmonth + "-" + mday;
                            imgGenerated.setText("" + mday + "-" + mmonth + "-" + selectedYear);

                            InitDataView(mDate);
                        }
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                mDatePicker.show();
                break;
        }
    }

    private void setUiPageViewController() {

        dotsCount = mViewpagerAdapter.getCount();
        dots = new ImageView[dotsCount];
        pager_indicator.removeAllViews();

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(getActivity());
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 0, 4, 0);

            pager_indicator.addView(dots[i], params);
        }

        dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
    }

    public class LoadThoughtInitiate extends AsyncTask<Void, StringBuilder, StringBuilder> {

        private String mDate;

        LoadThoughtInitiate(String date) {
            mDate = date;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dataItem = new ArrayList<>();
            switcher.showProgressView();
        }

        @Override
        protected StringBuilder doInBackground(Void... params) {

            HttpOperations httpOperations = new HttpOperations(getActivity());
            StringBuilder result = httpOperations.doNewThought(mDate);
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
                        JSONArray feedArray = jsonObj.getJSONArray("quote_list");
                        for (int i = 0; i < feedArray.length(); i++) {

                            ListItem item = new ListItem();
                            JSONObject feedObj = (JSONObject) feedArray.get(i);

                            if((!feedObj.getString("quote").equals(""))
                                    && (!feedObj.getString("quote").equals("null"))) {

                                item.set_id(feedObj.getString("id"));
                                item.set_name(feedObj.getString("quote"));

                                dataItem.add(item);
                            }
                        }

                       LoadToViewpager();

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

    private void LoadToViewpager(){

        mViewpagerAdapter = new ViewPagerAdapter(getActivity(),
                mImageResources,mImageResources.length,dataItem);

        mViewpager.setAdapter(mViewpagerAdapter);
        setUiPageViewController();

        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < dotsCount; i++) {
                    dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
                }

                dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
