package com.hengye.share.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import java.io.File;

public class ImageUtil {

    public static int getBitmapMaxSize() {
        return 2048;
    }
    public static boolean isThisBitmapTooLargeToRead(String path) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int width = options.outWidth;
        int height = options.outHeight;
        if (width > getBitmapMaxSize() || height > getBitmapMaxSize()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isNoSuffixImg(String url){
        if(url != null){
            if(url.endsWith(".gif")
                    || url.endsWith(".jpg")
                    || url.endsWith(".jpeg")){
                return false;
            }
        }
        return true;
    }

    public static boolean isThisBitmapTooLargeToRead(Bitmap bitmap){
        if(bitmap == null){
            return false;
        }
        return bitmap.getWidth() > getBitmapMaxSize() || bitmap.getHeight() > getBitmapMaxSize();
    }

    public static boolean isThisPictureGif(String url) {
        return !TextUtils.isEmpty(url) && url.endsWith(".gif");
    }



    public static Bitmap decodeBitmapFromSDCard(String path,
                                                int reqWidth, int reqHeight) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }

    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (height > reqHeight && reqHeight != 0) {
                inSampleSize = (int) Math.floor((double) height / (double) reqHeight);
            }

            int tmp = 0;

            if (width > reqWidth && reqWidth != 0) {
                tmp = (int) Math.floor((double) width / (double) reqWidth);
            }

            inSampleSize = Math.max(inSampleSize, tmp);
        }
        int roundedSize;
        if (inSampleSize <= 8) {
            roundedSize = 1;
            while (roundedSize < inSampleSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (inSampleSize + 7) / 8 * 8;
        }

        return roundedSize;
    }
}
