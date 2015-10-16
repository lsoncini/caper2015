package com.caper.caper2015.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;

import com.caper.caper2015.R;
import com.caper.caper2015.parse.Company;
import com.caper.caper2015.view.StandList;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Lucas on 06/10/2015.
 */
public class FavoritesActivity extends ActionBarActivity implements StandList.StandListListener, StandDetailFragment.FavoritesListener{

    StandListFragment slf = new StandListFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stands);
        ArrayList<String> ids =  new ArrayList<>();
        ids.addAll(getSharedPreferences("favorites", MODE_PRIVATE).getStringSet("ids", new HashSet<String>()));
        if (savedInstanceState == null) {
            Bundle b = new Bundle();
            b.putStringArrayList("ids",ids);
            slf = new StandListFragment();
            slf.setArguments(b);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, slf)
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
    public void onStandSelected(Company stand) {
        StandDetailFragment standDetailFragment = new StandDetailFragment().setStand(stand);
        standDetailFragment.setFavoritesListener(this);
        navTo(standDetailFragment);
    }

    void navTo(LoadingFragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack("")
                .commit()
        ;
    }

    @Override
    public void onFavoriteListChanged() {
        ((StandDetailFragment.FavoritesListener)slf).onFavoriteListChanged();
    }
}
