package com.caper.caper2015.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

import com.caper.caper2015.R;

import butterknife.Bind;

import com.caper.caper2015.parse.Event;
import com.caper.caper2015.view.EventList;
import com.parse.ParseException;

import java.util.List;

public class EventListFragment extends LoadingFragment {

    @Bind(R.id.eventList)
    EventList eventList;

    List<Event> events = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_list, container, false);
        ButterKnife.bind(this, view);
        List<String> ids = getArguments().getStringArrayList("ids");
        try {
            if(ids==null) {
                events = Event.createQuery().include("booth")
                        .include("humans")
                        .include("companies")
                        .orderByAscending("date_start")
                        .whereNotEqualTo("date_start", null)
                        .whereNotEqualTo("date_end", null).find();
            } else{
                events = Event.createQuery().include("booth")
                        .include("humans")
                        .include("companies")
                        .orderByAscending("date_start").whereContainedIn("objectId",ids)
                        .whereNotEqualTo("date_start", null)
                        .whereNotEqualTo("date_end", null).find();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        eventList.setListener((EventList.EventListListener) getActivity());
        updateView();
        return view;
    }

    public void updateView() {
        if (getView() == null) return;

        eventList.setEvents(events);
    }

    @Override
    public String getTitle() { return getResources().getString(R.string.events_title);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateView();
    }
}
