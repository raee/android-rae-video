package com.rae.videodemo.activity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.rae.ui.widget.RaeVideoLayout;
import com.rae.ui.widget.RaeVideoView;
import com.rae.videodemo.R;

/**
 * Created by ChenRui on 2016/10/18 下午6:12.
 */
public class ListViewDemoActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.at_list_view;
    }

    @Override
    protected void initView() {
        ListView listView = (ListView) findViewById(R.id.list);
        ListViewDemoAdapter adapter = new ListViewDemoAdapter(this);
        adapter.addAll(readerDemoDataJson());
        listView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RaeVideoView.releaseAll(); // 不要忘记这句代码哦
    }


    class ListViewDemoAdapter extends ArrayAdapter<ListViewDemoViewModel> {

        public ListViewDemoAdapter(Context context) {
            super(context, 0);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            RaeVideoLayout layout;
            if (convertView == null) {
                layout = new RaeVideoLayout(parent.getContext(), null);
                convertView = layout;
            } else {
                layout = (RaeVideoLayout) convertView;
            }


            ListViewDemoViewModel item = getItem(position);
            layout.setVideoPath(item.videoPath);
            layout.setVideoCover(item.videoCover);

            return convertView;
        }
    }

}
