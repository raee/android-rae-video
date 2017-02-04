package com.rae.videodemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.rae.videodemo.activity.BaseActivity;
import com.rae.videodemo.activity.FullScreenVideoDemoActivity;
import com.rae.videodemo.activity.ListViewDemoActivity;
import com.rae.videodemo.activity.RaeVideoLayoutDemoActivity;
import com.rae.videodemo.activity.SimpleVideoDemoActivity;
import com.rae.videodemo.activity.SingleVideoDemoActivity;


public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        showBackUp(false);
        ListView listView = (ListView) findViewById(R.id.list_view);
        DemoListViewAdapter adapter = new DemoListViewAdapter(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(adapter);
    }

    class DemoListViewHolder {
        public String name;
        public Class<?> cls;

        public DemoListViewHolder(String name, Class<?> cls) {
            this.name = name;
            this.cls = cls;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    class DemoListViewAdapter extends ArrayAdapter<DemoListViewHolder> implements AdapterView.OnItemClickListener {
        private DemoListViewHolder[] mDataList = new DemoListViewHolder[]{
                new DemoListViewHolder("单播放器版", SingleVideoDemoActivity.class),
                new DemoListViewHolder("简单版播放器", SimpleVideoDemoActivity.class),
                new DemoListViewHolder("全屏播放器", FullScreenVideoDemoActivity.class),
                new DemoListViewHolder("标准版播放器(带封面图)", RaeVideoLayoutDemoActivity.class),
                new DemoListViewHolder("ListView", ListViewDemoActivity.class)
        };

        public DemoListViewAdapter(Context context) {
            super(context, R.layout.item_demo, R.id.tv_demo_item_title);
            addAll(mDataList);
        }


        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            DemoListViewHolder data = mDataList[position % mDataList.length];
            Class<?> cls = data.cls;
            Intent intent = new Intent(getContext(), cls);
            intent.putExtra("title", data.name);
            startActivity(intent);
        }
    }


}
