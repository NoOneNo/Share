package com.hengye.photopicker.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hengye.photopicker.R;
import com.hengye.photopicker.activity.ThemeActivity;
import com.hengye.photopicker.fragment.PhotoPickerFragment.SelectPhotoCallBackForPhotoAdapter;
import com.hengye.photopicker.model.Photo;
import com.hengye.photopicker.util.BitmapUtil;
import com.hengye.photopicker.util.PhotoPicker;
import com.hengye.photopicker.util.ThreadPoolUtils;
import com.hengye.photopicker.util.theme.BaseTheme;

public class PhotoAdapter extends BaseAdapter implements OnScrollListener {
    // private static final String TAG = "MainActivity";

    protected static final int MSG_SHOW_PHOTO = 1;

    protected static final int MSG_SHOW_PHOTO_SHADOW = 2;

    private Context mContext;
    private ArrayList<Photo> mData;
    private GridView mGridView;
    private int mImageSize;
    private BitmapUtil mBitmapUtil;

//    private ArrayList<Photo> mSelectPhotos;// 用于保存选择的图片
    //	private Map<Photo, Boolean> mSelectPhotoMap;// 用于保存图片的是否选择状态
    private Animation mSelectPhotoAnim;
    private SelectPhotoCallBackForPhotoAdapter mSelectPhotoCallBackForPhotoAdapter;// 选择图片后回调activity的方法操控activity的UI更新
    private BaseTheme mTheme;
    private int mMaxSelectImageCount;// 可选择图片的最大数量
    private int mMaxSelectVideoCount;// 可选择视频的最大数量

    private boolean isPressed;

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

    public PhotoAdapter(Context mContext, ArrayList<Photo> mData, GridView mGridView,
                        SelectPhotoCallBackForPhotoAdapter mSelectPhotoCallBackForPhotoAdapter) {
        this.mContext = mContext;
        this.mData = mData;
        this.mGridView = mGridView;
        this.mSelectPhotoCallBackForPhotoAdapter = mSelectPhotoCallBackForPhotoAdapter;
        mTheme = mSelectPhotoCallBackForPhotoAdapter.getCustomTheme();
//        this.mMaxSelectImageCount = mMaxSelectImageCount;
        this.mBitmapUtil = BitmapUtil.getInstance();
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        int mMargin1Dp = mContext.getResources().getDimensionPixelSize(R.dimen.margin_1_dp);
        this.mImageSize = (dm.widthPixels - mMargin1Dp * 5 * 2) / 3;
        initSelectPhotoAnim();

        addTakePhotoIfNeed();
    }

    private void addTakePhotoIfNeed(){
        if(mTheme.isCanTakePhoto()){
            if(mData != null){
                if(mData.size() != 0){
                    if(!mData.get(0).isNull()){
                        this.mData.add(0, new Photo(true));
                    }
                }else{
                    this.mData.add(0, new Photo(true));
                }
            }
        }
    }

