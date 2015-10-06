package com.caper.caper2015.activity;

/**
 * Created by Lucas on 15/09/2015.
 */
import android.app.Application;

import com.caper.caper2015.parse.Booth;
import com.caper.caper2015.R;
import com.caper.caper2015.parse.Building;
import com.caper.caper2015.parse.Company;
import com.caper.caper2015.parse.Config;
import com.caper.caper2015.parse.Event;
import com.caper.caper2015.parse.Human;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class CaperApp extends Application{

    public void onCreate() {
        // Register ParseObject subclasses

        ParseObject.registerSubclass(Booth.class);
        ParseObject.registerSubclass(Building.class);
        ParseObject.registerSubclass(Company.class);
        ParseObject.registerSubclass(Event.class);
        ParseObject.registerSubclass(Human.class);
        ParseObject.registerSubclass(Config.class);

        // Initialize Parse
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, getString(R.string.parse_application_id),
                getString(R.string.parse_client_key));
        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        ParseACL.setDefaultACL(defaultACL, true);
    }
}