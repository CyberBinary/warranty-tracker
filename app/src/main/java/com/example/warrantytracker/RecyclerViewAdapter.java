package com.example.warrantytracker;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter {

    List<dvice> deviceList;
    Context  context;

    public RecyclerViewAdapter(List<dvice> deviceList, Context context) {
        this.deviceList = deviceList;
        this.context = context;
    }
}


