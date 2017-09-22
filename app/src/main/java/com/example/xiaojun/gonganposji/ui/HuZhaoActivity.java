package com.example.xiaojun.gonganposji.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.TextureView;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.xiaojun.gonganposji.MyAppLaction;
import com.example.xiaojun.gonganposji.R;
import com.example.xiaojun.gonganposji.beans.BaoCunBean;
import com.example.xiaojun.gonganposji.beans.BaoCunBeanDao;
import com.example.xiaojun.gonganposji.beans.HuZhaoFanHuiBean;
import com.example.xiaojun.gonganposji.beans.Photos;
import com.example.xiaojun.gonganposji.beans.UserInfoBena;
import com.example.xiaojun.gonganposji.dialog.JiaZaiDialog;
import com.example.xiaojun.gonganposji.dialog.QueRenDialog;
import com.example.xiaojun.gonganposji.dialog.TiJIaoDialog;
import com.example.xiaojun.gonganposji.utils.FileUtil;
import com.example.xiaojun.gonganposji.utils.GsonUtil;
import com.example.xiaojun.gonganposji.utils.LibVLCUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sdsmdg.tastytoast.TastyToast;
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

public class HuZhaoActivity extends Activity implements View.OnClickListener {
    private ImageView huzhaoim,xianchangim;
    private EditText name,fanghao,dianhua;
    private TextView tuifang;
    private Button baocun,paizhao;
    private BaoCunBeanDao baoCunBeanDao=null;
    private BaoCunBean baoCunBean=null;
    private TextureView videoView;
    private MediaPlayer mediaPlayer=null;
    private IVLCVout vlcVout=null;
    private IVLCVout.Callback callback;
    private LibVLC libvlc;
    private Media media;
    private FaceDet mFaceDet;
    private LinearLayout ggg;
    private Bitmap bitmap2=null;
    private String benrenPath=null;
    private boolean isA=false;
    private File mSavePhotoFile;
    private final int REQUEST_TAKE_PHOTO=33;
    public static final int TIMEOUT = 1000 * 60;
    private JiaZaiDialog jiaZaiDialog=null;
    private UserInfoBena userInfoBena=null;
    private TiJIaoDialog tiJIaoDialog=null;
    private String zhuji=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hu_zhao);
        baoCunBeanDao= MyAppLaction.myAppLaction.getDaoSession().getBaoCunBeanDao();
        baoCunBean=baoCunBeanDao.load(123456L);
        mFaceDet = MyAppLaction.mFaceDet;

        if (baoCunBean!=null && baoCunBean.getZhuji()!=null){
            zhuji=baoCunBean.getZhuji();
        }else {
            Toast tastyToast= TastyToast.makeText(HuZhaoActivity.this,"请先设置主机地址",TastyToast.LENGTH_LONG,TastyToast.ERROR);
            tastyToast.setGravity(Gravity.CENTER,0,0);
            tastyToast.show();
        }

        userInfoBena=new UserInfoBena();


        String fn = "bbbb.jpg";
        FileUtil.isExists(FileUtil.PATH, fn);
        mSavePhotoFile=new File( FileUtil.SDPATH + File.separator + FileUtil.PATH + File.separator + fn);

        ititView();
        try {

            if (baoCunBean!=null){

                callback=new IVLCVout.Callback() {
                    @Override
                    public void onNewLayout(IVLCVout vlcVout, int width, int height, int visibleWidth, int visibleHeight, int sarNum, int sarDen) {

                    }

                    @Override
                    public void onSurfacesCreated(IVLCVout vlcVout) {

                        if (mediaPlayer != null && baoCunBean.getCameraIP() !=null) {

                            //"rtsp://192.168.2.56:554/user=admin_password=tljwpbo6_channel=1_stream=0.sdp?real_stream"
                            ///user=admin&password=&channel=1&stream=0.sdp
                            final Uri uri=Uri.parse("rtsp://"+baoCunBean.getCameraIP()+":554/user=admin_password=_channel=1_stream=0.sdp");
                            media = new Media(libvlc, uri);
                            mediaPlayer.setMedia(media);
                            videoView.setKeepScreenOn(true);
                            mediaPlayer.play();


                        }else {
                            Toast tastyToast= TastyToast.makeText(HuZhaoActivity.this,"请先设置摄像头IP",TastyToast.LENGTH_LONG,TastyToast.ERROR);
                            tastyToast.setGravity(Gravity.CENTER,0,0);
                            tastyToast.show();
                        }


                    }

                    @Override
                    public void onSurfacesDestroyed(IVLCVout vlcVout) {

                    }

                    @Override
                    public void onHardwareAccelerationError(IVLCVout vlcVout) {

//                                if (mediaPlayer != null && baoCunBean.getCameraIP() !=null) {
//                                    final Uri uri=Uri.parse("rtsp://"+baoCunBean.getCameraIP()+"/user=admin&password=&channel=1&stream=0.sdp");
//                                    media = new Media(libvlc, uri);
//                                    mediaPlayer.setMedia(media);
//                                    videoView.setKeepScreenOn(true);
//                                    mediaPlayer.play();
//
//                                }else {
//                                    Toast tastyToast= TastyToast.makeText(HuZhaoActivity.this,"请先设置摄像头IP",TastyToast.LENGTH_LONG,TastyToast.ERROR);
//                                    tastyToast.setGravity(Gravity.CENTER,0,0);
//                                    tastyToast.show();
//                                }

                    }
                };
                videoView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
                    @Override
                    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                      //  Log.d("HuZhaoActivity", "fffffffffffffff");
                        vlcVout.setVideoView(videoView);
                        vlcVout.addCallback(callback);
                        vlcVout.attachViews();

                    }

                    @Override
                    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
                        Log.d("HuZhaoActivity", "改变");
                    }

                    @Override
                    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                        Log.d("HuZhaoActivity", "onSurfaceTextureDestroyed销毁");

                        return true;
                    }

                    @Override
                    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
                        Log.d("HuZhaoActivity", "ddddd"+surface.getTimestamp());

                    }
                });


            }
        }catch (Exception e){
            Log.d("HuZhaoActivity", e.getMessage()+"");
        }

    }

    private void ititView() {
        videoView = (TextureView) findViewById(R.id.fff);
       // videoView.setAspectRatio(3, 4);
        ggg= (LinearLayout) findViewById(R.id.ggg);
        huzhaoim= (ImageView) findViewById(R.id.zhengjianim);
        huzhaoim.setOnClickListener(this);
        huzhaoim.setClickable(false);
        xianchangim= (ImageView) findViewById(R.id.xianchangim);
        xianchangim.setOnClickListener(this);
        name= (EditText) findViewById(R.id.name);
        fanghao= (EditText) findViewById(R.id.fanghao);
        dianhua= (EditText) findViewById(R.id.dianhua);
        tuifang= (TextView) findViewById(R.id.tuifang);
        tuifang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HuZhaoActivity.this, DatePickActivity.class);
                startActivityForResult(intent,2);
            }
        });
        baocun= (Button) findViewById(R.id.baocun);
        baocun.setOnClickListener(this);
        paizhao= (Button) findViewById(R.id.paizhao);
        paizhao.setOnClickListener(this);
        libvlc = LibVLCUtil.getLibVLC(HuZhaoActivity.this);
        mediaPlayer = new MediaPlayer(libvlc);
        vlcVout = mediaPlayer.getVLCVout();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.zhengjianim: //第一张

                startCamera();

                break;
            case R.id.xianchangim: //第二张
                if (vlcVout != null) {

                    AnimatorSet animatorSet = new AnimatorSet();
                    animatorSet.playTogether(
                            ObjectAnimator.ofFloat(ggg, "scaleY", 1f, 0f),
                            ObjectAnimator.ofFloat(ggg, "scaleX", 1f, 0f)
                            //	ObjectAnimator.ofFloat(helper.itemView,"alpha",0f,1f)
                    );
                    animatorSet.setDuration(600);
                    animatorSet.setInterpolator(new AccelerateInterpolator());
                    animatorSet.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            ggg.setVisibility(View.GONE);

                        }
                    });
                    animatorSet.start();
                }
                break;
            case R.id.baocun:
                if (!fanghao.getText().toString().equals("") && !userInfoBena.getScanPhoto().equals("") && !userInfoBena.getCardPhoto().equals("")){
                    link_save();
                }else {

                    Toast tastyToast= TastyToast.makeText(HuZhaoActivity.this,"你没有填写房号或拍照",TastyToast.LENGTH_LONG,TastyToast.ERROR);
                    tastyToast.setGravity(Gravity.CENTER,0,0);
                    tastyToast.show();
                }

                break;
            case R.id.paizhao:
                try {

                        paizhao.setClickable(false);
                        bitmap2 = videoView.getBitmap();
                        if (bitmap2 != null) {

                            List<VisionDetRet> results = mFaceDet.detect(bitmap2);

                            if (results != null) {
                                int s = results.size();
                                VisionDetRet face;
                                if (s > 0) {
                                    face = results.get(0);
                                    paizhao.setClickable(true);
                                    ggg.setVisibility(View.VISIBLE);
                                    int xx = 0;
                                    int yy = 0;
                                    int xx2 = 0;
                                    int yy2 = 0;
                                    int ww = bitmap2.getWidth();
                                    int hh = bitmap2.getHeight();
                                    if (face.getRight() - 300 >= 0) {
                                        xx = face.getRight() - 300;
                                    } else {
                                        xx = 0;
                                    }
                                    if (face.getTop() - 220 >= 0) {
                                        yy = face.getTop() - 220;
                                    } else {
                                        yy = 0;
                                    }
                                    if (xx + 430 <= ww) {
                                        xx2 = 430;
                                    } else {
                                        xx2 = ww - xx - 1;
                                    }
                                    if (yy + 430 <= hh) {
                                        yy2 = 430;
                                    } else {
                                        yy2 = hh - yy - 1;
                                    }

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

                                    Bitmap bitmap = Bitmap.createBitmap(bitmap2, xx, yy, xx2, yy2);

                                    String fn = "benren.jpg";
                                    FileUtil.isExists(FileUtil.PATH, fn);
                                    saveBitmap2File(bitmap, FileUtil.SDPATH + File.separator + FileUtil.PATH + File.separator + fn, 100);

                                    ObjectAnimator animator2 = ObjectAnimator.ofFloat(ggg, "scaleY", 0f, 1f);
                                    animator2.setDuration(600);//时间1s
                                    animator2.start();
                                    //起始为1，结束时为0
                                    ObjectAnimator animator = ObjectAnimator.ofFloat(ggg, "scaleX", 0f, 1f);
                                    animator.setDuration(600);//时间1s
                                    animator.addListener(new Animator.AnimatorListener() {
                                        @Override
                                        public void onAnimationStart(Animator animation) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animation) {

                                        }

                                        @Override
                                        public void onAnimationCancel(Animator animation) {

                                        }

                                        @Override
                                        public void onAnimationRepeat(Animator animation) {

                                        }
                                    });
                                    animator.start();


                                } else {
                                    final QueRenDialog dialog = new QueRenDialog(HuZhaoActivity.this, "没有检测到人脸,请重新拍摄!");
                                    dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
                                    dialog.setOnPositiveListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            paizhao.setClickable(true);
                                            dialog.dismiss();
                                        }
                                    });
                                    dialog.show();
                                }

                            }
                        }

                    }catch(Exception e){
                        Log.d("HuZhaoActivity", e.getMessage() + "");
                    }

                break;

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_TAKE_PHOTO:  //拍照
                    //注意，如果拍照的时候设置了MediaStore.EXTRA_OUTPUT，data.getData=null
                 //   huzhaoim.setImageURI(Uri.fromFile());
                    Glide.with(HuZhaoActivity.this)
                            .load(mSavePhotoFile)
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            //.transform(new GlideCircleTransform(RenGongFuWuActivity.this,1, Color.parseColor("#ffffffff")))
                            .into(huzhaoim);
                    link_P1(mSavePhotoFile);

                    break;
            }
        }
        if (resultCode == Activity.RESULT_OK && requestCode == 2) {
            // 选择预约时间的页面被关闭
            String date = data.getStringExtra("date");
            tuifang.setText(date);
        }
    }

    /**
     * 启动拍照
     * @param
     */
    private void startCamera() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Continue only if the File was successfully created
            if (mSavePhotoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(mSavePhotoFile));//设置文件保存的URI
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
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
            benrenPath=path;

            Glide.with(HuZhaoActivity.this)
                    .load(benrenPath)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    //.transform(new GlideCircleTransform(RenGongFuWuActivity.this,1, Color.parseColor("#ffffffff")))
                    .into(xianchangim);

             isA=true;

            huzhaoim.setClickable(true);

            link_P2();

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
      //  Log.d("HuZhaoActivity", "暂停");
        if (mediaPlayer!=null){
            mediaPlayer=null;
            media=null;
        }
        if (vlcVout!=null){
            vlcVout.detachViews();
            vlcVout.removeCallback(callback);
            callback=null;
            vlcVout=null;
        }
        if (videoView!=null){
            videoView.setSurfaceTextureListener(null);
            videoView=null;
        }
        if (libvlc!=null){
            libvlc.release();
            libvlc=null;
        }
        System.gc();
        super.onPause();
    }

    @Override
    protected void onDestroy() {


        super.onDestroy();
    }

    private void link_P1(File filename1 ) {
        jiaZaiDialog=new JiaZaiDialog(HuZhaoActivity.this);
        jiaZaiDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        jiaZaiDialog.setText("上传图片中...");
        if (!HuZhaoActivity.this.isFinishing()){
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

        RequestBody fileBody1 = RequestBody.create(MediaType.parse("application/octet-stream") , filename1);
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
                        Toast tastyToast= TastyToast.makeText(HuZhaoActivity.this,"上传图片出错，请返回后重试！",TastyToast.LENGTH_LONG,TastyToast.ERROR);
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

                }catch (Exception e){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (jiaZaiDialog!=null && jiaZaiDialog.isShowing()){
                                jiaZaiDialog.dismiss();
                                jiaZaiDialog=null;
                            }
                            Toast tastyToast= TastyToast.makeText(HuZhaoActivity.this,"上传图片出错，请返回后重试！",TastyToast.LENGTH_LONG,TastyToast.ERROR);
                            tastyToast.setGravity(Gravity.CENTER,0,0);
                            tastyToast.show();
                        }
                    });
                    Log.d("WebsocketPushMsg", e.getMessage());
                }
            }
        });

    }

    private void link_P2() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                jiaZaiDialog=new JiaZaiDialog(HuZhaoActivity.this);
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
       File file2 = new File(benrenPath);
        RequestBody fileBody2 = RequestBody.create(MediaType.parse("application/octet-stream") , file2);
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
                        Toast tastyToast= TastyToast.makeText(HuZhaoActivity.this,"上传图片出错，请返回后重试！",TastyToast.LENGTH_LONG,TastyToast.ERROR);
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
                   String ss=body.string();
                    JsonObject jsonObject= GsonUtil.parse(ss).getAsJsonObject();
                    Gson gson=new Gson();
                    Photos zhaoPianBean=gson.fromJson(jsonObject,Photos.class);
                    userInfoBena.setScanPhoto(zhaoPianBean.getExDesc());

                }catch (Exception e){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (jiaZaiDialog!=null && jiaZaiDialog.isShowing()){
                                jiaZaiDialog.dismiss();
                                jiaZaiDialog=null;
                            }
                            Toast tastyToast= TastyToast.makeText(HuZhaoActivity.this,"上传图片出错，请返回后重试！",TastyToast.LENGTH_LONG,TastyToast.ERROR);
                            tastyToast.setGravity(Gravity.CENTER,0,0);
                            tastyToast.show();
                        }
                    });
                    Log.d("WebsocketPushMsg", e.getMessage());
                }
            }
        });

    }

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
                .add("name",name.getText().toString().trim())
                .add("gender","0")
                .add("cardPassport",userInfoBena.getCardPhoto())
                .add("scanPassport",userInfoBena.getScanPhoto())
                .add("phone",dianhua.getText().toString().trim())
                .add("homeNumber",fanghao.getText().toString().trim())
                .add("outTime2",tuifang.getText().toString().trim())
                .add("accountId",baoCunBean.getJiudianID())
                .build();
        // Log.d("InFoActivity2", userInfoBena.getGender());
        Request.Builder requestBuilder = new Request.Builder()
                // .header("Content-Type", "application/json")
                .post(body)
                .url(zhuji+ "/savePassportResult.do");

        if (!HuZhaoActivity.this.isFinishing() && tiJIaoDialog==null  ){
            tiJIaoDialog=new TiJIaoDialog(HuZhaoActivity.this);
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast tastyToast = TastyToast.makeText(HuZhaoActivity.this, "网络出错,请检查网络", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        tastyToast.setGravity(Gravity.CENTER, 0, 0);
                        tastyToast.show();

                    }
                });
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
                    String ss = body.string();
                   // Log.d("HuZhaoActivity", ss);
                    JsonObject jsonObject= GsonUtil.parse(ss.trim()).getAsJsonObject();
                    Gson gson=new Gson();
                    HuZhaoFanHuiBean bean=gson.fromJson(jsonObject,HuZhaoFanHuiBean.class);

                        if (bean.getDtoResult()==0) {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    Toast tastyToast = TastyToast.makeText(HuZhaoActivity.this, "保存成功", TastyToast.LENGTH_LONG, TastyToast.INFO);
                                    tastyToast.setGravity(Gravity.CENTER, 0, 0);
                                    tastyToast.show();
                                    finish();
                                }
                            });


                        } else {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    Toast tastyToast = TastyToast.makeText(HuZhaoActivity.this, "保存失败", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                                    tastyToast.setGravity(Gravity.CENTER, 0, 0);
                                    tastyToast.show();

                                }
                            });

                        }

                }catch (Exception e){

                    if (tiJIaoDialog!=null){
                        tiJIaoDialog.dismiss();
                        tiJIaoDialog=null;
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Toast tastyToast = TastyToast.makeText(HuZhaoActivity.this, "网络出错,请检查网络", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                            tastyToast.setGravity(Gravity.CENTER, 0, 0);
                            tastyToast.show();

                        }
                    });
                    Log.d("WebsocketPushMsg", e.getMessage()+"");
                }
            }
        });


    }
}
