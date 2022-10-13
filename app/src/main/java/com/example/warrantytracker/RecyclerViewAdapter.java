package com.example.warrantytracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    List<dvice> deviceList;
    Context  context;

    public RecyclerViewAdapter(List<dvice> deviceList, Context context) {
        this.deviceList = deviceList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_line_device,parent,false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tv_devName.setText(deviceList.get(position).getName());
        holder.tv_devDate.setText(String.valueOf(deviceList.get(position).getDateOfWarranty()));
        Glide.with(this.context).load(deviceList.get(position).getImageURL()).into(holder.iv_devPic);



    }

    @Override
    public int getItemCount() {

        return deviceList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
    ImageView iv_devPic;
    TextView tv_devName;
    TextView tv_devDate;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_devPic = itemView.findViewById(R.id.iv_devicePicture);
            tv_devName = itemView.findViewById(R.id.tv_deviceName);
            tv_devDate = itemView.findViewById(R.id.tv_deviceDate);
        }
    }
}


