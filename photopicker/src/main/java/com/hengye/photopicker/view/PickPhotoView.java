package com.hengye.photopicker.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hengye.photopicker.R;
import com.hengye.photopicker.activity.GalleryActivity;
import com.hengye.photopicker.activity.PhotoPickerActivity;
import com.hengye.photopicker.fragment.GalleryFragment;
import com.hengye.photopicker.model.Photo;
import com.hengye.photopicker.util.BitmapUtil;
import com.hengye.photopicker.util.theme.BaseTheme;
import com.hengye.photopicker.util.theme.GreenTheme;

import java.util.ArrayList;

public class PickPhotoView extends GridLayout implements View.OnClickListener{

    public PickPhotoView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public PickPhotoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public PickPhotoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    BaseTheme mTheme;
    int mMargin1Dp;
    int mItemMaxCount, mItemMargin;
    int mItemSize = -1;
    boolean mIsDefaultColumnCount = false;
    boolean mIsHeightCustom = false;
    int mHeightCustomSize = 0;

    boolean mIsFirstTime = true;

    public int getHeightCustomSize() {
        return mHeightCustomSize;
    }

    public void setHeightCustomSize(int heightCustomSize) {
        this.mHeightCustomSize = heightCustomSize;
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PickPhotoView);
        mItemMaxCount = ta.getInt(R.styleable.PickPhotoView_maxSize, 9);

        int addPhotoBtnResId = ta.getResourceId(R.styleable.PickPhotoView_addPhotoBtnSrc, R.drawable.btn_add_photo);
        setAddPhotoBtnResId(addPhotoBtnResId);

        int columnCount = ta.getInt(R.styleable.PickPhotoView_columnCount, -1);

        if(columnCount == -1){
            mIsDefaultColumnCount = true;
            columnCount = getColumnCount(mItemMaxCount);
        }
        setColumnCount(columnCount);

        int height = ta.getDimensionPixelSize(R.styleable.PickPhotoView_fixedHeight, -1);

        if(height != -1){
            mIsHeightCustom = true;
            mHeightCustomSize = height;
        }

        ta.recycle();

