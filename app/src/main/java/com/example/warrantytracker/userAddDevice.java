package com.example.warrantytracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

public class userAddDevice extends AppCompatActivity {

    Button btn_ok, btn_cancel;
    List<dvice> deviceList;
    EditText et_devDate, et_devName, et_devImageURL;

    MyApplication myApplication = (MyApplication) this.getApplication();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_add_device);

        deviceList = myApplication.getDeviceList();

        btn_ok = findViewById(R.id.button_ok);
        btn_cancel = findViewById(R.id.button2_cancel);
        et_devDate = findViewById(R.id.et_date);
        et_devName = findViewById(R.id.et_name);
        et_devImageURL = findViewById(R.id.et_image);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //create device object
                int nextId = myApplication.getNextId();
                dvice newDevice = new dvice(nextId, et_devName.getText().toString(), Integer.parseInt(et_devDate.getText().toString()), et_devImageURL.getText().toString());
                //add the object to the global list of devices

                deviceList.add(newDevice);
                myApplication.setNextId(nextId++);

                //go back to the main activity

                Intent intent = new Intent( userAddDevice.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( userAddDevice.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}