package senssun.ycblelib;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.content.Context;

import com.czjk.blelib.model.Alarm;
import com.czjk.blelib.model.BaseEvent;
import com.czjk.blelib.model.DoNotDisturb;
import com.czjk.blelib.model.HealthSport;
import com.czjk.blelib.model.LongSit;
import com.czjk.blelib.model.Unit;
import com.czjk.blelib.utils.Constant;
import com.czjk.blelib.utils.HexUtil;
import com.czjk.blelib.utils.SPUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import senssun.ycblelib.db.DBTools;

import static senssun.ycblelib.db.DBTools.dbTools;


public class YCProtocolUtils {
    private static YCProtocolUtils protocolUtils;
    public  static YCViseBluetooth viseBluetooth= YCViseBluetooth.getInstance();




    public static YCProtocolUtils getInstance() {
        if (protocolUtils == null) {
            synchronized (YCProtocolUtils.class) {
                if (protocolUtils == null) {
                    protocolUtils = new YCProtocolUtils();
                }
            }
        }
        return protocolUtils;
    }

    public void init(Context context) {
        viseBluetooth.init(context);
        SPUtil.getInstance(context);
        DBTools.getInstance(context);

        viseBluetooth.setOnServiceDisplayStatus(new YCViseBluetooth.OnServiceDisplayStatus() {
            @Override
            public void OnStatus(String status) {
                String[] strdata=status.split("-");
                if(strdata[1].equals("status")) {
                    switch (strdata[2]) {
                        case "disconnect": {
                            EventBus.getDefault().post(new BaseEvent(BaseEvent.EventType.STATE_DISCONNECTED));
                        }
                        break;
                        case "connect": {
                            EventBus.getDefault().post(new BaseEvent(BaseEvent.EventType.STATE_CONNECTED));
                        }
                        break;
                    }
                }
            }
        });
    }




//    IConnectCallback callback = new IConnectCallback() {
//        @Override
//        public void onConnectSuccess(BluetoothGatt gatt, int status) {
//            BluetoothGattCharacteristic Characteristic = gatt.getService(SERVICE_UUID).getCharacteristic(CHARACTERISTIC_COMMANDNOTIFY_UUID);
//            BluetoothGattCharacteristic massDataCharacteristic = gatt.getService(SERVICE_UUID).getCharacteristic(CHARACTERISTIC_MASSDATANOTIFY_UUID);
//
//            if (Characteristic != null&&massDataCharacteristic!=null) {
//                boolean Success = viseBluetooth.enableCharacteristicNotification(Characteristic, false);
//                boolean massDataSuccess = viseBluetooth.enableCharacteristicNotification(massDataCharacteristic, false);
//                if (Success&&massDataSuccess) {
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    EventBus.getDefault().post(new BaseEvent(BaseEvent.EventType.STATE_CONNECTED));
//                }
//            }
//        }
//
//        @Override
//        public void onConnectFailure(BleException exception) {
//            BleLog.e("onConnectFailure:" + exception.getDescription());
//            EventBus.getDefault().post(new BaseEvent(BaseEvent.EventType.STATE_DISCONNECTED));
//        }
//
//        @Override
//        public void onDisconnect() {
//            BleLog.e("onDisconnect");
//            EventBus.getDefault().post(new BaseEvent(BaseEvent.EventType.STATE_DISCONNECTED));
//        }
//    };

    public BluetoothGatt getBluetoothGatt(){
        return viseBluetooth.getBluetoothGatt();
    }


    public void connect(BluetoothDevice device) {
        viseBluetooth.connectByDevice(device, false);
    }

    public void connect(String mac) {
        connect(viseBluetooth.getConnectBt(mac));
    }

    public void disconnect() {
        if (isConnDevice()) {
            viseBluetooth.disconnect();
//            viseBluetooth.close();
        }
    }

    public void clear() {
        viseBluetooth.clear();
    }

    public void readRemoteRssi() {
        viseBluetooth.readRemoteRssi();
    }

    private void write(byte[] params) {
        if(params!=null){

            String str=String.format("%04X ", Crc16Calc(params,params.length)).trim();

            params=concat(params,new byte[]{(byte)Integer.parseInt(str.substring(str.length()-2,str.length()),16),
                    (byte)Integer.parseInt(str.substring(0,2),16)

            });

            viseBluetooth.write(params);
        }
    }

//    private void sendCmd(byte cmdId, byte key) {
//        byte[] cmd = new byte[20];
//        cmd[0] = cmdId;
//        cmd[1] = key;
//        for (int i = 2; i < 20; i++) {
//            cmd[i] = Constant.RESERVED;
//        }
//        write(cmd);
//    }

