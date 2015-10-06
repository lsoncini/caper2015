package com.caper.caper2015.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.caper.caper2015.R;
import com.caper.caper2015.parse.Event;


import java.text.SimpleDateFormat;

import butterknife.ButterKnife;
import butterknife.Bind;

public class EventListItem extends FrameLayout {

    public static final int COLOR_SIZE = 8;

    @Bind(R.id.event_title)     TextView title;
    @Bind(R.id.event_booth)     TextView booth;
    @Bind(R.id.principal_text)  TextView bigTV;
    @Bind(R.id.secondary_text)  TextView smallTV;
    @Bind(R.id.circle)          LinearLayout circle;

    public EventListItem(Context context) {
        super(context);
        init();
    }

    public EventListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EventListItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    void init() {
        addView(inflate(getContext(), R.layout.event_list_item, null));
        ButterKnife.bind(this);

    }

    public void setEvent(Event event, int position) {

        title.setText(event.getTitle());
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        bigTV.setText(sdf.format(event.getDateStart()));
        smallTV.setText(sdf.format(event.getDateEnd()));

        booth.setText(event.getBooth().getCode());
        Drawable drawable = getResources().getDrawable(R.drawable.circular_list_bg);
        drawable.setColorFilter(getCircleColor(position,getResources()), PorterDuff.Mode.SRC);
        circle.setBackgroundDrawable(drawable);
    }

    public static int getCircleColor(int position, Resources res){
        switch (position%COLOR_SIZE){
            case 0: return res.getColor(R.color.color_pallete_red);
            case 1: return res.getColor(R.color.color_pallete_orange);
            case 2: return res.getColor(R.color.primary_color);
            case 3: return res.getColor(R.color.color_pallete_green);
            case 4: return res.getColor(R.color.color_pallete_cyan);
            case 5: return res.getColor(R.color.color_pallete_blue);
            case 6: return res.getColor(R.color.color_pallete_purple);
            case 7: return res.getColor(R.color.color_pallete_pink);
            default:return res.getColor(R.color.secondary_color);
        }
    }
}