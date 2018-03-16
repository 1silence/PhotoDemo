package com.example.administrator.photodemo.photo;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;


/**
 * 关于拍照，选择图片，裁剪的基类activity。
 * 子类通过继承此activity，并且实现{@link ObtainPictureListener}接口。
 * 子类利用回调，来获取基类activity中的图片。此图片是通过裁剪后的最终图片。
 */
// TODO: 2017/6/6 这个框架存在个问题，两个Activity同时继承这个基类activity时，
// TODO: 2017/6/6  由于父类是个单例对象，所以之前继承这个activity的类中将收不到onActivityResult回调
// TODO: 2018/3/12 此类问题已解决
public abstract class PictureActivity extends Activity {

    /**
     * 图片对象
     */
    public Photograph mPhotograph;

    /**
     * 子类获取图片的回调
     */
    private ObtainPictureListener mObtainPictureListener;

    /**
     * 拍照时，自己封装的图片uri
     */
    private Uri photoUri;

    /**
     * 用于判断是否输出带标记的图片
     */
    private boolean isIncludePositionMap;

    /**
     * 用于标记某张图片的具体位置
     */
    private int mPicturePosition;

    public String imagePath;

    /**
     * 标记是图库中的图片进行裁剪，还是拍好的图片进行裁剪
     */
    private boolean isFromAlbum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPhotograph = Photograph.getInstance(this);
    }

    /**
     * 拿到回调句柄，便于得到图片时通知子类activity更新UI
     *
     * @param mObtainPictureListener
     * @param isIncludePositionMap   true为返回含位置的map集合
     */
    public void setCallBackListener(ObtainPictureListener mObtainPictureListener, boolean isIncludePositionMap) {
        this.mObtainPictureListener = mObtainPictureListener;
        this.isIncludePositionMap = isIncludePositionMap;
    }

    /**
     * 通知输出该图片的位置
     *
     * @param mPicturePosition
     */
    public void setPicturePosition(int mPicturePosition) {
        this.mPicturePosition = mPicturePosition;
    }

    /**
     * 所有拍照、选择图片、裁剪图片等行为，都在此监听
     *
     * @param requestCode 请求码
     * @param resultCode  结果码
     * @param data        图片数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != -1) {
            return;
        }
        switch (requestCode) {
            case TakePhoto.TAKE_PHOTO:
                photoUri = mPhotograph.mTakePhoto.getPhotoUri();
//                mPhotograph.cropPicture(photoUri);
                Bitmap mPicture = null;
                try {
//                    if (Build.VERSION.SDK_INT >= 24) {
//                        mPicture = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.fromFile(mPhotograph.createCropFile())));
//                    } else {
                    mPicture = BitmapFactory.decodeStream(getContentResolver().openInputStream(photoUri));
//                    }

                    if (isIncludePositionMap) {
                        Map<Integer, Bitmap> bitmapMap = new HashMap<>();
                        bitmapMap.put(mPicturePosition, mPicture);
                        mObtainPictureListener.obtainPictureWithPosition(bitmapMap);
                    } else {
                        mObtainPictureListener.obtainPicture(mPicture);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case ChoosePhoto.CHOOSE_PHOTO:
//                handleImageOnKitKat(data);
                Bitmap mPicture1 = null;
                try {
//                    if (Build.VERSION.SDK_INT >= 24) {
//                        mPicture = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.fromFile(mPhotograph.createCropFile())));
//                    } else {
                    mPicture1 = BitmapFactory.decodeStream(getContentResolver().openInputStream(data.getData()));
//                    }

                    if (isIncludePositionMap) {
                        Map<Integer, Bitmap> bitmapMap = new HashMap<>();
                        bitmapMap.put(mPicturePosition, mPicture1);
                        mObtainPictureListener.obtainPictureWithPosition(bitmapMap);
                    } else {
                        mObtainPictureListener.obtainPicture(mPicture1);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


                break;
            case Photograph.CROP_PHOTO:
                /*Bitmap mPicture = null;
                try {
                    if (Build.VERSION.SDK_INT >= 24) {
                        mPicture = BitmapFactory.decodeStream(getContentResolver().openInputStream(Photograph.cropUri));
                    } else {
                        mPicture = BitmapFactory.decodeStream(getContentResolver().openInputStream(photoUri));
                    }

                    if (isIncludePositionMap) {
                        Map<Integer, Bitmap> bitmapMap = new HashMap<>();
                        bitmapMap.put(mPicturePosition, mPicture);
                        mObtainPictureListener.obtainPictureWithPosition(bitmapMap);
                    } else {
                        mObtainPictureListener.obtainPicture(mPicture);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }*/
                break;
        }
    }


    private void handleImageOnKitKat(Intent data) {
        imagePath = null;
        photoUri = data.getData();
        if (DocumentsContract.isDocumentUri(this, photoUri)) {
            //如果是document类型的uri,则通过document id处理
            String docId = DocumentsContract.getDocumentId(photoUri);
            if ("com.android.providers.media.documents".equals(photoUri.getAuthority())) {
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.downloads.documents".equals(photoUri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(photoUri.getScheme())) {
            //如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(photoUri, null);
        } else if ("file".equalsIgnoreCase(photoUri.getScheme())) {
            //如果是file类型的Uri,直接获取图片路径即可
            imagePath = photoUri.getPath();
        }
        mPhotograph.cropPicture(photoUri);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过Uri和selection老获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

}
