package com.example.apple.easyspend.common.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apple.easyspend.R;
import com.example.apple.easyspend.utils.StatusBarUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EmptyActivity extends AppCompatActivity {

    @Bind(R.id.toolbar_back)
    ImageView toolbarBack;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.nodate)
    ImageView nodate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);
        ButterKnife.bind(this);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary), 30);
        String title = getIntent().getStringExtra("title");
        toolbarTitle.setText(title);
    }

    @OnClick(R.id.toolbar_back)
    public void onViewClicked() {
        finish();
    }
}
