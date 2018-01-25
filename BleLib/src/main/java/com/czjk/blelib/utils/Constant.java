package com.czjk.blelib.utils;


public class Constant {

    public static final String SP_NAME = "sp_name_bracelet";
    public static final String SP_KEY_DEVICE_ADDRESS = "sp_device_address";
    public  static final  byte CMD_ID_DFU = 0x01;
    public  static final  byte CMD_ID_GET = 0x02;
    public  static final  byte CMD_ID_SET = 0x03;
    public  static final  byte CMD_ID_BIND = 0x04;
    public  static final  byte CMD_ID_APP_CONTROL = 0x05;

    public  static final  byte KEY_OTA = 0x01;//APP向手环请求空中升级
    public  static final  byte KEY_RESET = 0x02;//APP向手环请求恢复出厂设置
    public  static final  byte KEY_TRANSPORT = 0x03;//APP向手环发送开启运输模式命令


    public  static final  byte KEY_GET_FW = 0x01;//APP获取手环版本信息
    public  static final  byte KEY_GET_TIME = 0x02;//APP获取手环系统时间
    public  static final  byte KEY_GET_MAC = 0x03;//APP获取手环MAC地址
    public  static final  byte KEY_GET_ELECTRICITY = 0x04;//APP获取手环电量信息
    public  static final  byte KEY_GET_LIVE_DATA = 0x05;  //APP获取手环实时数据(总步数+心率+血压+距离+卡路里)
    public  static final  byte KEY_GET_SYNC = 0x06;//APP获取手环历史数据
    public  static final  byte KEY_GET_MEASURE_HR = 0x07;//APP获取手环实时心率
    public  static final  byte KEY_GET_BLOOD = 0x08;//APP获取手环实时血压
    public  static final  byte KEY_GET_NOTICE = 0x09;//APP获取手环通知提醒开关状态
    public  static final  byte KEY_GET_ALARM = 0x0a;//APP获取手环闹钟设置状态
    public  static final  byte KEY_GET_USERINFO = 0x0b;//APP获取手环闹钟设置状态
    public  static final  byte KEY_GET_UNIT = 0x0c;//APP获取手环测量单位公英制
    public  static final  byte KEY_GET_LONG_SIT = 0x0d;//APP获取手环久坐提醒配置
    public  static final  byte KEY_GET_HEART_MODE = 0x0e;//APP获取手环心率监测模式
    public  static final  byte KEY_GET_RAISE = 0x0f;//APP获取手环抬腕开关状态
    public  static final  byte KEY_GET_DONOTDISTURB = 0x10;//APP获取手环勿扰模式开关状态
    public  static final  byte KEY_GET_PPG = 0x11;//APP获取手环实时PPG
    public  static final  byte KEY_GET_RR = 0x12;//APP获取手环实时R-R间隔
    public  static final  byte KEY_GET_BLOODTHRESHOLD= 0x13;//APP获取手环血压预警阈值
    public  static final  byte KEY_GET_TARGET = 0x14;//APP获取手环运动及睡眠目标
    public  static final  byte KEY_GET_HANDCHANGE= 0x15;//APP获取手环左右手佩戴配置
    public  static final  byte KEY_GET_SPORTRECORD= 0x16;//APP获取手环的运动记录
    public  static final  byte KEY_GET_SLEEPRECORD= 0x17;//APP获取手环的睡眠记录
    public  static final  byte KEY_GET_ALLALARMRECORD= 0x18;//APP获取手环所有闹钟的记录
    public  static final  byte KEY_GET_CURRENTACCELERATION= 0x19;//APP获取手环实时加速度数据
    public  static final  byte KEY_GET_CURRENTECGRECORD= 0x20;//APP获取手环实时加速度数据


