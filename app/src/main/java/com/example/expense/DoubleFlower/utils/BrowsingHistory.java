package com.example.expense.DoubleFlower.utils;


import com.example.expense.DoubleFlower.R;
import com.example.expense.DoubleFlower.activity.MyApp;
import com.example.expense.DoubleFlower.common.Api;
import com.example.expense.DoubleFlower.common.ApiService;
import com.example.expense.DoubleFlower.common.Contacts;
import com.example.expense.DoubleFlower.common.OnRequestDataListener;
import com.example.expense.DoubleFlower.common.SPUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.meituan.android.walle.WalleChannelReader;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by tantan on 2017/7/14.
 */

public class BrowsingHistory {
    public  void execute(final String prdId) {

        String token = SPUtil.getString(Contacts.TOKEN);
        String channel = WalleChannelReader.getChannel(MyApp.getApp());
        String netError = MyApp.getApp().getString(R.string.app_name);

        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("token",token);
            jsonObject.put("product_id",prdId);
            jsonObject.put("app_name",netError);
            jsonObject.put("channel","jiehuahua");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ApiService.GET_SERVICE(Api.APPLY, jsonObject, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject json) {

            }

            @Override
            public void requestFailure(int code, String msg) {

            }
        });


    }
}
