package com.rae.ui.media.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.rae.ui.media.IjkVideoView;
import com.rae.ui.media.R;
import com.rae.ui.widget.RaeVideoView;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * 视频封面图视图
 * Created by ChenRui on 2016/10/12 下午4:46.
 */
public class MediaCoverController extends FrameLayout implements View.OnClickListener {

    private RaeVideoView mMediaController;
    private View mPlayView;
    private ImageView mCoverImageView;
    private View mCoverProgressBar;
    private int mImageHeight;

    public MediaCoverController(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.rae_simple_media_cover, this);
        mCoverImageView = (ImageView) findViewById(R.id.img_media_cover);
        mPlayView = findViewById(R.id.img_media_cover_play);
        mPlayView.setOnClickListener(this);
        mCoverProgressBar = findViewById(R.id.progressBar_cover_loading);

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (!(getParent() instanceof IjkVideoView)) {
            throw new IllegalAccessError("封面图的父类必须是继承自IjkVideoView");
        }

        mMediaController = (RaeVideoView) getParent();
        mMediaController.setOnInfoListener(new IMediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(IMediaPlayer iMediaPlayer, int what, int i1) {
                switch (what) {
                    case IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                        postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mCoverProgressBar.setVisibility(View.GONE);
                                setVisibility(View.GONE);
                            }
                        }, 500);
                        break;
                }

                return false;
            }
        });

        int height = ((ViewGroup) getParent()).getHeight();
        if (height > 0)
            getLayoutParams().height = height;

        if (mImageHeight > 0) {
            mMediaController.getLayoutParams().height = mImageHeight;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.img_media_cover_play) {
            mCoverProgressBar.setVisibility(View.VISIBLE);
            mPlayView.setVisibility(View.GONE);
            mMediaController.setVideoPath(mMediaController.getVideoPath());
            mMediaController.start();
        }
    }

    // 设置封面图
    public void setCoverImageUrl(String url) {
        setCoverImageUrl(url, null);
    }


    // 设置封面图
    public void setCoverImageUrl(String url, DisplayImageOptions config) {
        ImageLoader.getInstance().displayImage(url, mCoverImageView, config, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                // 计算图片高度
                int w = loadedImage.getWidth();
                int h = loadedImage.getHeight();
                int mathWidth = getResources().getDisplayMetrics().widthPixels;
                int height = mathWidth * h / w;
                if (height > 0 && view.getLayoutParams() != null) {
                    view.getLayoutParams().height = height;
                    mImageHeight = height;
                    // 这个时候视频高度也应该是封面图高度了
                    if (mMediaController != null && mMediaController.getLayoutParams() != null)
                        mMediaController.getLayoutParams().height = height;
                }

            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
    }
}