    public boolean isConnDevice() {
        return viseBluetooth.isConnected();
    }

    public void RESET() {
        byte[] cmd = new byte[4];
        cmd[0] = Constant.CMD_ID_DFU;
        cmd[1] = Constant.KEY_RESET;
        write(cmd);
    }

    public void TRANSPORT() {
        byte[] cmd = new byte[4];
        cmd[0] = Constant.CMD_ID_DFU;
        cmd[1] = Constant.KEY_TRANSPORT;
        write(cmd);
    }

    public void OTA() {
        byte[] cmd = new byte[4];
        cmd[0] = Constant.CMD_ID_DFU;
        cmd[1] = Constant.KEY_OTA;
        write(cmd);
    }

    public void getFW() {
        byte[] cmd = new byte[4];
        cmd[0] = Constant.CMD_ID_GET;
        cmd[1] = Constant.KEY_GET_FW;
        write(cmd);
    }

    public void getTime() {
        byte[] cmd = new byte[4];
        cmd[0] = Constant.CMD_ID_GET;
        cmd[1] = Constant.KEY_GET_TIME;
        write(cmd);
    }

    public void getMac(){
        byte[] cmd = new byte[4];
        cmd[0] = Constant.CMD_ID_GET;
        cmd[1] = Constant.KEY_GET_MAC;

        write(cmd);
    }

    public void getElectricity() {
        byte[] cmd = new byte[4];
        cmd[0] = Constant.CMD_ID_GET;
        cmd[1] = Constant.KEY_GET_ELECTRICITY;

        write(cmd);
    }

    public void getLiveData() {
        byte[] cmd = new byte[4];
        cmd[0] = Constant.CMD_ID_GET;
        cmd[1] = Constant.KEY_GET_LIVE_DATA;

        write(cmd);
    }

    public void getHisData(Calendar beginCal,Calendar endCal) {
        int lenght=8;
        byte[] cmd = new byte[12];
        cmd[0] = Constant.CMD_ID_GET;
        cmd[1] = Constant.KEY_GET_SYNC;
        cmd[2] =(byte)Integer.parseInt(Long.toHexString(lenght%256), 16);
        cmd[3] =(byte)Integer.parseInt(Long.toHexString(lenght/256), 16);

        cmd[4] = (byte) (beginCal.get(Calendar.YEAR)-2000);
        cmd[5] = (byte) (beginCal.get(Calendar.MONTH)+1);
        cmd[6] = (byte) (beginCal.get(Calendar.DAY_OF_MONTH));
        cmd[7] = (byte) (beginCal.get(Calendar.HOUR_OF_DAY));

        cmd[8] = (byte) (endCal.get(Calendar.YEAR)-2000);
        cmd[9] = (byte) (endCal.get(Calendar.MONTH)+1);
        cmd[10] = (byte) (endCal.get(Calendar.DAY_OF_MONTH));
        cmd[11] = (byte) (endCal.get(Calendar.HOUR_OF_DAY));

        write(cmd);
    }

    public void getSleepsData() {
        byte[] cmd = new byte[4];
        cmd[0] = Constant.CMD_ID_GET;
        cmd[1] = Constant.KEY_GET_SLEEPRECORD;

        write(cmd);
    }

    public void  getAllAlarmRecord(){
        byte[] cmd = new byte[4];
        cmd[0] = Constant.CMD_ID_GET;
        cmd[1] = Constant.KEY_GET_ALLALARMRECORD;
        write(cmd);
    }

    public void  getCurrentAcceleration(){
        byte[] cmd = new byte[5];
        cmd[0] = Constant.CMD_ID_GET;
        cmd[1] = Constant.KEY_GET_CURRENTACCELERATION;
        cmd[2]=0x01;
        cmd[4]=0x01;
        write(cmd);
    }

    public void  getCurrentEcgRecord(){
        byte[] cmd = new byte[5];
        cmd[0] = Constant.CMD_ID_GET;
        cmd[1] = Constant.KEY_GET_CURRENTECGRECORD;
        cmd[2]=0x01;
        cmd[4]=0x01;
        write(cmd);
    }


    public void setHeartRate(boolean isOpen) {
        int lenght=1;
        byte[] cmd = new byte[5];
        cmd[0] = Constant.CMD_ID_GET;
        cmd[1] = Constant.KEY_GET_MEASURE_HR;
        cmd[2] =(byte)Integer.parseInt(Long.toHexString(lenght%256), 16);
        cmd[3] =(byte)Integer.parseInt(Long.toHexString(lenght/256), 16);
        cmd[4] = (byte) (isOpen?1:0);
        write(cmd);
    }

