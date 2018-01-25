package senssun.ycblelib;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import com.czjk.blelib.model.State;
import com.czjk.blelib.utils.BleLog;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import static com.czjk.blelib.model.State.CONNECT_PROCESS;
import static com.czjk.blelib.model.State.CONNECT_SUCCESS;
import static com.czjk.blelib.model.State.DISCONNECT;
import static senssun.ycblelib.YCProtocolUtils.Crc16Calc;
import static senssun.ycblelib.YCProtocolUtils.concat;


public class YCViseBluetooth {
    private final static String TAG = YCViseBluetooth.class.getSimpleName();

    public static final UUID SERVICE_UUID = UUID.fromString("00000af0-0000-1000-8000-00805f9b34fb");
    public static final UUID CHARACTERISTIC_COMMANDWRITE_UUID = UUID.fromString("00000af6-0000-1000-8000-00805f9b34fb");
    public static final UUID CHARACTERISTIC_COMMANDNOTIFY_UUID = UUID.fromString("00000af7-0000-1000-8000-00805f9b34fb");
    public static final UUID CHARACTERISTIC_RESERVEDWRITE_UUID = UUID.fromString("00000af1-0000-1000-8000-00805f9b34fb");
    public static final UUID CHARACTERISTIC_MESSDATANOTIFY_UUID = UUID.fromString("00000af2-0000-1000-8000-00805f9b34fb");
//    private int mConnectionState = STATE_DISCONNECTED;

//    public static final int STATE_DISCONNECTED = 0;
//    public static final int STATE_CONNECTING = 1;
//    public static final int STATE_CONNECTED = 2;


    //    public static final int DEFAULT_SCAN_TIME = 20000;
//    public static final int DEFAULT_CONN_TIME = 10000;
//    private static final int MSG_CONNECT_TIMEOUT = 6;
    public Context context;
    private BluetoothManager bluetoothManager;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothGatt bluetoothGatt;
    //    private IConnectCallback connectCallback;
    private State state = DISCONNECT;
    //    private int scanTimeout = DEFAULT_SCAN_TIME;
//    private int connectTimeout = DEFAULT_CONN_TIME;
    private static YCViseBluetooth viseBluetooth;

    private boolean broastStop=false;
    //    private ArrayList<String> stringBuffer = new ArrayList();
    private StringBuffer stringBufferCommand = new StringBuffer();
    private StringBuffer stringBufferMess = new StringBuffer();
    public static String logPath = null;//log日志存放路径
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.US);//日期格式;
    private static Date date = new Date();
    public static String fileName;
    public static boolean isWrite=false;

