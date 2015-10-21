package com.caper.caper2015.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.caper.caper2015.R;
import com.caper.caper2015.parse.Event;
import com.caper.caper2015.view.DayList;
import com.parse.ParseException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Lucas on 20/10/2015.
 */
public class DayListFragment extends LoadingFragment implements DayList.DayListListener{

    @Bind(R.id.dayList) DayList dayList;
    private static final String FIRST_DAY = "28/10/2015";
    private static final String SECOND_DAY = "29/10/2015";
    private static final String THIRD_DAY = "30/10/2015";
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    List<Event> events = new ArrayList<>();
    ArrayList<String> ids1 = new ArrayList<>();
    ArrayList<String> ids2 = new ArrayList<>();
    ArrayList<String> ids3 = new ArrayList<>();

    List<String> days = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day_list, container, false);
        ButterKnife.bind(this, view);
        try {
            events = Event.createQuery().include("booth")
                    .include("humans")
                    .include("companies")
                    .orderByAscending("date_start")
                    .whereNotEqualTo("date_start", null)
                    .whereNotEqualTo("date_end", null).find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for(Event e : events){
            Log.i("fecha", sdf.format(e.getDateStart()));
            if(sdf.format(e.getDateStart()).compareTo(FIRST_DAY)==0)
                ids1.add(e.getObjectId());
            else if(sdf.format(e.getDateStart()).compareTo(SECOND_DAY)==0)
                ids2.add(e.getObjectId());
            else if(sdf.format(e.getDateStart()).compareTo(THIRD_DAY)==0)
                ids3.add(e.getObjectId());
        }
        Log.i("cant ids in ids1",String.valueOf(ids1.size()));
        Log.i("cant ids in ids2",String.valueOf(ids2.size()));
        Log.i("cant ids in ids3",String.valueOf(ids3.size()));
        days = new ArrayList<>();
        days.add(FIRST_DAY);
        days.add(SECOND_DAY);
        days.add(THIRD_DAY);
        dayList.setListener(this);
        updateView();
        return view;
    }

    public void updateView() {
        if (getView() == null) return;

        showSpinner();
        dayList.setDays(days);
        hideSpinner();

    }

    @Override
    public String getTitle() {
        return getResources().getString(R.string.events_title);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateView();
    }

    @Override
    public void onDaySelected(String day) {
        Log.i("clicked day", day);
        ArrayList<String> ids = new ArrayList<>();
        switch (day){
            case FIRST_DAY:
                ids.addAll(ids1);
                break;
            case SECOND_DAY:
                ids.addAll(ids2);
                break;
            default:
                ids.addAll(ids3);
                break;
        }
        Intent mainIntent = new Intent(getActivity(), EventsActivity.class);
        mainIntent.putStringArrayListExtra("ids", ids);
        startActivity(mainIntent);
    }
}