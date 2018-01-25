package com.czjk.bleDemo.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.czjk.bleDemo.R;
import com.czjk.bleDemo.base.BaseFragment;
import com.czjk.bleDemo.ui.adapter.DeviceAdapter;
import com.czjk.blelib.utils.Constant;
import com.czjk.blelib.utils.SPUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.senssun.ble.sdk.BleDevice;
import cn.senssun.ble.sdk.BleScan;
import senssun.ycblelib.YCProtocolUtils;


public class DeviceFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, DeviceAdapter.OnRecyclerViewItemClickListener {

    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    private static int TIME_OUT_SCAN = 10000;
    private List<BleDevice> mDeviceList = new ArrayList<>();
    private List<String> mDevices = new ArrayList<>();
    private DeviceAdapter adapter;
    BleScan bleScan=new BleScan();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View home = inflater.inflate(R.layout.fragment_device, null);
        ButterKnife.bind(this,home);
        return home;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupRecyclerView(rv);
        bleScan.Create(getActivity());
        bleScan.setOnScanListening(scanListening);
        bleScan.ScanLeStartDevice(TIME_OUT_SCAN);
//        protocolUtils.scanDevices(periodScanCallback);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        adapter = new DeviceAdapter(getContext(), mDeviceList);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.RED);
        swipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setDistanceToTriggerSync(100);
        swipeRefreshLayout.setProgressViewEndTarget(false, 100);
    }

//    PeriodScanCallback periodScanCallback = new PeriodScanCallback() {
//        @Override
//        public void scanTimeout() {
//
//        }
//        @Override
//        public void onFind(BleDevice device) {
//            if (!mDevices.contains(device.getAddress())) {
//                mDevices.add(device.getAddress());
//                mDeviceList.add(device);
//                adapter.notifyDataSetChanged();
//            }
//        }
//    };

    BleScan.OnScanListening scanListening=new BleScan.OnScanListening() {
        @Override
        public void OnListening(cn.senssun.ble.sdk.BleDevice bleDevice) {
            if (!mDevices.contains(bleDevice.getBluetoothDevice().getAddress())) {
                mDevices.add(bleDevice.getBluetoothDevice().getAddress());
                mDeviceList.add(bleDevice);
                adapter.notifyDataSetChanged();
            }

        }
    };
    //
    @Override
    public void onRefresh() {
        bleScan.scanLeStopDevice();
//        protocolUtils.stopScan();
        mDevices.clear();
        mDeviceList.clear();
        bleScan.setOnScanListening(scanListening);
        bleScan.ScanLeStartDevice();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
                bleScan.scanLeStopDevice();
            }
        }, TIME_OUT_SCAN);
    }
    //
    @Override
    public void onItemClick(View view, BleDevice data) {
//        protocolUtils.stopScan();
//        SPUtil.setStringValue(Constant.SP_KEY_DEVICE_ADDRESS, data.getAddress());
//        EventBus.getDefault().post(new BaseEvent(BaseEvent.EventType.RECONNECTION));
        SPUtil.setStringValue(Constant.SP_KEY_DEVICE_ADDRESS, data.getBluetoothDevice().getAddress());
        bleScan.scanLeStopDevice();
        YCProtocolUtils.getInstance().connect(data.getBluetoothDevice().getAddress());
    }
}
