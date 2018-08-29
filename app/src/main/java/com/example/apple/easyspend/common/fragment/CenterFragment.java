package com.example.apple.easyspend.common.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.apple.easyspend.R;
import com.example.apple.easyspend.bean.LoginEvent;
import com.example.apple.easyspend.common.ActivityUtils;
import com.example.apple.easyspend.common.Contacts;
import com.example.apple.easyspend.common.SPUtil;
import com.example.apple.easyspend.common.activity.EmptyActivity;
import com.example.apple.easyspend.common.activity.FeedbackActivity;
import com.example.apple.easyspend.common.activity.HtmlActivity;
import com.example.apple.easyspend.common.activity.LoginActivity;
import com.example.apple.easyspend.common.activity.SettingActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 *
 * @author apple
 * 我的
 */
public class CenterFragment extends Fragment {


    @Bind(R.id.tv_login)
    TextView tvLogin;
    @Bind(R.id.linearLayout2)
    LinearLayout linearLayout2;
    private String token;
    private final int LOGIN_REQUESTION=10000;
    private final int LOAN_REQUESTION=20000;
    private final int FREE_REQUESTION=30000;
    private final int RESULT_CODE=200;
    public CenterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_center, container, false);
        ButterKnife.bind(this, view);
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        initView();

        return view;
    }

    private void initView() {
        token = SPUtil.getString(Contacts.TOKEN);
        String phone = SPUtil.getString(Contacts.PHONE);
        if(!TextUtils.isEmpty(phone)){
            tvLogin.setText(phone);
        }

    }
    @Subscribe
    public void getLogin(LoginEvent event){
        if(event.msg!=null){
            tvLogin.setText(event.msg);
        }else {
            tvLogin.setText("未登录");
        }
        token = SPUtil.getString(Contacts.TOKEN);
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.super_schedule, R.id.super_free, R.id.super_safe,
            R.id.super_feedback, R.id.super_setting,R.id.layout_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.super_schedule:
                token = SPUtil.getString(Contacts.TOKEN);
                if(TextUtils.isEmpty(token)){
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(intent,LOAN_REQUESTION);
                }else {
                    Intent intent = new Intent(getActivity(), EmptyActivity.class);
                    intent.putExtra("title","贷款进度");
                    startActivity(intent);
                }
                break;
            case R.id.super_free:
                token = SPUtil.getString(Contacts.TOKEN);
                if(TextUtils.isEmpty(token)){
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(intent,FREE_REQUESTION);
                }else {
                    Intent intent = new Intent(getActivity(), EmptyActivity.class);
                    intent.putExtra("title","我的免息券");
                    startActivity(intent);
                }
                break;
            case R.id.super_safe:
                Intent intent = new Intent(getActivity(), HtmlActivity.class);
                intent.putExtra("title", "安全小贴士");
                intent.putExtra("link", "http://m.anwenqianbao.com/#/minTips");
                startActivity(intent);
                break;
            case R.id.super_feedback:
                ActivityUtils.startActivity(FeedbackActivity.class);
                break;
            case R.id.super_setting:
                ActivityUtils.startActivity(SettingActivity.class);
                break;
            case R.id.layout_login:
                token = SPUtil.getString(Contacts.TOKEN);
                if(TextUtils.isEmpty(token)){
                    Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(loginIntent,LOGIN_REQUESTION);
                }
                break;
            default:
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case LOGIN_REQUESTION:
                if(resultCode==RESULT_CODE){
                    String phone = data.getStringExtra("phone");
                    tvLogin.setText(phone);
                    token = SPUtil.getString(Contacts.TOKEN);
                }
                break;
            case LOAN_REQUESTION:
                if(resultCode==RESULT_CODE){
                    token = SPUtil.getString(Contacts.TOKEN);
                    Intent intent = new Intent(getActivity(), EmptyActivity.class);
                    intent.putExtra("title","贷款进度");
                    startActivity(intent);
                }
                break;
            case FREE_REQUESTION:
                if(resultCode==RESULT_CODE){
                    token = SPUtil.getString(Contacts.TOKEN);
                    Intent intent = new Intent(getActivity(), EmptyActivity.class);
                    intent.putExtra("title","我的免息券");
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
