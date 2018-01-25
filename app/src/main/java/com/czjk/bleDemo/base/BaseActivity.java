package com.czjk.bleDemo.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.czjk.bleDemo.R;
import com.czjk.bleDemo.manager.ScreenManager;

import butterknife.ButterKnife;

public class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    public Context mContext;
    public Resources mResources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mContext = this;
        mResources = getResources();
        ScreenManager.getScreenManager().pushActivity(this);
    }


    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        initTitleBar();
        initParams();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent != null) {
            setIntent(intent);
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.push_in_right, R.anim.push_out_left);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.push_in_right, R.anim.push_out_left);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.push_in_left, R.anim.push_out_right);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.bind(this);
        ScreenManager.getScreenManager().popActivity(this);
    }

    public void initTitleBar() {
    }

    public void initParams() {
    }


    public void exit() {
        MyApplication.getInstance().killMyself();
    }

}
