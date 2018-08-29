package com.example.apple.easyspend.utils;


import com.example.apple.easyspend.activity.MyApp;
import com.example.apple.easyspend.common.Api;
import com.example.apple.easyspend.common.Contacts;
import com.example.apple.easyspend.common.SPUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;



/**
 * Created by tantan on 2017/7/14.
 */

public class BrowsingHistory {
    public  void execute(final String prdId) {

        String token = SPUtil.getString(Contacts.TOKEN);
        OkGo.<String>post(Api.APPLY)
                .tag(MyApp.getApp())
                .params("token", token)
                .params("id", prdId)
                .execute(new StringCallback() {

                    @Override
                    public void onSuccess(Response<String> response) {

                    }
                });



    }
}
