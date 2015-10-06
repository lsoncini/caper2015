package com.caper.caper2015.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.caper.caper2015.R;
import com.caper.caper2015.parse.Booth;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Lucas on 19/09/2015.
 */
public class BoothList extends FrameLayout {

    @Bind(R.id.list)
    LinearLayout list;
    Boolean hasBooths=false;

    public BoothList(Context context) {
        super(context);
        init();
    }

    public BoothList(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BoothList(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        addView(inflate(getContext(), R.layout.booth_list, null));
        ButterKnife.bind(this);

        list.setFocusable(false);
    }

    public void setBooths(List<Booth> booths, BoothListItem.BoothListItemListener l) {
        if(hasBooths)
            return;
        for(Booth b : booths){
            BoothListItem item = new BoothListItem(getContext());
            item.setBooth(b);
            list.addView(item);
            item.setBoothListItemListener(l);
        }
        hasBooths=true;
    }
}