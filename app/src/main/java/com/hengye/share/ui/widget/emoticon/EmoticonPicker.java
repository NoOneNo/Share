package com.hengye.share.ui.widget.emoticon;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hengye.share.R;
import com.hengye.share.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EmoticonPicker extends LinearLayout implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    public EmoticonPicker(Context context) {
        this(context, null);
    }

    public EmoticonPicker(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmoticonPicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);
    }

    ViewPager mViewPager;

    public void init(Context context) {

        setOrientation(LinearLayout.VERTICAL);

        View view = View.inflate(context, R.layout.widget_emoticon_picker, this);

        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);

        mViewPager.setAdapter(new EmoticonPageAdapter());

    }

    EditText mEditText;
    Activity mActivity;
    int mPickerHeight;
    final LayoutTransition mLayoutTransition = new LayoutTransition();

    public void setEditText(Activity activity, ViewGroup rootLayout, EditText editText) {
        mActivity = activity;
        mEditText = editText;
        mEditText.setFilters(new InputFilter[]{getEmoticonInputFilter()});
        rootLayout.setLayoutTransition(mLayoutTransition);
        setupAnimations(mLayoutTransition);
    }

    public void show(boolean showAnimation) {
        if (showAnimation) {
            mLayoutTransition.setDuration(200);
        } else {
            mLayoutTransition.setDuration(0);
        }
        mPickerHeight = EmoticonPickerUtil.getKeyboardHeight(mActivity);
        EmoticonPickerUtil.hideKeyBoard(this.mEditText);
        getLayoutParams().height = mPickerHeight;
        setVisibility(View.VISIBLE);
        //open EmoticonPicker, press home, press app switcher to return to write weibo interface,
        //keyboard will be opened by android system when EmoticonPicker is showing,
        // this method is used to fix this issue
        mActivity.getWindow()
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public void hide() {
        setVisibility(View.GONE);
        mActivity.getWindow()
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    private void setupAnimations(LayoutTransition transition) {
//        ObjectAnimator animIn = ObjectAnimator.ofFloat(null, "translationY",
//                EmoticonPickerUtil.getScreenHeight(mActivity), mPickerHeight).
//                setDuration(transition.getDuration(LayoutTransition.APPEARING));
//        transition.setAnimator(LayoutTransition.APPEARING, animIn);

        //进入时不显示动画，增加体验
        ObjectAnimator animIn = ObjectAnimator.ofFloat(null, "translationY",
                0, mPickerHeight).
                setDuration(transition.getDuration(LayoutTransition.APPEARING));
        transition.setAnimator(LayoutTransition.APPEARING, animIn);

        ObjectAnimator animOut = ObjectAnimator.ofFloat(null, "translationY", mPickerHeight,
                EmoticonPickerUtil.getScreenHeight(mActivity)).
                setDuration(transition.getDuration(LayoutTransition.DISAPPEARING));
        transition.setAnimator(LayoutTransition.DISAPPEARING, animOut);
    }

    public class EmoticonPageAdapter extends PagerAdapter {
        public final static int EMOTICON_GRID_NUM_COLUMNS = 7;

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            GridView gridView = new GridView(getContext());
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            gridView.setLayoutParams(lp);
            gridView.setNumColumns(EMOTICON_GRID_NUM_COLUMNS);
            gridView.setAdapter(new EmoticonAdapter(getContext(), position, EMOTICON_GRID_NUM_COLUMNS));
            gridView.setOnItemClickListener(EmoticonPicker.this);
            gridView.setOnItemLongClickListener(EmoticonPicker.this);
            container.addView(gridView);
            return gridView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    public class EmoticonAdapter extends BaseAdapter {

        public final static int EMOTICON_WEIBO_INDEX = 0;
        public final static int EMOTICON_LXH_INDEX = 1;
        public final static int EMOTICON_EMOJI_INDEX = 2;

        int mIndex;
        int mTotalSize, mNumColumns, mIconSize;
        Context mContext;
        List<String> mKey;
        Map<String, Bitmap> mEmoticonBitmap;

        public EmoticonAdapter(Context context, int index, int numColumns) {
            switch (index) {
                case EMOTICON_WEIBO_INDEX:
                    mEmoticonBitmap = Emoticon.getInstance().getSortedEmoticonBitmap().get(Emoticon.EMOTICON_TYPE_WEIBO);
                    break;
                case EMOTICON_LXH_INDEX:
                    mEmoticonBitmap = Emoticon.getInstance().getSortedEmoticonBitmap().get(Emoticon.EMOTICON_TYPE_LXH);
                    break;
                case EMOTICON_EMOJI_INDEX:
                    mEmoticonBitmap = Emoticon.getInstance().getSortedEmoticonBitmap().get(Emoticon.EMOTICON_TYPE_EMOJI);
                    break;
                default:
                    throw new IllegalArgumentException("emoticon position is invalid");
            }

            mContext = context;
            mIndex = index;
            mNumColumns = numColumns;
            mKey = new ArrayList<>(mEmoticonBitmap.keySet());
            mTotalSize = mKey.size();
            mIconSize = mContext.getResources().getDisplayMetrics().widthPixels / mNumColumns;
        }

        @Override
        public int getCount() {
            return mTotalSize;
        }

        @Override
        public String getItem(int position) {
            return mKey.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = new ImageView(mContext);
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(mIconSize, mIconSize);
                convertView.setLayoutParams(lp);
            }
            bindData(position, convertView);
            return convertView;
        }

        public void bindData(int position, View convertView) {
            ImageView imageView = (ImageView) convertView;
            int padding = getContext().getResources().getDimensionPixelSize(R.dimen.icon_emoticon_padding);
            imageView.setPadding(padding, padding, padding, padding);
            imageView.setImageBitmap(mEmoticonBitmap.get(mKey.get(position)));
        }
    }


    /**
     * 输入文本的过滤，根据输入替换库中的表情
     */
    private InputFilter mEmoticonInputFilter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            // 是delete直接返回
            if ("".equals(source)) {
                return null;
            }
            if ("[最右]".equals(source)) {
                return "→_→";
            }

            return source;
            //已经跟其他uri一起处理图片，这里无须再重复处理
//            Bitmap bitmap = Emoticon.getInstance().getEmoticonBitmap().get(source.toString());
//            if (bitmap != null) {
//                SpannableString emotionSpanned = new SpannableString(source.toString());
//                ImageSpan imageSpan = new ImageSpan(getContext(), bitmap, ImageSpan.ALIGN_BOTTOM);
//                emotionSpanned.setSpan(imageSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                return emotionSpanned;
//            } else {
//                return source;
//            }
        }
    };

    public InputFilter getEmoticonInputFilter() {
        return mEmoticonInputFilter;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String key = (String) parent.getItemAtPosition(position);
        if (!TextUtils.isEmpty(key)) {
            getDefaultEmotionSelectedListener().onEmotionSelected(key);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        String key = (String) parent.getItemAtPosition(position);
        if (!TextUtils.isEmpty(key)) {
            getDefaultEmotionSelectedListener().onEmotionSelected(key);
            ToastUtil.showToast(key);
            return true;
        }
        return false;
    }

    private OnEmotionSelectedListener mDefaultEmotionSelectedListener = new OnEmotionSelectedListener() {
        @Override
        public void onEmotionSelected(String key) {
            EmoticonPickerUtil.addContentToEditTextEnd(mEditText, key);
        }
    };

    public OnEmotionSelectedListener getDefaultEmotionSelectedListener() {
        return mDefaultEmotionSelectedListener;
    }

    public void setDefaultEmotionSelectedListener(OnEmotionSelectedListener defaultEmotionSelectedListener) {
        this.mDefaultEmotionSelectedListener = defaultEmotionSelectedListener;
    }

    public interface OnEmotionSelectedListener {
        void onEmotionSelected(String key);
    }
}