    public void setBlood(boolean isOpen) {
        int lenght=1;
        byte[] cmd = new byte[5];
        cmd[0] = Constant.CMD_ID_GET;
        cmd[1] = Constant.KEY_GET_BLOOD;
        cmd[2] =(byte)Integer.parseInt(Long.toHexString(lenght%256), 16);
        cmd[3] =(byte)Integer.parseInt(Long.toHexString(lenght/256), 16);
        cmd[4] = (byte) (isOpen?1:0);
        write(cmd);
    }

    public void setNotice() {
        int lenght=0;
        byte[] cmd = new byte[4];
        cmd[0] = Constant.CMD_ID_GET;
        cmd[1] = Constant.KEY_GET_NOTICE;
        cmd[2] =(byte)Integer.parseInt(Long.toHexString(lenght%256), 16);
        cmd[3] =(byte)Integer.parseInt(Long.toHexString(lenght/256), 16);
        write(cmd);
    }


    public void getAlarm(int id) {
        int lenght=1;
        byte[] cmd = new byte[5];
        cmd[0] = Constant.CMD_ID_GET;
        cmd[1] = Constant.KEY_GET_ALARM;
        cmd[2] =(byte)Integer.parseInt(Long.toHexString(lenght%256), 16);
        cmd[3] =(byte)Integer.parseInt(Long.toHexString(lenght/256), 16);
        cmd[4]=(byte)id;
        write(cmd);
    }

    public void getUserInfo() {
        int lenght=0;
        byte[] cmd = new byte[4];
        cmd[0] = Constant.CMD_ID_GET;
        cmd[1] = Constant.KEY_GET_USERINFO;
        cmd[2] =(byte)Integer.parseInt(Long.toHexString(lenght%256), 16);
        cmd[3] =(byte)Integer.parseInt(Long.toHexString(lenght/256), 16);
        write(cmd);
    }

    public void getUnit() {
        int lenght=0;
        byte[] cmd = new byte[4];
        cmd[0] = Constant.CMD_ID_GET;
        cmd[1] = Constant.KEY_GET_UNIT;
        cmd[2] =(byte)Integer.parseInt(Long.toHexString(lenght%256), 16);
        cmd[3] =(byte)Integer.parseInt(Long.toHexString(lenght/256), 16);
        write(cmd);

    }

    public void getLongSit() {
        int lenght=0;
        byte[] cmd = new byte[4];
        cmd[0] = Constant.CMD_ID_GET;
        cmd[1] = Constant.KEY_GET_LONG_SIT;
        cmd[2] =(byte)Integer.parseInt(Long.toHexString(lenght%256), 16);
        cmd[3] =(byte)Integer.parseInt(Long.toHexString(lenght/256), 16);
        write(cmd);
    }

    public void getHeartRateMode() {
        int lenght=0;
        byte[] cmd = new byte[4];
        cmd[0] = Constant.CMD_ID_GET;
        cmd[1] = Constant.KEY_GET_HEART_MODE;
        cmd[2] =(byte)Integer.parseInt(Long.toHexString(lenght%256), 16);
        cmd[3] =(byte)Integer.parseInt(Long.toHexString(lenght/256), 16);
        write(cmd);
    }

    public void getUPHandGesture() {
        int lenght=0;
        byte[] cmd = new byte[4];
        cmd[0] = Constant.CMD_ID_GET;
        cmd[1] = Constant.KEY_GET_RAISE;
        cmd[2] =(byte)Integer.parseInt(Long.toHexString(lenght%256), 16);
        cmd[3] =(byte)Integer.parseInt(Long.toHexString(lenght/256), 16);
        write(cmd);
    }

    public void getDoNotDisturb() {
        int lenght=0;
        byte[] cmd = new byte[4];
        cmd[0] = Constant.CMD_ID_GET;
        cmd[1] = Constant.KEY_GET_DONOTDISTURB;
        cmd[2] =(byte)Integer.parseInt(Long.toHexString(lenght%256), 16);
        cmd[3] =(byte)Integer.parseInt(Long.toHexString(lenght/256), 16);
        write(cmd);
    }

    public void getPPG(boolean isOpen) {
        int lenght=1;
        byte[] cmd = new byte[5];
        cmd[0] = Constant.CMD_ID_GET;
        cmd[1] = Constant.KEY_GET_PPG;
        cmd[2] =(byte)Integer.parseInt(Long.toHexString(lenght%256), 16);
        cmd[3] =(byte)Integer.parseInt(Long.toHexString(lenght/256), 16);
        cmd[4] = (byte) (isOpen?1:0);
        write(cmd);
    }

