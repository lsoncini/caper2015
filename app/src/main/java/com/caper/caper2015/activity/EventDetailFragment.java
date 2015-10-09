package com.caper.caper2015.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.caper.caper2015.R;
import com.caper.caper2015.parse.Booth;
import com.caper.caper2015.parse.Building;
import com.caper.caper2015.parse.Company;
import com.caper.caper2015.parse.Event;
import com.caper.caper2015.parse.Human;
import com.caper.caper2015.view.ContactList;
import com.caper.caper2015.view.EventListItem;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Bind;

public class EventDetailFragment extends LoadingFragment {

    public String getTitle() {
        return event.getTitle();
    }

    @Bind(R.id.header_layout)       RelativeLayout header;
    @Bind(R.id.principal_text)      TextView startDate;
    @Bind(R.id.secondary_text)      TextView endDate;
    @Bind(R.id.arrow)               ImageView arrow;
    @Bind(R.id.circle)              LinearLayout circle;
    @Bind(R.id.date)                TextView date;
    @Bind(R.id.event_title)         TextView title;
    @Bind(R.id.event_booth)         TextView booth;

    @Bind(R.id.floor_plan)          ImageView map;
    @Bind(R.id.description)         TextView description;
    @Bind(R.id.humansList)          ContactList humansList;
    @Bind(R.id.companiesList)       ContactList companiesList;

    @Bind(R.id.description_layout)  LinearLayout descriptionLayout;
    @Bind(R.id.description_title)   TextView descriptionTitle;
    @Bind(R.id.people_layout)       LinearLayout peopleLayout;
    @Bind(R.id.people_title)        TextView peopleTitle;
    @Bind(R.id.companies_layout)    LinearLayout companiesLayout;
    @Bind(R.id.companies_title)     TextView companiesTitle;

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");
    public Event event;
    int position;
    public Bitmap bitmap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_detail, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateView();
    }

    public EventDetailFragment setEvent(Event event, int position) {
        this.event = event;
        this.position = position;
        updateView();
        return this;
    }

    private void updateView() {
        if (getView() == null) return;
        showSpinner();

        loadFullEvent(event);

        hideSpinner();
    }

    private void loadFullEvent(final Event event) {
        this.event = event;

        companiesTitle.setText(R.string.companies);
        peopleTitle.setText(R.string.people_event);
        descriptionTitle.setText(R.string.description);


        date.setVisibility(View.VISIBLE);
        date.setText(sdf.format(event.getDateStart()));
        header.setBackgroundColor(EventListItem.getCircleColor(position,getResources()));
        header.getBackground().setAlpha(180);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        startDate.setText(sdf.format(event.getDateStart()));
        endDate.setText(sdf.format(event.getDateEnd()));
        title.setText(event.getTitle());
        title.setMaxLines(5);
        title.setTextColor(Color.WHITE);
        booth.setText(event.getBooth().getCode());
        booth.setTextColor(Color.WHITE);
        header.setPadding(20, 40, 20, 40);
        arrow.setVisibility(View.GONE);
        Drawable drawable = getResources().getDrawable(R.drawable.circular_list_bg);
        drawable.setColorFilter(EventListItem.getCircleColor(position,getResources()), PorterDuff.Mode.SRC);
        circle.setBackgroundDrawable(drawable);

        String descriptionStr = event.getDescription();
        if(descriptionStr==null){
            descriptionLayout.setVisibility(View.GONE);
        }else{
            description.setText(descriptionStr);
        }

        List<String> humanNameList= new ArrayList<String>();
        List<String> humanMailList= new ArrayList<String>();
        for(Human h: event.getHumans()){
            humanNameList.add(h.getName());
            humanMailList.add(h.getEmail());
        }
        if(humanNameList.size()==0){
            peopleLayout.setVisibility(View.GONE);
        }else{
            humansList.setContacts(humanNameList,humanMailList);
        }

        List<String> companiesNameList= new ArrayList<String>();
        for(Company c: event.getCompanies())
            companiesNameList.add(c.getName());
        if(companiesNameList.size()==0){
            companiesLayout.setVisibility(View.GONE);
        }else{
            companiesList.setContacts(companiesNameList,null);
        }

        setBuildingData();
        /*if(bitmap==null) {
            File file = new File(getActivity().getFilesDir(), event.getBooth().getBuilding().getObjectId());
            byte[] data = MapDisplayActivity.convertFileToByteArray(file);
            bitmap = BitmapFactory
                    .decodeByteArray(
                            data, 0,
                            data.length);
            Bitmap tempBitmap = bitmap.copy(Bitmap.Config.RGB_565, true);
            Canvas tempCanvas = new Canvas(tempBitmap);
            tempCanvas.drawBitmap(tempBitmap, 0, 0, null);
            Booth b = event.getBooth();
            Paint paint = new Paint();
            paint.setColor(getResources().getColor(R.color.primary_color));
            Paint paintBorder = new Paint();
            paintBorder.setColor(Color.BLACK);
            paintBorder.setStrokeWidth(3);
            paintBorder.setStyle(Paint.Style.STROKE);
            tempCanvas.drawCircle(b.getLocationX(), b.getLocationY(), 20, paint);
            tempCanvas.drawCircle(b.getLocationX(), b.getLocationY(), 20, paintBorder);

            bitmap = tempBitmap;

            setCroppedImage(b.getLocationX(), b.getLocationY(), tempBitmap);
        }*/


        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapDisplayActivity.class);
                ArrayList<String> booths = new ArrayList<String>();
                booths.add(event.getBooth().getObjectId());

                intent.putStringArrayListExtra("list",booths);
                startActivity(intent);
            }
        });

    }

    public void setBuildingData(){
        ParseQuery<Building> query = new ParseQuery<Building>("Building").fromLocalDatastore();

        query.getInBackground(event.getBooth().getBuilding().getObjectId(), new GetCallback<Building>() {
            @Override
            public void done(Building building, ParseException e) {
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
                                    Booth b = event.getBooth();
                                    Paint paint = new Paint();
                                    paint.setColor(getResources().getColor(R.color.primary_color));
                                    Paint paintBorder = new Paint();
                                    paintBorder.setColor(Color.BLACK);
                                    paintBorder.setStrokeWidth(3);
                                    paintBorder.setStyle(Paint.Style.STROKE);
                                    tempCanvas.drawCircle(b.getLocationX(),b.getLocationY(),20, paint);
                                    tempCanvas.drawCircle(b.getLocationX(),b.getLocationY(),20, paintBorder);

                                    bitmap=tempBitmap;

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
        Log.i("map width", String.valueOf(map.getWidth()));
        Log.i("map measured", String.valueOf(map.getMeasuredWidth()));

        Bitmap focusBitmap = Bitmap.createBitmap(bitmap, focusX<0? 0:focusX,focusY<0 ? 0 : focusY, width, height);
        map.setImageBitmap(focusBitmap);
    }
}
