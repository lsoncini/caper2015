package com.caper.caper2015.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.webkit.WebView;

import com.caper.caper2015.R;

/**
 * Created by Lucas on 18/10/2015.
 */
public class AboutActivity extends ActionBarActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_about);

        WebView myWebView = (WebView) this.findViewById(R.id.webView);

        myWebView.loadUrl("file:///android_asset/legal.html");
        myWebView.getSettings().setDefaultTextEncodingName("utf-8");

    }

}