    public void getRR(boolean isOpen) {
        int lenght=1;
        byte[] cmd = new byte[5];
        cmd[0] = Constant.CMD_ID_GET;
        cmd[1] = Constant.KEY_GET_RR;
        cmd[2] =(byte)Integer.parseInt(Long.toHexString(lenght%256), 16);
        cmd[3] =(byte)Integer.parseInt(Long.toHexString(lenght/256), 16);
        cmd[4] = (byte) (isOpen?1:0);
        write(cmd);
    }
//    Command Id:0x02,Key:0x13 APP获取手环血压预警阈值

    public void getBloodThreshold(){
        int lenght=0;
        byte[] cmd = new byte[4];
        cmd[0] = Constant.CMD_ID_GET;
        cmd[1] = Constant.KEY_GET_BLOODTHRESHOLD;
        cmd[2] =(byte)Integer.parseInt(Long.toHexString(lenght%256), 16);
        cmd[3] =(byte)Integer.parseInt(Long.toHexString(lenght/256), 16);
        write(cmd);
    }

    public void getTarget(int type){
        int lenght=1;
        byte[] cmd = new byte[5];
        cmd[0] = Constant.CMD_ID_GET;
        cmd[1] = Constant.KEY_GET_TARGET;
        cmd[2] =(byte)Integer.parseInt(Long.toHexString(lenght%256), 16);
        cmd[3] =(byte)Integer.parseInt(Long.toHexString(lenght/256), 16);
        cmd[4] = (byte) type;
        write(cmd);
    }

    public void getHandChange(){
        int lenght=1;
        byte[] cmd = new byte[4];
        cmd[0] = Constant.CMD_ID_GET;
        cmd[1] = Constant.KEY_GET_HANDCHANGE;
        cmd[2] =(byte)Integer.parseInt(Long.toHexString(lenght%256), 16);
        cmd[3] =(byte)Integer.parseInt(Long.toHexString(lenght/256), 16);
        write(cmd);
    }

    public void getSportRecord(){
        int lenght=0;
        byte[] cmd = new byte[4];
        cmd[0] = Constant.CMD_ID_GET;
        cmd[1] = Constant.KEY_GET_SPORTRECORD;
        cmd[2] =(byte)Integer.parseInt(Long.toHexString(lenght%256), 16);
        cmd[3] =(byte)Integer.parseInt(Long.toHexString(lenght/256), 16);
        write(cmd);
    }


    public void setTime(int year, int month, int day, int hour, int minute, int second) {
        int lenght=6;
        byte[] cmd = new byte[10];
        cmd[0] = Constant.CMD_ID_SET;
        cmd[1] = Constant.KEY_SET_TIME;
        cmd[2] =(byte)Integer.parseInt(Long.toHexString(lenght%256), 16);
        cmd[3] =(byte)Integer.parseInt(Long.toHexString(lenght/256), 16);
        cmd[4] = (byte) year;
        cmd[5] = (byte) month;
        cmd[6] = (byte) day;
        cmd[7] = (byte) hour;
        cmd[8] = (byte) minute;
        cmd[9] = (byte) second;
        write(cmd);
    }

    public void setAlarm(Alarm alarm) {
        int lenght=5;
        byte[] cmd = new byte[9];
        cmd[0] = Constant.CMD_ID_SET;
        cmd[1] = Constant.KEY_SET_ALARM;
        cmd[2] =(byte)Integer.parseInt(Long.toHexString(lenght%256), 16);
        cmd[3] =(byte)Integer.parseInt(Long.toHexString(lenght/256), 16);
        int id = alarm.getAlarmId();
        cmd[4] = (byte) id;
        cmd[5] = (byte) alarm.getState();
        String[] time = alarm.getTime().split(":");
        int hour = Integer.parseInt(time[0]);
        int minute = Integer.parseInt(time[1]);
        cmd[6] = (byte) hour;
        cmd[7] = (byte) minute;
        int week = HexUtil.binaryToAlgorism(alarm.getRepeat());
        cmd[8] = (byte) week;
        write(cmd);
    }

    public void setUserInfo(int age,int height,int weight,int sex,int Stride) {
        int lenght=5;
        byte[] cmd = new byte[9];
        cmd[0] = Constant.CMD_ID_SET;
        cmd[1] = Constant.KEY_SET_USER_INFO;
        cmd[2] =(byte)Integer.parseInt(Long.toHexString(lenght%256), 16);
        cmd[3] =(byte)Integer.parseInt(Long.toHexString(lenght/256), 16);
        cmd[4] = (byte) age;
        cmd[5] = (byte) height;
        cmd[6] = (byte) weight;
        cmd[7] = (byte) sex;
        cmd[8] = (byte)Integer.parseInt(Long.toHexString(Stride), 16);
        write(cmd);
    }



