package com.example.warrantytracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.warrantytracker.database.AppDatabase;
import com.example.warrantytracker.database.Device;

import java.util.List;

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.MyViewHolder>{
    private final RecyclerViewInterface recyclerViewInterface;
    // i think the context is used to create a deviceListAdapter state??
    private Context context;
    private List<Device> deviceList;
    public DeviceListAdapter(Context context, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    ////////////////////////////////////
    // sets the adapter's deviceList to the imported deviceList
    ////////////////////////////////////////////
    public void setDeviceList(List<Device> deviceList) {
        this.deviceList = deviceList;
        notifyDataSetChanged();
    }
    //////////////////////////////////////////////
    // updates the db by getting the db, getting all the devices,
    // setting that list as the deviceList
    // notifying that the dataset has changed
    ////////////////////////////////////////////
    public void update() {
        AppDatabase db = AppDatabase.getDbInstance(context);
        List<Device> deviceList = db.deviceDao().getAllDevices();
        this.deviceList = deviceList;
        notifyDataSetChanged();
    }

    ///////////////////////////////////
    // creates the recyclerview appearance from recycler_view_row.xml
    // returns the MyViewHolder
    //////////////////////////////////////////////////////////

    @NonNull
    @Override
    public DeviceListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_view_row, parent, false);

        return new MyViewHolder(view, recyclerViewInterface);
    }

    /////////////////////////////////////////////////////////////////
    // sets the deviceName on the card to the correct device ID's devicename
    // "     " manufacturer "                                              "
    @Override
    public void onBindViewHolder(@NonNull DeviceListAdapter.MyViewHolder holder, int position) {
        holder.deviceName.setText(this.deviceList.get(position).deviceName);
        holder.deviceManufacturer.setText(this.deviceList.get(position).manufacturer);
    }

    ///////////////////////////////
    // gets deviceList's size
    ///////////////////////////////
    @Override
    public int getItemCount() {
        return this.deviceList.size();
    }

    /////////////////////////////////////////////////////////////
    // populates the devices I think
    // places an OnClickListener on the cards which gets the device's position
    // this is used in calling editDevice
    ///////////////////////////////////////////////////////////////////
    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView deviceName;
        TextView deviceManufacturer;

        public MyViewHolder(@NonNull View view, RecyclerViewInterface recyclerViewInterface) {
            super(view);
            deviceName = view.findViewById(R.id.deviceName);
            deviceManufacturer = view.findViewById(R.id.deviceManufacturer);

            AppDatabase db = AppDatabase.getDbInstance(context);
            List<Device> deviceList = db.deviceDao().getAllDevices();


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   if (recyclerViewInterface != null) {
                       int pos = getAdapterPosition();
                        int deviceID = deviceList.get(pos).deviceID;
                        if (pos != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onItemClick(deviceID);
                        }
                   }
                }
            });



        }
    }
}
