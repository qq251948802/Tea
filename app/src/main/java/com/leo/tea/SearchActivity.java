package com.leo.tea;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.leo.tea.adapters.ListViewAdapter;
import com.leo.tea.beans.HeaderLines;
import com.leo.tea.uri.Uri;
import com.leo.tea.utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

public class SearchActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {



    private Handler mHandler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case 138:
                    List<HeaderLines> lines= (List<HeaderLines>) msg.obj;
                    data.addAll(lines);
                    mAdapter.notifyDataSetChanged();
                break;

            }
        }

    };

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_search);
            initView();
            initListView();
            initData();
        }

        private void initData() {

            Intent intent = getIntent();

            final String content = intent.getStringExtra("content");

            new Thread(new Runnable() {
                @Override
                public void run() {

                    byte[] res = HttpUtils.parseUriContent(Uri.SEARCH_URL + content);

                    Log.d("flag", "----------->得到的数据为run:1" + new String(res));
                    try {
                        JSONObject jsonObject = new JSONObject(new String(res));


                        JSONArray jsonArray = jsonObject.optJSONArray("data");


                        List<HeaderLines> headerLines = JSON.parseArray(jsonArray.toString(), HeaderLines.class);
                        Log.d("flag", "----------->得到的数据为run2:" + headerLines.toString());
                        mHandler.sendMessage(Message.obtain(mHandler, 138, headerLines));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }


        private BaseAdapter mAdapter;
        private List<HeaderLines> data = new ArrayList<>();

        private void initListView() {
            mAdapter = new ListViewAdapter(SearchActivity.this, data);
            mListView.setAdapter(mAdapter);
            mListView.setOnItemClickListener(this);
        }

        private ListView mListView;
        private ImageView mImageView;
        private void initView() {
            mListView = (ListView) findViewById(R.id.search_listView);
            mImageView= (ImageView) findViewById(R.id.back);
            mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SearchActivity.this.finish();
                }
            });
        }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
       // Toast.makeText(this,"positon"+position,Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, DetailActivity.class);

        intent.putExtra("HeaderLines", data.get(position));
        intent.putExtra("path", Uri.CONTENT_URL + data.get(position).getId());

        this.startActivity(intent);
    }
}
