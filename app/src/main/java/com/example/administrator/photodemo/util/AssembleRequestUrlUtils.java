package com.example.administrator.photodemo.util;

import android.text.TextUtils;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangyi on 2018/3/7.
 */

public class AssembleRequestUrlUtils {

    public static final String IP_AND_HOST = "http://www.ready-go.cn:88/service?";

    private static final String API_VERSION = "1.3.0";
    private static final String API_AUTH = "test";
    private static final String API_SIGN_TYPE = "sha-1";
    private static final String API_MAC_KEY = "87654321";

    public static String getRequestUrl(String mti, Map<String, String> requestParams){
        if (TextUtils.isEmpty(mti)) {
            throw new RuntimeException("请求的url不能为空");
        }
        String timestamp = System.currentTimeMillis() + "";
        //设置普遍参数
        Map<String, String> generalParams = new HashMap<>();
        generalParams.put("mti", mti);
        generalParams.put("version", API_VERSION);
        generalParams.put("timestamp", timestamp);
        generalParams.put("tid", timestamp);
        generalParams.put("authname", API_AUTH);
        generalParams.put("signType", API_SIGN_TYPE);

        //将想要传递的参数放到完整的map中
        Iterator<Map.Entry<String, String>> iterator = requestParams.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, String> params = iterator.next();
            String key = params.getKey();
            String value = params.getValue();
            generalParams.put(key, value);
        }

        //将key进行字典排序
        List<String> keys = new ArrayList<>();
        Iterator<Map.Entry<String, String>> paramsIterator = generalParams.entrySet().iterator();
        while (paramsIterator.hasNext()){
            Map.Entry<String, String> allParams = paramsIterator.next();
            String key = allParams.getKey();
            keys.add(key);
        }
        Collections.sort(keys);
        //开始拼接get请求的url
        String assembleUrlEncode = "";//url编码的请求地址
        String assembleUriEncode = IP_AND_HOST;//uri编码的请求地址
        try {
            for (String key : keys){
                assembleUriEncode += key + "=" + URLEncoder.encode(generalParams.get(key), "UTF-8") + "&";
                assembleUrlEncode += key + "=" + generalParams.get(key) + "&";
            }
            assembleUriEncode = assembleUriEncode.substring(0, assembleUriEncode.length() - 1);
            assembleUrlEncode = assembleUrlEncode.substring(0, assembleUrlEncode.length() - 1);
        }catch (Exception e){

        }
        //开始sha1签名加密
        String mac = assembleUrlEncode + "&key=" + API_MAC_KEY;
        mac = getSha1(mac).toUpperCase();
        assembleUriEncode += "&mac=" + mac;
        return assembleUriEncode;
    }

    /**
     *
     * @param str
     * @return
     */
    public static String getSha1(String str){
        if(str==null||str.length()==0){
            return null;
        }
        char hexDigits[] = {'0','1','2','3','4','5','6','7','8','9',
                'a','b','c','d','e','f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("UTF-8"));

            byte[] md = mdTemp.digest();
            int j = md.length;
            char buf[] = new char[j*2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (Exception e) {
            return null;
        }
    }

}
