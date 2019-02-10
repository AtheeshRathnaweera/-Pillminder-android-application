package com.atheeshproperty.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class ShowGoogleSearchResults extends AppCompatActivity {

    private WebView web;
    private ProgressBar progress;
    String medname;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_web_view);

        Intent intent = getIntent();
        medname = intent.getExtras().getString("MedName");

        String url = "https://www.google.com/search?q="+medname;

        Log.d("status","This is the med name: "+medname);

        progress = findViewById(R.id.progressBar);
        web = findViewById(R.id.webView);
        web.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progress.setVisibility(View.VISIBLE);
                setTitle("Loading......");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progress.setVisibility(View.GONE);
                setTitle(view.getTitle());
            }
        });

        web.loadUrl(url);

        WebSettings settings = web.getSettings();
        settings.setJavaScriptEnabled(true);

    }

    @Override
    public void onBackPressed() {
        if(web.canGoBack()){
            web.goBack();
        }else{
            super.onBackPressed();
        }

    }
}
