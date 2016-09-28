package com.hengye.photopicker.fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hengye.photopicker.R;
import com.hengye.photopicker.activity.PhotoPickerActivity;
import com.hengye.photopicker.activity.ThemeActivity;
import com.hengye.photopicker.adapter.PhotoAdapter;
import com.hengye.photopicker.adapter.PhotoAlbumAdapter;
import com.hengye.photopicker.model.Photo;
import com.hengye.photopicker.model.PhotoAlbum;
import com.hengye.photopicker.util.PhotosUtil;
import com.hengye.photopicker.util.ThreadPoolUtils;
import com.hengye.photopicker.util.theme.BaseTheme;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PhotoPickerFragment extends ThemeFragment implements View.OnClickListener{

//    public final static  String PICK_PHOTO_DATA = "pickPhotoData";
//    public final static String MAX_PICK_PHOTO_COUNT = "maxPickPhotoCount";
    private final static int MSG_LOAD_IMAGES = 1;
    private final static int MSG_LOAD_PHOTO_ALBUMS = 2;
//    private final static int DEFAULT_MAX_PICK_IMAGE_COUNT = 9;//默认最多只能选择9张图片
    //--视图层
    private GridView mPhotosGridView;
    private RelativeLayout mSelectAlbumLayout, mTitleBar;
    private LinearLayout mPrimitiveLayout;
    private ListView mPhotoAlbumsListView;
    private View mShadow, mPrimitiveBtn;
    private TextView mCancel, mSelectAlbumNow, mPrimitiveText;
    private Button mPreviewBtn, mNextBtn;//下一步按钮
    private View mPhotoAlbumNowView;
    //--视图层

    private PhotosUtil mPhotosUtil;
    private List<PhotoAlbum> mPhotoAlbumList;
    private int mPhotoAlbumNowIndex;
    private PhotoAlbum mPhotoAlbumNow;
    private PhotoAdapter mPhotoAdapter;
    private PhotoAlbumAdapter mPhotoAlbumAdapter;
    private boolean isSelectAlbum = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_select_photo, container, false);
        initView(rootView);
        initDifferentVersions();
        initData();
        initClick();
        initCustomThemeStyle();
        return rootView;
    }

    private void initView(View rootView){
        mTitleBar = (RelativeLayout) rootView.findViewById(R.id.titlebar_photo_pick);
        mCancel = (TextView) rootView.findViewById(R.id.title_left_bt);
        mPhotosGridView = (GridView) rootView.findViewById(R.id.activity_photo_gridview);
        mSelectAlbumLayout = (RelativeLayout) rootView.findViewById(R.id.activity_select_photo_album_layout_click);
        mSelectAlbumNow = (TextView) rootView.findViewById(R.id.activity_select_photo_album);
        mPhotoAlbumsListView = (ListView) rootView.findViewById(R.id.popupwindow_select_photo_album_listview);
        mShadow = rootView.findViewById(R.id.activity_select_photo_shadow_layout);
        mPreviewBtn = (Button) rootView.findViewById(R.id.activity_select_photo_layout_preview_btn);
        mNextBtn = (Button) rootView.findViewById(R.id.activity_select_photo_layout_next_btn);
        mPrimitiveLayout = (LinearLayout) rootView.findViewById(R.id.activity_select_photo_layout_primitive_layout);
        mPrimitiveBtn = rootView.findViewById(R.id.activity_select_photo_layout_primitive);
        mPrimitiveText = (TextView) rootView.findViewById(R.id.activity_select_photo_layout_primitive_tv);

        if(getCustomTheme().isPickSinglePhoto()){
            rootView.findViewById(R.id.activity_select_photo_layout).setVisibility(View.GONE);
        }
    }

    private void initCustomThemeStyle(){
        mTitleBar.setBackgroundColor(getResources().getColor(getCustomTheme().getTitleBackground()));
        mCancel.setTextColor(getResources().getColor(getCustomTheme().getTitleTextFontColor()));
        mSelectAlbumNow.setTextColor(getResources().getColor(getCustomTheme().getTitleTextFontColor()));
        mSelectAlbumNow.setCompoundDrawablesWithIntrinsicBounds(0, 0, getCustomTheme().getTitleNavigationUnSelectedIcon(), 0);
        mPhotoAlbumsListView.setSelector(getCustomTheme().getAlbumListViewSelector());
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private void initDifferentVersions(){
        mPhotosGridView.setOverScrollMode(View.OVER_SCROLL_IF_CONTENT_SCROLLS);
    }

    private void initData(){
        canClickNext(false);
        updatePrimitive(false);
        mPhotoAdapter = new PhotoAdapter(getActivity(), new ArrayList<Photo>(), mPhotosGridView, new SelectPhotoCallBackForPhotoAdapter());
        mPhotosGridView.setAdapter(mPhotoAdapter);
        mPhotosGridView.setOnScrollListener(mPhotoAdapter);

        mPhotoAlbumAdapter = new PhotoAlbumAdapter(getActivity(), new ArrayList<PhotoAlbum>(), mPhotoAlbumsListView, new SelectPhotoCallBackForPhotoAlbumAdapter());
        mPhotoAlbumsListView.setAdapter(mPhotoAlbumAdapter);
        mPhotoAlbumsListView.setOnScrollListener(mPhotoAlbumAdapter);

        loadData();
    }

    private int getMaxPhotoPickCount(){
//        return getActivity().getIntent().getIntExtra(MAX_PICK_PHOTO_COUNT, DEFAULT_MAX_PICK_PHOTO_COUNT);
        return getCustomTheme().getMaxSelectImageSize();
    }

    private void loadData(){
        ThreadPoolUtils.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                mPhotosUtil = PhotosUtil.getInstance(getActivity().getApplicationContext().getContentResolver());
                int type = getCustomTheme().getContentType();
                List<PhotoAlbum> temp;
                if(type == BaseTheme.TYPE_IMAGE){
                    temp = mPhotosUtil.getImageAlbums();
                }else if(type == BaseTheme.TYPE_VIDEO){
                    temp = mPhotosUtil.getVideoAlbums();
                }else{
                    temp = mPhotosUtil.getPhotoAlbums();
                }
                setData(temp);
            }
        });
    }

    public void setData(List<PhotoAlbum> pas){
        mPhotoAlbumList = pas;
        int type = getCustomTheme().getContentType();
        if (mPhotoAlbumList != null && mPhotoAlbumList.size() > 0) {
            //得到包括了所有图片的一个相册
            mPhotoAlbumNowIndex = 0;
            //设置第一个相册的名字为所有图片

            if(type == BaseTheme.TYPE_IMAGE){
                mPhotoAlbumList.get(0).setBucketDisplayName(getResources().getString(R.string.select_photo_all_image));
            }else if(type == BaseTheme.TYPE_VIDEO){
                mPhotoAlbumList.get(0).setBucketDisplayName(getResources().getString(R.string.select_photo_all_video));
            }else{
                mPhotoAlbumList.get(0).setBucketDisplayName(getResources().getString(R.string.select_photo_all_photo));
                if(PhotosUtil.ALL_VIDEO_ALBUM.equals(mPhotoAlbumList.get(1).getBucketId())){
                    mPhotoAlbumList.get(1).setBucketDisplayName(getResources().getString(R.string.select_photo_all_video));
                }
            }

            updateSelectPhotosIfNeed();
            mPhotoAlbumNow = mPhotoAlbumList.get(0);
            //更新当前显示的相册图片
            Message msg1 = Message.obtain();
            msg1.what = MSG_LOAD_IMAGES;
            mHandler.sendMessage(msg1);
            //更新相册列表
            Message msg2 = Message.obtain();
            msg2.what = MSG_LOAD_PHOTO_ALBUMS;
            mHandler.sendMessage(msg2);
        }
    }

    private void updateSelectPhotosIfNeed(){
        ArrayList<Photo> photos = (ArrayList<Photo>)getActivity().getIntent().getSerializableExtra(PhotoPickerActivity.PICK_PHOTO_DATA);

        if(photos != null && photos.size() != 0){
            List<Photo> totalPhotos = mPhotoAlbumList.get(0).getImages();
            if(totalPhotos != null && totalPhotos.size() != 0){
                for(Photo photo : photos){
                    int index = totalPhotos.indexOf(photo);
                    if(index != -1){
                        Photo target = totalPhotos.get(index);
                        target.setSelected(true);
                        getSelectPhotos().add(target);
                    }
                }

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        canClickNext(getSelectPhotos().size() != 0);
                        updatePrimitive(mPrimitiveLayout.isSelected());
                    }
                });
               
            }

        }

    }

    private void initClick(){
        mCancel.setOnClickListener(this);
        mShadow.setOnClickListener(this);
        mSelectAlbumLayout.setOnClickListener(this);
        mPreviewBtn.setOnClickListener(this);
        mNextBtn.setOnClickListener(this);
        mPrimitiveLayout.setOnClickListener(this);
        mPhotoAlbumsListView.setOnItemClickListener(mListViewItemClick);
    }

    @Override
    public void onClick(View v){
        int id = v.getId();
        if(id == R.id.title_left_bt){
            //取消按钮
            onClickCancel();
        }else if(id == R.id.activity_select_photo_shadow_layout){
            //阴影层按钮
            mSelectAlbumLayout.performClick();
        }else if(id == R.id.activity_select_photo_album_layout_click){
            //相册层按钮
            onClickAlbum();
        }else if(id == R.id.activity_select_photo_layout_preview_btn){
            //预览按钮
            ArrayList<Photo> temp = new ArrayList<Photo>();
            temp.addAll(mPhotoAdapter.getSelectPhotos());
            startGalleryFragment(temp, 0);
//            startGalleryActivity(mPhotoAdapter.getSelectPhotos(), 0);
        }else if(id == R.id.activity_select_photo_layout_next_btn){
            //下一步按钮
            finishAtyWithData();
        }else if(id == R.id.activity_select_photo_layout_primitive_layout){
            //原图按钮
            onClickPrimitiveBtn(v);
        }
    }

    private void onClickCancel() {
        if(mPhotoAlbumsListView.getVisibility() == View.VISIBLE){
            mSelectAlbumLayout.performClick();
        }else {
            finishAty();
        }
    }

    private void onClickPrimitiveBtn(View v) {
        if (v.isSelected()){
            v.setSelected(false);
            updatePrimitive(false);
        }else{
            v.setSelected(true);
            updatePrimitive(true);
        }
    }

    private void finishAtyWithData() {
        ((ThemeActivity)getActivity()).finishAtyWithData(mPhotoAdapter.getSelectPhotos(), mPrimitiveLayout.isSelected());
    }

    private void finishAty() {
        getActivity().finish();
    }

    //展开收缩相册
    private void onClickAlbum() {
        if(mPhotoAlbumsListView.isShown()){//收缩
//            mSelectAlbumNow.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.navigationbar_arrow_down, 0);
            mSelectAlbumNow.setCompoundDrawablesWithIntrinsicBounds(0, 0, getCustomTheme().getTitleNavigationUnSelectedIcon(), 0);
            mPhotoAlbumsListView.setAnimation(getHideAnimation());
            mPhotoAlbumsListView.setVisibility(View.GONE);
            mShadow.setVisibility(View.GONE);
        }
        else{//展开
//            mSelectAlbumNow.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.navigationbar_arrow_up, 0);
            mSelectAlbumNow.setCompoundDrawablesWithIntrinsicBounds(0, 0, getCustomTheme().getTitleNavigationSelectedIcon(), 0);
            mPhotoAlbumsListView.setAnimation(getShowAnimation());
            mPhotoAlbumsListView.setVisibility(View.VISIBLE);
            mPhotoAlbumsListView.setFocusable(true);
            mPhotoAlbumAdapter.preLoadbBitmaps();
        }
    }

    private Animation.AnimationListener mClickAnimationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
            mSelectAlbumLayout.setClickable(false);
        }
        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if (mPhotoAlbumsListView.isShown()) {
                mShadow.setVisibility(View.VISIBLE);
            }
            mSelectAlbumLayout.setClickable(true);
            if(isSelectAlbum){
                isSelectAlbum = false;
                Message msg = Message.obtain();
                msg.what = MSG_LOAD_IMAGES;
                mHandler.sendMessage(msg);
            }
        }
    };

    private AdapterView.OnItemClickListener mListViewItemClick = new AdapterView.OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id){
            //如果点击的是同一个相册
            if(mPhotoAlbumNowIndex == position){
                isSelectAlbum = false;
            }else{
                mPhotoAlbumNowIndex = position;
                mPhotoAlbumNow = mPhotoAlbumAdapter.getItem(position);
                isSelectAlbum = true;

                if(mPhotoAlbumNowView != null){
                    //改变相册列表上一次选中相册的样式
                    mPhotoAlbumNowView.setBackgroundColor(getResources().getColor(R.color.select_photo_album_listview_layout));
                }else{
                    //改变相册列表里第一行数据的样式(默认是第一行数据被选中)
                    parent.getChildAt(0).findViewById(R.id.adapter_listview_photo_album_item_layout)
                            .setBackgroundColor(getResources().getColor(R.color.select_photo_album_listview_layout));
                }
                mPhotoAlbumNowView = view.findViewById(R.id.adapter_listview_photo_album_item_layout);
                mPhotoAlbumNowView.setBackgroundColor(getResources().getColor(getCustomTheme().getAlbumListItemSelected()));
//                mPhotoAlbumNowView.setBackgroundColor(getResources().getColor(R.color.select_photo_listview_photo_album_item_select_2));
            }
            mSelectAlbumLayout.performClick();
        }
    };

    //	private Animation mShowAnimation, mHideAnimation;
    private final long DEFAULT_ALBUM_ANIMATION_DURATION = 200;

    private Animation getShowAnimation(){
        Animation showAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
//		mShowAnimation.setInterpolator(new OvershootInterpolator());//向前甩一定值后再回到原来位置
        showAnimation.setDuration(DEFAULT_ALBUM_ANIMATION_DURATION);
        showAnimation.setAnimationListener(mClickAnimationListener);
        return showAnimation;
    }

    private Animation getHideAnimation(){
        Animation hideAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -1.0f);
