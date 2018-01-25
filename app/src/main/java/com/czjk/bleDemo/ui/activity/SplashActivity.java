package com.czjk.bleDemo.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.czjk.bleDemo.R;
import com.czjk.bleDemo.base.BaseActivity;
import com.czjk.bleDemo.utils.PermissionsChecker;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.RECEIVE_SMS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class SplashActivity extends BaseActivity {
    private static final long ALPHA_DURATION = 2000;
    public static final int APPLY_PERMISSION = 1;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.tv)
    TextView tv;
    private PermissionsChecker permissionsChecker;
    static final String[] PERMISSIONS = new String[]{ACCESS_COARSE_LOCATION, CAMERA, WRITE_EXTERNAL_STORAGE, READ_PHONE_STATE, RECEIVE_SMS, READ_CONTACTS};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        permissionsChecker = new PermissionsChecker(mContext);
        beginAlpha();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == APPLY_PERMISSION && hasAllPermissionsGranted(grantResults)) {
            redirectTo();
        } else {
            Toast.makeText(this, "不授权无法使用，请重新安装", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void requestPermissions(String... permissions) {
        ActivityCompat.requestPermissions(this, permissions, APPLY_PERMISSION);
    }

    private boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    private void beginAlpha() {

        PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", 0.3f, 1f);
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 0.3f, 1f);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 0.3f, 1f);
        ObjectAnimator objectAnimator1 = ObjectAnimator.ofPropertyValuesHolder(img, alpha, scaleX, scaleY);
        ObjectAnimator objectAnimator2 = ObjectAnimator.ofPropertyValuesHolder(tv, alpha, scaleX, scaleY);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(objectAnimator1, objectAnimator2);
        animatorSet.setInterpolator(new AccelerateInterpolator());
        animatorSet.setDuration(ALPHA_DURATION);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                //没有考虑用户永久拒绝的情况
                if (permissionsChecker.lacksPermissions(PERMISSIONS)) {
                    requestPermissions(PERMISSIONS);
                } else {
                    redirectTo();
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animatorSet.start();
    }

    private void redirectTo() {
        startActivity(new Intent(mContext, MainActivity.class));
        finish();
    }
}
