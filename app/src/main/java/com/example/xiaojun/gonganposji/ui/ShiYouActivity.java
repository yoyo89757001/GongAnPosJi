package com.example.xiaojun.gonganposji.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.xiaojun.gonganposji.R;

public class ShiYouActivity extends Activity implements View.OnClickListener {
   private LinearLayout l1,l2,l3,l4;
    private String name=null;
    private SensorInfoReceiver sensorInfoReceiver;
    private boolean bidui;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shi_you);
        name=getIntent().getStringExtra("name");
        bidui=getIntent().getBooleanExtra("biduijieguo",false);
        id=getIntent().getStringExtra("id");
        IntentFilter intentFilter1 = new IntentFilter();
        intentFilter1.addAction("guanbi2");
        sensorInfoReceiver = new SensorInfoReceiver();
        registerReceiver(sensorInfoReceiver, intentFilter1);

        l1= (LinearLayout) findViewById(R.id.l1);
        l1.setOnClickListener(this);
        l2= (LinearLayout) findViewById(R.id.l2);
        l2.setOnClickListener(this);
        l3= (LinearLayout) findViewById(R.id.l3);
        l3.setOnClickListener(this);
        l4= (LinearLayout) findViewById(R.id.l4);
        l4.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.l1:
                Intent intent=new Intent(ShiYouActivity.this,DengJiActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("type",1);
                intent.putExtra("bidui",bidui);
                intent.putExtra("id",id);
                startActivity(intent);

                break;
            case R.id.l2:
                Intent intent2=new Intent(ShiYouActivity.this,DengJiActivity.class);
                intent2.putExtra("name",name);
                intent2.putExtra("type",2);
                intent2.putExtra("bidui",bidui);
                intent2.putExtra("id",id);
                startActivity(intent2);

                break;
            case R.id.l3:
                Intent intent3=new Intent(ShiYouActivity.this,DengJiActivity.class);
                intent3.putExtra("name",name);
                intent3.putExtra("type",3);
                intent3.putExtra("bidui",bidui);
                intent3.putExtra("id",id);
                startActivity(intent3);

                break;
            case R.id.l4:
                Intent intent4=new Intent(ShiYouActivity.this,DengJiActivity.class);
                intent4.putExtra("name",name);
                intent4.putExtra("type",4);
                intent4.putExtra("bidui",bidui);
                intent4.putExtra("id",id);
                startActivity(intent4);

                break;

        }

    }

    private class SensorInfoReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (action.equals("guanbi2")) {
              finish();

            }
        }}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(sensorInfoReceiver);
    }
}
