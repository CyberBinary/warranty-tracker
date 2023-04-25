package com.example.warrantytracker;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.example.warrantytracker.database.AppDatabase;
import com.example.warrantytracker.database.Device;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class WebViewActivity extends AppCompatActivity {

    private WebView manufWebView;
    private FloatingActionButton manufBack;
    private FloatingActionButton manufOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());
        int position = getIntent().getIntExtra("devicePosition", 0);
        Device device = db.deviceDao().loadDeviceById(position);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view);

        WebView webView = findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());

        //////////////////////////////
        //   ADD URLS TO LOAD HERE
        ////////////////////////////////
        switch (device.manufacturer.toLowerCase()) {
            case "lg":
                webView.loadUrl("https://www.lg.com/us/support/repair-service/schedule-repair-continued");
                break;
            case "samsung":
                webView.loadUrl("https://www.samsung.com/us/support/warranty/");
                break;
            case "sony":
                webView.loadUrl("https://us.esupport.sony.com/support/s/service?language=en_US");
                break;
            case "dell":
                webView.loadUrl("https://www.dell.com/support/home/en-us?app=warranty");
                break;
            case "asus":
                webView.loadUrl("https://www.asus.com/us/support/warranty-status-inquiry/");
                break;
        }
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            //////////////////////////////////////////////////////
            // After page finished loading, runs javascript injection
            // YOU NEED TO UPDATE WHEN ADDING DEVICE PROPERTIES
            /////////////////////////////////////////////////////
            public void onPageFinished(WebView view, String url) {
                ////////////////////////////////
                //     ADD WEBSITE JAVASCRIPT FUNCTIONALITY HERE
                ///////////////////////////////////////////////
                switch (device.manufacturer.toLowerCase()) {
                    case "lg":
                        webView.loadUrl("javascript:(function() { document.getElementById('search-keyword').value = '" + device.deviceName + "'; ;})()");
                        webView.loadUrl("javascript:(function() { document.getElementById('serialNumber').value = '" + device.deviceSerial + "'; ;})()");
                        webView.loadUrl("javascript:(function() { document.getElementById('purchasedDate').value = '" + device.deviceDateOfPurchase + "'; ;})()");
                        System.out.println("AUTOFILL");
                        break;
                }
            }
        });

        manufBack = findViewById(R.id.backButton);
        /* manufOpen = findViewById(R.id.openBrowser); */

        // code to go back
        manufBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /*
        // code to open in browser
        FloatingActionButton openBrowser = findViewById(R.id.openBrowser);
        manufOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = manufWebView.getUrl();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                intent.setPackage("com.android.chrome");
                startActivity(intent);
            }
        }); */

        final Activity activity = this;
        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == MotionEvent.ACTION_UP && webView.canGoBack()) {
                    webView.goBack();
                    return true;
                }
                return false;
            }
        });
    }
}
