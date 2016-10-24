package com.hengye.photopicker.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hengye.photopicker.R;
import com.hengye.photopicker.fragment.PhotoPickerFragment.SelectPhotoCallBackForPhotoAlbumAdapter;
import com.hengye.photopicker.model.Photo;
import com.hengye.photopicker.model.PhotoAlbum;
import com.hengye.photopicker.util.BitmapUtil;
import com.hengye.photopicker.util.PhotosUtil;
import com.hengye.photopicker.util.ThreadPoolUtils;

public class PhotoAlbumAdapter extends BaseAdapter implements OnScrollListener {

    private static final int MSG_SHOW_PHOTO_ALBUM = 0;

    //	private final String mUrlSuffix = "/photoAlbum";
    private Context mContext;
    private List<PhotoAlbum> mData;
    private ListView mListView;
    private int mImageSize;
    private BitmapUtil mBitmapUtil;
    private SelectPhotoCallBackForPhotoAlbumAdapter mSelectPhotoCallBackForPhotoAlbumAdapter;
    /**
     * 第一张可见图片的下标
     */
    private int mFirstVisibleItem;

    /**
     * 一屏有多少张图片可见
     */
    private int mVisibleItemCount;
    /**
     * 预读除了可见之外的图片的数量
     */
    private int mInvisibleItemCount = 0;
    /**
     * 记录是否刚打开程序，用于解决进入程序不滚动屏幕，不会下载图片的问题。
     */
    private boolean isFirstEnter = true;

    public PhotoAlbumAdapter(Context mContext, List<PhotoAlbum> mData, ListView mListView,
                             SelectPhotoCallBackForPhotoAlbumAdapter mSelectPhotoCallBackForPhotoAlbumAdapter) {
        this.mContext = mContext;
        this.mData = mData;
        this.mListView = mListView;
        this.mSelectPhotoCallBackForPhotoAlbumAdapter = mSelectPhotoCallBackForPhotoAlbumAdapter;
        this.mBitmapUtil = BitmapUtil.getInstance();
        int mMargin1Dp = mContext.getResources().getDimensionPixelSize(R.dimen.margin_1_dp);
        this.mImageSize = mMargin1Dp * 50;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public PhotoAlbum getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_listview_photo_album_item, parent, false);
            vh = new ViewHolder();
            vh.mPhotoAlbumItemLL = (LinearLayout) convertView.findViewById(R.id.adapter_listview_photo_album_item_layout);
            vh.mPhotoALbumItemIV = (ImageView) convertView.findViewById(R.id.adapter_listview_photo_album_item_img);
            vh.mPhotoAlbumName = (TextView) convertView.findViewById(R.id.adapter_listview_photo_album_item_name);
            vh.mPhotoAlbumCount = (TextView) convertView.findViewById(R.id.adapter_listview_photo_album_item_num);
            vh.mPhotoAlbumPoint = (View) convertView.findViewById(R.id.adapter_listview_photo_album_item_point);
            convertView.setTag(vh);
            initCustomThemeStyle(vh);
//			adjustImageSize(mVH.mImageViewRL);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        PhotoAlbum pa = mData.get(position);
        Photo photo = mData.get(position).getCoverPhoto();
        final String path = photo.isVideo() ? photo.getThumbnail() : photo.getDataPath();
        vh.mPhotoALbumItemIV.setTag(path);
        setImageView(path, vh.mPhotoALbumItemIV);
        vh.mPhotoAlbumName.setText(mData.get(position).getBucketDisplayName());

        vh.mPhotoAlbumCount.setText("(" + mData.get(position).getImagesSize() + ")");

        if (isExistSelectPhoto(pa, mSelectPhotoCallBackForPhotoAlbumAdapter.getSelectPhotos())) {
            vh.mPhotoAlbumPoint.setVisibility(View.VISIBLE);
        } else {
            vh.mPhotoAlbumPoint.setVisibility(View.GONE);
        }

        if (position == mSelectPhotoCallBackForPhotoAlbumAdapter.getPhotoAlbumIndexNow()) {
//            mVH.mPhotoAlbumItemLL.setBackgroundColor(mContext.getResources().getColor(R.color.select_photo_listview_photo_album_item_select_2));
            vh.mPhotoAlbumItemLL.setBackgroundColor(mContext.getResources().
                    getColor(mSelectPhotoCallBackForPhotoAlbumAdapter.getCustomTheme().getAlbumListItemSelected()));
        } else {
            vh.mPhotoAlbumItemLL.setBackgroundColor(mContext.getResources().getColor(R.color.select_photo_album_listview_layout));
        }

