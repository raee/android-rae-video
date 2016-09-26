package com.rae.ui.media.controller;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.rae.ui.R;

/**
 * Created by ChenRui on 16/9/25 下午9:18.
 */

public class SimpleMediaController extends RaeMediaController implements IMediaController, View.OnClickListener {


    private ViewGroup mParentView;


    public SimpleMediaController(Context context, ViewGroup parentView) {
        super(context);
        if (parentView == null) {
            throw new NullPointerException("视频控制器父容器不能为空！");
        }

        // 添加到父容器中
        mParentView = parentView;
        parentView.addView(this);


    }

    @Override
    protected void initView() {

        mPlayView = (ImageView) findViewById(R.id.img_media_play);
        mSeekBar = (SeekBar) findViewById(R.id.progressBar_media);
        mCurrentTimeView = (TextView) findViewById(R.id.tv_media_current_time);
        mEndTimeView = (TextView) findViewById(R.id.tv_media_end_time);
        mLoadingBar = findViewById(R.id.progressBar_loading);

        super.initView();
    }

    @Override
    protected View getLoadingBar() {
        return mLoadingBar;
    }

    @Override
    protected TextView getEndTimeView() {
        return mEndTimeView;
    }

    @Override
    protected TextView getCurrentTimeView() {
        return mCurrentTimeView;
    }

    @Override
    protected SeekBar getSeekBarView() {
        return mSeekBar;
    }

    @Override
    protected ImageView getPlayView() {
        return mPlayView;
    }

    @Override
    public void show() {
        getLayoutParams().height = mParentView.getHeight();
        super.show();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.rae_simple_media_controller;
    }
}
