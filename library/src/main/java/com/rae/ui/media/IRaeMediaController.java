package com.rae.ui.media;

import android.view.View;

/**
 * 扩展控制器
 * Created by ChenRui on 2016/10/19 下午12:41.
 */
public interface IRaeMediaController {
    void setMediaPlayer(IRaeMediaPlayerController player);

    View getView();

    void onDestroy();

    void show();

    void hide();
}
