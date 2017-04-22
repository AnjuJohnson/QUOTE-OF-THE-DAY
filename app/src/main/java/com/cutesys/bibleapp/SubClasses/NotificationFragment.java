package com.cutesys.bibleapp.SubClasses;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cutesys.bibleapp.AdapterClasses.NotificationAdapter;
import com.cutesys.bibleapp.Helperclasses.Config;
import com.cutesys.bibleapp.Helperclasses.HttpOperations;
import com.cutesys.bibleapp.Helperclasses.ListItem;
import com.cutesys.bibleapp.R;
import com.cutesys.qdlibrary.Switcher.Switcher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Kris on 4/19/2017.
 */

public class NotificationFragment extends Fragment {

    private Switcher switcher;
    private RecyclerView mrecyclerview;

    private NotificationAdapter mNotificationAdapter;
    ArrayList<ListItem> dataItem;
    int start = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.notificationfragment, container, false);

        InitIDView(rootView);
        return rootView;
    }

    private void InitIDView(View rootview){


        switcher = new Switcher.Builder(getActivity())
                .addContentView(rootview.findViewById(R.id.mrecyclerview))
                 .addErrorView(rootview.findViewById(R.id.error_view))
                .addProgressView(rootview.findViewById(R.id.progress_view))
                .setErrorLabel((TextView) rootview.findViewById(R.id.error_label))
                 .setEmptyLabel((TextView) rootview.findViewById(R.id.empty_label))
                 .addEmptyView(rootview.findViewById(R.id.empty_view))
                .build();

        mrecyclerview = ((RecyclerView)rootview.findViewById(R.id.mrecyclerview));
        mrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onStart() {
        super.onStart();

        InitGetData();
    }

    private void InitGetData(){
        Config mConfig = new Config(getActivity());
        if(mConfig.isOnline(getActivity())){
            LoadThoughtNotificationInitiate mLoadThoughtNotificationInitiate =
                    new LoadThoughtNotificationInitiate();
            mLoadThoughtNotificationInitiate.execute((Void) null);
        }else {
            switcher.showErrorView("No Internet Connection");
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

            HttpOperations httpOperations = new HttpOperations(getActivity());
            StringBuilder result = httpOperations.doNotifications();
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
                        JSONArray feedArray = jsonObj.getJSONArray("notification_list");
                        for (int i = 0; i < feedArray.length(); i++) {

                            ListItem item = new ListItem();
                            JSONObject feedObj = (JSONObject) feedArray.get(i);

                            if((!feedObj.getString("notification").equals(""))
                                    && (!feedObj.getString("notification").equals("null"))) {

                                item.set_id(feedObj.getString("id"));
                                item.set_name(feedObj.getString("notification"));
                                item.set_data(feedObj.getString("date"));

                                dataItem.add(item);
                            }
                        }

                        start = dataItem.size();
                        mNotificationAdapter = new NotificationAdapter(getActivity(), dataItem);
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
}
