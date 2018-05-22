package com.example.apple.fivebuy.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.apple.fivebuy.R;
import com.example.apple.fivebuy.adapter.MyViewPagerAdapter;
import com.example.apple.fivebuy.adapter.NoTouchViewPager;
import com.example.apple.fivebuy.fragment.HomeFragment;
import com.example.apple.fivebuy.fragment.WelfareFragment;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.majiajie.pagerbottomtabstrip.NavigationController;
import me.majiajie.pagerbottomtabstrip.PageBottomTabLayout;
import me.majiajie.pagerbottomtabstrip.item.BaseTabItem;
import me.majiajie.pagerbottomtabstrip.item.NormalItemView;

public class HomeActivity extends AppCompatActivity {

    @Bind(R.id.tab)
    PageBottomTabLayout tab;
    private NavigationController navigationController;

    public  static MyViewPagerAdapter pagerAdapter;
    public  static NoTouchViewPager viewPager;


    public static void launch(Context context) {
        context.startActivity(new Intent(context, HomeActivity.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.theme_color),90);
        ButterKnife.bind(this);
        initView();
    }
    private void initView() {
        viewPager = (NoTouchViewPager) findViewById(R.id.viewPager);
        navigationController = tab.custom()
                .addItem(newItem(R.mipmap.iv_home, R.mipmap.iv_home_select,"主页"))
                .addItem(newItem(R.mipmap.iv_welfare, R.mipmap.iv_welfare_select,"福利"))
                .build();

        ArrayList<Fragment> list=new ArrayList<>();
        list.add(new HomeFragment());
        list.add(new WelfareFragment());
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
        normalItemView.setTextCheckedColor(getResources().getColor(R.color.theme_color));
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
