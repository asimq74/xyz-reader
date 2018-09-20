package com.example.xyzreader.ui;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xyzreader.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HeaderView extends LinearLayout {

    @BindView(R.id.header_view_title)
    TextView title;

    @BindView(R.id.header_view_author)
    TextView subTitle;

    @BindView(R.id.header_view_published_date)
    TextView publishedDate;

    public HeaderView(Context context) {
        super(context);
    }

    public HeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    public void bindTo(String title) {
        bindTo(title, "");
    }

    public void bindTo(String title, String subTitle) {
        hideOrSetText(this.title, title);
        hideOrSetText(this.subTitle, subTitle);
    }

    public void bindTo(String title, String subTitle, String publishedDate) {
        bindTo(title, subTitle);
        hideOrSetText(this.publishedDate, publishedDate);
    }

    private void hideOrSetText(TextView tv, String text) {
        if (text == null || text.equals(""))
            tv.setVisibility(GONE);
        else
            tv.setText(text);
    }


}
