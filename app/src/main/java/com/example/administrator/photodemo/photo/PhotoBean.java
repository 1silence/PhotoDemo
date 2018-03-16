package com.example.administrator.photodemo.photo;

import android.graphics.Bitmap;


import java.io.Serializable;

/**
 * Created by zhangyi on 2017/6/12.
 */

public class PhotoBean implements Serializable {

    private String id;
    private String code;//图片编码。根据编码来判断是否需要特殊操作等等
    private String name;//图片对应的名称
    private String note;//拍摄示意图的操作说明
    private String demoImg;//示意图的拍摄url
    private String orderNum;

    private Bitmap picture;//拍摄的图片本身
    private boolean hasPicture;//该位置上是否已经有图片

    public PhotoBean() {
    }

    public PhotoBean(String id, String code, String name, String note, String demoImg, String orderNum, Bitmap picture, boolean hasPicture) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.note = note;
        this.demoImg = demoImg;
        this.orderNum = orderNum;
        this.picture = picture;
        this.hasPicture = hasPicture;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setDemoImg(String demoImg) {
        this.demoImg = demoImg;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(id);
//        dest.writeString(code);
//        dest.writeString(name);
//        dest.writeString(note);
//        dest.writeString(demoImg);
//        dest.writeString(orderNum);
//    }
//
//    public PhotoBean(Parcel in){
//        this.id = in.readString();
//        this.code = in.readString();
//        this.name = in.readString();
//        this.note = in.readString();
//        this.demoImg = in.readString();
//        this.orderNum = in.readString();
//    }
//
//    public static final Parcelable.Creator<PhotoBean> CREATOR = new Parcelable.Creator<PhotoBean>(){
//
//        @Override
//        public PhotoBean createFromParcel(Parcel source) {
//            return new PhotoBean(source);
//        }
//
//        @Override
//        public PhotoBean[] newArray(int size) {
//            return new PhotoBean[size];
//        }
//    };

    public Bitmap getPicture() {
        return picture;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }

    public boolean isHasPicture() {
        return hasPicture;
    }

    public void setHasPicture(boolean hasPicture) {
        this.hasPicture = hasPicture;
    }
}
