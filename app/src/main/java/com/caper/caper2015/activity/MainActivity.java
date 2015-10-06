package com.caper.caper2015.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.caper.caper2015.R;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);

        LinearLayout events = (LinearLayout)findViewById(R.id.eventsButton);
        LinearLayout stands = (LinearLayout)findViewById(R.id.standsButton);
        LinearLayout maps = (LinearLayout)findViewById(R.id.mapsButton);
        LinearLayout info = (LinearLayout)findViewById(R.id.infoButton);
        LinearLayout settings = (LinearLayout)findViewById(R.id.settingsButton);
        LinearLayout favs = (LinearLayout)findViewById(R.id.favsButton);

        events.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ZZEventsActivity.class);
                startActivity(intent);
            }
        });
        stands.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StandsActivity.class);
                startActivity(intent);
            }
        });
        maps.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapDisplayActivity.class);
                intent.putStringArrayListExtra("list",null);
                startActivity(intent);
            }
        });
        info.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,InfoActivity.class);
                startActivity(intent);
            }
        });
        favs.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,FavoritesActivity.class);
                startActivity(intent);
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(intent);
            }
        });
    }
}
