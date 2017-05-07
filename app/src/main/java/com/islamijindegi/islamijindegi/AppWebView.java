package com.islamijindegi.islamijindegi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

public class AppWebView extends AppCompatActivity {

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_web_view);

        webView=(WebView) findViewById(R.id.wView);

        //webView.loadUrl("file:///android_res/raw/amalus_sunnah.PDF");
        webView.loadUrl("http://www.google.com");
    }
}
