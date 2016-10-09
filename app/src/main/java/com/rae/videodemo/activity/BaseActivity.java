package com.rae.videodemo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.rae.ui.widget.RaeVideoView;
import com.rae.videodemo.R;

/**
 * Created by ChenRui on 2016/9/26 0026 11:48.
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected RaeVideoView mVideoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        // Activity标题
        String title = getIntent().getStringExtra("title");
        if (title != null) {
            setTitle(title);
        }

        showBackUp(true);

        mVideoView = (RaeVideoView) findViewById(R.id.rae_video); // 默认播放器的ID都是这个
        initView();

    }

    // 显示返回按钮
    protected void showBackUp(boolean showed) {
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(showed);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    protected abstract int getLayoutId();

    protected abstract void initView();

}
