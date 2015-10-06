package com.caper.caper2015.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.caper.caper2015.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Lucas on 05/10/2015.
 */
public class SettingsActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle(getResources().getString(R.string.settings_title));
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_info, menu);
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


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        @Bind(R.id.lastUpdateTV)    TextView lastUpdateTV;
        @Bind(R.id.updateDataLayout)RelativeLayout update;
        @Bind(R.id.aboutLayout)     RelativeLayout about;
        SimpleDateFormat localSDF = new SimpleDateFormat("yyMMddHHmmss");
        SimpleDateFormat screenSDF = new SimpleDateFormat("dd/MM/yy HH:mm");

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
            ButterKnife.bind(this, rootView);
            String lastLocalUpdate = getActivity().getSharedPreferences("parseData", MODE_PRIVATE).getString("lastUpdate", "");

            try {
                lastUpdateTV.setText(screenSDF.format(localSDF.parse(lastLocalUpdate)));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mainIntent = new Intent().setClass(
                            getActivity(), SplashScreenActivity.class);
                    startActivity(mainIntent);
                    getActivity().finish();
                }
            });

            about.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(),"ABOUT CLICKED",Toast.LENGTH_LONG).show();
                }
            });

            return rootView;
        }
    }
}