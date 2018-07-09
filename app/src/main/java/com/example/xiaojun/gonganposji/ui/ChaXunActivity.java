package com.example.xiaojun.gonganposji.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.xiaojun.gonganposji.MyAppLaction;
import com.example.xiaojun.gonganposji.R;
import com.example.xiaojun.gonganposji.beans.BaoCunBean;
import com.example.xiaojun.gonganposji.beans.BaoCunBeanDao;



import java.net.URLEncoder;

public class ChaXunActivity extends Activity {
    private WebView webView;
    private TextView title;
    private ImageView famhui;
    private BaoCunBeanDao baoCunBeanDao=null;
    private BaoCunBean baoCunBean=null;
    private int type=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cha_xun);
        webView= (WebView) findViewById(R.id.webwiew);
        baoCunBeanDao= MyAppLaction.myAppLaction.getDaoSession().getBaoCunBeanDao();
        baoCunBean=baoCunBeanDao.load(123456L);
        type=getIntent().getIntExtra("type",0);

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

        if (baoCunBean!=null){

            try {
            //    Log.d("ChaXunActivity", baoCunBean.getZhuji() + "/police/ipad.html?accountName=" + baoCunBean.getJiudianID());
//                String str =baoCunBean.getJiudianName(); //默认环境，已是UTF-8编码
//                String strGBK = URLEncoder.encode(str,"UTF-8");
                if (type==0){
                    webView.loadUrl(baoCunBean.getZhuji()+"/police/ipad.html?accountId="+baoCunBean.getJiudianID());
                    Log.d("ChaXunActivity", baoCunBean.getZhuji() + "/police/ipad.html?accountId=" + baoCunBean.getJiudianID());
                }else {
                    webView.loadUrl(baoCunBean.getZhuji()+"/police/tuifang.html?accountId="+baoCunBean.getJiudianID());
                    Log.d("ChaXunActivity", baoCunBean.getZhuji() + "/police/tuifang.html?accountId=" + baoCunBean.getJiudianID());
                }

            } catch (Exception e) {
                Log.d("ChaXunActivity", e.getMessage()+"");

            }

        }

      //  Log.d("ChaXunActivity", ip + "/police/ipad.html?accountName=" + strGBK);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView!=null){
            webView.removeAllViews();

        }

    }
}
