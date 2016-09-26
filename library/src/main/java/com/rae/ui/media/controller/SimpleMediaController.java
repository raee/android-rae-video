package com.rae.ui.media.controller;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;

import com.rae.ui.R;

import java.util.Locale;

/**
 * Created by ChenRui on 16/9/25 下午9:18.
 */

public class SimpleMediaController extends FrameLayout implements IMediaController, View.OnClickListener {

    private static final int MESSAGE_FADE_OUT = 0;
    private static final int MESSAGE_SHOW_PROGRESS = 1;

    private MediaController.MediaPlayerControl mPlayer;
    private int mDefaultTimeOut = 3000;
    private ImageView mPlayView;
    private ViewGroup mParentView;
    private SeekBar mProgressBar;
    private TextView mEndTimeView;
    private TextView mCurrentTimeView;
    private boolean mDragging;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MESSAGE_FADE_OUT:
                    hide();
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

    public SimpleMediaController(Context context) {
        super(context);
        LayoutInflater.from(getContext()).inflate(R.layout.rae_simple_media_controller, this);
        setVisibility(View.GONE);
        mPlayView = (ImageView) findViewById(R.id.img_media_play);
        mProgressBar = (SeekBar) findViewById(R.id.progressBar_media);
        mProgressBar.setOnSeekBarChangeListener(mSeekListener);
        mProgressBar.setMax(1000);

        mCurrentTimeView = (TextView) findViewById(R.id.tv_media_current_time);
        mEndTimeView = (TextView) findViewById(R.id.tv_media_end_time);

        mPlayView.setOnClickListener(this);

    }


    private int setProgress() {
        if (mPlayer == null || mDragging) {
            return 0;
        }
        int position = mPlayer.getCurrentPosition();
        int duration = mPlayer.getDuration();
        if (mProgressBar != null) {
            if (duration > 0) {
                // use long to avoid overflow
                long pos = 1000L * position / duration;
                mProgressBar.setProgress((int) pos);
            }
            int percent = mPlayer.getBufferPercentage();
            mProgressBar.setSecondaryProgress(percent * 10);
        }

        if (mEndTimeView != null)
            mEndTimeView.setText(buildTimeMilli(duration));
        if (mCurrentTimeView != null)
            mCurrentTimeView.setText(buildTimeMilli(position));

        return position;
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
    }

    @Override
    public boolean isShowing() {
        return this.getVisibility() == View.VISIBLE;
    }

    private void updatePausePlay() {
        if (mPlayer == null) return;
        updatePausePlay(!mPlayer.isPlaying());
    }

    private void updatePausePlay(boolean selected) {
        mPlayView.setSelected(selected);
    }

    @Override
    public void setAnchorView(View view) {

    }

    @Override
    public void setEnabled(boolean enabled) {

    }

    @Override
    public void setMediaPlayer(MediaController.MediaPlayerControl player) {
        mPlayer = player;
        updatePausePlay();
    }

    @Override
    public void show(int timeout) {
        setVisibility(View.VISIBLE);
        getLayoutParams().height = mParentView.getHeight();

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
    public void onLoading() {

    }

    @Override
    public void showOnce(View view) {
        //不用实现
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.img_media_play){
            doPauseResume();
        }
    }

    public void attach(ViewGroup parent) {
        mParentView = parent;
        parent.addView(this);
    }

    private void doPauseResume() {
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
    }

    private String buildTimeMilli(long duration) {
        long total_seconds = duration / 1000;
        long hours = total_seconds / 3600;
        long minutes = (total_seconds % 3600) / 60;
        long seconds = total_seconds % 60;
        if (duration <= 0) {
            return "--:--";
        }
        if (hours >= 100) {
            return String.format(Locale.US, "%d:%02d:%02d", hours, minutes, seconds);
        } else if (hours > 0) {
            return String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format(Locale.US, "%02d:%02d", minutes, seconds);
        }
    }

}
