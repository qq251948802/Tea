package com.leo.tea;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.leo.tea.beans.HeaderLines;
import com.leo.tea.utils.CollectionHelper;
import com.leo.tea.utils.HttpUtils;
import com.leo.tea.utils.SQLiteUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){

                case 123:

                    String data= (String) msg.obj;

                    mWebView.loadDataWithBaseURL(null,data, "text/html", "UTF-8",null);



                break;


            }

        }
    };
    private HeaderLines mHeaderLines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initView();
        Intent intent = getIntent();
        String path = intent.getStringExtra("path");
        mHeaderLines = (HeaderLines) intent.getSerializableExtra("HeaderLines");
        setViewData(mHeaderLines);
        initWebView();
        getResourceData(path);
    }

    private void setViewData(HeaderLines headerLines) {

        detailTitle.setText(headerLines.getTitle());
        detailSource.setText(headerLines.getSource());
        detailNickname.setText(headerLines.getNickname());
        detailTime.setText(headerLines.getCreate_time());


    }

    private void getResourceData(final String path) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                byte[] web = HttpUtils.parseUriContent(path);
                try {
                    JSONObject jsonObject=new JSONObject(new String(web));

                    jsonObject= jsonObject.optJSONObject("data");

                    String webContent = jsonObject.optString("wap_content");

                    mHandler.sendMessage(Message.obtain(mHandler,123,webContent));
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        }).start();




    }

    private void initWebView() {

        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient());

        mWebView.getSettings().setJavaScriptEnabled(true);

        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);

        mWebView.getSettings().setDatabaseEnabled(true);
        mWebView.getSettings().setDefaultTextEncodingName("UTF-8");

    }

    private WebView mWebView;
    private TextView detailTitle,detailSource,detailNickname,detailTime;
    private ImageView detail_collectcontent,detail_back;
    private void initView() {

        detailTitle= (TextView) findViewById(R.id.detailTitle);
        detailSource= (TextView) findViewById(R.id.detailSource);
        detailNickname= (TextView) findViewById(R.id.detailNickname);
        detailTime= (TextView) findViewById(R.id.detailTime);
        detail_back= (ImageView) findViewById(R.id.detail_back);
        mWebView= (WebView) findViewById(R.id.webView);

        detail_collectcontent= (ImageView) findViewById(R.id.detail_collectcontent);
        detail_collectcontent.setOnClickListener(this);
        detail_back.setOnClickListener(this);
    }

    private SQLiteUtils dbHelper;
    private SQLiteDatabase db;
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.detail_collectcontent:
               dbHelper=new SQLiteUtils(this);
                db=dbHelper.getReadableDatabase();
                ContentValues values=new ContentValues();
                values.put("_id",mHeaderLines.getId());
                values.put("title",mHeaderLines.getTitle());
                values.put("source",mHeaderLines.getSource());
                values.put("description",mHeaderLines.getDescription());
                values.put("wap_thumb",mHeaderLines.getWap_thumb());
                values.put("create_time",mHeaderLines.getCreate_time());
                values.put("nickname",mHeaderLines.getNickname());
                db.insert("collection",null,values);
                db.close();

                Toast.makeText(this,"收藏成功",Toast.LENGTH_SHORT).show();

            break;
            case R.id.detail_back:

                DetailActivity.this.finish();
                break;

        }
    }
}