    private void initSelectPhotoAnim() {
        mSelectPhotoAnim = new ScaleAnimation(1.0f, 1.15f, 1.0f, 1.15f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mSelectPhotoAnim.setDuration(250);
        mSelectPhotoAnim.setInterpolator(new DecelerateInterpolator());
        mSelectPhotoAnim.setFillAfter(false);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Photo getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.adapter_gridview_photo_item, parent, false);
            vh = new ViewHolder();
            vh.mTakePhotoLayout = convertView.findViewById(R.id.adapter_gridview_take_photo_layout);
            vh.mVideoLayout = convertView.findViewById(R.id.adapter_gridview_video_layout);
            vh.mVideoDuration = (TextView) convertView.findViewById(R.id.adapter_gridview_video_duration);
            vh.mImageViewFL = (FrameLayout) convertView
                    .findViewById(R.id.adapter_gridview_frame_layout);
            vh.mImageViewRL = (RelativeLayout) convertView
                    .findViewById(R.id.adapter_gridview_photo_item_layout);
            vh.mImageView = (ImageView) convertView
                    .findViewById(R.id.adapter_gridview_photo_item_img);
            vh.mUnSelectBtn = convertView
                    .findViewById(R.id.adapter_gridview_photo_item_btn_unselect);
            vh.mSelectBtn = convertView.findViewById(R.id.adapter_gridview_photo_item_btn_select);
            vh.mSelect = convertView.findViewById(R.id.adapter_gridview_photo_item_select_scope);
            vh.mShadowSelect = convertView.findViewById(R.id.adapter_gridview_photo_item_shadow_select);
            vh.mShadowClick = convertView.findViewById(R.id.adapter_gridview_photo_item_shadow_click);
            vh.mFooterView = (ViewStub) convertView.findViewById(R.id.adapter_gridview_footer);
            convertView.setTag(vh);
            adjustImageSize(vh.mImageViewFL);
            initCustomThemeStyle(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        vh.position = position;
        initSelectPhotoListener(vh);

        Photo photo = mData.get(position);

        if(photo.isNull()){

            vh.mTakePhotoLayout.setVisibility(View.VISIBLE);
            vh.mImageViewRL.setVisibility(View.GONE);
        }else {
            vh.mTakePhotoLayout.setVisibility(View.GONE);
            vh.mImageViewRL.setVisibility(View.VISIBLE);
            if (photo.isVideo()) {
                vh.mVideoLayout.setVisibility(View.VISIBLE);
                vh.mVideoDuration.setText(photo.formatVideoDuration());
            } else {
                vh.mVideoLayout.setVisibility(View.GONE);
            }

            final String path = photo.isVideo() ? photo.getThumbnail() : photo.getDataPath();
            vh.mImageView.setTag(path);
            vh.mImageView.setVisibility(View.VISIBLE);
            setImageView(path, vh.mImageView);

            setSelectBtn(vh, photo.isSelected());
        }

        if (position == mData.size() - 1) {
            vh.mFooterView.setVisibility(View.VISIBLE);
        } else {
            vh.mFooterView.setVisibility(View.GONE);
        }
        return convertView;
    }

    private void initCustomThemeStyle(ViewHolder vh){
        mMaxSelectImageCount = mTheme.getMaxSelectImageSize();
        mMaxSelectVideoCount = mTheme.getMaxSelectVideoSize();
        vh.mSelectBtn.setBackgroundResource(mTheme.getPhotoSelected());
    }

    private OnClickListener mItemClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.adapter_gridview_take_photo_layout){
                mSelectPhotoCallBackForPhotoAdapter.takePhoto();
            }

