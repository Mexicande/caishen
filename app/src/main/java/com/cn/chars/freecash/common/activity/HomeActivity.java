package com.cn.chars.freecash.common.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.cn.chars.freecash.adapter.NoTouchViewPager;
import com.cn.chars.freecash.common.fragment.CenterFragment;
import com.cn.chars.freecash.common.fragment.HomeFragment;
import com.cn.chars.freecash.common.fragment.MainFragment;
import com.cn.chars.freecash.common.fragment.WelfareFragment;
import com.cn.chars.freecash.utils.ToastUtils;
import com.cn.chars.freecash.R;
import com.cn.chars.freecash.adapter.NoTouchViewPager;
import com.cn.chars.freecash.common.Api;
import com.cn.chars.freecash.common.MyViewPagerAdapter;
import com.cn.chars.freecash.common.fragment.CenterFragment;
import com.cn.chars.freecash.common.fragment.HomeFragment;
import com.cn.chars.freecash.common.fragment.WelfareFragment;
import com.cn.chars.freecash.utils.ToastUtils;
import com.jaeger.library.StatusBarUtil;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.majiajie.pagerbottomtabstrip.NavigationController;
import me.majiajie.pagerbottomtabstrip.PageBottomTabLayout;
import me.majiajie.pagerbottomtabstrip.item.BaseTabItem;
import me.majiajie.pagerbottomtabstrip.item.NormalItemView;

public class HomeActivity extends AppCompatActivity {

    @Bind(R.id.tab)
    PageBottomTabLayout tab;
    public static  NavigationController navigationController;

    public  static MyViewPagerAdapter pagerAdapter;
    public  static NoTouchViewPager viewPager;
    private int newversioncode;

    public static void launch(Context context) {
        context.startActivity(new Intent(context, HomeActivity.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
    }
    private void initView() {
        viewPager = (NoTouchViewPager) findViewById(R.id.viewPager);
        navigationController = tab.custom()
                .addItem(newItem(R.mipmap.iv_home, R.mipmap.iv_home_select,"主页"))
                .addItem(newItem(R.mipmap.iv_welfare, R.mipmap.iv_welfare_select,"产品"))
                .addItem(newItem(R.mipmap.iv_center, R.mipmap.iv_center_select,"我的"))
                .build();
        ArrayList<Fragment> list=new ArrayList<>();
        list.add(new MainFragment());
        list.add(new WelfareFragment());
        list.add(new CenterFragment());
        pagerAdapter=new MyViewPagerAdapter(getSupportFragmentManager(),list);
        viewPager.setAdapter(pagerAdapter);
        //自动适配ViewPager页面切换
        navigationController.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(list.size());

    }


    //创建一个Item
    private BaseTabItem newItem(int drawable, int checkedDrawable, String text){
        NormalItemView normalItemView = new NormalItemView(this);
        normalItemView.initialize(drawable,checkedDrawable,text);
        normalItemView.setTextDefaultColor(Color.GRAY);
        normalItemView.setTextCheckedColor(getResources().getColor(R.color.colorPrimary));
        return normalItemView;
    }

    private long mLastBackTime = 0;
    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - mLastBackTime) < 1000) {
            finish();
        } else {
            mLastBackTime = System.currentTimeMillis();
            Toast.makeText(this, "请再确认一次", Toast.LENGTH_SHORT).show();
        }

    }
}
