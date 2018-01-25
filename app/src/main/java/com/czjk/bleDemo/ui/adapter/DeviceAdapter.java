package com.czjk.bleDemo.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.czjk.bleDemo.R;

import java.util.List;

import cn.senssun.ble.sdk.BleDevice;


public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder> implements View.OnClickListener{

    public  interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, BleDevice data);
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }



    private List<BleDevice> datas;
    private Context context;
    public DeviceAdapter(Context context,List<BleDevice> datas) {
        super();
        this.context = context;
        this.datas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.device_list_item, parent, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.tv_name.setText(datas.get(position).getBluetoothDevice().getName());
        holder.tv_mac.setText(datas.get(position).getBluetoothDevice().getAddress());
        int rssi = datas.get(position).getRssi();
        holder.tv_rssi.setText(rssi+"");
        if (rssi>-60){
            holder.img_rssi.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_signal_cellular_4_bar_black_24dp));
        }else if (rssi>-75){
            holder.img_rssi.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_signal_cellular_3_bar_black_24dp));
        }else if (rssi>-90){
            holder.img_rssi.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_signal_cellular_2_bar_black_24dp));
        }else {
            holder.img_rssi.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_signal_cellular_1_bar_black_24dp));
        }
        holder.itemView.setTag(datas.get(position));
    }
    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v,(BleDevice)v.getTag());
        }
    }
    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final ImageView img_rssi;
        public final TextView tv_name;
        public final TextView tv_rssi;
        public final TextView tv_mac;

        public ViewHolder(View view) {
            super(view);
            img_rssi = (ImageView) view.findViewById(R.id.img_rssi);
            tv_rssi = (TextView) view.findViewById(R.id.tv_rssi);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_mac = (TextView) view.findViewById(R.id.tv_mac);
        }
    }
}

