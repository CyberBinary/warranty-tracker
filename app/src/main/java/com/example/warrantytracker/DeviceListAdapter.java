package com.example.warrantytracker;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.warrantytracker.database.AppDatabase;
import com.example.warrantytracker.database.Device;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
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
        //holder.dateOfPurchaseInput.setText(this.deviceList.get(position).deviceDateOfPurchase);
        if (this.deviceList.get(position).deviceImage != null && holder.deviceImage != null) {
            holder.deviceImage.setImageURI(Uri.parse(this.deviceList.get(position).deviceImage));
        }
        holder.deviceTimeRemaining.setText(daysRemaining(this.deviceList.get(position)));
    }

    private String daysRemaining(Device device){
        Calendar calendar2 = Calendar.getInstance();

        int years = device.warrantyYears;
        calendar2.add(Calendar.YEAR, years);
        int months = device.warrantyMonths;
        calendar2.add(Calendar.MONTH, months);
        int daysBetween = 365;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            daysBetween = (int) ChronoUnit.DAYS.between(Calendar.getInstance().getTime().toInstant(), calendar2.toInstant());

        }
        return (daysBetween + " days remaining");
    }
    private String makeDateString(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date selectedDate = calendar.getTime();
        String strDate = sdf.format(selectedDate);
        return strDate;
    }

    // transform the string above that you get turn it into a calendar object ^^
    private Calendar makeStringDate(String str_date) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = (Date)sdf.parse(str_date);
        calendar.setTime(date);
        return calendar;
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
        ImageView deviceImage;
        TextView deviceName;
        TextView deviceManufacturer;
        TextView dateOfPurchaseInput;

        TextView deviceTimeRemaining;

        public MyViewHolder(@NonNull View view, RecyclerViewInterface recyclerViewInterface) {
            super(view);
            deviceName = view.findViewById(R.id.deviceName);
            deviceManufacturer = view.findViewById(R.id.deviceManufacturer);
            deviceImage = view.findViewById(R.id.deviceImage);
            deviceTimeRemaining = view.findViewById(R.id.deviceTimeRemaining);

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
