package com.example.administrator.photodemo.act;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.administrator.photodemo.R;
import com.example.administrator.photodemo.TApplication;
import com.example.administrator.photodemo.photo.Const;
import com.example.administrator.photodemo.photo.ObtainPictureListener;
import com.example.administrator.photodemo.photo.PhotoBean;
import com.example.administrator.photodemo.photo.PictureActivity;
import com.example.administrator.photodemo.util.AssembleRequestUrlUtils;
import com.example.administrator.photodemo.util.OkHttpUtils;
import com.example.administrator.photodemo.util.RequestBean;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.OkHttpClient;

public class Photo2Activity extends PictureActivity implements ObtainPictureListener, View.OnClickListener {

    private ImageView imageView;
    private ImageView imageViewTwo;
    private ImageView imageViewThree;
    private TApplication application;
    /**
     * 图片数组和图片名数组
     */
    private File[] photos = new File[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo2);
        application = (TApplication) getApplication();
        findViewById(R.id.photo).setOnClickListener(this);
        findViewById(R.id.photo_two).setOnClickListener(this);
        findViewById(R.id.photo_dr).setOnClickListener(this);
        findViewById(R.id.login).setOnClickListener(this);
        imageView = findViewById(R.id.photo_image);
        imageViewTwo = findViewById(R.id.photo_image_two);
        imageViewThree = findViewById(R.id.photo_image_three);
        setCallBackListener(this, true);
    }

    @Override
    public void obtainPicture(Bitmap picture) {
        imageView.setImageBitmap(picture);
    }

    @Override
    public void obtainPictureWithPosition(Map<Integer, Bitmap> pictureMap) {
        Set set = pictureMap.keySet();
        Iterator<Integer> iterator = set.iterator();
        int position = iterator.next();
        Log.i("pictureMap", "" + position);
        switch (position) {
            case 0:
                imageView.setImageBitmap(pictureMap.get(0));
                convertFileFromBitmap(pictureMap.get(0), position, 0);
                RequestBean requestBean = new RequestBean();
                Map<String, String> params = new HashMap<>();
                params.put("idPic", String.valueOf(photos[0]));
                requestBean.setRequestUrl(AssembleRequestUrlUtils.getRequestUrl("/appV2/getIdCardAutoInfo", params));
                requestBean.getHeaders().put("Content-Type", "multipart/form-data;charset=utf-8");
                requestBean.getParams().put("idPic", photos[0]);
                OkHttpUtils.getInstance().request("post", requestBean, 1);
                break;
            case 1:
                imageViewTwo.setImageBitmap(pictureMap.get(1));
                Bitmap bitmap = compressScale(pictureMap.get(1));
                convertFileFromBitmap(bitmap, position, 100);


                RequestBean requestBean1 = new RequestBean();
                Map<String, String> params1 = new HashMap<>();
                requestBean1.setRequestUrl(AssembleRequestUrlUtils.getRequestUrl("/appV2/getIdCardAutoInfo", params1));
                requestBean1.getHeaders().put("Content-Type", "multipart/form-data;charset=utf-8");
                requestBean1.getParams().put("idPic", photos[1]);
                OkHttpUtils.getInstance().request("post", requestBean1, 1);


//                RequestBean requestBean1 = new RequestBean();
//                Map<String, String> params1 = new HashMap<>();
//                params1.put("realname", "张三");
//                params1.put("idcard", "121321312321312321");
//                params1.put("id_change", "1");
//                params1.put("driver_change", "0");
//                params1.put("areaId", "");
//                params1.put("email", "");
//                requestBean1.setRequestUrl(AssembleRequestUrlUtils.getRequestUrl("/appV2/updateUserInfoIdCard", params1));
//                requestBean1.getHeaders().put("Content-Type", "multipart/form-data;charset=utf-8");
//                requestBean1.getParams().put("idPic", photos[1]);
//                OkHttpUtils.getInstance().request("post", requestBean1, 1);
                break;
            case 2:
                imageViewThree.setImageBitmap(pictureMap.get(2));
                break;
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.photo:
                //从图库中选择
                mPhotograph.mTakePhoto.takePhoto();
//                mPhotograph.mChosePhoto.choosePhoto();
                setPicturePosition(1);
                break;
            case R.id.photo_two:
//                mPhotograph.mTakePhoto.takePhoto();
//                intoOperateActivity(1, true);
                mPhotograph.mChosePhoto.choosePhoto();
                setPicturePosition(0);
                break;
            case R.id.photo_dr:
                mPhotograph.mTakePhoto.takePhoto();
//                intoOperateActivity(1, true);
                setPicturePosition(2);
                break;

            case R.id.login:
                RequestBean requestBean = new RequestBean();
                Map<String, String> params = new HashMap<>();
                params.put("username", "13511038586");
                params.put("password", "123456");
                params.put("umengId", "Ajgxijlia7ldOiZxfNQiUHU7MQ1vy8ujXpHdL15p2Lrw");
                params.put("type", "1");
                requestBean.setRequestUrl(AssembleRequestUrlUtils.getRequestUrl("/appV2/login", params));
                OkHttpUtils.getInstance().request("get", requestBean, 0);
                break;
        }
    }

    public void intoOperateActivity(int position, boolean isNeedBatteryCapacity) {
        Intent intent = new Intent(this, Photo2Activity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("picPosition", position);
        bundle.putBoolean("batteryCapacity", isNeedBatteryCapacity);
        intent.putExtras(bundle);
        startActivityForResult(intent, Const.COMPOSE_PICTURE);
    }

/*    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Const.COMPOSE_PICTURE) {
            if (resultCode == Const.COMPOSE_PICTURE_OK) {
                Map<Integer, Bitmap> composePictureMap = application.getComposePicture();
                Set set = composePictureMap.keySet();
                Iterator<Integer> iterator = set.iterator();
                int picPosition = iterator.next();
                showAndSaveRepeatPicture(picPosition, composePictureMap);
            }
        }
        if (requestCode == Const.TAKE_PHOTO_REPEAT) {
            if (resultCode == Const.TAKE_PHOTO_REPEAT_OK) {
                Map<Integer, Bitmap> repeatPictureMap = application.getRepeatPicture();
                Set set = repeatPictureMap.keySet();
                Iterator<Integer> iterator = set.iterator();
                int picPosition = iterator.next();
                showAndSaveRepeatPicture(picPosition, repeatPictureMap);
            }
        }
    }*/

    /**
     * 显示并存储重拍后的照片
     *
     * @param position         照片所在的位置
     * @param repeatPictureMap 重拍后的照片map
     */
    private void showAndSaveRepeatPicture(int position, Map<Integer, Bitmap> repeatPictureMap) {
//        convertFileFromBitmap(repeatPictureMap.get(position), position);
    }

    /**
     * 将图片封装到File中，准备上传使用
     *
     * @param position
     * @param picSize  代表图片的大小(单位KB)，如果值为0，那么质量百分百上传。如果有值,按照大小进行质量控制
     */
    public void convertFileFromBitmap(Bitmap bitmap, int position, int picSize) {
        try {
            String picPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/pic" + position;
            File pictureFile = new File(picPath);
            //压缩图片
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            if (picSize == 0) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
//            } else {
//                int picQuality = 90;
//                while (bos.toByteArray().length / 1024 > picSize) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
//                    bos.reset(); // 重置baos即清空baos
//                    bitmap.compress(Bitmap.CompressFormat.JPEG, picQuality, bos);// 这里压缩options%，把压缩后的数据存放到baos中
//                    picQuality -= 10;// 每次都减少10
//                }
//            }
            byte[] bytes = bos.toByteArray();
            //将图片封装成File对象
            FileOutputStream outputStream = new FileOutputStream(pictureFile);
            outputStream.write(bytes);
            outputStream.close();
            bos.close();
            photos[position] = pictureFile;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 图片按比例大小压缩方法
     *
     * @param image （根据Bitmap图片压缩）
     * @return
     */
    public static Bitmap compressScale(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        // 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
        if (baos.toByteArray().length / 1024 > 1024) {
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 80, baos);// 这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        // float hh = 800f;// 这里设置高度为800f
        // float ww = 480f;// 这里设置宽度为480f
        float hh = 640f;
        float ww = 640f;
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) { // 如果高度高的话根据高度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be; // 设置缩放比例
        // newOpts.inPreferredConfig = Config.RGB_565;//降低图片从ARGB888到RGB565
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
        //return bitmap;
    }


    /**
     * 质量压缩方法
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 90;
        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset(); // 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    public void getIdCard(Bitmap bitmap) {
        RequestBean requestBean = new RequestBean();
        requestBean.setRequestUrl(AssembleRequestUrlUtils.getRequestUrl("/appV2/getIdCardAutoInfo", null));
        requestBean.getHeaders().put("Content-Type", "multipart/form-data;charset=utf-8");
        requestBean.getParams().put("idPic", "");
        OkHttpUtils.getInstance().request("post", requestBean, 1);

    }
}
