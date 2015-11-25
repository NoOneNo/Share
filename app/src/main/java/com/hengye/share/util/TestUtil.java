package com.hengye.share.util;

public class TestUtil {

    public static void calculateSize(int maxW, int maxH, int actualW, int actualH){
        if(actualW < maxW && actualH < maxH){
        //宽和高都不超出 不做任何处理。

        }else if(actualW < maxW || actualH < maxH){
        //宽或者高有一边超出 裁剪超出的一边。
        }else {
        //宽和高都超出
            if(actualW > 4096 && actualH > 4096){
                //循环等比例缩放，直至有一边不超过4096
            }

            if(actualW > 4096){
                //如果宽超出限制，计算宽应该裁剪的宽度

                int showW = actualH / maxH * maxW;
                actualW = showW;

                //修改Bitmap实际长宽
            }else if(actualH > 4096){
                //如果高超出限制，计算高应该裁剪的高度
                int showH = actualW / maxW* maxH;
                actualH = showH;

                //修改Bitmap实际长宽
            }


            //计算缩放比例
            if(actualW - maxW > actualH - maxH){
//                宽比高超出的多

//                最后根据
            }else{
//                高比宽超出的多

            }

        }

    }
}
