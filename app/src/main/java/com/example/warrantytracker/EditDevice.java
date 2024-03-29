package com.example.warrantytracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.warrantytracker.database.AppDatabase;
import com.example.warrantytracker.database.Device;

import java.util.Calendar;

public class EditDevice extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;
    private Button dateButton;

    //////////////////////////////////////////////
    // On create, loads add_device.xml layout
    // creates and populates text inputs, creates buttons
    /////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_device);

        intDatePicker();
        dateButton = findViewById(R.id.dateOfPurchaseInput);
        dateButton.setText(getTodaysDate());

        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());
        int position = getIntent().getIntExtra("devicePosition", 0);
        Device device = db.deviceDao().loadDeviceById(position);

        //////////////////////////////////////
        // Pulls fields from add_device.xml
        //
        /////////////////////////////////////
        final EditText deviceNameInput = findViewById(R.id.nameInput);
        final EditText deviceManufacturerInput = findViewById(R.id.manufacturerInput);
        final EditText deviceSerialInput = findViewById(R.id.serialInput);
        final Button deviceDateOfPurchaseInput = findViewById(R.id.dateOfPurchaseInput);
        deviceNameInput.setText(device.deviceName);
        deviceManufacturerInput.setText(device.manufacturer);
        deviceSerialInput.setText(device.deviceSerial);
        deviceDateOfPurchaseInput.setText(device.deviceDateOfPurchase);

        ///////////////////////
        // save button function
        //
        ///////////////////////
        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editDevice(deviceNameInput.getText().toString(), deviceManufacturerInput.getText().toString(),
                        deviceSerialInput.getText().toString(), deviceDateOfPurchaseInput.getText().toString());
            }
        });
        Button linkButton = findViewById(R.id.linkButton);
        linkButton.setVisibility(View.VISIBLE);
        linkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent webViewIntent = new Intent(getApplicationContext(), WebViewActivity.class);
                webViewIntent.putExtra("devicePosition", position);
                // DEPRECATED API, UPDATE TO ACTIVITY RESULT API AT LATER DATE
                int LAUNCH_WEB_VIEW = 1;
                startActivityForResult(webViewIntent, LAUNCH_WEB_VIEW);
            }
        });
    }

    //////////////////////////////////////////////////////////////
    // takes device input pulled above and saves it to the database
    //
    /////////////////////////////////////////////////////////////////
    private void editDevice(String deviceName, String deviceManufacturer, String deviceSerial,
            String deviceDateOfPurchase) {
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());
        int position = getIntent().getIntExtra("devicePosition", 0);
        Device device = db.deviceDao().loadDeviceById(position);
        device.deviceName = deviceName;
        device.manufacturer = deviceManufacturer;
        device.deviceSerial = deviceSerial;
        device.deviceDateOfPurchase = deviceDateOfPurchase;
        db.deviceDao().updateDevice(device);
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void intDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dateButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);

    }

    ////////////////////////////////////
    // Format for the Calendar date shown
    // on the recycler list
    ///////////////////////////////////

    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month) {
        if (month == 1)
            return "JAN";
        if (month == 2)
            return "FEB";
        if (month == 3)
            return "MAR";
        if (month == 4)
            return "APR";
        if (month == 5)
            return "MAY";
        if (month == 6)
            return "JUN";
        if (month == 7)
            return "JUL";
        if (month == 8)
            return "AUG";
        if (month == 9)
            return "SEP";
        if (month == 10)
            return "OCT";
        if (month == 11)
            return "NOV";
        if (month == 12)
            return "DEC";

        // default
        return "JAN";
    }

    public void openDatePicker(View view) {
        datePickerDialog.show();
    }

}