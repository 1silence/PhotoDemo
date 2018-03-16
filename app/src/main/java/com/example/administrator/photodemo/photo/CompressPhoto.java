package com.example.administrator.photodemo.photo;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

/**
 * 压缩图片的相关类
 * Created by kevin on 2016/10/18.
 */
public class CompressPhoto {

    /**
     * 压缩图片，将图片压入到字节流当中，便于上传给服务器，或者封装到file中
     * @param bitmap
     * @return
     */
    public byte[] compress(Bitmap bitmap){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] bytes = bos.toByteArray();
        return bytes;
    }

}
