package com.hengye.share.model.sina;

import com.hengye.share.model.Result;

/**
 * Created by yuhy on 2016/11/29.
 */

public class WBResult {

    public Result convert(){
        Result result = new Result();
        result.setResult(isResult());
        return result;
    }

    /**
     * result : true
     */

    private boolean result;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public boolean isSuccess(){
        return result;
    }
}
