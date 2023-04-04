package com.example.warrantytracker.database;

import android.net.Uri;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/////////////////////////////////////////////////////
// Device properties are CREATED here
// ID, Name, manufacturer, serial number, and date of purchace
/////////////////////////////////////////////////////

@Entity
public class Device {

    @PrimaryKey(autoGenerate = true)
    public int deviceID;

    @ColumnInfo(name = "device_name")
    public String deviceName;

    @ColumnInfo(name = "manufacturer")
    public String manufacturer;

    @ColumnInfo(name = "serial_number")
    public String deviceSerial;

    @ColumnInfo(name = "date_of_purchase")
    public String deviceDateOfPurchase;

    @ColumnInfo(name = "device_image")
    public String deviceImage;

    @ColumnInfo(name = "warranty_months")
    public int warrantyMonths;

    @ColumnInfo(name = "warranty_years")
    public int warrantyYears;

}