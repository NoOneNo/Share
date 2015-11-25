package com.hengye.share.util;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.util.LruCache;
import android.util.Log;

import java.io.InputStream;

public class ImageLoader {

//	private static final Logger L = LoggerFactory.getLogger(ImageLoader.class);
	/**
	 * 图片缓存技术的核心类，用于缓存所有下载好的图片，在程序内存达到设定值时会将最少最近使用的图片移除掉。
	 */
	private static LruCache<String, BitmapData> mMemoryCache;

	public class BitmapData{
		private Bitmap bitmap;
		private int width;
		private int height;
		private float scale;
		
		public BitmapData(){
		}
		public BitmapData(Bitmap bitmap, int width, int height){
			super();
			this.bitmap = bitmap;
			this.width = width;
			this.height = height;
		}
		public Bitmap getBitmap(){
			return bitmap;
		}
		public void setBitmap(Bitmap bitmap){
			this.bitmap = bitmap;
		}
		public int getWidth(){
			return width;
		}
		public void setWidth(int width){
			this.width = width;
		}
		public int getHeight(){
			return height;
		}
		public void setHeight(int height){
			this.height = height;
		}
		public float getScale(){
			return scale;
		}
		public void setScale(float scale){
			this.scale = scale;
		}
		
	}
	/**
	 * ImageLoader的实例。
	 */	
	private volatile static ImageLoader mImageLoader;

	private ImageLoader() {
		// 获取应用程序最大可用内存
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int cacheSize = maxMemory / 8;
		// 设置图片缓存大小为程序最大可用内存的1/8
		mMemoryCache = new LruCache<String, BitmapData>(cacheSize) {
			@Override
			protected int sizeOf(String key, BitmapData bitmapData) {
				if(bitmapData.getBitmap() == null){
					return 0;
				}
				return bitmapData.getBitmap().getByteCount();
			}
		};
	}

	/**
	 * 获取ImageLoader的实例。
	 * 
	 * @return ImageLoader的实例。
	 */
    
    public static ImageLoader getInstance() {  
	    if (mImageLoader == null) {  
	        synchronized (ImageLoader.class) {  
		        if (mImageLoader == null) {  
		        	mImageLoader = new ImageLoader();  
		        }  
	        }  
	    }  
	    return mImageLoader;  
    }  

	/**
	 * 将一张图片存储到LruCache中。
	 * 
	 * @param key
	 *            LruCache的键，这里传入图片的URL地址。
	 * @param bitmap
	 *            LruCache的键，这里传入从网络上下载的Bitmap对象。
	 */
	public void addBitmapToMemoryCache(String key, BitmapData bitmapData) {
		if (getBitmapFromMemoryCache(key) == null) {
			mMemoryCache.put(key, bitmapData);
		}
	}

	/**
	 * 从LruCache中获取一张图片，如果不存在就返回null。
	 * 
	 * @param key
	 *            LruCache的键，这里传入图片的URL地址。
	 * @return 对应传入键的Bitmap对象，或者null。
	 */
	public BitmapData getBitmapFromMemoryCache(String key) {
		return mMemoryCache.get(key);
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth) {
		// 源图片的宽度
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (width > reqWidth) {
			// 计算出实际宽度和目标宽度的比率
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = widthRatio;
		}
		return inSampleSize;
	}

	public static Bitmap decodeSampledBitmapFromResource(String pathName,
														 int reqWidth) {
		// 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(pathName, options);
		// 调用上面定义的方法计算inSampleSize值
		options.inSampleSize = calculateInSampleSize(options, reqWidth);
		// 使用获取到的inSampleSize值再次解析图片
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(pathName, options);
	}
	
