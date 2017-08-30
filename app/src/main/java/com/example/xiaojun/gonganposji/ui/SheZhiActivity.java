package com.example.xiaojun.gonganposji.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.anupcowkur.reservoir.Reservoir;
import com.anupcowkur.reservoir.ReservoirGetCallback;
import com.anupcowkur.reservoir.ReservoirPutCallback;
import com.example.xiaojun.gonganposji.R;
import com.example.xiaojun.gonganposji.dialog.XiuGaiXinXiDialog;
import com.google.gson.reflect.TypeToken;
import com.sdsmdg.tastytoast.TastyToast;

import java.lang.reflect.Type;

public class SheZhiActivity extends Activity {
    private Button ipDiZHI,gengxin,chaxun,zhuji2;
    private TextView title;
    private ImageView famhui;
    private String ip=null;
    private String zhuji=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_she_zhi);
        Type resultType = new TypeToken<String>() {
        }.getType();
        Reservoir.getAsync("ipipip", resultType, new ReservoirGetCallback<String>() {
            @Override
            public void onSuccess(final String i) {
                ip=i;

             }

        @Override
        public void onFailure(Exception e) {

        }

     });
        Type resultType2 = new TypeToken<String>() {
        }.getType();
        Reservoir.getAsync("zhuji", resultType2, new ReservoirGetCallback<String>() {
            @Override
            public void onSuccess(final String i) {
                zhuji=i;

            }

            @Override
            public void onFailure(Exception e) {
                zhuji="http://183.3.158.132:8090";
            }

        });

        zhuji2= (Button) findViewById(R.id.zhuji);
        zhuji2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final XiuGaiXinXiDialog dialog=new XiuGaiXinXiDialog(SheZhiActivity.this);
                dialog.setOnQueRenListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Reservoir.putAsync("zhuji",dialog.getContents(), new ReservoirPutCallback() {
                            @Override
                            public void onSuccess() {
                                TastyToast.makeText(SheZhiActivity.this,"保存成功",TastyToast.LENGTH_LONG,TastyToast.INFO).show();
                                dialog.dismiss();
                            }

                            @Override
                            public void onFailure(Exception e) {
                                TastyToast.makeText(SheZhiActivity.this,"保存失败",TastyToast.LENGTH_LONG,TastyToast.INFO).show();
                                dialog.dismiss();
                            }
                        });

                    }


                });
                dialog.setQuXiaoListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                if (zhuji!=null){
                    dialog.setContents("设置主机地址",zhuji);
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

                                Reservoir.putAsync("ipipip",dialog.getContents(), new ReservoirPutCallback() {
                                    @Override
                                    public void onSuccess() {
                                        TastyToast.makeText(SheZhiActivity.this,"保存成功",TastyToast.LENGTH_LONG,TastyToast.INFO).show();
                                        dialog.dismiss();
                                    }

                                    @Override
                                    public void onFailure(Exception e) {
                                        TastyToast.makeText(SheZhiActivity.this,"保存失败",TastyToast.LENGTH_LONG,TastyToast.INFO).show();
                                        dialog.dismiss();
                                    }
                                });

                            }


                });
                dialog.setQuXiaoListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                if (ip!=null){
                    dialog.setContents("设置IP摄像头地址",ip);
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

    }
}
