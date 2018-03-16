package com.example.administrator.photodemo.photo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.UUID;

/**
 * 这是一个单例对象。在这个对象中拥有拍照、从图库中选择图片等多个入口。
 * Created by kevin on 2016/10/18.
 */
public class Photograph {

    /**
     * 裁剪图片对应的requestCode
     */
    public static final int CROP_PHOTO = 10003;

    /**
     * 从图库中选择图片类的对象
     */
    public ChoosePhoto mChosePhoto;

    /**
     * 拍照类的对象
     */
    public TakePhoto mTakePhoto;

    /**
     * 压缩图片的对象
     */
    public CompressPhoto mCompressPhoto;

    /**
     * 此activity为回调activity，任何对于图片的操作，都会在此activity中收到回调。
     */
//    private static Activity mActivity;

    private static WeakReference<Activity> weakReference;

    public static Uri cropUri;

    public Photograph() {
        mCompressPhoto = new CompressPhoto();
        mChosePhoto = new ChoosePhoto(weakReference);
        mTakePhoto = new TakePhoto(weakReference);
    }

    /**
     * 单例模式
     * 但是由于这里是单例，所以导致ChoosePhoto、TakePhoto中的activity无法随着newInstance()的调用而更新。
     * 会导致在多次调用newInstance()之后，在onActivityResult()中无法收到对应的回调(因为每一次activity销毁并重新创建后，都是不同的activity实例。)
     * so , 我们需要在每次调用newInstance()时,顺便调用setCallBackActivity。
     * <p>
     * 上面问题，已经通过非单例来解决
     *
     * @param activity
     * @return
     */
    public static Photograph getInstance(Activity activity) {
        weakReference = new WeakReference<>(activity);
        TakePhoto.setCallBackActivity(weakReference);
        ChoosePhoto.setCallBackActivity(weakReference);
        return new Photograph();
    }

    /**
     * 裁剪图片
     *
     * @param photoUri 需要被裁剪图片的Uri
     */
    public void cropPicture(Uri photoUri) {
        if (photoUri == null) {
            return;
        }
        Intent cropIntent = new Intent("com.android.camera.action.CROP"); //剪裁
        cropIntent.setDataAndType(photoUri, "image/*");
        cropIntent.putExtra("scale", true);
        //设置宽高比例
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        //设置裁剪图片宽高
        cropIntent.putExtra("outputX", 600);
        cropIntent.putExtra("outputY", 600);
        cropIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        if (Build.VERSION.SDK_INT >= 24) {
            cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            cropUri = Uri.fromFile(createCropFile());
            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, cropUri);//此处用photoUri的话，是一个经过包装的ContentUri，会报错不能识别
        } else {
            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        }
        Activity activity = weakReference.get();
        if (activity != null) {
            activity.startActivityForResult(cropIntent, CROP_PHOTO);
        }
    }

    /**
     * 给图片添加水印
     *
     * @param resBitmap
     * @return
     */
    public Bitmap addWaterMark(Bitmap resBitmap, String mark) {
        int w = resBitmap.getWidth();
        int h = resBitmap.getHeight();

        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        Paint p = new Paint();
        p.setAntiAlias(true);// 去锯齿
        canvas.drawBitmap(resBitmap, 0, 0, p);
        //开始添加水印
        p.setColor(Color.RED);
        p.setTextSize(20);
        canvas.drawText(mark, 30, 30, p);
        p.setColor(Color.YELLOW);
        canvas.drawText(mark, 30, 60, p);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return bmp;
    }

    /**
     * 两张图片合成为一张
     *
     * @param bitmap1
     * @param bitmap2
     * @return
     */
    public Bitmap compositeBitmap(Bitmap bitmap1, Bitmap bitmap2) {
        Bitmap desBitmap = Bitmap.createBitmap(bitmap1.getWidth() + bitmap2.getWidth(), bitmap1.getHeight(), bitmap1.getConfig());
        Canvas canvas = new Canvas(desBitmap);
        canvas.drawBitmap(bitmap1, 0, 0, new Paint());
        canvas.drawBitmap(bitmap2, bitmap1.getWidth(), 0, new Paint());
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return desBitmap;
    }

    /**
     * 创建
     *
     * @return
     */
    public File createCropFile() {
        File rootDirectory = Environment.getExternalStorageDirectory();
        File file = new File(rootDirectory, "/photo/crop");
        if (!file.exists()) {
            file.mkdirs();
        }
        String fileName = UUID.randomUUID().toString() + ".png";
        File cropFile = new File(file, fileName);
        return cropFile;
    }


}
