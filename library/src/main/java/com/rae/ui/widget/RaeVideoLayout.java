package com.rae.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.rae.ui.media.IRaeMediaController;
import com.rae.ui.media.IRenderView;
import com.rae.ui.media.controller.MediaCompleteController;
import com.rae.ui.media.controller.MediaCoverController;
import com.rae.ui.media.controller.SimpleMediaController;

import java.util.Map;
import java.util.WeakHashMap;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * 带封面图布局
 * Created by ChenRui on 2016/10/13 下午11:18.
 */
public class RaeVideoLayout extends RaeVideoView implements IMediaPlayer.OnCompletionListener {

    public static final int VIEW_TYPE_COVER = 1;
    public static final int VIEW_TYPE_COMPLETION = 2; // 播放完成

    private final WeakHashMap<Integer, IRaeMediaController> mViewWeakHashMap = new WeakHashMap<>(); // 类型管理

    public RaeVideoLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initController(context);
    }

    protected void initController(Context context) {
        setCurrentAspectRatio(IRenderView.AR_ASPECT_FILL_PARENT);
        setMediaController(new SimpleMediaController(context));
        addMediaController(VIEW_TYPE_COVER, new MediaCoverController(getContext()));
        addMediaController(VIEW_TYPE_COMPLETION, new MediaCompleteController(context));
        addOnCompletionListener(this);
    }

    @Override
    protected boolean isInPlaybackState() {
        return super.isInPlaybackState() && mCurrentState != STATE_PLAYBACK_COMPLETED;
    }


    /**
     * 添加控制器,如果控制已经添加会移除原来的控制器。
     *
     * @param viewType   控制器类型,参考{{@link #VIEW_TYPE_COMPLETION}} VIEW_TYPE_* 的常量
     * @param controller 控制器
     */
    public void addMediaController(int viewType, IRaeMediaController controller) {
        View view = controller.getView();
        controller.setMediaPlayer(this);
        if (mViewWeakHashMap.containsKey(viewType)) {
            removeView(view);
        }

        // 完成的
        if (viewType == VIEW_TYPE_COMPLETION) {
            controller.hide();
        }

        addView(view);
        mViewWeakHashMap.put(viewType, controller);
    }

    /**
     * 获取控制器
     *
     * @param viewType
     * @return
     */
    public IRaeMediaController getMediaController(int viewType) {
        if (!mViewWeakHashMap.containsKey(viewType)) return null;
        return mViewWeakHashMap.get(viewType);
    }


    /**
     * @param path the path of the video.
     */
    @Override
    public void setVideoPath(String path) {
        setVideoPath(path, false); // 点击封面图才开始播放
    }

    /**
     * 设置封面图
     * <pre>视频的高度会根据封面图的大小重新进行设置</pre>
     *
     * @param url
     */
    public void setVideoCover(String url) {
        setVideoCover(url, null);
    }

    public void setVideoCover(String url, DisplayImageOptions option) {
        if (!mViewWeakHashMap.containsKey(VIEW_TYPE_COVER)) return;
        MediaCoverController view = (MediaCoverController) mViewWeakHashMap.get(VIEW_TYPE_COVER).getView();
        view.setVisibility(View.VISIBLE);
        view.setCoverImageUrl(url, option);
    }

    @Override
    public void onCompletion(IMediaPlayer iMediaPlayer) {
        if (mViewWeakHashMap.containsKey(VIEW_TYPE_COVER) && !mViewWeakHashMap.containsKey(VIEW_TYPE_COMPLETION)) {
            mViewWeakHashMap.get(VIEW_TYPE_COVER).show();
            return;
        }

        for (Map.Entry<Integer, IRaeMediaController> item : mViewWeakHashMap.entrySet()) {
            if (item.getKey() == VIEW_TYPE_COMPLETION) {
                item.getValue().show();
            } else {
                item.getValue().hide();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (Map.Entry<Integer, IRaeMediaController> item : mViewWeakHashMap.entrySet()) {
            item.getValue().onDestroy();
        }
    }
}
