package com.caper.caper2015.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.caper.caper2015.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Lucas on 20/10/2015.
 */
public class DayList extends FrameLayout{
    public interface DayListListener {
        void onDaySelected(String day);
    }

    @Bind(R.id.list) ListView list;

    DayAdapter adapter;
    DayListListener listener;


    public DayList(Context context) {
        super(context);
        init();
    }

    public DayList(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DayList(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        addView(inflate(getContext(), R.layout.day_list, null));
        ButterKnife.bind(this);

        list.setFocusable(false);

        list.setAdapter(adapter = new DayAdapter(getContext()));
        list.setOnItemClickListener(onListItemClick);
    }

    public void clear() {
        adapter.clear();
    }

    public void addDays(List<String> days) {
        adapter.addAll(days);
    }

    public void setDays(List<String> days) {
        adapter.clear();
        adapter.addAll(days);
    }

    public void setListener(DayListListener listener) {
        this.listener = listener;
    }


    private final AdapterView.OnItemClickListener onListItemClick = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (listener != null)
                listener.onDaySelected(adapter.getItem(position));
        }
    };


    public static class DayAdapter extends ArrayAdapter<String> {

        public DayAdapter(Context context) {
            super(context, 0);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            DayListItem view = (convertView != null) ?
                    (DayListItem) convertView :
                    new DayListItem(getContext())
                    ;

            view.setDay(getItem(position));

            return view;
        }
    }
}
