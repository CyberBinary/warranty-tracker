package com.example.warrantytracker;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.warrantytracker.database.AppDatabase;
import com.example.warrantytracker.database.Device;

public class AddDevice extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_device);

        //Pulls fields from add_device.xml
        final EditText deviceNameInput = findViewById(R.id.nameInput);
        final EditText deviceManufacturerInput = findViewById(R.id.manufacturerInput);
        final EditText deviceSerialInput = findViewById(R.id.serialInput);
        final EditText deviceDateOfPurchaseInput = findViewById(R.id.dateOfPurchaseInput);
        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNewDevice(deviceNameInput.getText().toString(), deviceManufacturerInput.getText().toString(), deviceSerialInput.getText().toString(), deviceDateOfPurchaseInput.getText().toString());
            }
        });
    }

    //takes device input pulled above and saves it to the database
    private void saveNewDevice(String deviceName, String deviceManufacturer, String deviceSerial, String deviceDateOfPurchase){
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());

        Device device = new Device();
        device.deviceName = deviceName;
        device.manufacturer = deviceManufacturer;
        device.deviceSerial = deviceSerial;
        device.deviceDateOfPurchase = deviceDateOfPurchase;
        db.deviceDao().insertDevice(device);

        finish();
    }
}