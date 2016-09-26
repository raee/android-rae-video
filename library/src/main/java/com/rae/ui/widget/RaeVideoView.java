package com.rae.ui.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.rae.ui.services.MediaPlayerService;
import com.rae.ui.media.IjkVideoView;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.misc.ITrackInfo;

/**
 * Created by ChenRui on 16/9/25 上午1:10.
 */

public class RaeVideoView extends IjkVideoView {

    private static final int STATE_WINDOW_FOCUS_CHANGE = 10;

    private boolean mAudioEnable;

    public RaeVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void selectAudioTrack(IMediaPlayer mediaPlayer) {
        ITrackInfo[] tracks = mediaPlayer.getTrackInfo();
        int index = 0;
        for (ITrackInfo info : tracks) {
            if (info.getTrackType() == ITrackInfo.MEDIA_TRACK_TYPE_AUDIO) {
                if (mAudioEnable)
                    selectTrack(index);
                else deselectTrack(index);
                return;
            }
            index++;
        }
    }

    @Override
    protected void onMediaPlayPrepared(IMediaPlayer mediaPlayer) {
//        selectAudioTrack(mediaPlayer);
    }

    /**
     * 切换到当前VideoView 来
     */
    public void toggle() {
        mMediaPlayer = MediaPlayerService.getMediaPlayer();
        setRender(RENDER_TEXTURE_VIEW);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        // 当前界面没有焦点,可能进入后台
        if (mMediaPlayer != null && !hasWindowFocus && mMediaPlayer.isPlaying()) {
            pause();
            mCurrentState = STATE_WINDOW_FOCUS_CHANGE;
        }

        // 进入前台
        if (mMediaPlayer != null && mCurrentState == STATE_WINDOW_FOCUS_CHANGE && hasWindowFocus) {
            start();
        }
    }

    public static void releaseAll() {
        MediaPlayerService.setMediaPlayer(null);
    }
}
