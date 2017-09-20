package com.example.xiaojun.gonganposji.ui;


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.xiaojun.gonganposji.MyAppLaction;
import com.example.xiaojun.gonganposji.R;
import com.example.xiaojun.gonganposji.beans.BaoCunBean;
import com.example.xiaojun.gonganposji.beans.BaoCunBeanDao;
import com.example.xiaojun.gonganposji.beans.Photos;
import com.example.xiaojun.gonganposji.beans.ShiBieBean;
import com.example.xiaojun.gonganposji.beans.UserInfoBena;
import com.example.xiaojun.gonganposji.dialog.JiaZaiDialog;
import com.example.xiaojun.gonganposji.dialog.JiuDianBean;
import com.example.xiaojun.gonganposji.dialog.QueRenDialog;
import com.example.xiaojun.gonganposji.dialog.TiJIaoDialog;
import com.example.xiaojun.gonganposji.utils.FileUtil;
import com.example.xiaojun.gonganposji.utils.GsonUtil;
import com.example.xiaojun.gonganposji.view.AutoFitTextureView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.zxing.other.BeepManager;
import com.sdsmdg.tastytoast.TastyToast;
import com.telpo.tps550.api.TelpoException;
import com.telpo.tps550.api.idcard.IdCard;
import com.telpo.tps550.api.idcard.IdentityInfo;
import com.tzutalin.dlib.FaceDet;
import com.tzutalin.dlib.VisionDetRet;
import org.videolan.libvlc.IVLCVout;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class InFoActivity2 extends Activity {
    private EditText shenfengzheng,xingbie,mingzu,chusheng,dianhua,fazhengjiguan,
            youxiaoqixian,zhuzhi,fanghao,shibiejieguo,xiangsifdu;
    private TextView chepai;
    private ImageView zhengjianzhao,xianchengzhao;
    private Button button;
    private TextView name;
    private File mSavePhotoFile;
  //  private HorizontalProgressBarWithNumber progressBarWithNumber;
   // public static final String HOST="http://192.168.0.104:8080";
    private JiaZaiDialog jiaZaiDialog=null;
    private String xiangsi="";
    private String biduijieguo="";
    private TiJIaoDialog tiJIaoDialog=null;
  //  public static final String HOST="http://192.168.2.101:8081";
   // public static final String HOST="http://174p2704z3.51mypc.cn:11100";
  //  public static final String HOST="http://192.168.2.43:8080";
    public static final int TIMEOUT = 1000 * 60;
    private static boolean isTrue=true;
    private static boolean isTrue2=true;
    private boolean bidui=false;
    private Bitmap bitmapBig=null;
    private GetIDInfoTask async=null;
    private UserInfoBena userInfoBena=null;
    private SensorInfoReceiver sensorInfoReceiver;
  //  private String filePath=null;
    private String filePath2=null;
    private File file1=null;
 //   private File file2=null;
    private Thread thread;
    private String shengfenzhengPath=null;
//    private static int lian=0;
//    private Handler mhandler = null;
//    private int iDetect = 0;
    private BeepManager beepManager;
    private IdentityInfo info;
    private Bitmap zhengjianBitmap;
    private byte[] images;
  //  private byte[] fringerprint;
  //  private String fringerprintData;
  //  private final int REQUEST_TAKE_PHOTO=33;
    private static boolean isTrue3=true;
    private static boolean isTrue4=true;
    private FaceDet mFaceDet=null;
    private AutoFitTextureView videoView;
    private ImageView imageView;
    private MediaPlayer mediaPlayer=null;
    private IVLCVout vlcVout=null;
    private IVLCVout.Callback callback;
    private Media media;
    private TextView tishi;
    private LinearLayout jiemian;
    private static int count=1;
    private static final int MESSAGE_QR_SUCCESS = 1;
    private  LibVLC libvlc;
    private boolean isBaoCun=false;
    private boolean isReadCard=false;
    private BaoCunBeanDao baoCunBeanDao=null;
    private BaoCunBean baoCunBean=null;



    Handler mHandler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_QR_SUCCESS:

                    Bitmap bitmap= (Bitmap) msg.obj;
                    imageView.setImageBitmap(bitmap);

                    break;
                case 22:

                    tishi.setVisibility(View.VISIBLE);
                    tishi.setText("比对失败,开始第"+count+"次比对,请再次看下摄像头");

                    break;

            }

        }
    };


    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
             if (msg.what == 300) {

                 Toast tastyToast= TastyToast.makeText(InFoActivity2.this,"开启读卡失败",TastyToast.LENGTH_LONG,TastyToast.ERROR);
                 tastyToast.setGravity(Gravity.CENTER,0,0);
                 tastyToast.show();

            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhujiemian2);
        baoCunBeanDao=MyAppLaction.myAppLaction.getDaoSession().getBaoCunBeanDao();
        baoCunBean=baoCunBeanDao.load(123456L);

        mFaceDet= MyAppLaction.mFaceDet;
        libvlc=MyAppLaction.libvlc;


        isTrue3=true;
        isTrue4=true;


        String fn = "bbbb.jpg";
        FileUtil.isExists(FileUtil.PATH, fn);
        mSavePhotoFile=new File( FileUtil.SDPATH + File.separator + FileUtil.PATH + File.separator + fn);

        beepManager = new BeepManager(this, R.raw.beep);
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {

                   IdCard.open(InFoActivity2.this);

                    startReadCard();

                } catch (Exception e) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast tastyToast= TastyToast.makeText(InFoActivity2.this,"无法连接读卡器",TastyToast.LENGTH_LONG,TastyToast.ERROR);
                            tastyToast.setGravity(Gravity.CENTER,0,0);
                            tastyToast.show();
                        }
                    });
                }
            }
        }).start();



        IntentFilter intentFilter1 = new IntentFilter();
        intentFilter1.addAction("guanbi");
        intentFilter1.addAction("guanbi2");
        sensorInfoReceiver = new SensorInfoReceiver();
        registerReceiver(sensorInfoReceiver, intentFilter1);

        userInfoBena=new UserInfoBena();


        ImageView imageView= (ImageView) findViewById(R.id.dd);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        initView();

        if (baoCunBean!=null){
            bofang();
        }else {
            Toast tastyToast = TastyToast.makeText(InFoActivity2.this, "请先设置摄像头IP地址", TastyToast.LENGTH_LONG, TastyToast.ERROR);
            tastyToast.setGravity(Gravity.CENTER, 0, 0);
            tastyToast.show();
        }

        jiaZaiDialog=new JiaZaiDialog(InFoActivity2.this);
        jiaZaiDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        jiaZaiDialog.show();

    }

    private void bofang() {
        videoView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {

             //   Log.d("InFoActivity2", "改222222"+width+"   "+height);

                vlcVout.attachViews();

            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

             //   Log.d("InFoActivity2", "改222222变");
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {


                return true;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {


            }
        });

                callback=new IVLCVout.Callback() {
                    @Override
                    public void onNewLayout(IVLCVout vlcVout, int width, int height, int visibleWidth, int visibleHeight, int sarNum, int sarDen) {

                    }

                    @Override
                    public void onSurfacesCreated(IVLCVout vlcVout) {


                                if (mediaPlayer != null) {
                                    final Uri uri=Uri.parse("rtsp://"+baoCunBean.getCameraIP()+"/user=admin&password=&channel=1&stream=0.sdp");
                                    media = new Media(libvlc, uri);
                                    mediaPlayer.setMedia(media);
                                    mediaPlayer.play();
                                }

                    }

                    @Override
                    public void onSurfacesDestroyed(IVLCVout vlcVout) {
                        vlcVout.removeCallback(callback);
                    }

                    @Override
                    public void onHardwareAccelerationError(IVLCVout vlcVout) {

                    }
                };


                if (vlcVout!=null){
                    vlcVout.setVideoView(videoView);
                    vlcVout.addCallback(callback);
                }

    }

    private void startReadCard() {

        isTrue=true;
        isTrue2=true;

        thread = new Thread(new Runnable() {
            @Override
            public void run() {

                while (isTrue) {
                    if (isTrue2){
                        isTrue2=false;
                        try {

                             async= new GetIDInfoTask();
                             async.execute();

                       } catch (Exception e) {
                            isTrue=false;
                            Log.d("SerialReadActivity", e.getMessage());
                            mHandler.obtainMessage(300, e.getMessage()).sendToTarget();

                        }

                    }


                }
            }

        });
        thread.start();


    }



    private void initView() {

        videoView= (AutoFitTextureView) findViewById(R.id.fff);
        videoView.setAspectRatio(4,3);
        imageView= (ImageView) findViewById(R.id.ffff);
        jiemian= (LinearLayout) findViewById(R.id.jiemian);
        tishi= (TextView) findViewById(R.id.tishi);

        name= (TextView) findViewById(R.id.name);
        shenfengzheng= (EditText) findViewById(R.id.shenfenzheng);
        xingbie= (EditText) findViewById(R.id.xingbie);
        mingzu= (EditText) findViewById(R.id.mingzu);
        chusheng= (EditText) findViewById(R.id.chusheng);
        dianhua= (EditText) findViewById(R.id.dianhua);
        fazhengjiguan= (EditText) findViewById(R.id.jiguan);
        youxiaoqixian= (EditText) findViewById(R.id.qixian);
        zhuzhi= (EditText) findViewById(R.id.dizhi);
        fanghao= (EditText) findViewById(R.id.fanghao);
        chepai= (TextView) findViewById(R.id.chepai);
        xiangsifdu= (EditText) findViewById(R.id.xiangsidu);
        shibiejieguo= (EditText) findViewById(R.id.jieguo);
        zhengjianzhao= (ImageView) findViewById(R.id.zhengjian);
        xianchengzhao= (ImageView) findViewById(R.id.paizhao);
        button= (Button) findViewById(R.id.wancheng);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!userInfoBena.getCertNumber().equals("") && baoCunBean!=null){
                    try {
                        if (bidui){
                            isBaoCun=true;
                            link_save();
                        }else {
                            final QueRenDialog dialog=new QueRenDialog(InFoActivity2.this,"比对不通过,你确定要保存");
                            dialog.setOnPositiveListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    isBaoCun=true;
                                    link_save();
                                    dialog.dismiss();

                                }
                            });
                            dialog.setViewShow();
                            dialog.setButtonText("保 存");
                            dialog.setOnQuXiaoListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    finish();

                                }
                            });
                            dialog.show();
                        }


                    }catch (Exception e){
                        Toast tastyToast= TastyToast.makeText(InFoActivity2.this,"数据异常,请返回后重试",TastyToast.LENGTH_LONG,TastyToast.ERROR);
                        tastyToast.setGravity(Gravity.CENTER,0,0);
                        tastyToast.show();
                        Log.d("InFoActivity", e.getMessage());
                    }

                }else {
                    Toast tastyToast= TastyToast.makeText(InFoActivity2.this,"请先读取身份证信息",TastyToast.LENGTH_LONG,TastyToast.ERROR);
                    tastyToast.setGravity(Gravity.CENTER,0,0);
                    tastyToast.show();
                }

            }
        });

        //progressBarWithNumber= (HorizontalProgressBarWithNumber) findViewById(R.id.id_progressbar01);

        mediaPlayer=new MediaPlayer(libvlc);
        vlcVout = mediaPlayer.getVLCVout();

    }


    private class SensorInfoReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (action.equals("guanbi")) {
                userInfoBena.setCardPhoto(intent.getStringExtra("cardPath"));
                userInfoBena.setScanPhoto(intent.getStringExtra("saomiaoPath"));
                bidui=intent.getBooleanExtra("biduijieguo",false);

               if (bidui){
                   shibiejieguo.setText("比对通过");
                   biduijieguo="比对通过";

               }else {
                   shibiejieguo.setText("比对不通过");
                   biduijieguo="比对不通过";

               }
               xiangsi=intent.getStringExtra("xiangsidu");
                xiangsifdu.setText(intent.getStringExtra("xiangsidu")+"");

                Bitmap bitmap= BitmapFactory.decodeFile(FileUtil.SDPATH+ File.separator+FileUtil.PATH+File.separator+"bbbb.jpg");
                xianchengzhao.setImageBitmap(bitmap);
            }
            if (action.equals("guanbi2")){
                finish();
            }
        }
    }


    private class GetIDInfoTask extends
            AsyncTask<Void, Integer, TelpoException> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //开始
            info = null;
            zhengjianBitmap = null;

        }

        @Override
        protected TelpoException doInBackground(Void... arg0) {
            TelpoException result = null;
            try {
                publishProgress(1);
//				info = IdCard.checkIdCard(4000);
                info = IdCard.checkIdCard(1600);//luyq modify
                if (info != null) {
                    images = IdCard.getIdCardImage();
                    zhengjianBitmap = IdCard.decodeIdCardImage(images);
                    // luyq add 增加指纹信息
                    //fringerprint = IdCard.getFringerPrint();
                    //fringerprintData = Utils.getFingerInfo(fringerprint, InFoActivity2.this);
                }
            } catch (TelpoException e) {
                Log.d("GetIDInfoTask", "异常" + e.getMessage());
                result = e;
            }
            return result;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPostExecute(TelpoException result) {
            super.onPostExecute(result);

            if (result == null && !info.getName().equals("timeout")) {
                isTrue2 = false;
                isTrue = false;

                if (async != null) {
                    async.cancel(true);
                    async = null;
                }

                //设置信息
                beepManager.playBeepSoundAndVibrate();
                name.setText(info.getName().trim());
                xingbie.setText(info.getSex());
                shenfengzheng.setText(info.getNo());
                mingzu.setText(info.getNation());
                String time = info.getBorn().substring(0, 4) + "-" + info.getBorn().substring(4, 6) + "-" + info.getBorn().substring(6, 8);
                chusheng.setText(time);
                fazhengjiguan.setText(info.getApartment());

                String time2 = info.getPeriod().substring(0, 4) + "-" + info.getPeriod().substring(4, 6) + "-" + info.getPeriod().substring(6, 8);
                String time3 = info.getPeriod().substring(9, 13) + "-" + info.getPeriod().substring(13, 15) + "-" + info.getPeriod().substring(15, 17);
                youxiaoqixian.setText(time2 + " " + time3);
                zhuzhi.setText(info.getAddress());

                if (jiaZaiDialog!=null && jiaZaiDialog.isShowing()){
                    jiaZaiDialog.setText("开启摄像头中,请稍后...");
                }

                zhengjianzhao.setImageBitmap(zhengjianBitmap);
                String fn="aaaa.jpg";
                FileUtil.isExists(FileUtil.PATH,fn);

                saveBitmap2File(zhengjianBitmap.copy(Bitmap.Config.ARGB_8888,false), FileUtil.SDPATH+ File.separator+FileUtil.PATH+File.separator+fn,100);

                userInfoBena = new UserInfoBena(info.getName(), info.getSex().equals("男") ? 1 + "" : 2 + "", info.getNation(), time, info.getAddress(), info.getNo(), info.getApartment(), time2, time3, null, null, null);


            } else {
                isTrue2 = true;
//                Toast tastyToast = TastyToast.makeText(InFoActivity2.this, "读取身份证信息失败", TastyToast.LENGTH_LONG, TastyToast.ERROR);
//                tastyToast.setGravity(Gravity.CENTER, 0, 0);
//                tastyToast.show();

            }
        }

    }


    /***
     *保存bitmap对象到文件中
     * @param bm
     * @param path
     * @param quality
     * @return
     */
    public  void saveBitmap2File(Bitmap bm, final String path, int quality) {
        if (null == bm || bm.isRecycled()) {
            Log.d("InFoActivity", "回收|空");
            return ;
        }
        try {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(file));
            bm.compress(Bitmap.CompressFormat.JPEG, quality, bos);
            bos.flush();
            bos.close();
            shengfenzhengPath=path;
            isReadCard=true;

            //开启摄像头
            kaishiPaiZhao();

        } catch (Exception e) {
            e.printStackTrace();

        } finally {

            if (!bm.isRecycled()) {
                bm.recycle();
            }
            bm = null;
        }
    }

    private void kaishiPaiZhao(){
        if (mediaPlayer==null){
            finish();
            Toast tastyToast= TastyToast.makeText(InFoActivity2.this,"开启摄像头失败,请重新读卡",TastyToast.LENGTH_LONG,TastyToast.ERROR);
            tastyToast.setGravity(Gravity.CENTER,0,0);
            tastyToast.show();
            return;
        }

        if (mediaPlayer.isPlaying()){

            videoView.invalidate();

            startThread();

        }else {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        Thread.sleep(6000);
                        if (mediaPlayer.isPlaying()) {

                            startThread();

                        } else {

                           runOnUiThread(new Runnable() {
                               @Override
                               public void run() {
                                   Toast tastyToast= TastyToast.makeText(InFoActivity2.this,"当前网络不稳定,需要重新读卡开启摄像头,请稍候",TastyToast.LENGTH_LONG,TastyToast.INFO);
                                   tastyToast.setGravity(Gravity.CENTER,0,0);
                                   tastyToast.show();
                               }
                           });

                            Intent intent=new Intent();
                            intent.putExtra("date", "11");
                            setResult(Activity.RESULT_OK, intent);

                            finish();

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }).start();
        }
    }

    private void startThread(){

        if (jiaZaiDialog!=null && jiaZaiDialog.isShowing()){
            jiaZaiDialog.dismiss();
            jiaZaiDialog=null;
        }
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(jiemian, "scaleY", 1f, 0f);
        animator2.setDuration(600);//时间1s
        animator2.start();
        //起始为1，结束时为0
        ObjectAnimator animator = ObjectAnimator.ofFloat(jiemian, "scaleX", 1f, 0f);
        animator.setDuration(600);//时间1s
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                jiemian.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();



        Thread thread=  new Thread(new Runnable() {
            @Override
            public void run() {

                while (isTrue3){

                    if (isTrue4){
                        isTrue4=false;

                        try {
                           bitmapBig=videoView.getBitmap();


                            if (bitmapBig!=null){

                                List<VisionDetRet> results = mFaceDet.detect(bitmapBig);

                                if (results != null) {

                                    int s = results.size();

                                    VisionDetRet face;
                                    if (s > 0) {

                                        if (s > count - 1) {

                                            face = results.get(count - 1);

                                        } else {

                                            face = results.get(0);

                                        }

                                        int xx = 0;
                                        int yy = 0;
                                        int xx2 = 0;
                                        int yy2 = 0;
                                        int ww = bitmapBig.getWidth();
                                        int hh = bitmapBig.getHeight();
                                        if (face.getRight() - 220 >= 0) {
                                            xx = face.getRight() - 220;
                                        } else {
                                            xx = 0;
                                        }
                                        if (face.getTop() - 220 >= 0) {
                                            yy = face.getTop() - 220;
                                        } else {
                                            yy = 0;
                                        }
                                        if (xx + 400 <= ww) {
                                            xx2 = 400;
                                        } else {
                                            xx2 = ww - xx ;
                                        }
                                        if (yy + 480 <= hh) {
                                            yy2 = 480;
                                        } else {
                                            yy2 = hh - yy ;
                                        }

                                        //     Bitmap bmpf = bitmapBig.copy(Bitmap.Config.RGB_565, true);
//
//                                               //返回识别的人脸数
//                                               //	int faceCount = new FaceDetector(bmpf.getWidth(), bmpf.getHeight(), 1).findFaces(bmpf, facess);
//                                               //	FaceDetector faceCount2 = new FaceDetector(bmpf.getWidth(), bmpf.getHeight(), 2);
//
//                                               myFace = new FaceDetector.Face[numberOfFace];       //分配人脸数组空间
//                                               myFaceDetect = new FaceDetector(bmpf.getWidth(), bmpf.getHeight(), numberOfFace);
//                                               numberOfFaceDetected = myFaceDetect.findFaces(bmpf, myFace);    //FaceDetector 构造实例并解析人脸
//
//                                               if (numberOfFaceDetected > 0) {
//
//                                                   FaceDetector.Face face;
//                                                   if (numberOfFaceDetected>count-1){
//                                                       face = myFace[count-1];
//
//                                                   }else {
//                                                       face = myFace[0];
//
//                                                   }
//
//                                                   PointF pointF = new PointF();
//                                                   face.getMidPoint(pointF);
//
//
//                                                 //  myEyesDistance = (int)face.eyesDistance();
//
//                                                   int xx=0;
//                                                   int yy=0;
//                                                   int xx2=0;
//                                                   int yy2=0;
//
//                                                   if ((int)pointF.x-200>=0){
//                                                       xx=(int)pointF.x-200;
//                                                   }else {
//                                                       xx=0;
//                                                   }
//                                                   if ((int)pointF.y-320>=0){
//                                                       yy=(int)pointF.y-320;
//                                                   }else {
//                                                       yy=0;
//                                                   }
//                                                   if (xx+350 >=bitmapBig.getWidth()){
//                                                       xx2=bitmapBig.getWidth()-xx;
//
//                                                   }else {
//                                                       xx2=350;
//                                                   }
//                                                   if (yy+500>=bitmapBig.getHeight()){
//                                                       yy2=bitmapBig.getHeight()-yy;
//
//                                                   }else {
//                                                       yy2=500;
//                                                   }

                                        Bitmap bitmap = Bitmap.createBitmap(bitmapBig, xx, yy, xx2, yy2);

                                        // Bitmap bitmap = Bitmap.createBitmap(bitmapBig,0,0,bitmapBig.getWidth(),bitmapBig.getHeight());

                                        Message message3 = Message.obtain();
                                        message3.what = MESSAGE_QR_SUCCESS;
                                        message3.obj = bitmap;
                                        mHandler2.sendMessage(message3);


                                        String fn = "bbbb.jpg";
                                        FileUtil.isExists(FileUtil.PATH, fn);
                                        saveBitmap2File2(bitmap.copy(Bitmap.Config.ARGB_8888,false), FileUtil.SDPATH + File.separator + FileUtil.PATH + File.separator + fn, 100);
                                        bitmapBig.recycle();
                                        bitmapBig=null;

                                    } else {
                                        isTrue4 = true;
                                    }

                                    //  bmpf.recycle();
                                    //  bmpf = null;
                                }else {
                                    isTrue4 = true;
                                }


                            }else {
                                isTrue4 = true;
                            }


                        }catch (Exception e){
                            Log.d("InFoActivity2", e.getMessage()+"");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast tastyToast = TastyToast.makeText(InFoActivity2.this, "截图时发生未知错误,请关闭后重试", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                                     tastyToast.setGravity(Gravity.CENTER, 0, 0);
                                      tastyToast.show();
                                }
                            });
                        }



                    }

                }


            }
        });

        thread.start();

    }

    public  void saveBitmap2File2(Bitmap bm, final String path, int quality) {
        try {
            filePath2=path;
            if (null == bm) {
                Log.d("InFoActivity", "回收|空");
                return ;
            }

            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(file));
            bm.compress(Bitmap.CompressFormat.JPEG, quality, bos);
            bos.flush();
            bos.close();


            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tishi.setVisibility(View.VISIBLE);
                    tishi.setText("上传图片中。。。");
                    link_P1(shengfenzhengPath,filePath2);
                }
            });




        } catch (Exception e) {
            e.printStackTrace();

        } finally {

            if (!bm.isRecycled()) {
                bm.recycle();
            }
            bm = null;
        }
    }


    @Override
    protected void onPause() {
        super.onPause();


        if (vlcVout!=null){
            vlcVout.removeCallback(callback);
            callback=null;
            vlcVout=null;
        }

        if (mediaPlayer!=null){
            media.release();
            media=null;
            mediaPlayer=null;
        }
        if (libvlc!=null){
            libvlc.release();
            libvlc=null;
        }

        isTrue4=false;
        isTrue3=false;
        count=1;

        isTrue2=false;
        isTrue=false;

        if (jiaZaiDialog!=null && jiaZaiDialog.isShowing()){
            jiaZaiDialog.dismiss();
            jiaZaiDialog=null;
        }
        if (tiJIaoDialog!=null && tiJIaoDialog.isShowing()){
            tiJIaoDialog.dismiss();
            tiJIaoDialog=null;
        }
    }

    @Override
    protected void onDestroy() {


        if (beepManager != null){
            beepManager.close();
            beepManager = null;
        }
        IdCard.close();

        if (async!=null){
            async.cancel(true);
            async=null;
        }


        if (jiaZaiDialog!=null && jiaZaiDialog.isShowing()){
            jiaZaiDialog.dismiss();
            jiaZaiDialog=null;
        }
        unregisterReceiver(sensorInfoReceiver);
        baoCunBeanDao=null;
        super.onDestroy();

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_OK) {
//            switch (requestCode) {
//                case REQUEST_TAKE_PHOTO:  //拍照
//                    //注意，如果拍照的时候设置了MediaStore.EXTRA_OUTPUT，data.getData=null
//                    xianchengzhao.setImageURI(Uri.fromFile(mSavePhotoFile));
//
//                    link_P1(shengfenzhengPath,filePath2);
//
//                    break;
//
//            }
//        }
//    }

//    /**
//     * 启动拍照
//     * @param
//     */
//    private void startCamera() {
//
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        // Ensure that there's a camera activity to handle the intent
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            // Continue only if the File was successfully created
//            if (mSavePhotoFile != null) {
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
//                        Uri.fromFile(mSavePhotoFile));//设置文件保存的URI
//                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
//            }
//        }
//    }

    private void link_save() {
        //final MediaType JSON=MediaType.parse("application/json; charset=utf-8");
        //http://192.168.2.4:8080/sign?cmd=getUnSignList&subjectId=jfgsdf
        OkHttpClient okHttpClient= new OkHttpClient.Builder()
                .writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true)
                .build();


//    /* form的分割线,自己定义 */
//        String boundary = "xx--------------------------------------------------------------xx";
        RequestBody body = new FormBody.Builder()
                .add("cardNumber",userInfoBena.getCertNumber())
                .add("name",userInfoBena.getPartyName())
                .add("gender",userInfoBena.getGender())
                .add("birthday",userInfoBena.getBornDay())
                .add("address",userInfoBena.getCertAddress())
                .add("cardPhoto",userInfoBena.getCardPhoto())
                .add("scanPhoto",userInfoBena.getScanPhoto())
                .add("organ",userInfoBena.getCertOrg())
                .add("termStart",userInfoBena.getEffDate())
                .add("termEnd",userInfoBena.getExpDate())
                .add("accountId",baoCunBean.getJiudianID()+"")
                .add("result",biduijieguo)
                .add("homeNumber",fanghao.getText().toString().trim())
                .add("phone",dianhua.getText().toString().trim())
                .add("carNumber",chepai.getText().toString().trim())
                .add("score",xiangsi)
                .build();
        // Log.d("InFoActivity2", userInfoBena.getGender());
        Request.Builder requestBuilder = new Request.Builder()
                // .header("Content-Type", "application/json")
                .post(body)
                .url(baoCunBean.getZhuji() + "/saveCompareResult.do");

        if (!InFoActivity2.this.isFinishing() && tiJIaoDialog==null  ){
            tiJIaoDialog=new TiJIaoDialog(InFoActivity2.this);
            tiJIaoDialog.show();
        }

        // step 3：创建 Call 对象
        Call call = okHttpClient.newCall(requestBuilder.build());

        //step 4: 开始异步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("AllConnects", "请求识别失败"+e.getMessage());
                if (tiJIaoDialog!=null){
                    tiJIaoDialog.dismiss();
                    tiJIaoDialog=null;
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (tiJIaoDialog!=null){
                    tiJIaoDialog.dismiss();
                    tiJIaoDialog=null;
                }
               // Log.d("AllConnects", "请求识别成功"+call.request().toString());
                //获得返回体
                try {

                    ResponseBody body = response.body();
                    String ss = body.string().trim();
                 //     Log.d("InFoActivity", "ss" + ss);
                    if (isBaoCun) {

                    if (ss.contains("1")) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Toast tastyToast = TastyToast.makeText(InFoActivity2.this, "保存成功", TastyToast.LENGTH_LONG, TastyToast.INFO);
                                tastyToast.setGravity(Gravity.CENTER, 0, 0);
                                tastyToast.show();

                            }
                        });

                        finish();
                    } else if (ss.equals("这个是黑名单")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                final QueRenDialog dialog = new QueRenDialog(InFoActivity2.this, "请注意,这个是黑名单!");
                                dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
                                dialog.setOnPositiveListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        finish();
                                    }
                                });
                                dialog.show();
                            }
                        });

                    } else {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Toast tastyToast = TastyToast.makeText(InFoActivity2.this, "保存失败", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                                tastyToast.setGravity(Gravity.CENTER, 0, 0);
                                tastyToast.show();

                            }
                        });
                        finish();

                    }
                }

                }catch (Exception e){

                    if (tiJIaoDialog!=null){
                        tiJIaoDialog.dismiss();
                        tiJIaoDialog=null;
                    }
                    Log.d("WebsocketPushMsg", e.getMessage());
                }
            }
        });


    }


