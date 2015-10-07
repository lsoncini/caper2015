package com.caper.caper2015.activity;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;

import com.caper.caper2015.R;
import com.caper.caper2015.parse.Company;
import com.caper.caper2015.view.StandList;


public class StandsActivity extends ActionBarActivity implements StandList.StandListListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stands);
        if (savedInstanceState == null) {
            Bundle b = new Bundle();
            b.putStringArrayList("ids",null);
            Fragment f = new StandListFragment();
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
    public void onStandSelected(Company stand) {
        navTo(new StandDetailFragment().setStand(stand));
    }

    void navTo(LoadingFragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack("")
                .commit()
        ;
    }
}
