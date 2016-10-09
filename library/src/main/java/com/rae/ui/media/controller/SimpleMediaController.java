package com.rae.ui.media.controller;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.rae.ui.media.R;

/**
 * 简单版播放器控制器
 * Created by ChenRui on 16/9/25 下午9:18.
 */
public class SimpleMediaController extends RaeMediaController {

    private RelativeLayout mLayout;

    /**
     * @param context
     * @param parentView 附加到的父容器中。
     */
    public SimpleMediaController(Context context, ViewGroup parentView) {
        super(context);
        if (parentView == null || parentView.getParent() == null) {
            throw new NullPointerException("视频控制器父容器不能为空,并且不能作为根布局！");
        }

        // 添加到父容器中
        mLayout = new RelativeLayout(context);
        ViewGroup vg = (ViewGroup) parentView.getParent();

        // 移除原来的位置
        int index = vg.indexOfChild(parentView);
        vg.removeView(parentView);

        // 添加到新的布局中
        mLayout.addView(parentView);
        mLayout.addView(this);

        vg.addView(mLayout, index);

        // 父控件布局监听
        parentView.addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(final View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                // 控制器的高度应该等于视频的高度。
                if (v.getHeight() > 0) {
                    Log.d("Rae", "视频高度开始改变:" + v.getHeight());
                    ViewGroup.LayoutParams lp = getLayoutParams();
                    lp.height = v.getHeight();
                    setLayoutParams(lp);
                }
            }
        });

    }

    @Override
    protected View getLoadingBar() {
        return findViewById(R.id.progressBar_loading);
    }

    @Override
    protected TextView getEndTimeView() {
        return (TextView) findViewById(R.id.tv_media_end_time);
    }

    @Override
    protected TextView getCurrentTimeView() {
        return (TextView) findViewById(R.id.tv_media_current_time);
    }

    @Override
    protected SeekBar getSeekBarView() {
        return (SeekBar) findViewById(R.id.progressBar_media);
    }

    @Override
    protected ImageView getPlayView() {
        return (ImageView) findViewById(R.id.img_media_play);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.rae_simple_media_controller;
    }

}
