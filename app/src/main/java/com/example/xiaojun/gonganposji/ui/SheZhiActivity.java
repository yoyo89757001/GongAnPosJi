package com.example.xiaojun.gonganposji.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.anupcowkur.reservoir.Reservoir;
import com.anupcowkur.reservoir.ReservoirGetCallback;
import com.anupcowkur.reservoir.ReservoirPutCallback;
import com.example.xiaojun.gonganposji.MyAppLaction;
import com.example.xiaojun.gonganposji.R;
import com.example.xiaojun.gonganposji.beans.BaoCunBean;
import com.example.xiaojun.gonganposji.beans.BaoCunBeanDao;
import com.example.xiaojun.gonganposji.dialog.XiuGaiJiuDianDialog;
import com.example.xiaojun.gonganposji.dialog.XiuGaiXinXiDialog;
import com.google.gson.reflect.TypeToken;
import com.sdsmdg.tastytoast.TastyToast;

import java.lang.reflect.Type;

import static com.example.xiaojun.gonganposji.MyAppLaction.jiuDianBean;

public class SheZhiActivity extends Activity {
    private Button ipDiZHI,gengxin,chaxun,zhuji2,jiudian;
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

        zhuji2= (Button) findViewById(R.id.zhuji);


        zhuji2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final XiuGaiXinXiDialog dialog=new XiuGaiXinXiDialog(SheZhiActivity.this);
                dialog.setOnQueRenListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (baoCunBean==null){
                            BaoCunBean baoCunBean2=new BaoCunBean();
                            baoCunBean2.setId(123456L);
                            baoCunBean2.setZhuji(dialog.getContents());
                            baoCunBeanDao.insert(baoCunBean2);
                            TastyToast.makeText(SheZhiActivity.this,"保存成功",TastyToast.LENGTH_LONG,TastyToast.INFO).show();
                            baoCunBean=baoCunBeanDao.load(123456L);
                            dialog.dismiss();

                        }else {
                            baoCunBean.setZhuji(dialog.getContents());
                            baoCunBeanDao.update(baoCunBean);
                            baoCunBean=baoCunBeanDao.load(123456L);
                            TastyToast.makeText(SheZhiActivity.this,"更新成功",TastyToast.LENGTH_LONG,TastyToast.INFO).show();
                            dialog.dismiss();
                        }

                    }


                });
                dialog.setQuXiaoListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                if (baoCunBean!=null){
                    dialog.setContents("设置主机地址",baoCunBean.getZhuji()+"");
                }else {
                    dialog.setContents("设置主机地址","http://183.3.158.132:8090");

                }
                dialog.show();
            }
        });

        ipDiZHI= (Button) findViewById(R.id.shezhiip);
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
        ipDiZHI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final XiuGaiXinXiDialog dialog=new XiuGaiXinXiDialog(SheZhiActivity.this);
                dialog.setOnQueRenListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (baoCunBean==null){
                            BaoCunBean baoCunBean2=new BaoCunBean();
                            baoCunBean2.setId(123456L);
                            baoCunBean2.setCameraIP(dialog.getContents());
                            baoCunBeanDao.insert(baoCunBean2);
                            TastyToast.makeText(SheZhiActivity.this,"保存成功",TastyToast.LENGTH_LONG,TastyToast.INFO).show();
                            baoCunBean=baoCunBeanDao.load(123456L);
                            dialog.dismiss();
                        }else {
                            baoCunBean.setCameraIP(dialog.getContents());
                            baoCunBeanDao.update(baoCunBean);
                            TastyToast.makeText(SheZhiActivity.this,"更新成功",TastyToast.LENGTH_LONG,TastyToast.INFO).show();
                            baoCunBean=baoCunBeanDao.load(123456L);
                            dialog.dismiss();
                        }

                  }


                });
                dialog.setQuXiaoListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                if (baoCunBean!=null){
                    dialog.setContents("设置IP摄像头地址",baoCunBean.getCameraIP()+"");
                }else {
                    dialog.setContents("设置IP摄像头地址","192.168.2.32");
                }
                dialog.show();
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
                            baoCunBean2.setJiudianName(dianDialog.getJiuDianBean().getName());
                            baoCunBeanDao.insert(baoCunBean2);
                            TastyToast.makeText(SheZhiActivity.this,"保存成功",TastyToast.LENGTH_LONG,TastyToast.INFO).show();
                            baoCunBean=baoCunBeanDao.load(123456L);
                            dianDialog.dismiss();
                        }else {
                            baoCunBean.setJiudianID(dianDialog.getJiuDianBean().getId());
                            baoCunBean.setJiudianName(dianDialog.getJiuDianBean().getName());
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
                if (baoCunBean!=null){
                    dianDialog.setContents(baoCunBean.getJiudianID()+"",baoCunBean.getJiudianName()+"");
                }else {
                    dianDialog.setContents("10000431","南北纵横酒店");
                }
                dianDialog.show();
            }
        });

    }
}
