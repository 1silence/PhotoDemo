package com.example.administrator.photodemo.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/2/27.
 */

public class OkHttpUtils {

    private static final int SEND_MSG_MAIN_PROCESS_FOR_SUCCESS = 10000;
    private static final int SEND_MSG_MAIN_PROCESS_FOR_FAILURE = 10001;
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");
    public static OkHttpUtils okHttpUtils;
    private OkHttpClient okHttpClient;
    private static WeakReference<Context> mWeakReference;

    public static OkHttpUtils getInstance() {
        if (okHttpUtils == null) {
            synchronized (OkHttpUtils.class) {
                if (okHttpUtils == null) {
                    okHttpUtils = new OkHttpUtils();
                }
            }
        }
        return okHttpUtils;
    }

    private OkHttpUtils() {
        okHttpClient = new OkHttpClient.Builder()
                .cookieJar(new CookieJar() {
                    private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        cookieStore.put(url.host(), cookies);
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        List<Cookie> cookies = cookieStore.get(url.host());
                        return cookies != null ? cookies : new ArrayList<Cookie>();
                    }
                })
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();

    }


    /**
     * 创建请求对象
     *
     * @param requestMethod 请求类型 get\post
     * @param requestBean   请求参数
     * @param temp
     * @return
     */
    private Request createRequestClient(String requestMethod, RequestBean requestBean, int temp) {
        if (requestBean == null || TextUtils.isEmpty(requestBean.getRequestUrl())) {
            return null;
        }
        //获取请求所需要的数据
        String requestUrl = requestBean.getRequestUrl();
        Map<String, String> headers = requestBean.getHeaders();
        Map<String, Object> params = requestBean.getParams();

        Request.Builder builder = new Request.Builder();
        //请求url
        builder.url(requestUrl);
        //写入消息头
        Iterator<Map.Entry<String, String>> headerIterator = headers.entrySet().iterator();
        while (headerIterator.hasNext()) {
            Map.Entry<String, String> header = headerIterator.next();
            builder.addHeader(header.getKey(), header.getValue());
        }
        //

        if (temp == 1) {
            String next = params.keySet().iterator().next();
            RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), (File) params.get(next));
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(next, next + ".jpg", fileBody)
                    .build();

/*            MultipartBody.Builder builder1 = new MultipartBody.Builder();
            builder1.setType(MultipartBody.FORM);
            String next = params.keySet().iterator().next();
            File file = (File) params.get(next);
            String name = file.getName();
            builder1.addFormDataPart(next, name, RequestBody.create(MEDIA_TYPE_PNG, (File) params.get(next)));*/
            if (TextUtils.equals(requestMethod, "post")) {
                builder.post(requestBody);
            } else {
                builder.get();
            }
        } else {

            FormBody.Builder formBuilder = new FormBody.Builder();
            Iterator<Map.Entry<String, Object>> paramIterator = params.entrySet().iterator();
            while (paramIterator.hasNext()) {
                Map.Entry<String, Object> param = paramIterator.next();
                formBuilder.add(param.getKey(), (String) param.getValue());
            }
//            RequestBody requestBody = formBuilder.build();
            if (TextUtils.equals(requestMethod, "post")) {
                builder.post(formBuilder.build());
            } else {
                builder.get();
            }
        }


        return builder.build();
    }

    public void request(String requestMethod, final RequestBean requestBean, int temp) {
        if (requestBean == null) {
            throw new RuntimeException("在调用request()前, 请先设置请求所需参数");
        }
        Request requestClient = createRequestClient(requestMethod, requestBean, temp);
        okHttpClient.newCall(requestClient).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.toString().contains("Timeout")) {
                    Log.i("jsonObject", "错误" + call.toString() + e.getLocalizedMessage());
                    return;
                }
                Log.i("jsonObject", "错误" + call.toString() + e.getLocalizedMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String string = response.body().string();
                    JSONObject jsonObject = new JSONObject(string);
                    Log.i("jsonObject", "" + jsonObject);
                    String retCode = jsonObject.getString("retCode");
                    if (TextUtils.equals(retCode, "0000")) {
                        String body = jsonObject.getString("body").toString();
                    } else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
