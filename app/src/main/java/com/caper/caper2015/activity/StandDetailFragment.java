package com.caper.caper2015.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.caper.caper2015.R;
import com.caper.caper2015.parse.Booth;
import com.caper.caper2015.parse.Building;
import com.caper.caper2015.parse.Company;
import com.caper.caper2015.parse.Human;
import com.caper.caper2015.view.BoothList;
import com.caper.caper2015.view.BoothListItem;
import com.caper.caper2015.view.ContactList;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;

public class StandDetailFragment extends LoadingFragment implements BoothListItem.BoothListItemListener {

    public String getTitle() {
        return stand==null?"Stand Details":stand.getName();
    }

    @Bind(R.id.stand_name)          TextView name;
    @Bind(R.id.add_fav)             TextView addFav;
    @Bind(R.id.remove_fav)          TextView removeFav;
    @Bind(R.id.floor_plan)          ImageView map;
    @Bind(R.id.phone)               TextView phone;
    @Bind(R.id.web)                 TextView web;
    @Bind(R.id.email)               TextView email;
    @Bind(R.id.humansList)          ContactList humansList;
    @Bind(R.id.boothsList)          BoothList boothsList;
    @Bind(R.id.description)         TextView description;

    @Bind(R.id.location_layout)     LinearLayout locationLayout;
    @Bind(R.id.description_layout)  LinearLayout descriptionLayout;
    @Bind(R.id.description_title)   TextView descriptionTitle;
    @Bind(R.id.people_layout)       LinearLayout peopleLayout;
    @Bind(R.id.people_title)        TextView peopleTitle;
    @Bind(R.id.actions_layout)      LinearLayout actionsLayout;

    public Company stand;
    public Bitmap bitmap;
    public FavoritesListener listener;

    public interface FavoritesListener{
        public void onFavoriteListChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stand_detail, container, false);
        ButterKnife.bind(this, view);

        addFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Set<String> ids = getActivity().getSharedPreferences("favorites", Context.MODE_PRIVATE).getStringSet("ids", new HashSet<String>());
                ids.add(stand.getObjectId());
                getActivity().getSharedPreferences("favorites", Context.MODE_PRIVATE).edit().putStringSet("ids",ids).commit();
                removeFav.setVisibility(View.VISIBLE);
                addFav.setVisibility(View.GONE);
                if(listener!=null)
                    listener.onFavoriteListChanged();
            }
        });
        removeFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Set<String> ids = getActivity().getSharedPreferences("favorites", Context.MODE_PRIVATE).getStringSet("ids", new HashSet<String>());
                ids.remove(stand.getObjectId());

                getActivity().getSharedPreferences("favorites", Context.MODE_PRIVATE).edit().putStringSet("ids",ids).commit();
                addFav.setVisibility(View.VISIBLE);
                removeFav.setVisibility(View.GONE);
                if(listener!=null)
                    listener.onFavoriteListChanged();
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateView();
    }

    @Override
    public void onResume(){
        super.onResume();
        updateView();
    }

    public StandDetailFragment setStand(Company stand) {
        this.stand = stand;
        updateView();
        return this;
    }

    public void setFavoritesListener(FavoritesListener l){
        listener = l;
    }

    private void updateView() {
        if (getView() == null) return;
        showSpinner();

        loadFullStand(stand);

        hideSpinner();
    }

    private void loadFullStand(final Company stand) {
        this.stand = stand;
        name.setText(stand.getName());

        peopleTitle.setText(R.string.people_company);
        descriptionTitle.setText(R.string.description_company);

        Set<String> ids = getActivity().getSharedPreferences("favorites", Context.MODE_PRIVATE).getStringSet("ids", new HashSet<String>());
        if(ids.contains(stand.getObjectId())) {
            addFav.setVisibility(View.GONE);
        }
        else {
            removeFav.setVisibility(View.GONE);
        }

        String descriptionStr = stand.getDescription();
        if(descriptionStr==null){
            descriptionLayout.setVisibility(View.GONE);
        }else{
            description.setText(descriptionStr);
        }

        String phoneStr = stand.getPhone();
        if(phoneStr!=null)
            phone.setText(phoneStr);
        String webStr = stand.getWeb();
        if(webStr!=null) {
            //web.setText(actionsStr);
            web.setTextColor(getResources().getColor(R.color.hyperlink_color));
            SpannableString spanStr = new SpannableString(webStr);
            spanStr.setSpan(new UnderlineSpan(), 0, spanStr.length(), 0);
            web.setText(spanStr);
            web.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("http://" + stand.getWeb()));
                    startActivity(i);
                }
            });
        }
        String mailStr = stand.getEmail();
        if(mailStr!=null)
            email.setText(mailStr);

        if(phoneStr==null && mailStr==null && webStr==null)
            actionsLayout.setVisibility(View.GONE);
        List<String> humanNameList= new ArrayList<String>();
        List<String> humanMailList= new ArrayList<String>();

        for(Human h: stand.getHumans()){
            humanNameList.add(h.getName());
            humanMailList.add(h.getEmail());
        }
        if(humanNameList.size()==0){
           peopleLayout.setVisibility(View.GONE);
        }else{
            humansList.setContacts(humanNameList,humanMailList);
        }

        List<Booth> booths = stand.getBooths();
        if(booths.size()==0){
            locationLayout.setVisibility(View.GONE);
        }else{
            boothsList.setBooths(booths, this);
            //setBuildingData();
            ViewTreeObserver vto = map.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                @Override
                public void onGlobalLayout() {
                    paintMap();
                    ViewTreeObserver obs = map.getViewTreeObserver();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        obs.removeOnGlobalLayoutListener(this);
                    } else {
                        obs.removeGlobalOnLayoutListener(this);
                    }
                }

            });
        }

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapDisplayActivity.class);
                ArrayList<String> booths = new ArrayList<String>();
                for(Booth b : stand.getBooths()){
                    String id = b.getObjectId();
                    if(!booths.contains(id))
                        booths.add(id);
                }
                intent.putStringArrayListExtra("list",booths);
                startActivity(intent);
            }
        });
    }
