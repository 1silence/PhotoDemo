package com.example.administrator.photodemo.photo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.UUID;


/**
 * Created by kevin on 2016/10/18.
 */
public class TakePhoto {

    /**
     * 拍照的requestCode
     */
    public static final int TAKE_PHOTO = 10000;

    /**
     * 拍照时给图片设定的存放Uri
     */
    private Uri photoUri;

    /**
     * 拍照的回调activity
     */
    private static WeakReference<Activity> mWeakReference;

    /**
     * 构造器
     *
     */
    public TakePhoto(WeakReference<Activity> weakReference) {
        mWeakReference = weakReference;
    }

    /**
     * 用于实时更新回调activity，便于对应activity中的onActivityResult时刻保持正常接收状态
     *
//     * @param act
     */
    public static void setCallBackActivity(WeakReference<Activity> weakReference) {
        mWeakReference = weakReference;
    }

    /**
     * 拍照
     */
    public void takePhoto() {
        Activity activity = mWeakReference.get();
        if (activity == null){
            return;
        }
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Const.WRITE_STORAGE_REQUEST_CODE);//自定义的code
            return;
        }
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, Const.CAMERA_REQUEST_CODE);//自定义的code
            return;
        }
        //在拍照的时候，先设置一个uri，让拍好的图片保存到这里
        File photoFile = createTakePhotoFile();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= 24) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            photoUri = FileProvider.getUriForFile(activity, Const.PROVIDER_PATH, photoFile);//通过FileProvider创建一个content类型的Uri
        } else {
            photoUri = Uri.fromFile(photoFile);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);


        activity.startActivityForResult(intent, TAKE_PHOTO);
    }

    /**
     * 拍照图片的保存路径
     * @return
     */
    public File createTakePhotoFile() {
        File rootDirectory = Environment.getExternalStorageDirectory();
        File file = new File(rootDirectory, "/photo/take");
        if (!file.exists()) {
            file.mkdirs();
        }
        String fileName = UUID.randomUUID().toString() + ".png";
        return new File(file, fileName);
    }


    /**
     * 返回储存拍照图片的Uri
     *
     * @return
     */
    public Uri getPhotoUri() {
        if (photoUri == null) {
            return null;
        }
        return photoUri;
    }

}
