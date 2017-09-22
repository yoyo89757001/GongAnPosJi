package com.example.xiaojun.gonganposji.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.xiaojun.gonganposji.MyAppLaction;
import com.example.xiaojun.gonganposji.R;
import com.example.xiaojun.gonganposji.beans.BaoCunBean;
import com.example.xiaojun.gonganposji.beans.BaoCunBeanDao;
import com.example.xiaojun.gonganposji.dialog.XiuGaiJiuDianDialog;
import com.sdsmdg.tastytoast.TastyToast;


public class SheZhiActivity extends Activity {
    private Button gengxin,chaxun,jiudian;
    private TextView title;
    private ImageView famhui;
    private BaoCunBeanDao baoCunBeanDao=null;
    private BaoCunBean baoCunBean=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_she_zhi);

        baoCunBeanDao= MyAppLaction.myAppLaction.getDaoSession().getBaoCunBeanDao();
        baoCunBean=baoCunBeanDao.load(123456L);



        gengxin= (Button) findViewById(R.id.jiancha);
        title= (TextView) findViewById(R.id.title);
        title.setText("系统设置");
        famhui= (ImageView) findViewById(R.id.leftim);
        famhui.setVisibility(View.VISIBLE);
        famhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        gengxin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TastyToast.makeText(SheZhiActivity.this,"已经是最新版本",TastyToast.LENGTH_LONG,TastyToast.INFO).show();

            }
        });
        chaxun= (Button) findViewById(R.id.chaxun);
        chaxun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SheZhiActivity.this,ChaXunActivity.class));

            }
        });

        jiudian= (Button) findViewById(R.id.jiudian);
        jiudian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final XiuGaiJiuDianDialog dianDialog=new XiuGaiJiuDianDialog(SheZhiActivity.this);
                dianDialog.setOnQueRenListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (baoCunBean==null){
                            BaoCunBean baoCunBean2=new BaoCunBean();
                            baoCunBean2.setId(123456L);
                            baoCunBean2.setJiudianID(dianDialog.getJiuDianBean().getId());
                            baoCunBean2.setCameraIP(dianDialog.getJiuDianBean().getIp());
                            baoCunBean2.setZhuji(dianDialog.getJiuDianBean().getZhuji());
                            baoCunBeanDao.insert(baoCunBean2);
                            TastyToast.makeText(SheZhiActivity.this,"保存成功",TastyToast.LENGTH_LONG,TastyToast.INFO).show();
                            baoCunBean=baoCunBeanDao.load(123456L);
                            dianDialog.dismiss();
                        }else {
                            baoCunBean.setJiudianID(dianDialog.getJiuDianBean().getId());
                            baoCunBean.setCameraIP(dianDialog.getJiuDianBean().getIp());
                            baoCunBean.setZhuji(dianDialog.getJiuDianBean().getZhuji());
                            baoCunBeanDao.update(baoCunBean);
                            TastyToast.makeText(SheZhiActivity.this,"更新成功",TastyToast.LENGTH_LONG,TastyToast.INFO).show();
                            baoCunBean=baoCunBeanDao.load(123456L);
                            dianDialog.dismiss();
                        }

                    }
                });
                dianDialog.setQuXiaoListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dianDialog.dismiss();
                    }
                });
                if (baoCunBean!=null && baoCunBean.getCameraIP()!=null && baoCunBean.getJiudianID()!=null && baoCunBean.getZhuji()!=null){
                    dianDialog.setContents(baoCunBean.getJiudianID(),baoCunBean.getCameraIP(),baoCunBean.getZhuji());
                }
                dianDialog.show();
            }
        });
        TextView banben= (TextView) findViewById(R.id.banben);
        banben.setText("V"+getVersionName(SheZhiActivity.this));

    }

    /**
     * 获取版本号
     * @param context 上下文
     * @return 版本号
     */
    public  String getVersionName(Context context){
        //获取包管理器
        PackageManager pm = context.getPackageManager();
        //获取包信息
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(),0);
            return  packageInfo.versionName;
           //返回版本号
          //  return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "1.0.0";
    }
}
