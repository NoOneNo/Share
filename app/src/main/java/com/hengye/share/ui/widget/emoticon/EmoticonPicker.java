package com.hengye.share.ui.widget.emoticon;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hengye.share.R;
import com.hengye.share.util.ToastUtil;
import com.hengye.share.util.ViewUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EmoticonPicker extends LinearLayout
        implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, ViewPager.OnPageChangeListener {

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
    EditText mEditText;
    LinearLayout mTabContainer;
    View[] mTabs;
    View mLastTab;
    Activity mActivity;
    int mPickerHeight;
    final LayoutTransition mLayoutTransition = new LayoutTransition();
    List<EmoticonPage> emoticonPages;

    public void init(Context context) {

        setOrientation(LinearLayout.VERTICAL);

        View view = View.inflate(context, R.layout.widget_emoticon_picker, this);

        mTabContainer = (LinearLayout) view.findViewById(R.id.tab_container);
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);

        mViewPager.setAdapter(new EmoticonPageAdapter(emoticonPages = listEmoticonPages()));

        setupIndicators();
    }

    /**
     * 用此方法初始化控件
     *
     * @param activity
     * @param rootLayout
     * @param editText
     */
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

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mViewPager.addOnPageChangeListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mViewPager.removeOnPageChangeListener(this);
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

    private List<EmoticonPage> listEmoticonPages() {
        List<EmoticonPage> emoticonPages = new ArrayList<>();
        emoticonPages.add(new EmoticonPage(EmoticonUtil.TYPE_WEIBO, R.drawable.d_hehe));
        emoticonPages.add(new EmoticonPage(EmoticonUtil.TYPE_EMOJI, R.drawable.emoji_0x1f604));
        emoticonPages.add(new EmoticonPage(EmoticonUtil.TYPE_LXH, R.drawable.lxh_oye));
        return emoticonPages;
    }

    private void setupIndicators() {

        int size = emoticonPages.size();
        if (mTabs == null || mTabs.length != size) {
            mTabs = new View[size];
        } else {
            Arrays.fill(mTabs, null);
        }

        int width = ViewUtil.dp2px(60f);
        int padding = ViewUtil.dp2px(5f);

        for (int i = 0; i < size; i++) {
            addTabIcon(emoticonPages.get(i), i, width, padding);
        }

        onPageSelected(0);

        findViewById(R.id.btn_delete).setOnTouchListener(new EmoticonUtil.RepeatListener(500, 50, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDefaultOnEmoticonBackspaceClickedListener().onEmoticonBackspaceClicked(v);
            }
        }));
//        findViewById(R.id.btn_delete).setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getDefaultOnEmoticonBackspaceClickedListener().onEmoticonBackspaceClicked(v);
//            }
//        });
    }

    private void addTabIcon(EmoticonPage page, int index, int width, int padding) {
        ImageButton icon = new ImageButton(getContext());

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.MATCH_PARENT);
        icon.setPadding(padding, padding, padding, padding);
        icon.setBackgroundResource(R.drawable.selector_ripple_default);
        icon.setScaleType(ImageView.ScaleType.FIT_CENTER);
        icon.setImageResource(page.getIconResId());
        mTabContainer.addView(icon, mTabContainer.getChildCount() - 2, params);
        mTabs[index] = icon;
        final int indexToMove = index;
        icon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(indexToMove, true);
            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (mTabs == null || position >= mTabs.length) {
            return;
        }
        if (mLastTab != null) {
            mLastTab.setSelected(false);
        }
        mLastTab = mTabs[position];
        mLastTab.setSelected(true);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class EmoticonPageAdapter extends PagerAdapter {
        public final static int EMOTICON_GRID_NUM_COLUMNS = 7;

        List<EmoticonPage> emoticonPages;

        public EmoticonPageAdapter(List<EmoticonPage> emoticonPages) {
            this.emoticonPages = emoticonPages;
        }

        @Override
        public int getCount() {
            return emoticonPages.size();
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
            EmoticonPage emoticonPage = emoticonPages.get(position);
            gridView.setAdapter(new EmoticonAdapter(getContext(), emoticonPage.getType(), EMOTICON_GRID_NUM_COLUMNS));
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

        int mNumColumns, mIconSize;
        Context mContext;
        List<Emoticon> mEmoticons;

        public EmoticonAdapter(Context context, @EmoticonUtil.Type String type, int numColumns) {
            mContext = context;
            mNumColumns = numColumns;
            mIconSize = mContext.getResources().getDisplayMetrics().widthPixels / mNumColumns;
            mEmoticons = EmoticonUtil.getEmojicons(type);
        }

        @Override
        public int getCount() {
            return mEmoticons.size();
        }

        @Override
        public Emoticon getItem(int position) {
            return mEmoticons.get(position);
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
            Emoticon emoticon = getItem(position);
            imageView.setImageResource(emoticon.getResId());
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
        }
    };

    public InputFilter getEmoticonInputFilter() {
        return mEmoticonInputFilter;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Emoticon emoticon = (Emoticon) parent.getItemAtPosition(position);
        String key = emoticon.getKey();
        if (!TextUtils.isEmpty(key)) {
            getDefaultEmotionSelectedListener().onEmoticonSelected(key);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Emoticon emoticon = (Emoticon) parent.getItemAtPosition(position);
        String key = emoticon.getKey();
        if (!TextUtils.isEmpty(key)) {
            getDefaultEmotionSelectedListener().onEmoticonSelected(key);
            ToastUtil.showToast(key);
            return true;
        }
        return false;
    }

    private OnEmoticonSelectedListener mDefaultEmotionSelectedListener = new OnEmoticonSelectedListener() {
        @Override
        public void onEmoticonSelected(String key) {
            EmoticonPickerUtil.addContentToEditTextEnd(mEditText, key);
        }
    };

    private OnEmoticonBackspaceClickedListener mOnEmoticonBackspaceClickedListener = new OnEmoticonBackspaceClickedListener() {
        @Override
        public void onEmoticonBackspaceClicked(View v) {
            if (mEditText != null) {
                EmoticonPickerUtil.backspace(mEditText);
            }
        }
    };

    public OnEmoticonSelectedListener getDefaultEmotionSelectedListener() {
        return mDefaultEmotionSelectedListener;
    }

    public void setDefaultEmotionSelectedListener(OnEmoticonSelectedListener defaultEmotionSelectedListener) {
        this.mDefaultEmotionSelectedListener = defaultEmotionSelectedListener;
    }


    public OnEmoticonBackspaceClickedListener getDefaultOnEmoticonBackspaceClickedListener() {
        return mOnEmoticonBackspaceClickedListener;
    }

    public void setDefaultOnEmoticonBackspaceClickedListener(OnEmoticonBackspaceClickedListener defaultOnEmoticonBackspaceClickedListener) {
        this.mOnEmoticonBackspaceClickedListener = defaultOnEmoticonBackspaceClickedListener;
    }

    public interface OnEmoticonSelectedListener {
        void onEmoticonSelected(String key);
    }

    public interface OnEmoticonBackspaceClickedListener {
        void onEmoticonBackspaceClicked(View v);
    }

}
