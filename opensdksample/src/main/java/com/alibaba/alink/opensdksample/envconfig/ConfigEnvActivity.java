package com.alibaba.alink.opensdksample.envconfig;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.alibaba.alink.opensdksample.R;
import com.aliyun.alink.bone.CdnEnv;
import com.aliyun.alink.business.alink.ALinkEnv;
import com.aliyun.alink.business.login.AlinkLoginBusiness;

public class ConfigEnvActivity extends Activity implements OnCheckedChangeListener {

    private RadioGroup alinkEnvRG;
    private RadioGroup cdnEnvRG;
    private RadioGroup deviceConfigModelRG;

    private ALinkEnv aLinkEnv = null;
    private CdnEnv cdnEnv = null;
    private String deviceConfigMode = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_env);

        init();
    }

    private void init(){
        this.alinkEnvRG = (RadioGroup) findViewById(R.id.radiogroup_envconfig_alinkenv);
        this.cdnEnvRG = (RadioGroup) findViewById(R.id.radiogroup_envconfig_cdnenv);
        this.deviceConfigModelRG = (RadioGroup) findViewById(R.id.radiogroup_envconfig_device_config_mode);

        this.alinkEnvRG.setOnCheckedChangeListener(this);
        this.cdnEnvRG.setOnCheckedChangeListener(this);
        this.deviceConfigModelRG.setOnCheckedChangeListener(this);

        loadCurEnvStatus();
    }

    private void loadCurEnvStatus(){
        if (EnvConfigure.aLinkEnv !=null) {
            aLinkEnv = EnvConfigure.aLinkEnv;
            switch (EnvConfigure.aLinkEnv){
                case Online:
                    this.alinkEnvRG.check(R.id.radiobutton_envconfig_alinkenv_online);
                    break;
                case Preview:
                    this.alinkEnvRG.check(R.id.radiobutton_envconfig_alinkenv_pre);
                    break;
                case Sandbox:
                    this.alinkEnvRG.check(R.id.radiobutton_envconfig_alinkenv_sandbox);
                    break;
                case Daily:
                    this.alinkEnvRG.check(R.id.radiobutton_envconfig_alinkenv_daily);
                    break;
                default:
                    break;
            }
        }

        if (EnvConfigure.cdnEnv != null) {
            cdnEnv = EnvConfigure.cdnEnv;
            switch (EnvConfigure.cdnEnv) {
                case Release:
                    this.cdnEnvRG.check(R.id.radiobutton_envconfig_cdnenv_release);
                    break;
                case Test:
                    this.cdnEnvRG.check(R.id.radiobutton_envconfig_cdnenv_test);
                    break;
                default:
                    break;
            }
        }

        if ("auth".equalsIgnoreCase(EnvConfigure.deviceConfigMode)) {
            deviceConfigModelRG.check(R.id.radiobutton_envconfig_device_config_mode_auth);
        } else {
            deviceConfigModelRG.check(R.id.radiobutton_envconfig_device_config_mode_normal);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int id = group.getId();
        if (id == R.id.radiogroup_envconfig_alinkenv) {

            if (checkedId == R.id.radiobutton_envconfig_alinkenv_online) {
                aLinkEnv = ALinkEnv.Online;
            }else if (checkedId == R.id.radiobutton_envconfig_alinkenv_pre) {
                aLinkEnv = ALinkEnv.Preview;
            }else if (checkedId == R.id.radiobutton_envconfig_alinkenv_sandbox) {
                aLinkEnv = ALinkEnv.Sandbox;
            }else if (checkedId == R.id.radiobutton_envconfig_alinkenv_daily) {
                aLinkEnv = ALinkEnv.Daily;
            }

        }else if (id == R.id.radiogroup_envconfig_cdnenv) {
            if (checkedId == R.id.radiobutton_envconfig_cdnenv_release) {
                cdnEnv = CdnEnv.Release;
            } else if (checkedId == R.id.radiobutton_envconfig_cdnenv_test) {
                cdnEnv = CdnEnv.Test;
            }
        } else if (id == R.id.radiogroup_envconfig_device_config_mode) {
            if (checkedId == R.id.radiobutton_envconfig_device_config_mode_auth) {
                deviceConfigMode = "auth";
            } else {
                deviceConfigMode = "addDevice";
            }
        }
    }

    public void swithSaveEnv(View view) {
        if (aLinkEnv != null) {
            EnvConfigure.saveAlinkEnv(getApplicationContext(),aLinkEnv);
        }

        if (cdnEnv != null) {
            EnvConfigure.saveCDNEnv(getApplicationContext(),cdnEnv);
        }

        if ("auth".equalsIgnoreCase(deviceConfigMode)) {
            EnvConfigure.saveDeviceConfigMode(getApplicationContext(), "auth");
        } else {
            EnvConfigure.saveDeviceConfigMode(getApplicationContext(), "addDevice");
        }

        if (AlinkLoginBusiness.getInstance().isLogin()) {
            AlinkLoginBusiness.getInstance().logout(this,null);
        }
        
        Toast.makeText(ConfigEnvActivity.this, "设置成功，请杀掉应用重新登录", Toast.LENGTH_SHORT).show();
    }
}
