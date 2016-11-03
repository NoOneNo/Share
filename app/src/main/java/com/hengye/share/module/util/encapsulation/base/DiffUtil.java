package com.hengye.share.module.util.encapsulation.base;

import java.util.List;

/**
 * Created by yuhy on 2016/10/18.
 */

public class DiffUtil {

    public static int checkEquals(List<?> dataList, List<?> targetList){
        if(dataList.isEmpty() || targetList.isEmpty()){
            return -1;
        }

        Object target = targetList.get(0);
        int dataLength = dataList.size();

        //从dataList尾部查找
        for(int i = dataLength - 1; i >= 0; i--){
            Object data = dataList.get(i);

            if(data == null){
                continue;
            }

            //data跟targetList第一个数据相等
            if(data.equals(target)){

                //如果是最后一个
                if(i == dataLength - 1){
                    return i;
                }

                //data位置到dataList最后的长度必须小于或等于target位置到targetList最后的长度
                //如果大于则忽略
                if(dataLength - i > targetList.size()){
                    continue;
                }
                //从data到dataList最后的数据是否跟target位置后面的数据匹配
                boolean isEquals = true;
                for(int j = i + 1; j < dataLength; j++){
                    Object dataObj = dataList.get(j);
                    Object targetObj = targetList.get(j - i);

                    if(dataObj != null && targetObj != null){
                        if(!dataObj.equals(targetObj)){
                            isEquals = false;
                        }
                    }
                }

                if(isEquals){
                    return i;
                }

            }

        }
        return -1;
    }


}
