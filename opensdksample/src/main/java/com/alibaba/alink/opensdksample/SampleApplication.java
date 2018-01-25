package com.alibaba.alink.opensdksample;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.multidex.MultiDexApplication;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.alink.opensdksample.envconfig.EnvConfigure;
import com.alibaba.alink.opensdksample.qrcode.QRCodeHelper;
import com.alibaba.sdk.android.openaccount.Environment;
import com.alibaba.wireless.security.open.SecurityGuardManager;
import com.alibaba.wireless.security.open.staticdatastore.IStaticDataStoreComponent;
import com.aliyun.alink.AlinkSDK;
import com.aliyun.alink.alirn.RNGlobalConfig;
import com.aliyun.alink.alirn.rnpackage.biz.BizPackageHolder;
import com.aliyun.alink.bone.CdnEnv;
import com.aliyun.alink.business.account.OALoginBusiness;
import com.aliyun.alink.business.alink.ALinkEnv;
import com.aliyun.alink.business.helper.ChannelBindHelper;
import com.aliyun.alink.business.login.IAlinkLoginCallback;
import com.aliyun.alink.utils.ALog;
import com.facebook.react.FrescoPackage;
import com.taobao.accs.ACCSManager;
import com.taobao.accs.IAppReceiver;
import com.taobao.accs.client.AccsConfig;
import com.taobao.accs.common.Constants;

import org.android.spdy.SpdyProtocol;

import java.util.HashMap;
import java.util.Map;

import mtopsdk.mtop.intf.MtopSetting;

/**
 * Created by huanyu.zhy on 16/12/7.
 */
public class SampleApplication extends MultiDexApplication {

    private static final String TAG = "SampleApplication";
    private ALinkEnv alinkEnv = ALinkEnv.Online;
    private CdnEnv cdnEnv = CdnEnv.Release;
    private String appkey;

    @Override
    public void onCreate() {
        super.onCreate();

        initEnv();

        getAppKey();

        initAccs();

        boolean isMainProcess = isMainProcess(this);
        if (!isMainProcess) {
            return;
        }

        initAlinkSDK();

        QRCodeHelper.init(this);
    }

    private void initEnv() {
        EnvConfigure.init(this);
        if (EnvConfigure.aLinkEnv !=null){
            alinkEnv = EnvConfigure.aLinkEnv;
        }else {
            EnvConfigure.saveAlinkEnv(this,alinkEnv);
        }

        if (EnvConfigure.cdnEnv !=null){
            cdnEnv = EnvConfigure.cdnEnv;
        } else {
            EnvConfigure.saveCDNEnv(this, cdnEnv);
        }
    }

    private void getAppKey() {
        try {
            int index;
            if (alinkEnv == ALinkEnv.Daily) {
                index = 2;
            } else {
                index = 0;
            }

            // initialize security guard
            // if only read appKey, skip initialize is ok
            SecurityGuardManager.getInitializer().initialize(getApplicationContext());

            SecurityGuardManager sgMgr = SecurityGuardManager.getInstance(this);
            if (sgMgr !=null) {
                IStaticDataStoreComponent sdsComp = sgMgr.getStaticDataStoreComp();
                if (sdsComp !=null) {
                    appkey = sdsComp.getAppKeyByIndex(index, "");
                    ALog.d(TAG, appkey);
                }
            }
        }catch (Exception e){
            ALog.d(TAG,"");
        }
    }