            if (!(v.getTag(R.id.adapter_gridview_photo_item_layout) instanceof ViewHolder)) {
                return;
            }
            final ViewHolder vh = (ViewHolder) v.getTag(R.id.adapter_gridview_photo_item_layout);
            int id = v.getId();
            if (id == R.id.adapter_gridview_photo_item_img) {

                if(mTheme.isPickSinglePhoto()){

                    if(mTheme.getSinglePhotoStartActivityName() != null) {
                        PhotoPicker.startSinglePhotoPickerTarget(
                                mContext,
                                mTheme.getSinglePhotoStartActivityName(),
                                mData.get(vh.position));
                        ((Activity) mContext).finish();
                        return;
                    }else{
                        Activity activity = (Activity) mContext;
                        Intent intent = new Intent();
                        intent.putExtra(BaseTheme.KEY_SINGLE_PHOTO, mData.get(vh.position));
                        activity.setResult(Activity.RESULT_OK, intent);
                        activity.finish();
                        return;
                    }
                }

                if (vh.mShadowClick.getVisibility() == View.GONE) {
                    vh.mShadowClick.setVisibility(View.VISIBLE);
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            vh.mShadowClick.setVisibility(View.GONE);
                        }
                    }, 50);
                }
                int[] locationOnScreen = new int[2];
                v.getLocationOnScreen(locationOnScreen);

                ArrayList<Photo> temp = new ArrayList<Photo>();
                int index = vh.position;
                temp.addAll(mData);
                if(mTheme.isCanTakePhoto()){
                    if(temp.get(0).isNull()){
                        temp.remove(0);
                        index--;
                    }
                }
                Log.i("photo click", "photo path : " + mData.get(vh.position).getDataPath());
                mSelectPhotoCallBackForPhotoAdapter.startGalleryFragment(temp, index, locationOnScreen, v.getWidth());
            } else if (id == R.id.adapter_gridview_photo_item_select_scope) {
                Photo photo = mData.get(vh.position);
                if (vh.mSelectBtn.getVisibility() == View.VISIBLE) {
                    // 取消选择
                    photo.setSelected(false);
                    getSelectPhotos().remove(photo);
//                    mSelectPhotos.remove(photo);
                } else {
                    // 选择图片
                    if(photo.isVideo()){
                        if (isOutSideMaxSelectVideoCount()) {
                            return;
                        }
                    }else {
                        if (isOutSideMaxSelectImageCount()) {
                            return;
                        }
                    }
                    photo.setSelected(true);
                    getSelectPhotos().add(photo);
//                    mSelectPhotos.add(photo);
                    vh.mSelectBtn.startAnimation(mSelectPhotoAnim);
                }
                setSelectBtn(vh, photo.isSelected());

                mSelectPhotoCallBackForPhotoAdapter.canClickNext(getSelectPhotos().size() != 0);
                mSelectPhotoCallBackForPhotoAdapter.updatePhotoAlbum();
//				Log.i("PhotoAdapter", "select size : " + mSelectPhotos.size());
            }

        }
    };

    private OnTouchListener mOnItemTouchListner = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (!(v.getTag(R.id.adapter_gridview_photo_item_layout) instanceof ViewHolder)) {
                return false;
            }
            final ViewHolder vh = (ViewHolder) v.getTag(R.id.adapter_gridview_photo_item_layout);

            switch (event.getAction()) {
//				MotionEvent.a
                case MotionEvent.ACTION_DOWN:
                    isPressed = true;
                    ThreadPoolUtils.getInstance().execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                synchronized (this) {
                                    this.wait(100);
                                }
                                doImageViewShadow(vh.mShadowClick);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    break;
                case MotionEvent.ACTION_UP:
                    isPressed = false;
                    vh.mShadowClick.setVisibility(View.GONE);
                    v.performClick();
                    break;
                case MotionEvent.ACTION_OUTSIDE:
                case MotionEvent.ACTION_CANCEL:
                    isPressed = false;
                    vh.mShadowClick.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
            return true;
        }
    };

    private boolean isOutSideMaxSelectImageCount() {
        if (Photo.getExistImageCount(getSelectPhotos()) >= mMaxSelectImageCount) {
            String str = String.format(
                    mContext.getString(R.string.select_photo_image_count_limit),
                    mMaxSelectImageCount);
            Toast.makeText(mContext, str, Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return false;
        }
    }

    private boolean isOutSideMaxSelectVideoCount() {
        if (Photo.getExistVideoCount(getSelectPhotos()) >= mMaxSelectVideoCount) {
            String str = String.format(
                    mContext.getString(R.string.select_photo_video_count_limit),
                    mMaxSelectVideoCount);
            Toast.makeText(mContext, str, Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return false;
        }
    }

    private void initSelectPhotoListener(final ViewHolder vh) {
        vh.mSelect.setTag(R.id.adapter_gridview_photo_item_layout, vh);
        vh.mSelect.setOnClickListener(mItemClickListener);
        vh.mTakePhotoLayout.setOnClickListener(mItemClickListener);
        vh.mImageView.setTag(R.id.adapter_gridview_photo_item_layout, vh);
        vh.mImageView.setOnClickListener(mItemClickListener);
        vh.mImageView.setOnTouchListener(mOnItemTouchListner);
    }

    private void doImageViewShadow(View shadow) {
        if (isPressed) {
            Message msg = Message.obtain();
            msg.what = MSG_SHOW_PHOTO_SHADOW;
            msg.obj = shadow;
            mHandler.sendMessage(msg);
        }
    }

    private void setSelectBtn(ViewHolder vh, boolean isSelected) {
        if(mTheme.isPickSinglePhoto()){
            vh.mUnSelectBtn.setVisibility(View.GONE);
            vh.mSelectBtn.setVisibility(View.GONE);
            vh.mShadowSelect.setVisibility(View.GONE);
            vh.mSelect.setEnabled(false);
            return;
        }

        if (isSelected) {
            vh.mUnSelectBtn.setVisibility(View.GONE);
            vh.mSelectBtn.setVisibility(View.VISIBLE);
            vh.mShadowSelect.setVisibility(View.VISIBLE);
        } else {
            vh.mSelectBtn.setVisibility(View.GONE);
            vh.mUnSelectBtn.setVisibility(View.VISIBLE);
            vh.mShadowSelect.setVisibility(View.GONE);
        }
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

    private void adjustImageSize(FrameLayout mImageViewFL) {
        LayoutParams lp = mImageViewFL.getLayoutParams();
        lp.height = mImageSize;
        mImageViewFL.setLayoutParams(lp);
    }

//    private void adjustImageSize(RelativeLayout mImageViewRL) {
//        LayoutParams lp = mImageViewRL.getLayoutParams();
//        lp.height = mImageSize;
//        mImageViewRL.setLayoutParams(lp);
//    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // 仅当GridView静止时才去下载图片，GridView滑动时取消所有正在下载的任务
        if (scrollState == SCROLL_STATE_IDLE) {
            loadBitmaps(mFirstVisibleItem, mVisibleItemCount, true);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                         int totalItemCount) {
//		if(mShadowNow != null){
//			mShadowNow.setVisibility(View.GONE);
//		}

        mFirstVisibleItem = firstVisibleItem;
        mVisibleItemCount = visibleItemCount;
        // 下载的任务应该由onScrollStateChanged里调用，但首次进入程序时onScrollStateChanged并不会调用，
        // 因此在这里为首次进入程序开启下载任务。
        if (isFirstEnter && visibleItemCount > 0) {
            loadBitmaps(firstVisibleItem, visibleItemCount, true);
            isFirstEnter = false;
        }
    }

    public void reloadBitmap(){
        loadBitmaps(mFirstVisibleItem, mVisibleItemCount, true);
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
            // mInvisibleItemCount是为了滑动的时候提前加载不可见的图片,预先设定好加载数量,为了更好的体验;
            for (int i = firstVisibleItem; i < firstVisibleItem + visibleItemCount
                    + mInvisibleItemCount; i++) {
                // 触发scroll的时候有预先加载6张图片，防止溢出, 不能等于0是因为第一张图片是拍照;
                if (i >= this.mData.size()) {
                    return;
                }
                // else if(i == 0){
                // continue;
                // }
                final String path;
                Photo photo = this.mData.get(i);
                if(photo.isNull()){
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
                            Bitmap bitmap = BitmapUtil.decodeSampledBitmapFromPath(path,
                                    mImageSize, mImageSize, mData.get(index).getOrientation());
                            if (bitmap == null) {
                                return;
                            }

                            mBitmapUtil.addBitmapToMemoryCache(getCacheKey(path), bitmap);
                            // 如果此时加载的是可见的图片，才通知UI线程加载图片
                            if (index < mFirstInvisibleItemCount && isShowImage) {
                                Message msg = Message.obtain();
                                msg.what = MSG_SHOW_PHOTO;
                                msg.obj = path;
                                mHandler.sendMessage(msg);
                            }

                        }
                    });

                } else {
                    ImageView imageView = (ImageView) mGridView.findViewWithTag(path);
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
                case MSG_SHOW_PHOTO:
                    String imageUrl = (String) msg.obj;
                    // 根据Tag找到相应的ImageView控件，将下载好的图片显示出来。
                    ImageView imageView = (ImageView) mGridView.findViewWithTag(imageUrl);
                    setImageView(imageUrl, imageView);
                    break;
                case MSG_SHOW_PHOTO_SHADOW:
                    View shadow = (View) msg.obj;
                    shadow.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;

            }
        }
    };

    public void refresh(ArrayList<Photo> data) {

        this.mBitmapUtil.clearCache();

        if (data == null) {
            this.mData.clear();
            addTakePhotoIfNeed();
        } else {
            this.mData = data;
            addTakePhotoIfNeed();
            // 预读15张图片
            loadBitmaps(0, 15, true);
        }

        notifyDataSetChanged();
    }

    public ArrayList<Photo> getSelectPhotos() {
        return ((ThemeActivity)mContext).getSelectPhotos();
    }

    public String getCacheKey(String key){
        return "photo:" + key;
    }

    static class ViewHolder {
        FrameLayout mImageViewFL;
        RelativeLayout mImageViewRL;
        ImageView mImageView;
        TextView mVideoDuration;//视频时长
        View mTakePhotoLayout;
        View mVideoLayout;//区分是照片还是视频
        View mSelect;// 选择层，用来判断是否属于选择相片
        View mUnSelectBtn;
        View mSelectBtn;
        View mShadowSelect;
        View mShadowClick;
        ViewStub mFooterView;

        int position;//记录当前ViewHolder属于哪个位置
    }
}
