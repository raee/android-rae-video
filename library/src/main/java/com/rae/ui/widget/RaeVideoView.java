package com.rae.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import com.rae.ui.media.IjkVideoView;
import com.rae.ui.media.R;
import com.rae.ui.services.MediaPlayerService;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * 视频播放器
 * Created by ChenRui on 16/9/25 上午1:10.
 */
public class RaeVideoView extends IjkVideoView {

    private static final int STATE_WINDOW_FOCUS_CHANGE = 10;
    public static final int VIEW_TYPE_COVER = 1; // 封面图
    public static final int VIEW_TYPE_COMPLETION = 2; // 视频播放完成时显示。

    private final HashMap<Integer, View> mViewHashMap = new LinkedHashMap<>(); // 视图集合
    private long mCurrentPosition; // 保存当前播放进度

    public RaeVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RaeVideoView);
        int len = a.getIndexCount();
        for (int i = 0; i < len; i++) {
            int index = a.getIndex(i);
        }
        a.recycle();

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
        int targetState = mCurrentState;

        // 当前界面没有焦点,可能进入后台
        if (mMediaPlayer != null && !hasWindowFocus) {
            if (mMediaPlayer.isPlaying())
                pause();
            mCurrentState = STATE_WINDOW_FOCUS_CHANGE;
            mTargetState = targetState;
            mCurrentPosition = mMediaPlayer.getCurrentPosition();
        }

        // 进入前台
        if (mMediaPlayer != null
                && mCurrentState == STATE_WINDOW_FOCUS_CHANGE
                && hasWindowFocus
                ) {
            if (mTargetState == STATE_PLAYING)
                start();

            if (!mMediaPlayer.isPlaying()) {
                setRender(RENDER_SURFACE_VIEW);
            }

        }
    }

    // 添加视图
    public void addView(int viewType, View view) {

        // 移除已经存在的View
        if (mViewHashMap.containsKey(viewType)) {
            removeView(mViewHashMap.get(viewType));
        }

        // 添加View
        mViewHashMap.put(viewType, view);

        // 初始化View的显示
        view.setVisibility(View.GONE);

        addView(view);
    }


    public static void releaseAll() {
        MediaPlayerService.setMediaPlayer(null);
    }

}
