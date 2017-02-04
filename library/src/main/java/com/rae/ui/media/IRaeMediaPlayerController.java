package com.rae.ui.media;

import android.widget.MediaController;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by ChenRui on 2016/10/19 下午12:52.
 */
public interface IRaeMediaPlayerController extends MediaController.MediaPlayerControl {

    void addOnCompletionListener(IMediaPlayer.OnCompletionListener l);

    void addOnErrorListener(IMediaPlayer.OnErrorListener l);

    void addOnInfoListener(IMediaPlayer.OnInfoListener l);

    void addOnPreparedListener(IMediaPlayer.OnPreparedListener l);

    void resume();

    // 重置播放状态
    void restart();

    String getVideoTitle();
}
