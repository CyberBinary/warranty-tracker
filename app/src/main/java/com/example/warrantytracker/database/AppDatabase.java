package com.example.warrantytracker.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import com.example.warrantytracker.database.Device;

/////////////////////////////////////////////////
// Structure that holds the devices and the methods to interface with them
//////////////////////////////////////////////////

@Database(entities = {Device.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase{

    public abstract DeviceDao deviceDao();

    private static AppDatabase INSTANCE;

    public static AppDatabase getDbInstance(Context context) {
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "DB_NAME")
                    .allowMainThreadQueries()
                    .build();

        }
        return INSTANCE;
    }
}