package com.rae.ui.media.controller;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;

import com.rae.ui.media.R;

import java.util.Locale;

import tv.danmaku.ijk.media.player.IMediaPlayer;


/**
 * base media controller
 * Created by ChenRui on 2016/9/26 0026 12:44.
 */
public abstract class RaeMediaController extends FrameLayout implements IMediaController, View.OnClickListener {

    protected static final int MESSAGE_FADE_OUT = 0;
    protected static final int MESSAGE_SHOW_PROGRESS = 1;
    protected static final int MESSAGE_UPDATE_PLAY_STATE = 2;

    protected int mDefaultTimeOut = 3000;
    protected MediaController.MediaPlayerControl mPlayer;
    protected boolean mDragging;

    protected SeekBar mSeekBar;
    protected View mLoadingBar;
    protected TextView mEndTimeView;
    protected TextView mCurrentTimeView;
    protected ImageView mPlayView;

    protected Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MESSAGE_FADE_OUT:
                    hide();
                    break;
                case MESSAGE_UPDATE_PLAY_STATE:
                    updatePausePlay();
                    break;
                case MESSAGE_SHOW_PROGRESS:
                    int pos = setProgress();
                    if (!mDragging && isShowing() && mPlayer.isPlaying()) {
                        msg = obtainMessage(MESSAGE_SHOW_PROGRESS);
                        sendMessageDelayed(msg, 1000 - (pos % 1000));
                    }
                    break;
            }
        }
    };

    public RaeMediaController(Context context) {
        super(context);
        inflaterLayout();
        initView();
    }


    public RaeMediaController(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflaterLayout();
        initView();


    }

    private void inflaterLayout() {
        LayoutInflater.from(getContext()).inflate(getLayoutId(), this);
        setVisibility(View.GONE);
    }

    // 可以重写该方法进行布局初始化操作
    protected void initView() {
        mPlayView = getPlayView();
        mSeekBar = getSeekBarView();
        mCurrentTimeView = getCurrentTimeView();
        mEndTimeView = getEndTimeView();
        mLoadingBar = getLoadingBar();

        if (mPlayView != null) {
            mPlayView.setOnClickListener(this);
        }

        if (mSeekBar != null) {
            mSeekBar.setOnSeekBarChangeListener(mSeekListener);
            mSeekBar.setMax(1000);
        }
    }

    /**
     * 加载中
     *
     * @return
     */
    protected abstract View getLoadingBar();

    /**
     * 获取布局文件
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 播放结束时间文本显示，如：05:00
     *
     * @return
     */
    protected abstract TextView getEndTimeView();


    /**
     * 当前播放进度文本显示，如：00:01
     *
     * @return
     */
    protected abstract TextView getCurrentTimeView();

    /**
     * 播放进度控制条
     *
     * @return
     */
    protected abstract SeekBar getSeekBarView();

    /**
     * 播放按钮，播放的时候selected = true ,请在xml配置好selector
     *
     * @return
     */
    protected abstract ImageView getPlayView();

    @Override
    public void setMediaPlayer(MediaController.MediaPlayerControl player) {
        mPlayer = player;
        updatePausePlay();
    }

    @Override
    public void show(int timeout) {
        setVisibility(View.VISIBLE);

        updatePausePlay();
        mHandler.sendEmptyMessage(MESSAGE_SHOW_PROGRESS);
        if (timeout != 0) {
            mHandler.removeMessages(MESSAGE_FADE_OUT);
            Message msg = mHandler.obtainMessage(MESSAGE_FADE_OUT);
            mHandler.sendMessageDelayed(msg, timeout);
        }

    }

    @Override
    public void show() {
        show(mDefaultTimeOut);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.img_media_play) {
            doPauseResume();
        }
    }

    protected void doPauseResume() {
        if (mPlayer == null) return;

        if (mPlayer.isPlaying()) {
            mPlayer.pause();
            mHandler.removeMessages(MESSAGE_FADE_OUT);
            show(360000);
        } else {
            mPlayer.start();
            mHandler.sendEmptyMessageDelayed(MESSAGE_FADE_OUT, mDefaultTimeOut);
        }
        updatePausePlay(!mPlayView.isSelected());
        mHandler.sendEmptyMessageDelayed(MESSAGE_UPDATE_PLAY_STATE, 300);
        mHandler.sendEmptyMessage(MESSAGE_SHOW_PROGRESS);
    }


    protected String buildTimeMilli(long duration) {
        long total_seconds = duration / 1000;
        long hours = total_seconds / 3600;
        long minutes = (total_seconds % 3600) / 60;
        long seconds = total_seconds % 60;
        if (duration <= 0) {
            return "00:00";
        }
        if (hours >= 100) {
            return String.format(Locale.US, "%d:%02d:%02d", hours, minutes, seconds);
        } else if (hours > 0) {
            return String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format(Locale.US, "%02d:%02d", minutes, seconds);
        }
    }


    protected int setProgress() {
        if (mPlayer == null || mDragging) {
            return 0;
        }
        int position = mPlayer.getCurrentPosition();
        int duration = mPlayer.getDuration();


        if (mSeekBar != null) {
            if (duration > 0) {
                // use long to avoid overflow
                long pos = mSeekBar.getMax() * position / duration;
                mSeekBar.setProgress((int) pos);
            }
            int percent = mPlayer.getBufferPercentage();
            mSeekBar.setSecondaryProgress(percent * 10);
        }

        if (mEndTimeView != null)
            mEndTimeView.setText(buildTimeMilli(duration));
        if (mCurrentTimeView != null)
            mCurrentTimeView.setText(buildTimeMilli(position));


        onVideoProgressChange(position, duration, mCurrentTimeView.getText(), mEndTimeView.getText());

        return position;
    }

    /**
     * 显示进度回调
     *
     * @param position         当前播放进度
     * @param duration         总长度
     * @param currentTimeMilli 计算好的当前播放时间
     * @param endTimeMilli     计算好的总视频时间
     */
    protected void onVideoProgressChange(int position, int duration, CharSequence currentTimeMilli, CharSequence endTimeMilli) {

    }


    // There are two scenarios that can trigger the seekbar listener to trigger:
    //
    // The first is the user using the touchpad to adjust the posititon of the
    // seekbar's thumb. In this case onStartTrackingTouch is called followed by
    // a number of onProgressChanged notifications, concluded by onStopTrackingTouch.
    // We're setting the field "mDragging" to true for the duration of the dragging
    // session to avoid jumps in the position in case of ongoing playback.
    //
    // The second scenario involves the user operating the scroll ball, in this
    // case there WON'T BE onStartTrackingTouch/onStopTrackingTouch notifications,
    // we will simply apply the updated position without suspending regular updates.
    private final SeekBar.OnSeekBarChangeListener mSeekListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onStartTrackingTouch(SeekBar bar) {
            show(3600000);

            mDragging = true;

            // By removing these pending progress messages we make sure
            // that a) we won't update the progress while the user adjusts
            // the seekbar and b) once the user is done dragging the thumb
            // we will post one of these messages to the queue again and
            // this ensures that there will be exactly one message queued up.
            mHandler.removeMessages(MESSAGE_SHOW_PROGRESS);
        }

        @Override
        public void onProgressChanged(SeekBar bar, int progress, boolean fromuser) {
            if (!fromuser) {
                // We're not interested in programmatically generated changes to
                // the progress bar's position.
                return;
            }

            long duration = mPlayer.getDuration();
            long newposition = (duration * progress) / 1000L;
            mPlayer.seekTo((int) newposition);
            if (mCurrentTimeView != null)
                mCurrentTimeView.setText(buildTimeMilli((int) newposition));
        }

        @Override
        public void onStopTrackingTouch(SeekBar bar) {
            mDragging = false;
            setProgress();
            updatePausePlay();
            show(mDefaultTimeOut);

            // Ensure that progress is properly updated in the future,
            // the call to show() does not guarantee this because it is a
            // no-op if we are already showing.
            mHandler.sendEmptyMessage(MESSAGE_SHOW_PROGRESS);
        }
    };

    @Override
    public void hide() {
        setVisibility(View.GONE);
        updatePausePlay();
    }

    @Override
    public boolean isShowing() {
        return this.getVisibility() == View.VISIBLE;
    }

    private void updatePausePlay() {
        if (mPlayer == null) return;
        updatePausePlay(!mPlayer.isPlaying());
    }

    protected void updatePausePlay(boolean selected) {
        if (mPlayView != null)
            mPlayView.setSelected(selected);
    }

    @Override
    public void showOnce(View view) {
        //不用实现
    }

    @Override
    public void setAnchorView(View view) {

    }

    @Override
    public void setEnabled(boolean enabled) {
    }

    @Override
    public void onMediaInfo(int what) {
        switch (what) {
            case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
                updateLoadingBar(true);
                break;
            case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
                updateLoadingBar(false);
                break;
        }
    }

    // 更新加载状态
    protected void updateLoadingBar(boolean show) {
//        if (show) {
//            mHandler.removeMessages(MESSAGE_FADE_OUT);
//        } else {
//            mHandler.sendEmptyMessageDelayed(MESSAGE_FADE_OUT, mDefaultTimeOut);
//        }

        if (mPlayer == null || !mPlayer.isPlaying()) return;

        // 显示加载中
        if (mLoadingBar != null)
            mLoadingBar.setVisibility(show ? View.VISIBLE : View.GONE);

        // 隐藏播放按钮
        if (mPlayView != null)
            mPlayView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onCompletion() {
        mHandler.removeMessages(MESSAGE_SHOW_PROGRESS);
        mHandler.removeMessages(MESSAGE_FADE_OUT);
    }

}
