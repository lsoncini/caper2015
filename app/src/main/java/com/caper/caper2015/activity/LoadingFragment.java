package com.caper.caper2015.activity;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.caper.caper2015.R;

public abstract class LoadingFragment extends Fragment {

    public abstract String getTitle();

    private View loadingView = null;

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getTitle());
    }

    public void showSpinner(){

        if(loadingView == null){
            LayoutInflater inflater = getActivity().getLayoutInflater();
            loadingView = inflater.inflate(R.layout.loading_indicator,null);
            getActivity().getWindow().addContentView(loadingView, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
        } else {
            loadingView.setVisibility(View.VISIBLE);
        }

    }

    public void hideSpinner(){
            if(loadingView != null)
                loadingView.setVisibility(View.GONE);
    }

}
