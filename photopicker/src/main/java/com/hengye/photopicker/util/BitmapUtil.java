package com.hengye.photopicker.util;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v4.util.LruCache;

public class BitmapUtil {
    private LruCache<String, Bitmap> mMemoryCache;

    private static volatile BitmapUtil instance;

    private BitmapUtil() {
        // 获取应用程序最大可用内存  
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @SuppressLint("NewApi")
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount();
            }
        };
    }

    public static BitmapUtil getInstance() {
        if (instance == null) {
            synchronized (BitmapUtil.class) {
                if (instance == null) {
                    instance = new BitmapUtil();
                }
            }
        }
        return instance;
    }

    /**
     * 将一张图片存储到LruCache中。
     *
     * @param key    LruCache的键，这里传入图片的URL地址。
     * @param bitmap LruCache的键，这里传入从网络上下载的Bitmap对象。
     */
    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemoryCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap loadBitmap(String path, int size) {
        return loadBitmap(path, size, size);
    }

    public Bitmap loadBitmap(String path, int maxWidth, int maxHeight) {
        Bitmap bitmap = BitmapUtil.getInstance().getBitmapFromMemoryCache(path);
        if (bitmap == null) {
            bitmap = BitmapUtil.decodeSampledBitmapFromPath(path, maxWidth, maxHeight);
            if (bitmap != null) {
                BitmapUtil.getInstance().addBitmapToMemoryCache(path, bitmap);
            }
        }
        return bitmap;
    }

    /**
     * 从LruCache中获取一张图片，如果不存在就返回null。
     *
     * @param key LruCache的键，这里传入图片的URL地址。
     * @return 对应传入键的Bitmap对象，或者null。
     */
    public Bitmap getBitmapFromMemoryCache(String key) {
        if (key == null) {
            return null;
        }
        return mMemoryCache.get(key);
    }

    public void clearCache() {
        if (mMemoryCache != null) {
            if (mMemoryCache.size() > 0) {
                mMemoryCache.evictAll();
            }
        }
    }

    public static Bitmap decodeSampledBitmapFromPath(String path, int maxWidth, int maxHeight) {
        return decodeSampledBitmapFromPath(path, maxWidth, maxHeight, null);
    }

    public static Bitmap decodeSampledBitmapFromPath(String path, int maxWidth, int maxHeight, String orientation) {
        // Long startTime, finishTime;
        // startTime = System.currentTimeMillis();
        if(path == null){
            return null;
        }

        Bitmap bitmap = null;
        // 给定的BitmapFactory设置解码的参数
        final BitmapFactory.Options options = new BitmapFactory.Options();
        // 从解码器中获取原始图片的宽高，这样避免了直接申请内存空间
        options.inJustDecodeBounds = true;
        try {
            BitmapFactory.decodeFile(path, options);
            // Calculate inSampleSize
            options.inSampleSize = calculateScale(options, maxWidth, maxHeight);
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inDither = false;
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeFile(path, options);

            if (orientation != null && orientation.length() != 0 && !orientation.equals("0")) {
                int angle;
                try {
                    angle = Integer.parseInt(orientation);
                } catch (Exception e) {
                    e.printStackTrace();
                    angle = 0;
                }

                if (0 < angle && angle < 360) {
                    Matrix m = new Matrix();
                    m.setRotate(angle); // 旋转angle度
                    Bitmap bitmapBefore = bitmap;
                    bitmap = (Bitmap.createBitmap
                            (bitmapBefore, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true));// 从新生成图片
                    bitmapBefore.recycle();
                }
            }

        } catch (OutOfMemoryError err) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        // finishTime = System.currentTimeMillis();
        // Log.i("ImageGalleryAdapter", "runtime decde bitmap : " + (finishTime
        // - startTime));
        return bitmap;
    }

//	public static Bitmap decodeSampledBitmapFromUri(ContentResolver cr,
//			Uri uri, int maxWidth, int maxHeight) {
//		// Long startTime, finishTime;
//		// startTime = System.currentTimeMillis();
//    	Bitmap bitmap = null;
//		InputStream input;
//		// 给定的BitmapFactory设置解码的参数
//		final BitmapFactory.Options options = new BitmapFactory.Options();
//		// 从解码器中获取原始图片的宽高，这样避免了直接申请内存空间
//		options.inJustDecodeBounds = true;
//		try {
//			input = cr.openInputStream(uri);
//			BitmapFactory.decodeStream(input, null, options);
//			input.close();
//			// Calculate inSampleSize
//			options.inSampleSize = calculateScale(options, maxWidth, maxHeight);
//			options.inPreferredConfig = Bitmap.Config.RGB_565;
//			options.inDither = false;
//			options.inJustDecodeBounds = false;
//			input = cr.openInputStream(uri);
//			bitmap = BitmapFactory.decodeStream(input, null, options);
//			input.close();
//		} catch (OutOfMemoryError err) {
//			return null;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//		// finishTime = System.currentTimeMillis();
//		// Log.i("ImageGalleryAdapter", "runtime decde bitmap : " + (finishTime
//		// - startTime));
//		return bitmap;
//	}

    public static int calculateScale(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        //如果图片实际宽度大于屏幕宽度，进行缩放
        int actualWidth = options.outWidth;
        int actualHeight = options.outHeight;
        float scale;
        if (actualWidth > reqWidth || actualHeight > reqHeight) {
            //横向缩放图片
            if (actualWidth - reqWidth > actualHeight - reqHeight) {
//				Log.i("mDebug", "横向缩放图片");
                scale = reqWidth / (actualWidth * 1.0f);
            }
            //竖向缩放图片
            else {
//				Log.i("mDebug", "竖向缩放图片");
                scale = reqHeight / (actualHeight * 1.0f);
            }
        }
        //显示图片实际大小，不需要缩放
        else {
//			Log.i("mDebug", "不需要缩放图片");
            scale = 1f;
        }

        return Math.round(1 / scale);
    }

    public class BitmapData {

        private Bitmap bitmap;
        private int actualWidth;
        private int actualHeight;
        private int showWidth;
        private int showHeight;
        private int scale;

        public Bitmap getBitmap() {
            return bitmap;
        }

        public void setBitmap(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        public int getActualWidth() {
            return actualWidth;
        }

        public void setActualWidth(int actualWidth) {
            this.actualWidth = actualWidth;
        }

        public int getActualHeight() {
            return actualHeight;
        }

        public void setActualHeight(int actualHeight) {
            this.actualHeight = actualHeight;
        }

        public int getShowWidth() {
            return showWidth;
        }

        public void setShowWidth(int showWidth) {
            this.showWidth = showWidth;
        }

        public int getShowHeight() {
            return showHeight;
        }

        public void setShowHeight(int showHeight) {
            this.showHeight = showHeight;
        }

        public int getScale() {
            return scale;
        }

        public void setScale(int scale) {
            this.scale = scale;
        }

        @Override
        public String toString() {
            return "BitmapData [bitmap=" + bitmap + ", actualWidth=" + actualWidth + ", actualHeight=" + actualHeight + ", showWidth=" + showWidth + ", showHeight=" + showHeight + ", scale=" + scale
                    + "]";
        }
    }
}




















