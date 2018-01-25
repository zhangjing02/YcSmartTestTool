package com.czjk.bleDemo.ui.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.czjk.bleDemo.R;
import com.czjk.bleDemo.base.BaseFragment;
import com.czjk.bleDemo.model.Sleep;
import com.czjk.bleDemo.model.SleepItem;
import com.czjk.bleDemo.service.DfuService;
import com.czjk.blelib.model.Alarm;
import com.czjk.blelib.model.BaseEvent;
import com.czjk.blelib.model.DoNotDisturb;
import com.czjk.blelib.model.HealthSport;
import com.czjk.blelib.model.LongSit;
import com.czjk.blelib.model.Unit;
import com.czjk.blelib.utils.BleLog;
import com.czjk.blelib.utils.Constant;
import com.czjk.blelib.utils.HexUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import cn.senssun.ble.sdk.BleScan;
import no.nordicsemi.android.dfu.DfuProgressListener;
import no.nordicsemi.android.dfu.DfuServiceInitiator;
import no.nordicsemi.android.dfu.DfuServiceListenerHelper;
import senssun.ycblelib.YCProtocolUtils;
import senssun.ycblelib.YCViseBluetooth;

import static com.czjk.blelib.utils.Constant.KEY_BIND;
import static com.czjk.blelib.utils.Constant.KEY_BLOOD_CALIBRATION;
import static com.czjk.blelib.utils.Constant.KEY_CONTROL_CAMERAMUSIC;
import static com.czjk.blelib.utils.Constant.KEY_DISCOUNT_BLE;
import static com.czjk.blelib.utils.Constant.KEY_FIND_BLE;
import static com.czjk.blelib.utils.Constant.KEY_FIND_PHONE;
import static com.czjk.blelib.utils.Constant.KEY_FINISH_SYNC;
import static com.czjk.blelib.utils.Constant.KEY_GET_ALARM;
import static com.czjk.blelib.utils.Constant.KEY_GET_BLOOD;
import static com.czjk.blelib.utils.Constant.KEY_GET_BLOODTHRESHOLD;
import static com.czjk.blelib.utils.Constant.KEY_GET_DONOTDISTURB;
import static com.czjk.blelib.utils.Constant.KEY_GET_ELECTRICITY;
import static com.czjk.blelib.utils.Constant.KEY_GET_FW;
import static com.czjk.blelib.utils.Constant.KEY_GET_HANDCHANGE;
import static com.czjk.blelib.utils.Constant.KEY_GET_HEART_MODE;
import static com.czjk.blelib.utils.Constant.KEY_GET_LIVE_DATA;
import static com.czjk.blelib.utils.Constant.KEY_GET_LONG_SIT;
import static com.czjk.blelib.utils.Constant.KEY_GET_MAC;
import static com.czjk.blelib.utils.Constant.KEY_GET_MEASURE_HR;
import static com.czjk.blelib.utils.Constant.KEY_GET_NOTICE;
import static com.czjk.blelib.utils.Constant.KEY_GET_PPG;
import static com.czjk.blelib.utils.Constant.KEY_GET_RAISE;
import static com.czjk.blelib.utils.Constant.KEY_GET_RR;
import static com.czjk.blelib.utils.Constant.KEY_GET_SLEEPRECORD;
import static com.czjk.blelib.utils.Constant.KEY_GET_SPORTRECORD;
import static com.czjk.blelib.utils.Constant.KEY_GET_SYNC;
import static com.czjk.blelib.utils.Constant.KEY_GET_TARGET;
import static com.czjk.blelib.utils.Constant.KEY_GET_TIME;
import static com.czjk.blelib.utils.Constant.KEY_GET_UNIT;
import static com.czjk.blelib.utils.Constant.KEY_GET_USERINFO;
import static com.czjk.blelib.utils.Constant.KEY_OTA;
import static com.czjk.blelib.utils.Constant.KEY_REJECT_PHONE;
import static com.czjk.blelib.utils.Constant.KEY_RESET;
import static com.czjk.blelib.utils.Constant.KEY_SENDMESS_BLE;
import static com.czjk.blelib.utils.Constant.KEY_SEND_SOS;
import static com.czjk.blelib.utils.Constant.KEY_SET_ALARM;
import static com.czjk.blelib.utils.Constant.KEY_SET_ALARM_NAME;
import static com.czjk.blelib.utils.Constant.KEY_SET_BLOOD_THRESHOLD;
import static com.czjk.blelib.utils.Constant.KEY_SET_CAMERA;
import static com.czjk.blelib.utils.Constant.KEY_SET_DONOTDISTURB;
import static com.czjk.blelib.utils.Constant.KEY_SET_HANDCHANGE;
import static com.czjk.blelib.utils.Constant.KEY_SET_HEART_MODE;
import static com.czjk.blelib.utils.Constant.KEY_SET_LONG_SIT;
import static com.czjk.blelib.utils.Constant.KEY_SET_NOTICE;
import static com.czjk.blelib.utils.Constant.KEY_SET_SCREEN_MODE;
import static com.czjk.blelib.utils.Constant.KEY_SET_TARGET;
import static com.czjk.blelib.utils.Constant.KEY_SET_TIME;
import static com.czjk.blelib.utils.Constant.KEY_SET_UP_HAND_GESTURE;
import static com.czjk.blelib.utils.Constant.KEY_SET_USER_INFO;
import static com.czjk.blelib.utils.Constant.KEY_SET_USER_UNIT;
import static com.czjk.blelib.utils.Constant.KEY_TRANSPORT;
import static com.czjk.blelib.utils.Constant.KEY_UNBIND;
import static com.czjk.blelib.utils.Constant.RECORD_TYPE_PLAYBALL;
import static com.czjk.blelib.utils.Constant.RECORD_TYPE_RIDING;
import static com.czjk.blelib.utils.Constant.RECORD_TYPE_ROPESKIPPING;
import static com.czjk.blelib.utils.Constant.RECORD_TYPE_RUNNING;
import static senssun.ycblelib.YCViseBluetooth.arrToString;
import static senssun.ycblelib.YCViseBluetooth.logPath;
import static senssun.ycblelib.db.DBTools.dbTools;


