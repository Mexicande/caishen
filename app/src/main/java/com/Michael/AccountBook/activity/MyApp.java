package com.Michael.AccountBook.freecash.activity;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;


import com.Michael.AccountBook.freecash.common.Contacts;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpHeaders;
import com.meituan.android.walle.WalleChannelReader;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2016/6/28.
 */
public class MyApp extends Application {
    //dao
    public static String today_date = "";

    private static MyApp instance;

    public static SharedPreferences sp;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initOkGo();
        sp = super.getSharedPreferences("eSetting", Context.MODE_PRIVATE);
        //只能被本应用访问
    }

    public static MyApp getApp(){
        return instance;
    }

    private void initOkGo() {
        String channel = WalleChannelReader.getChannel(this.getApplicationContext());
        MobclickAgent.startWithConfigure(new MobclickAgent.UMAnalyticsConfig(this,Contacts.UMENG_KEY
                ,channel));
        HttpHeaders parser=new HttpHeaders();
        parser.put("channel",channel);
        OkGo.getInstance().init(this)
                .addCommonHeaders(parser);
    }


}
