package com.caper.caper2015.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.caper.caper2015.R;
import com.caper.caper2015.parse.Event;
import com.parse.ParseException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ZZEventsActivity extends TabActivity implements EventDetailFragment.EventDetailListener{
    // TabSpec Names
    private static final String FIRST_DAY = "28 oct. 2015";
    private static final String SECOND_DAY = "29 oct. 2015";
    private static final String THIRD_DAY = "30 oct. 2015";
    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
    List<Event> events = new ArrayList<>();
    ArrayList<String> ids1 = new ArrayList<>();
    ArrayList<String> ids2 = new ArrayList<>();
    ArrayList<String> ids3 = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getActionBar()!=null)
            getActionBar().hide();
        setContentView(R.layout.zzevents_activity);

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
            Log.i("fecha",sdf.format(e.getDateStart()));
            if(sdf.format(e.getDateStart()).equals(FIRST_DAY))
                ids1.add(e.getObjectId());
            else if(sdf.format(e.getDateStart()).equals(SECOND_DAY))
                ids2.add(e.getObjectId());
            else if(sdf.format(e.getDateStart()).equals(THIRD_DAY))
                ids3.add(e.getObjectId());
        }


        Log.i("blabla", "no se que onda");
        TabHost tabHost = getTabHost();

        // First Day Tab
        TabSpec firstSpec = tabHost.newTabSpec(FIRST_DAY);
        // Tab Icon
        firstSpec.setIndicator(FIRST_DAY);
        Intent firstIntent = new Intent(this, EventsActivity.class);
        firstIntent.putStringArrayListExtra("ids",ids1);
        // Tab Content
        firstSpec.setContent(firstIntent);

        // Second Day Tab
        TabSpec secondSpec = tabHost.newTabSpec(SECOND_DAY);
        secondSpec.setIndicator(SECOND_DAY);
        Intent secondIntent = new Intent(this, EventsActivity.class);
        secondIntent.putStringArrayListExtra("ids", ids2);

        secondSpec.setContent(secondIntent);

        // Third Day Tab
        TabSpec thirdSpec = tabHost.newTabSpec(THIRD_DAY);
        thirdSpec.setIndicator(THIRD_DAY);
        Intent thirdIntent = new Intent(this, EventsActivity.class);
        thirdIntent.putStringArrayListExtra("ids", ids3);

        thirdSpec.setContent(thirdIntent);

        // Adding all TabSpec to TabHost
        tabHost.addTab(firstSpec); // Adding First Day tab
        tabHost.addTab(secondSpec); // Adding Second Day tab
        tabHost.addTab(thirdSpec); // Adding Third Day tab
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_stands, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void detailOpened() {
        getTabHost().getTabWidget().setVisibility(View.GONE);
    }

    @Override
    public void detailClosed() {
        getTabHost().getTabWidget().setVisibility(View.VISIBLE);
    }
}