package com.example.expense.DoubleFlower.common;

import com.example.expense.DoubleFlower.R;
import com.example.expense.DoubleFlower.activity.MyApp;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;


/**
 * Created by apple on 2018/4/13.
 */

public class ApiService {
    /**
     * @param listener
     * banner
     */
    public static void GET_SERVICE(String url,JSONObject jsonObject, final OnRequestDataListener listener) {
        newExcuteJsonPost(url,jsonObject,listener);
    }

    private static void newExcuteJsonPost(String url, JSONObject jsonObject, final OnRequestDataListener listener){
        final String netError = MyApp.getApp().getString(R.string.net_error);
        OkGo.<String>post(url)
                .tag(MyApp.getApp())
                .upJson(jsonObject)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if(response.body()!=null){
                            try {
                                JSONObject jsonObject=new JSONObject(response.body());
                                int code = jsonObject.getInt("error_code");
                                if(code==0){
                                    listener.requestSuccess(code, jsonObject);
                                }else {
                                    listener.requestFailure(code, jsonObject.getString("error_message"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else {
                            listener.requestFailure(-1, netError);
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        listener.requestFailure(-1, netError);

                    }
                });


    }

}
