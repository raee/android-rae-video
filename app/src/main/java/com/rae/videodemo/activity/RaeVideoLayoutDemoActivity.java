package com.rae.videodemo.activity;

import com.rae.ui.widget.RaeVideoLayout;
import com.rae.ui.widget.RaeVideoView;
import com.rae.videodemo.R;

/**
 * 标准版示例
 * Created by ChenRui on 2016/10/13 下午11:15.
 */
public class RaeVideoLayoutDemoActivity extends BaseActivity {

    protected RaeVideoLayout mRaeVideoLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.at_basic;
    }

    @Override
    protected void initView() {
        mRaeVideoLayout = (RaeVideoLayout) findViewById(R.id.rae_video_layout);
        mRaeVideoLayout.setVideoPath(getString(R.string.demo_basic_video_url));
        mRaeVideoLayout.setVideoCover(getString(R.string.demo_cover_url_simple));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RaeVideoView.releaseAll(); // 记得不要忘记释放资源文件了
    }
}
