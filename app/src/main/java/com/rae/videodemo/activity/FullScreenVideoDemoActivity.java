package com.rae.videodemo.activity;

import com.rae.ui.RaeFullVideoActivity;
import com.rae.ui.widget.RaeVideoView;
import com.rae.videodemo.R;

/**
 * 全屏播放例子
 * Created by ChenRui on 2016/9/26 0026 11:48.
 */

public class FullScreenVideoDemoActivity extends RaeFullVideoActivity {

    @Override
    protected void start() {
        setTitle(getString(R.string.demo_basic_video_title)); // 设置视频标题,实际为: mMediaController.setTitle(title.toString());
        mVideoView.setVideoPath(getString(R.string.demo_basic_video_url));
        super.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RaeVideoView.releaseAll(); // 停止播放,释放资源
    }
}
