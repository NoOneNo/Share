package com.hengye.share.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.cache.BitmapCache;
import com.android.volley.cache.DiskLruCacheUtil;
import com.android.volley.cache.ImageDiskLruCache;
import com.android.volley.toolbox.BitmapUtil;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.ImageKey;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import java.io.File;


public class RequestManager {
	/**
	 * Log or request TAG
	 */
	public static final String TAG = "VolleyPatterns";

	/** Default on-disk cache directory. */
	private static final String DEFAULT_CACHE_DIR = "volley";
	
	private static RequestQueue mRequestQueue;
	private static com.android.volley.toolbox.ImageLoader mImageLoader;
	private static DiskBasedCache mDiskBasedCache;

	private static ImageDiskLruCache mImageDiskCache;
	private static BitmapCache mImageBitmapCache;

	private RequestManager() {
		// no instances
	}

	/**
	 * @param context A {@link Context} to use for creating the cache dir.
	 * @param cacheDirectoryName A filename to use for naming the volley cache dir.
	 * @param maxCacheSizeInBytes The maximum size of the volley cache in bytes.
	 */
	public static void init(Context context, String cacheDirectoryName, int maxCacheSizeInBytes) {
		DiskLruCacheUtil.init(context);
		mDiskBasedCache = getVolleyDiskCache(context, cacheDirectoryName, maxCacheSizeInBytes);
		mRequestQueue = Volley.newRequestQueue(context, null, mDiskBasedCache, ImageDiskLruCache.getInstance());
		mImageBitmapCache = BitmapCache.getInstance();
		mImageDiskCache = ImageDiskLruCache.getInstance();
		mImageLoader = new com.android.volley.toolbox.ImageLoader(mRequestQueue, mImageBitmapCache);
		VolleyLog.setIsLog(false);
	}

	public static void init(Context context) {
		init(context, null, 0);
	}

	private static DiskBasedCache getVolleyDiskCache(Context context, String cacheDirectoryName, int maxCacheSizeInBytes){
		File cacheDir = new File(context.getCacheDir(), TextUtils.isEmpty(cacheDirectoryName) ? DEFAULT_CACHE_DIR : cacheDirectoryName);

		DiskBasedCache diskBasedCache;
        if(maxCacheSizeInBytes == 0){
			diskBasedCache = new DiskBasedCache(cacheDir);
        }else{
			diskBasedCache = new DiskBasedCache(cacheDir, maxCacheSizeInBytes);
        }
		return diskBasedCache;
	}

	public static RequestQueue getRequestQueue() {
		if (mRequestQueue != null) {
			return mRequestQueue;
		} else {
			throw new IllegalStateException("RequestQueue not initialized");
		}
	}

	public static DiskBasedCache getDiskBasedCache() {
		if (mDiskBasedCache != null) {
			return mDiskBasedCache;
		} else {
			throw new IllegalStateException("DiskBasedCache not initialized");
		}
	}

	/**
	 * Returns instance of ImageLoader initialized with {@see FakeImageCache}
	 * which effectively means that no memory caching is used. This is useful
	 * for images that you know that will be show only once.
	 *
	 * @return
	 */
	public static ImageLoader getImageLoader() {
		if (mImageLoader != null) {
			return mImageLoader;
		} else {
			throw new IllegalStateException("ImageLoader not initialized");
		}
	}

	public static BitmapCache getImageBitmapCache(){
		if (mImageBitmapCache != null) {
			return mImageBitmapCache;
		} else {
			throw new IllegalStateException("BitmapCache not initialized");
		}
	}

	public static ImageDiskLruCache getImageDiskCache(){
		if (mImageDiskCache != null) {
			return mImageDiskCache;
		} else {
			throw new IllegalStateException("ImageDiskLruCache not initialized");
		}
	}

	/**
	 * Adds the specified request to the global queue, if tag is specified then
	 * it is used else Default TAG is used.
	 * 
	 * @param req
	 * @param tag
	 */
	public static <T> void addToRequestQueue(Request<T> req, String tag) {
		// set the default tag if tag is empty
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

		VolleyLog.d("Adding request to queue: %s", req.getUrl());

		mRequestQueue.add(req);
	}

	/**
	 * Adds the specified request to the global queue using the Default TAG.
	 * 
	 * @param req
	 */
	public static <T> void addToRequestQueue(Request<T> req) {
		// set the default tag if tag is empty
		req.setTag(TAG);

		mRequestQueue.add(req);
	}

	/**
	 * Cancels all pending requests by the specified TAG, it is important to
	 * specify a TAG so that the pending/ongoing requests can be cancelled.
	 * 
	 * @param tag
	 */
	public static void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}

	public static boolean isExistCacheAndNotExpired(String cacheKey){
		Cache.Entry entry = mDiskBasedCache.get(cacheKey);
		return !(entry == null || entry.isExpired());
	}

	/**
	 * Clear image disk cache;
	 */
	public static void clearImageDiskCache(){
		mImageDiskCache.clearCache();
	}

	/**
	 * @return the size of image disk cache;
     */
	public static long getImageDiskCacheSize(){
		return mImageDiskCache.getCacheSize();
	}

	/**
	 * @param url disk cache key
	 * @return the path of image disk cache.
     */
	public static String getImageDiskCachePath(String url){
		return mImageDiskCache.getDiskCachePath(url);
	}

	/**
	 * @param url disk cache key
	 * @return the file of image disk cache.
     */
	public static File getImageDiskCacheFile(String url) {
		return mImageDiskCache.getDiskCacheFile(url);
	}

	/**
	 * clear image bitmap cache
	 */
	public static void clearImageBitmapCache(){
		mImageBitmapCache.clearCache();
	}

	/**
	 * @param url bitmap cache key
	 * @return the bitmap of bitmap cache
     */
	public static Bitmap getBitmapFromCache(String url){
		return mImageBitmapCache.getBitmap(url);
	}

	/**
	 * put bitmap to cache.
	 * @param url bitmap cache key
	 * @param bitmap the bitmap put to cache
     */
	public static void putBitmapToCache(String url, Bitmap bitmap){
		mImageBitmapCache.putBitmap(url, bitmap);
	}

	/**
	 * If bitmap cache exist return bitmap cache, else return image disk cache;
	 * @param imageKey bitmap key
	 * @return bitmap by image key
     */
	public static Bitmap getBitmapByKey(ImageKey imageKey) {
		Bitmap bitmap = mImageBitmapCache.getBitmap(imageKey.getMemoryCacheKey());
		if(bitmap == null) {
			bitmap = mImageDiskCache.getBitmapFromDiskLruCache(imageKey);
			if(bitmap != null) {
				mImageBitmapCache.putBitmap(imageKey.getMemoryCacheKey(), bitmap);
			}
		}

		return bitmap;
	}

	/**
	 * see {@link #getBitmapByKey(ImageKey)}
	 * @param url
	 * @return
     */
	public static Bitmap getBitmapByUrl(String url) {
		return getBitmapByKey(new ImageKey(url, 0, 0, 0, 0, ImageView.ScaleType.FIT_CENTER));
	}

	public static Bitmap getBitmapByLocalPath(ImageKey imageKey){
		Bitmap bitmap = mImageBitmapCache.getBitmap(imageKey.getDiskCacheKey());
		if(bitmap == null) {
			bitmap = BitmapUtil.getSuitableBitmap(imageKey);
			if(bitmap != null) {
				mImageBitmapCache.putBitmap(imageKey.getDiskCacheKey(), bitmap);
			}
		}
		return bitmap;
	}

}

