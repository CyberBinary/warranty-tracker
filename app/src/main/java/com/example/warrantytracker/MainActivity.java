package com.example.warrantytracker;

/* import static com.example.warrantytracker.R.id.sortBy; */

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Spinner;

import com.example.warrantytracker.database.AppDatabase;
import com.example.warrantytracker.database.Device;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerViewInterface {
    private DeviceListAdapter deviceListAdapter;
    ConstraintLayout constraintLayout;

    /////////////////////////////////////////////
    // On create loads activity_main.xml layout
    // creates button to add device on click
    // initializes recyclerview and loads the device list
    /////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        constraintLayout=findViewById(R.id.constraint_layout);

        ///////////////////////////////////////////////////////
        // Setup notification channel
        ///////////////////////////////////////////////////////

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channel1", "notification", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        ///////////////////////////////////////////////////////
        // Button function to add device
        // Button will take you to addDevice page
        ///////////////////////////////////////////////////////

        FloatingActionButton addDevice=findViewById(R.id.addDevice);
        addDevice.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                System.out.println("Button Clicked");
                // DEPRECATED API, UPDATE TO ACTIVITY RESULT API IN FUTURE
                Intent addDeviceIntent=new Intent(getApplicationContext(),AddDevice.class);
                startActivityForResult(addDeviceIntent,1);
                deviceListAdapter.update();
                deviceListAdapter.notifyDataSetChanged();
            }
        });

        // sort by name
        Button sortName = findViewById(R.id.sortName);
        sortName.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                System.out.println("Button Clicked");
                sortDeviceList(0);
            }
        });
        // sort by manufacturer
        Button sortManufacturer = findViewById(R.id.sortManufacturer);
        sortManufacturer.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                System.out.println("Button Clicked");
                sortDeviceList(1);
            }
        });

        /* // sort dropdown button
        Button sortBy = findViewById(R.id.sortBy);
        sortBy.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(MainActivity.this, sortBy);
                popup.getMenuInflater().inflate(R.menu.menu_main, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.sortName:
                                System.out.println("Button Clicked");
                                sortDeviceList(0);
                                return true;
                            case R.id.sortManufacturer:
                                System.out.println("Button Clicked");
                                sortDeviceList(1);
                        }
                        return false;
                    }
                });
                popup.show();
            }
        })); */

        initRecyclerView();

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                // this method is called
                // when the item is moved.
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                deleteDevice(position);
            }
            // at last we are adding this
            // to our recycler view.
        }).attachToRecyclerView(findViewById(R.id.recyclerView));
        initRecyclerView();

        loadDeviceList();

    }

    /////////////////////////////
    // Loads the recyclerview
    // sets the layoutManager to recyclerview
    // sets dividers
    // sets the deviceListAdapter, then sets the recyclerview to the adapter
    /////////////////////////////////
    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        deviceListAdapter = new DeviceListAdapter(this, this);
        recyclerView.setAdapter(deviceListAdapter);
    }

    //////////////////////////////////////////////////
    // Loads the db deviceList into the deviceListAdapter
    /////////////////////////////////////////////////
    private void loadDeviceList() {
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());
        List<Device> deviceList = db.deviceDao().getAllDevices();
        deviceListAdapter.setDeviceList(deviceList);
    }

    ////////////////////////////////////
    // reloads the recyclerview
    //////////////////////////////////
    private void reloadRecyclerView() {
        deviceListAdapter.update();
        deviceListAdapter.notifyDataSetChanged();
    }

    //////////////////////////////////////////
    // reloads the deviceList when a result is retrieved
    // result obtained from adding or editing a device AND saving it
    //////////////////////////////////////////

    private void sortDeviceList(int setting) {
        AppDatabase db = AppDatabase.getDbInstance((this.getApplicationContext()));
        List<Device> deviceList;
        switch (setting) {
            case 0: //SORT BY NAME
                deviceList = db.deviceDao().sortDevicesByName();
                deviceListAdapter.setDeviceList(deviceList);
                break;
            case 1:
                deviceList = db.deviceDao().sortDevicesByManufacturer();
                deviceListAdapter.setDeviceList(deviceList);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 100) {
            loadDeviceList();
            deviceListAdapter.update();
        }

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                deviceListAdapter.update();
            }
        }
        deviceListAdapter.update();
    }

    ////////////////////////////////////
    // launches the edit device page on clicking a device
    // gets the device's position and passes it to the editDevice page
    // updates the deviceListAdapter once completed
    ////////////////////////////////////////

    @Override
    public void onItemClick(int position) {
        Intent editDeviceIntent = new Intent(getApplicationContext(), EditDevice.class);
        editDeviceIntent.putExtra("devicePosition", position);
        // DEPRECATED API, UPDATE TO ACTIVITY RESULT API AT LATER DATE
        int LAUNCH_EDIT_DEVICE = 1;
        startActivityForResult(editDeviceIntent, LAUNCH_EDIT_DEVICE);
        deviceListAdapter.update();
        deviceListAdapter.notifyDataSetChanged();
    }

    @Override
    public int onDeviceSwipe(int position) {
        return position;
    }
    private void deleteDevice(int position) {
        // this method is called when we swipe our item to right direction.
        // on below line we are getting the item at a particular position.

        // below line is to get the position
        // of the item at that position.
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());
        // this method is called when item is swiped.
        // below line is to remove item from our array list.
        db.deviceDao().delete(db.deviceDao().getAllDevices().get(position));
        // below line is to notify our item is removed from adapter.
        deviceListAdapter.update();
        deviceListAdapter.notifyDataSetChanged();


    }
}