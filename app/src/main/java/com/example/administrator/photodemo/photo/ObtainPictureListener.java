package com.example.administrator.photodemo.photo;

import android.graphics.Bitmap;

import java.util.Map;

/**
 * 此回调用来获取父类中的图片对象
 * Created by kevin on 2016/10/28.
 */
public interface ObtainPictureListener {

    /**
     * 无标记，适用于adapter的显示
     * @param picture
     */
    void obtainPicture(Bitmap picture);

    /**
     * 给图片一个位置标记
     * @param pictureMap
     */
    void obtainPictureWithPosition(Map<Integer, Bitmap> pictureMap);

}
