package com.alibaba.alink.opensdksample.debugpage;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.alink.opensdksample.ConfigHelper;
import com.alibaba.alink.opensdksample.R;
import com.alibaba.alink.opensdksample.envconfig.ConfigEnvActivity;
import com.alibaba.alink.opensdksample.envconfig.EnvConfigure;
import com.alibaba.alink.opensdksample.qrcode.QRCodeHelper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.alirn.dev.BoneDevHelper;
import com.aliyun.alink.business.alink.ALinkBusinessEx;
import com.aliyun.alink.business.downstream.DeviceBusiness;
import com.aliyun.alink.business.login.AlinkLoginBusiness;
import com.aliyun.alink.business.login.IAlinkLoginCallback;
import com.aliyun.alink.business.mtop.MTopResponse;
import com.aliyun.alink.business.mtop.MTopResponseData;
import com.aliyun.alink.page.pageroutor.ARouter;
import com.aliyun.alink.page.pageroutor.ExtraBundle;
import com.aliyun.alink.page.rn.RNActivity;
import com.aliyun.alink.sdk.net.anet.api.AError;
import com.aliyun.alink.sdk.net.anet.api.persistentnet.IOnPushListener;
import com.aliyun.alink.sdk.net.anet.api.transitorynet.TransitoryRequest;
import com.aliyun.alink.sdk.net.anet.api.transitorynet.TransitoryResponse;
import com.aliyun.alink.utils.ALog;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import demo.smurfs.sds.aliyun.com.sds_bluetooth_demo.MainActivity;

public class DebugActivity extends Activity {

    private static final String TAG = "DebugActivity";
    private static final int REQUEST_CODE_ADD_DEVICE = 0x2000;

    private ListView listView;
    private JSONArray listJson = null;
    private JSONObject configJson = null;
    private DeviceBusiness deviceBiz = null;
    private Button buttonReload = null;

