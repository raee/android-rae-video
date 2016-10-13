package com.rae.videodemo.activity;

import com.rae.videodemo.R;

/**
 * 单个播放器示例
 * Created by ChenRui on 16/10/5 下午12:32.
 */
public class SingleVideoDemoActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.at_base_video;
    }

    @Override
    protected void initView() {
        // 设置播放地址
        mVideoView.setVideoPath(getString(R.string.demo_video_url_single));

        // 开始播放
        mVideoView.start();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        // 重新开始播放
        mVideoView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVideoView.pause(); // 暂停播放
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVideoView.release(); // 停止播放,释放资源
    }
}
