package com.example.warrantytracker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.warrantytracker.database.AppDatabase;
import com.example.warrantytracker.database.Device;

import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerViewInterface {
    private DeviceListAdapter deviceListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ///////////////////////////////////////////////////////
        // Button function to add device
        // Button will take you to addDevice page
        ///////////////////////////////////////////////////////

        Button addDevice = findViewById(R.id.addDevice);
        addDevice.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                System.out.println("Button Clicked");
                //DEPRECATED API, UPDATE TO ACTIVITY RESULT API IN FUTURE
                Intent addDeviceIntent = new Intent(getApplicationContext(), AddDevice.class);
                startActivityForResult(addDeviceIntent, 1);
                deviceListAdapter.update();
                deviceListAdapter.notifyDataSetChanged();
            }
        });


        initRecyclerView();

        loadDeviceList();
    }

    //////////////////////////////////////////////////
    // Recycler view format.
    // Device list items are displayed in a recycler view
    /////////////////////////////////////////////////

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        deviceListAdapter = new DeviceListAdapter(this, this);
        recyclerView.setAdapter(deviceListAdapter);
    }

    private void loadDeviceList() {
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());
        List<Device> deviceList = db.deviceDao().getAllDevices();
        deviceListAdapter.setDeviceList(deviceList);
    }

    private void reloadRecyclerView() {
        deviceListAdapter.update();
        deviceListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 100) {
            loadDeviceList();
            deviceListAdapter.update();
        }

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                deviceListAdapter.update();
            }
        }
        deviceListAdapter.update();
    }


    /////////////////////////////////////////////
    // click on text view boxes to edit
    // the list
    ////////////////////////////////////////////
    @Override
    public void onItemClick(int position) {
        Intent editDeviceIntent = new Intent(getApplicationContext(), EditDevice.class);
        editDeviceIntent.putExtra("devicePosition", position);
        //DEPRECATED API, UPDATE TO ACTIVITY RESULT API AT LATER DATE
        int LAUNCH_EDIT_DEVICE = 1;
        startActivityForResult(editDeviceIntent, LAUNCH_EDIT_DEVICE);
        deviceListAdapter.update();
        deviceListAdapter.notifyDataSetChanged();

    }
}