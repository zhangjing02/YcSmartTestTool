package com.czjk.bleDemo.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toast;

import com.czjk.bleDemo.R;
import com.czjk.bleDemo.base.BaseActivity;
import com.czjk.bleDemo.ui.fragment.DeviceFragment;
import com.czjk.bleDemo.ui.fragment.HomeFragment;
import com.czjk.blelib.model.BaseEvent;
import com.czjk.blelib.utils.Constant;
import com.czjk.blelib.utils.SPUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import senssun.ycblelib.YCProtocolUtils;


public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    private Fragment homeFragment, deviceFragment;
    private long mLastOnKeyBackTime = 0;
    String mac = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        toolbar.setTitle("主页");
        toolbar.setSubtitleTextAppearance(mContext, R.style.Style_Text);
        mac = SPUtil.getStringValue(Constant.SP_KEY_DEVICE_ADDRESS, null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mac != null && !YCProtocolUtils.getInstance().isConnDevice()) {
            YCProtocolUtils.getInstance().connect(mac);
            toolbar.setSubtitle("正在连接:"+mac);
        }
    }

    @Override
    public void initParams() {
        super.initParams();
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        homeFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.hold_fragment, homeFragment).commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else
                doExit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            toolbar.setTitle("主页");
            if (homeFragment == null) {
                homeFragment = new HomeFragment();
            }
            ft.replace(R.id.hold_fragment, homeFragment);
        } else if (id == R.id.nav_device) {
            toolbar.setTitle("设备");
            if (deviceFragment == null) {
                deviceFragment = new DeviceFragment();
            }
            ft.replace(R.id.hold_fragment, deviceFragment);
        }
        ft.commit();
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void doExit() {
        final long THRESHOLD = 2 * 1000;
        long curOnKeyBackTime = System.currentTimeMillis();
        long diffTime = curOnKeyBackTime - mLastOnKeyBackTime;
        mLastOnKeyBackTime = curOnKeyBackTime;
        if (diffTime > THRESHOLD) {
            Toast.makeText(mContext, "再按一次退出程序", Toast.LENGTH_SHORT).show();
        } else {
            exit();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        YCProtocolUtils.getInstance().disconnect();
        YCProtocolUtils.getInstance().clear();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void bleConnect(BaseEvent event) {
        BaseEvent.EventType type = event.getEventType();

        if (type == BaseEvent.EventType.RECONNECTION || type == BaseEvent.EventType.STATE_CONNECT_FAILURE) {
            YCProtocolUtils.getInstance().disconnect();
            mac = SPUtil.getStringValue(Constant.SP_KEY_DEVICE_ADDRESS, null);
            YCProtocolUtils.getInstance().connect(mac);
            toolbar.setSubtitle("正在连接:"+mac);
        }
        if (type == BaseEvent.EventType.STATE_CONNECTED) {
            toolbar.setSubtitle("已连接");
        }
        if (type == BaseEvent.EventType.STATE_DISCONNECTED) {
            toolbar.setSubtitle("未连接");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.i("mac",mac);
                    YCProtocolUtils.getInstance().connect(mac);
                    toolbar.setSubtitle("正在连接:"+mac);
                }
            }, 1000);
        }
    }


}
