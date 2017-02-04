package com.rae.ui.media.controller;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rae.ui.media.R;

/**
 * 基础版播放器控制器
 * Created by ChenRui on 16/9/25 下午9:18.
 */
public class FullScreenMediaController extends SimpleMediaController {

    private final TextView mTitleView;
    private ImageView mBackView;

    public FullScreenMediaController(Context context) {
        super(context);
        mTitleView = (TextView) findViewById(R.id.tv_media_title);
        mBackView = getBackView();

        if (mBackView != null && getContext() instanceof Activity) {
            setOnBackClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Activity at = (Activity) getContext();
                    at.finish();
                }
            });
        }

    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(String title) {

        if (TextUtils.isEmpty(title)) {
            mTitleView.setVisibility(GONE);
        } else {
            mTitleView.setText(title);
        }
    }


    public void setOnBackClickListener(OnClickListener listener) {
        if (mBackView != null)
            mBackView.setOnClickListener(listener);
    }


    public ImageView getBackView() {
        if (mBackView == null)
            mBackView = (ImageView) findViewById(R.id.img_media_back);
        return mBackView;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.rae_full_video_media_controller;
    }
}