//
//    private void startThread() {
//
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                if (jiaZaiDialog != null && jiaZaiDialog.isShowing()) {
//                    jiaZaiDialog.dismiss();
//                }
//
//                ObjectAnimator animator2 = ObjectAnimator.ofFloat(jiemian, "scaleY", 1f, 0f);
//                animator2.setDuration(600);//时间1s
//                animator2.start();
//                //起始为1，结束时为0
//                ObjectAnimator animator = ObjectAnimator.ofFloat(jiemian, "scaleX", 1f, 0f);
//                animator.setDuration(600);//时间1s
//                animator.addListener(new Animator.AnimatorListener() {
//                    @Override
//                    public void onAnimationStart(Animator animation) {
//
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        jiemian.setVisibility(View.GONE);
//                    }
//
//                    @Override
//                    public void onAnimationCancel(Animator animation) {
//
//                    }
//
//                    @Override
//                    public void onAnimationRepeat(Animator animation) {
//
//                    }
//                });
//                animator.start();
//            }
//        });
//    }



//        Thread thread=  new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                while (isTrue3){
//
//                    if (isTrue4){
//                        isTrue4=false;
//                        try {
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//
//                                    bitmapBig=videoView.getBitmap();
//
//                                    if (bitmapBig!=null){
//
//                                       new Thread(new Runnable() {
//                                           @Override
//                                           public void run() {
//
//                                               Bitmap bmpf = bitmapBig.copy(Bitmap.Config.RGB_565, true);
//
//                                               //返回识别的人脸数
//                                               //	int faceCount = new FaceDetector(bmpf.getWidth(), bmpf.getHeight(), 1).findFaces(bmpf, facess);
//                                               //	FaceDetector faceCount2 = new FaceDetector(bmpf.getWidth(), bmpf.getHeight(), 2);
//
//                                               myFace = new FaceDetector.Face[numberOfFace];       //分配人脸数组空间
//                                               myFaceDetect = new FaceDetector(bmpf.getWidth(), bmpf.getHeight(), numberOfFace);
//                                               numberOfFaceDetected = myFaceDetect.findFaces(bmpf, myFace);    //FaceDetector 构造实例并解析人脸
//
//                                               if (numberOfFaceDetected > 0) {
//
//                                                   FaceDetector.Face face;
//                                                   if (numberOfFaceDetected>count-1){
//                                                       face = myFace[count-1];
//
//                                                   }else {
//                                                       face = myFace[0];
//
//                                                   }
//
//                                                   PointF pointF = new PointF();
//                                                   face.getMidPoint(pointF);
//
//
//                                                 //  myEyesDistance = (int)face.eyesDistance();
//
//                                                   int xx=0;
//                                                   int yy=0;
//                                                   int xx2=0;
//                                                   int yy2=0;
//
//                                                   if ((int)pointF.x-200>=0){
//                                                       xx=(int)pointF.x-200;
//                                                   }else {
//                                                       xx=0;
//                                                   }
//                                                   if ((int)pointF.y-320>=0){
//                                                       yy=(int)pointF.y-320;
//                                                   }else {
//                                                       yy=0;
//                                                   }
//                                                   if (xx+350 >=bitmapBig.getWidth()){
//                                                       xx2=bitmapBig.getWidth()-xx;
//
//                                                   }else {
//                                                       xx2=350;
//                                                   }
//                                                   if (yy+500>=bitmapBig.getHeight()){
//                                                       yy2=bitmapBig.getHeight()-yy;
//
//                                                   }else {
//                                                       yy2=500;
//                                                   }
//
//
//                                                   Bitmap bitmap = Bitmap.createBitmap(bitmapBig,xx,yy,xx2,yy2);
//
//                                                 //  Bitmap bitmap = Bitmap.createBitmap(bitmapBig,0,0,bitmapBig.getWidth(),bitmapBig.getHeight());
//
//                                                   Message message=Message.obtain();
//                                                   message.what=MESSAGE_QR_SUCCESS;
//                                                   message.obj=bitmap;
//                                                   mHandler2.sendMessage(message);
//
//
//                                                   String fn="bbbb.jpg";
//                                                   FileUtil.isExists(FileUtil.PATH,fn);
//                                                   saveBitmap2File2(bitmap, FileUtil.SDPATH+ File.separator+FileUtil.PATH+File.separator+fn,100);
//
//
//                                               }else {
//                                                   isTrue4=true;
//                                               }
//
//                                               bmpf.recycle();
//                                               bmpf = null;
//                                           }
//                                       }).start();
//
//
//                                    }
//                                }
//                            });
//
//                        }catch (IllegalStateException e){
//                            Log.d("InFoActivity2", e.getMessage()+"");
//                        }
//
//
//
//                    }
//
//                }
//
//
//            }
//        });
//
//        thread.start();
//
//    }

//    public  void saveBitmap2File2(Bitmap bm, final String path, int quality) {
//        try {
//            filePath2=path;
//            if (null == bm) {
//                Log.d("InFoActivity", "回收|空");
//                return ;
//            }
//
//            File file = new File(path);
//            if (file.exists()) {
//                file.delete();
//            }
//            BufferedOutputStream bos = new BufferedOutputStream(
//                    new FileOutputStream(file));
//            bm.compress(Bitmap.CompressFormat.JPEG, quality, bos);
//            bos.flush();
//            bos.close();
//
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//
//                }
//            });
//
//            link_P1(shengfenzhengPath,filePath2);
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//
//        } finally {
//
////			if (!bm.isRecycled()) {
////				bm.recycle();
////			}
//            bm = null;
//        }
//    }


    private void link_P1(String filename1, final String fileName2) {
        jiaZaiDialog=new JiaZaiDialog(InFoActivity2.this);
        jiaZaiDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        jiaZaiDialog.setText("上传图片中...");
        if (!InFoActivity2.this.isFinishing()){
            jiaZaiDialog.show();
        }


        //final MediaType JSON=MediaType.parse("application/json; charset=utf-8");
        //http://192.168.2.4:8080/sign?cmd=getUnSignList&subjectId=jfgsdf
        OkHttpClient okHttpClient= new OkHttpClient.Builder()
                .writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true)
                .build();

         /* 第一个要上传的file */
        file1 = new File(filename1);
        RequestBody fileBody1 = RequestBody.create(MediaType.parse("application/octet-stream") , file1);
        final String file1Name = System.currentTimeMillis()+"testFile1.jpg";

//    /* 第二个要上传的文件,*/
//        File file2 = new File(fileName2);
//        RequestBody fileBody2 = RequestBody.create(MediaType.parse("application/octet-stream") , file2);
//        String file2Name =System.currentTimeMillis()+"testFile2.jpg";


//    /* form的分割线,自己定义 */
//        String boundary = "xx--------------------------------------------------------------xx";

        MultipartBody mBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
            /* 底下是上传了两个文件 */
                .addFormDataPart("voiceFile" , file1Name , fileBody1)
                  /* 上传一个普通的String参数 */
                //  .addFormDataPart("subject_id" , subject_id+"")
                //  .addFormDataPart("image_2" , file2Name , fileBody2)
                .build();
        Request.Builder requestBuilder = new Request.Builder()
                // .header("Content-Type", "application/json")
                .post(mBody)
                .url(baoCunBean.getZhuji() + "/AppFileUploadServlet?FilePathPath=cardFilePath&AllowFileType=.jpg,.gif,.jpeg,.bmp,.png&MaxFileSize=10");

        // step 3：创建 Call 对象
        Call call = okHttpClient.newCall(requestBuilder.build());

        //step 4: 开始异步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (jiaZaiDialog!=null && jiaZaiDialog.isShowing()){
                            jiaZaiDialog.dismiss();
                            jiaZaiDialog=null;
                        }
                        Toast tastyToast= TastyToast.makeText(InFoActivity2.this,"上传图片出错，请返回后重试！",TastyToast.LENGTH_LONG,TastyToast.ERROR);
                        tastyToast.setGravity(Gravity.CENTER,0,0);
                        tastyToast.show();

                    }
                });
               // Log.d("AllConnects", "请求识别失败"+e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
              //  Log.d("AllConnects", "请求识别成功"+call.request().toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (jiaZaiDialog!=null && jiaZaiDialog.isShowing()){
                            jiaZaiDialog.dismiss();
                            jiaZaiDialog=null;
                        }
                    }
                });
                //获得返回体
                try {

                    ResponseBody body = response.body();
                    String ss=body.string();

                  //  Log.d("AllConnects", "aa   "+ss);

                    JsonObject jsonObject= GsonUtil.parse(ss).getAsJsonObject();
                    Gson gson=new Gson();
                    Photos zhaoPianBean=gson.fromJson(jsonObject,Photos.class);
                    userInfoBena.setCardPhoto(zhaoPianBean.getExDesc());
                    link_P2(fileName2);


                }catch (Exception e){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (jiaZaiDialog!=null && jiaZaiDialog.isShowing()){
                                jiaZaiDialog.dismiss();
                                jiaZaiDialog=null;
                            }
                            Toast tastyToast= TastyToast.makeText(InFoActivity2.this,"上传图片出错，请返回后重试！",TastyToast.LENGTH_LONG,TastyToast.ERROR);
                            tastyToast.setGravity(Gravity.CENTER,0,0);
                            tastyToast.show();
                        }
                    });
                    Log.d("WebsocketPushMsg", e.getMessage());
                }
            }
        });

    }

    private void link_P2( String fileName2) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                jiaZaiDialog=new JiaZaiDialog(InFoActivity2.this);
                jiaZaiDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
                jiaZaiDialog.setText("上传图片中...");
                jiaZaiDialog.show();
            }
        });

        //final MediaType JSON=MediaType.parse("application/json; charset=utf-8");
        //http://192.168.2.4:8080/sign?cmd=getUnSignList&subjectId=jfgsdf
        OkHttpClient okHttpClient= new OkHttpClient.Builder()
                .writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true)
                .build();

