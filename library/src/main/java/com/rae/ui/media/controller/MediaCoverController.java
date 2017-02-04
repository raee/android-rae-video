package com.rae.ui.media.controller;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.rae.ui.media.IRaeMediaController;
import com.rae.ui.media.IRaeMediaPlayerController;
import com.rae.ui.media.R;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * 视频封面图视图
 * Created by ChenRui on 2016/10/12 下午4:46.
 */
public class MediaCoverController extends FrameLayout implements IRaeMediaController, View.OnClickListener {

    private IRaeMediaPlayerController mMediaController;
    private ImageView mPlayView;
    private ImageView mCoverImageView;
    private ImageView mAlphaView;
    private View mCoverProgressBar;
    private int mImageHeight;

    public MediaCoverController(Context context) {
        this(context, null);
    }

    public MediaCoverController(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.RaeMediaControllerStyle);
    }

    public MediaCoverController(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RaeMediaController, defStyleAttr, 0);
        int len = a.getIndexCount();
        for (int i = 0; i < len; i++) {
            int index = a.getIndex(i);
            if (index == R.styleable.RaeMediaController_playBackground) {
                mPlayView.setImageDrawable(a.getDrawable(index));
            }
        }
        a.recycle();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.rae_simple_media_cover, this);
        mCoverImageView = (ImageView) findViewById(R.id.img_media_cover);
        mPlayView = (ImageView) findViewById(R.id.img_media_cover_play);
        mAlphaView = (ImageView) findViewById(R.id.img_media_cover_alpha);
        mPlayView.setOnClickListener(this);
        mCoverProgressBar = findViewById(R.id.progressBar_cover_loading);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ViewGroup parent = (ViewGroup) getParent();
        int parentHeight = parent.getHeight();

        if (parentHeight > 0) {
            getLayoutParams().height = parentHeight;
        }
        if (mImageHeight > 0) {
            parent.getLayoutParams().height = mImageHeight;
        }
    }

    private void startHideAnim() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setDuration(300);
        startAnimation(alphaAnimation);
        mCoverProgressBar.startAnimation(alphaAnimation);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.img_media_cover_play) {
            startAnim();
            mMediaController.resume();
            mMediaController.start();

        }
    }

    // 设置封面图
    public void setCoverImageUrl(String url) {
        setCoverImageUrl(url, null);
    }


    // 设置封面图
    public void setCoverImageUrl(String url, DisplayImageOptions config) {
        //reset();
        ImageLoader.getInstance().displayImage(url, mCoverImageView, config, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                mPlayView.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                //mPlayView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                mPlayView.setVisibility(View.VISIBLE);
                // 计算图片高度
                int w = loadedImage.getWidth();
                int h = loadedImage.getHeight();
                int mathWidth = getResources().getDisplayMetrics().widthPixels;
                int height = mathWidth * h / w;
                if (height > 0 && view.getLayoutParams() != null) {
                    view.getLayoutParams().height = height;
                    mImageHeight = height;
                    // 这个时候视频高度也应该是封面图高度了
                    if (mMediaController != null && getParent() != null && ((View) getParent()).getLayoutParams() != null)
                        ((View) getParent()).getLayoutParams().height = height;
                }

            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                mPlayView.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * @param listener
     */
    public void setCoverClickListener(View.OnClickListener listener) {
        mCoverImageView.setOnClickListener(listener);
    }

    // 重置
    private void reset() {

        // 隐藏加载中
        mCoverProgressBar.clearAnimation();
        mCoverProgressBar.setVisibility(GONE);

        // 清除封面图动画
        mCoverImageView.clearAnimation();

        // 显示播放按钮
        mPlayView.setVisibility(VISIBLE);
        mPlayView.clearAnimation();

        // 隐藏遮罩层
        mAlphaView.clearAnimation();
        mAlphaView.setVisibility(GONE);

        // 执行显示
        clearAnimation();
        setVisibility(View.VISIBLE);
    }


    @Override
    public void hide() {
        setVisibility(View.GONE);
    }

    private void startAnim() {
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(300);
        scaleAnimation.setFillAfter(true);
        mCoverImageView.startAnimation(scaleAnimation);

        ScaleAnimation alphaViewAnim = new ScaleAnimation(1.0f, 10f, 1.0f, 10f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        alphaViewAnim.setDuration(500);
        alphaViewAnim.setFillAfter(true);
        mAlphaView.setVisibility(VISIBLE);
        mAlphaView.startAnimation(alphaViewAnim);


        AnimationSet playAnim = new AnimationSet(true);

        AlphaAnimation playAlphaAnim = new AlphaAnimation(1, 0);
        playAlphaAnim.setDuration(300);
        ScaleAnimation scaleFadeoutAnimation = new ScaleAnimation(1.0f, 0f, 1.0f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleFadeoutAnimation.setDuration(500);

        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(300);
        rotateAnimation.setInterpolator(new AccelerateInterpolator());

        playAnim.addAnimation(scaleFadeoutAnimation);
        playAnim.addAnimation(rotateAnimation);
        playAnim.addAnimation(playAlphaAnim);

        playAnim.setFillAfter(true);
        mPlayView.startAnimation(playAnim);

        AlphaAnimation loadingBarAnim = new AlphaAnimation(0, 1);
        loadingBarAnim.setDuration(500);
        loadingBarAnim.setStartOffset(300);
        loadingBarAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mCoverProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mCoverProgressBar.startAnimation(loadingBarAnim);


    }

    @Override
    public void setMediaPlayer(IRaeMediaPlayerController player) {
        mMediaController = player;
        mMediaController.addOnInfoListener(new IMediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(IMediaPlayer iMediaPlayer, int what, int i1) {
                switch (what) {
                    case IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                        postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startHideAnim();
                                hide();
                            }
                        }, 500);
                        break;
                    case IMediaPlayer.MEDIA_ERROR_SERVER_DIED:
                        reset();
                        break;
                }

                return false;
            }
        });

        mMediaController.addOnErrorListener(new IMediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
                reset();
                return true;
            }
        });

    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void onDestroy() {
        reset();
    }

    @Override
    public void show() {
        setVisibility(VISIBLE);
    }
}
