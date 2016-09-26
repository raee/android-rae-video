package com.rae.videodemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.rae.ui.widget.RaeVideoView;
import com.rae.ui.media.controller.SimpleMediaController;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RaeVideoView mVideoView;
    private Button mBtnPlay;
    private Button mBtnAudio;
    private RaeVideoView mVideoView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtnPlay = (Button) findViewById(R.id.btn_play);
        mBtnAudio = (Button) findViewById(R.id.btn_audio);
        mBtnAudio.setOnClickListener(this);
        mBtnPlay.setOnClickListener(this);

        mVideoView = (RaeVideoView) findViewById(R.id.video);
        mVideoView2 = (RaeVideoView) findViewById(R.id.video2);
        mVideoView.setVideoPath("http://svideo.spriteapp.com/video/2016/0923/57e507c8f16d4_wpd.mp4");
        mVideoView.start();
        SimpleMediaController mediaController = new SimpleMediaController(this);
//        mVideoView.setMediaController(new AndroidMediaController(this));
        mVideoView.setMediaController(mediaController);
        mediaController.attach(mVideoView);
        mBtnPlay.setText("暂停");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_audio:
//                if (mBtnAudio.getText().equals("静音")) {
//                    mVideoView.setAudioEnable(false);
//                    mBtnAudio.setText("取消静音");
//                } else {
//                    mVideoView.setAudioEnable(true);
//                    mBtnAudio.setText("静音");
//                }
                break;
            case R.id.btn_play:
                mVideoView2.toggle();

//                if (mBtnPlay.getText().equals("播放")) {
//                    mVideoView.start();
//                    mBtnPlay.setText("暂停");
//                } else {
//                    mVideoView.pause();
//                    mBtnPlay.setText("播放");
//                }
                break;
        }
    }


}
