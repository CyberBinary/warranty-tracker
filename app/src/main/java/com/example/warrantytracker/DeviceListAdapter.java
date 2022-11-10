package com.example.warrantytracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.warrantytracker.database.Device;

import java.util.List;

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.MyViewHolder>{
    private final RecyclerViewInterface recyclerViewInterface;

    private Context context;
    private List<Device> deviceList;
    public DeviceListAdapter(Context context, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    public void setDeviceList(List<Device> deviceList) {
        this.deviceList = deviceList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DeviceListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_view_row, parent, false);

        return new MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceListAdapter.MyViewHolder holder, int position) {
        holder.deviceName.setText(this.deviceList.get(position).deviceName);
        holder.deviceManufacturer.setText(this.deviceList.get(position).manufacturer);
    }

    @Override
    public int getItemCount() {
        return this.deviceList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView deviceName;
        TextView deviceManufacturer;

        public MyViewHolder(@NonNull View view, RecyclerViewInterface recyclerViewInterface) {
            super(view);
            deviceName = view.findViewById(R.id.deviceName);
            deviceManufacturer = view.findViewById(R.id.deviceManufacturer);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   if (recyclerViewInterface != null) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onItemClick(pos);
                        }
                   }
                }
            });
        }
    }
}