/*
    public void setBuildingData(){
        ParseQuery<Building> query = new ParseQuery<Building>("Building").fromLocalDatastore();
        query.getInBackground(stand.getBooths().get(0).getBuilding().getObjectId(), new GetCallback<Building>() {
            @Override
            public void done(final Building building, ParseException e) {
                if(e==null){
                    if(bitmap==null){
                        ParseFile file = building.getFloorPlan();
                        file.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                if(e==null){
                                    Log.d("test", "We've got data in data.");
                                    bitmap = BitmapFactory
                                            .decodeByteArray(
                                                    data, 0,
                                                    data.length);

                                    Bitmap tempBitmap = bitmap.copy(Bitmap.Config.RGB_565,true);
                                    Canvas tempCanvas = new Canvas(tempBitmap);
                                    tempCanvas.drawBitmap(tempBitmap, 0, 0, null);
                                    Paint paint = new Paint();
                                    paint.setColor(getResources().getColor(R.color.primary_color));
                                    Paint paintBorder = new Paint();
                                    paintBorder.setColor(Color.BLACK);
                                    paintBorder.setStrokeWidth(3);
                                    paintBorder.setStyle(Paint.Style.STROKE);
                                    for(Booth booth : stand.getBooths()) {
                                        tempCanvas.drawCircle(booth.getLocationX(), booth.getLocationY(), 20, paint);
                                        tempCanvas.drawCircle(booth.getLocationX(), booth.getLocationY(), 20, paintBorder);
                                    }

                                    bitmap=tempBitmap;

                                    Booth b = stand.getBooths().get(0);
                                    setCroppedImage(b.getLocationX(),b.getLocationY(),tempBitmap);

                                }else{
                                    Log.d("test", "There was a problem downloading the data.");
                                }
                            }
                        });
                    }
                }
                else{
                    e.printStackTrace();
                }
            }
        });
    }
*/
    private void setCroppedImage(int xMap, int yMap, Bitmap bitmap){
        int width = map.getWidth();
        int height = map.getHeight();
        int focusX = xMap - width/2;
        if(focusX+width > bitmap.getWidth())
        {
            focusX = bitmap.getWidth()-width;
        }
        int focusY = yMap - height/2;
        if(focusY+height > bitmap.getHeight())
        {
            focusY = bitmap.getHeight()-height;
        }
        Log.i("focusX", String.valueOf(focusX));
        Log.i("focusY", String.valueOf(focusY));
        Log.i("bitmap width", String.valueOf(bitmap.getWidth()));
        Log.i("bitmap height", String.valueOf(bitmap.getHeight()));

        Bitmap focusBitmap = Bitmap.createBitmap(bitmap, focusX<0? 0:focusX,focusY<0 ? 0 : focusY, width, height);
        map.setImageBitmap(focusBitmap);
    }

    @Override
    public void onBoothItemSelected(Booth booth) {
        setCroppedImage(booth.getLocationX(),booth.getLocationY(),bitmap);
    }

    Bitmap getBitmap(Building building){
        Bitmap bitmap;
        File file = new File(getActivity().getFilesDir(),building.getObjectId());
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

    public void paintMap(){
        bitmap = getBitmap(stand.getBooths().get(0).getBuilding());

        Bitmap tempBitmap = bitmap.copy(Bitmap.Config.RGB_565,true);
        Canvas tempCanvas = new Canvas(tempBitmap);
        tempCanvas.drawBitmap(tempBitmap, 0, 0, null);
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.primary_color));
        Paint paintBorder = new Paint();
        paintBorder.setColor(Color.BLACK);
        paintBorder.setStrokeWidth(3);
        paintBorder.setStyle(Paint.Style.STROKE);
        for(Booth booth : stand.getBooths()) {
            tempCanvas.drawCircle(booth.getLocationX(), booth.getLocationY(), 20, paint);
            tempCanvas.drawCircle(booth.getLocationX(), booth.getLocationY(), 20, paintBorder);
        }

        bitmap=tempBitmap;

        Booth b = stand.getBooths().get(0);
        setCroppedImage(b.getLocationX(),b.getLocationY(),tempBitmap);
    }
}