//		mHideAnimation.setInterpolator(new AnticipateInterpolator());//开始的时候向后然后向前甩
        hideAnimation.setDuration(DEFAULT_ALBUM_ANIMATION_DURATION);
        hideAnimation.setAnimationListener(mClickAnimationListener);
        return hideAnimation;
    }

    //切换预览和下一步的按钮样式
    private void canClickNext(boolean flag){
        if(flag){
            if(mPrimitiveLayout.isSelected()){
                updatePrimitive(true);
            }

            mNextBtn.setText(String.format(getString(R.string.select_photo_next), mPhotoAdapter.getSelectPhotos().size()));
            //如果已经可以点击下一步的时候，以下样式无需重复再改直接返回;
            if(mNextBtn.isEnabled()){
                return;
            }
            mPreviewBtn.setEnabled(true);
            mPreviewBtn.setTextColor(getResources().getColor(R.color.select_photo_text_able_bg_white));
            mNextBtn.setEnabled(true);
//            mNextBtn.setBackgroundResource(R.drawable.btn_bg_orange);
            mNextBtn.setBackgroundResource(getCustomTheme().getNextBtn());
            mNextBtn.setTextColor(getResources().getColor(R.color.select_photo_text_able_bg_orange));
            mPrimitiveLayout.setEnabled(true);
            mPrimitiveText.setTextColor(getResources().getColor(R.color.select_photo_text_able_bg_white));

        }else{
            mPreviewBtn.setEnabled(false);
            mPreviewBtn.setTextColor(getResources().getColor(R.color.select_photo_text_unable_bg_white));
            mNextBtn.setEnabled(false);
            mNextBtn.setBackgroundResource(R.drawable.btn_bg_white);
            mNextBtn.setText(getString(R.string.select_photo_next_null));
            mNextBtn.setTextColor(getResources().getColor(R.color.select_photo_text_unable_bg_white));

            mPrimitiveLayout.setEnabled(false);
            mPrimitiveText.setTextColor(getResources().getColor(R.color.select_photo_text_unable_bg_white));
            updatePrimitive(false);
        }
    }
    //切换显示原图的按钮样式
    private void updatePrimitive(boolean flag){
        if(flag){
//            mPrimitiveBtn.setBackgroundResource(R.drawable.compose_photo_preview_right);
            mPrimitiveBtn.setBackgroundResource(getCustomTheme().getAlbumPointSelected());
            mPrimitiveText.setText( String.format(getString(R.string.select_photo_primitive), getSelectPhotoSize()));
        }else{
            mPrimitiveBtn.setBackgroundResource(R.drawable.compose_photo_preview_default);
            mPrimitiveText.setText(getString(R.string.select_photo_primitive_null));
        }
    }

    @SuppressLint("HandlerLeak")
    public Handler mHandler = new Handler()
    {
        @SuppressLint("NewApi")
        public void handleMessage(Message msg)
        {
            switch (msg.what) {
                case MSG_LOAD_IMAGES:
                    //检查图片路径是否合法
//                    checkPhotosIsExist(mPhotoAlbumNow.getImages());
                    //加载本相册的图片，并且更新相册标题名称
                    mPhotoAdapter.refresh(mPhotoAlbumNow.getImages());

                    mPhotosGridView.setAdapter(mPhotoAdapter);//让 gridview初始位置变成0，用其他方法不完善
                    mSelectAlbumNow.setText(mPhotoAlbumNow.getBucketDisplayName());
                    break;
                case MSG_LOAD_PHOTO_ALBUMS:
                    mPhotoAlbumAdapter.refresh(mPhotoAlbumList);
                    setMaxHeightPhotoAlbumListView();

                    break;
                default:
                    break;
            }
        }
    };

    private void setMaxHeightPhotoAlbumListView() {
        //限制相册列表的最大高度为6行;
        if(mPhotoAlbumList.size() > 6){
            ViewGroup.LayoutParams lp = mPhotoAlbumsListView.getLayoutParams();
            int margin1Dp = getResources().getDimensionPixelSize(R.dimen.margin_1_dp);
            lp.height = margin1Dp * 60 * 6;
            mPhotoAlbumsListView.setLayoutParams(lp);
        }
    }

    private String getSelectPhotoSize(){
        return Photo.formatTotalImageSize(getActivity().getApplicationContext(), mPhotoAdapter.getSelectPhotos());
    }

    private void checkPhotosIsExist(List<Photo> images){
        for(int i = 0; i < images.size(); i++){
            if(!checkPhotoIsExist(images.get(i).getDataPath())){
                images.remove(i);
            }
        }
    }

    private boolean checkPhotoIsExist(String path){
        if(path == null){
            return false;
        }
        return new File(path).exists();
    }

    private void startGalleryFragment(ArrayList<Photo> photos, int index){
        startGalleryFragment(photos, index, null, 0);
    }

    private void startGalleryFragment(ArrayList<Photo> photos, int index, int[] locationOnScreen, int width){
        startGalleryFragment(photos, index, locationOnScreen, width, false);
    }
    private void startGalleryFragment(ArrayList<Photo> photos, int index, int[] locationOnScreen, int width, boolean isTakePhoto){
        GalleryFragment fragment = GalleryFragment.newInstance(photos, index, locationOnScreen, width, isTakePhoto, mPrimitiveLayout.isSelected());
        ((PhotoPickerActivity)getActivity()).addGalleryFragment(fragment);
    }

