package com.rae.ui.media.controller;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.rae.ui.media.R;
import com.rae.ui.widget.RaeVideoView;

/**
 * 基础版播放器控制器
 * Created by ChenRui on 16/9/25 下午9:18.
 */
public class FullScreenMediaController extends SimpleMediaController {

    private final TextView mTitleView;

    public FullScreenMediaController(Context context, RaeVideoView parentView) {
        super(context, parentView);
        mTitleView = (TextView) findViewById(R.id.tv_media_title);
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

    @Override
    protected int getLayoutId() {
        return R.layout.rae_full_video_media_controller;
    }
}
