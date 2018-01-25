package com.alibaba.alink.opensdksample;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.alink.opensdksample.debugpage.DebugActivity;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taobao.agoo.TaobaoBaseIntentService;

import org.android.agoo.common.AgooConstants;

import java.util.concurrent.atomic.AtomicInteger;

public class TaobaoIntentService extends TaobaoBaseIntentService {
    private final static String TAG = "TaobaoIntentService";

    static AtomicInteger notificationIndex = new AtomicInteger(1);
    
    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate()");
    }

    @Override
    protected void onError(Context context, String errorId) {
        Log.e(TAG, "onError: " + errorId);
    }

    @Override
    protected void onRegistered(Context context, String registrationId) {
        Log.e(TAG, "onRegistered: " + registrationId);
    }

    @Override
    protected void onUnregistered(Context context, String registrationId) {
        Log.e(TAG, "onUnregistered: " + registrationId);
    }

    @Override
    protected void onMessage(final Context context, final Intent intent) {
        String message = (null != intent ? intent.getStringExtra(AgooConstants.MESSAGE_BODY) : "");

        Log.e(TAG, "onMessage(): [" + message + "]");
        if (TextUtils.isEmpty(message)) {
            return;
        }

        try{
            JSONObject jsonObject = JSON.parseObject(message);

            String title = jsonObject.getString("title");
            String ticker = jsonObject.getString("ticker");
            String text = jsonObject.getString("text");


            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setContentTitle(title);
            builder.setTicker(ticker);
            builder.setContentText(text);


            Intent intent1 = new Intent(this,DebugActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent1,PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);


            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(notificationIndex.getAndIncrement(), builder.build());

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
