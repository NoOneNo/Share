package com.hengye.share.ui.fragment.test;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.hengye.share.R;
import com.hengye.share.ui.base.BaseFragment;

public class TestElasticScrollViewFragment extends BaseFragment {

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_test_elastic_scrollview;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView textView = (TextView) findViewById(R.id.text);


        String s = "";
        for (int i = 0; i < 40; i++) {
            s += i + "\n";
        }

        textView.setText(s);

        // 设置TextView的背景颜色，更容易观察出弹性回弹效果。
        textView.setBackgroundColor(Color.RED);
    }
}