//    ArrayList<Byte> bytes=new ArrayList<>();

    public static YCViseBluetooth getInstance() {
        if (viseBluetooth == null) {
            synchronized (YCViseBluetooth.class) {
                if (viseBluetooth == null) {
                    viseBluetooth = new YCViseBluetooth();
                }
            }
        }
        return viseBluetooth;
    }


    private List<String> mSendDataList = new ArrayList<String>();

    public List<String> getmSendDataList() {
        return mSendDataList;
    }

    public static Timer TimerOne;

    // 停止计时
    public void loadClose() {
        if (TimerOne != null) {
            TimerOne.cancel();
            TimerOne = null;
        }
    }

    // 开始计时
    public void loadStart() {
        loadClose();
        TimerOne = new Timer();

        TimerOne.schedule(new TimerTask() {
            @Override
            public void run() {
//                BleLog.i("时钟在走");
                Timehandler.sendEmptyMessage(0);
            }
        }, 0, 800);
    }


    String SendWrite;
    int SendTimes=3;
    final Handler Timehandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            BleLog.i("时钟消息响应");
            if (mSendDataList.size() > 0) {
                if(SendWrite==null||!SendWrite.equals(mSendDataList.get(0))){
                    SendWrite=mSendDataList.get(0);
                    SendTimes=3;

                }else if(SendWrite.equals(mSendDataList.get(0))){
                   String[]pushMessage =mSendDataList.get(0).split("-");
                   if (pushMessage[0].equals("04")&&pushMessage[1].equals("04")){
                       mSendDataList.remove(0);
                       SendWrite=null;
                       Timehandler.sendEmptyMessage(0);
                       return ;
                   }
                    if(SendTimes<=0){
                        mSendDataList.remove(0);
                        SendWrite=null;
                        Timehandler.sendEmptyMessage(0);
                        return ;
                    }
                }

//                if (mSendDataList.size() > 0) {
                sendData(mSendDataList.get(0));
                SendTimes--;
//                }
//                mSendDataList.remove(0);
            }
        }
    };

    private boolean sendData(String outBuffer) {
        if(outBuffer==null)return false;
        String[] outBuf = outBuffer.split("-");
        byte[] out = new byte[outBuf.length];
        for(int i = 0;i<outBuf.length;i++)out[i] = (byte) Integer.parseInt(outBuf[i], 16);
        if(bluetoothGatt==null)return false;
        BluetoothGattService service = bluetoothGatt.getService(SERVICE_UUID);
        if (service != null) {
            BluetoothGattCharacteristic characteristic = service.getCharacteristic(CHARACTERISTIC_COMMANDWRITE_UUID);
            if (characteristic != null) {
                characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
                boolean res = characteristic.setValue(out);
                boolean res1 = viseBluetooth.getBluetoothGatt().writeCharacteristic(characteristic);
                return res & res1;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private BluetoothGattCallback coreGattCallback = new BluetoothGattCallback() {

        @Override
        public void onConnectionStateChange(final BluetoothGatt gatt, final int status, final int newState) {
            Log.i(TAG, "status: " + status  +" newState: "+ newState );

            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    stringBufferCommand=new StringBuffer();
                    stringBufferMess=new StringBuffer();
                    mSendDataList.clear();
                    loadStart();
                    state = CONNECT_SUCCESS;
                    Log.i(TAG, "Connected to GATT server.");
                    Log.i(TAG, "Attempting to start service discovery:" + gatt.discoverServices());

                    mSendBroastData();
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    stringBufferCommand=new StringBuffer();
                    stringBufferMess=new StringBuffer();
                    mSendDataList.clear();
                    loadClose();

                    state = DISCONNECT;
                    clear(); // 防止出现status 133
                    Log.i(TAG, "Disconnected from GATT server.");
                    mSendDataList.clear();
                }
            } else {
                Log.d(TAG, "onConnectionStateChange received: " + status);
                state = DISCONNECT;
                clear(); // 防止出现status 133


                mSendDataList.clear();
            }
            Bundle bundle=new Bundle();
            bundle.putSerializable("State",state);
            Message msg=new Message();
            msg.setData(bundle);
            mConnHandler.sendMessage(msg);
        }
//            BleLog.i("onConnectionStateChange  status: " + status + " ,newState: " + newState +
//                    "  ,thread: " + Thread.currentThread().getId());
//            if (newState == BluetoothGatt.STATE_CONNECTED) {
//                mSendDataList.clear();
//                loadStart();
//
//                mSendBroastData();
//                broastStop=false;
//
//                gatt.discoverServices();
//            } else if (newState == BluetoothGatt.STATE_DISCONNECTED) {
//                broastStop=true;
//
//                mSendDataList.clear();
//                loadClose();
//                state = State.DISCONNECT;
//                if (handler != null) {
//                    handler.removeMessages(MSG_CONNECT_TIMEOUT);
//                }
//                if (connectCallback != null) {
//                    close();
//                    runOnMainThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (status == 0) {
//                                connectCallback.onDisconnect();
//                            } else {
//                                connectCallback.onConnectFailure(new ConnectException(gatt, status));
//                            }
//                        }
//                    });
//                }
//
//            } else if (newState == BluetoothGatt.STATE_CONNECTING) {
//                state = State.CONNECT_PROCESS;
//            }
//        }

        @Override
        public void onServicesDiscovered(final BluetoothGatt gatt, final int status) {

            if(status != 0||gatt==null||gatt.getService(SERVICE_UUID)==null)disconnect();
            bluetoothGatt = gatt;
            final BluetoothGattCharacteristic Characteristic = gatt.getService(SERVICE_UUID).getCharacteristic(CHARACTERISTIC_COMMANDNOTIFY_UUID);
            final BluetoothGattCharacteristic massDataCharacteristic = gatt.getService(SERVICE_UUID).getCharacteristic(CHARACTERISTIC_MESSDATANOTIFY_UUID);


            if (Characteristic != null&&massDataCharacteristic!=null) {//

                new Thread(){
                    @Override
                    public void run() {
                        int CharacteristicTimes=20;
                        int massDataCharacteristicTimes=20;
                        boolean Success=false;
                        while(!Success&&CharacteristicTimes>0) {
                            Success = viseBluetooth.setCharacteristicNotification(Characteristic, true);
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            CharacteristicTimes--;
                        }


                        boolean massDataSuccess=false;
                        while(!massDataSuccess&&massDataCharacteristicTimes>0){
                            massDataSuccess = viseBluetooth.setCharacteristicNotification(massDataCharacteristic, true);
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            massDataCharacteristicTimes--;
                        }
                        if ( Success&&massDataSuccess){
                        }else{
                            disconnect();
                        }

                    }
                }.start();
            }else{
                disconnect();
            }

//            if (status == BluetoothGatt.GATT_SUCCESS&&gatt!=null&&displayGattServices(mBluetoothGatt.getServices())) {
//            } else {
//                disconnect();
//            }

//            BleLog.i("onServicesDiscovered  status: " + status);
//            if (handler != null) {
//                handler.removeMessages(MSG_CONNECT_TIMEOUT);
//            }
//            if (status == 0) {
//                bluetoothGatt = gatt;
//                state = State.CONNECT_SUCCESS;
//                BleLog.e(state.toString());
//                if (connectCallback != null) {
//                    runOnMainThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            connectCallback.onConnectSuccess(gatt, status);
//                        }
//                    });
//                }
//            } else {
//                state = State.CONNECT_FAILURE;
//                BleLog.e(state.toString());
//                if (connectCallback != null) {
//                    close();
//                    runOnMainThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            connectCallback.onConnectFailure(new ConnectException(gatt, status));
//                        }
//                    });
//                }
//            }
        }


        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic, final int status) {
//            byte[] data=characteristic.getValue();
//            StringBuffer sb=new StringBuffer(data.length);
//            for(byte byteChar : data){
//                String ms=String.format("%02X ", byteChar).trim();
//                sb.append(ms);
//            }
            BleLog.i("onCharacteristicWrite  status: " + status + ", data:" + Arrays.toString(characteristic.getValue()));//+" \n data:"+ sb.toString());

//            final byte[] data = characteristic.getValue();
//            if (data != null ) {
//                for(byte byteChar : data){
//                    String ms=String.format("%02X ", byteChar).trim();
//                    stringBuffer.add(ms);
//                }
//            }

        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, final int rssi, final int status) {
            BleLog.e("onReadRemoteRssi  status: " + status + ", rssi:" + rssi);
            EventBus.getDefault().post(rssi);
        }

        int i=0;

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
            BleLog.i("onCharacteristicChanged + i:"+i +" value:"+ Arrays.toString(characteristic.getValue()));
            i++;

            final byte[] data = characteristic.getValue();
            if (data != null ) {
                for(byte byteChar : data){

                    if(characteristic.getUuid().equals(CHARACTERISTIC_COMMANDNOTIFY_UUID)){
                        if(stringBufferCommand.length()>0){
                            stringBufferCommand.append(String.format("-%02X", byteChar).trim());
                        }else{
                            stringBufferCommand.append(String.format("%02X", byteChar).trim());
                        }

                    }else if(characteristic.getUuid().equals(CHARACTERISTIC_MESSDATANOTIFY_UUID)){
                        if(stringBufferMess.length()>0){
                            stringBufferMess.append(String.format("-%02X", byteChar).trim());
                        }else{
                            stringBufferMess.append(String.format("%02X", byteChar).trim());
                        }
                    }else{
                        return;
                    }
//                    bytes.add(byteChar);
                }

            }
        }
    };

    /***************************************自定义发送连接代码***/
    Handler mMessHandler = new Handler() {
        public void handleMessage(Message msg) {
            Bundle bundle=msg.getData();
//            String[] mess=bundle.getStringArray("Mess");
//            String[] CommandMess=bundle.getStringArray("CommandMess");

            for(OnServiceDisplayDATA onServiceDisplayDATA:mOnServiceDisplayDATAList){
                onServiceDisplayDATA.OnDATA(bundle);
            }
        }
    };

    Handler mConnHandler = new Handler() {
        public void handleMessage(Message msg) {
            Bundle bundle=msg.getData();
            State state= (State) bundle.getSerializable("State");
            switch (state) {
                case CONNECT_SUCCESS:
                    if(mOnServiceDisplayStatus!=null){
                        mOnServiceDisplayStatus.OnStatus("result-status-connect");
                    }
                    break;
                case DISCONNECT:
                    if(mOnServiceDisplayStatus!=null){
                        mOnServiceDisplayStatus.OnStatus("result-status-disconnect");
                    }
                    break;
            }
        }
    };

    private void mSendBroastData(){
        if(!ta.isAlive()){
            ta.start();
        }
    }

    int datalength=2;
    int typelength=2;
    int crclength=2;
    int guidelength= datalength+typelength+crclength;//2位类型 2位长度 2CRC16

    public  ThreadA ta = new ThreadA();
    class ThreadA extends Thread {
        @Override
        public void run() {
            while(!broastStop){
                try{
                    {//stringBufferCommand
                        if(stringBufferCommand.length()>0){
                            if(stringBufferCommand.substring(0,1).equals("-")){stringBufferCommand.delete(0,1);}

                            String[] datas = stringBufferCommand.toString().split("-");
                            if (datas.length >= 6) {//2位引导 2位长度 2CRC16
                                Log.i(TAG, "处理后的数据Command：" + arrToString(datas));


                                if (Integer.parseInt(datas[0], 16) <= 5
                                        && Integer.parseInt(datas[1], 16) <= 23) {
                                    String lengthStr = datas[3] + datas[2];
                                    int length = Integer.valueOf(lengthStr, 16);
                                    if (datas.length >= length + guidelength) { //2个引导 2个长度 2个CRC
                                        byte[] cmd = new byte[length + typelength + datalength];
                                        for (int i = 0; i < length + typelength + datalength; i++) {
                                            cmd[i] = (byte) Integer.parseInt(datas[i], 16);
                                        }

                                        int verify = Crc16Calc(cmd, cmd.length);

                                        String verStr = datas[length + guidelength - 1] + datas[length + guidelength - 2];
                                        int ver = Integer.parseInt(verStr, 16);

                                        if (verify == ver) {

                                            String[] strTmp = new String[length + guidelength];
                                            System.arraycopy(datas, 0, strTmp, 0, length + guidelength);

                                            stringBufferCommand.delete(0, (strTmp.length-1) * 3+2);

                                            //判断删除命令
                                            List<String> delbuffer = new ArrayList<String>();
                                            for (String buffer : mSendDataList) {
                                                String[] out = buffer.split("-");
                                                if (out[0].equals(strTmp[0]) &&
                                                        out[1].equals(strTmp[1])) {
                                                    delbuffer.add(buffer);
                                                }
                                            }
                                            for (String buffer : delbuffer) {
                                                mSendDataList.remove(buffer);
                                            }

                                            //发出
                                            Log.i(TAG, "发出的数据" + arrToString(strTmp));
                                            Message msg = new Message();
                                            Bundle b = new Bundle();
                                            b.putStringArray("Command", strTmp);
                                            msg.setData(b);
                                            mMessHandler.sendMessage(msg);

//                                mOnServiceDisplayDATA.OnDATA(strTmp);
                                            strTmp = (String[]) concat(new String[]{"Command"},strTmp);

                                            EventBus.getDefault().post(b);
                                        } else {
                                                stringBufferCommand=new StringBuffer();
                                        }
                                    }
                                } else {
                                    stringBufferCommand=new StringBuffer();
                                }
                            }
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    stringBufferCommand=new StringBuffer();
                }
                {//stringBufferMess
                    try{
                        if(stringBufferMess.length()>0){
                            if(stringBufferMess.substring(0,1).equals("-")){stringBufferMess.delete(0,1);}
                            String[] datas = stringBufferMess.toString().split("-");

                            if (datas.length >= 6) {//2位引导 2位长度 2CRC16
                                Log.i(TAG, "处理后的数据Mess:" + arrToString(datas));

                                if (isWrite){ //此处把相应的指令得到的数字解析后,存入对应的文件.
                                    int xx1=0,yy1=0,zz1=0;
                                    if (datas[0].equals("02")&&datas[1].equals("11")){  //保存PPG
                                        if (datas.length>10){
                                            for (int j = 0; j <(datas.length-7)/4 ; j++){
                                                xx1 = Integer.valueOf(datas[7 + 4*j] + datas[6 + 4*j]+datas[5 + 4*j] + datas[4 + 4*j], 16);
                                                Log.i(TAG, "onCreate: xx1我们能看到的PPG数据是?" + xx1);
                                            }
                                            char VERBOSE = 'v';
                                            writeToFile(VERBOSE, "PPG数据是:", String.valueOf(xx1));
                                        }
                                    }

                                    if (datas[0].equals("02")&&datas[1].equals("19")){
                                        if (datas.length>10){
                                            for (int j = 0; j <(datas.length-9)/6 ; j++){
                                                xx1 = Integer.valueOf(datas[5 + 6*j] + datas[4 + 6*j], 16);
                                                yy1 = Integer.valueOf(datas[7 + 6*j] + datas[6 + 6*j], 16);
                                                zz1 = Integer.valueOf(datas[9 + 6*j] + datas[8 + 6*j], 16);
                                                Log.i(TAG, "onCreate: xx1我们能看到的三轴数据是?" + xx1);
                                                Log.i(TAG, "onCreate: yy1我们能看到的三轴数据是?" + yy1);
                                                Log.i(TAG, "onCreate: zz1我们能看到的三轴数据是?" + zz1);
                                            }
                                            char VERBOSE = 'v';
                                            writeToFile(VERBOSE, "加速度是:",xx1+","+yy1+","+zz1 );
                                            writeToFile(VERBOSE, "加速度是:",Arrays.toString(datas) );
                                        }
                                    }

                                    if (datas[0].equals("02")&&datas[1].equals("20")){
                                        if (datas.length>10){
                                            for (int j = 0; j <(datas.length-7)/4 ; j++){
                                                xx1 = Integer.valueOf(datas[7 + 4*j] + datas[6 + 4*j]+datas[5 + 4*j] + datas[4 + 4*j], 16);
                                                Log.i(TAG, "onCreate: xx1我们能看到的ECG数据是?" + xx1);
                                            }
                                            char VERBOSE = 'v';
                                            writeToFile(VERBOSE, "ECG是:", String.valueOf(xx1));
                                        }

                                    }

                                }

                                if (Integer.parseInt(datas[0], 16) <= 5
                                        && Integer.parseInt(datas[1], 16) <= 23) {
                                    String lengthStr = datas[3] + datas[2];
                                    int length = Integer.valueOf(lengthStr, 16);
                                    if (datas.length >= length + guidelength) { //2个引导 2个长度 2个CRC
                                        byte[] cmd = new byte[length + typelength + datalength];
                                        for (int i = 0; i < length + typelength + datalength; i++) {
                                            cmd[i] = (byte) Integer.parseInt(datas[i], 16);
                                        }

                                        int verify = Crc16Calc(cmd, cmd.length);

                                        String verStr = datas[length + guidelength - 1] + datas[length + guidelength - 2];
                                        int ver = Integer.parseInt(verStr, 16);

                                        if (verify == ver) {

                                            String[] strTmp = new String[length + guidelength];
                                            System.arraycopy(datas, 0, strTmp, 0, length + guidelength);

                                            stringBufferMess.delete(0, (strTmp.length-1) * 3+2);

                                            //发出
                                            Log.i(TAG, "发出的数据" + arrToString(strTmp));
                                            Message msg = new Message();
                                            Bundle b = new Bundle();
                                            b.putStringArray("Mess", strTmp);
                                            msg.setData(b);
                                            mMessHandler.sendMessage(msg);

////                                mOnServiceDisplayDATA.OnDATA(strTmp);
//                                            strTmp = (String[]) concat(new String[]{"Mess"},strTmp);

                                            EventBus.getDefault().post(b);
                                        } else {
                                            stringBufferMess=new StringBuffer();
                                        }
                                    }
                                } else {
                                    stringBufferMess=new StringBuffer();
                                }
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        stringBufferMess=new StringBuffer();
                    }
                }

                try {
                    Thread.sleep(40);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }

    }

    public static String arrToString(String[] arr){
        String str="";
        for(int i=0;i<arr.length;i++){
            str=str+arr[i];
        }
        return str;
    }
    public void init(Context context) {
        if (this.context == null) {
            this.context = context.getApplicationContext();
            bluetoothManager = (BluetoothManager) this.context.getSystemService(Context.BLUETOOTH_SERVICE);
            bluetoothAdapter = bluetoothManager.getAdapter();
        }
    }


    public synchronized BluetoothGatt connect(BluetoothDevice device, boolean autoConnect) {
        if(state != DISCONNECT)return null;

        if (device == null) {
            Log.w(TAG, "Device not found.  Unable to connect.");
            return null;
        }
        // We want to directly connect to the device, so we are setting the autoConnect
        // parameter to false.
        state = CONNECT_PROCESS;
        Log.d(TAG, "Trying to create a new connection.");
        return  device.connectGatt(context, false, coreGattCallback);
    }



//    public synchronized BluetoothGatt connect(BluetoothDevice bluetoothDevice, boolean autoConnect){//}, IConnectCallback connectCallback) {
//        if (bluetoothDevice == null ) {
//            throw new IllegalArgumentException("this BluetoothDevice or IConnectCallback is Null!");
//        }
//        if(state!= DISCONNECT)return null;
////        if (handler != null) {
////            Message msg = handler.obtainMessage(MSG_CONNECT_TIMEOUT, connectCallback);
////            handler.sendMessageDelayed(msg, connectTimeout);
////        }
////        this.connectCallback = connectCallback;
//        state = CONNECT_PROCESS;
//        return bluetoothDevice.connectGatt(this.context, autoConnect, coreGattCallback);
//    }

//    public void connect(BleDevice bleDevice, boolean autoConnect, IConnectCallback connectCallback) {
//        if (bleDevice == null) {
//            throw new IllegalArgumentException("this BluetoothLeDevice is Null!");
//        }
//        connect(bleDevice.getDevice(), autoConnect, connectCallback);
//    }


    public void connectByDevice(BluetoothDevice bluetoothDevice, boolean autoConnect){//}, IConnectCallback connectCallback) {
        if (bluetoothDevice == null) {
            throw new IllegalArgumentException("this BluetoothLeDevice is Null!");
        }
        connect(bluetoothDevice, autoConnect);
    }


    public void readRemoteRssi() {
        getBluetoothGatt().readRemoteRssi();
    }


    public boolean setCharacteristicNotification(BluetoothGattCharacteristic characteristic,
                                                 boolean enabled) {
        BluetoothGatt gatt=getBluetoothGatt();
        if ( gatt == null) {
//            LOG.logW(TAG, "BluetoothAdapter not initialized");
            return false;
        }
        gatt.setCharacteristicNotification(characteristic, enabled);

        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
        if(descriptor==null)return false;

        if (enabled) {
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);//ENABLE_NOTIFICATION_VALUE
        } else {
            descriptor.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
        }
//        LOG.logW(TAG, "enabled:"+ enabled);
        boolean Dp=gatt.writeDescriptor(descriptor);
        SystemClock.sleep(200);
        return Dp;
    }

    public boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

//    public void runOnMainThread(Runnable runnable) {
//        if (isMainThread()) {
//            runnable.run();
//        } else {
//            if (handler != null) {
//                handler.post(runnable);
//            }
//        }
//    }

    public boolean isConnected() {
        if (state == CONNECT_SUCCESS) {
            return true;
        } else {
            return false;
        }
    }


    public synchronized boolean refreshDeviceCache() {
        try {
            final Method refresh = BluetoothGatt.class.getMethod("refresh");
            if (refresh != null && bluetoothGatt != null) {
                final boolean success = (Boolean) refresh.invoke(getBluetoothGatt());
                BleLog.i("Refreshing result: " + success);
                return success;
            }
        } catch (Exception e) {
            BleLog.e("An exception occured while refreshing device", e);
        }
        return false;
    }

    public synchronized void disconnect() {
        if (bluetoothGatt != null) {
            bluetoothGatt.disconnect();
        }
    }

    public synchronized void close() {
        if (bluetoothGatt != null) {
            bluetoothGatt.close();
//            BleLog.e("bluetoothGatt.close");
        }
        state = DISCONNECT;
    }

    public synchronized void clear() {
        disconnect();
        refreshDeviceCache();
        close();
//        if (handler != null) {
//            handler.removeCallbacksAndMessages(null);
//        }
    }


    public BluetoothGatt getBluetoothGatt() {
        return bluetoothGatt;
    }


//    public ViseBluetooth setScanTimeout(int scanTimeout) {
//        this.scanTimeout = scanTimeout;
//        return this;
//    }


//    public ViseBluetooth setState(State state) {
//        this.state = state;
//        return this;
//    }

    public interface OnServiceDisplayStatus{
        void OnStatus(String status);
    }

    OnServiceDisplayStatus mOnServiceDisplayStatus=null;
    public void setOnServiceDisplayStatus(OnServiceDisplayStatus e){
        mOnServiceDisplayStatus=e;
    }

    public void write(final byte[] outBuffer) {
        StringBuilder stringBuilder = new StringBuilder(outBuffer.length);
        for(byte byteChar : outBuffer){
            String ms=String.format("%02X ", byteChar).trim();
            stringBuilder.append(ms+"-");
        }
        BleLog.i("添加发下去的命令"+stringBuilder.toString());
        mSendDataList.add(stringBuilder.toString());
    }

    public BluetoothDevice getConnectBt(final String mac) {
        if (mac == null || mac.split(":").length != 6) {
            throw new IllegalArgumentException("Illegal MAC!");
        }
        return bluetoothAdapter.getRemoteDevice(mac);
    }

    public interface OnServiceDisplayDATA{
        void OnDATA(Bundle bundle);
    }

    ArrayList<OnServiceDisplayDATA> mOnServiceDisplayDATAList=new ArrayList<>();

    public void setOnServiceDisplayDATA(OnServiceDisplayDATA e){
        mOnServiceDisplayDATAList.add(e);
    }

    public void RemoveOnServiceDisplayDATA(OnServiceDisplayDATA e){
        mOnServiceDisplayDATAList.remove(e);
    }
    public void RemoveAllOnServiceDisplayDATA(){
        mOnServiceDisplayDATAList.clear();
    }


    private  void writeToFile(char type, String tag, String msg) {

        if (null == logPath) {
            Log.e(TAG, "logPath == null ，未初始化LogToFile");
            return;
        }
        String log = dateFormat.format(date) + " " + type + " " + tag + " " + msg + "\n";//log日志内容，可以自行定制

        //如果父路径不存在
        File file = new File(logPath);
        if (!file.exists()) {
            file.mkdirs();//创建父路径
        }

        FileOutputStream fos = null;//FileOutputStream会自动调用底层的close()方法，不用关闭
        BufferedWriter bw = null;
        try {
            fos = new FileOutputStream(fileName, true);//这里的第二个参数代表追加还是覆盖，true为追加，flase为覆盖
            bw = new BufferedWriter(new OutputStreamWriter(fos));
            bw.write(log);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) {
                    bw.close();//关闭缓冲流
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
