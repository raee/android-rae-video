package com.rae.videodemo.activity;

import com.rae.ui.media.controller.BasicMediaController;
import com.rae.ui.widget.RaeVideoView;
import com.rae.videodemo.R;

/**
 * 简单版播放例子
 * Created by ChenRui on 2016/9/26 0026 11:48.
 */

public class BasicVideoDemoActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.at_basic;
    }

    @Override
    protected void initView() {


        // 设置播放控制
        BasicMediaController mediaController = new BasicMediaController(this, mVideoView);
        mediaController.setCoverImageUrl(getString(R.string.demo_basic_video_cover_url));
        mVideoView.setMediaController(mediaController);


        // 设置播放路径，这个时候还没有播放。
        mVideoView.setVideoPath(getString(R.string.demo_basic_video_url));

        // 开始播放
        //mVideoView.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 停止播放,释放资源
        RaeVideoView.releaseAll();
    }
}