//         /* 第一个要上传的file */
//        File file1 = new File(filename1);
//        RequestBody fileBody1 = RequestBody.create(MediaType.parse("application/octet-stream") , file1);
//        final String file1Name = System.currentTimeMillis()+"testFile1.jpg";

    /* 第二个要上传的文件,*/
        //file2 = new File(fileName2);

        RequestBody fileBody2 = RequestBody.create(MediaType.parse("application/octet-stream") , mSavePhotoFile);
        String file2Name =System.currentTimeMillis()+"testFile2.jpg";


//    /* form的分割线,自己定义 */
//        String boundary = "xx--------------------------------------------------------------xx";

        MultipartBody mBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
            /* 底下是上传了两个文件 */
                //  .addFormDataPart("image_1" , file1Name , fileBody1)
                  /* 上传一个普通的String参数 */
                //  .addFormDataPart("subject_id" , subject_id+"")
                .addFormDataPart("voiceFile" , file2Name , fileBody2)
                .build();
        Request.Builder requestBuilder = new Request.Builder()
                // .header("Content-Type", "application/json")
                .post(mBody)
                .url(baoCunBean.getZhuji() + "/AppFileUploadServlet?FilePathPath=scanFilePath&AllowFileType=.jpg,.gif,.jpeg,.bmp,.png&MaxFileSize=10");


        // step 3：创建 Call 对象
        Call call = okHttpClient.newCall(requestBuilder.build());

        //step 4: 开始异步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
              //  Log.d("AllConnects", "请求识别失败"+e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (jiaZaiDialog!=null && jiaZaiDialog.isShowing()){
                            jiaZaiDialog.dismiss();
                            jiaZaiDialog=null;
                        }
                        Toast tastyToast= TastyToast.makeText(InFoActivity2.this,"上传图片出错，请返回后重试！",TastyToast.LENGTH_LONG,TastyToast.ERROR);
                        tastyToast.setGravity(Gravity.CENTER,0,0);
                        tastyToast.show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
              //  Log.d("AllConnects", "请求识别成功"+call.request().toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (jiaZaiDialog!=null && jiaZaiDialog.isShowing()){
                            jiaZaiDialog.dismiss();
                            jiaZaiDialog=null;
                        }
                    }
                });

                //获得返回体
                try {

                    ResponseBody body = response.body();
                    // Log.d("AllConnects", "aa   "+response.body().string());

                    JsonObject jsonObject= GsonUtil.parse(body.string()).getAsJsonObject();
                    Gson gson=new Gson();
                    Photos zhaoPianBean=gson.fromJson(jsonObject,Photos.class);
                    userInfoBena.setScanPhoto(zhaoPianBean.getExDesc());

                    link_tianqi3();


                }catch (Exception e){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (jiaZaiDialog!=null && jiaZaiDialog.isShowing()){
                                jiaZaiDialog.dismiss();
                                jiaZaiDialog=null;
                            }
                            Toast tastyToast= TastyToast.makeText(InFoActivity2.this,"上传图片出错，请返回后重试！",TastyToast.LENGTH_LONG,TastyToast.ERROR);
                            tastyToast.setGravity(Gravity.CENTER,0,0);
                            tastyToast.show();
                        }
                    });
                    Log.d("WebsocketPushMsg", e.getMessage());
                }
            }
        });

    }

    private void link_tianqi3() {
        //final MediaType JSON=MediaType.parse("application/json; charset=utf-8");
        //http://192.168.2.4:8080/sign?cmd=getUnSignList&subjectId=jfgsdf
        OkHttpClient okHttpClient= new OkHttpClient.Builder()
                .writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true)
                .build();