    // init ACCS
    private void initAccs() {

        int env = Constants.DEBUG;
        if (alinkEnv == ALinkEnv.Preview) {
            env = Constants.PREVIEW;
        } else if (alinkEnv == ALinkEnv.Online) {
            env = Constants.RELEASE;
        }

        AccsConfig.setSecurityGuardOff(AccsConfig.SECURITY_TYPE.SECURITY_OPEN);
        AccsConfig.setGroup(AccsConfig.ACCS_GROUP.OPEN);
        AccsConfig.setAccsCenterHosts("openacs.m.taobao.com",
                "openacs.m.taobao.com", "openacs.m.taobao.com");
        AccsConfig.setChannelHosts("openjmacs.m.taobao.com",
                "openjmacs.m.taobao.com", "openjmacs.m.taobao.com");
        AccsConfig.setAccsCenterIps(new String[]{"140.205.160.76"},
                new String[]{"140.205.172.12"},
                new String[]{"10.125.50.231"});
        AccsConfig.setChannelIps(new String[]{"140.205.163.94"},
                new String[]{"140.205.172.12"},
                new String[]{"10.125.50.231"});
        AccsConfig.setTnetPubkey(SpdyProtocol.PUBKEY_SEQ_OPEN, SpdyProtocol.PUBKEY_SEQ_OPEN);
        AccsConfig.setChannelReuse(false);

        com.taobao.accs.utl.ALog.setUseTlog(false);
        anet.channel.util.ALog.setUseTlog(false);
        ACCSManager.setMode(this, env);

        ACCSManager.bindApp(this, appkey, "ttidzorotest",
                new IAppReceiver() {
                    private final Map<String, String> SERVICES = new HashMap<String, String>() {

                        {
                            put("accs", "com.taobao.taobao.CallbackService");
                            put("accs-console", "com.taobao.taobao.CallbackService");
                            put("agooSend", "org.android.agoo.accs.AgooService");
                            put("agooAck", "org.android.agoo.accs.AgooService");
                            put("agooTokenReport", "org.android.agoo.accs.AgooService");
                        }
                    };

                    String TAG = "AppReceiver";

                    @Override
                    public void onData(String userId, String dataId, byte[] data) {
                        Log.d(TAG, "onData()");
                    }

                    @Override
                    public void onBindApp(int errorCode) {
                        Log.d(TAG, "onBindApp(): " + errorCode);
                        ChannelBindHelper.getInstance().registerChannel(
                                com.ut.device.UTDevice.getUtdid(SampleApplication.this),
                                "AGOO",
                                errorCode == 200);
                    }

                    @Override
                    public void onUnbindApp(int errorCode) {
                        Log.d(TAG, "onUnbindApp(): " + errorCode);
                    }

                    @Override
                    public void onBindUser(String userId, int errorCode) {
                        Log.d(TAG, "onBindUser(): " + userId + "/" + errorCode);
                    }

                    @Override
                    public void onUnbindUser(int errorCode) {
                        Log.d(TAG, "onUnbindUser(): " + errorCode);
                    }

                    @Override
                    public void onSendData(String dataId, int errorCode) {
                        Log.d(TAG, "onSendData(): " + errorCode);
                    }

                    @Override
                    public String getService(String serviceId) {
                        String service = SERVICES.get(serviceId);
                        return service;
                    }

                    @Override
                    public Map<String, String> getAllServices() {
                        return SERVICES;
                    }
                });
    }

    private void initAlinkSDK() {
        AlinkSDK.setLogLevel(ALog.LEVEL_DEBUG);

        // init account & open sdk
        {

            OALoginBusiness oaBiz = new OALoginBusiness();

            IAlinkLoginCallback alinkLoginCallback = new IAlinkLoginCallback() {
                @Override
                public void onSuccess() {
                    Toast.makeText(SampleApplication.this, "账号初始化成功", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(int code, String message) {
                    Toast.makeText(SampleApplication.this, "账号初始化异常: " + message, Toast.LENGTH_LONG).show();
                }
            };

            Environment env = Environment.TEST;
            if (alinkEnv == ALinkEnv.Preview) {
                env = Environment.PRE;
            } else if (alinkEnv == ALinkEnv.Online) {
                env = Environment.ONLINE;
            } else if (alinkEnv == ALinkEnv.Sandbox) {
                env = Environment.SANDBOX;
            }
            oaBiz.init(getApplicationContext(), env, alinkLoginCallback);

            MtopSetting.setAppKeyIndex(0, 2);
            AlinkSDK.setEnv(this, alinkEnv);
            AlinkSDK.init(SampleApplication.this, appkey, oaBiz);
        }

        // add image support with fresco
        BizPackageHolder bizPackageHolder = RNGlobalConfig.getBizPackageHolder();
        bizPackageHolder.addPackage(new FrescoPackage());

        // init boneKit if you need
        {
            AlinkSDK.setCdnEnv(cdnEnv);
            String appVersion = getAppVersion(this);
            AlinkSDK.initBoneKit(this, appkey, appVersion, true);
        }
    }

    static String getAppVersion(Context context) {
        String version = "";

        try {

            String packageName = context.getPackageName();

            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);

            version = packageInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return version;
    }

    static boolean isMainProcess(Context context) {

        int pid = android.os.Process.myPid();
        String processName = "";

        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                processName = appProcess.processName;
                break;
            }
        }

        String packageName = context.getPackageName();
        return (processName.equals(packageName));
    }
}
