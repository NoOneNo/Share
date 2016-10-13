package com.hengye.share.ui.widget.image;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hengye.photopicker.activity.GalleryActivity;
import com.hengye.photopicker.activity.PhotoPickerActivity;
import com.hengye.photopicker.fragment.GalleryFragment;
import com.hengye.photopicker.model.Photo;
import com.hengye.photopicker.util.BitmapUtil;
import com.hengye.photopicker.util.theme.BaseTheme;
import com.hengye.photopicker.util.theme.GreenTheme;
import com.hengye.share.R;
import com.hengye.share.adapter.recyclerview.CommonAdapter;
import com.hengye.share.adapter.recyclerview.ItemViewHolder;
import com.hengye.share.module.base.ActivityHelper;
import com.hengye.share.module.base.BaseActivity;
import com.hengye.share.ui.widget.listener.OnItemClickListener;
import com.hengye.share.ui.widget.recyclerview.ItemTouchUtil;
import com.hengye.share.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuhy on 2016/9/27.
 */

public class GridGalleryEditorView extends RecyclerView implements OnItemClickListener{

    public GridGalleryEditorView(Context context) {
        this(context, null);
    }

    public GridGalleryEditorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GridGalleryEditorView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init();
    }

    private static final int DEFAULT_COLUMN_COUNT = 3;

    boolean mIsAutoHide = true;
    int mColumnCount, mItemSize;
    GridLayoutManager mGridLayoutManager;
    GridGalleryEditAdapter mAdapter;

    private void init() {

        setOverScrollMode(View.OVER_SCROLL_NEVER);

        setAdapter(mAdapter = new GridGalleryEditAdapter(getContext(), new ArrayList<String>()));
        mAdapter.setOnItemClickListener(this);
        setColumnCount(DEFAULT_COLUMN_COUNT);

        ItemTouchUtil.attachByDrag(this, mAdapter);
        updateAfterEdit();

        if (getContext() instanceof BaseActivity) {
            ((BaseActivity) getContext()).getActivityHelper().registerActivityLifecycleListener(new ActivityHelper.DefaultActivityLifecycleListener() {
                @Override
                public void onActivityResulted(Activity activity, int requestCode, int resultCode, Intent data) {
                    handleResult(requestCode, resultCode, data);
                }
            });
        }
    }

    private GridLayoutManager getGridLayoutManager() {
        return getGridLayoutManager(DEFAULT_COLUMN_COUNT);
    }

    private GridLayoutManager getGridLayoutManager(int spanCount) {
        if (mGridLayoutManager == null) {
            mGridLayoutManager = new GridLayoutManager(getContext(), spanCount);
            setLayoutManager(mGridLayoutManager);
        }
        return mGridLayoutManager;
    }

    @Override
    public void onItemClick(View view, int position) {
        int id = view.getId();
        if(id == R.id.adapter_gridview_photo_item_img){
            previewPhoto(position);
        }else if(id == R.id.adapter_gridview_photo_item_select_delete){
            deletePhoto(position);
        }
    }

    public int getColumnCount() {
        return mColumnCount;
    }

    public void setColumnCount(int columnCount) {
        if (columnCount < 1) {
            return;
        }

        mColumnCount = columnCount;
        getGridLayoutManager().setSpanCount(mColumnCount);
        updateItemSize();
    }

    public void setPaths(ArrayList<String> paths) {
        mAdapter.refresh(paths);
        updateAfterEdit();
    }

    private void updateAfterEdit(){
        hideIfNeed();
        updateAddBtn();
    }

    private void hideIfNeed(){
        if(!isAutoHide()){
            return;
        }
        if (mAdapter.isEmpty()) {
            setVisibility(View.GONE);
        } else {
            setVisibility(View.VISIBLE);
        }
    }

    public boolean isAutoHide() {
        return mIsAutoHide;
    }

    public void setAutoHide(boolean autoHide) {
        mIsAutoHide = autoHide;
    }

    public void updateAddBtn(){
        mAdapter.setShowAddBtn(mAdapter.getBasicItemCount() < 9);
    }

    public List<String> getPaths(){
        return mAdapter.getData();
    }

    public void startPhotoPicker() {
        performAddPhotoClick();
    }

    public void previewPhoto(){
        previewPhoto(0);
    }

    public void previewPhoto(int startPosition){
        Intent intent = new Intent(getContext(), GalleryActivity.class);
        intent.putExtra(GalleryFragment.IMG_TOTAL_PHOTO, getPhotos(mAdapter.getData()));
        intent.putExtra(GalleryFragment.IMG_INDEX, startPosition);
        intent.putExtra(PhotoPickerActivity.PICK_PHOTO_PRIMITIVE, true);
        intent.putExtra(BaseTheme.KEY_CUSTOM_THEME, getTheme());
        ((Activity)getContext()).startActivityForResult(intent, 1);
//        GalleryActivity.startWithIntent(getContext(), (ArrayList<String>)mAdapter.getData(), startPosition);
    }

    public void deletePhoto(int position){
        mAdapter.removeItem(position);
        updateAfterEdit();
    }

    private void performAddPhotoClick() {
        Intent intent;
        intent = new Intent(getContext(), PhotoPickerActivity.class);
        intent.putExtra(PhotoPickerActivity.PICK_PHOTO_DATA, getPhotos(mAdapter.getData()));
        intent.putExtra(PhotoPickerActivity.PICK_PHOTO_PRIMITIVE, true);
        intent.putExtra(BaseTheme.KEY_CUSTOM_THEME, getTheme());
        ((Activity) getContext()).startActivityForResult(intent, 1);
    }

    BaseTheme mTheme;

    public BaseTheme getTheme() {
        if (mTheme == null) {
            mTheme = generateDefaultTheme();
        }
        return mTheme;
    }

    public BaseTheme generateDefaultTheme() {
        BaseTheme theme = new GreenTheme();
        theme.setIsCanTakePhoto(true);
        theme.setContentType(BaseTheme.TYPE_IMAGE);
        theme.setMaxSelectImageSize(9);
        return theme;
    }

    private ArrayList<Photo> getPhotos(List<String> paths) {
        if (paths == null || paths.isEmpty()) {
            return null;
        }
        ArrayList<Photo> photos = new ArrayList<>();
        for (String path : paths) {
            Photo photo = new Photo();
            photo.setDataPath(path);
            photo.setSelected(true);
            photos.add(photo);
        }
        return photos;
    }

    private ArrayList<String> getPaths(List<Photo> photos) {
        if (photos == null || photos.isEmpty()) {
            return null;
        }
        ArrayList<String> paths = new ArrayList<>();
        for (Photo photo : photos) {
            paths.add(photo.getDataPath());
        }
        return paths;
    }

    private boolean handleResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
