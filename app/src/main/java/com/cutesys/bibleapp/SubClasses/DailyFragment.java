package com.cutesys.bibleapp.SubClasses;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cutesys.bibleapp.AdapterClasses.ViewPagerAdapter;
import com.cutesys.bibleapp.Alarm.AlarmMe;
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
public class DailyFragment extends Fragment {

    private SqliteHelper helper;

    private Switcher switcher;
    private Calendar cal;
    private int day, year, month;
    private String mDate, mday, mmonth;
    private FloatingActionButton setdate;
    private ViewPagerAdapter mViewpagerAdapter;

    private ViewPager mViewpager;
    private LinearLayout pager_indicator;
    private ImageView[] dots;

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
        View rootView = inflater.inflate(R.layout.dailyfragment, container, false);

        helper = new SqliteHelper(getActivity(), "Thoughtfortheday", null, 1);

        InitIDView(rootView);
        return rootView;
    }

    private void InitIDView(View rootview){
        setdate = (FloatingActionButton) rootview.findViewById(R.id.setdate);
        setdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),AlarmMe.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.bottom_up,
                        android.R.anim.fade_out);
                getActivity().finish();
            }
        });
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

        switcher = new Switcher.Builder(getActivity())
                .addContentView(rootview.findViewById(R.id.contentview))
                .addErrorView(rootview.findViewById(R.id.error_view))
                .addProgressView(rootview.findViewById(R.id.progress_view))
                .setErrorLabel((TextView) rootview.findViewById(R.id.error_label))
                .setEmptyLabel((TextView) rootview.findViewById(R.id.empty_label))
                .addEmptyView(rootview.findViewById(R.id.empty_view))
                .build();

        mViewpager=(ViewPager)rootview.findViewById(R.id.viewpager);
        pager_indicator = (LinearLayout)rootview.findViewById(R.id.viewPagerCountDots);

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

    public class LoadThoughtInitiate extends AsyncTask<Void, StringBuilder, StringBuilder> {

        private String mDate;

        LoadThoughtInitiate(String date) {
            mDate = date;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dataItem = new ArrayList<>();
            helper.Delete_thought_details();
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

                                List<HashMap<String, String>> fillMaps = new ArrayList<HashMap<String, String>>();
                                HashMap<String, String> map;
                                map = new HashMap<String, String>();

                                map.put("thought_id", feedObj.getString("id"));
                                map.put("thought", feedObj.getString("quote"));
                                map.put("date", mDate);
                                fillMaps.add(map);
                                helper.Insert_thought_details(fillMaps);
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