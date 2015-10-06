package com.caper.caper2015.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.caper.caper2015.parse.Event;
import com.caper.caper2015.R;


import java.util.List;

import butterknife.ButterKnife;
import butterknife.Bind;

public class EventList extends FrameLayout{
    public interface EventListListener {
        void onEventSelected(Event event, int position);
    }

    @Bind(R.id.list)
    ListView list;

    EventAdapter adapter;
    EventListListener listener;


    public EventList(Context context) {
        super(context);
        init();
    }

    public EventList(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EventList(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        addView(inflate(getContext(), R.layout.event_list, null));
        ButterKnife.bind(this);

        list.setFocusable(false);

        list.setAdapter(adapter = new EventAdapter(getContext()));
        list.setOnItemClickListener(onListItemClick);
    }

    public void clear() {
        adapter.clear();
    }

    public void addEvents(List<Event> events) {
        adapter.addAll(events);
    }

    public void setEvents(List<Event> events) {
        adapter.clear();
        adapter.addAll(events);
    }

    public void setListener(EventListListener listener) {
        this.listener = listener;
    }


    private final AdapterView.OnItemClickListener onListItemClick = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (listener != null)
                listener.onEventSelected(adapter.getItem(position), position);
        }
    };


    public static class EventAdapter extends ArrayAdapter<Event> {

        public EventAdapter(Context context) {
            super(context, 0);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            EventListItem view = (convertView != null) ?
                    (EventListItem) convertView :
                    new EventListItem(getContext())
                    ;

            view.setEvent(getItem(position), position);

            return view;
        }
    }
}
