package com.hengye.share.util.http.okhttp;

/**
 * Created by yuhy on 2016/12/18.
 */

public interface ProgressListener {

    /**
     * @param bytesRead     已经下载或上传字节数
     * @param contentLength 总字节数
     * @param done          是否完成
     */
    void update(long bytesRead, long contentLength, boolean done);
}
