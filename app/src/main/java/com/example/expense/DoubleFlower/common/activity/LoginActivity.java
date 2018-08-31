package com.example.expense.DoubleFlower.common.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.allen.library.SuperButton;
import com.example.expense.DoubleFlower.R;
import com.example.expense.DoubleFlower.activity.MyApp;
import com.example.expense.DoubleFlower.bean.LoginEvent;
import com.example.expense.DoubleFlower.common.Api;
import com.example.expense.DoubleFlower.common.ApiService;
import com.example.expense.DoubleFlower.common.CodeUtils;
import com.example.expense.DoubleFlower.common.Contacts;
import com.example.expense.DoubleFlower.common.OnRequestDataListener;
import com.example.expense.DoubleFlower.common.SPUtil;
import com.example.expense.DoubleFlower.utils.BrowsingHistory;
import com.example.expense.DoubleFlower.utils.CaptchaTimeCount;
import com.example.expense.DoubleFlower.utils.Constants;
import com.example.expense.DoubleFlower.utils.ToastUtils;
import com.example.expense.DoubleFlower.utils.editext.PowerfulEditText;
import com.jaeger.library.StatusBarUtil;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.greenrobot.eventbus.EventBus;
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
public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.ed_phone)
    PowerfulEditText edPhone;
    @Bind(R.id.result)
    ImageView result;
    @Bind(R.id.verify_iv)
    ImageView verifyIv;
    @Bind(R.id.et_Result)
    EditText etResult;
    @Bind(R.id.layout_Result)
    RelativeLayout layoutResult;
    @Bind(R.id.ed_code)
    PowerfulEditText edCode;
    @Bind(R.id.bt_code)
    Button btCode;
    @Bind(R.id.layout_code)
    RelativeLayout layoutCode;
    @Bind(R.id.bt_login)
    SuperButton btLogin;
    private CaptchaTimeCount captchaTimeCount;
    private int oldNew = 0;
    private KProgressHUD hud;
    private String phone;
    private int isolduser;

    private CodeUtils codeUtils;
    private String yanZhengResult;
    private String etYanZhengCode;
    private String yanZhengCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.white), 40);
        ButterKnife.bind(this);
        initView();
        setListener();
        initYanzheng();

    }

    private void initView() {
        captchaTimeCount = new CaptchaTimeCount(Constants.MILLIS_IN_TOTAL, Constants.COUNT_DOWN_INTERVAL, btCode, this);

        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDimAmount(0.5f);

    }
    private void setListener() {
        edPhone.addTextListener(new PowerfulEditText.TextListener() {
            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {
                if (!etResult.getText().toString().isEmpty()&& s.toString().length() == 11) {
                    btLogin.setEnabled(true);
                    btLogin.setUseShape();
                } else {
                    btLogin.setEnabled(false);
                    btLogin.setUseShape();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etResult.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (edPhone.getText().toString().length() == 11 && !s.toString().isEmpty() ) {
                    btLogin.setEnabled(true);
                    btLogin.setUseShape();
                } else {
                    btLogin.setEnabled(false);
                    btLogin.setUseShape();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edCode.addTextListener(new PowerfulEditText.TextListener() {
            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {
                if (edPhone.getText().toString().length() == 11 && s.toString().length()==4 ) {
                    btLogin.setEnabled(true);
                    btLogin.setUseShape();
                } else {
                    btLogin.setEnabled(false);
                    btLogin.setUseShape();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void initYanzheng() {
        codeUtils = CodeUtils.getInstance();
        Bitmap bitmap = codeUtils.createBitmap();
        verifyIv.setImageBitmap(bitmap);
        yanZhengCode = codeUtils.getCode();
        yanZhengResult = codeUtils.getResult() + "";
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
    /**
     * 验证码效验
     */
    private void verCode(String code) {
        hud.show();
        phone = edPhone.getText().toString();

        JSONObject object=new JSONObject();
        try {
            object.put("userphone",phone);
            object.put("app_name",getString(R.string.app_name));
            object.put("terminal","2");
            object.put("code",code);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        ApiService.GET_SERVICE(Api.LOGIN.CHECKCODE, object, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                hud.dismiss();

                try {
                    JSONObject date = data.getJSONObject("data");
                        String token = date.getString("token");
                        SPUtil.putString(Contacts.TOKEN, token);
                        SPUtil.putString( Contacts.PHONE, phone);
                        EventBus.getDefault().post(new LoginEvent(phone));
                        String title = getIntent().getStringExtra("title");
                        String link = getIntent().getStringExtra("link");
                        if(!TextUtils.isEmpty(title)){
                            String id = getIntent().getStringExtra("id");
                            new BrowsingHistory().execute(id);
                            Intent intent=new Intent(LoginActivity.this, HtmlActivity.class);
                            intent.putExtra("title",title);
                            intent.putExtra("link",link);
                            startActivity(intent);
                        }else {
                            Intent intent=new Intent();
                            intent.putExtra("phone",phone);
                            setResult(200,intent);
                        }
                        finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void requestFailure(int code, String msg) {
                hud.dismiss();
                ToastUtils.showToast(MyApp.getApp(), msg);
            }
        });

    }

    /**
     * isOldUser
     * 新老用户
     */
    private void isOldUser() {
        hud.show();
        phone = edPhone.getText().toString();

        JSONObject object=new JSONObject();
        try {
            object.put("userphone",phone);
            object.put("app_name",getString(R.string.app_name));
            object.put("terminal","2");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiService.GET_SERVICE(Api.LOGIN.isOldUser, object, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                hud.dismiss();
                try {
                    JSONObject date = data.getJSONObject("data");
                    oldNew = date.getInt("is_user");
                    if (oldNew == 0) {
                        String token = date.getString("token");
                        SPUtil.putString(Contacts.TOKEN, token);
                        SPUtil.putString(Contacts.PHONE, phone);
                        layoutCode.setVisibility(View.GONE);
                        EventBus.getDefault().post(new LoginEvent(phone));
                        String title = getIntent().getStringExtra("title");
                        String link = getIntent().getStringExtra("link");
                        if(!TextUtils.isEmpty(title)){
                            String id = getIntent().getStringExtra("id");
                            new BrowsingHistory().execute(id);
                            Intent intent=new Intent(LoginActivity.this, HtmlActivity.class);
                            intent.putExtra("title",title);
                            intent.putExtra("link",link);
                            startActivity(intent);
                        }else {
                            Intent intent=new Intent();
                            intent.putExtra("phone",phone);
                            setResult(200,intent);
                        }
                        finish();

                    } else {
                        layoutResult.setVisibility(View.GONE);
                        layoutCode.setVisibility(View.VISIBLE);
                        btLogin.setEnabled(false);
                        btLogin.setUseShape();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void requestFailure(int code, String msg) {
                hud.dismiss();
                ToastUtils.showToast(MyApp.getApp(), msg);
            }
        });
    }

    @OnClick({R.id.back, R.id.bt_code, R.id.bt_login, R.id.verify_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                break;
            case R.id.bt_code:
                isOldUser();
                break;
            case R.id.bt_login:
                if (oldNew == 0) {
                    etYanZhengCode = etResult.getText().toString().trim();
                    if (TextUtils.isEmpty(etYanZhengCode)) {
                        ToastUtils.showToast(this, "请输入图片里的结果");
                        return;
                    }
                    if (!yanZhengResult.equals(etYanZhengCode)) {
                        ToastUtils.showToast(this, "图片结果输入有误");
                        etResult.getText().clear();
                        initYanzheng();
                    } else {
                        isOldUser();
                    }
                } else {
                    String code = edCode.getText().toString();
                    verCode(code);
                }

                break;
            case R.id.verify_iv:
                initYanzheng();
                break;
        }
    }
}
