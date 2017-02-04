package com.rae.videodemo.activity;

import android.view.View;
import android.widget.Button;

import com.rae.ui.media.controller.SimpleMediaController;
import com.rae.ui.widget.RaeVideoView;
import com.rae.videodemo.R;

/**
 * 简单版播放例子
 * Created by ChenRui on 2016/9/26 0026 11:48.
 */

public class SimpleVideoDemoActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.at_simple;
    }

    @Override
    protected void initView() {
        findViewById(R.id.btn_video_volume).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button view = (Button) v;
                if (view.getText().toString().contains("静音")) {
                    mVideoView.setVolume(0);
                    view.setText("恢复");
                } else {
                    mVideoView.setVolume(1);
                    view.setText("静音");
                }

            }
        });

        // 设置播放路径，这个时候还没有播放。
        mVideoView.setVideoPath(getString(R.string.demo_basic_video_url));

        // 设置播放控制
        SimpleMediaController mediaController = new SimpleMediaController(this);
        mVideoView.setMediaController(mediaController);

        // 开始播放
        mVideoView.start();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 停止播放,释放资源
        RaeVideoView.releaseAll();
    }
}
