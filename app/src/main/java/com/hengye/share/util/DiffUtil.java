package com.hengye.share.util;

import java.util.List;

/**
 * Created by yuhy on 2016/10/18.
 */

public class DiffUtil {

    public static int checkEquals(List<? extends Object> dataList, List<? extends Object> targetList){
        if(dataList.isEmpty() || targetList.isEmpty()){
            return -1;
        }

        Object target = targetList.get(0);
        int dataLength = dataList.size();

        for(int i = dataLength - 1; i >= 0; i--){
            Object data = dataList.get(i);

            if(data == null){
                continue;
            }

            if(data.equals(target)){
                return i;
            }

        }
        return -1;
    }


}
