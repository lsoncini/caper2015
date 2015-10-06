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
import com.caper.caper2015.parse.Company;


import java.util.List;

import butterknife.ButterKnife;
import butterknife.Bind;

public class StandList extends FrameLayout{
    public interface StandListListener {
        void onStandSelected(Company stand);
    }

    @Bind(R.id.list)
    ListView list;

    StandAdapter adapter;
    StandListListener listener;


    public StandList(Context context) {
        super(context);
        init();
    }

    public StandList(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StandList(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        addView(inflate(getContext(), R.layout.stand_list, null));
        ButterKnife.bind(this);

        list.setFocusable(false);

        list.setAdapter(adapter = new StandAdapter(getContext()));
        list.setOnItemClickListener(onListItemClick);
    }

    public void clear() {
        adapter.clear();
    }

    public void addStands(List<Company> stands) {
        adapter.addAll(stands);
    }

    public void setStands(List<Company> stands) {
        adapter.clear();
        adapter.addAll(stands);
    }

    public void setListener(StandListListener listener) {
        this.listener = listener;
    }


    private final AdapterView.OnItemClickListener onListItemClick = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (listener != null)
                listener.onStandSelected(adapter.getItem(position));
        }
    };


    public static class StandAdapter extends ArrayAdapter<Company> {

        public StandAdapter(Context context) {
            super(context, 0);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            StandListItem view = (convertView != null) ?
                    (StandListItem) convertView :
                    new StandListItem(getContext())
                    ;

            view.setStand(getItem(position));

            return view;
        }
    }
}