    public  static final  byte KEY_SET_TIME = 0x01;//APP设置手环系统时间
    public  static final  byte KEY_SET_ALARM = 0x02; //APP设置手环闹钟
    public  static final  byte KEY_SET_USER_INFO = 0x03;//APP设置手环用户信息
    public  static final  byte KEY_SET_USER_UNIT = 0x04;//APP设置手环测量单位公英制
    public  static final  byte KEY_SET_LONG_SIT = 0x05;//APP设置手环久坐提醒配置
    public  static final  byte KEY_SET_HEART_MODE = 0x06;//APP设置手环心率监测模式
    public  static final  byte KEY_SET_UP_HAND_GESTURE = 0x07;//APP设置手环抬腕开关状态
    public  static final  byte KEY_SET_DONOTDISTURB = 0x08;//APP设置手环勿扰模式开关状态
    public  static final  byte KEY_SET_NOTICE= 0x09;    //APP设置手环通知提醒开关状态
    public  static final  byte KEY_SET_ALARM_NAME = 0x0a;//APP设置手环闹钟名称
    public  static final  byte KEY_SET_CAMERA = 0x0b;//APP设置手环相机控制状态
    public  static final  byte KEY_SET_SCREEN_MODE = 0x0c;//APP设置手环横竖屏显示方式
    public  static final  byte KEY_SET_BLOOD_THRESHOLD = 0x0d;//APP设置手环血压预警阈值
    public  static final  byte KEY_SET_TARGET = 0x0e;//APP设置手环运动及睡眠目标
    public  static final  byte KEY_SET_HANDCHANGE = 0x0f;//APP设置手环左右手佩戴配置


    public  static final  byte KEY_BIND = 0x01;//APP绑定手环
    public  static final  byte KEY_UNBIND = 0x02;//APP解绑手环
    public  static final  byte KEY_DISCOUNT_BLE = 0x03;//APP发送断开连接请求
    public  static final  byte KEY_SENDMESS_BLE = 0x04;//APP发送消息推送
    public  static final  byte KEY_FIND_BLE = 0x05;//APP发送查找手环请求
    public  static final  byte KEY_BLOOD_CALIBRATION = 0x06;//APP血压校准


    public  static final  byte KEY_CONTROL_CAMERAMUSIC = 0x01; //手环向APP发送音乐相机控制命令
    public  static final  byte KEY_FIND_PHONE = 0x02; ///手环向APP发送寻找手机请求
    public  static final  byte KEY_SEND_SOS = 0x03;//手环向APP发送SOS请求
    public  static final  byte KEY_REJECT_PHONE = 0x04;//手环向APP发送拒绝接听来电请求
    public  static final  byte KEY_FINISH_SYNC = 0x05;//手环向APP发送数据传输完成通知命令

    //    public  static final  byte RESERVED = 0x00;
    public  static final int MSG_TYPE_PHONECOMING = 1;
    public  static final int MSG_TYPE_SMS = 2;
    public  static final int MSG_TYPE_EMAIL = 3;
    public  static final int MSG_TYPE_WX = 4;
    public  static final int MSG_TYPE_QQ = 5;
    public  static final int MSG_TYPE_FACEBOOK = 6;
    public  static final int MSG_TYPE_TWITTER = 7;
    public  static final int MSG_TYPE_WHATSAPP = 8;
    public  static final int MSG_TYPE_SINAWEIBO = 9;

    public  static final int RECORD_TYPE_RUNNING = 1;
    public  static final int RECORD_TYPE_ROPESKIPPING = 2;
    public  static final int RECORD_TYPE_RIDING = 3;
    public  static final int RECORD_TYPE_PLAYBALL = 4;

//
//    public  static final  byte  KEY_ALARM = 0x03;
//    public  static final  byte  KEY_ALARM_START = 0x00;
//    public  static final  byte  KEY_ALARM_NAME = 0x01;
//    public  static final  byte  KEY_ALARM_CONTENT = 0x02;
//    public  static final  byte  KEY_ALARM_STOP = 0x03;

    public static final String TABLE_NAME_DATA = "data";
    public static final String STEP_FIELD_DATE = "date";
    public static final String STEP_FIELD_STEP = "step";
    public static final String STEP_FIELD_TYPE = "type";
    public static final String HEARTRATE_FIELD_VALUE = "heart_value";
    public static final String RESTING_HEARTRATE_FIELD_VALUE = "resting_heart_value";
    public static final String BLOOD_FIELD_SYSTOLIC_VALUE = "systolic_value";
    public static final String BLOOD_FIELD_DIASTOLIC_VALUE = "diastolic_value";
}
