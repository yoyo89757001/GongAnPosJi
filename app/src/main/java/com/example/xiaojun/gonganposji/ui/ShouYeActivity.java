package com.example.xiaojun.gonganposji.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xiaojun.gonganposji.R;

public class ShouYeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shou_ye);

        TextView imageView= (TextView) findViewById(R.id.dengji2);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(ShouYeActivity.this,InFoActivity2.class),2);
            }
        });

        TextView imageView22= (TextView) findViewById(R.id.dengji3);
        imageView22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShouYeActivity.this,HuZhaoActivity.class));
            }
        });

        ImageView imageView2= (ImageView) findViewById(R.id.shezhi);
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ShouYeActivity.this, SheZhiActivity.class));

            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            System.out.println("按下了back键   onKeyDown()");
            return true;
        }else {
            return super.onKeyDown(keyCode, event);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == 2) {
            //  Log.d("ShouYeActivity", "回来了");
            // 选择预约时间的页面被关闭
            String date = data.getStringExtra("date");
            if (date.equals("11")){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            Thread.sleep(600);
                            startActivityForResult(new Intent(ShouYeActivity.this,InFoActivity2.class),2);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }).start();

            }

        }

    }
}
