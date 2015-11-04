package com.hengye.share.util;


import java.util.HashMap;
import java.util.Map;

public class UrlBuilder {

    private Map<String, String> mParameters;

    private String mUrl;

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
}
