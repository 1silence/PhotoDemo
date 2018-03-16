package com.example.administrator.photodemo;

import android.app.Application;
import android.graphics.Bitmap;

import java.util.Map;

/**
 * Created by kevin on 2016/10/19.
 */
public class TApplication extends Application {

    private static final String APATCH_PATH = "/out.apatch";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * 将重拍后的照片按位置存放
     */
    private Map<Integer, Bitmap> bitmapMap;

    public void setRepeatPicture(Map<Integer, Bitmap> bitmapMap) {
        this.bitmapMap = bitmapMap;
    }

    public Map<Integer, Bitmap> getRepeatPicture() {
        return bitmapMap;
    }

    /**
     * 将合成后的照片按位置存放
     */
    private Map<Integer, Bitmap> composeBitmapMap;

    public void setComposePicture(Map<Integer, Bitmap> composeBitmapMap) {
        this.composeBitmapMap = composeBitmapMap;
    }

    public Map<Integer, Bitmap> getComposePicture() {
        return composeBitmapMap;
    }
    /**
     * 是否包含拍摄按钮
     */
    private boolean isContainTakeBtn = true;

    public boolean isContainTakeBtn() {
        return isContainTakeBtn;
    }

    public void setContainTakeBtn(boolean containTakeBtn) {
        isContainTakeBtn = containTakeBtn;
    }

}
