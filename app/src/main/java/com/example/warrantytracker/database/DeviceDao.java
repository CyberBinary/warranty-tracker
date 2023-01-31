package com.example.warrantytracker.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import com.example.warrantytracker.database.Device;

///////////////////////////////////////
// Methods by which device data entries are accessed
// get all devices, load device by id, insert, update, delete (though functionality
// needs to be added on MainActivity)
//////////////////////////////////////////

@Dao
public interface DeviceDao {
    @Query("SELECT * FROM device")
    List<Device> getAllDevices();


    @Query("Select * FROM device WHERE deviceID = :id")
    Device loadDeviceById(int id);

    @Insert
    void insertDevice(Device... devices);

    @Update
    void updateDevice(Device device);

    @Delete
    void delete(Device device);
}