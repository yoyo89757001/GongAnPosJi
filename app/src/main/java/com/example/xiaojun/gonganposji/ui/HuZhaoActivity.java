package com.example.xiaojun.gonganposji.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.xiaojun.gonganposji.MyAppLaction;
import com.example.xiaojun.gonganposji.R;
import com.example.xiaojun.gonganposji.beans.BaoCunBean;
import com.example.xiaojun.gonganposji.beans.BaoCunBeanDao;
import com.example.xiaojun.gonganposji.dialog.QueRenDialog;
import com.example.xiaojun.gonganposji.utils.FileUtil;
import com.example.xiaojun.gonganposji.utils.LibVLCUtil;
import com.example.xiaojun.gonganposji.view.AutoFitTextureView;
import com.example.xiaojun.gonganposji.view.GlideCircleTransform;
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
import java.util.List;

public class HuZhaoActivity extends Activity implements View.OnClickListener {
    private ImageView huzhaoim,xianchangim;
    private EditText name,xingbie,dianhua,fanghao;
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
    private String shengfenzhengPath=null;
    private boolean isA=false;
    private File mSavePhotoFile;
    private final int REQUEST_TAKE_PHOTO=33;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hu_zhao);
        baoCunBeanDao= MyAppLaction.myAppLaction.getDaoSession().getBaoCunBeanDao();
        baoCunBean=baoCunBeanDao.load(123456L);
        mFaceDet = MyAppLaction.mFaceDet;

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
                        Log.d("HuZhaoActivity", baoCunBean.getCameraIP());
                        if (mediaPlayer != null && baoCunBean.getCameraIP() !=null) {
                            Log.d("HuZhaoActivity", baoCunBean.getCameraIP());
                            //"rtsp://192.168.2.56:554/user=admin_password=tljwpbo6_channel=1_stream=0.sdp?real_stream"
                            ///user=admin&password=&channel=1&stream=0.sdp
                            final Uri uri=Uri.parse("rtsp://"+baoCunBean.getCameraIP()+":554/user=admin_password=_channel=1_stream=0.sdp");
                            media = new Media(libvlc, uri);
                            mediaPlayer.setMedia(media);
                            videoView.setKeepScreenOn(true);
                            mediaPlayer.play();
                            mediaPlayer.retain();
                            mediaPlayer.setAudioDelay(1000);

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
        xingbie= (EditText) findViewById(R.id.xingbie);
        dianhua= (EditText) findViewById(R.id.dianhua);
        fanghao= (EditText) findViewById(R.id.fangjianhao);
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

                                    String fn = "ccc.jpg";
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
                   // link_P1(shengfenzhengPath,filePath2);

                    break;

            }
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
            shengfenzhengPath=path;

            Glide.with(HuZhaoActivity.this)
                    .load(shengfenzhengPath)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    //.transform(new GlideCircleTransform(RenGongFuWuActivity.this,1, Color.parseColor("#ffffffff")))
                    .into(xianchangim);

             isA=true;
//      kaishiPaiZhao();

            huzhaoim.setClickable(true);

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
        Log.d("HuZhaoActivity", "暂停");
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
}
