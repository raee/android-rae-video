package com.rae.ui.media.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;

import com.rae.ui.media.IRaeMediaController;
import com.rae.ui.media.IRaeMediaPlayerController;
import com.rae.ui.media.R;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * 播放完成视图
 * Created by ChenRui on 2016/10/19 下午12:29.
 */
public class MediaCompleteController extends FrameLayout implements IRaeMediaController {

    private IRaeMediaPlayerController mMediaPlayer;

    public MediaCompleteController(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.rae_simple_media_completion, this);
        initView();
    }

    protected View getReplayView() {
        return findViewById(R.id.tv_media_replay);
    }

    protected void initView() {
        View replayView = getReplayView();

        if (replayView != null) {
            replayView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mMediaPlayer != null) {
                        mMediaPlayer.restart();
                        hide();
                    }
                }
            });
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (getParent() != null) {
            int h = ((ViewGroup) getParent()).getHeight();
            if (h > 0)
                getLayoutParams().height = h;
        }
    }

    @Override
    public void setMediaPlayer(IRaeMediaPlayerController player) {
        mMediaPlayer = player;
        mMediaPlayer.addOnInfoListener(new IMediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(IMediaPlayer mp, int what, int i1) {
                switch (what) {
                    case IMediaPlayer.MEDIA_ERROR_SERVER_DIED:
                    case IMediaPlayer.MEDIA_INFO_AUDIO_RENDERING_START:
                    case IMediaPlayer.MEDIA_INFO_VIDEO_ROTATION_CHANGED:
                        hide();
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void onDestroy() {
        hide();
    }

    @Override
    public void show() {
        if (getVisibility() != VISIBLE) {
            Animation anim = new AlphaAnimation(0, 1);
            anim.setDuration(300);
            startAnimation(anim);
            setVisibility(VISIBLE);
        }
    }

    @Override
    public void hide() {
        setVisibility(GONE);
    }
}