        return convertView;
    }

    private void initCustomThemeStyle(ViewHolder vh){
        vh.mPhotoAlbumPoint.setBackgroundResource(mSelectPhotoCallBackForPhotoAlbumAdapter.getCustomTheme().getAlbumPointSelected());
    }

    private boolean isExistSelectPhoto(PhotoAlbum pa, List<Photo> selectPhotos) {
        if (selectPhotos.size() == 0) {
            return false;
        }
//        //如果是第一个相册，是没有相册文件夹路径的
//        if (pa.getDataPath() == null) {
//            return true;
//        }
        //如果是第一个所有图片、视频相册
        if(PhotosUtil.ALL_IMAGES_ALBUM.equals(pa.getBucketId())){
            return true;
        }
        //如果是所有视频相册
        boolean isAllVideoAlbum = false;
        if(PhotosUtil.ALL_VIDEO_ALBUM.equals(pa.getBucketId())){
            isAllVideoAlbum = true;
        }

        String bucketId = pa.getBucketId();
        for (Photo p : selectPhotos) {
            if(isAllVideoAlbum){
                //如果是所有视频相册
                if(p.isVideo()){
                    return true;
                }
            }else {
                if (p.getBucketId() != null && p.getBucketId().equals(bucketId)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 给ImageView设置图片。首先从LruCache中取出图片的缓存，设置到ImageView上。如果LruCache中没有该图片的缓存，
     * 就给ImageView设置一张默认图片。
     *
     * @param imageUrl  图片的URL地址，用于作为LruCache的键。
     * @param imageView 用于显示图片的控件。
     */
    private void setImageView(String imageUrl, ImageView imageView) {
        Bitmap bitmap = mBitmapUtil.getBitmapFromMemoryCache(getCacheKey(imageUrl));
        if (imageView != null) {
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
                imageView.setBackgroundColor(0xffffffff);
            } else {
                imageView.setImageBitmap(null);
                imageView.setBackgroundColor(0x66666666);
            }
        }
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // 仅当GridView静止时才去下载图片，GridView滑动时取消所有正在下载的任务  
        if (scrollState == SCROLL_STATE_IDLE) {
            loadBitmaps(mFirstVisibleItem, mVisibleItemCount, true);
        } else {
//            cancelAllTasks();  
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        mFirstVisibleItem = firstVisibleItem;
        mVisibleItemCount = visibleItemCount;
        // 下载的任务应该由onScrollStateChanged里调用，但首次进入程序时onScrollStateChanged并不会调用，  
        // 因此在这里为首次进入程序开启下载任务。  
        if (isFirstEnter && visibleItemCount > 0) {
            loadBitmaps(firstVisibleItem, visibleItemCount, true);
            isFirstEnter = false;
        }
    }

    /**
     * 加载Bitmap对象。此方法会在LruCache中检查所有屏幕中可见的ImageView的Bitmap对象，
     * 如果发现任何一个ImageView的Bitmap对象不在缓存中，就会开启异步线程去下载图片。
     *
     * @param firstVisibleItem 第一个可见的ImageView的下标
     * @param visibleItemCount 屏幕中总共可见的元素数
     */
    private void loadBitmaps(int firstVisibleItem, int visibleItemCount, final boolean isShowImage) {
        try {
            final int mFirstInvisibleItemCount = firstVisibleItem + visibleItemCount;
            //mInvisibleItemCount是为了滑动的时候提前加载不可见的图片,预先设定好加载数量,为了更好的体验;
            for (int i = firstVisibleItem; i < firstVisibleItem + visibleItemCount + mInvisibleItemCount; i++) {
                //触发scroll的时候有预先加载6张图片，防止溢出, 不能等于0是因为第一张图片是拍照;
                if (i >= this.mData.size()) {
                    return;
                }
//            	else if(i == 0){
//            		continue;
//            	}
                final String path;
                Photo photo = this.mData.get(i).getCoverPhoto();
                if(photo == null){
                    continue;
                }
                if(photo.isVideo()){
                    path = photo.getThumbnail();
                }else{
                    path = photo.getDataPath();
                }
                Bitmap bitmap = mBitmapUtil.getBitmapFromMemoryCache(getCacheKey(path));
                if (bitmap == null) {
                    final int index = i;

                    ThreadPoolUtils.getInstance().execute(new Runnable() {
                        @Override
                        public void run() {
                            Bitmap bitmap = BitmapUtil.decodeSampledBitmapFromPath(path, mImageSize, mImageSize, mData.get(index).getCoverPhoto().getOrientation());
                            if (bitmap == null) {
                                return;
                            }

                            mBitmapUtil.addBitmapToMemoryCache(getCacheKey(path), bitmap);
                            //如果此时加载的是可见的图片，才通知UI线程加载图片
                            if (index < mFirstInvisibleItemCount && isShowImage) {
                                Message msg = Message.obtain();
                                msg.what = MSG_SHOW_PHOTO_ALBUM;
                                msg.obj = path;
                                mHandler.sendMessage(msg);
                            }
                        }
                    });

                } else {
                    ImageView imageView = (ImageView) mListView.findViewWithTag(path);
                    if (imageView != null) {
                        imageView.setImageBitmap(bitmap);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SHOW_PHOTO_ALBUM:
                    String imageUrl = (String) msg.obj;
                    // 根据Tag找到相应的ImageView控件，将下载好的图片显示出来。
                    ImageView imageView = (ImageView) mListView.findViewWithTag(imageUrl);
                    setImageView(imageUrl, imageView);
                    break;
//			case MSG_SCROLL_TO_FIRST:
//				ImageGalleryAdapter.this.mGridView.scrollTo(0, 0);
//				break;
                default:
                    break;

            }
        }

    };

    public void preLoadbBitmaps() {
        loadBitmaps(mListView.getFirstVisiblePosition(), mListView.getFirstVisiblePosition() + 8, true);
    }

    public void refresh(List<PhotoAlbum> mData) {
//		this.mBitmapUtil.clearCache();

        if (mData == null) {
            this.mData.clear();
        } else {
            this.mData = mData;
        }
        //预读12张图片
        loadBitmaps(0, 12, true);
        notifyDataSetChanged();
    }

    public String getCacheKey(String key){
        return "photoAlbum:" + key;
    }

    static class ViewHolder {
        LinearLayout mPhotoAlbumItemLL;
        ImageView mPhotoALbumItemIV;
        TextView mPhotoAlbumName;
        TextView mPhotoAlbumCount;
        View mPhotoAlbumPoint;
    }
}
