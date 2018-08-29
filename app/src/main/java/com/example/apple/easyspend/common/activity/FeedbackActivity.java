package com.example.apple.easyspend.common.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apple.easyspend.R;
import com.example.apple.easyspend.utils.NetworkUtils;
import com.example.apple.easyspend.utils.StatusBarUtil;
import com.example.apple.easyspend.utils.ToastUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FeedbackActivity extends AppCompatActivity {

    @Bind(R.id.et_message)
    EditText etMessage;
    @Bind(R.id.apply)
    Button apply;
    @Bind(R.id.toolbar_back)
    ImageView toolbarBack;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);
        StatusBarUtil.setColor(this,getResources().getColor(R.color.colorPrimary),30);
        initView();
    }

    private void initView() {
        toolbarBack.setVisibility(View.VISIBLE);
        toolbarTitle.setText("意见反馈");
    }

    @OnClick({R.id.toolbar_back, R.id.apply})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                finish();
                break;
            case R.id.apply:
                boolean available = NetworkUtils.isAvailable(this);
                if(available){
                    if(!TextUtils.isEmpty(etMessage.getText().toString())){
                        ToastUtils.showToast(this,"感觉您的宝贵意见，我们将稍后作答");
                        finish();
                    }
                }else {
                    ToastUtils.showToast(this,"网络无法连接");
                }
                break;
            default:
                break;
        }
    }
}
