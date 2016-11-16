package com.leo.tea;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.leo.tea.adapters.ListViewAdapter;
import com.leo.tea.beans.HeaderLines;
import com.leo.tea.uri.Uri;
import com.leo.tea.utils.SQLiteUtils;

import java.util.ArrayList;
import java.util.List;

import static com.leo.tea.R.id.collection_listView;
import static com.leo.tea.R.id.collectionback;

public class HistoryActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        initView();

        initData();
        initListView();
    }
    private void initListView() {

        BaseAdapter adapter=new ListViewAdapter(this,data);

        history_listView.setAdapter(adapter);
        history_listView.setOnItemClickListener(this);
    }

    private SQLiteUtils dbHelper;
    private SQLiteDatabase db;
    private List<HeaderLines> data=new ArrayList<>();
    private void initData() {

        dbHelper=new SQLiteUtils(this);

        db=dbHelper.getReadableDatabase();

        Cursor cursor = db.query("info", null, null, null, null, null, null);
        while(cursor.moveToNext()){
            HeaderLines line=new HeaderLines();
            line.setId(cursor.getString(cursor.getColumnIndex("_id")));
            line.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            line.setSource(cursor.getString(cursor.getColumnIndex("source")));
            line.setNickname(cursor.getString(cursor.getColumnIndex("nickname")));
            line.setCreate_time(cursor.getString(cursor.getColumnIndex("create_time")));
            data.add(line);
        }
        db.close();

    }

    private ImageView historyback;
    private ListView history_listView;
    private void initView() {

        historyback= (ImageView) findViewById(R.id.historyback);
        history_listView= (ListView) findViewById(R.id.history_listView);
        historyback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HistoryActivity.this.finish();
            }
        });


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this,"positon"+position,Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, DetailActivity.class);

        intent.putExtra("HeaderLines", data.get(position));
        intent.putExtra("path", Uri.CONTENT_URL + data.get(position).getId());

        this.startActivity(intent);
    }
}