    /**
     * 设置公英制与时间制
     */
    public void setUnit(Unit unit) {
        int lenght=2;
        byte[] cmd = new byte[6];
        cmd[0] = Constant.CMD_ID_SET;
        cmd[1] = Constant.KEY_SET_USER_UNIT;
        cmd[2] =(byte)Integer.parseInt(Long.toHexString(lenght%256), 16);
        cmd[3] =(byte)Integer.parseInt(Long.toHexString(lenght/256), 16);

        cmd[4] = (byte) unit.getUnit();
        cmd[5] = (byte) unit.getTimeMode();
        write(cmd);
    }

    /**
     * 设置久坐提醒
     */
    public void setLongSit(LongSit longSit) {
        int lenght=7;
        byte[] cmd = new byte[11];
        cmd[0] = Constant.CMD_ID_SET;
        cmd[1] = Constant.KEY_SET_LONG_SIT;
        cmd[2] =(byte)Integer.parseInt(Long.toHexString(lenght%256), 16);
        cmd[3] =(byte)Integer.parseInt(Long.toHexString(lenght/256), 16);
        cmd[4] = (byte)Integer.parseInt(Long.toHexString(longSit.getStartHour()), 16);
        cmd[5] = (byte)Integer.parseInt(Long.toHexString(longSit.getStartMinute()), 16);
        cmd[6] = (byte)Integer.parseInt(Long.toHexString(longSit.getEndHour()), 16);
        cmd[7] = (byte)Integer.parseInt(Long.toHexString(longSit.getEndMinute()), 16);

        cmd[8] =(byte)Integer.parseInt(Long.toHexString(longSit.getInterval()%256), 16);
        cmd[9] =(byte)Integer.parseInt(Long.toHexString(longSit.getInterval()/256), 16);

        int week = HexUtil.binaryToAlgorism(longSit.getRepeat());
        cmd[10] = (byte) week;
        write(cmd);
    }

    /**
     * 设置心率检测模式
     *
     * @param isAuto true: 自动  false:  手动(停止每5分钟的检测)
     */
    public void setHeartRateAuto(boolean isAuto) {
        int lenght=1;
        byte[] cmd = new byte[5];
        cmd[0] = Constant.CMD_ID_SET;
        cmd[1] = Constant.KEY_SET_HEART_MODE;
        cmd[2] =(byte)Integer.parseInt(Long.toHexString(lenght%256), 16);
        cmd[3] =(byte)Integer.parseInt(Long.toHexString(lenght/256), 16);
        cmd[4] = (byte) (isAuto ? 1 : 0);
        write(cmd);
    }

    /**
     * 设置抬腕亮屏
     *
     * @param isOpen true: 开启  false:  关闭
     */
    public void setUPHandGesture(boolean isOpen) {
        int lenght=1;
        byte[] cmd = new byte[5];
        cmd[0] = Constant.CMD_ID_SET;
        cmd[1] = Constant.KEY_SET_UP_HAND_GESTURE;
        cmd[2] =(byte)Integer.parseInt(Long.toHexString(lenght%256), 16);
        cmd[3] =(byte)Integer.parseInt(Long.toHexString(lenght/256), 16);
        cmd[4] = (byte) (isOpen ? 1 : 0);
        write(cmd);
    }

    /**
     * 设置勿扰模式
     */
    public void setDoNotDisturb(DoNotDisturb doNotDisturb) {
        int lenght=5;
        byte[] cmd = new byte[9];
        cmd[0] = Constant.CMD_ID_SET;
        cmd[1] = Constant.KEY_SET_DONOTDISTURB;
        cmd[2] =(byte)Integer.parseInt(Long.toHexString(lenght%256), 16);
        cmd[3] =(byte)Integer.parseInt(Long.toHexString(lenght/256), 16);
        cmd[4] = (byte) (doNotDisturb.isOpen() ? 1 : 0);
        cmd[5] = (byte) doNotDisturb.getStartHour();
        cmd[6] = (byte) doNotDisturb.getStartMinute();
        cmd[7] = (byte) doNotDisturb.getEndHour();
        cmd[8] = (byte) doNotDisturb.getEndMinute();
        write(cmd);
    }


