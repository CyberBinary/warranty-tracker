package com.example.warrantytracker.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Device {

    @PrimaryKey(autoGenerate = true)
    public int deviceID;

    @ColumnInfo(name = "device_name")
    public String deviceName;

    @ColumnInfo(name = "manufacturer")
    public String manufacturer;

}