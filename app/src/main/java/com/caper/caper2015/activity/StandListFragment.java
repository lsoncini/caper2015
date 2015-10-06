package com.caper.caper2015.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import butterknife.ButterKnife;

import com.caper.caper2015.R;

import butterknife.Bind;

import com.caper.caper2015.parse.Company;
import com.caper.caper2015.view.StandList;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

public class StandListFragment extends LoadingFragment {

    @Bind(R.id.standList)   StandList standList;
    @Bind(R.id.empty_view)  RelativeLayout emptyView;

    List<Company> companies = null;
    List<String> ids;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stand_list, container, false);
        ButterKnife.bind(this, view);
        ids = getArguments().getStringArrayList("ids");
        try {
            companies = new ArrayList<Company>();
            if(ids == null) {
                for (Company c : Company.createQuery().include("booths")
                        .include("humans")
                        .orderByAscending("name")
                        .whereNotEqualTo("booths", null)
                        .find()) {
                    if (c.getBooths().size() != 0)
                        companies.add(c);
                }
            } else {
                for (Company c : Company.createQuery().include("booths")
                        .include("humans")
                        .orderByAscending("name")
                        .whereContainedIn("objectId",ids)
                        .whereNotEqualTo("booths", null)
                        .find()) {
                    if (c.getBooths().size() != 0)
                        companies.add(c);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        standList.setListener((StandList.StandListListener) getActivity());
        updateView();
        return view;
    }

    public void updateView() {
        if (getView() == null) return;

        showSpinner();
        standList.setStands(companies);
        if(ids!=null && ids.size()==0)
            emptyView.setVisibility(View.VISIBLE);
        else emptyView.setVisibility(View.GONE);
        hideSpinner();

    }

    @Override
    public String getTitle() {
        return getResources().getString(R.string.stands_title);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateView();
    }
}