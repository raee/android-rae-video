package com.rae.videodemo.activity;

import com.rae.ui.media.controller.SimpleMediaController;
import com.rae.ui.widget.RaeVideoView;
import com.rae.videodemo.R;

/**
 * 简单版播放例子
 * Created by ChenRui on 2016/9/26 0026 11:48.
 */

public class SimpleDemoActivity extends BaseActivity {
    private RaeVideoView mVideoView;

    @Override
    protected int getLayoutId() {
        return R.layout.at_simple;
    }

    @Override
    protected void initView() {
        mVideoView = (RaeVideoView) findViewById(R.id.rae_video);

        // 设置播放路径，这个时候还没有播放。
        mVideoView.setVideoPath(getString(R.string.demo_simple_video_url));

        // 设置播放控制
        SimpleMediaController mediaController = new SimpleMediaController(this, mVideoView);
        mVideoView.setMediaController(mediaController);

        // 开始播放
//        mVideoView.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        RaeVideoView.releaseAll();
    }
}
