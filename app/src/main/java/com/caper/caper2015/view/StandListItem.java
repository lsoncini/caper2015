package com.caper.caper2015.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.caper.caper2015.R;
import com.caper.caper2015.parse.Booth;
import com.caper.caper2015.parse.Company;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Bind;

public class StandListItem extends FrameLayout {

    @Bind(R.id.stand_name) TextView name;
    @Bind(R.id.principal_text) TextView principalTV;
    @Bind(R.id.secondary_text) TextView secondaryTV;

    public StandListItem(Context context) {
        super(context);
        init();
    }

    public StandListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StandListItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    void init() {
        addView(inflate(getContext(), R.layout.stand_list_item, null));
        ButterKnife.bind(this);
    }

    void setStand(Company stand) {
        Context c = getContext();

        name.setText(stand.getName());

        List<Booth> booths = stand.getBooths();
        if(booths.size()==1){
            Booth booth = booths.get(0);
            principalTV.setText(booth.getCode());
            secondaryTV.setText(c.getSharedPreferences("buildingNames",Context.MODE_PRIVATE).getString(booth.getBuilding().getObjectId(),""));
            /*ParseQuery<Building> query = new ParseQuery<Building>("Building").fromLocalDatastore();
            query.getInBackground(booth.getBuilding().getObjectId(), new GetCallback<Building>() {
                @Override
                public void done(Building building, ParseException e) {
                    if(e==null){
                        secondaryTV.setText(building.getName());
                    }
                    else{
                        e.printStackTrace();
                    }
                }
            });*/
        }
        else{
            principalTV.setText(String.valueOf(booths.size()));
            secondaryTV.setText(getResources().getString(R.string.locations));
        }
    }
}