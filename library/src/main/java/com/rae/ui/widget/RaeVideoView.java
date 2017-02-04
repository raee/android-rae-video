package com.rae.ui.widget;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewTreeObserver;

import com.rae.ui.media.IjkVideoView;
import com.rae.ui.services.MediaPlayerService;

/**
 * 视频播放器
 * Created by ChenRui on 16/9/25 上午1:10.
 */
public class RaeVideoView extends IjkVideoView {

    private static final int STATE_WINDOW_FOCUS_CHANGE = 6;

    private static final int MESSAGE_RESUME = 0;
    private static final int MESSAGE_PAUSE = 1;
    private static final int MESSAGE_REQUEST_VIEW = 2;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MESSAGE_RESUME) {
                onResume();
            }
            if (msg.what == MESSAGE_PAUSE) {
                onPause();
            }
            if (msg.what == MESSAGE_REQUEST_VIEW) {
                Log.i("rae", "requestView:" + getContext().getClass());
                setRender(RENDER_TEXTURE_VIEW);
                requestLayout();
                invalidate();
            }

        }
    };


    public RaeVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (!isShown()) {
                    onDestroy();
                }

                if (!isShown() && isPlaying()) {
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            release();
                        }
                    }, 500);
                }
            }
        });
    }


    /**
     * 设置播放声音
     *
     * @param volume
     */
    public void setVolume(int volume) {
        if (mMediaPlayer != null)
            mMediaPlayer.setVolume(volume, volume);
    }


    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus) {
            mHandler.removeMessages(MESSAGE_RESUME);
            mHandler.sendEmptyMessage(MESSAGE_RESUME);
        } else {
            mHandler.removeMessages(MESSAGE_PAUSE);
            mHandler.sendEmptyMessage(MESSAGE_PAUSE);
        }
    }

    private void log(String msg) {
        Log.d("Rae", msg);
    }

    public void onCreate() {
    }

    public void onDestroy() {
    }

    // 进入前台
    public void onResume() {
        if (mMediaPlayer == null) return;
        Log.d("rae", "来到前台:" + getVideoPath());
        int targetState = mTargetState;
        int currentState = mCurrentState;
        requestView();
        if (currentState == STATE_WINDOW_FOCUS_CHANGE && targetState == STATE_PLAYING) {
            start();
        }
    }

    // 当前界面没有焦点,可能进入后台
    public void onPause() {
        if (mMediaPlayer == null) return;
        int targetState = mCurrentState;
        if (mMediaPlayer.isPlaying())
            pause();
        mCurrentState = STATE_WINDOW_FOCUS_CHANGE;
        mTargetState = targetState;
        Log.d("rae", "进入后台:" + getContext().getClass());
    }

    /**
     * 把视频切换到当前的播放器中
     */
    public void requestView() {
        if (mMediaPlayer == null) return;
        if (mCurrentState != STATE_PLAYBACK_COMPLETED)
            mCurrentState = mMediaPlayer.isPlaying() ? STATE_PLAYING : STATE_PAUSED;
        mHandler.sendEmptyMessageDelayed(MESSAGE_REQUEST_VIEW, 100);
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        onCreate();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        onDestroy();
    }

    public String getVideoPath() {
        return mUri == null ? null : mUri.toString();
    }

    public Uri getVideoUri() {
        return mUri;
    }

    @Override
    public void release() {
        setVolume(0);
        super.release();
    }

    public static void releaseAll() {
        MediaPlayerService.setMediaPlayer(null);
        MediaPlayerService.intentToStop();
    }


}
