package com.caper.caper2015.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TabHost;

import com.caper.caper2015.R;
import com.caper.caper2015.adapter.AnimatedTabHostListener;
import com.caper.caper2015.parse.Booth;
import com.caper.caper2015.parse.Building;
import com.caper.caper2015.view.MapView;
import com.parse.ParseException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MapDisplayActivity extends Activity {

    List<Building> buildingsList;
    List<Booth> boothList;
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_maps);

        final TabHost tabs=(TabHost)findViewById(R.id.tabhost);

        tabs.setup();
        List<String> ids = getIntent().getExtras().getStringArrayList("list");
        Log.i("HOLDOASLDALSDLAS", String.valueOf(ids));
        try {
            if(ids!=null) {
                boothList = Booth.createQuery().whereContainedIn("objectId", ids).find();
                List<String> buildingIDs = new ArrayList<String>();
                for(Booth b : boothList)
                    buildingIDs.add(b.getBuilding().getObjectId());
                buildingsList = Building.createQuery().addAscendingOrder("name").whereContainedIn("objectId", buildingIDs).find();
            }else
                buildingsList = Building.createQuery().addAscendingOrder("name").find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(buildingsList!=null) {
            for (Building b : buildingsList) {
                TabHost.TabSpec spec = tabs.newTabSpec(b.getObjectId());
                spec.setContent(new TabHost.TabContentFactory() {
                    public View createTabContent(String tag) {
                        MapView mapView = new MapView(getApplicationContext());
                        try {
                            Building building = Building.createQuery().get(tag);
                            Bitmap bitmap = getBitmap(building);
                            if(boothList!=null){
                                Bitmap tempBitmap = bitmap.copy(Bitmap.Config.RGB_565,true);
                                Canvas tempCanvas = new Canvas(tempBitmap);
                                tempCanvas.drawBitmap(tempBitmap, 0, 0, null);
                                Paint paint = new Paint();
                                paint.setColor(getResources().getColor(R.color.primary_color));
                                Paint paintBorder = new Paint();
                                paintBorder.setColor(Color.BLACK);
                                paintBorder.setStrokeWidth(3);
                                paintBorder.setStyle(Paint.Style.STROKE);
                                for(Booth booth : boothList) {
                                    tempCanvas.drawCircle(booth.getLocationX(), booth.getLocationY(), 20, paint);
                                    tempCanvas.drawCircle(booth.getLocationX(), booth.getLocationY(), 20, paintBorder);
                                }
                                mapView.setImageBitmap(tempBitmap);
                            }else
                                mapView.setImageBitmap(bitmap);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        return mapView;
                    }
                });
                spec.setIndicator(b.getName());
                tabs.addTab(spec);
            }
        }

        tabs.setOnTabChangedListener(new AnimatedTabHostListener(getApplicationContext(),tabs));

    }

    Bitmap getBitmap(Building building){
        Bitmap bitmap;
        File file = new File(getFilesDir(),building.getObjectId());
        byte[] data = convertFileToByteArray(file);
        bitmap = BitmapFactory
                .decodeByteArray(
                        data,0,
                        data.length);
        return bitmap;
    }

    public static byte[] convertFileToByteArray(File f)
    {
        byte[] byteArray = null;
        try
        {
            InputStream inputStream = new FileInputStream(f);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024*8];
            int bytesRead = 0;

            while ((bytesRead = inputStream.read(b)) != -1)
            {
                bos.write(b, 0, bytesRead);
            }

            byteArray = bos.toByteArray();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return byteArray;
    }
}