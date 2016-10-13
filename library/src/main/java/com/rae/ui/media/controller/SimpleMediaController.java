package com.rae.ui.media.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rae.ui.RaeFullVideoActivity;
import com.rae.ui.media.R;
import com.rae.ui.widget.RaeVideoView;

import java.util.Locale;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * 简单版播放器控制器
 * Created by ChenRui on 16/9/25 下午9:18.
 */
public class SimpleMediaController extends RaeMediaController implements IMediaPlayer.OnErrorListener {

    protected View mFullScreenView;
    protected RaeVideoView mVideoView;

    /**
     * 推荐使用{@link #SimpleMediaController(Context, RaeVideoView)}
     *
     * @param context
     */
    public SimpleMediaController(Context context) {
        super(context);
    }

    public SimpleMediaController(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * @param context
     * @param parentView 附加到的父容器中的RaeVideoView。
     */
    public SimpleMediaController(Context context, RaeVideoView parentView) {
        super(context);
        if (parentView == null) {
            throw new NullPointerException("RaeVideoView is null!");
        }
        setVideoView(parentView);
        parentView.addView(this);
        parentView.setOnErrorListener(this);
        mFullScreenView = findViewById(R.id.img_media_full_screen);
        setFullScreenClickListener(this);
    }

    protected void setVideoView(RaeVideoView view) {
        mVideoView = view;
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.img_media_full_screen) {
            getContext().startActivity(new Intent(getContext(), RaeFullVideoActivity.class));
        }
    }

    /**
     * 全屏的时候请调用该方法
     */
    public void toggleFullScreen() {
        mFullScreenView.setSelected(true);
        setFullScreenClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getContext() instanceof Activity) {
                    Activity activity = (Activity) getContext();
                    activity.finish();
                }
            }
        });
    }

    /**
     * 设置全屏播放按钮点击事件
     *
     * @param listener
     */
    public void setFullScreenClickListener(View.OnClickListener listener) {
        mFullScreenView.setOnClickListener(listener);
    }

    @Override
    public void show() {
        // 控制器的高度 = 视频的高度
        getLayoutParams().height = mVideoView.getHeight();
        super.show();
    }

    @Override
    protected View getLoadingBar() {
        return findViewById(R.id.progressBar_loading);
    }

    @Override
    protected TextView getEndTimeView() {
        return (TextView) findViewById(R.id.tv_media_end_time);
    }

    @Override
    protected TextView getCurrentTimeView() {
        return (TextView) findViewById(R.id.tv_media_current_time);
    }

    @Override
    protected SeekBar getSeekBarView() {
        return (SeekBar) findViewById(R.id.progressBar_media);
    }

    @Override
    protected ImageView getPlayView() {
        return (ImageView) findViewById(R.id.img_media_play);
    }

    public View getFullScreenView() {
        return mFullScreenView;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.rae_simple_media_controller;
    }

    @Override
    public boolean onError(IMediaPlayer iMediaPlayer, int what, int code) {
        Toast.makeText(getContext(), String.format(Locale.getDefault(), "播放错误(what = %d, code = %d)", what, code), Toast.LENGTH_SHORT).show();
        return false;
    }
}
