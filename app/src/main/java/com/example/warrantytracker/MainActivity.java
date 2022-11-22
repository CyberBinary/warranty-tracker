package com.example.warrantytracker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

        Button addDevice = findViewById(R.id.addDevice);
        addDevice.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                System.out.println("Button Clicked");

                Intent addDeviceIntent = new Intent(getApplicationContext(), AddDevice.class);
                startActivity(addDeviceIntent);
                deviceListAdapter.update();
                deviceListAdapter.notifyDataSetChanged();
            }
        });


        initRecyclerView();

        loadDeviceList();
    }

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
        deviceListAdapter.update();
    }

    @Override
    public void onItemClick(int position) {
        //
        //ADD FUNCTIONALITY HERE
        //
        Intent editDeviceIntent = new Intent(getApplicationContext(), EditDevice.class);
        editDeviceIntent.putExtra("devicePosition", position);
        startActivity(editDeviceIntent);

        //reloadRecyclerView();

        deviceListAdapter.update();
        deviceListAdapter.notifyDataSetChanged();

    }
}