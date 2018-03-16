package com.example.administrator.photodemo.photo;

import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;

import java.lang.ref.WeakReference;

/**
 * 从图库中选择图片的相关类
 * Created by kevin on 2016/10/18.
 */
public class ChoosePhoto {

    /**
     * 从图库中选择图片的requestCode
     */
    public static final int CHOOSE_PHOTO = 10001;

    /**
     * 图库中选择图片后的回调activity
     */
    private static WeakReference<Activity> mWeakReference;

    /**
     * 构造器
//     * @param activity 回调activity
     */
    public ChoosePhoto(WeakReference<Activity> weakReference){
        mWeakReference = weakReference;
    }

    /**
     * 用于实时更新回掉activity，便于对应activity中的onActivityResult时刻保持正常接收状态
//     * @param activity
     */
    public static void setCallBackActivity(WeakReference<Activity> weakReference){
        mWeakReference = weakReference;
    }

    /**
     * 从图库中选择图片
     */
    public void choosePhoto(){
        Activity activity = mWeakReference.get();
        if (activity == null){
            return;
        }
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        activity.startActivityForResult(intent, CHOOSE_PHOTO);
    }

}
