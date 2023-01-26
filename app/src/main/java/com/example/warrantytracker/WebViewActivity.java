package com.example.warrantytracker;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.example.warrantytracker.database.AppDatabase;
import com.example.warrantytracker.database.Device;

public class WebViewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());
        int position = getIntent().getIntExtra("devicePosition", 0);
        Device device = db.deviceDao().loadDeviceById(position);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view);

        WebView webView = findViewById(R.id.webView);
        //////////////////////////////
        //   ADD URLS TO LOAD HERE
        ////////////////////////////////
        switch (device.manufacturer.toLowerCase()) {
            case "lg":
                webView.loadUrl("https://www.lg.com/us/support/repair-service/schedule-repair-continued");
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
    }
}
