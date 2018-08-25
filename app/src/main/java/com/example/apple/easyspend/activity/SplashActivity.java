package com.example.apple.easyspend.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.example.apple.easyspend.R;
import com.example.apple.easyspend.common.Api;
import com.example.apple.easyspend.common.ApiService;
import com.example.apple.easyspend.common.OnRequestDataListener;
import com.example.apple.easyspend.common.SPUtil;
import com.example.apple.easyspend.common.activity.GuideActivity;
import com.example.apple.easyspend.common.activity.HomeActivity;
import com.example.apple.easyspend.utils.SharedPreferencesUtil;
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
        StatusBarUtil.setColor(this, getResources().getColor(R.color.theme_color),40);

        boolean open = SPUtil.getBoolean(SplashActivity.this, "open", false);
      /*  if(!open){
            setUrl();
        }else {
            mHandler.sendEmptyMessageDelayed(3, 1000);
        }*/
        mHandler.sendEmptyMessageDelayed(3, 1000);

    }
    private void setUrl() {

        Map<String,String>map=new HashMap<>();
        String name = getResources().getString(R.string.appName);
        String channel = WalleChannelReader.getChannel(this.getApplicationContext());
        map.put("name",name);
        map.put("market",channel);
        ApiService.GET_SERVICE(Api.STATUS.getStatus, map, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject json) {

                try {
                    JSONObject data = json.getJSONObject("data");
                    int status = data.getInt("status");
                    if(status==0){
                        mHandler.sendEmptyMessageDelayed(2
                                , 1000);
                    }else {
                        SPUtil.putBoolean(SplashActivity.this,"open",true);
                        setWelcome();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void requestFailure(int code, String msg) {

            }
        });

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
                    case 2:
                        MainActivity.launch(activity);
                        activity.finish();
                        break;
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
