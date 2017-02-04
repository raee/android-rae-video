package com.rae.videodemo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.rae.ui.widget.RaeVideoView;
import com.rae.videodemo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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


    // 获取示例数据,不用细看
    protected List<ListViewDemoViewModel> readerDemoDataJson() {
        List<ListViewDemoViewModel> result = new ArrayList<>();

        try {
            InputStream stream = getAssets().open("demo.json");
            BufferedInputStream bs = new BufferedInputStream(stream);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int len = 0;
            byte[] temp = new byte[128];
            while ((len = bs.read(temp)) != -1) {
                out.write(temp, 0, len);
            }
            String json = out.toString();

            bs.close();
            stream.close();
            out.close();

            JSONArray arr = new JSONArray(json);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);

                ListViewDemoViewModel model = new ListViewDemoViewModel(obj.getString("media_path"), obj.getString("media_cover"));
                result.add(model);
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return result;
    }

    protected class ListViewDemoViewModel {
        public String videoPath;
        public String videoCover;

        public ListViewDemoViewModel(String videoPath, String videoCover) {
            this.videoPath = videoPath;
            this.videoCover = videoCover;
        }
    }

}
