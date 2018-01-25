package com.alibaba.alink.opensdksample.qrcode;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.aliyun.alink.page.pageroutor.ARouter;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

/**
 * Created by guikong on 17/6/23.
 */
public class QRCodeHelper {

    private static final int REQUEST_CODE_QR_CODE = 0x1000;

    public static void init(Context context){
        ZXingLibrary.initDisplayOpinion(context);
    }

    public static void open(Activity activity) {
        Intent intent = new Intent(activity, CaptureActivity.class);
        activity.startActivityForResult(intent, REQUEST_CODE_QR_CODE);
    }

    public static void onActivityResult(Context context, int requestCode, int resultCode, Intent data) {
        if (Activity.RESULT_OK == resultCode
                && REQUEST_CODE_QR_CODE == requestCode) {

            Bundle bundle = data.getExtras();

            if (bundle == null) {
                return;
            }

            if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                Toast.makeText(context, "解析二维码失败", Toast.LENGTH_LONG).show();
                return;
            }

            if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                String result = bundle.getString(CodeUtils.RESULT_STRING);

                ARouter.navigate(context, result);
                Toast.makeText(context, "解析结果:" + result, Toast.LENGTH_LONG).show();
            }
        }
    }
}
