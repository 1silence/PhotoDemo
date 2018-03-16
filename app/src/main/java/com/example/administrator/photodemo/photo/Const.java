package com.example.administrator.photodemo.photo;

/**
 * Created by zhangyi on 2017/4/24.
 */

public class Const {

    public static final String LOCK = "LOCK";//同步锁对象

    public static final boolean IS_TEST = false;

    public static final int TAKE_PHOTO = 10000;
    public static final int CHOOSE_PHOTO = 10001;
    public static final int CROP_PHOTO = 10003;

    public static final int LOCATION_REQUEST_CODE = 1;
    public static final int WRITE_STORAGE_REQUEST_CODE = 2;
    public static final int CAMERA_REQUEST_CODE = 3;

    public static final int VIDEO_RECORD = 11001;
    public static final int VIDEO_RECORD_FINISHED = 11002;

    public static final int COMPOSE_PICTURE = 12001;
    public static final int COMPOSE_PICTURE_OK = 12002;

    public static final int TAKE_PHOTO_REPEAT = 13001;
    public static final int TAKE_PHOTO_REPEAT_OK = 13002;

    public static final int NEED_UPLOAD_PICTURE = 14001;
    public static final int PICTURE_HAS_UPLOADED = 14002;

    public static final int PICTURE_CAN_DELETE = 14003;
    public static final int PICTURE_HAS_DELETED = 14004;

    public static final String STATE_OR_DATA_RECEIVER = "update_activity_state";

    public static final String PROVIDER_PATH = "com.example.administrator.photodemo.fileprovider";

}
