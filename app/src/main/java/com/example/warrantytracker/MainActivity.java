package com.example.warrantytracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Warranty Tracker App";
    Button AddButton;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    MyApplication myApplication = (MyApplication) this.getApplication();

    List<dvice> deviceList;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;











    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        deviceList = myApplication.getDeviceList();


        Log.d(TAG, "onCreate" + deviceList.toString());
        Toast.makeText(this, "Device count = " + deviceList.size(), Toast.LENGTH_SHORT).show();
        //AddButton = findViewById(R.id.AddButton);


        AddButton = findViewById(R.id.AddButton);

        AddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, userAddDevice.class);
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
        mAdapter = new RecyclerViewAdapter(deviceList, MainActivity.this);
        recyclerView.setAdapter(mAdapter);




        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home: {
                        Toast.makeText(MainActivity.this, "Home Selected", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.contact: {
                        Toast.makeText(MainActivity.this, "Contact Us Selected", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.gallery: {
                        Toast.makeText(MainActivity.this, "Gallery Selected", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.about: {
                        Toast.makeText(MainActivity.this, "About Selected", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.login: {
                        Toast.makeText(MainActivity.this, "Login Selected", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.display: {
                        Toast.makeText(MainActivity.this, "Display Selected", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.notifications: {
                        Toast.makeText(MainActivity.this, "Notifications Selected", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                return false;
            }
        });



    }
















    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }

    }
}