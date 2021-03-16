package com.cbb.baselib.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cbb.baselib.R;
import com.cbb.baselib.util.ScreenUtil;

/**
 * Created by 坎坎.
 * Date: 2019/12/25
 * Time: 16:46
 * describe:
 */
public class TitleView extends LinearLayout {
    // 单位均为dp/sp
    private int default_margin = 15;
    private int default_right_text_size = 14;
    private int default_right_text_color = R.color.lib_detail;
    private int default_center_text_size = 18;
    private int default_center_text_color = R.color.lib_normal;
    private int default_left_img_res = R.drawable.back;
    private ImageView leftImage;
    private ImageView rightImage;
    private TextView centerText;
    private TextView rightText;
    private RelativeLayout title;
    int statusBarHeight;
    int padding;

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        statusBarHeight = (int) ScreenUtil.Companion.getStatusBarHeight(context);
        padding = ScreenUtil.Companion.dp2px(context, 10);
        setPadding(0, statusBarHeight, 0, 0);
        createTitle(context);


        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.title_view);
        // 左边图片
        boolean left_img_visible = typedArray.getBoolean(R.styleable.title_view_left_img_visible, false);
        if (left_img_visible) {
            int left_img = typedArray.getInt(R.styleable.title_view_left_img, default_left_img_res);
            addLeftImageView(context, left_img);
        }

        // 右边图片
        boolean right_img_visible = typedArray.getBoolean(R.styleable.title_view_right_img_visible, false);
        if (right_img_visible) {
            int right_img = typedArray.getInt(R.styleable.title_view_right_img, 0);
            addRightImageView(context, right_img);
        }

        // 右边文字

        String right_text = typedArray.getString(R.styleable.title_view_right_text);
        if (right_text != null) {
            int right_text_color = typedArray.getColor(R.styleable.title_view_right_text_color, 0);
            int right_text_size = typedArray.getInt(R.styleable.title_view_right_text_size, default_right_text_size);
            addRightTextView(context, right_text, right_text_color, right_text_size);
        }


        String title_text = typedArray.getString(R.styleable.title_view_title_text);
        if (title_text != null) {
            int title_text_color = typedArray.getColor(R.styleable.title_view_title_text_color, 0);
            int title_text_size = typedArray.getInt(R.styleable.title_view_title_text_size, default_center_text_size);
            addCenterTextView(context, title_text, title_text_color, title_text_size);
        }
        typedArray.recycle();
    }

    private void createTitle(final Context context) {
        title = new RelativeLayout(context);
        title.setPadding(0, padding, 0, padding);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(title, lp);
    }

    private void addLeftImageView(final Context context, int res) {
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, ScreenUtil.Companion.dp2px(context, 24));
        lp.addRule(RelativeLayout.CENTER_VERTICAL);
        leftImage = new ImageView(context);
        leftImage.setImageResource(res);
        leftImage.setPadding(ScreenUtil.Companion.dp2px(context, default_margin), 0, 10, 0);
        title.addView(leftImage, lp);
        leftImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) context).onBackPressed();
            }
        });
    }

    private void addRightImageView(Context context, int res) {
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, ScreenUtil.Companion.dp2px(context, 24));
        lp.addRule(RelativeLayout.CENTER_VERTICAL);
        rightImage = new ImageView(context);
        if (res != 0) {
            rightImage.setImageResource(res);
        }
        rightImage.setPadding(10, 0, 10, 0);
        title.addView(rightImage, lp);
    }

    private void addCenterTextView(Context context, String text, int color, int size) {
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        centerText = new TextView(context);
        centerText.setText(text);
        centerText.setTextSize(size);
        if(color == 0){
            centerText.setTextColor(context.getResources().getColor(default_center_text_color));
        }else centerText.setTextColor(color);
        title.addView(centerText, lp);
    }

    private void addRightTextView(Context context, String text, int color, int size) {
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lp.addRule(RelativeLayout.CENTER_VERTICAL);
        rightText = new TextView(context);
        rightText.setText(text);
        rightText.setTextSize(size);
        rightText.setPadding(10,10,50,10);
        if(color == 0){
            rightText.setTextColor(context.getResources().getColor(default_center_text_color));
        }else rightText.setTextColor(color);
        title.addView(rightText, lp);
    }

    public void setOnRightImageViewClickListener(OnClickListener listener) {
        if (rightImage != null) {
            rightImage.setOnClickListener(listener);
        }
    }

    public void setOnRightTextClickListener(OnClickListener listener) {
        if (rightText != null) {
            rightText.setOnClickListener(listener);
        }
    }
}
