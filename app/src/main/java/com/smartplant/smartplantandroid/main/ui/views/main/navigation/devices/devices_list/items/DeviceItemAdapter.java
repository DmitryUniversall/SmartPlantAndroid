package com.smartplant.smartplantandroid.main.ui.views.main.navigation.devices.devices_list.items;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smartplant.smartplantandroid.main.components.auth.models.User;

import java.util.ArrayList;
import java.util.List;


public class DeviceItemAdapter extends RecyclerView.Adapter<DeviceItemAdapter.DeviceViewHolder> {
    private final Context _context;
    private List<User> _deviceList;

    public static class DeviceViewHolder extends RecyclerView.ViewHolder {
        private final DeviceCardItem deviceCardItem;

        public DeviceViewHolder(@NonNull DeviceCardItem itemView) {
            super(itemView);
            this.deviceCardItem = itemView;
        }
    }

    public DeviceItemAdapter(Context context) {
        this._context = context;
        this._deviceList = new ArrayList<>();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setDeviceList(List<User> deviceList) {
        this._deviceList = deviceList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DeviceCardItem deviceCardItem = new DeviceCardItem(this._context);
        return new DeviceViewHolder(deviceCardItem);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder holder, int position) {
        User device = this._deviceList.get(position);
        holder.deviceCardItem.bind(device);
    }

    @Override
    public int getItemCount() {
        return this._deviceList.size();
    }
}
