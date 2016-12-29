package com.hengye.share.util.http.okhttp;

import android.os.Looper;
import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by yuhy on 2016/12/18.
 */

public class ProgressResponseInterceptor implements Interceptor {

    private static final String TAG = "ProgressResponseInterce";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        return originalResponse.newBuilder()
                .body(new ProgressResponseBody(originalResponse.body(), new ProgressListener() {
                    @Override
                    public void update(long bytesRead, long contentLength, boolean done) {
                        Log.e(TAG, Looper.myLooper() + "");
                        Log.e(TAG, "onProgress: " + "total ---->" + contentLength + "done ---->" + bytesRead);
                    }
                }))
                .build();
    }
}
