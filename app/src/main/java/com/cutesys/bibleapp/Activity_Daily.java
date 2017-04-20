package com.cutesys.bibleapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cutesys.bibleapp.Alarm.AlarmMe;

/**
 * Created by user on 4/18/2017.
 */

public class Activity_Daily extends Fragment {
    private FloatingActionButton fab;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dailyverses, container, false);
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),AlarmMe.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.bottom_up,
                        android.R.anim.fade_out);
                getActivity().finish();
            }
        });
        return rootView;
    }
}
