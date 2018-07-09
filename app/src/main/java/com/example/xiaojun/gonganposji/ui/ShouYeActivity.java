package com.example.xiaojun.gonganposji.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xiaojun.gonganposji.R;
import com.example.xiaojun.gonganposji.beans.ZuiFan;
import com.example.xiaojun.gonganposji.utils.Utils;
import com.example.xiaojun.gonganposji.view.Util;
import com.example.xiaojun.gonganposji.view.VerticalScrolledListview;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ShouYeActivity extends Activity {
    private LinearLayout linearLayout;
    private List<ZuiFan> zuiFanList=new ArrayList<>();
    private final Timer timer = new Timer();
    private TimerTask task;
    private static int count=0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shou_ye);
        linearLayout= (LinearLayout) findViewById(R.id.tishi_ll);
        TextView imageView= (TextView) findViewById(R.id.dengji2);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(ShouYeActivity.this,InFoActivity3.class),2);
            }
        });

        TextView imageView22= (TextView) findViewById(R.id.dengji3);
        imageView22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShouYeActivity.this,HuZhaoActivity2.class));
            }
        });

        TextView imageView44= (TextView) findViewById(R.id.dengji4);
        imageView44.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShouYeActivity.this,ChaXunActivity.class).putExtra("type",11));
            }
        });

        TextView imageView2= (TextView) findViewById(R.id.shezhi);
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ShouYeActivity.this, SheZhiActivity.class));

            }
        });

        String s="刘志强," +
                "2017年12月，济宁市市中辖区发生一起非法拘禁案，经工作，确定刘志强有重大嫌疑，现在逃。" +
                "刘志强，男，24岁，汉族，1993年3年17日出生，身份证号码370883199303176812，户籍地:山东省济宁市任城区海关东路5号府河小区17号楼1单元201号。" +
                "为及时抓获犯罪嫌疑人，消除社会安全隐患，现予以公开通缉，望社会各界和广大人民群众积极提供有关线索，发现情况，请及时拨打110报警电话或联系人电话。" +
                "联系人:仲警官 18653762119" +
                "王警官 18653762726";

       List<String> ss= Utils.getStrList(s,250);


        ZuiFan zuiFan=new ZuiFan();
        zuiFan.setTishi(ss);
        zuiFan.setBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.zf1));


        String s1="戈树涛," +
                "2017年6月，济宁市市中辖区发生一起故意伤害案，经工作，确定戈树涛有重大嫌疑，现在逃。" +
                "戈树涛，男，28岁，汉族，1989年9月4日出生，身份证号码370811198909044076，户籍地:山东省济宁市任城区唐口街道办事处戈户村光明街55号。" +
                "为及时抓获犯罪嫌疑人，消除社会安全隐患，现予以公开通缉，望社会各界和广大人民群众积极提供有关线索，发现情况，请及时拨打110报警电话或联系人电话。" +
                "联系人:荣警官 18653762110 袁警官 18653762035";

        List<String> ss1= Utils.getStrList(s1,250);
        ZuiFan zuiFan2=new ZuiFan();
        zuiFan2.setTishi(ss1);
        zuiFan2.setBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.zf2));

        String s2="潘广伟,（在逃人员编号：T2306010301002016080005），男，汉族，1964年02月20日出生，身份证号码：440105196402201515，护照号：E18971179，往来港澳通行证号：C05580701，户籍地：广东省广州市海珠区素社一巷8号105房。该潘广州口音，身高175厘米。";
        List<String> ss2= Utils.getStrList(s2,250);

        ZuiFan zuiFan3=new ZuiFan();
        zuiFan3.setTishi(ss2);
        zuiFan3.setBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.zf3));
        zuiFanList.add(zuiFan);
        zuiFanList.add(zuiFan2);
        zuiFanList.add(zuiFan3);

    }



    Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what==999){
                final View view1 = View.inflate(ShouYeActivity.this, R.layout.tishi_item, null);
                ImageView imageView= (ImageView) view1.findViewById(R.id.touxiang);
                VerticalScrolledListview textView= (VerticalScrolledListview) view1.findViewById(R.id.tishi);
                imageView.setImageBitmap(zuiFanList.get(count).getBitmap());
                textView.setData(zuiFanList.get(count).getTishi());
                try {
                    linearLayout.removeViewAt(0);
                }catch (Exception e){
                    Log.d("ShouYeActivity", e.getMessage()+"");

                }

                linearLayout.addView(view1);
                count++;
                if (count>2){
                    count=0;
                }
              //  timer.schedule(task, 3000);


            }

            return false;
        }
    });

    @Override
    protected void onResume() {
        super.onResume();
        if (task!=null)
            task.cancel();

        task = new TimerTask() {
            @Override
            public void run() {

                Message message = new Message();
                message.what = 999;
                handler.sendMessage(message);
                //	Log.d(TAG, "gggggggggggg");

            }
        };
        timer.schedule(task, 1000,12000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (task!=null)
            task.cancel();
        if (timer!=null)
            timer.cancel();
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
