package com.rae.videodemo;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.rae.videodemo.activity.BaseActivity;
import com.rae.videodemo.activity.SimpleDemoActivity;


public class MainActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        ListView listView = (ListView) findViewById(R.id.list_view);
        DemoListViewAdapter adapter = new DemoListViewAdapter(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(adapter);
    }

    class DemoListViewAdapter extends ArrayAdapter<String> implements AdapterView.OnItemClickListener {
        private String[] mDataList = new String[]{
                "简单版播放器"
        };

        private Class<?>[] mIntentList = new Class[]{
                SimpleDemoActivity.class
        };

        public DemoListViewAdapter(Context context) {
            super(context, R.layout.item_demo, R.id.tv_demo_item_title);
            addAll(mDataList);
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Class<?> cls = mIntentList[position % mIntentList.length];
            startActivity(new Intent(getContext(), cls));
        }
    }


}
