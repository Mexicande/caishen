package com.cn.chars.freecash.common.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.chars.freecash.utils.ToastUtils;
import com.cn.chars.freecash.R;
import com.jaeger.library.StatusBarUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class HtmlActivity extends AppCompatActivity {


    @Bind(R.id.bar)
    ProgressBar bar;
    @Bind(R.id.webView)
    WebView webView;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary), 90);
        ButterKnife.bind(this);
        getDate();


    }



    private void getDate() {
        String title = getIntent().getStringExtra("title");
        toolbarTitle.setText(title);
        String html = getIntent().getStringExtra("link");

        if (html != null) {
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
            webSettings.setUseWideViewPort(true);
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setBlockNetworkImage(false);
            webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
            webSettings.setGeolocationEnabled(true);
            webSettings.setDomStorageEnabled(true);
            webSettings.setDatabaseEnabled(true);
            webSettings.setAppCacheEnabled(true);
            webSettings.setSupportZoom(false);
            webSettings.setNeedInitialFocus(false);
            webSettings.setLoadsImagesAutomatically(true);
            webSettings.setBuiltInZoomControls(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                webView.getSettings().setMixedContentMode(
                        WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
            }
            if (Build.VERSION.SDK_INT >= 21) {
                webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }
            webView.loadUrl(html);

            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if (parseScheme(url)) {

                    } else {
                        WebView.HitTestResult hitTestResult = view.getHitTestResult();
                        if (!TextUtils.isEmpty(url) && hitTestResult == null) {
                            view.loadUrl(url);
                            return true;
                        }
                    }
                    return false;
                }
            });
            webView.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int i, KeyEvent keyEvent) {
                    if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {

                        if (i == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
                            webView.goBack();
                            return true;
                        }
                    }
                    return false;
                }
            });
            webView.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    // TODO 自动生成的方法存根

                    if (newProgress == 100) {
                        bar.setVisibility(View.GONE);
                        //加载完网页进度条消失
                    } else {
                        bar.setVisibility(View.VISIBLE);
                        //开始加载网页时显示进度条
                        bar.setProgress(newProgress);
                        //设置进度值
                    }
                }
            });
        }
    }

    public boolean parseScheme(String url) {
        if (url.contains("platformapi/startapp")) {
            try {
                Uri uri = Uri.parse(url);
                Intent intent;
                intent = Intent.parseUri(url,
                        Intent.URI_INTENT_SCHEME);
                intent.addCategory("android.intent.category.BROWSABLE");
                intent.setComponent(null);
                startActivity(intent);
            } catch (Exception e) {
                ToastUtils.showToast(this, "请安装最新版支付宝");
            }
            return true;
        } else if (url.contains("qqapi")) {
            try {
                Uri uri = Uri.parse(url);
                Intent intent;
                intent = Intent.parseUri(url,
                        Intent.URI_INTENT_SCHEME);
                intent.addCategory("android.intent.category.BROWSABLE");
                intent.setComponent(null);
                startActivity(intent);
            } catch (Exception e) {
                ToastUtils.showToast(this, "请安装最新版腾讯QQ");
            }
            return true;
        } else if (url.contains("tmast://appdetails?")) {
            try {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(HtmlActivity.this, "请安装最新版应用宝", Toast.LENGTH_SHORT).show();
            }
            return true;
        } else if (url.endsWith(".apk")) {
            downloadApk(url);
            return true;

        } else {
            return false;
        }

    }

    /**
     * 应用内拦截下载
     */
    private void downloadApk(String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        webView.destroy();
    }

    @OnClick(R.id.toolbar_back)
    public void onViewClicked() {
        finish();
    }
}
