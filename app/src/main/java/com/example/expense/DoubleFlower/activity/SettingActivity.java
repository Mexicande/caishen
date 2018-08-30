package com.example.expense.DoubleFlower.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.expense.DoubleFlower.R;
import com.example.expense.DoubleFlower.dao.DaoSession;
import com.example.expense.DoubleFlower.dao.User;
import com.example.expense.DoubleFlower.dao.UserDao;
import com.example.expense.DoubleFlower.utils.BitMapUtil;
import com.example.expense.DoubleFlower.utils.FastBlur;
import com.example.expense.DoubleFlower.utils.FileUitlity;
import com.example.expense.DoubleFlower.utils.StatusBarUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView topImg;
    private View set_top, passWord, my_info, checkVersion;
    private View setting_top;
    private Button login_bt;
    private TextView userName, my_says;
    private ImageView headImg;

    private MyApp myApp;
    private User user;
    private DaoSession daoSession;
    private UserDao userDao;
    private SharedPreferences sharedPreferences;
    private File mCropFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        myApp = MyApp.getApp();
        myApp.addToList(this);
        user = MyApp.user;
        sharedPreferences = super.getSharedPreferences("says", Context.MODE_PRIVATE);
        daoSession = myApp.getDaoSession(this);
        userDao = daoSession.getUserDao();
        initView();
    }

    @Override
    public void onStart() {
        super.onStart();
        StatusBarUtil.setColor(this, Color.parseColor("#607d8b"), 0);
        user = MyApp.user;
        userName.setText(user.getU_name());
        my_says.setText(getSay());
    }

    //获得签名
    private String getSay() {
        return sharedPreferences.getString("person_say", "");
    }

    private void initView() {
        setting_top = findViewById(R.id.setting_top);
        topImg = (ImageView) findViewById(R.id.set_top_img);
        passWord = findViewById(R.id.passWord);//访问密码设置
        userName = (TextView) findViewById(R.id.title);
        my_info = findViewById(R.id.my_info);
        my_says = (TextView) findViewById(R.id.my_says);
        headImg = (ImageView) findViewById(R.id.headImg);
        checkVersion = findViewById(R.id.checkVersion);

        userName.setText(user.getU_name());
        if (!"".equals(user.getU_img())) {
            //Bitmap bitmap = getLoacalBitmap(user.getU_img());
            Bitmap bitmap1 = BitMapUtil.getBitmap(user.getU_img(), 100, 100);
            headImg.setImageBitmap(bitmap1);
        }

        passWord.setOnClickListener(this);
        my_info.setOnClickListener(this);
        headImg.setOnClickListener(this);
        my_says.setOnClickListener(this);
        userName.setOnClickListener(this);
        checkVersion.setOnClickListener(this);

        setHeight();//设置top高度
        zoomApplyBlur();//模糊背景

    }

    //设置高度
    private void setHeight() {
        //获取屏幕高宽
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int windowsHeight = metric.heightPixels;
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) setting_top.getLayoutParams();
        params.height = windowsHeight / 3;
        setting_top.setLayoutParams(params);
    }

    //zoom背景模糊
    private void zoomApplyBlur() {
        topImg.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                topImg.getViewTreeObserver().removeOnPreDrawListener(this);
                topImg.buildDrawingCache();

                Bitmap bmp = topImg.getDrawingCache();
                blur(bmp, setting_top);
                return true;
            }
        });
    }

    //图片模糊
    private void blur(Bitmap bkg, View view) {
        float scaleFactor = 6;
        float radius = 20;
        Bitmap overlay = Bitmap.createBitmap((int) (view.getMeasuredWidth() / scaleFactor),
                (int) (view.getMeasuredHeight() / scaleFactor), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.translate(-view.getLeft() / scaleFactor, -view.getTop() / scaleFactor);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bkg, 0, 0, paint);
        overlay = FastBlur.doBlur(overlay, (int) radius, true);
        view.setBackground(new BitmapDrawable(getResources(), overlay));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.passWord:
                Intent intent3 = new Intent(SettingActivity.this, BaseSettingActivity.class);
                startActivity(intent3);
                break;
            case R.id.my_info:
                Intent intent = new Intent(SettingActivity.this, UserBaseInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.title:
                Intent intent1 = new Intent(SettingActivity.this, UserBaseInfoActivity.class);
                startActivity(intent1);
                break;
            case R.id.headImg:
                changeHeadImg();
                break;
            case R.id.my_says:
                Intent intent2 = new Intent(SettingActivity.this, UserBaseInfoActivity.class);
                startActivity(intent2);
                break;
            case R.id.checkVersion:
                toast("已是最新版本");
                break;
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyUp(keyCode, event);
    }

    /**
     * 加载本地图片
     *
     * @param url
     * @return
     */
    public Bitmap getLoacalBitmap(String url) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //dialog
    private MaterialDialog dialog = null;
    private File file;

    private void changeHeadImg() {
        dialog = new MaterialDialog.Builder(this)
                .title(" ")
                .titleColor(Color.parseColor("#546e7a"))
                .customView(R.layout.change_headimg_dialog, true)
                .positiveColor(Color.parseColor("#546e7a"))
                .negativeText(android.R.string.cancel)
                .negativeColor(Color.parseColor("#546e7a"))
                .build();
        View dialog_view = dialog.getCustomView();
        View camare = dialog_view.findViewById(R.id.camare);
        View photos = dialog_view.findViewById(R.id.photos);
        camare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Uri fileUri = null;

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //创建文件路径
                file = FileUitlity.getInstance(getApplicationContext()).makeDir("head_image");
                //定义图片路径和名称
                path = file.getParent() + File.separatorChar + System.currentTimeMillis() + ".jpg";
                if (Build.VERSION.SDK_INT >= 24) {
                    fileUri = FileProvider.getUriForFile(getApplicationContext(), "com.example.apple.baika.provider", new File(path));
                } else {
                    fileUri = Uri.fromFile(new File(path));
                }
                //保存图片
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                //图片质量
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                startActivityForResult(intent, 1);
            }
        });
        photos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                file = FileUitlity.getInstance(getApplicationContext()).makeDir("head_image");
                //调用手机相册
                allPhoto();
            }
        });
        dialog.show();
    }

    //调用手机相册
    private void allPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 0);
    }

    private String path = "";
    private String img_name = "";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //如果返回码不为-1，则表示不成功
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == 0) {if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            File imgUri = new File(GetImagePath.getPath(this, data.getData()));
            Uri dataUri = FileProvider.getUriForFile
                    (this, "com.example.apple.baika.provider", imgUri);
            // Uri dataUri = getImageContentUri(data.getData());
            startPhoneZoom(dataUri);

        } else {
            Uri uri = data.getData();
            //crop(uri);
            startPhoneZoom(uri);
        }

        } else if (requestCode == 1) {
            Uri fileUri = null;
            if (Build.VERSION.SDK_INT >= 24) {
                fileUri = FileProvider.getUriForFile(getApplicationContext(), "com.example.apple.baika.provider", new File(path));
            } else {
                fileUri = Uri.fromFile(new File(path));
            }
            //相机返回结果，调用系统裁剪
            startPhoneZoom(fileUri);  //相机返回结果，调用系统裁剪
        } else if (requestCode == 2) {
            Uri inputUri = FileProvider.getUriForFile(this, "com.example.apple.baika.provider", mCropFile);//通过FileProvider创建一个content类型的Uri
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(inputUri));

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            headImg.setImageBitmap(bitmap);


