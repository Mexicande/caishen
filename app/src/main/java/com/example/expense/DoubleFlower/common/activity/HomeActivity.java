package com.example.expense.DoubleFlower.common.activity;

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

import com.example.expense.DoubleFlower.R;
import com.example.expense.DoubleFlower.adapter.NoTouchViewPager;
import com.example.expense.DoubleFlower.common.Api;
import com.example.expense.DoubleFlower.common.MyViewPagerAdapter;
import com.example.expense.DoubleFlower.common.fragment.CenterFragment;
import com.example.expense.DoubleFlower.common.update.AppUpdateUtils;
import com.example.expense.DoubleFlower.common.update.OkGoUpdateHttpUtil;
import com.example.expense.DoubleFlower.common.update.UpdateAppBean;
import com.example.expense.DoubleFlower.common.update.UpdateAppManager;
import com.example.expense.DoubleFlower.common.update.UpdateCallback;
import com.example.expense.DoubleFlower.common.fragment.HomeFragment;
import com.example.expense.DoubleFlower.common.fragment.WelfareFragment;
import com.example.expense.DoubleFlower.utils.ToastUtils;
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
      //  StatusBarUtil.setColor(this, getResources().getColor(R.color.white),114);
        StatusBarUtil.setTranslucentForImageViewInFragment(this, null);
        ButterKnife.bind(this);
        AndPermission.with(this)
                .requestCode(200)
                .permission(Manifest.permission.READ_PHONE_STATE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .callback(listener)
                .start();

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
        list.add(new HomeFragment());
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

    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // 权限申请成功回调。
            // 和onActivityResult()的requestCode一样，用来区分多个不同的请求。
            if (requestCode == 200) {
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // 权限申请失败回调。
            ToastUtils.showToast(HomeActivity.this, "为了您的账号安全,请打开设备权限");
            if (requestCode == 200) {
                if ((AndPermission.hasAlwaysDeniedPermission(HomeActivity.this, deniedPermissions))) {
                    AndPermission.defaultSettingDialog(HomeActivity.this, 500).show();
                }
            }
        }
    };
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 500:
                // 这个400就是上面defineSettingDialog()的第二个参数。
                // 你可以在这里检查你需要的权限是否被允许，并做相应的操作。
                if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                } else {
                    ToastUtils.showToast(HomeActivity.this, "获取权限失败");
                }
                break;
            default:
                break;
        }
    }
}
