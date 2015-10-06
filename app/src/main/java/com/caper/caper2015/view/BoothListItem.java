package com.caper.caper2015.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.caper.caper2015.R;
import com.caper.caper2015.parse.Booth;

import java.text.SimpleDateFormat;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Lucas on 19/09/2015.
 */
public class BoothListItem extends FrameLayout {

    @Bind(R.id.booth_code)      TextView boothCode;
    @Bind(R.id.building_name)   TextView buildingName;

    BoothListItemListener listener;
    Booth booth;

    public interface BoothListItemListener {
        void onBoothItemSelected(Booth booth);
    }
    public BoothListItem(Context context) {
        super(context);
        init();
    }

    public BoothListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BoothListItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    void init() {
        addView(inflate(getContext(), R.layout.booth_list_item, null));
        ButterKnife.bind(this);
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null)
                    listener.onBoothItemSelected(booth);
            }
        });
    }

    void setBooth(Booth booth) {
        this.booth = booth;
        boothCode.setText(booth.getCode());
        buildingName.setText(getContext().getSharedPreferences("buildingNames",Context.MODE_PRIVATE).getString(booth.getBuilding().getObjectId(),""));
    }

    public void setBoothListItemListener(BoothListItemListener l){
        listener = l;
    }
}
