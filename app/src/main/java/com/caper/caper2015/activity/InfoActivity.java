package com.caper.caper2015.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.caper.caper2015.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Lucas on 26/09/2015.
 */
public class InfoActivity extends ActionBarActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        setTitle(getResources().getString(R.string.app_name));
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

        @Bind(R.id.fb_link)     RelativeLayout fb;
        @Bind(R.id.youtube_link)     RelativeLayout youtube;
        @Bind(R.id.twitter_link)     RelativeLayout tw;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_info, container, false);
            ButterKnife.bind(this, rootView);

            fb.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    Intent intent;
                    String url = "https://www.facebook.com/CAPER-SHOW-108299529195290";
                    try {
                        getActivity().getPackageManager().getPackageInfo("com.facebook.katana", 0);
                        Uri uri = Uri.parse("fb://facewebmodal/f?href=" + url);
                        intent = new Intent(Intent.ACTION_VIEW,uri);
                    } catch (Exception e) {
                        intent = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
                    }
                    startActivity(intent);
                }

            });
            youtube.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent= new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.youtube.com/channel/UCA3oPY5ZztBniKnpXHHqjBw"));
                    startActivity(intent);
                }
            });
            tw.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent;
                    try {
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=CAPERShow"));
                        startActivity(intent);
                    } catch (Exception e) {
                        intent = new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.twitter.com/#!/CAPERShow"));
                        startActivity(intent);
                    }

                }
            });
            return rootView;
        }
    }
}
