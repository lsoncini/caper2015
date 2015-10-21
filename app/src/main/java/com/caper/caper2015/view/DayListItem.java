package com.caper.caper2015.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.caper.caper2015.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Lucas on 20/10/2015.
 */
public class DayListItem extends FrameLayout {

    @Bind(R.id.day)     TextView dayTV;

    public DayListItem(Context context) {
        super(context);
        init();
    }

    public DayListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DayListItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    void init() {
        addView(inflate(getContext(), R.layout.day_list_item, null));
        ButterKnife.bind(this);

    }

    public void setDay(String day) {
        Context c = getContext();

        dayTV.setText(day);
    }
}