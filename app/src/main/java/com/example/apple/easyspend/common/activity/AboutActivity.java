package com.example.apple.easyspend.common.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apple.easyspend.R;
import com.example.apple.easyspend.common.AppUtils;
import com.example.apple.easyspend.utils.StatusBarUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutActivity extends AppCompatActivity {

    @Bind(R.id.toolbar_back)
    ImageView toolbarBack;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.version)
    TextView version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary), 30);
        initView();
    }

    private void initView() {

        toolbarBack.setVisibility(View.VISIBLE);
        toolbarTitle.setText("关于我们");
        String appVersionName = AppUtils.getAppVersionName();
        version.setText(getString(R.string.app_name) + "V" + appVersionName);
    }

    @OnClick(R.id.toolbar_back)
    public void onViewClicked() {
        finish();
    }
}
