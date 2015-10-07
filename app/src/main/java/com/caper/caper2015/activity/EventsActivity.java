package com.caper.caper2015.activity;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;

import com.caper.caper2015.R;
import com.caper.caper2015.parse.Event;
import com.caper.caper2015.view.EventList;

import java.util.ArrayList;

public class EventsActivity extends ActionBarActivity implements EventList.EventListListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar()!=null)
            getSupportActionBar().hide();
        setContentView(R.layout.activity_events);

        ArrayList<String> ids = getIntent().getExtras().getStringArrayList("ids");
        if (savedInstanceState == null) {

            Bundle b = new Bundle();
            b.putStringArrayList("ids",ids);
            Fragment f = new EventListFragment();
            f.setArguments(b);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, f)
                    .commit()
            ;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_stands, menu);
        return true;
    }

    @Override
    public void onEventSelected(Event event, int position) {
        navTo(new EventDetailFragment().setEvent(event,position));
    }

    void navTo(LoadingFragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack("")
                .commit()
        ;
    }
}
