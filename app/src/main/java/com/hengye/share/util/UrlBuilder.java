package com.hengye.share.util;


import com.android.volley.error.AuthFailureError;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class UrlBuilder {

    private static final String DEFAULT_PARAMS_ENCODING = "UTF-8";

    private Map<String, String> mParameters;

    private String mUrl;

    private String mEncoding = DEFAULT_PARAMS_ENCODING;

    public UrlBuilder(){
        mParameters = new HashMap<>();
    }

    public UrlBuilder(String url){
        mParameters = new HashMap<>();
        mUrl = url;
    }

    public String getRequestUrl(){
        StringBuilder result = new StringBuilder();
        result.append(getUrl());
        result.append('?');
        for (Map.Entry<String, String> entry : getParameters().entrySet()) {
            result.append(entry.getKey());
            result.append('=');
            result.append(entry.getValue());
            result.append('&');
        }
        result.deleteCharAt(result.length() - 1);
        return result.toString();
    }

    @Override
    public String toString() {
        return getRequestUrl();
    }

    public void addParameter(String key, String value){
        mParameters.put(key, value);
    }

    public void addParameter(String key, int value){
        mParameters.put(key, String.valueOf(value));
    }

    public void addParameter(String key, float value) {
        mParameters.put(key, String.valueOf(value));
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    public Map<String, String> getParameters() {
        return mParameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.mParameters = parameters;
    }

    public byte[] getBody(){
        Map params = this.getParameters();
        return params != null && params.size() > 0? encodeParameters(params, this.getParamsEncoding()):null;
    }

    private static byte[] encodeParameters(Map<String, String> params, String paramsEncoding) {
        StringBuilder encodedParams = new StringBuilder();

        try {
            Iterator uee = params.entrySet().iterator();

            while(uee.hasNext()) {
                java.util.Map.Entry entry = (java.util.Map.Entry)uee.next();
                encodedParams.append(URLEncoder.encode((String) entry.getKey(), "UTF-8"));
                encodedParams.append('=');
                encodedParams.append(URLEncoder.encode((String)entry.getValue(), "UTF-8"));
                if(uee.hasNext()) {
                    encodedParams.append('&');
                }
            }

            return encodedParams.toString().getBytes();
        } catch (UnsupportedEncodingException var6) {
            throw new RuntimeException("Encoding not supported: " + paramsEncoding, var6);
        }
    }

    protected void setParamsEncoding(String encoding){
        mEncoding = encoding;
    }

    protected String getParamsEncoding() {
        return mEncoding;
    }
}