    private RequestQueue requestQueue = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);

        initView();

        loadListData();

        initPushTunnel ();

        EnvConfigure.init(this.getApplicationContext());
    }

    private void initPushTunnel () {
        deviceBiz = new DeviceBusiness();
        deviceBiz.startWatching(DeviceBusiness.FEATURE_DOWNSTREAM_WATCHER);
        deviceBiz.setDownstreamListener(new IOnPushListener() {
            @Override
            public void onCommand(String s) {
                Toast.makeText(DebugActivity.this, "收到下推数据",Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean filter(String s) {
                return true;
            }
        }, true);
    }

    private void initView() {
        this.buttonReload = (Button) findViewById(R.id.button_reload);

        this.listView = (ListView) findViewById(R.id.bone_listview);
        this.listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = listJson.getJSONObject(position).getString("url");
                if (TextUtils.isEmpty(url)) {
                    Toast.makeText(DebugActivity.this, "配置URL为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                ARouter.navigate(DebugActivity.this, url);
            }
        });
    }

    // Btn Onclick Action

    public void envSwitch(View view) {
        Intent intent = new Intent(this, ConfigEnvActivity.class);
        startActivity(intent);
    }

    public void accountLogin(View view) {
        if (AlinkLoginBusiness.getInstance().isLogin()) {
            Toast.makeText(this, "账号已登录", Toast.LENGTH_SHORT).show();
        } else {
            AlinkLoginBusiness.getInstance().login(this, null);
        }
    }

    public void accountLogout(View view) {
        if (!AlinkLoginBusiness.getInstance().isLogin()) {
            Toast.makeText(this, "账号未登录", Toast.LENGTH_SHORT).show();
            return;
        }

        AlinkLoginBusiness.getInstance().logout(this, new IAlinkLoginCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(DebugActivity.this, "账号登出成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(DebugActivity.this, "账号登出失败,"+s, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void wifiDeviceConfig(View view) {
        if (configJson == null || TextUtils.isEmpty(configJson.getString("deviceConfigURL"))) {
            Toast.makeText(this, "配置获取失败或配置URL为空",Toast.LENGTH_SHORT).show();
            return;
        }
        String url = configJson.getString("deviceConfigURL");

        // url = "p.aliplus.com/deviceConfig"
//        ARouter.navigateForResult(DebugActivity.this, url, REQUEST_CODE_ADD_DEVICE);
        Map<String, String> params = new HashMap<>(1);
        params.put("deviceConfigMode", EnvConfigure.deviceConfigMode);

        Bundle bundle = new Bundle(1);
        bundle.putString("extraParams", JSON.toJSONString(params));

        new ExtraBundle().putExtras(bundle).navigateForResult(DebugActivity.this, url, REQUEST_CODE_ADD_DEVICE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (REQUEST_CODE_ADD_DEVICE == requestCode
                && RESULT_OK == resultCode) {

            String uuid = data.getStringExtra("uuid");
            String model = data.getStringExtra("model");
            Toast.makeText(this, String.format(Locale.ENGLISH, "配网成功:uuid=%s;model=%s", uuid, model), Toast.LENGTH_LONG).show();
            // do your business with uuid & model

            return;
        }

        QRCodeHelper.onActivityResult(this, requestCode, resultCode, data);
    }

    public void bleDeviceDiscovery(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void boneDebug(View view) {
        final EditText editText = new EditText(this);

        new ConfigHelper().loadFromDisk(this);

        editText.setText(ConfigHelper.ip);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请输入DevSever的IP:")
                .setView(editText)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String ip = editText.getText().toString();
                        handleIP(ip);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.show();
    }

    public void scanDebug(View view) {
        QRCodeHelper.open(this);
    }

    /**
     * 演示 MTOP 接口的调用
     *
     * @param view
     */
    public void getAlinkTime(View view) {

        TransitoryRequest transitoryRequest = new TransitoryRequest();

        transitoryRequest.setMethod("mtop.openalink.app.core.alinktime.get");
        ALinkBusinessEx biz = new ALinkBusinessEx();
        biz.request(transitoryRequest, new ALinkBusinessEx.IListener() {
            @Override
            public void onSuccess(TransitoryRequest transitoryRequest, TransitoryResponse transitoryResponse) {

                MTopResponse response = (MTopResponse) transitoryResponse.getOriginResponseObject();
                MTopResponseData data = response.data;

                String detailMessage = "请求已返回";

                if ("1000".equalsIgnoreCase(data.code)) {
                    try {

                        JSONObject jsonObject = (JSONObject) data.data;
                        String time = jsonObject.getString("time");
                        long timeInMillis = Long.valueOf(time) * 1000;
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(timeInMillis);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd, HH:mm:ss");
                        String strTime = simpleDateFormat.format(calendar.getTime());
                        detailMessage = String.format(Locale.ENGLISH, "请求成功: alink time = \"%s\"", strTime);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                Toast.makeText(DebugActivity.this, detailMessage, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(TransitoryRequest transitoryRequest, AError aError) {
                String detailMessage = String.format(Locale.ENGLISH, "请求失败: code:%d,message:%s", aError.getCode(), aError.getMsg());
                Toast.makeText(DebugActivity.this, detailMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleIP(String ip) {

        // check ip
        boolean match = Pattern.matches("((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))", ip);

        if (!match) {
            Toast.makeText(this, "无效的IP地址", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!TextUtils.equals(ip, ConfigHelper.ip)) {
            ConfigHelper.ip = ip;
            new ConfigHelper().saveToDisk(this);
        }

        new BoneDevHelper().getBundleInfoAsync(this, ip, new BoneDevHelper.OnBondBundleInfoGetListener() {
            @Override
            public void onSuccess(BoneDevHelper.BoneBundleInfo boneBundleInfo) {
                if (null == boneBundleInfo) {
                    return;
                }

                if (null == boneBundleInfo.data) {
                    return;
                }

                if (!"200".equalsIgnoreCase(boneBundleInfo.code)) {

                    String message = boneBundleInfo.data.get("message");
                    Toast.makeText(DebugActivity.this, message, Toast.LENGTH_SHORT).show();

                    return;
                }

                String url = boneBundleInfo.data.get("url");
                String config = boneBundleInfo.data.get("config");
                String params = boneBundleInfo.data.get("params");

                url = url.replaceFirst("http://", "react://");

                Intent intent = new Intent(DebugActivity.this, RNActivity.class);
                intent.setData(Uri.parse(url));
                intent.putExtra("extraConfig", config);
                intent.putExtra("extraParams", params);

                DebugActivity.this.startActivity(intent);
            }

            @Override
            public void onError(String message, Exception e) {
                Toast.makeText(DebugActivity.this, message, Toast.LENGTH_SHORT).show();

                if (null != e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void reload(View view) {
        loadListData();
    }

    private void loadListData() {

        if (null == this.requestQueue) {
            this.requestQueue = Volley.newRequestQueue(this);
        }

        String url ="http://gaic.alicdn.com/tms/boneDemoConfig.json";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        configJson = JSON.parseObject(response);
                        listJson = configJson.getJSONArray("boneAppList");
                        listView.setAdapter(new ListViewAdapter(DebugActivity.this,listJson));

                        if (null != buttonReload) {
                            buttonReload.setVisibility(View.GONE);
                        }
                    }catch (Exception e){
                        ALog.e(TAG,"error = "+ e.toString());
                        Toast.makeText(DebugActivity.this,"配置解析失败",Toast.LENGTH_SHORT).show();

                        if (null != buttonReload) {
                            buttonReload.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DebugActivity.this,"加载配置失败",Toast.LENGTH_SHORT).show();

                if (null != buttonReload) {
                    buttonReload.setVisibility(View.VISIBLE);
                }
            }
        });
        this.requestQueue.add(stringRequest);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (null != requestQueue) {
            requestQueue.stop();
        }
    }
}