/*            if (!"".equals(img_name)) {
                saveBitmap(bitmap, img_name);
                img_name = "";
            }
            user.setU_img(final_imgurl);
            headImg.setImageBitmap(bitmap);
            userDao.update(user);
            final_imgurl = "";
        }
    */
    }

}

    //调用系统裁剪
    private String final_imgurl = "";

    private void startPhoneZoom(Uri uri) {
        //定义图片路径和名称
        String path = file.getParent() + File.separatorChar + System.currentTimeMillis() + ".jpg";
        //裁剪后的File对象
        mCropFile = new File(path);
        final_imgurl=path;

        Uri outPutUri = Uri.fromFile(mCropFile);

        Intent intent = new Intent("com.android.camera.action.CROP");
        //sdk>=24
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            intent.setDataAndType(uri, "image/*");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutUri);
            intent.addFlags(FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(FLAG_GRANT_WRITE_URI_PERMISSION);

        } else {
            if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                String url = GetImagePath.getPath(this, uri);//这个方法是处理4.4以上图片返回的Uri对象不同的处理方法
                intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
            } else {
                intent.setDataAndType(uri, "image/*");
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutUri);
        }


        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        intent.putExtra("return-data", false);
        intent.putExtra("noFaceDetection", false);//去除默认的人脸识别，否则和剪裁匡重叠
        intent.putExtra("outputFormat", "JPEG");
        //intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());// 图片格式
        startActivityForResult(intent, 2);//这里就将裁剪后的图片的Uri返回了
    }
    /**
     * 保存bitmap
     */
    public void saveBitmap(Bitmap bm, String name) {
        File f = new File(file.getParent() + File.separatorChar, name);
        Log.i("---", f.toString());
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void toast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
}
