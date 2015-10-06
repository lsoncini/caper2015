package com.caper.caper2015.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.caper.caper2015.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Lucas on 19/09/2015.
 */
public class ContactList extends FrameLayout {

    @Bind(R.id.list)
    LinearLayout list;

    Boolean hasContacts=false;
    //ContactAdapter adapter;

    public ContactList(Context context) {
        super(context);
        init();
    }

    public ContactList(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ContactList(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        addView(inflate(getContext(), R.layout.contact_list, null));
        ButterKnife.bind(this);

        list.setFocusable(false);

        //list.setAdapter(adapter = new ContactAdapter(getContext()));
    }

    /*public void clear() {
        adapter.clear();
    }

    public void addContacts(List<String> contacts) {
        adapter.addAll(contacts);
    }*/

    public void setContacts(List<String> contacts, List<String> info) {
        //adapter.clear();
        //adapter.addAll(contacts);
        if(hasContacts)
            return;
        if(info != null) {
            for (int i = 0; i < contacts.size(); i++) {
                ContactListItem item = new ContactListItem(getContext());
                item.setContact(contacts.get(i), info.get(i));
                list.addView(item);
            }
        }else{
            for (int i = 0; i < contacts.size(); i++) {
                ContactListItem item = new ContactListItem(getContext());
                item.setContact(contacts.get(i), null);
                list.addView(item);
            }
        }
        hasContacts = true;
    }
/*
    public static class ContactAdapter extends ArrayAdapter<String> {

        public ContactAdapter(Context context) {
            super(context, 0);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ContactListItem view = (convertView != null) ?
                    (ContactListItem) convertView :
                    new ContactListItem(getContext())
                    ;

            view.setContact(getItem(position));

            return view;
        }
    }*/
}