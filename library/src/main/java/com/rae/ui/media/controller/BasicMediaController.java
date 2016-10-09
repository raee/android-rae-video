package com.rae.ui.media.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.rae.ui.media.R;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * 基础版播放器控制器
 * Created by ChenRui on 16/9/25 下午9:18.
 */
public class BasicMediaController extends SimpleMediaController {


    private final View mCoverPlayView;
    // 封面图
    private View mCoverLayout;

    private ImageView mCoverView;
    private View mCoverProgressBar;

    public BasicMediaController(Context context, ViewGroup parentView) {
        super(context, parentView);
        mCoverLayout = LayoutInflater.from(context).inflate(R.layout.rae_simple_media_cover, null);
        addView(mCoverLayout);
        setVisibility(View.VISIBLE);

        mCoverView = (ImageView) mCoverLayout.findViewById(R.id.img_media_cover);
        mCoverPlayView = mCoverLayout.findViewById(R.id.img_media_cover_play);
        mCoverPlayView.setOnClickListener(this);
        mCoverProgressBar = mCoverLayout.findViewById(R.id.progressBar_cover_loading);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.rae_basic_media_controller;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.img_media_cover_play) {
            doPauseResume();
            mCoverProgressBar.setVisibility(View.VISIBLE);
            mCoverPlayView.setVisibility(View.GONE);
        }
    }

    // 设置封面图
    public void setCoverImageUrl(String url) {
        setCoverImageUrl(url, null);
    }

    @Override
    public void onMediaInfo(int what) {
        super.onMediaInfo(what);
        switch (what) {
            case IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                mCoverLayout.setVisibility(View.GONE);
                mCoverProgressBar.setVisibility(View.GONE);
                mCoverPlayView.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void hide() {
        if (mCoverLayout.getVisibility() == View.VISIBLE) return;
        super.hide();
    }

    // 设置封面图
    public void setCoverImageUrl(String url, DisplayImageOptions config) {
        ImageLoader.getInstance().displayImage(url, mCoverView, config);
    }
}
