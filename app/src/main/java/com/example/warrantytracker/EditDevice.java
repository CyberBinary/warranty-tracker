package com.example.warrantytracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.warrantytracker.database.AppDatabase;
import com.example.warrantytracker.database.Device;

public class EditDevice extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_device);

        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());
        int position = getIntent().getIntExtra("devicePosition", 0);
        Device device = db.deviceDao().loadDeviceById(position);

        //Pulls fields from add_device.xml
        final EditText deviceNameInput = findViewById(R.id.nameInput);
        final EditText deviceManufacturerInput = findViewById(R.id.manufacturerInput);
        final EditText deviceSerialInput = findViewById(R.id.serialInput);
        final EditText deviceDateOfPurchaseInput = findViewById(R.id.dateOfPurchaseInput);
        deviceNameInput.setText(device.deviceName);
        deviceManufacturerInput.setText(device.manufacturer);
        deviceSerialInput.setText(device.deviceSerial);
        deviceDateOfPurchaseInput.setText(device.deviceDateOfPurchase);

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editDevice(deviceNameInput.getText().toString(), deviceManufacturerInput.getText().toString(), deviceSerialInput.getText().toString(), deviceDateOfPurchaseInput.getText().toString());
            }
        });
    }

    //takes device input pulled above and saves it to the database
    private void editDevice(String deviceName, String deviceManufacturer, String deviceSerial, String deviceDateOfPurchase){
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());
        int position = getIntent().getIntExtra("devicePosition", 0);
        Device device = db.deviceDao().loadDeviceById(position);
        device.deviceName = deviceName;
        device.manufacturer = deviceManufacturer;
        device.deviceSerial = deviceSerial;
        device.deviceDateOfPurchase = deviceDateOfPurchase;
        db.deviceDao().updateDevice(device);

        finish();
    }
}
