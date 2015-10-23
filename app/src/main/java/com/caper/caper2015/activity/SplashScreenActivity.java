package com.caper.caper2015.activity;

/**
 * Created by Lucas on 15/09/2015.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import com.caper.caper2015.R;
import com.caper.caper2015.parse.Booth;
import com.caper.caper2015.parse.Building;
import com.caper.caper2015.parse.Company;
import com.caper.caper2015.parse.Config;
import com.caper.caper2015.parse.Event;
import com.caper.caper2015.parse.Human;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SplashScreenActivity extends Activity {

    // Set the duration of the splash screen
    private static final long SPLASH_SCREEN_DELAY = 1000;
    static SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmmss");
    static int loadedClasses = 0;
    AlertDialog alertDialog;
    @Bind(R.id.loading)
    TextView loadingTV;

    Handler mhandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message inputMessage) {
            super.handleMessage(inputMessage);
            String action = inputMessage.getData().getString("action");
            if(action.equals("show dialog"))
                alertDialog.show();
            if(action.equals("updating"))
                loadingTV.setText(getResources().getString(R.string.updating));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set portrait orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Hide title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.splash_screen);
        ButterKnife.bind(this);

        getSharedPreferences("appData",MODE_PRIVATE).edit().putString("version","1.1.0").commit();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.error_loading);
        builder.setMessage(R.string.reload_msg);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                loadFromParse();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                finish();
            }
        });
        alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                loadFromParse();
            }
        };

        // Simulate a long loading process on application startup.
        Timer timer = new Timer();
        timer.schedule(task, SPLASH_SCREEN_DELAY);
    }

    private void loadFromParse() {

        if(!isNetworkAvailable() || dataUpdated()){
            startMainActivity();
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString("action","updating");
        Message msg = Message.obtain();
        msg.setData(bundle);
        mhandler.sendMessage(msg);

        SaveCallback sc = new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                    SplashScreenActivity.loadedClasses++;
                    if(loadedClasses==5){//className.equals("Human")){

                        getSharedPreferences("parseData",MODE_PRIVATE).edit().putBoolean("hasData",true).commit();
                        Date today = new Date();
                        Log.i("FECHA DE HOY", today.toString());
                        getSharedPreferences("parseData", MODE_PRIVATE).edit().putString("lastUpdate",format.format(today)).commit();

                        // Start the next activity
                        startMainActivity();

                        // Close the activity so the user won't able to go back this
                        // activity pressing Back button
                        finish();
                    }
                }else {
                    getSharedPreferences("parseData",MODE_PRIVATE).edit().putBoolean("hasData",false).commit();
                    Log.i("SplashScreenActivity",
                            "Error pinning objects: "
                                    + e.getMessage());
                }
            }
        };

        try {
            ParseObject.pinAllInBackground(Booth.load(), sc);
            List<Building> buildings= Building.load();
            for(Building b : buildings){
                getSharedPreferences("buildingNames",MODE_PRIVATE).edit().putString(b.getObjectId(),b.getName()).commit();
                try {
                    File file = new File(getApplicationContext().getFilesDir(),b.getObjectId());
                    file.createNewFile();
                    FileOutputStream outputStream = new FileOutputStream(file, false);
                    outputStream.write(b.getFloorPlan().getData());
                    outputStream.close();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
            ParseObject.pinAllInBackground(buildings, sc);
            ParseObject.pinAllInBackground(Event.load(), sc);
            ParseObject.pinAllInBackground(Company.load(), sc);
            ParseObject.pinAllInBackground(Human.load(), sc);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void startMainActivity(){
        Boolean hasData = getSharedPreferences("parseData",MODE_PRIVATE).getBoolean("hasData", false);
        if(hasData){
            Intent mainIntent = new Intent().setClass(
                    SplashScreenActivity.this, MainActivity.class);
            startActivity(mainIntent);
            finish();
        } else{
            Bundle bundle = new Bundle();
            bundle.putString("action", "show dialog");
            Message msg = Message.obtain();
            msg.setData(bundle);
            mhandler.sendMessage(msg);
        }
    }

    private Boolean dataUpdated(){
        try {
            Date parseLastUpdate = Config.getLastUpdated();//ParseCloud.callFunction("getLastUpdated",new HashMap<String, Object>());
            Log.i("FECHA PARSE",parseLastUpdate.toString());
            String lastUpdate = getSharedPreferences("parseData",MODE_PRIVATE).getString("lastUpdate",null);
            if(lastUpdate!=null){
                Date lastUpdateDate = format.parse(lastUpdate);
                Log.i("FECHA SYSTEM",lastUpdateDate.toString());
                if(parseLastUpdate.compareTo(lastUpdateDate) < 0) {
                    Log.i("dataUpdated", String.valueOf(parseLastUpdate.compareTo(lastUpdateDate)));
                    getSharedPreferences("parseData", MODE_PRIVATE).edit().putString("lastUpdate",format.format(new Date())).commit();
                    return true;
                }
            } else{
                Log.i("dataUpdated","null" );
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Log.i("dataUpdated","error parse");
        } catch (java.text.ParseException t){
            t.printStackTrace();
            Log.i("dataUpdated","error text");

        }
        return false;
    }
}