public class HomeFragment extends BaseFragment {
    BleScan bleScan=new BleScan();
    @BindView(R.id.tv1)TextView tv1;
    @BindView(R.id.tv2)TextView tv2;
    private DeviceRssiThread deviceRssiThread;
    private ProgressDialog progressDialog;
    private String mst_str="这次事件肯定没有完，林sir等KOL的公信力大大下降，对于以此为生自媒体就是断了财路，" +
            "所以肯定会有动作。另外flow一个月后的改版能让声音提升到哪个价位也值得期待，个人认为要做到和gr07一个档次，" +
            "成本绝对不低，魅族是没有赚头的，一般来说不会做这种亏本生意；要是出了逊gr07一头的耳机，那KOL就再也起不来";

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View home = inflater.inflate(R.layout.fragment_home, null);
        ButterKnife.bind(this, home);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }


        YCProtocolUtils.viseBluetooth.setOnServiceDisplayDATA(new YCViseBluetooth.OnServiceDisplayDATA() {
            @Override
            public void OnDATA(Bundle bundle) {
//                getBleXXData(data);
            }
        });
        return home;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @OnClick({R.id.buttonReset,R.id.buttonTransport,R.id.button1, R.id.button2, R.id.button3,R.id.buttonMac, R.id.button4, R.id.button5,
            R.id.button6,R.id.buttonGetSleepRecord, R.id.button7, R.id.button8,R.id.buttonBloodOpen,R.id.buttonBloodClose,
            R.id.buttonNotice, R.id.button10,
            R.id.button11, R.id.button12, R.id.button13, R.id.button14, R.id.button15,
            R.id.button16, R.id.buttonPPG,R.id.buttonRR, R.id.buttonCloseRR, R.id.buttonBloodWarning,R.id.buttonTarget,R.id.buttonGETHANDCHANGE,R.id.buttonGETSPORTRECORD,
            R.id.button17, R.id.button18, R.id.button19, R.id.button20,R.id.buttonBritishSystem,
            R.id.button21, R.id.button22, R.id.buttonScreenOpen,R.id.buttonScreenClose , R.id.button24,
            R.id.buttonNoticeSwitch,R.id.buttonBind,R.id.buttonAlarmName,
            R.id.buttonCameraOpen, R.id.buttonCameraClose,
            R.id.buttonScreen,R.id.buttonBloodThreshold,R.id.buttonSetTarget,
            R.id.buttonSetHANDCHANGE,R.id.buttonSetHANDCHANGE2,
            R.id.button25,R.id.buttonDiscountBle,R.id.buttonBloodCalibration,
            R.id.button26, R.id.button27, R.id.button28, R.id.button29, R.id.button30,
            R.id.button31, R.id.button32, R.id.button33, R.id.button34, R.id.button35,
            R.id.button41,R.id.button45,R.id.button46,R.id.button47})
    @Override
    public void onClick(View v) {
        if (!YCProtocolUtils.getInstance().isConnDevice()) {
            Toast.makeText(mContext, "设备没有连接", Toast.LENGTH_SHORT).show();
            return;
        }
//        Calendar now = Calendar.getInstance();
//        int month = now.get(Calendar.MONTH) + 1;
//        int day = now.get(Calendar.DAY_OF_MONTH);
//        int hour = now.get(Calendar.HOUR_OF_DAY);
//        int minute = now.get(Calendar.MINUTE);
//        int second = now.get(Calendar.SECOND);
//        int year = now.get(Calendar.YEAR);
//        int todayWeek = now.get(Calendar.DAY_OF_WEEK) - 2;
//        if (todayWeek == -1) {
//            todayWeek = 6;
//        }
        switch (v.getId()) {
            case R.id.buttonReset://Command Id:0x01,Key:0x02 APP向手环请求恢复出厂设置
                YCProtocolUtils.getInstance().RESET();
                break;
            case R.id.buttonTransport://Command Id:0x01,Key:0x03 APP向手环发送开启运输模式命令
                YCProtocolUtils.getInstance().TRANSPORT();
                break;
            case R.id.button1:  //2.1.1	Command Id:0x01,Key:0x01 APP向手环请求空中升级
//            {
//
//                UUID SERVICE_UUID = UUID.fromString("00000af0-0000-1000-8000-00805f9b34fb");
//                UUID CHARACTERISTIC_COMMANDNOTIFY_UUID = UUID.fromString("00000af7-0000-1000-8000-00805f9b34fb");
//
//                BluetoothGatt gatt = YCProtocolUtils.getInstance().viseBluetooth.getBluetoothGatt();
//
//                BluetoothGattCharacteristic Characteristic = gatt.getService(SERVICE_UUID).getCharacteristic(CHARACTERISTIC_COMMANDNOTIFY_UUID);
//
//                boolean massDataSuccess = YCProtocolUtils.getInstance().viseBluetooth.setCharacteristicNotification(Characteristic, true);
//                Toast.makeText(getActivity(), "isOpen" + massDataSuccess, Toast.LENGTH_SHORT).show();
//            }
                YCProtocolUtils.getInstance().OTA();
                break;

            case R.id.button2:  //2.2.1	Command Id:0x02,Key:0x01 APP获取手环版本信息
//            {
//                UUID SERVICE_UUID = UUID.fromString("00000af0-0000-1000-8000-00805f9b34fb");
//                UUID CHARACTERISTIC_MASSDATANOTIFY_UUID = UUID.fromString("00000af2-0000-1000-8000-00805f9b34fb");
//
//                BluetoothGatt gatt = YCProtocolUtils.getInstance().getBluetoothGatt();
//
//                BluetoothGattCharacteristic massDataCharacteristic = gatt.getService(SERVICE_UUID).getCharacteristic(CHARACTERISTIC_MASSDATANOTIFY_UUID);
//
//
//                boolean massDataSuccess = YCProtocolUtils.getInstance().viseBluetooth.setCharacteristicNotification(massDataCharacteristic, true);
//                Toast.makeText(getActivity(), "isOpen" + massDataSuccess, Toast.LENGTH_SHORT).show();
//
//            }
                YCProtocolUtils.getInstance().getFW();
                break;

            case R.id.button3:  //2.2.2	Command Id:0x02,Key:0x02 APP获取手环系统时间
                YCProtocolUtils.getInstance().getTime();
                break;

            case R.id.buttonMac:  //2.2.3	Command Id:0x02,Key:0x03 APP获取手环MAC地址
                YCProtocolUtils.getInstance().getMac();
                break;
            case R.id.button4:  //2.2.4	Command Id:0x02,Key:0x04 APP获取手环电量信息
                YCProtocolUtils.getInstance().getElectricity();
                break;

            case R.id.button5:  //2.2.5  Command Id:0x02,Key:0x05 APP获取手环实时数据
                YCProtocolUtils.getInstance().getLiveData();
                break;

            case R.id.button6:  //2.2.6	Command Id:0x02,Key:0x06 APP获取手环历史数据
                Calendar beginCalendar=Calendar.getInstance();
                Calendar endCalendar=Calendar.getInstance();
                beginCalendar.add(Calendar.DAY_OF_MONTH,-3);
                YCProtocolUtils.getInstance().getHisData(beginCalendar,endCalendar);
                break;

            case R.id.buttonGetSleepRecord:  //2.2.6	Command Id:0x02,Key:0x06 APP获取手环睡眠历史数据
//                Calendar beginCalendar=Calendar.getInstance();
//                Calendar endCalendar=Calendar.getInstance();
//                beginCalendar.add(Calendar.DAY_OF_MONTH,-3);
                YCProtocolUtils.getInstance().getSleepsData();
                break;
            case R.id.button7:  //2.2.7	Command Id:0x02,Key:0x07 APP获取手环实时心率
                YCProtocolUtils.getInstance().setHeartRate(true);
                break;

            case R.id.button8:  //关闭实时心率测量
                YCProtocolUtils.getInstance().setHeartRate(false);
                break;

            case R.id.buttonBloodOpen:  //2.2.8	Command Id:0x02,Key:0x08 APP获取手环实时血压
                YCProtocolUtils.getInstance().setBlood(true);
                break;

            case R.id.buttonBloodClose:  //2.2.8	Command Id:0x02,Key:0x08 APP获取手环实时血压
                YCProtocolUtils.getInstance().setBlood(false);
                break;

            case R.id.buttonNotice:// Command Id:0x02,Key:0x09 APP获取手环通知提醒开关状态
                YCProtocolUtils.getInstance().setNotice();
                break;
            case R.id.button10:  //Command Id:0x02,Key:0x0A APP获取手环闹钟设置状态
                YCProtocolUtils.getInstance().getAlarm(0);  //0-7  闹钟id
                break;

            case R.id.button11:  //Command Id:0x02,Key:0x0B APP获取手环用户信息
                YCProtocolUtils.getInstance().getUserInfo();
                break;

            case R.id.button12:  //Command Id:0x02,Key:0x0C APP获取手环测量单位公英制
                YCProtocolUtils.getInstance().getUnit();
                break;

            case R.id.button13:  //Command Id:0x02,Key:0x0D APP获取手环久坐提醒配置
                YCProtocolUtils.getInstance().getLongSit();
                break;

            case R.id.button14:  // Command Id:0x02,Key:0x0E APP获取手环心率监测模式
                YCProtocolUtils.getInstance().getHeartRateMode();
                break;

            case R.id.button15:  //Command Id:0x02,Key:0x0F APP获取手环抬腕开关状态
                YCProtocolUtils.getInstance().getUPHandGesture();
                break;

            case R.id.button16:  //Command Id:0x02,Key:0x10 APP获取手环勿扰模式开关状态
                YCProtocolUtils.getInstance().getDoNotDisturb();
                break;

            case R.id.buttonPPG:  //Command Id:0x02,Key:0x11 APP获取手环实时PPG
                YCViseBluetooth.isWrite=true;
                YCProtocolUtils.getInstance().getPPG(true);
                YCViseBluetooth.logPath = Environment.getExternalStorageDirectory().getAbsolutePath() +"/CurrentPPG/";
                YCViseBluetooth.fileName = logPath + "/log_" + YCViseBluetooth.dateFormat.format(new Date()) + ".log";//log日志名，使用时间命名，保证不重复
                break;
            case R.id.buttonRR:  //Command Id:0x02,Key:0x12 APP获取手环实时RR间隔
                YCProtocolUtils.getInstance().getRR(true);
                break;

            case R.id.buttonCloseRR:  //Command Id:0x02,Key:0x12 APP获取手环实时RR间隔
                YCProtocolUtils.getInstance().getRR(false);
                break;

            case R.id.buttonBloodWarning://2.2.19	 Command Id:0x02,Key:0x13 APP获取手环血压预警阈值
                YCProtocolUtils.getInstance().getBloodThreshold();
                break;

            case R.id.buttonTarget://2.2.20	 Command Id:0x02,Key:0x14 APP获取手环运动及睡眠目标
                //type目标类型: 0步数  1卡路里  2距离  3睡眠时间
                YCProtocolUtils.getInstance().getTarget(0);
                break;

            case R.id.buttonGETHANDCHANGE://Command Id:0x02,Key:0x15 APP获取手环左右手佩戴配置
                YCProtocolUtils.getInstance().getHandChange();
                break;

            case R.id.buttonGETSPORTRECORD://Command Id:0x02,Key:0x15 APP获取手环的运动记录
                YCProtocolUtils.getInstance().getSportRecord();
                break;

            case R.id.button17: {  //Command Id:0x03,Key:0x01 APP设置手环系统时间

                Calendar now = Calendar.getInstance();
                int year = now.get(Calendar.YEAR);
                int month = now.get(Calendar.MONTH) + 1;
                int day = now.get(Calendar.DAY_OF_MONTH);
                int hour = now.get(Calendar.HOUR_OF_DAY);
                int minute = now.get(Calendar.MINUTE);
                int second = now.get(Calendar.SECOND);

                YCProtocolUtils.getInstance().setTime(year - 2000, month, day, hour, minute, second);
            }break;

            case R.id.button18: {  //Command Id:0x03,Key:0x02 APP设置手环闹钟
//                int alarmId = 2;//Alarm_id(0-7)
//                int status = 1; //0:关闭闹钟,1:开启闹钟,2:删除闹钟
//                int Hour = 21;//0-23
//                int Minute = 33;//0-59



                new Thread() {
                    public void run() {
                        for (int i=0;i<8;i++) {
                            int alarmId = i;//Alarm_id(0-7)
                            int status = 1; //0:关闭闹钟,1:开启闹钟,2:删除闹钟
                            int Hour = 21;//0-23
                            int Minute = 48+i ;//0-59

                            Alarm alarm = new Alarm(alarmId, "备注", Hour + ":" + Minute, "11111110", status);
                            YCProtocolUtils.getInstance().setAlarm(alarm);

                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }.start();

            }break;

            case R.id.button19:  // Command Id:0x03,Key:0x03 APP设置手环用户信息
                int Height=160;//单位cm
                int Weight=52;//(单位kg)
                int sex=0;//0:女,1:男
                int age=26;//年龄
                int Stride=0;//步幅
                if(sex==0){
                    Stride = (int) (0.413 *Height);
                }else{
                    Stride = (int) (0.415 *Height);
                }
                YCProtocolUtils.getInstance().setUserInfo(age, Height, Weight, sex,Stride);
                break;

            case R.id.button20: {  //Command Id:0x03,Key:0x04 APP设置手环测量单位公英制
                int distanceUnit = 0;//距离长度单位(0:公制,1:英制)
                int timeUnit = 0;//时间制式(0:24小时制,1:12小时制)
                YCProtocolUtils.getInstance().setUnit(new Unit(distanceUnit, timeUnit));
            }break;

            case R.id.buttonBritishSystem: {  //Command Id:0x03,Key:0x04 APP设置手环测量单位公英制
                int distanceUnit = 1;//距离长度单位(0:公制,1:英制)
                int timeUnit = 1;//时间制式(0:24小时制,1:12小时制)
                YCProtocolUtils.getInstance().setUnit(new Unit(distanceUnit, timeUnit));
            }break;


            case R.id.button21:  //Command Id:0x03,Key:0x05 APP设置手环久坐提醒配置
                // 11111110       Bit1~Bit7: 周一~周日是否重复
                int Start_hour=9;//开始小时
                int Start_minute=20;//开始分钟
                int End_hour=22;//结束小时
                int End_minute=20;//结束分钟
                int interval=15;//提醒间隔(低字节在前)
                YCProtocolUtils.getInstance().setLongSit(new LongSit(Start_hour, Start_minute, End_hour, End_minute, interval, "11111110"));
                break;

            case R.id.button22:  //Command Id:0x03,Key:0x06 APP设置手环心率监测模式
                YCProtocolUtils.getInstance().setHeartRateAuto(false);
                break;

            case R.id.buttonScreenOpen:  // Command Id:0x03,Key:0x07 APP设置手环抬腕开关状态
                YCProtocolUtils.getInstance().setUPHandGesture(true);
                break;

            case R.id.buttonScreenClose:  // Command Id:0x03,Key:0x07 APP设置手环抬腕开关状态
                YCProtocolUtils.getInstance().setUPHandGesture(false);
                break;

            case R.id.button24:  //Command Id:0x03,Key:0x08 APP设置手环勿扰模式开关状态
                int start_hour=22;  //开始时间时0-23
                int start_minute=0; //开始时间分0-59
                int end_hour=7;     //结束时间时0-23
                int end_minute=20;  //结束时间分0-59
                YCProtocolUtils.getInstance().setDoNotDisturb(new DoNotDisturb(start_hour, start_minute, end_hour, end_minute, true));
                break;

            case R.id.buttonNoticeSwitch:  //Command Id:0x03,Key:0x09 APP设置手环通知提醒开关状态
                /*
                 notify_item0各个Bit定义如下
                    Bit0	短信(0:关,1:开)
                    Bit1	来电(0:关,1:开)
                    Bit2	邮件(0:关,1:开)
                    Bit3	微信(0:关,1:开)
                    Bit4	QQ(0:关,1:开)
                    Bit5	Facebook(0:关,1:开)
                    Bit6	Twitter(0:关,1:开)
                    Bit7	WhatsApp(0:关,1:开)
                 */

                boolean noticeSwitch=true;//通知提醒总开关:notify_switch,0x00:总开关关,0x01:总开关开
                String noticeItem0="11111111";//提醒子开关0:notify_item0
                String noticeItem1="11111111";//提醒子开关1:notify_item1
                int call_delay=1;//来电延时:单位秒
                YCProtocolUtils.getInstance().setNoticeSwitch(noticeSwitch,noticeItem0,noticeItem1,call_delay);
                break;

            case R.id.buttonAlarmName: {
                int alarmId = 2;//Alarm_id(0-7)
                String alarmName="测试1";
                YCProtocolUtils.getInstance().setAlarmName(alarmId,alarmName);
            }
            break;

            case R.id.buttonCameraOpen:{//Command Id:0x03,Key:0x0B APP设置手环相机控制状态
                YCProtocolUtils.getInstance().setCamera(true);
            }
            break;

            case R.id.buttonCameraClose:{//Command Id:0x03,Key:0x0B APP设置手环相机控制状态
                YCProtocolUtils.getInstance().setCamera(false);
            }
            break;

            case R.id.buttonScreen:{//Command Id:0x03,Key:0x0C APP设置手环横竖屏显示方式
                YCProtocolUtils.getInstance().setScreen(true);
            }
            break;
            case R.id.buttonBloodThreshold:{//Command Id:0x03,Key:0x0D APP设置手环血压预警阈值
                int highVoltage=120;
                YCProtocolUtils.getInstance().setBloodThreshold(highVoltage);
            }
            break;
            case R.id.buttonSetTarget://Command Id:0x03,Key:0x0E APP设置手环运动及睡眠目标
                int type=0;//Type(目标类型) 0:步数 1:卡路里 2:距离 3:睡眠时间
                int target=100; //目标值(低字节在前)
                YCProtocolUtils.getInstance().setTarget(type,target);
                break;

            case R.id.buttonSetHANDCHANGE: {//Command Id:0x03,Key:0x0F APP设置手环左右手佩戴配置
                int WearingMode = 0;//0:左手,1:右手
                YCProtocolUtils.getInstance().setHandChange(WearingMode);
            }break;
            case R.id.buttonSetHANDCHANGE2: {//Command Id:0x03,Key:0x0F APP设置手环左右手佩戴配置
                int WearingMode = 1;//0:左手,1:右手
                YCProtocolUtils.getInstance().setHandChange(WearingMode);
            }break;
            case R.id.buttonBind://Command Id:0x004,Key:0x01 APP绑定手环
                YCProtocolUtils.getInstance().setBind();
                break;

            case R.id.button25://Command Id:0x04,Key:0x02 APP解绑手环
                YCProtocolUtils.getInstance().setUnBind();
//                SPUtil.setStringValue(Constant.SP_KEY_DEVICE_ADDRESS, null);
//                YCProtocolUtils.getInstance().clear();
                break;

            case R.id.buttonDiscountBle://Command Id:0x04,Key:0x03 APP发送断开连接请求
                YCProtocolUtils.getInstance().setDiscountBLE();
                break;

            case R.id.buttonBloodCalibration://Command Id:0x04,Key:0x06 APP发送血压校准请求
                int highVoltage=120;
                int lowVoltage=80;

                YCProtocolUtils.getInstance().setBloodCalibration(lowVoltage,highVoltage);
                break;

            case R.id.button27: {  //来电
                char dd = '\0';
                YCProtocolUtils.getInstance().Remind(Constant.MSG_TYPE_PHONECOMING,"+8613326961012" + dd + "Call"+ dd);
            }   break;

            case R.id.button26: {  //Command Id:0x04,Key:0x04 APP发送消息推送
                char dd = '\0';
                mst_str=mst_str.substring(0,21)+"...";
                YCProtocolUtils.getInstance().Remind(Constant.MSG_TYPE_SMS, "巫一凡" + dd + mst_str + dd);
            }break;

            case R.id.button28: {  //邮件
                char dd = '\0';
                YCProtocolUtils.getInstance().Remind(Constant.MSG_TYPE_EMAIL,"测试邮件" + dd + "下班了！"+ dd);
            }   break;

            case R.id.button29: {  //微信
                char dd = '\0';
                mst_str=mst_str.substring(0,21)+"...";
                YCProtocolUtils.getInstance().Remind(Constant.MSG_TYPE_WX, "巫一凡" + dd + mst_str+ dd);
            }break;

            case R.id.button30: {  //QQ
                char dd = '\0';
                mst_str=mst_str.substring(0,21)+"...";
                YCProtocolUtils.getInstance().Remind(Constant.MSG_TYPE_QQ, "巫一凡" + dd + mst_str+ dd);
            }break;

            case R.id.button31: {  //Facebook
                char dd = '\0';

                String str="下班了！阿迪斯十大首富扫地机宽幅来";

                String content=str.length()>128?str.substring(0,128)+"...":str;
                //标题只能15个字节 1个中文2个字节 UTF-8占3个字节  内容150个字节
                YCProtocolUtils.getInstance().Remind(Constant.MSG_TYPE_FACEBOOK, "测试Facebook" + dd + content+ dd);
            }break;

            case R.id.button32: {  //Twitter
                char dd = '\0';
                String str="下班了！阿迪斯十大首富扫地机宽幅来";

                String content=str.length()>128?str.substring(0,128)+"...":str;

                YCProtocolUtils.getInstance().Remind(Constant.MSG_TYPE_TWITTER, "测试Twitter" + dd + content+ dd);
            }break;

            case R.id.button33: {  //Whatsapp
                char dd = '\0';
                YCProtocolUtils.getInstance().Remind(Constant.MSG_TYPE_WHATSAPP, "测试Whatsapp" + dd + "下班了！"+ dd);
            }break;

            case R.id.button34: {  //新浪微博
                char dd = '\0';
                YCProtocolUtils.getInstance().Remind(Constant.MSG_TYPE_SINAWEIBO, "测试新浪微博" + dd + "下班了！"+ dd);
            }break;

            case R.id.button35:  //Command Id:0x04,Key:0x05 APP发送查找手环请求
                YCProtocolUtils.getInstance().findBle();
                break;

//            case R.id.button40:  //获取心率数据
//                Heart heartData = getHeartData(year, month, day);
//                BleLog.e(heartData.toString());
//                break;

            case R.id.button41:  //清除数据库数据
                dbTools.deleteAllData();
                break;

//            case R.id.button42:  //进入拍照
//                ProtocolUtils.getInstance().enterCamera();
//                break;
//
//            case R.id.button43:  //退出拍照
//                ProtocolUtils.getInstance().outCamera();
//                break;

            case R.id.button45:
                YCProtocolUtils.getInstance().getAllAlarmRecord();
                break;
            case R.id.button46:
                YCViseBluetooth.isWrite=true;
                YCProtocolUtils.getInstance().getCurrentAcceleration();
                YCViseBluetooth.logPath = Environment.getExternalStorageDirectory().getAbsolutePath() +"/CurrentAcceleration/";
                YCViseBluetooth.fileName = logPath + "/log_" + YCViseBluetooth.dateFormat.format(new Date()) + ".log";//log日志名，使用时间命名，保证不重复
                break;
            case R.id.button47:
                YCViseBluetooth.isWrite=true;
                YCProtocolUtils.getInstance().getCurrentEcgRecord();
                YCViseBluetooth.logPath = Environment.getExternalStorageDirectory().getAbsolutePath() +"/CurrentEcg/";
                YCViseBluetooth.fileName = logPath + "/log_" + YCViseBluetooth.dateFormat.format(new Date()) + ".log";//log日志名，使用时间命名，保证不重复
                break;
        }
    }

    private void sync() {

    }

    @OnCheckedChanged({R.id.button44})
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (!YCProtocolUtils.getInstance().isConnDevice()) {
            Toast.makeText(mContext, "设备没有连接", Toast.LENGTH_SHORT).show();
            return;
        }
        switch (buttonView.getId()) {
            case R.id.button44:  //防丢提醒  也可以监听连接断开
                if (deviceRssiThread == null) {
                    deviceRssiThread = new DeviceRssiThread();
                }
                if (isChecked) {
                    deviceRssiThread.start();
                } else {
                    deviceRssiThread.isRunning = false;
                    deviceRssiThread = null;
                }
                break;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void saveData(byte[] data) {
        //[2, 8, 17, 1, 9, 1,70, 42,    2017年1月9号01点钟数据 静息心率70  数据长度42  每10分钟7个字节 6*7
        //     18, 12, 0, 0, 68, 0, 0,   18表示睡眠清醒状态    12 0 0 表示步数   68心率   0  0  血压
        //     18, 0, 0, 0, 66, 0, 0,    20  深睡  19浅睡  21 睡眠结束   17  睡眠开始  01表示运动状态
        //     20, 0, 0, 0, 68, 0, 0,
        //     20, 0, 0, 0, 69, 0, 0,
        //     20, 0, 0, 0, 68, 0, 0,
        //     19, 0, 0, 0, 66, 0, 0]
        int year = data[2] + 2000;
        int month = data[3];
        int day = data[4];
        int hour = data[5];
        int restingHeart = data[6];
        String date = "" + year + (month < 10 ? "0" + month : month) + (day < 10 ? "0" + day : day)
                + (hour < 10 ? "0" + hour : hour);
        String[] time = {"00", "10", "20", "30", "40", "50"};
        for (int i = 1; i < 7; i++) {
            int type = data[i * 7 + 1];
            byte[] d = {data[i * 7 + 2], data[i * 7 + 3], data[i * 7 + 4]};
            String s = HexUtil.bytesToHexString(d);
            String s1 = s.substring(0, 2);
            String s2 = s.substring(2, 4);
            String s3 = s.substring(4, 6);
            String sum;
            if (s3.equals("00")) {
                sum = s2 + s1;
            } else {
                sum = s3 + s2 + s1;
            }
            int step = HexUtil.hexStringToAlgorism(sum);
            int heart = data[i * 7 + 5];
            int systolic = data[i * 7 + 6];
            int diastolic = data[i * 7 + 7];
            // 每10分钟的数据  date---- 主键 201701091610
            //                step--- 步数
            //                type----  数据类型  01 步数   17-21睡眠com.czjk.blelib.model
            //                heart---- 心率值
            //                restingHeart---- 静息心率值
            //                systolic  收缩压
            //                diastolic  舒张压
            dbTools.saveData(new HealthSport(date + time[(i - 1) % 6], step, type, heart, restingHeart, systolic, diastolic));
        }
    }



    String newMac;

    private Handler mMonitorHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            new DfuServiceInitiator(newMac).setDisableNotification(true)
                    .setZip(R.raw.oled091_v08_20170719_v2)
                    .start(mContext, DfuService.class);
        }
    };




    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getBleData(final Bundle b) {

        if(b.getStringArray("Command")!=null){
            String[] data=b.getStringArray("Command");
            Log.i("BleLig", "收到数据Command"+arrToString(data));

            //regionOTA
            if(Integer.valueOf(data[0],16)==Constant.CMD_ID_DFU&&Integer.valueOf(data[1],16)==KEY_OTA){
                if(Integer.valueOf(data[4],16)==1){
                    tv1.setText("电量低于50%");
                }else{
                    tv1.setText("允许升级");
                }
            }
            //endregion
            //region手环恢复出厂设置命令
            if(Integer.valueOf(data[0],16)==Constant.CMD_ID_DFU&&Integer.valueOf(data[1],16)==KEY_RESET){
                if(Integer.parseInt(data[4],16)==0){
                    tv1.setText("手环恢复出厂设置命令开启成功");
                }else{
                    tv1.setText("手环恢复出厂设置命令开启失败");
                }
            }
            //endregion
            //region手环发送开启运输模式命令
            if(Integer.valueOf(data[0],16)==Constant.CMD_ID_DFU&&Integer.valueOf(data[1],16)==KEY_TRANSPORT){
                if(Integer.parseInt(data[4],16)==0){
                    tv1.setText("手环发送开启运输模式命令开启成功");
                }else{
                    tv1.setText("手环发送开启运输模式命令开启失败");
                }
            }
            //endregion
            //region软件版本
            if(Integer.valueOf(data[0],16)==Constant.CMD_ID_GET&&Integer.valueOf(data[1],16)==KEY_GET_FW){
                tv1.setText("软件版本:"+Integer.valueOf(data[5]+data[4],16));
            }
            //endregion
            //region手环的系统时间
            if(Integer.valueOf(data[0],16)==Constant.CMD_ID_GET&&Integer.valueOf(data[1],16)==KEY_GET_TIME){
                int year=Integer.valueOf(data[4],16)+2000;
                int month=Integer.valueOf(data[5],16);
                int day=Integer.valueOf(data[6],16);
                int hour=Integer.valueOf(data[7],16);
                int minute=Integer.valueOf(data[8],16);
                int second=Integer.valueOf(data[9],16);
                int weekDay=Integer.valueOf(data[10],16);
                tv1.setText("手环的系统时间:"+year+"-"+month+"-"+day+" "+hour+":"+minute+":"+second+" weekDay"+weekDay);
            }
            //endregion
            //region手环的MAC
            if(Integer.valueOf(data[0],16)==Constant.CMD_ID_GET&&Integer.valueOf(data[1],16)==KEY_GET_MAC){
                tv1.setText("手环的MAC:"+data[9]+":"+data[8]+":"+data[7]+":"+data[6]+":"+data[5]+":"+data[4]);
            }
            //endregion
            //region手环的电量
            if(Integer.valueOf(data[0],16)==Constant.CMD_ID_GET&&Integer.valueOf(data[1],16)==KEY_GET_ELECTRICITY){
                tv1.setText("手环的电量:"+Integer.valueOf(data[4],16));
            }
            //endregion
            //region手环实时数据
            if(Integer.valueOf(data[0],16)==Constant.CMD_ID_GET&&Integer.valueOf(data[1],16)==KEY_GET_LIVE_DATA){
                int step=Integer.valueOf(data[6]+data[5]+data[4],16);
                int heart=Integer.valueOf(data[7],16);
                int SBP=Integer.valueOf(data[8],16);
                int DBP=Integer.valueOf(data[9],16);
                int distance=Integer.valueOf(data[12]+data[11]+data[10],16);
                int kcal=Integer.valueOf(data[15]+data[14]+data[13],16);
                tv1.setText("手环实时数据:"+"step:"+step +" heart:"+heart+" SBP:"+SBP+" DBP:"+DBP+" Distance:"+distance+" Kcal:"+kcal);
            }
            //endregion
            //region同步历史
            if(Integer.valueOf(data[0],16)==Constant.CMD_ID_GET&&Integer.valueOf(data[1],16)==KEY_GET_SYNC){ //同步历史
                switch (data[4]){
                    case"00":
                        tv1.setText("手环同步历史 成功");
                        break;
                    case "01":
                        tv1.setText("手环同步历史 失败");
                        break;
                    case "FF":
                        tv1.setText("手环同步历史 结束");
                        break;

                }
            }
            //endregion
            //region同步睡眠历史
            if(Integer.valueOf(data[0],16)==Constant.CMD_ID_GET&&Integer.valueOf(data[1],16)==KEY_GET_SLEEPRECORD){ //同步睡眠历史
                switch (data[4]){
                    case"00":
                        tv1.setText("手环同步睡眠历史 成功");
                        break;
                    case "01":
                        tv1.setText("手环同步睡眠历史 失败");
                        break;
                    case "FF":
                        tv1.setText("手环同步睡眠历史 结束");
                        break;
                }
            }
            //endregion
            //region实时心率监测
            if(Integer.valueOf(data[0],16)==Constant.CMD_ID_GET&&Integer.valueOf(data[1],16)==KEY_GET_MEASURE_HR){
                if(Integer.parseInt(data[4],16)==0){
                    tv1.setText("开启实时心率监测");
                }else{
                    tv1.setText("关闭实时心率监测");
                }
            }
            //endregion
            //region实时血压监测
            if(Integer.valueOf(data[0],16)==Constant.CMD_ID_GET&&Integer.valueOf(data[1],16)==KEY_GET_BLOOD){
                if(Integer.parseInt(data[4],16)==0){
                    tv1.setText("开启实时血压监测");
                }else{
//                02 08 02 00 78 4E EF 42
                    tv1.setText("关闭实时血压监测");
                }
            }
            //endregion
            //region通知提醒总开关
            if(Integer.valueOf(data[0],16)==Constant.CMD_ID_GET&&Integer.valueOf(data[1],16)==KEY_GET_NOTICE){
                String notify_item0=String.format("%08d", Integer.valueOf(Integer.toBinaryString(Integer.parseInt(data[5],16))));
//            Integer.toBinaryString(Integer.parseInt(data[5],16));
                String notify_item1=String.format("%08d", Integer.valueOf(Integer.toBinaryString(Integer.parseInt(data[6],16))));
//            Integer.toBinaryString(Integer.parseInt(data[6],16));
                int call_delay=Integer.parseInt(data[7],16);

                if(Integer.parseInt(data[4],16)==0){
                    tv1.setText("关闭通知提醒总开关"+"item0:"+notify_item0+" item1:"+notify_item1+" delay:"+call_delay);
                }else{
                    tv1.setText("开启通知提醒总开关"+"item0:"+notify_item0+" item1:"+notify_item1+" delay:"+call_delay);
                }
            }
            //endregion
            //region手环闹钟设置状态
            if(Integer.valueOf(data[0],16)==Constant.CMD_ID_GET&&Integer.valueOf(data[1],16)==KEY_GET_ALARM){
                int Alarm_id=Integer.parseInt(data[4],16);//0-7

                int Status=Integer.parseInt(data[5],16);//0:关闭,1:开启,2,删除
                int Hour=Integer.parseInt(data[6],16);//0-23
                int Minute=Integer.parseInt(data[7],16);//0-59

                String Repeat=String.format("%08d", Integer.valueOf(Integer.toBinaryString(Integer.parseInt(data[8],16))));

                tv1.setText("手环闹钟设置状态"+"id:"+Alarm_id+" Status:"+Status+" Hour:"+Hour +" Minute:"+Minute+" Repeat:"+Repeat);
            }
            //endregion
            //region手环用户信息
            if(Integer.valueOf(data[0],16)==Constant.CMD_ID_GET&&Integer.valueOf(data[1],16)==KEY_GET_USERINFO){
                int age=Integer.parseInt(data[4],16);

                int Height=Integer.parseInt(data[5],16);
                int Weight=Integer.parseInt(data[6],16);
                int Gender=Integer.parseInt(data[7],16);//0:女,1:男
                int Stride=Integer.parseInt(data[8],16);//cm

                tv1.setText("手环用户信息"+"age:"+age+" Height:"+Height+" Weight:"+Weight +" Gender:"+Gender+" Stride:"+Stride);
            }
            //endregion
            //region 手环测量单位公英制
            if(Integer.valueOf(data[0],16)==Constant.CMD_ID_GET&&Integer.valueOf(data[1],16)==KEY_GET_UNIT){
                int disUnit=Integer.parseInt(data[4],16);//距离长度单位(0:公制,1:英制)
                int timeUnit=Integer.parseInt(data[5],16);//时间制式(0:24小时制,1:12小时制)

                tv1.setText("手环测量单位公英制"+"disUnit:"+disUnit+" timeUnit:"+timeUnit);
            }
            //endregion
            //region手环久坐提醒配置
            if(Integer.valueOf(data[0],16)==Constant.CMD_ID_GET&&Integer.valueOf(data[1],16)==KEY_GET_LONG_SIT){
                int Start_hour=Integer.parseInt(data[4],16);
                int Start_minute=Integer.parseInt(data[5],16);
                int End_hour=Integer.parseInt(data[6],16);
                int End_minute=Integer.parseInt(data[7],16);
                int Remind_interval=Integer.parseInt(data[9]+data[8],16);

                String Repeat=String.format("%08d", Integer.valueOf(Integer.toBinaryString(Integer.parseInt(data[10],16))));

                tv1.setText("手环久坐提醒配置"+"Start_hour:"+Start_hour+" Start_minute:"+Start_minute+" End_hour:"+End_hour+" End_minute:"+End_minute+" Remind_interval:"+Remind_interval+" Repeat:"+Repeat);
            }
            //endregion
            //region手环心率监测模式
            if(Integer.valueOf(data[0],16)==Constant.CMD_ID_GET&&Integer.valueOf(data[1],16)==KEY_GET_HEART_MODE){
                if(Integer.parseInt(data[4],16)==0){
                    tv1.setText("手环心率监测模式 关闭");
                }else{
                    tv1.setText("手环心率监测模式 开启");
                }
            }
            //endregion
            //region手环抬腕开关状态
            if(Integer.valueOf(data[0],16)==Constant.CMD_ID_GET&&Integer.valueOf(data[1],16)==KEY_GET_RAISE){
                if(Integer.parseInt(data[4],16)==0){
                    tv1.setText("手环抬腕开关状态 关闭");
                }else{
                    tv1.setText("手环抬腕开关状态 开启");
                }
            }
            //endregion
            //region手环勿扰模式开关状态
            if(Integer.valueOf(data[0],16)==Constant.CMD_ID_GET&&Integer.valueOf(data[1],16)==KEY_GET_DONOTDISTURB){
                int Start_hour=Integer.parseInt(data[5],16);
                int Start_minute=Integer.parseInt(data[6],16);
                int End_hour=Integer.parseInt(data[7],16);
                int End_minute=Integer.parseInt(data[8],16);

                if(Integer.parseInt(data[4],16)==0){
                    tv1.setText("手环勿扰模式开关状态 关闭");
                }else{
                    tv1.setText("手环勿扰模式开关状态 开启" + " Start_hour:"+Start_hour+" Start_minute:"+Start_minute +" End_hour:"+End_hour+" End_minute"+End_minute);
                }
            }
            //endregion
            //region手环实时PPG数据点
            if(Integer.valueOf(data[0],16)==Constant.CMD_ID_GET&&Integer.valueOf(data[1],16)==KEY_GET_PPG){
                if(Integer.parseInt(data[4],16)==0){
                    tv1.setText("手环获取实时PPG 成功");
                }else{
                    tv1.setText("手环获取实时PPG 失败");
                }
            }
            //endregion
            //region手环实时RR间隔
            if(Integer.valueOf(data[0],16)==Constant.CMD_ID_GET&&Integer.valueOf(data[1],16)==KEY_GET_RR){
                if(Integer.parseInt(data[4],16)==0){
                    tv1.setText("手环实时RR间隔 成功");
                }else{
                    tv1.setText("手环实时RR间隔 失败");
                }

//                int length=Integer.valueOf(data[3]+data[2],16);
//
//                String tmpStr = "";
//                for(int i=4;i<data.length-2;i++){
//                    tmpStr=data[i]+tmpStr;
//                }
//
//                float dd=Float.intBitsToFloat(Integer.parseInt(tmpStr,16));
//
//                tv1.setText("手环实时RR间隔:"+dd);
            }
            //endregion
            //region获取手环血压预警阈值
            if(Integer.valueOf(data[0],16)==Constant.CMD_ID_GET&&Integer.valueOf(data[1],16)==KEY_GET_BLOODTHRESHOLD){

                int highVoltage=Integer.valueOf(data[4],16);
//            int lowVoltage=Integer.valueOf(data[5],16);
                tv1.setText("获取手环血压预警阈值"+" highVoltage:"+highVoltage);
            }
            //endregion
            //region获取手环运动及睡眠目标
            if(Integer.valueOf(data[0],16)==Constant.CMD_ID_GET&&Integer.valueOf(data[1],16)==KEY_GET_TARGET){

                int type=Integer.valueOf(data[4],16);//0:步数 1:卡路里 2:距离 3:睡眠时间
                int target=Integer.valueOf(data[8]+data[7]+data[6]+data[5],16);
                tv1.setText("获取手环运动及睡眠目标"+" type:"+type+" target:"+target);
            }
            //endregion
            //region 获取手环左右手佩戴配置
            if(Integer.valueOf(data[0],16)==Constant.CMD_ID_GET&&Integer.valueOf(data[1],16)==KEY_GET_HANDCHANGE){

                if(Integer.parseInt(data[4],16)==0){
                    tv1.setText("获取手环左右手佩戴配置 左手");
                }else{
                    tv1.setText("获取手环左右手佩戴配置 右手");
                }
            }
            //endregion
            //region 获取手环的运动记录
            if(Integer.valueOf(data[0],16)==Constant.CMD_ID_GET&&Integer.valueOf(data[1],16)==KEY_GET_SPORTRECORD){

                switch (data[4]){
                    case"00":
                        tv1.setText("获取手环的运动记录 成功");
                        break;
                    case "01":
                        tv1.setText("获取手环的运动记录 失败");
                        break;
                    case "FF":
                        tv1.setText("获取手环的运动记录 结束");
                        break;

                }
            }
            //endregion
            //region APP设置手环系统时间成功
            if(Integer.valueOf(data[0],16)==Constant.CMD_ID_SET&&Integer.valueOf(data[1],16)==KEY_SET_TIME){
                if(Integer.parseInt(data[4],16)==0){
                    tv1.setText("APP设置手环系统时间成功");
                }else{
                    tv1.setText("APP设置手环系统时间失败");
                }
            }
            //endregion
            //region APP设置手环闹钟
            if(Integer.valueOf(data[0],16)==Constant.CMD_ID_SET&&Integer.valueOf(data[1],16)==KEY_SET_ALARM){

                if(Integer.parseInt(data[4],16)==0){
                    tv1.setText("APP设置手环闹钟成功");
                }else{
                    tv1.setText("APP设置手环闹钟失败");
                }

            }
            //endregion
            //regionAPP设置手环用户信息
            if(Integer.valueOf(data[0],16)==Constant.CMD_ID_SET&&Integer.valueOf(data[1],16)==KEY_SET_USER_INFO){

                if(Integer.parseInt(data[4],16)==0){//0x00:命令执行成功,0x01命令执行失败
                    tv1.setText("APP设置手环用户信息 成功");
                }else{
                    tv1.setText("APP设置手环用户信息 失败");
                }
            }
            //endregion
            //regionAPP设置手环测量单位公英制
            if(Integer.valueOf(data[0],16)==Constant.CMD_ID_SET&&Integer.valueOf(data[1],16)==KEY_SET_USER_UNIT){

                if(Integer.parseInt(data[4],16)==0){//0x00:命令执行成功,0x01命令执行失败
                    tv1.setText("APP设置手环测量单位公英制 成功");
                }else{
                    tv1.setText("APP设置手环测量单位公英制 失败");
                }
            }
            //endregion
            //regionAPP设置手环久坐提醒配置
            if(Integer.valueOf(data[0],16)==Constant.CMD_ID_SET&&Integer.valueOf(data[1],16)==KEY_SET_LONG_SIT){

                if(Integer.parseInt(data[4],16)==0){//0x00:命令执行成功,0x01命令执行失败
                    tv1.setText("APP设置手环久坐提醒配置 成功");
                }else{
                    tv1.setText("APP设置手环久坐提醒配置 失败");
                }
            }
            //endregion
            //regionAPP设置手环心率监测模式
            if(Integer.valueOf(data[0],16)==Constant.CMD_ID_SET&&Integer.valueOf(data[1],16)==KEY_SET_HEART_MODE){

                if(Integer.parseInt(data[4],16)==0){//0x00:命令执行成功,0x01命令执行失败
                    tv1.setText("APP设置手环心率监测模式 成功");
                }else{
                    tv1.setText("APP设置手环心率监测模式 失败");
                }
            }
            //endregion
            //regionAPP设置手环抬腕开关状态
            if(Integer.valueOf(data[0],16)==Constant.CMD_ID_SET&&Integer.valueOf(data[1],16)==KEY_SET_UP_HAND_GESTURE){

                if(Integer.parseInt(data[4],16)==0){//0x00:命令执行成功,0x01命令执行失败
                    tv1.setText("APP设置手环抬腕开关状态 成功");
                }else{
                    tv1.setText("APP设置手环抬腕开关状态 失败");
                }
            }
            //endregion
            //regionAPP设置手环勿扰模式开关状态
            if(Integer.valueOf(data[0],16)==Constant.CMD_ID_SET&&Integer.valueOf(data[1],16)==KEY_SET_DONOTDISTURB){

                if(Integer.parseInt(data[4],16)==0){//0x00:命令执行成功,0x01命令执行失败
                    tv1.setText("APP设置手环勿扰模式开关状态 成功");
                }else{
                    tv1.setText("APP设置手环勿扰模式开关状态 失败");
                }
            }
            //endregion
            //region APP设置手环通知提醒开关状态
            if(Integer.valueOf(data[0],16)==Constant.CMD_ID_SET&&Integer.valueOf(data[1],16)==KEY_SET_NOTICE){

                if(Integer.parseInt(data[4],16)==0){//0x00:命令执行成功,0x01命令执行失败
                    tv1.setText("APP设置手环通知提醒开关状态 成功");
                }else{
                    tv1.setText("APP设置手环通知提醒开关状态 失败");
                }
            }
            //endregion
            //region APP设置手环闹钟名称
            if(Integer.valueOf(data[0],16)==Constant.CMD_ID_SET&&Integer.valueOf(data[1],16)==KEY_SET_ALARM_NAME){

                if(Integer.parseInt(data[4],16)==0){//0x00:命令执行成功,0x01命令执行失败
                    tv1.setText("APP设置手环闹钟名称 成功");
                }else{
                    tv1.setText("APP设置手环闹钟名称 失败");
                }
            }
            //endregion
            //regionAPP设置手环相机控制状态
            if(Integer.valueOf(data[0],16)==Constant.CMD_ID_SET&&Integer.valueOf(data[1],16)==KEY_SET_CAMERA){

                if(Integer.parseInt(data[4],16)==0){//0x00:命令执行成功,0x01命令执行失败
                    tv1.setText("APP设置手环相机控制状态 成功");
                }else{
                    tv1.setText("APP设置手环相机控制状态 失败");
                }
            }
            //endregion
            //regionAPP设置手环横竖屏显示方式
            if(Integer.valueOf(data[0],16)==Constant.CMD_ID_SET&&Integer.valueOf(data[1],16)==KEY_SET_SCREEN_MODE){

                if(Integer.parseInt(data[4],16)==0){//0x00:命令执行成功,0x01命令执行失败
                    tv1.setText("APP设置手环横竖屏显示方式 成功");
                }else{
                    tv1.setText("APP设置手环横竖屏显示方式 失败");
                }
            }
            //endregion
            //regionAPP设置手环血压预警阈值
            if(Integer.valueOf(data[0],16)==Constant.CMD_ID_SET&&Integer.valueOf(data[1],16)==KEY_SET_BLOOD_THRESHOLD){

                if(Integer.parseInt(data[4],16)==0){//0x00:命令执行成功,0x01命令执行失败
                    tv1.setText("APP设置手环血压预警阈值 成功");
                }else{
                    tv1.setText("APP设置手环血压预警阈值 失败");
                }
            }
            //endregion
            //regionAPP设置手环运动及睡眠目标
            if(Integer.valueOf(data[0],16)==Constant.CMD_ID_SET&&Integer.valueOf(data[1],16)==KEY_SET_TARGET){

                if(Integer.parseInt(data[4],16)==0){//0x00:命令执行成功,0x01命令执行失败
                    tv1.setText("APP设置手环运动及睡眠目标 成功");
                }else{
                    tv1.setText("APP设置手环运动及睡眠目标 失败");
                }
            }
            //endregion
            //regionAPP设置手环左右手佩戴配置
            if(Integer.valueOf(data[0],16)==Constant.CMD_ID_SET&&Integer.valueOf(data[1],16)==KEY_SET_HANDCHANGE){

                if(Integer.parseInt(data[4],16)==0){//0x00:命令执行成功,0x01命令执行失败
                    tv1.setText("APP设置手环左右手佩戴配置 成功");
                }else{
                    tv1.setText("APP设置手环左右手佩戴配置 失败");
                }
            }
            //endregion
            //regionAPP绑定手环
            if(Integer.valueOf(data[0],16)==Constant.CMD_ID_BIND&&Integer.valueOf(data[1],16)==KEY_BIND){

                if(Integer.parseInt(data[4],16)==0){//0x00:命令执行成功,0x01命令执行失败
                    tv1.setText("APP绑定手环 成功");
                }else{
                    tv1.setText("APP绑定手环 失败");
                }
            }
            //endregion
            //regionAPP解绑手环
            if(Integer.valueOf(data[0],16)==Constant.CMD_ID_BIND&&Integer.valueOf(data[1],16)==KEY_UNBIND){

                if(Integer.parseInt(data[4],16)==0){//0x00:命令执行成功,0x01命令执行失败
                    tv1.setText("APP解绑手环 成功");
                }else{
                    tv1.setText("APP解绑手环 失败");
                }
            }
            //endregion
            //regionAPP发送断开连接请求
            if(Integer.valueOf(data[0],16)==Constant.CMD_ID_BIND&&Integer.valueOf(data[1],16)==KEY_DISCOUNT_BLE){

                if(Integer.parseInt(data[4],16)==0){//0x00:命令执行成功,0x01命令执行失败
                    tv1.setText("APP发送断开连接请求 成功");
                }else{
                    tv1.setText("APP发送断开连接请求 失败");
                }
            }
            //endregion
            //regionAPP血压校验
            if(Integer.valueOf(data[0],16)==Constant.CMD_ID_BIND&&Integer.valueOf(data[1],16)==KEY_BLOOD_CALIBRATION){

                if(Integer.parseInt(data[4],16)==0){//0x00:命令执行成功,0x01命令执行失败
                    tv1.setText("APP血压校验 成功");
                }else{
                    tv1.setText("APP血压校验 失败");
                }
            }
            //endregion
            //regionAPP发送消息推送
            if(Integer.valueOf(data[0],16)==Constant.CMD_ID_BIND&&Integer.valueOf(data[1],16)==KEY_SENDMESS_BLE){

                if(Integer.parseInt(data[4],16)==0){//0x00:命令执行成功,0x01命令执行失败
                    tv1.setText("APP发送消息推送 成功");
                }else{
                    tv1.setText("APP发送消息推送 失败");
                }
            }
            //endregion
            //regionAPP发送查找手环请求
            if(Integer.valueOf(data[0],16)==Constant.CMD_ID_BIND&&Integer.valueOf(data[1],16)==KEY_FIND_BLE){

                if(Integer.parseInt(data[4],16)==0){//0x00:命令执行成功,0x01命令执行失败
                    tv1.setText("APP发送查找手环请求 成功");
                }else{
                    tv1.setText("APP发送查找手环请求 失败");
                }
            }
            //endregion
            //region 手环主动向APP发送查找手机请求
            if(Integer.valueOf(data[0],16)==Constant.CMD_ID_APP_CONTROL&&Integer.valueOf(data[1],16)==KEY_FIND_PHONE){
                tv1.setText("手环主动向APP发送查找手机请求");
            }
            //endregion
            //region 手环主动向APP发送音乐相机控制命令
            if(Integer.valueOf(data[0],16)==Constant.CMD_ID_APP_CONTROL&&Integer.valueOf(data[1],16)==KEY_CONTROL_CAMERAMUSIC){
                switch (Integer.parseInt(data[4],16)){
                    case 1:
                        tv1.setText("手环主动向APP发送音乐相机控制命令 播放音乐");
                        break;
                    case 2:
                        tv1.setText("手环主动向APP发送音乐相机控制命令 暂停音乐");
                        break;
                    case 3:
                        tv1.setText("手环主动向APP发送音乐相机控制命令 上一曲");
                        break;
                    case 4:
                        tv1.setText("手环主动向APP发送音乐相机控制命令 下一曲");
                        break;
                    case 5:
                        tv1.setText("手环主动向APP发送音乐相机控制命令 拍照");
                        break;
                    case 6:
                        tv1.setText("手环主动向APP发送音乐相机控制命令 进入拍照");
                        break;
                    case 7:
                        tv1.setText("手环主动向APP发送音乐相机控制命令 退出拍照");
                        break;
                }
            }
            //endregion
            //region 手环主动向APP发送SOS请求
            if(Integer.valueOf(data[0],16)==Constant.CMD_ID_APP_CONTROL&&Integer.valueOf(data[1],16)==KEY_SEND_SOS){
                tv1.setText("手环主动向APP发送SOS请求");
            }
            //endregion
            //region 手环向APP发送拒绝接听来电请求
            if(Integer.valueOf(data[0],16)==Constant.CMD_ID_APP_CONTROL&&Integer.valueOf(data[1],16)==KEY_REJECT_PHONE){
                tv1.setText("手环向APP发送拒绝接听来电请求");
            }
            //endregion
            //region手环向APP发送数据传输完成通知命令
            if(Integer.valueOf(data[0],16)==Constant.CMD_ID_APP_CONTROL&&Integer.valueOf(data[1],16)==KEY_FINISH_SYNC){
                tv1.setText("手环向APP发送数据传输完成通知命令");
            }
            //endregion

        }else if(b.getStringArray("Mess")!=null){
            String[] data=b.getStringArray("Mess");
            Log.i("BleLig", "收到数据Mess"+arrToString(data));

            //region实时心率监测
            if(Integer.valueOf(data[0],16)==Constant.CMD_ID_GET&&Integer.valueOf(data[1],16)==KEY_GET_MEASURE_HR){
                tv2.setText("实时心率监测 心率："+Integer.valueOf(data[4],16));
            }
            //endregion
            //region实时血压监测
            if(Integer.valueOf(data[0],16)==Constant.CMD_ID_GET&&Integer.valueOf(data[1],16)==KEY_GET_BLOOD){
                tv2.setText("实时血压监测 收缩压:"+Integer.valueOf(data[4],16) +" 舒张压:"+Integer.valueOf(data[5],16) );
            }
            //endregion
            //region同步历史
            if(Integer.valueOf(data[0],16)==Constant.CMD_ID_GET&&Integer.valueOf(data[1],16)==KEY_GET_SYNC){ //同步历史
                int length=Integer.valueOf(data[3]+data[2],16);
                int begin=4; //2个类型 2个长度 5个头记录
                for (int i=0;i<length/14;i++){
                    int Interval=i * 14+begin;
                    int timeStamp = Integer.parseInt(data[Interval + 3]+data[Interval + 2]+data[Interval + 1]+data[Interval + 0],16);
                    Calendar cal=Calendar.getInstance();
                    cal.set(2000,0,1,0,0,0);
                    cal.add(Calendar.SECOND,timeStamp);
                    Log.i("ttttttt", "getBleData:001每一条数据的时间是?"+cal.getTime());
                    int sleepStatus=Integer.parseInt(data[Interval + 4],16);

                    int Step = Integer.parseInt(data[Interval +  6]+data[Interval +  5],16);
                    Log.i("ttttttt", "getBleData:002每一条数据的步数是?"+Step);
                    int heart = Integer.parseInt(data[Interval +  7],16);
                    int SBP = Integer.parseInt(data[Interval +  8],16);
                    int DBP = Integer.parseInt(data[Interval +  9],16);

                    tv2.setText("日期:"+new SimpleDateFormat("yyyyMMddHHmm").format(cal.getTimeInMillis())+"\n"+
                            "睡眠状态:"+sleepStatus+"\n"+"运动步数:"+Step+"\n"+"心率:"+heart+"\n"+"SBP:"+SBP+"\n"+"DBP:"+DBP+"\n"
                    );
                    dbTools.saveData(new HealthSport(new SimpleDateFormat("yyyyMMddHHmm").format(cal.getTimeInMillis())
                            , Step, sleepStatus, heart, heart, SBP, DBP));

                    Log.i("BleLig", "收到历史日期："+data[Interval + 3]+data[Interval + 2]+data[Interval + 1]+data[Interval + 0]+"\n"+new SimpleDateFormat("yyyyMMddHHmm").format(cal.getTimeInMillis()));
                }
            }
            //endregion
            //region同步运动记录
            if(Integer.valueOf(data[0],16)==Constant.CMD_ID_GET&&Integer.valueOf(data[1],16)==KEY_GET_SPORTRECORD){
                int length=Integer.valueOf(data[3]+data[2],16);
                int begin=4; //2个类型 2个长度
                for (int i=0;i<length/13;i++){
                    int Interval=i * 14+begin;
                    int type=Integer.parseInt(data[Interval + 0],16);
                    //开始运动时间戳
                    int timeStamp = Integer.parseInt(data[Interval + 4]+data[Interval + 3]+data[Interval + 2]+data[Interval + 1],16);
                    Calendar cal=Calendar.getInstance();
                    cal.set(2000,0,1,0,0,0);
                    cal.add(Calendar.SECOND,timeStamp);

                    switch (type){
                        case RECORD_TYPE_RUNNING:{
                            int Step = Integer.parseInt(data[Interval +  6]+data[Interval +  5],16); //步数
                            int distance = Integer.parseInt(data[Interval +  8]+data[Interval +  7],16);   //里程(米）
                            int RecordTimes = Integer.parseInt(data[Interval +  10]+data[Interval +  9],16);//运动时间(秒)
                            int kcal=Integer.valueOf(data[Interval +  12]+data[Interval +  11],16);//消耗的卡路里(千卡)

                            tv2.setText("日期:"+new SimpleDateFormat("yyyyMMddHHmm").format(cal.getTimeInMillis())+"\n"+
                                    "类型：跑步 "+"步数:"+Step+"\n"+"里程:"+distance+"\n"+"运动时间:"+RecordTimes+"\n"+"消耗的卡路里:"+kcal
                            );
                            Log.i("BleLig", "收到运动记录："+tv2.getText().toString());
                        }
                        break;
                        case RECORD_TYPE_ROPESKIPPING: {
                            int RopeSkipping = Integer.parseInt(data[Interval + 6] + data[Interval + 5], 16); //跳绳个数
//                           Integer.parseInt(data[Interval +  8]+data[Interval +  7],16);   //保留
                            int RecordTimes = Integer.parseInt(data[Interval + 10] + data[Interval + 9], 16);//运动时间(秒)
                            int kcal = Integer.valueOf(data[Interval + 12] + data[Interval + 11], 16);//消耗的卡路里(千卡)

                            tv2.setText("日期:"+new SimpleDateFormat("yyyyMMddHHmm").format(cal.getTimeInMillis())+"\n"+
                                    "类型：跳绳个数 "+RopeSkipping+"\n"+"运动时间:"+RecordTimes+"\n"+"消耗的卡路里:"+kcal
                            );
                            Log.i("BleLig", "收到运动记录："+tv2.getText().toString());
                        }
                        break;
                        case RECORD_TYPE_RIDING:{
//                            Integer.parseInt(data[Interval + 6] + data[Interval + 5], 16); //保留
//                            Integer.parseInt(data[Interval +  8]+data[Interval +  7],16);   //保留
                            int RecordTimes = Integer.parseInt(data[Interval + 10] + data[Interval + 9], 16);//运动时间(秒)
                            int kcal = Integer.valueOf(data[Interval + 12] + data[Interval + 11], 16);//消耗的卡路里(千卡)

                            tv2.setText("日期:"+new SimpleDateFormat("yyyyMMddHHmm").format(cal.getTimeInMillis())+"\n"+
                                    "类型：骑行 "+"运动时间:"+RecordTimes+"\n"+"消耗的卡路里:"+kcal
                            );

                            Log.i("BleLig", "收到运动记录："+tv2.getText().toString());
                        }
                        break;
                        case RECORD_TYPE_PLAYBALL:{
                            int Step = Integer.parseInt(data[Interval +  6]+data[Interval +  5],16); //步数
                            int distance = Integer.parseInt(data[Interval +  8]+data[Interval +  7],16);   //里程(米）
                            int RecordTimes = Integer.parseInt(data[Interval +  10]+data[Interval +  9],16);//运动时间(秒)
                            int kcal=Integer.valueOf(data[Interval +  12]+data[Interval +  11],16);//消耗的卡路里(千卡)

                            tv2.setText("日期:"+new SimpleDateFormat("yyyyMMddHHmm").format(cal.getTimeInMillis())+"\n"+
                                    "类型：打球 "+"步数:"+Step+"\n"+"里程:"+distance+"\n"+"运动时间:"+RecordTimes+"\n"+"消耗的卡路里:"+kcal
                            );
                            Log.i("BleLig", "收到运动记录："+tv2.getText().toString());
                        }
                        break;
                    }
                }
            }
            //endregion
            //region同步睡眠记录
            if(Integer.valueOf(data[0],16)==Constant.CMD_ID_GET&&Integer.valueOf(data[1],16)==KEY_GET_SLEEPRECORD){
                int length=Integer.valueOf(data[3]+data[2],16);
                int begin=4; //2个类型 2个长度
                int index=begin;
//                for (int i=0;i<length;i++){

                List<Sleep> sleeps=new ArrayList<>();
                while(index<=length-20){
                    String recordHead=data[index+1]+data[index+0]; //睡眠记录帧头
                    index+=2;

                    int recordLength=Integer.valueOf(data[index+1]+data[index+0],16);//本帧总字节长度 (包含记录帧头)
                    index+=2;

                    int beginTimeStamp = Integer.parseInt(data[index + 3]+data[index + 2]+data[index + 1]+data[index + 0],16);//开始睡觉时间戳
                    Log.i("tttttttttttt", "getBleData: 我们同步历史的数据开始点是?"+beginTimeStamp);
                    Calendar beginCal=Calendar.getInstance();
                    beginCal.set(2000,0,1,0,0,0);
                    beginCal.add(Calendar.SECOND,beginTimeStamp);
                    index+=4;

                    int endTimeStamp = Integer.parseInt(data[index + 3]+data[index + 2]+data[index + 1]+data[index + 0],16);//醒来时间戳
                    Calendar endCal=Calendar.getInstance();
                    endCal.set(2000,0,1,0,0,0);
                    endCal.add(Calendar.SECOND,endTimeStamp);
                    index+=4;

                    int deepTimes=Integer.valueOf(data[index+1]+data[index+0],16);//深睡次数
                    index+=2;

                    int lightTimes=Integer.valueOf(data[index+1]+data[index+0],16);//浅睡次数
                    index+=2;

                    int deepTotalTime=Integer.valueOf(data[index+1]+data[index+0],16);//深睡眠总时间（单位：分）
                    index+=2;

                    int lightTotalTime=Integer.valueOf(data[index+1]+data[index+0],16);//浅睡眠总时间（单位：分）
                    index+=2;

                    List<SleepItem> items=new ArrayList<>();
                    for(int i=0;i<(recordLength-20)/8;i++){
                        String sleepStatus=data[index+0];//深睡或浅睡标志 深睡：0xF1 浅睡：0xF2
                        index+=1;

                        int sleepTimeStamp=Integer.parseInt(data[index+3]+data[index+2]+data[index+1]+data[index+0],16);// 深睡或浅睡开始时间戳
                        Log.i("ttttttt", "saveData_sleep: 006---1开始睡眠的记录是?"+sleepTimeStamp);
                        Calendar sleepCal=Calendar.getInstance();
                        sleepCal.set(2000,0,1,0,0,0);
                        sleepCal.add(Calendar.SECOND,sleepTimeStamp);

                        index+=4;

                        int sleepTotalTime=Integer.valueOf(data[index+2]+data[index+1]+data[index+0],16); //深睡或浅睡时长（单位：秒）

                        Log.i("ttttttt", "saveData_sleep: 006---2开始睡眠的记录是?"+sleepTotalTime);
                        index+=3;

                        SleepItem item=new SleepItem();

                        sleepCal.add(Calendar.SECOND, sleepTotalTime);

                        item.setDate(new SimpleDateFormat("yyyyMMddHHmm").format(sleepCal.getTime()));
                        item.setOffset(sleepTotalTime/60);

                        switch (sleepStatus){
                            case "F1"://深睡
                                item.setType(20);
                                break;
                            case "F2"://浅睡
                                item.setType(19);
                                break;
                        }
                        items.add(item);
                    }

                    boolean isExist=false;
                    for(int i=0;i<sleeps.size();i++){
                        Sleep currSleep=sleeps.get(i);
                        int year=endCal.get(Calendar.YEAR);
                        int month=endCal.get(Calendar.MONTH)+1;
                        int day=endCal.get(Calendar.DAY_OF_MONTH);
                        if( currSleep.getYear()==year&&currSleep.getMonth()==month&&currSleep.getDay()==day){
                            isExist=true;

                            boolean isEdit=false;
                            if(Long.valueOf(currSleep.getBeginDate())>Long.valueOf(new SimpleDateFormat("yyyyMMddHHmm").format(beginCal.getTime()))){
                                currSleep.setBeginDate(new SimpleDateFormat("yyyyMMddHHmm").format(beginCal.getTime()));
                                isEdit=true;
                            }

                            if(Long.valueOf(currSleep.getEndDate())<Long.valueOf(new SimpleDateFormat("yyyyMMddHHmm").format(endCal.getTime()))){
                                currSleep.setEndDate(new SimpleDateFormat("yyyyMMddHHmm").format(endCal.getTime()));
                                isEdit=true;
                            }

                            if(isEdit){
                                currSleep.setDeepSleepMinutes(currSleep.getDeepSleepMinutes()+deepTotalTime);
                                currSleep.setLightSleepMinutes(currSleep.getLightSleepMinutes()+lightTotalTime);
                                currSleep.setTotalSleepMinutes(currSleep.getTotalSleepMinutes()+deepTotalTime+lightTotalTime);
                                currSleep.getList().addAll(items);
                            }
                        }
                    }

                    if(!isExist){
                        Sleep sleep=new Sleep();
                        sleep.setList(items);

                        sleep.setYear(endCal.get(Calendar.YEAR));
                        sleep.setMonth(endCal.get(Calendar.MONTH)+1);
                        sleep.setDay(endCal.get(Calendar.DAY_OF_MONTH));
                        sleep.setBeginDate(new SimpleDateFormat("yyyyMMddHHmm").format(beginCal.getTime()));
                        sleep.setEndDate(new SimpleDateFormat("yyyyMMddHHmm").format(endCal.getTime()));
                        sleep.setDeepSleepMinutes(deepTotalTime);
                        sleep.setLightSleepMinutes(lightTotalTime);
                        sleep.setTotalSleepMinutes(deepTotalTime+lightTotalTime);
                        sleeps.add(sleep);
                    }
                }

//                for(int i=0;i<sleeps.size();i++){
//                    Sleep currSleep=sleeps.get(i);
//
//                    for(int j=0;j<sleeps.size();j++){
//                        Sleep sleep=sleeps.get(j);
//                        if(sleep.getYear()==currSleep.getYear()&&sleep.getMonth()==currSleep.getMonth()&&sleep.getDay()==currSleep.getDay()){
//                            boolean isAdd=false;
//                            if(Integer.valueOf(currSleep.getBeginDate())>Integer.valueOf(sleep.getBeginDate())) {
//                                currSleep.setBeginDate(sleep.getBeginDate());
//                                isAdd=true;
//                            }
//
//                            if(Integer.valueOf(currSleep.getEndDate())<Integer.valueOf(sleep.getEndDate())) {
//                                currSleep.setEndDate(sleep.getEndDate());
//                                isAdd=true;
//                            }
//
//                            if(isAdd){
//                                currSleep.setDeepSleepMinutes(currSleep.getDeepSleepMinutes()+sleep.getDeepSleepMinutes());
//                                currSleep.setLightSleepMinutes(currSleep.getLightSleepMinutes()+sleep.getLightSleepMinutes());
//                                currSleep.setTotalSleepMinutes(currSleep.getDeepSleepMinutes()+currSleep.getLightSleepMinutes());
//                            }
//                        }
//                    }
//
//                    Log.i("BleLig", "收到睡眠数据"+currSleep.toString());
//                }

//                }
        }
        //endregion
        //region手环实时RR间隔
        if(Integer.valueOf(data[0],16)==Constant.CMD_ID_GET&&Integer.valueOf(data[1],16)==KEY_GET_RR){
            String tmpStr = "";
            for(int i=4;i<data.length-2;i++){
                tmpStr=data[i]+tmpStr;
            }

            float dd=Float.intBitsToFloat(Integer.parseInt(tmpStr,16));

            tv1.setText("手环实时RR间隔:"+dd);
        }
        //endregion
        //region手环实时PPG数据点
        if(Integer.valueOf(data[0],16)==Constant.CMD_ID_GET&&Integer.valueOf(data[1],16)==KEY_GET_PPG){
            String tmpStr = "";
            for(int i=4;i<data.length-2;i+=4){
                BigInteger bi = new BigInteger(data[i+3]+data[i+2]+data[i+1]+data[i], 16);
                tmpStr=tmpStr+bi.intValue()+"\n";
            }
            tv1.setText("手环实时PPG数据点:"+tmpStr);

            Log.i("BleLigPPG", tmpStr);
        }
        //endregion
    }else{
        Log.i("BleLig", "收到不知名数据");
    }
}

    List<Sleep> jieguosleeps=new ArrayList<>();

//    private void SyncSleepData(final Sleep sleep) throws ParseException {
//
//        DeviceSensor deviceSensor = DeviceSensorRepository.getDeviceSensorByType(SysDataService.this, BleDevice.SportScale);
//
//        Calendar calDay = Calendar.getInstance();
//        calDay.set(sleep.getYear(), sleep.getMonth() - 1, sleep.getDay(), 0, 0, 0);
//        long dayTimeStamp = calDay.getTimeInMillis()/1000*1000;
//
//        long beginTime=new SimpleDateFormat("yyyyMMddHHmm").parse(sleep.getBeginDate()).getTime();
//        long endTime = new SimpleDateFormat("yyyyMMddHHmm").parse(sleep.getEndDate()).getTime();
//
//
//
////        ScaleRecord sR = ScaleRecordRepository.getScaleRecordForSignDateTimeSensorType(SysDataService.this, "23",dayTimeStamp, ScaleRecord.SportType);
//        ScaleRecord sR=ScaleRecordRepository.getScaleRecordForSignDateTime(SysDataService.this,dayTimeStamp,ScaleRecord.SportType);
//        if (sR == null||sR.getValue("23")==null||sR.getValue("19")==null||sR.getValue("20")==null||sR.getValue("24")==null||sR.getValue("25")==null||
//                !sR.getValue("19").equals(String.valueOf(sleep.getDeepSleepMinutes()))||
//                !sR.getValue("20").equals(String.valueOf(sleep.getLightSleepMinutes()))||
//                !sR.getValue("23").equals(String.valueOf(sleep.getTotalSleepMinutes()))||
//                !sR.getValue("24").equals(String.valueOf(beginTime))||
//                !sR.getValue("25").equals(String.valueOf(endTime))){
//
//
//            if(sR == null) sR = new ScaleRecord(ScaleRecord.SportType, UnUP.Value());
//            else if(sR.getBatchId()!=null)sR.setIsSend(Edit.Value());
//            if (deviceSensor.getDeviceId() != null)
//                sR.setDeviceId(deviceSensor.getDeviceId());
//            sR.setTimestamp(dayTimeStamp);
//            sR.setValue("19", String.valueOf(sleep.getDeepSleepMinutes()));
//            sR.setValue("20", String.valueOf(sleep.getLightSleepMinutes()));
//            sR.setValue("23", String.valueOf(sleep.getTotalSleepMinutes()));
//            sR.setValue("24", String.valueOf(beginTime));
//            sR.setValue("25", String.valueOf(endTime));
//            ScaleRecordRepository.insertOrUpdate(SysDataService.this, sR);
//        }
//
//
//        List<SleepItem> sleepItems=sleep.getList();
//
//        for (SleepItem item: sleepItems) {
//
//            Calendar cal = Calendar.getInstance();
//            cal.setTime(new SimpleDateFormat("yyyyMMddHHmm").parse(item.getDate()));
//            cal.add(Calendar.MINUTE,-item.getOffset());
//
//            Long detailTimeStamp=cal.getTimeInMillis()/1000*1000;
//
//            String SleepStatus;
//            switch (item.getType()) {
//                case 18:
//                    SleepStatus=String.valueOf(Constants.SLEEP_AWAKE);
//                    break;
//                case 19:
//                    SleepStatus=String.valueOf(Constants.SLEEP_LIGHR);
//                    break;
//                case 20:
//                    SleepStatus=String.valueOf(Constants.SLEEP_DEEP);
//                    break;
//                default:
//                    SleepStatus="";
//                    break;
//            }
//
////            ScaleRecord sRD = ScaleRecordRepository.getScaleRecordForSignDateTimeSensorType(SysDataService.this,"231", detailTimeStamp, ScaleRecord.SleepScaleType);
//            ScaleRecord sRD=ScaleRecordRepository.getScaleRecordForSignDateTime(SysDataService.this,detailTimeStamp,ScaleRecord.SleepScaleType);
//            if (sRD == null||sRD.getValue("002") == null ||sRD.getValue("231")==null||sRD.getValue("232")==null||
//                    !sRD.getValue("231").equals(String.valueOf(item.getOffset())) ||
//                    !sRD.getValue("232").equals(SleepStatus)
//                    ) {
//                if (sRD == null) sRD = new ScaleRecord(ScaleRecord.SleepScaleType, UnUP.Value());
//                else  if(sRD.getBatchId()!=null)sRD.setIsSend(Edit.Value());
//                sRD.setTimestamp(detailTimeStamp);
//                sRD.setDeviceId(deviceSensor.getDeviceId());
//                sRD.setValue("002", String.valueOf(dayTimeStamp));
//                sRD.setValue("231", String.valueOf(item.getOffset()));
//                sRD.setValue("232", SleepStatus);
//
//                ScaleRecordRepository.insertOrUpdate(SysDataService.this, sRD);
//            }
//        }
//    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getRssi(Integer data) {
        if (data < -70 && !isPlaying) {
            startAlarm();
            isPlaying = true;
        }
    }

    private boolean isPlaying = false;

    private void startAlarm() {
        MediaPlayer mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(mContext, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));
            mMediaPlayer.setLooping(true);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            BleLog.e("stop");
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        DfuServiceListenerHelper.registerProgressListener(mContext, mDfuProgressListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        DfuServiceListenerHelper.unregisterProgressListener(mContext, mDfuProgressListener);
    }

    private final DfuProgressListener mDfuProgressListener = new DfuProgressListener() {
        @Override
        public void onDeviceConnecting(String deviceAddress) {
            BleLog.e("dfu", "onDeviceConnecting");
        }

        @Override
        public void onDeviceConnected(String deviceAddress) {
            BleLog.e("dfu", "onDeviceConnected");
        }

        @Override
        public void onDfuProcessStarting(String deviceAddress) {
            BleLog.e("dfu", "onDfuProcessStarting");
            showProgressDialog();
        }

        @Override
        public void onDfuProcessStarted(String deviceAddress) {
            BleLog.e("dfu", "onDfuProcessStarted");
        }

        @Override
        public void onEnablingDfuMode(String deviceAddress) {
            BleLog.e("dfu", "onEnablingDfuMode");
        }

        @Override
        public void onProgressChanged(String deviceAddress, int percent, float speed, float avgSpeed, int currentPart, int partsTotal) {
            BleLog.e("dfu", "onProgressChanged" + percent);
            progressDialog.setProgress(percent);
        }

        @Override
        public void onFirmwareValidating(String deviceAddress) {
            BleLog.e("dfu", "onFirmwareValidating");
        }

        @Override
        public void onDeviceDisconnecting(String deviceAddress) {

            BleLog.e("dfu", "onDeviceDisconnecting");
        }

        @Override
        public void onDeviceDisconnected(String deviceAddress) {
            BleLog.e("dfu", "onDeviceDisconnected");

        }

        @Override
        public void onDfuCompleted(String deviceAddress) {
            BleLog.e("dfu", "onDfuCompleted");
            stopDfu();
            //升级成功，重新连接设备
            if(progressDialog!=null)progressDialog.dismiss();;
            Toast.makeText(mContext, "升级成功", Toast.LENGTH_SHORT).show();
            EventBus.getDefault().post(new BaseEvent(BaseEvent.EventType.RECONNECTION));
        }

        @Override
        public void onDfuAborted(String deviceAddress) {
            BleLog.e("dfu", "onDfuAborted");
            if(progressDialog!=null)progressDialog.dismiss();;
            Toast.makeText(mContext, "升级失败，请重新点击升级。", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(String deviceAddress, int error, int errorType, String message) {
            BleLog.e("dfu", "onError");
            stopDfu();
            if(progressDialog!=null)progressDialog.dismiss();
            Toast.makeText(mContext, "升级失败，请重新点击升级。", Toast.LENGTH_SHORT).show();
        }
    };

    private void stopDfu() {
        Intent intent = new Intent(mContext, DfuService.class);
        getActivity().stopService(intent);
    }

    public void showProgressDialog() {
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setTitle("正在升级");
        progressDialog.setMessage("请稍等......");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.setMax(100);
        progressDialog.show();
    }

public class DeviceRssiThread extends Thread {
    public volatile boolean isRunning = true;

    @Override
    public void run() {
        while (isRunning) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            YCProtocolUtils.getInstance().readRemoteRssi();
        }
    }
}

//    private Sleep getSleepData(int year, int month, int day) {
//        DateTime dateTime = new DateTime(year, month, day, 0, 0, 0);
//        String preTime = dateTime.minusDays(1).toString("yyyyMMdd").substring(0, 8);
//        String time = dateTime.toString("yyyyMMdd").substring(0, 8);
//        ArrayList<HealthSport> daySleepData = SleepUtils.getDaySleepData(preTime, time);
//        if (daySleepData == null || daySleepData.size() == 0) {
//            return null;
//        } else {
//            int size = daySleepData.size();
//            String date = daySleepData.get(size - 1).getDate();
//            int sleepEndedTimeH = Integer.parseInt(date.substring(8, 10));
//            int sleepEndedTimeM = Integer.parseInt(date.substring(10, 12));
//            int totalSleepMinutes = (size - 1) * 10;
//            List<SleepItem> list = new ArrayList<>();
//            //开始
//            list.add(new SleepItem(17, 10));
//            int ls = 0;
//            int ds = 0;
//            int offset = 0;
//            for (int i = 1; i < size - 1; i++) {
//                HealthSport healthSport = daySleepData.get(i);
//                int type = healthSport.getType();
//                int nextType = daySleepData.get(i + 1).getType();
//                offset++;
//                if (type == 18) {
//                    if (nextType != 18) {
//                        list.add(new SleepItem(18, offset * 10));
//                        offset = 0;
//                    }
//                }
//                if (type == 19) {
//                    ls++;
//                    if (nextType != 19) {
//                        list.add(new SleepItem(19, offset * 10));
//                        offset = 0;
//                    }
//                }
//                if (type == 20) {
//                    ds++;
//                    if (nextType != 20) {
//                        list.add(new SleepItem(20, offset * 10));
//                        offset = 0;
//                    }
//                }
//            }
//            //结束
////            list.add(new SleepItem(21, 10));
//            return new Sleep(year, month, day, sleepEndedTimeH, sleepEndedTimeM, totalSleepMinutes, ls * 10, ds * 10, list);
//        }
//    }

}
