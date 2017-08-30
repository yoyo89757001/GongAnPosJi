package com.example.xiaojun.gonganposji.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.anupcowkur.reservoir.Reservoir;
import com.anupcowkur.reservoir.ReservoirGetCallback;
import com.example.xiaojun.gonganposji.R;
import com.example.xiaojun.gonganposji.view.X5WebView;
import com.google.gson.reflect.TypeToken;
import com.sdsmdg.tastytoast.TastyToast;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;

public class ChaXunActivity extends Activity {
    private X5WebView webView;
    private TextView title;
    private ImageView famhui;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cha_xun);
        webView= (X5WebView) findViewById(R.id.webwiew);

        title= (TextView) findViewById(R.id.title);
        title.setText("比对记录");
        famhui= (ImageView) findViewById(R.id.leftim);
        famhui.setVisibility(View.VISIBLE);
        famhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        WebSettings webSetting = webView.getSettings();
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(false);
        // webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        // webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setAppCachePath(this.getDir("appcache", 0).getPath());
        webSetting.setDatabasePath(this.getDir("databases", 0).getPath());
        webSetting.setGeolocationDatabasePath(this.getDir("geolocation", 0)
                .getPath());
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

            }
        });

        Type resultType2 = new TypeToken<String>() {
        }.getType();
        Reservoir.getAsync("zhuji", resultType2, new ReservoirGetCallback<String>() {
            @Override
            public void onSuccess(final String i) {
                String str ="系统管理员"; //默认环境，已是UTF-8编码
                String strGBK = null;
                try {
                    strGBK = URLEncoder.encode(str,"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();

                }
                System.out.println(strGBK);

                webView.loadUrl(i+"/police/ipad.html?accountName="+strGBK);

            }

            @Override
            public void onFailure(final Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TastyToast.makeText(ChaXunActivity.this,e.getMessage(),TastyToast.LENGTH_LONG,TastyToast.INFO).show();
                    }
                });

            }

        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView!=null){
            webView.removeAllViews();

        }

    }
}
