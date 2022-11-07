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
    private Context context;
    private List<Device> deviceList;
    private OnNoteListener OnNoteListener;

    public DeviceListAdapter(Context context, OnNoteListener onNoteListener) {
        this.context = context;
        this.OnNoteListener = onNoteListener;
    }

    public DeviceListAdapter(MainActivity context) {

    }

    public void setDeviceList(List<Device> deviceList) {
        this.deviceList = deviceList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DeviceListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_view_row, parent, false);

        return new MyViewHolder(view, OnNoteListener);
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

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView deviceName;
        TextView deviceManufacturer;

        OnNoteListener onNoteListener;

        public MyViewHolder(View view, OnNoteListener onNoteListener) {
            super(view);
            deviceName = view.findViewById(R.id.deviceName);
            deviceManufacturer = view.findViewById(R.id.deviceManufacturer);
            this.onNoteListener = onNoteListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onNoteListener.onNoteClick(getAdapterPosition());

        }
    }
    public interface OnNoteListener{
        void onNoteClick(int position);
    }
}
