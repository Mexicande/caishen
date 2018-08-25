package com.example.apple.easyspend.common.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.apple.easyspend.R;
import com.example.apple.easyspend.activity.MyApp;
import com.example.apple.easyspend.common.Api;
import com.example.apple.easyspend.common.ApiService;
import com.example.apple.easyspend.common.CommonUtil;
import com.example.apple.easyspend.common.Contacts;
import com.example.apple.easyspend.common.OnRequestDataListener;
import com.example.apple.easyspend.common.SPUtil;
import com.example.apple.easyspend.common.SlideView;
import com.example.apple.easyspend.common.VerListener;
import com.example.apple.easyspend.common.VerificationFragment;
import com.example.apple.easyspend.utils.CaptchaTimeCount;
import com.example.apple.easyspend.utils.RegexUtils;
import com.example.apple.easyspend.utils.ToastUtils;
import com.jaeger.library.StatusBarUtil;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * @author apple
 */
public class LoginActivity extends AppCompatActivity implements VerListener {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.tv_phone)
    TextView tvPhone;
    @Bind(R.id.bt_code)
    Button btCode;
    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.login_phone_r2)
    RelativeLayout loginPhoneR2;
    @Bind(R.id.tv_code)
    TextView tvCode;
    @Bind(R.id.et_code)
    EditText etCode;
    @Bind(R.id.layout_code)
    RelativeLayout layoutCode;
    @Bind(R.id.slideview)
    SlideView slideview;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.et_name)
    EditText etName;
    @Bind(R.id.tv_card)
    TextView tvCard;
    @Bind(R.id.et_card)
    EditText etCard;
    @Bind(R.id.bt_login)
    Button btLogin;
    @Bind(R.id.layout_name)
    LinearLayout layoutName;
    private CaptchaTimeCount captchaTimeCount;
    private int oldNew = 0;
    private  KProgressHUD hud;
    private String phone;
    private int isolduser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.white),40);
        ButterKnife.bind(this);
        captchaTimeCount = new CaptchaTimeCount(Contacts.Times.MILLIS_IN_TOTAL, Contacts.Times.COUNT_DOWN_INTERVAL, btCode, this);
        initView();

    }
    private void initView() {
        slideview.addSlideListener(new SlideView.OnSlideListener() {
            @Override
            public void onSlideSuccess() {
                if (layoutCode.getVisibility() == View.VISIBLE) {
                    String code = etCode.getText().toString();
                    if (!TextUtils.isEmpty(code) && code.length() == 4) {
                        verCode(code);
                        slideview.reset();
                    } else {
                        slideview.reset();
                        ToastUtils.showToast(MyApp.getApp(), "验证码错误");
                    }
                } else {
                    SPUtil.putString(LoginActivity.this,Contacts.TOKEN, phone);
                    if(isolduser==0){
                        String html = getIntent().getStringExtra("html");
                        startActivity(new Intent(LoginActivity.this, HtmlActivity.class).putExtra("html",html));
                        finish();
                    }else {
                        loginPhoneR2.setVisibility(View.GONE);
                        layoutName.setVisibility(View.VISIBLE);
                        slideview.setVisibility(View.GONE);
                        slideview.reset();
                    }
                }
            }
        });

        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDimAmount(0.5f);

    }

    /**
     * 验证码效验
     */

    private void verCode(String code) {

        hud.show();

        Map<String,String>map=new HashMap<>();
        map.put("userphone", phone);
        map.put("code", code);

        ApiService.GET_SERVICE(Api.LOGIN.CHECKCODE, map, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                hud.dismiss();

                try {
                    JSONObject date = data.getJSONObject("data");
                    String msg = date.getString("msg");
                    String isSucess = date.getString("isSuccess");
                    if ("1".equals(isSucess)) {
                        SPUtil.putString(LoginActivity.this,Contacts.TOKEN, phone);
                        if(isolduser==0){
                            String html = getIntent().getStringExtra("html");
                            startActivity(new Intent(LoginActivity.this, HtmlActivity.class).putExtra("html",html));
                            finish();
                        }else {
                            layoutName.setVisibility(View.VISIBLE);
                            slideview.setVisibility(View.GONE);
                            layoutCode.setVisibility(View.GONE);
                            loginPhoneR2.setVisibility(View.GONE);
                        }
                    } else {
                        slideview.reset();
                    }
                    ToastUtils.showToast(MyApp.getApp(), msg);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void requestFailure(int code, String msg) {
                slideview.reset();
                hud.dismiss();
                ToastUtils.showToast(MyApp.getApp(), msg);
            }
        });

    }
    @Override
    public void success() {
        isOldUser();
    }

    /**
     * isOldUser
     * 新老用户
     */
    private void isOldUser() {
        Map<String,String>map=new HashMap<>();
        map.put("userphone", phone);

        ApiService.GET_SERVICE(Api.LOGIN.isOldUser, map, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                try {
                    JSONObject date = data.getJSONObject("data");
                    oldNew = date.getInt("isolduser");
                    isolduser = date.getInt("iden_status");
                    fillData(oldNew);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void requestFailure(int code, String msg) {
                ToastUtils.showToast(MyApp.getApp(), msg);
            }
        });
    }

    /**
     * 新老用户
     *
     * @param isolduser 1：老用户 0：新用户
     */
    private void fillData(int isolduser) {
        if (isolduser == 1) {
            if (layoutCode.getVisibility() == View.VISIBLE) {
                layoutCode.setVisibility(View.GONE);
            }
            slideview.setVisibility(View.VISIBLE);
        } else {
            layoutCode.setVisibility(View.VISIBLE);
            layoutName.setVisibility(View.GONE);
            getCode();
        }

    }

    /**
     * 验证码获取
     */
    private void getCode() {
        captchaTimeCount.start();

        Map<String,String>map=new HashMap<>();
        map.put("userphone", phone);
        ApiService.GET_SERVICE(Api.LOGIN.CODE, map, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                try {
                    JSONObject date = data.getJSONObject("data");
                    String msg = date.getString("msg");
                    String isSucess = date.getString("isSuccess");
                    if ("1".equals(isSucess)) {
                        slideview.setVisibility(View.VISIBLE);
                    }
                    ToastUtils.showToast(MyApp.getApp(), msg);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void requestFailure(int code, String msg) {
                ToastUtils.showToast(MyApp.getApp(), msg);
            }
        });

    }


    @OnClick({R.id.back, R.id.bt_code,R.id.bt_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.bt_code:
                verPhone();
                break;
            case R.id.bt_login:
                break;
            default:
                break;
        }
    }




    private void verPhone() {
        phone = etPhone.getText().toString();
        boolean b = CommonUtil.checkPhone(phone, true);
        if (b) {
            VerificationFragment verification = new VerificationFragment();
            verification.show(getSupportFragmentManager(), "ver");
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                v.setFocusable(false);
                v.setFocusableInTouchMode(true);
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (im != null) {
                im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
}