        mMargin1Dp = context.getResources().getDimensionPixelSize(R.dimen.margin_1_dp);
        mItemMargin = mMargin1Dp * 5;

    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);

        ensureItemSize();

        updateAddPhotoBtn();
    }

    private void ensureItemSize(){
        if(mItemSize == -1) {
            if(mIsDefaultColumnCount) {
                mItemSize = getItemSize(getMeasuredWidth(), mItemMaxCount);
            }else{
                mItemSize = getMeasuredWidth() / getColumnCount();
            }
            addPhotos();
        }
    }

    private void updateAddPhotoBtn(){
        if(!hasAddPhotoBtn){
            if(getChildCount() < mItemMaxCount){
                addView(generateAddPhotoBtn());
                hasAddPhotoBtn = true;
            }

            if(mIsFirstTime){
                mIsFirstTime = false;

                if(getOnSetupListener() != null){
                    getOnSetupListener().onInit();
                }
            }
        }
    }


    public BaseTheme getTheme() {
        if(mTheme == null){
            mTheme = generateDefaultTheme();
        }
        return mTheme;
    }

    public BaseTheme generateDefaultTheme(){
        BaseTheme theme = new GreenTheme();
        theme.setIsCanTakePhoto(true);
        theme.setContentType(BaseTheme.TYPE_IMAGE);
        theme.setMaxSelectImageSize(mItemMaxCount);
        return theme;
    }

    public void setTheme(BaseTheme theme) {
        this.mTheme = theme;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.btn_add_photo){
            performAddPhotoClick();
        } else if(id == R.id.adapter_gridview_photo_item_select_delete){
            Photo photo = (Photo) v.getTag(R.id.adapter_gridview_photo_item_layout);
            View view = findViewWithTag(photo);
            if(view != null){
                removeView(view);
                if(mPhotos.size() == mItemMaxCount){
                    updateAddPhotoBtn();
                }
                deletePhoto(photo);

                if(getOnDeletePhotoListener() != null){
                    getOnDeletePhotoListener().onDeletePhoto(view, photo);
                }
            }
        } else if(id == R.id.adapter_gridview_photo_item_img){
            Photo photo = (Photo) v.getTag(R.id.adapter_gridview_photo_item_img);
            int index = mPhotos.indexOf(photo);
            if(index == -1){
                return;
            }

            Intent intent = new Intent(getContext(), GalleryActivity.class);
            intent.putExtra(GalleryFragment.IMG_TOTAL_PHOTO, mPhotos);
            intent.putExtra(GalleryFragment.IMG_INDEX, index);
            intent.putExtra(PhotoPickerActivity.PICK_PHOTO_PRIMITIVE, mIsPrimitive);
            intent.putExtra(BaseTheme.KEY_CUSTOM_THEME, getTheme());
            ((Activity)getContext()).startActivityForResult(intent, REQUEST_PICK_PHOTO);
        }
    }

    protected void deletePhoto(Photo photo){
        mPhotos.remove(photo);
    }

    public void performAddPhotoClick(){
        Intent intent;
        intent = new Intent(getContext(), PhotoPickerActivity.class);
        intent.putExtra(PhotoPickerActivity.PICK_PHOTO_DATA, mPhotos);
        intent.putExtra(PhotoPickerActivity.PICK_PHOTO_PRIMITIVE, mIsPrimitive);
        intent.putExtra(BaseTheme.KEY_CUSTOM_THEME, getTheme());
        ((Activity)getContext()).startActivityForResult(intent, getRequestCode());
    }

    private View mAddPhotoBtn;
    public View getAddPhotoButton(){
        return mAddPhotoBtn;
    }

    private boolean hasAddPhotoBtn = false;
    private View generateAddPhotoBtn(){
        mAddPhotoBtn = new View(getContext());
        mAddPhotoBtn.setId(R.id.btn_add_photo);
        mAddPhotoBtn.setBackgroundResource(getAddPhotoBtnResId());
        GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
        lp.width = mItemSize - mItemMargin;
        if(mIsHeightCustom){
            lp.height = mHeightCustomSize - mItemMargin;
        }else {
            lp.height = mItemSize - mItemMargin;
        }
        lp.rightMargin = mItemMargin;
        lp.bottomMargin = mItemMargin;
        mAddPhotoBtn.setLayoutParams(lp);
        mAddPhotoBtn.setOnClickListener(this);
        return mAddPhotoBtn;
    }

    private View generatePhotoView(Photo photo){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_pick_photo, null);
        ImageView iv = (ImageView) view.findViewById(R.id.adapter_gridview_photo_item_img);
        View videoInfo = view.findViewById(R.id.adapter_gridview_video_layout);
        TextView videoDuration = (TextView) view.findViewById(R.id.adapter_gridview_video_duration);
        ImageButton delete = (ImageButton) view.findViewById(R.id.adapter_gridview_photo_item_select_delete);

        int width = mItemSize;
        int height = 0;

        if(mIsHeightCustom){
            height = mHeightCustomSize;
        }else {
            height = mItemSize;
        }
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(width, height);

        view.setLayoutParams(lp);

        String path = photo.isVideo() ? photo.getThumbnail() : photo.getDataPath();
        if (path != null) {
            iv.setImageBitmap(BitmapUtil.getInstance().loadBitmap(path, mItemSize));
        }

        ViewGroup.LayoutParams deleteLP = delete.getLayoutParams();
        deleteLP.width = height / 4;
        deleteLP.height = height / 4;
        delete.setLayoutParams(deleteLP);

        iv.setOnClickListener(this);
        iv.setTag(R.id.adapter_gridview_photo_item_img, photo);
        delete.setOnClickListener(this);
        delete.setTag(R.id.adapter_gridview_photo_item_layout, photo);
        view.setTag(photo);
        return view;
    }

    public final static int REQUEST_PICK_PHOTO = 6;

    int mRequestCode = REQUEST_PICK_PHOTO;

    public int getRequestCode() {
        return mRequestCode;
    }

    public void setRequestCode(int requestCode) {
        this.mRequestCode = requestCode;
    }

    ArrayList<Photo> mPhotos;
    boolean mIsPrimitive;

    public boolean handleResult(int requestCode, int resultCode, Intent data){
        if(requestCode == getRequestCode() && resultCode == Activity.RESULT_OK){
            mIsPrimitive = data.getBooleanExtra(PhotoPickerActivity.PICK_PHOTO_PRIMITIVE, false);
            mPhotos = (ArrayList<Photo>)data.getSerializableExtra(PhotoPickerActivity.PICK_PHOTO_DATA);
            updatePhotos();
            return true;
        }
        return false;
    }

    public void updatePhotos(){
        removeAllViews();
        hasAddPhotoBtn = false;
        if(mPhotos == null || mPhotos.size() == 0){
            updateAddPhotoBtn();
            return;
        }
//        for(int i = mPhotos.size() - 1; i >= 0; i--){
        for(int i = 0; i < mPhotos.size(); i++){
            addView(generatePhotoView(mPhotos.get(i)));
        }
        updateAddPhotoBtn();
    }

    public ArrayList<Photo> getPhotos() {
        return mPhotos;
    }

    public int getPhotoSize(){
        if(mPhotos == null || mPhotos.isEmpty()){
            return 0;
        }
        return mPhotos.size();
    }

    public boolean isIsPrimitive() {
        return mIsPrimitive;
    }

    public static int getItemSize(int maxWidth, int count){
//        int marginLength = margin * 2;
        if(count == 1){
            return (maxWidth) / 2;
        }else if(count == 2 || count == 4){
            return (maxWidth) / 3;
        }else{
            return (maxWidth) / 3;
        }
    }

    public static int getColumnCount(int size){
        if(size == 1){
            return 1;
        }else if(size == 2 || size == 4){
            return 2;
        }else{
            return 3;
        }
    }

    /**
     * 清楚所有选中的图片, 并且清空显示图片的View
     */
    public void clearAllPhotos(){
        removeAllViews();
        hasAddPhotoBtn = false;
        updateAddPhotoBtn();
        if(mPhotos != null) {
            mPhotos.clear();
        }
    }

    ArrayList<Photo> mAddPhotos;

    public void addPhotos(){
        if(mAddPhotos != null){
            mPhotos = mAddPhotos;
            mAddPhotos = null;
            updatePhotos();
        }
    }

    public void setAddPhotos(ArrayList<Photo> addPhotos) {
        this.mAddPhotos = addPhotos;
    }

    public interface onDeletePhotoListener{
       void onDeletePhoto(View view, Photo photo);
    }

    public interface onSetupListener{
        void onInit();
    }

    private onDeletePhotoListener mOnDeletePhotoListener;
    private onSetupListener mOnSetupListener;

    public onDeletePhotoListener getOnDeletePhotoListener() {
        return mOnDeletePhotoListener;
    }

    public void setOnDeletePhotoListener(onDeletePhotoListener onDeletePhotoListener) {
        this.mOnDeletePhotoListener = onDeletePhotoListener;
    }

    public PickPhotoView.onSetupListener getOnSetupListener() {
        return mOnSetupListener;
    }

    public void setOnSetupListener(PickPhotoView.onSetupListener onSetupListener) {
        this.mOnSetupListener = onSetupListener;
    }

    private int mAddPhotoBtnResId;

    public int getAddPhotoBtnResId() {
        if(mAddPhotoBtnResId == 0){
            return R.drawable.btn_add_photo;
        }
        return mAddPhotoBtnResId;
    }

    public void setAddPhotoBtnResId(int addPhotoBtnResId) {
        this.mAddPhotoBtnResId = addPhotoBtnResId;
    }
}
