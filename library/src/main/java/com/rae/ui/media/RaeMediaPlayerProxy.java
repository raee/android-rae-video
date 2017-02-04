package com.rae.ui.media;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.TextureMediaPlayer;

/**
 * Created by ChenRui on 2016/10/19 上午11:17.
 */
public class RaeMediaPlayerProxy extends TextureMediaPlayer {

    private OnInfoListener mOnInfoListener;

    public RaeMediaPlayerProxy(IMediaPlayer backEndMediaPlayer) {
        super(backEndMediaPlayer);
    }

    @Override
    public void release() {
        super.release();
        if (mOnInfoListener != null) {
            mOnInfoListener.onInfo(mBackEndMediaPlayer, IjkMediaPlayer.MEDIA_ERROR_SERVER_DIED, 0);
        }
    }

    @Override
    public void setOnInfoListener(OnInfoListener listener) {
        super.setOnInfoListener(listener);
        mOnInfoListener = listener;
    }
}
