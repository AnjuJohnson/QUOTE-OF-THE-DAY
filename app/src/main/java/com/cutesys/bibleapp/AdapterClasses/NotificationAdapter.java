package com.cutesys.bibleapp.AdapterClasses;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cutesys.bibleapp.Helperclasses.ListItem;
import com.cutesys.bibleapp.R;

import java.util.ArrayList;

/**
 * Created by Athira on 3/2/2017.
 */
public class NotificationAdapter extends RecyclerView.Adapter {

    private Activity mActivity;
    private RecyclerView mRecyclerView;

    private ArrayList<ListItem> mListItem;
    private ListItem item;

    public NotificationAdapter(Activity activity, ArrayList<ListItem> listitem){
        mActivity = activity;
        mListItem = listitem;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView text, date;

        public ViewHolder(View view) {
            super(view);

            text = (TextView) view.findViewById(R.id.text);
            date = (TextView) view.findViewById(R.id.date);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View rootView = LayoutInflater.
                from(mActivity).inflate(R.layout.notificationitem, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        rootView.setLayoutParams(lp);
        return new ViewHolder(rootView);


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int i) {
        final ViewHolder mViewHolder = (ViewHolder) holder;
        item = mListItem.get(i);

        mViewHolder.text.setText(item.get_name());
        mViewHolder.date.setText(item.get_data());

    }

    @Override
    public int getItemCount() {
        return mListItem.size();
    }
}
