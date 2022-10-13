package com.example.warrantytracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SecondActivity extends AppCompatActivity {

    private static final String TAG = "Warranty Tracker App";
    Button AddButton;

    MyApplication myApplication = (MyApplication) this.getApplication();

    List<dvice> deviceList;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        deviceList = myApplication.getDeviceList();


        Log.d(TAG, "onCreate" + deviceList.toString());
        Toast.makeText(this, "Device count = " + deviceList.size(), Toast.LENGTH_SHORT).show();
        //AddButton = findViewById(R.id.AddButton);


        AddButton = findViewById(R.id.AddButton);

        AddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SecondActivity.this, userAddDevice.class);
                startActivity(intent);

            }
        });

        recyclerView = findViewById(R.id.listDevice);
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter
        mAdapter = new RecyclerViewAdapter(deviceList, SecondActivity.this);
        recyclerView.setAdapter(mAdapter);


    }


}