//            mIsPrimitive = data.getBooleanExtra(PhotoPickerActivity.PICK_PHOTO_PRIMITIVE, false);
            ArrayList<Photo> photos = (ArrayList<Photo>) data.getSerializableExtra(PhotoPickerActivity.PICK_PHOTO_DATA);
            setPaths(getPaths(photos));
            return true;
        }
        return false;
    }

    private void updateItemSize(){
        mItemSize = ViewUtil.getScreenWidth(getContext()) / mColumnCount;
    }

    private class GridGalleryEditAdapter extends CommonAdapter<String> {

        public GridGalleryEditAdapter(Context context, List<String> data) {
            super(context, data);
        }

        @Override
        public ItemViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
            return new EditItemHolder(LayoutInflater.from(getContext()).inflate(R.layout.widget_item_pick_photo, parent, false));
        }

        boolean isShowAddBtn = false;
        View addPhotoBtn;

        private View getAddPhotoBtn(){
            if(addPhotoBtn == null){
                addPhotoBtn = inflate(R.layout.widget_item_add_photo);
                addPhotoBtn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startPhotoPicker();
                    }
                });
            }
            return addPhotoBtn;
        }


        public void setShowAddBtn(boolean showAddBtn) {

            if(isShowAddBtn != showAddBtn) {
                isShowAddBtn = showAddBtn;
                if (showAddBtn) {
                    setFooter(getAddPhotoBtn());
                } else {
                    removeFooter();
                }
            }
        }

        public class EditItemHolder extends ItemViewHolder<String> {

            ImageView mImage;
            View mDeleteBtn;

            public EditItemHolder(View v) {
                super(v);
                mImage = (ImageView) findViewById(R.id.adapter_gridview_photo_item_img);
                mDeleteBtn = findViewById(R.id.adapter_gridview_photo_item_select_delete);

                registerOnClickListener(mImage);
                registerOnClickListener(mDeleteBtn);
            }

            @Override
            public void bindData(Context context, String s, int position) {
                super.bindData(context, s, position);
                mImage.setImageBitmap(BitmapUtil.getInstance().loadBitmap(s, mItemSize));

                ViewGroup.LayoutParams lp = mDeleteBtn.getLayoutParams();
                int deleteBtnSize = mItemSize / 4;
                if(lp.width != deleteBtnSize){
                    lp.width = deleteBtnSize;
                    lp.height = deleteBtnSize;
                    mDeleteBtn.requestLayout();
                }
            }
        }

    }
}

















