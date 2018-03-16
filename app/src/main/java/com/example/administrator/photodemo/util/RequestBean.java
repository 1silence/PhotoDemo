package com.example.administrator.photodemo.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 请求类的bean
 */
public class RequestBean {

    public RequestBean() {
        headers = new HashMap<>();
        params = new HashMap<>();
    }

    /**
     * 请求URL
     */
    private String requestUrl;

    /**
     * 请求头
     */
    private Map<String, String> headers;

    /**
     * 请求体
     */
    private Map<String, Object> params;

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

}