	public static BitmapData decodeSampledBitmapFromPathNoBitmap(String path, int maxWidth, int maxHeight){
    	BitmapData bd = null;
		// 给定的BitmapFactory设置解码的参数
		final BitmapFactory.Options options = new BitmapFactory.Options();
		// 从解码器中获取原始图片的宽高，这样避免了直接申请内存空间
		options.inJustDecodeBounds = true;
		try {
			BitmapFactory.decodeFile(path, options);
			// Calculate inSampleSize
			bd = calculateScale(options, maxWidth, maxHeight);;
			options.inSampleSize = Math.round(1 / bd.getScale());
//			L.debug("image scale : {}", 1 / scale);
//			options.inJustDecodeBounds = false;
//			Bitmap bitmap = BitmapFactory.decodeFile(path, options);
			bd.setBitmap(null);
//			input.close();
		} catch (OutOfMemoryError err) {
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bd;	
	}
	
    public static BitmapData decodeSampledBitmapFromPath(String path, int maxWidth, int maxHeight) {
		// Long startTime, finishTime;
		// startTime = System.currentTimeMillis();
    	BitmapData bd = null;
		// 给定的BitmapFactory设置解码的参数
		final BitmapFactory.Options options = new BitmapFactory.Options();
		// 从解码器中获取原始图片的宽高，这样避免了直接申请内存空间
		options.inJustDecodeBounds = true;
		try {
			BitmapFactory.decodeFile(path, options);
			// Calculate inSampleSize
			bd = calculateScale(options, maxWidth, maxHeight);;
			options.inSampleSize = Math.round(1 / bd.getScale());
//			L.debug("image scale : {}", 1 / scale);
			options.inJustDecodeBounds = false;
			Bitmap bitmap = BitmapFactory.decodeFile(path, options);
			bd.setBitmap(bitmap);
//			input.close();
		} catch (OutOfMemoryError err) {
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		// finishTime = System.currentTimeMillis();
		// Log.i("ImageGalleryAdapter", "runtime decde bitmap : " + (finishTime
		// - startTime));
		return bd;
	}  	
    
    public static BitmapData decodeSampledBitmapFromUri(ContentResolver cr,
														Uri uri, int maxWidth, int maxHeight) {
		// Long startTime, finishTime;
		// startTime = System.currentTimeMillis();
    	BitmapData bd = null;
		InputStream input;
		// 给定的BitmapFactory设置解码的参数
		final BitmapFactory.Options options = new BitmapFactory.Options();
		// 从解码器中获取原始图片的宽高，这样避免了直接申请内存空间
		options.inJustDecodeBounds = true;
		try {
			input = cr.openInputStream(uri);
			BitmapFactory.decodeStream(input, null, options);
			input.close();
			// Calculate inSampleSize
			bd = calculateScale(options, maxWidth, maxHeight);;
			options.inSampleSize = Math.round(1 / bd.getScale());
//			L.debug("image scale : {}", 1 / scale);
			options.inJustDecodeBounds = false;
			input = cr.openInputStream(uri);
			Bitmap bitmap = BitmapFactory.decodeStream(input, null, options);
			bd.setBitmap(bitmap);
			input.close();
		} catch (OutOfMemoryError err) {
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		// finishTime = System.currentTimeMillis();
		// Log.i("ImageGalleryAdapter", "runtime decde bitmap : " + (finishTime
		// - startTime));
		return bd;
	}  	
    
//	private void calculateScale(int reqWidth, int reqHeight){
	public static BitmapData calculateScale(BitmapFactory.Options options, int maxWidth, int maxHeight){
		//如果图片实际宽度大于屏幕宽度，进行缩放
		int reqWidth = options.outWidth;
		int reqHeight = options.outHeight;
		float scale; 
		int showWidth, showHeight;
//		L.debug("bitmap width : {}, height : {}, max width : {}, height : {}", reqWidth, reqHeight, maxWidth, maxHeight);
		BitmapData bd = ImageLoader.getInstance().new BitmapData();
		if(reqWidth > maxWidth || reqHeight > maxHeight){
			//横向缩放图片
			if(reqWidth - maxWidth > reqHeight - maxHeight){
				Log.i("mDebug", "横向缩放图片");
				scale = maxWidth / (reqWidth * 1.0f);
				showWidth = maxWidth;
				showHeight = (int) (reqHeight * scale);
			}
			//竖向缩放图片
			else{
				Log.i("mDebug", "竖向缩放图片");
				scale = maxHeight / (reqHeight * 1.0f);
				showWidth = (int) (reqWidth * scale);
				showHeight = maxHeight;
			}
		}
		//显示图片实际大小，不需要缩放
		else{
			Log.i("mDebug", "不需要缩放图片");
			scale = 1f;
			showWidth = reqWidth;
			showHeight = reqHeight;
		}	
		
		bd.setWidth(showWidth);
		bd.setHeight(showHeight);
		bd.setScale(scale);
		
		return bd;
	}    
}