    /**
     * 设置手环通知提醒开关状态
     */
    public void setNoticeSwitch(boolean noticeSwitch,String noticeItem0,String noticeItem1,int call_delay) {
        int lenght=4;
        byte[] cmd = new byte[8];
        cmd[0] = Constant.CMD_ID_SET;
        cmd[1] = Constant.KEY_SET_NOTICE;
        cmd[2] =(byte)Integer.parseInt(Long.toHexString(lenght%256), 16);
        cmd[3] =(byte)Integer.parseInt(Long.toHexString(lenght/256), 16);
        cmd[4] = (byte) (noticeSwitch ? 1 : 0);
        int item0 = HexUtil.binaryToAlgorism(noticeItem0);
        cmd[5] = (byte) item0;
        int item1 = HexUtil.binaryToAlgorism(noticeItem1);
        cmd[6] = (byte) item1;
        cmd[7] = (byte)Integer.parseInt(Long.toHexString(call_delay), 16);
        write(cmd);
    }

    /**
     * 设置手环闹钟名称
     */
    public void setAlarmName(int alarmId,String alarmName) {
        int lenght=0;
        byte[] cmd = new byte[4];
        cmd[0] = Constant.CMD_ID_SET;
        cmd[1] = Constant.KEY_SET_ALARM_NAME;

        byte[] nameBytes=new byte[]{};
        try {
            nameBytes=alarmName.getBytes("UTF-8");
            lenght=nameBytes.length+1;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        cmd[2] =(byte)Integer.parseInt(Long.toHexString(lenght%256), 16);
        cmd[3] =(byte)Integer.parseInt(Long.toHexString(lenght/256), 16);

        cmd =concat(cmd,new byte[]{(byte)alarmId});
        cmd =concat(cmd,nameBytes);
        write(cmd);
    }

    /**
     * 设置手环相机控制状态
     */
    public void setCamera(boolean isOpen) {
        int lenght=1;
        byte[] cmd = new byte[5];
        cmd[0] = Constant.CMD_ID_SET;
        cmd[1] = Constant.KEY_SET_CAMERA;
        cmd[2] =(byte)Integer.parseInt(Long.toHexString(lenght%256), 16);
        cmd[3] =(byte)Integer.parseInt(Long.toHexString(lenght/256), 16);
        cmd[4] = (byte) (isOpen ? 1 : 0);
        write(cmd);
    }

    /**
     * 设置手环横竖屏显示方式
     */
    public void setScreen(boolean isLandscape) {
        int lenght=1;
        byte[] cmd = new byte[5];
        cmd[0] = Constant.CMD_ID_SET;
        cmd[1] = Constant.KEY_SET_SCREEN_MODE;
        cmd[2] =(byte)Integer.parseInt(Long.toHexString(lenght%256), 16);
        cmd[3] =(byte)Integer.parseInt(Long.toHexString(lenght/256), 16);
        cmd[4] = (byte) (isLandscape ? 0 : 1);
        write(cmd);
    }

    /**
     * 设置手环血压预警阈值
     */
    public void setBloodThreshold(int highVoltage) {
        int lenght=2;
        byte[] cmd = new byte[5];
        cmd[0] = Constant.CMD_ID_SET;
        cmd[1] = Constant.KEY_SET_BLOOD_THRESHOLD;
        cmd[2] =(byte)Integer.parseInt(Long.toHexString(lenght%256), 16);
        cmd[3] =(byte)Integer.parseInt(Long.toHexString(lenght/256), 16);
        cmd[4] = (byte)Integer.parseInt(Long.toHexString(highVoltage), 16);
//        cmd[5] = (byte)Integer.parseInt(String.valueOf(lowVoltage), 16);
        write(cmd);
    }

    /**
     * 设置手环运动及睡眠目标
     */
    public void setTarget(int type,int target) {
        int lenght=5;
        byte[] cmd = new byte[9];
        cmd[0] = Constant.CMD_ID_SET;
        cmd[1] = Constant.KEY_SET_TARGET;
        cmd[2] =(byte)Integer.parseInt(Long.toHexString(lenght%256), 16);
        cmd[3] =(byte)Integer.parseInt(Long.toHexString(lenght/256), 16);

        cmd[4] =(byte)Integer.parseInt(Long.toHexString(type), 16);

        cmd[5]=(byte)Integer.parseInt(Long.toHexString(target% 256), 16);
        cmd[6]=(byte)Integer.parseInt(Long.toHexString(target/256%256), 16);
        cmd[7]=(byte)Integer.parseInt(Long.toHexString(target/65536%65536), 16);
        cmd[8]=(byte)Integer.parseInt(Long.toHexString(target/16777216), 16);

        write(cmd);
    }


    /**
     * 设置手环左右手佩戴配置
     *
     */
    public void setHandChange(int WearingMode) {
        int lenght=1;
        byte[] cmd = new byte[5];
        cmd[0] = Constant.CMD_ID_SET;
        cmd[1] = Constant.KEY_SET_HANDCHANGE;
        cmd[2] =(byte)Integer.parseInt(Long.toHexString(lenght%256), 16);
        cmd[3] =(byte)Integer.parseInt(Long.toHexString(lenght/256), 16);
        cmd[4] = (byte) WearingMode;
        write(cmd);
    }


    /**
     * 绑定手环
     */
    public void setBind() {
        byte[] cmd = new byte[4];
        cmd[0] = Constant.CMD_ID_BIND;
        cmd[1] = Constant.KEY_BIND;
        write(cmd);
    }

    public void setUnBind() {
        byte[] cmd = new byte[4];
        cmd[0] = Constant.CMD_ID_BIND;
        cmd[1] = Constant.KEY_UNBIND;
        write(cmd);
    }

    public void setDiscountBLE() {
        byte[] cmd = new byte[4];
        cmd[0] = Constant.CMD_ID_BIND;
        cmd[1] = Constant.KEY_DISCOUNT_BLE;
        write(cmd);
    }

    public void setBloodCalibration(int lowVoltage,int highVoltage) {
        int lenght=2;
        byte[] cmd = new byte[6];
        cmd[0] = Constant.CMD_ID_BIND;
        cmd[1] = Constant.KEY_BLOOD_CALIBRATION;
        cmd[2] =(byte)Integer.parseInt(Long.toHexString(lenght%256), 16);
        cmd[3] =(byte)Integer.parseInt(Long.toHexString(lenght/256), 16);
        cmd[4] = (byte)Integer.parseInt(Long.toHexString(highVoltage), 16);
        cmd[5] = (byte)Integer.parseInt(Long.toHexString(lowVoltage), 16);
        write(cmd);
    }

    public void Remind(int type,String text) {

        int lenght=0;
        byte[] cmd = new byte[4];
        cmd[0] = Constant.CMD_ID_BIND;
        cmd[1] = Constant.KEY_SENDMESS_BLE;

        byte[] textBytes=new byte[]{};
        try {
            textBytes=text.getBytes("UTF-8");
            lenght=textBytes.length+1;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        cmd[2] =(byte)Integer.parseInt(Long.toHexString(lenght%256), 16);
        cmd[3] =(byte)Integer.parseInt(Long.toHexString(lenght/256), 16);

        cmd =concat(cmd,new byte[]{(byte)type});
        cmd =concat(cmd,textBytes);

        write(cmd);
    }

    public void findBle() {
        byte[] cmd = new byte[4];
        cmd[0] = Constant.CMD_ID_BIND;
        cmd[1] = Constant.KEY_FIND_BLE;
        write(cmd);
    }

//    private byte[] getStart(int type) {
//        byte[] cmd = new byte[20];
//        cmd[0] = Constant.CMD_ID_ALARM;
//        cmd[1] = Constant.KEY_ALARM;
//        cmd[2] = Constant.KEY_ALARM_START;
//        cmd[3] = (byte) type;
//        for (int i = 4; i < 20; i++) {
//            cmd[i] = Constant.RESERVED;
//        }
//        return cmd;
//    }



//    private byte[] getName(String fromName, int type) {
//        byte[] name;
//        if (fromName.matches("[0-9]+")) {
//            name = getNumberHexString(fromName);
//        } else {
//            name = HexUtil.hz2utf(fromName);
//        }
//        byte[] c = getBytes(true, type, name);
//        return c;
//    }

//    private byte[] getContent(String text, int type) {
//        if (text == null || text.length() == 0) {
//            byte[] cmd = new byte[20];
//            cmd[0] = Constant.CMD_ID_ALARM;
//            cmd[1] = Constant.KEY_ALARM;
//            cmd[2] = Constant.KEY_ALARM_CONTENT;
//            cmd[3] = (byte) type;
//            for (int i = 4; i < 20; i++) {
//                cmd[i] = Constant.RESERVED;
//            }
//            return cmd;
//        }
//        return getBytes(false, (byte) type, HexUtil.hz2utf(text));
//    }

//    private byte[] getStop(int type) {
//        byte[] cmd = new byte[20];
//        cmd[0] = Constant.CMD_ID_ALARM;
//        cmd[1] = Constant.KEY_ALARM;
//        cmd[2] = Constant.KEY_ALARM_STOP;
//        cmd[3] = (byte) type;
//        for (int i = 4; i < 20; i++) {
//            cmd[i] = Constant.RESERVED;
//        }
//        return cmd;
//    }

//    private byte[] getNumberHexString(String phone) {
//        int[] ints = HexUtil.string2ASCII(phone);
//        BleLog.e(Arrays.toString(ints));
//        int len = ints.length;
//        byte[] name = new byte[len];
//        for (int i = 0; i < len; i++) {
//            name[i] = (byte) ints[i];
//        }
//        return name;
//    }

//    private byte[] getBytes(boolean isName, int type, byte[] name) {
//        byte[] cmd = new byte[4];
//        cmd[0] = Constant.CMD_ID_ALARM;
//        cmd[1] = Constant.KEY_ALARM;
//        cmd[2] = isName ? Constant.KEY_ALARM_NAME : Constant.KEY_ALARM_CONTENT;
//        cmd[3] = (byte) type;
//        int len = name.length % 16 == 0 ? name.length / 16 : name.length / 16 + 1;
//        StringBuffer sb = new StringBuffer();
//        for (int i = 0; i < len; i++) {
//            if (i == len - 1) {
//                if (name.length % 16 == 0) {
//                    byte[] c1 = new byte[16];
//                    System.arraycopy(name, i * 16, c1, 0, 16);
//                    byte[] bytes = HexUtil.arraycat(cmd, c1);
//                    sb.append(HexUtil.bytesToHexString(bytes));
//                } else {
//                    int s = name.length % 16;
//                    byte[] c1 = new byte[s];
//                    System.arraycopy(name, i * 16, c1, 0, s);
//                    byte[] c2 = new byte[16 - s];
//                    for (int j = 0; j < 16 - s; j++) {
//                        c2[j] = Constant.RESERVED;
//                    }
//                    byte[] bytes = HexUtil.arraycat(c1, c2);
//                    byte[] b = HexUtil.arraycat(cmd, bytes);
//                    sb.append(HexUtil.bytesToHexString(b));
//                }
//            } else {
//                byte[] c1 = new byte[16];
//                System.arraycopy(name, i * 16, c1, 0, 16);
//                byte[] bytes = HexUtil.arraycat(cmd, c1);
//                sb.append(HexUtil.bytesToHexString(bytes));
//            }
//        }
//        return HexUtil.hexStringToBytes(sb.toString());
//    }



    public List<HealthSport> getHealthSport(int year, int month, int day) {
        String time = "" + year + (month < 10 ? "0" + month : month) + (day < 10 ? "0" + day : day);
        return dbTools.selectData(time);
    }

    public List<HealthSport> getMonthHealthSport(int year, int month) {
        String time = "" + year + (month < 10 ? "0" + month : month);
        return dbTools.selectData(time);
    }

    public List<HealthSport> getYearHealthSport(int year) {
        return dbTools.selectData("" + year);
    }

    public List<HealthSport> getAllHealthSport() {
        return dbTools.selectAllData();
    }

//    public void enterCamera() {
//        byte[] cmd = new byte[20];
//        cmd[0] = 0x07;
//        cmd[1] = 0x01;
//        cmd[2] = 0x07;
//        for (int i = 3; i < 20; i++) {
//            cmd[i] = Constant.RESERVED;
//        }
//        write(cmd);
//    }
//
//    public void outCamera() {
//        byte[] cmd = new byte[20];
//        cmd[0] = 0x07;
//        cmd[1] = 0x01;
//        cmd[2] = 0x08;
//        for (int i = 3; i < 20; i++) {
//            cmd[i] = Constant.RESERVED;
//        }
//        write(cmd);
//    }

    public static <T> Object[] concat(Object[] first, Object[] second) {
        Object[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }


    public static <T> byte[] concat(byte[] first, byte[] second) {
        byte[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    public static int Crc16Calc(byte[] data_arr, int data_len)
    {
        char crc16 =  0xFFFF;
        for(int i =0; i < data_len; i++)
        {
            crc16 = (char) (( crc16 >> 8) | (crc16 << 8));

//            System.out.println("i1:"+i+"\t"+String.format("%02X",(int)crc16));
            if (data_arr[i] < 0) {
                crc16 ^= (int) data_arr[i] + 256; // XOR byte into least sig. byte of
                // crc
            } else {
                crc16 ^= (int) data_arr[i]; // XOR byte into least sig. byte of crc
            }
//            crc16 ^= data_arr[i];
//            System.out.println("i2:"+i+"\t"+String.format("%02X",(int)crc16));
            crc16 ^= (char)(( crc16 & 0xFF) >> 4);
            crc16 ^= (char)(( crc16 << 8) << 4);
            crc16 ^= (char)((( crc16 & 0xFF) << 4) << 1);
        }
        return crc16;
    }


}

