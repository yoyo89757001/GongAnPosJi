package com.example.xiaojun.gonganposji.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.xiaojun.gonganposji.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.telpo.tps550.api.TelpoException;
import com.telpo.tps550.api.printer.UsbThermalPrinter;

import java.util.Hashtable;


public class DaYingActivity extends Activity {
    private LinearLayout baocun;
    private Button dayin;
    private TextView t1,t2,t3,t4,t5;
    private EditText beizhu;
    private ImageView erweima;
    private String Result;
    private ImageView iii;
    private int printGray=5; //灰度
    private Boolean nopaper = false;
    MyHandler handler;
    private ProgressDialog progressDialog;
    private final int NOPAPER = 3;
    private final int LOWBATTERY = 4;
    private final int PRINTVERSION = 5;
    private final int PRINTBARCODE = 6;
    private final int PRINTPAPERWALK = 8;
    private final int PRINTCONTENT = 9;
    private final int CANCELPROMPT = 10;
    private final int PRINTERR = 11;
    private final int OVERHEAT = 12;
    private final int MAKER = 13;
    private final int PRINTPICTURE = 14;
    private final int NOBLACKBLOCK = 15;
    private UsbThermalPrinter mUsbThermalPrinter = new UsbThermalPrinter(DaYingActivity.this);
    private Bitmap bitmap=null;
    private String name=null,shoufangren=null,riqi=null;

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case NOPAPER:
                    noPaperDlg();
                    break;
                case LOWBATTERY:
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(DaYingActivity.this);
                    alertDialog.setTitle(R.string.operation_result);
                    alertDialog.setMessage(getString(R.string.LowBattery));
                    alertDialog.setPositiveButton(getString(R.string.dialog_comfirm), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    alertDialog.show();
                    break;
                case NOBLACKBLOCK:
                    Toast.makeText(DaYingActivity.this, R.string.maker_not_find, Toast.LENGTH_SHORT).show();
                    break;
//                case PRINTVERSION:
//
//                    if (msg.obj.equals("1")) {
//                        textPrintVersion.setText(printVersion);
//                    } else {
//                        Toast.makeText(UsbPrinterActivity.this, R.string.operation_fail, Toast.LENGTH_LONG).show();
//                    }
//                    break;
//                case PRINTBARCODE:
//                    new barcodePrintThread().start();
//                    break;
//                case PRINTQRCODE:
//                    new qrcodePrintThread().start();
//                    break;
//                case PRINTPAPERWALK:
//                    new paperWalkPrintThread().start();
//                    break;
//                case PRINTCONTENT:
//                    new contentPrintThread().start();
//                    break;
//                case MAKER:
//                    new MakerThread().start();
//                    break;
                case PRINTPICTURE:
                    new printPicture().start();
                    break;
                case CANCELPROMPT:
                    if (progressDialog != null && !DaYingActivity.this.isFinishing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                    break;
                case OVERHEAT:
                    AlertDialog.Builder overHeatDialog = new AlertDialog.Builder(DaYingActivity.this);
                    overHeatDialog.setTitle(R.string.operation_result);
                    overHeatDialog.setMessage(getString(R.string.overTemp));
                    overHeatDialog.setPositiveButton(getString(R.string.dialog_comfirm), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    overHeatDialog.show();
                    break;
                default:
                    Toast.makeText(DaYingActivity.this, "Print Error!", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daying);
        name=getIntent().getStringExtra("name");
        shoufangren=getIntent().getStringExtra("shoufangren");
        riqi=getIntent().getStringExtra("time");

        t1= (TextView) findViewById(R.id.name);
        t2= (TextView) findViewById(R.id.danwei);
        t3= (TextView) findViewById(R.id.riqi);
        t4= (TextView) findViewById(R.id.beifangren);
        t5= (TextView) findViewById(R.id.bianha);
        beizhu= (EditText) findViewById(R.id.beizhu);
        erweima= (ImageView) findViewById(R.id.erweima);
        Bitmap bitmap2 = null;
        try {
            bitmap2 = CreateCode("3232132132", BarcodeFormat.QR_CODE, 316, 316);
            erweima.setImageBitmap(bitmap2);

        } catch (WriterException e) {
            Log.d("DaYingActivity", e.getMessage());

        }

        if (name!=null){
            t1.setText(name);
        }
        if (riqi!=null){
            t3.setText(riqi);
        }
        if (shoufangren!=null){
            t4.setText(shoufangren);
        }


        handler = new MyHandler();

        baocun= (LinearLayout) findViewById(R.id.baocun);
        dayin= (Button) findViewById(R.id.dayin);
        dayin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              bitmap = createViewBitmap(baocun);

                new printPicture().start();

            }
        });
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    mUsbThermalPrinter.start(0);
                    mUsbThermalPrinter.reset();

                } catch (TelpoException e) {
                    e.printStackTrace();
                }

            }
        }).start();
        Button guanbi= (Button) findViewById(R.id.guanbi);
        guanbi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("guanbi2");
                sendBroadcast(intent);
                finish();
            }
        });

    }

    private Bitmap createViewBitmap(View view){

        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }

    private void noPaperDlg() {
        AlertDialog.Builder dlg = new AlertDialog.Builder(DaYingActivity.this);
        dlg.setTitle(getString(R.string.noPaper));
        dlg.setMessage(getString(R.string.noPaperNotice));
        dlg.setCancelable(false);
        dlg.setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        dlg.show();
    }

    private class printPicture extends Thread {

        @Override
        public void run() {
            super.run();
            try {
                mUsbThermalPrinter.reset();
                mUsbThermalPrinter.setGray(printGray);
                mUsbThermalPrinter.setAlgin(UsbThermalPrinter.ALGIN_MIDDLE);

                if (bitmap!=null) {
                    mUsbThermalPrinter.printLogo(bitmap,false);
                    mUsbThermalPrinter.walkPaper(20);
                } else {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(DaYingActivity.this, getString(R.string.not_find_picture), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                Result = e.toString();
                if (Result.equals("com.telpo.tps550.api.printer.NoPaperException")) {
                    nopaper = true;
                } else if (Result.equals("com.telpo.tps550.api.printer.OverHeatException")) {
                    handler.sendMessage(handler.obtainMessage(OVERHEAT, 1, 0, null));
                } else {
                    handler.sendMessage(handler.obtainMessage(PRINTERR, 1, 0, null));
                }
            } finally {
                handler.sendMessage(handler.obtainMessage(CANCELPROMPT, 1, 0, null));
                if (nopaper){
                    handler.sendMessage(handler.obtainMessage(NOPAPER, 1, 0, null));
                    nopaper = false;
                    return;
                }
            }
        }
    }

    public Bitmap CreateCode(String str, com.google.zxing.BarcodeFormat type, int bmpWidth, int bmpHeight) throws WriterException {
        Hashtable<EncodeHintType,String> mHashtable = new Hashtable<EncodeHintType,String>();
        mHashtable.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        // 生成二维矩阵,编码时要指定大小,不要生成了图片以后再进行缩放,以防模糊导致识别失败
        BitMatrix matrix = new MultiFormatWriter().encode(str, type, bmpWidth, bmpHeight, mHashtable);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        // 二维矩阵转为一维像素数组（一直横着排）
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = 0xff000000;
                } else {
                    pixels[y * width + x] = 0xffffffff;
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        // 通过像素数组生成bitmap,具体参考api
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }
}
