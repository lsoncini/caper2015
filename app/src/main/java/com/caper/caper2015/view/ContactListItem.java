package com.caper.caper2015.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.caper.caper2015.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Lucas on 19/09/2015.
 */
public class ContactListItem extends FrameLayout {

    @Bind(R.id.contact_name)    TextView name;
    @Bind(R.id.contact_email)   TextView email;
    @Bind(R.id.initial_letters) TextView initials;

    public ContactListItem(Context context) {
        super(context);
        init();
    }

    public ContactListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ContactListItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    void init() {
        addView(inflate(getContext(), R.layout.contact_list_item, null));
        ButterKnife.bind(this);
    }

    void setContact(String contact, String info) {
        name.setText(contact);
        if(info == null)
            email.setVisibility(GONE);
        else
            email.setText(info);
        initials.setText(getInitials(contact));
    }

    private String getInitials(String name){
        String ans = "";
        String[] words = name.split(" ");
        for(String s : words){
            ans = ans+s.charAt(0);
            if(ans.length()==4)
                break;
        }
        return ans.toUpperCase();
    }
}