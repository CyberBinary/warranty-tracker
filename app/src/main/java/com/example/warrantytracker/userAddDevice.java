package com.example.warrantytracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class userAddDevice extends AppCompatActivity {

    Button btn_ok, btn_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_add_device);

        btn_ok = findViewById(R.id.button_ok);
        btn_cancel = findViewById(R.id.button2_cancel);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( userAddDevice.this, SecondActivity.class);
                startActivity(intent);
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( userAddDevice.this, SecondActivity.class);
                startActivity(intent);
            }
        });

    }
}