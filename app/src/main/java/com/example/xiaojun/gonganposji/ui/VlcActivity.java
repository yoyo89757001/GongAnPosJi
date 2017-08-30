//package com.example.xiaojun.gonganposji.ui;
//
//import android.app.Activity;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.graphics.Bitmap;
//import android.graphics.SurfaceTexture;
//import android.media.FaceDetector;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.util.Log;
//import android.view.TextureView;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//import com.example.xiaojun.gonganposji.R;
//import com.example.xiaojun.gonganposji.beans.Photos;
//import com.example.xiaojun.gonganposji.beans.ShiBieBean;
//import com.example.xiaojun.gonganposji.beans.UserInfoBena;
//import com.example.xiaojun.gonganposji.utils.GsonUtil;
//import com.example.xiaojun.gonganposji.utils.LibVLCUtil;
//
//import com.google.gson.Gson;
//import com.google.gson.JsonObject;
//
//import org.videolan.libvlc.IVLCVout;
//import org.videolan.libvlc.LibVLC;
//import org.videolan.libvlc.Media;
//import org.videolan.libvlc.MediaPlayer;
//
//import java.io.BufferedOutputStream;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.concurrent.TimeUnit;
//import okhttp3.Call;
//import okhttp3.Callback;
//import okhttp3.FormBody;
//import okhttp3.MediaType;
//import okhttp3.MultipartBody;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.RequestBody;
//import okhttp3.Response;
//import okhttp3.ResponseBody;
//
//public class VlcActivity extends Activity {
//
//    public static final String HOST="http://174p2704z3.51mypc.cn:33158";
//    private MediaPlayer mediaPlayer=null;
//    private IVLCVout vlcVout=null;
//    private IVLCVout.Callback callback;
//    private LibVLC libvlc;
//    private Media media;
//    private static boolean isTrue=true;
//    private static boolean isTrue2=true;
//    private Bitmap bitmapBig=null;
//    private ImageView imageView;
//    private int numberOfFace = 4;       //最大检测的人脸数
//    private FaceDetector myFaceDetect;  //人脸识别类的实例
//    private FaceDetector.Face[] myFace; //存储多张人脸的数组变量
//    int myEyesDistance;           //两眼之间的距离
//    int numberOfFaceDetected=0;       //实际检测到的人脸数
//    private static final int MESSAGE_QR_SUCCESS = 1;
//    private Handler mHandler;
//    private TextView tishi;
//    private static int count=1;
//    private UserInfoBena userInfoBena=null;
//    private SensorInfoReceiver sensorInfoReceiver;
//    private String filePath=null;
//    private String filePath2=null;
//    private File file1=null;
//    private File file2=null;
//    private String ip=null;
//    private Button jiance;
//    private boolean isA=true;
//    private TextureView videoView;
//    long c=0;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.vlc);
//
//        ip=getIntent().getStringExtra("ip");
//
//        isTrue=true;
//        isTrue2=true;
//
//        filePath=getIntent().getStringExtra("filePath");
//        userInfoBena=new UserInfoBena();
//
//         videoView= (TextureView) findViewById(R.id.fff);
//
//        imageView= (ImageView) findViewById(R.id.ffff);
//        tishi= (TextView) findViewById(R.id.tishi);
//
//        filePath=getIntent().getStringExtra("filePath");
//        IntentFilter intentFilter1 = new IntentFilter();
//        intentFilter1.addAction("guanbi");
//        sensorInfoReceiver = new SensorInfoReceiver();
//        registerReceiver(sensorInfoReceiver, intentFilter1);
//        final Uri uri=Uri.parse("rtsp://"+ip+"/user=admin&password=&channel=1&stream=0.sdp");
//        Log.d("VlcActivity", uri.toString());
//        initHandler();
//
//        libvlc= LibVLCUtil.getLibVLC(VlcActivity.this);
//        mediaPlayer=new MediaPlayer(libvlc);
//        vlcVout = mediaPlayer.getVLCVout();
//        callback=new IVLCVout.Callback() {
//            @Override
//            public void onNewLayout(IVLCVout vlcVout, int width, int height, int visibleWidth, int visibleHeight, int sarNum, int sarDen) {
//
//            }
//
//            @Override
//            public void onSurfacesCreated(IVLCVout vlcVout) {
//                if (mediaPlayer != null) {
//
//                    media = new Media(libvlc, uri);
//                    mediaPlayer.setMedia(media);
//
//                    mediaPlayer.play();
//                    videoView.setKeepScreenOn(true);
//                }
//
//
//            }
//
//            @Override
//            public void onSurfacesDestroyed(IVLCVout vlcVout) {
//
//            }
//
//            @Override
//            public void onHardwareAccelerationError(IVLCVout vlcVout) {
//
//            }
//        };
//        videoView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
//            @Override
//            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
//                vlcVout.attachViews();
//
//                startThread();
//
//            }
//
//            @Override
//            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
//
//            }
//
//            @Override
//            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
//                return false;
//            }
//
//            @Override
//            public void onSurfaceTextureUpdated(SurfaceTexture surface) {
//
//            }
//        });
//        vlcVout.addCallback(callback);
//        vlcVout.setVideoView(videoView);
//
//
//    }
//
//    private void startThread(){
//
//        Thread thread=  new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                while (isTrue){
//
//                    if (isTrue2){
//                        try {
//                            Thread.sleep(2000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Log.d("VlcActivity", "dddd");
//                                Toast.makeText(VlcActivity.this,"线程运行"+c++,Toast.LENGTH_SHORT).show();
//                            }
//                        });
//
////                        isTrue2=false;
////
////                        bitmapBig=videoView.getBitmap();
////
////                        if (bitmapBig!=null){
////
////                            Bitmap bmpf = bitmapBig.copy(Bitmap.Config.RGB_565, true);
////                            //返回识别的人脸数
////                            //	int faceCount = new FaceDetector(bmpf.getWidth(), bmpf.getHeight(), 1).findFaces(bmpf, facess);
////                            //	FaceDetector faceCount2 = new FaceDetector(bmpf.getWidth(), bmpf.getHeight(), 2);
////
////                            myFace = new FaceDetector.Face[numberOfFace];       //分配人脸数组空间
////                            myFaceDetect = new FaceDetector(bmpf.getWidth(), bmpf.getHeight(), numberOfFace);
////                            numberOfFaceDetected = myFaceDetect.findFaces(bmpf, myFace);    //FaceDetector 构造实例并解析人脸
////
////
////                            bmpf.recycle();
////                            bmpf = null;
////
////                            if (numberOfFaceDetected > 0) {
////
////                                FaceDetector.Face face = myFace[0];
////
////                                PointF pointF = new PointF();
////                                face.getMidPoint(pointF);
////                                myEyesDistance = (int)face.eyesDistance();
////                                int xx=0;
////                                int yy=0;
////                                if ((int)pointF.x-220>=0){
////                                    xx=(int)pointF.x-220;
////                                }else {
////                                    xx=0;
////                                }
////                                if ((int)pointF.y-220>=0){
////                                    yy=(int)pointF.y-220;
////                                }else {
////                                    yy=0;
////                                }
////                                if (xx + myEyesDistance + 300>bitmapBig.getWidth()){
////                                    xx=bitmapBig.getWidth()-(myEyesDistance + 300);
////                                    //  Log.d("fff", "bmp.getWidth():" + bitmapBig.getWidth());
////                                }
////                                if (yy + myEyesDistance + 300>bitmapBig.getHeight()){
////                                    yy=bitmapBig.getHeight()-(myEyesDistance + 300);
////                                    // Log.d("fff", "bmp.getWidth():" + bitmapBig.getWidth());
////                                }
////
////
////                                Bitmap bitmap = Bitmap.createBitmap(bitmapBig,xx,yy,myEyesDistance+300,myEyesDistance+300);
////
////                                Message message=Message.obtain();
////                                message.what=MESSAGE_QR_SUCCESS;
////                                message.obj=bitmap;
////                                mHandler.sendMessage(message);
////
////
////
////                                String fn="bbbb.jpg";
////                                FileUtil.isExists(FileUtil.PATH,fn);
////                                saveBitmap2File(bitmap, FileUtil.SDPATH+ File.separator+FileUtil.PATH+File.separator+fn,100);
////
////
////                            }else {
////                                isTrue2=true;
////                            }
////
////                        }
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
//
//
//    private void initHandler() {
//        mHandler = new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                switch (msg.what) {
//                    case MESSAGE_QR_SUCCESS:
//
//                        Bitmap bitmap= (Bitmap) msg.obj;
//                        imageView.setImageBitmap(bitmap);
//
//                        break;
//                    case 22:
//
//                        tishi.setVisibility(View.VISIBLE);
//                        tishi.setText("比对失败,开始第"+count+"次比对");
//
//                        break;
//
//                }
//
//            }
//        };
//    }
//
//    public  void saveBitmap2File(Bitmap bm, final String path, int quality) {
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
//                    tishi.setVisibility(View.VISIBLE);
//                    tishi.setText("上传图片中。。。");
//                }
//            });
//
//            link_P1(filePath,filePath2);
//
//
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
//
//
//    public static final int TIMEOUT = 1000 * 60;
//    private void link_P1(String filename1, final String fileName2) {
//
//
//        //final MediaType JSON=MediaType.parse("application/json; charset=utf-8");
//        //http://192.168.2.4:8080/sign?cmd=getUnSignList&subjectId=jfgsdf
//        OkHttpClient okHttpClient= new OkHttpClient.Builder()
//                .writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
//                .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
//                .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
//                .retryOnConnectionFailure(true)
//                .build();
//
//         /* 第一个要上传的file */
//        file1 = new File(filename1);
//        RequestBody fileBody1 = RequestBody.create(MediaType.parse("application/octet-stream") , file1);
//        final String file1Name = System.currentTimeMillis()+"testFile1.jpg";
//
////    /* 第二个要上传的文件,*/
////        File file2 = new File(fileName2);
////        RequestBody fileBody2 = RequestBody.create(MediaType.parse("application/octet-stream") , file2);
////        String file2Name =System.currentTimeMillis()+"testFile2.jpg";
//
//
////    /* form的分割线,自己定义 */
////        String boundary = "xx--------------------------------------------------------------xx";
//
//        MultipartBody mBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
//            /* 底下是上传了两个文件 */
//                .addFormDataPart("voiceFile" , file1Name , fileBody1)
//                  /* 上传一个普通的String参数 */
//                //  .addFormDataPart("subject_id" , subject_id+"")
//                //  .addFormDataPart("image_2" , file2Name , fileBody2)
//                .build();
//        Request.Builder requestBuilder = new Request.Builder()
//                // .header("Content-Type", "application/json")
//                .post(mBody)
//                .url(HOST + "/AppFileUploadServlet?FilePathPath=cardFilePath&AllowFileType=.jpg,.gif,.jpeg,.bmp,.png&MaxFileSize=10");
//
//        // step 3：创建 Call 对象
//        Call call = okHttpClient.newCall(requestBuilder.build());
//
//        //step 4: 开始异步请求
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        tishi.setText("上传图片出错，请返回后重试！");
//                    }
//                });
//                Log.d("AllConnects", "请求识别失败"+e.getMessage());
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                Log.d("AllConnects", "请求识别成功"+call.request().toString());
//                //获得返回体
//                try {
//
//                    ResponseBody body = response.body();
//                    String ss=body.string();
//
//                    Log.d("AllConnects", "aa   "+ss);
//
//                    JsonObject jsonObject= GsonUtil.parse(ss).getAsJsonObject();
//                    Gson gson=new Gson();
//                    Photos zhaoPianBean=gson.fromJson(jsonObject,Photos.class);
//                    userInfoBena.setCardPhoto(zhaoPianBean.getExDesc());
//                    link_P2(fileName2);
//
//
//                }catch (Exception e){
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            tishi.setText("上传图片出错，请返回后重试！");
//                        }
//                    });
//                    Log.d("WebsocketPushMsg", e.getMessage());
//                }
//            }
//        });
//
//
//    }
//
//    private void link_P2( String fileName2) {
//        //final MediaType JSON=MediaType.parse("application/json; charset=utf-8");
//        //http://192.168.2.4:8080/sign?cmd=getUnSignList&subjectId=jfgsdf
//        OkHttpClient okHttpClient= new OkHttpClient.Builder()
//                .writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
//                .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
//                .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
//                .retryOnConnectionFailure(true)
//                .build();
//
////         /* 第一个要上传的file */
////        File file1 = new File(filename1);
////        RequestBody fileBody1 = RequestBody.create(MediaType.parse("application/octet-stream") , file1);
////        final String file1Name = System.currentTimeMillis()+"testFile1.jpg";
//
//    /* 第二个要上传的文件,*/
//        file2 = new File(fileName2);
//        RequestBody fileBody2 = RequestBody.create(MediaType.parse("application/octet-stream") , file2);
//        String file2Name =System.currentTimeMillis()+"testFile2.jpg";
//
//
////    /* form的分割线,自己定义 */
////        String boundary = "xx--------------------------------------------------------------xx";
//
//        MultipartBody mBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
//            /* 底下是上传了两个文件 */
//                //  .addFormDataPart("image_1" , file1Name , fileBody1)
//                  /* 上传一个普通的String参数 */
//                //  .addFormDataPart("subject_id" , subject_id+"")
//                .addFormDataPart("voiceFile" , file2Name , fileBody2)
//                .build();
//        Request.Builder requestBuilder = new Request.Builder()
//                // .header("Content-Type", "application/json")
//                .post(mBody)
//                .url(HOST + "/AppFileUploadServlet?FilePathPath=scanFilePath&AllowFileType=.jpg,.gif,.jpeg,.bmp,.png&MaxFileSize=10");
//
//
//        // step 3：创建 Call 对象
//        Call call = okHttpClient.newCall(requestBuilder.build());
//
//        //step 4: 开始异步请求
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.d("AllConnects", "请求识别失败"+e.getMessage());
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        tishi.setText("上传图片出错，请返回后重试！");
//                    }
//                });
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                Log.d("AllConnects", "请求识别成功"+call.request().toString());
//                //删掉文件
//
//                //获得返回体
//                try {
//
//                    ResponseBody body = response.body();
//                    // Log.d("AllConnects", "aa   "+response.body().string());
//
//                    JsonObject jsonObject= GsonUtil.parse(body.string()).getAsJsonObject();
//                    Gson gson=new Gson();
//                    Photos zhaoPianBean=gson.fromJson(jsonObject,Photos.class);
//                    userInfoBena.setScanPhoto(zhaoPianBean.getExDesc());
//
//                    link_tianqi3();
//
//
//                }catch (Exception e){
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            tishi.setText("上传图片出错，请返回后重试！");
//                        }
//                    });
//                    Log.d("WebsocketPushMsg", e.getMessage());
//                }
//            }
//        });
//
//
//    }
//
//    private void link_tianqi3() {
//        //final MediaType JSON=MediaType.parse("application/json; charset=utf-8");
//        //http://192.168.2.4:8080/sign?cmd=getUnSignList&subjectId=jfgsdf
//        OkHttpClient okHttpClient= new OkHttpClient.Builder()
//                .writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
//                .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
//                .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
//                .retryOnConnectionFailure(true)
//                .build();
//
//
////    /* form的分割线,自己定义 */
////        String boundary = "xx--------------------------------------------------------------xx";
//        RequestBody body = new FormBody.Builder()
//                .add("cardPhoto",userInfoBena.getCardPhoto())
//                .add("scanPhoto",userInfoBena.getScanPhoto())
//                .build();
//
//        Request.Builder requestBuilder = new Request.Builder()
//                // .header("Content-Type", "application/json")
//                .post(body)
//                .url(HOST + "/compare.do");
//
//
//        // step 3：创建 Call 对象
//        Call call = okHttpClient.newCall(requestBuilder.build());
//
//        //step 4: 开始异步请求
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        tishi.setText("上传图片出错，请返回后重试！");
//                    }
//                });
//                Log.d("AllConnects", "请求识别失败"+e.getMessage());
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                Log.d("AllConnects", "请求识别成功"+call.request().toString());
//                //获得返回体
//                try {
//                    count++;
//
//                    ResponseBody body = response.body();
//                    //  Log.d("AllConnects", "识别结果返回"+response.body().string());
//
//                    JsonObject jsonObject= GsonUtil.parse(body.string()).getAsJsonObject();
//                    Gson gson=new Gson();
//                    final ShiBieBean zhaoPianBean=gson.fromJson(jsonObject,ShiBieBean.class);
//
//                    if (zhaoPianBean.getScore()>=75.0) {
//
//                        //比对成功
//                        sendBroadcast(new Intent("guanbi").putExtra("biduijieguo",true)
//                                .putExtra("xiangsidu",(zhaoPianBean.getScore()+"").substring(0,5))
//                                .putExtra("cardPath",userInfoBena.getCardPhoto()).putExtra("saomiaoPath",userInfoBena.getScanPhoto()));
//                        count=1;
//
//                    }else {
//
//
//                        if (count<=3){
//
//                            Message message=Message.obtain();
//                            message.what=22;
//                            mHandler.sendMessage(message);
//
//                            Thread.sleep(100);
//
//                            isTrue2=true;
//
//
//                        }else {
//
//                            sendBroadcast(new Intent("guanbi").putExtra("biduijieguo",false)
//                                    .putExtra("xiangsidu",(zhaoPianBean.getScore()+"").substring(0,5))
//                                    .putExtra("cardPath",userInfoBena.getCardPhoto()).putExtra("saomiaoPath",userInfoBena.getScanPhoto()));
//                            count=1;
//                        }
//
//                    }
//
//
//                }catch (Exception e){
//
//                    if (count<=3){
//
//                        Message message=Message.obtain();
//                        message.what=22;
//                        mHandler.sendMessage(message);
//
//                        try {
//                            Thread.sleep(1000);
//                        } catch (InterruptedException e1) {
//                            e1.printStackTrace();
//                        }
//
//                        isTrue2=true;
//
//
//                    }else {
//
//                        sendBroadcast(new Intent("guanbi").putExtra("biduijieguo",false).putExtra("xiangsidu","43.21")
//                                .putExtra("cardPath",userInfoBena.getCardPhoto()).putExtra("saomiaoPath",userInfoBena.getScanPhoto()));
//                        count=1;
//                    }
//
//                    Log.d("WebsocketPushMsg", e.getMessage());
//                }
//            }
//        });
//
//
//    }
//
//
//
//    private class SensorInfoReceiver extends BroadcastReceiver {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
//            String action = intent.getAction();
//            if (action.equals("guanbi")) {
//
//                finish();
//            }
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//
//        count=1;
//    }
//
//    @Override
//    protected void onDestroy() {
//
//        super.onDestroy();
//        unregisterReceiver(sensorInfoReceiver);
//    }
//}
