package com.lab3.michau.phones;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;

public class WebActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_activity);
        String url = getIntent().getStringExtra("url");
        WebView viewById = findViewById(R.id.webwiev);
        viewById.loadUrl(url);
    }
}
