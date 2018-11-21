package com.Michael.AccountBook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.Michael.AccountBook.utils.SharedPreferencesUtil;
import com.Michael.AccountBook.R;
import com.Michael.AccountBook.common.Api;
import com.Michael.AccountBook.common.ApiService;
import com.Michael.AccountBook.common.OnRequestDataListener;
import com.Michael.AccountBook.common.SPUtil;
import com.Michael.AccountBook.common.activity.GuideActivity;
import com.Michael.AccountBook.common.activity.HomeActivity;
import com.Michael.AccountBook.utils.SharedPreferencesUtil;
import com.jaeger.library.StatusBarUtil;
import com.meituan.android.walle.WalleChannelReader;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * @author apple
 *
 */
public class SplashActivity extends AppCompatActivity {
    private SwitchHandler mHandler = new SwitchHandler(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setWelcome();
        mHandler.sendEmptyMessageDelayed(3, 1000);

    }




    private static class SwitchHandler extends Handler {
        private WeakReference<SplashActivity> mWeakReference;

        SwitchHandler(SplashActivity activity) {
            mWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            SplashActivity activity = mWeakReference.get();
            if (activity != null) {
                switch (msg.what){
                    case 3:
                        HomeActivity.launch(activity);
                        activity.finish();
                        break;
                    default:
                        break;
                }
            }
        }
    }
    private void setWelcome(){
        boolean isFirstOpen = SharedPreferencesUtil.getBoolean(SplashActivity.this, SharedPreferencesUtil.FIRST_OPEN, true);
        if (isFirstOpen) {
            Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
            startActivity(intent);
            finish();
            return;
        }else {
            mHandler.sendEmptyMessageDelayed(3, 1000);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