//    private void startGalleryActivity(ArrayList<Photo> photos, int index){
//        startGalleryActivity(photos, index, null, 0);
//    }
//
//    private void startGalleryActivity(ArrayList<Photo> photos, int index, int[] locationOnScreen, int width){
////        GalleryFragment galleryFragment = new GalleryFragment();
//        Bundle bundle = new Bundle();
//        bundle.putSerializable(GalleryFragment.IMG_TOTAL_PHOTO, photos);
//        bundle.putInt(GalleryFragment.IMG_INDEX, index);
//        bundle.putInt(GalleryFragment.IMG_WIDTH, width);
//        bundle.putIntArray(GalleryFragment.IMG_LOCATION, locationOnScreen);
////        galleryFragment.setArguments(bundle);
//        Intent intent = new Intent(getActivity(), GalleryActivity.class);
//        intent.putExtras(bundle);
//        getActivity().startActivity(intent);
////        ((PhotoPickerActivity)getActivity()).addGalleryFragment(galleryFragment);
//    }

    public class SelectPhotoCallBackForPhotoAdapter{
        public void canClickNext(boolean flag){
            PhotoPickerFragment.this.canClickNext(flag);
        }

        public void takePhoto(){
            dispatchTakePictureIntent();
        }

        public void startGalleryFragment(ArrayList<Photo> photos, int index, int[] locationOnScreen, int width){
            //如果是点击图片预览的时候则判断图片是否存在，点击预览按钮则不做判断
            if(checkPhotoIsExist(photos.get(index).getDataPath())){
                PhotoPickerFragment.this.startGalleryFragment(photos, index, locationOnScreen, width);
//                PhotoPickerFragment.this.startGalleryActivity(photos, index, locationOnScreen, width);
            }else{
                Toast.makeText(getActivity().getApplicationContext(), getString(R.string.select_photo_inexistent),
                        Toast.LENGTH_SHORT).show();
            }
        }

        public void updatePhotoAlbum(){
            mPhotoAlbumAdapter.notifyDataSetChanged();
        }

        public BaseTheme getCustomTheme(){
            return PhotoPickerFragment.this.getCustomTheme();
        }
    }

    public class SelectPhotoCallBackForPhotoAlbumAdapter{
        public int getPhotoAlbumIndexNow(){
            return mPhotoAlbumNowIndex;
        }
        public List<Photo> getSelectPhotos(){
            return mPhotoAdapter.getSelectPhotos();
        }
        public BaseTheme getCustomTheme(){
            return PhotoPickerFragment.this.getCustomTheme();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mPhotoAdapter.notifyDataSetChanged();
        mPhotoAlbumAdapter.notifyDataSetChanged();
        canClickNext(getSelectPhotos().size() != 0);
        boolean isPrimitive = getActivity().getIntent().getBooleanExtra(PhotoPickerActivity.PICK_PHOTO_PRIMITIVE, false);
        mPrimitiveLayout.setSelected(isPrimitive);
        updatePrimitive(isPrimitive);
        mPhotoAdapter.reloadBitmap();
        Log.i("photo picker", "onresume()");
    }

//    String mCurrentPhotoPath;
    File mCurrentPhotoFile;

    static final int REQUEST_TAKE_PHOTO = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = mCurrentPhotoFile = PhotosUtil.createImageFile();
//                Log.i("photopicker", "photo file : " + mCurrentPhotoFile.getAbsolutePath());
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(mCurrentPhotoFile);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Log.i("photopicker", "requestCode : " + requestCode + " , resultCode : " + resultCode);
        if(requestCode == REQUEST_TAKE_PHOTO){
            if(resultCode == Activity.RESULT_OK) {
                galleryAddPic();
                ArrayList<Photo> photos = new ArrayList<Photo>();
                Photo photo = new Photo();
                photo.setSelected(true);
                photo.setDataPath(mCurrentPhotoFile.getAbsolutePath());
                photo.setSize(mCurrentPhotoFile.length());
                photos.add(photo);
                startGalleryFragment(photos, 0, null, 0, true);
            }else{
                if(mCurrentPhotoFile != null){
                    mCurrentPhotoFile.delete();
                }
            }
        }
    }
}