//    /* form的分割线,自己定义 */
//        String boundary = "xx--------------------------------------------------------------xx";
        RequestBody body = new FormBody.Builder()
                .add("cardNumber",userInfoBena.getCertNumber())
                .add("name",userInfoBena.getPartyName())
                .add("gender",userInfoBena.getGender())
                .add("birthday",userInfoBena.getBornDay())
                .add("address",userInfoBena.getCertAddress())
                .add("cardPhoto",userInfoBena.getCardPhoto())
                .add("scanPhoto",userInfoBena.getScanPhoto())
                .add("organ",userInfoBena.getCertOrg())
                .add("termStart",userInfoBena.getEffDate())
                .add("termEnd",userInfoBena.getExpDate())
                .add("accountId",baoCunBean.getJiudianID()+"")
                .add("count",count+"")
                .add("homeNumber",fanghao.getText().toString().trim())
                .add("phone",dianhua.getText().toString().trim())
                .add("carNumber",chepai.getText().toString().trim())
                .build();

        Request.Builder requestBuilder = new Request.Builder()
                // .header("Content-Type", "application/json")
                .post(body)
                .url(baoCunBean.getZhuji() + "/compare.do");


        // step 3：创建 Call 对象
        Call call = okHttpClient.newCall(requestBuilder.build());

        //step 4: 开始异步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tishi.setText("上传图片出错，请返回后重试！");
                    }
                });
                Log.d("AllConnects", "请求识别失败"+e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("AllConnects", "请求识别成功"+call.request().toString());
                //获得返回体
                try {
                    count++;

                    ResponseBody body = response.body();
                    // Log.d("AllConnects", "识别结果返回"+response.body().string());
                    String ss=body.string();
                  //  Log.d("InFoActivity", ss);
                    JsonObject jsonObject= GsonUtil.parse(ss).getAsJsonObject();
                    Gson gson=new Gson();
                    final ShiBieBean zhaoPianBean=gson.fromJson(jsonObject,ShiBieBean.class);

                    if (zhaoPianBean.getScore()>=65.0) {

                        //比对成功
                        sendBroadcast(new Intent("guanbi").putExtra("biduijieguo",true)
                                .putExtra("xiangsidu",(zhaoPianBean.getScore()+"").substring(0,5))
                                .putExtra("cardPath",userInfoBena.getCardPhoto()).putExtra("saomiaoPath",userInfoBena.getScanPhoto()));
                        count=1;

                        qiehuan();

                    }else {


                        if (count<=3){

                            Message message=Message.obtain();
                            message.what=22;
                            mHandler2.sendMessage(message);

                            isTrue4=true;


                        }else {

                            sendBroadcast(new Intent("guanbi").putExtra("biduijieguo",false)
                                    .putExtra("xiangsidu",(zhaoPianBean.getScore()+"").substring(0,5))
                                    .putExtra("cardPath",userInfoBena.getCardPhoto()).putExtra("saomiaoPath",userInfoBena.getScanPhoto()));
                            count=1;

                            qiehuan();
                        }

                    }


                }catch (Exception e){

                    if (count<=3){

                        Message message=Message.obtain();
                        message.what=22;
                        mHandler2.sendMessage(message);

                        isTrue4=true;


                    }else {

                        sendBroadcast(new Intent("guanbi").putExtra("biduijieguo",false).putExtra("xiangsidu","43.21")
                                .putExtra("cardPath",userInfoBena.getCardPhoto()).putExtra("saomiaoPath",userInfoBena.getScanPhoto()));
                        count=1;

                        qiehuan();

                    }

                    Log.d("WebsocketPushMsg", e.getMessage());
                }
            }
        });


    }

    private void qiehuan(){
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//
//                if (mediaPlayer!=null){
//                    mediaPlayer=null;
//                    media=null;
//                }
//                if (vlcVout!=null){
//                    vlcVout.detachViews();
//                    vlcVout.removeCallback(callback);
//                    callback=null;
//                    vlcVout=null;
//                }
//                if (libvlc!=null){
//                    libvlc.release();
//                }
//                if (videoView!=null){
//                    videoView.setSurfaceTextureListener(null);
//                }
//
//
//            }
//        }).start();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                videoView.setVisibility(View.GONE);
                jiemian.setVisibility(View.VISIBLE);
                isTrue4=false;
                isTrue3=false;

                ObjectAnimator animator2 = ObjectAnimator.ofFloat(jiemian, "scaleY", 0f, 1f);
                animator2.setDuration(600);//时间1s
                animator2.start();
                //起始为1，结束时为0
                ObjectAnimator animator = ObjectAnimator.ofFloat(jiemian, "scaleX", 0f, 1f);
                animator.setDuration(600);//时间1s
                animator.start();
            }
        });



    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (isReadCard && !isBaoCun){
                link_save();

            }
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

}
