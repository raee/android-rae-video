package com.rae.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import com.rae.ui.media.IjkVideoView;
import com.rae.ui.services.MediaPlayerService;

/**
 * 视频播放器
 * Created by ChenRui on 16/9/25 上午1:10.
 */
public class RaeVideoView extends IjkVideoView {

    private static final int STATE_WINDOW_FOCUS_CHANGE = 6;
    //    public static final int VIEW_TYPE_COVER = 1; // 封面图
//    public static final int VIEW_TYPE_COMPLETION = 2; // 视频播放完成时显示。
//    private final HashMap<Integer, View> mViewHashMap = new LinkedHashMap<>(); // 视图集合
//    private long mCurrentPosition; // 保存当前播放进度
    private String mVideoPath;

    public RaeVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
//        if (attrs != null) {
//            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RaeVideoView);
//            int len = a.getIndexCount();
//            for (int i = 0; i < len; i++) {
//                int index = a.getIndex(i);
//            }
//            a.recycle();
//        }
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
            onResume();
        } else {
            onPause();
        }
    }


    public void onCreate() {

    }

    public void onDestroy() {

    }

    // 进入前台
    public void onResume() {
        if (mMediaPlayer == null) return;
        Log.d("rae", "来到前台:" + getContext().getClass());
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
        mCurrentState = mMediaPlayer.isPlaying() ? STATE_PLAYING : STATE_PAUSED;
        postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i("rae", "requestView");
                setRender(RENDER_TEXTURE_VIEW);
                requestLayout();
                invalidate();
            }
        }, 100);
    }

//
//    // 添加视图
//    public void addView(int viewType, View view) {
//
//        // 移除已经存在的View
//        if (mViewHashMap.containsKey(viewType)) {
//            removeView(mViewHashMap.get(viewType));
//        }
//
//        // 添加View
//        mViewHashMap.put(viewType, view);
//
//        // 初始化View的显示
//        switch (viewType) {
//            case VIEW_TYPE_COVER:
//                view.setVisibility(View.VISIBLE);
//                break;
//            default:
//                view.setVisibility(View.GONE);
//                break;
//        }
//
//        addView(view);
//    }

    @Override
    public void setVideoPath(String path) {
//        if (mVideoPath == null && mViewHashMap.containsKey(VIEW_TYPE_COVER)) {
//            mVideoPath = path;
//            return;
//        }
        mVideoPath = path;
        super.setVideoPath(path);
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
        return mVideoPath;
    }

    public static void releaseAll() {
        MediaPlayerService.setMediaPlayer(null);
        MediaPlayerService.intentToStop();
    }

}
