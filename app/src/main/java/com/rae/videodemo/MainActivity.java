package com.rae.videodemo;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.rae.videodemo.activity.BaseActivity;
import com.rae.videodemo.activity.BasicVideoDemoActivity;
import com.rae.videodemo.activity.SingleVideoDemoActivity;
import com.rae.videodemo.activity.SimpleVideoDemoActivity;


public class MainActivity extends BaseActivity {

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
                new DemoListViewHolder("基础版播放器", BasicVideoDemoActivity.class)
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
