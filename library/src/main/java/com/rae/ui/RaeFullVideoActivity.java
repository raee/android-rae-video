package com.rae.ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.rae.ui.media.IRenderView;
import com.rae.ui.media.R;
import com.rae.ui.media.controller.FullScreenMediaController;
import com.rae.ui.services.MediaPlayerService;
import com.rae.ui.widget.RaeVideoView;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * 全屏播放
 * Created by ChenRui on 2016/10/12 下午9:11.
 */
public class RaeFullVideoActivity extends AppCompatActivity {

    protected RaeVideoView mVideoView;
    protected FullScreenMediaController mMediaController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // 如果视频的高度大于宽度就竖屏显示
        IMediaPlayer player = MediaPlayerService.getMediaPlayer();
        if (player != null && player.getVideoHeight() > player.getVideoWidth()) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 切换竖屏
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.rae_full_video_activity);
        mVideoView = (RaeVideoView) findViewById(R.id.rae_video_view);
        mMediaController = new FullScreenMediaController(this);
        mVideoView.setMediaController(mMediaController);
        mVideoView.setCurrentAspectRatio(IRenderView.AR_ASPECT_FILL_PARENT); // 设置为视频填满全屏
        mMediaController.toggleFullScreen();
        start();

        String title = getIntent().getStringExtra(Intent.EXTRA_TITLE);
        if (title != null)
            setTitle(title);
    }

    protected void start() {
        mVideoView.start();
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        mMediaController.setTitle(title.toString()); // 设置标题
    }
}
