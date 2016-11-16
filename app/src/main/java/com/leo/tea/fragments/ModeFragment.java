package com.leo.tea.fragments;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.LoadingLayoutProxy;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.leo.tea.DetailActivity;
import com.leo.tea.MainActivity;
import com.leo.tea.R;
import com.leo.tea.adapters.ListViewAdapter;
import com.leo.tea.adapters.MyPagerAdapter;
import com.leo.tea.asynctasks.ImageAsyncTask;
import com.leo.tea.beans.HeaderImage;
import com.leo.tea.beans.HeaderLines;
import com.leo.tea.uri.Uri;
import com.leo.tea.utils.IsNetConntectedUtils;
import com.leo.tea.utils.JSONUtils;
import com.leo.tea.utils.MyLruCache;
import com.leo.tea.utils.SDUtils;
import com.leo.tea.utils.SQLiteUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import static com.leo.tea.utils.HttpUtils.parseUriContent;
import static com.leo.tea.utils.SDUtils.getByte;

/**
 * Created by my on 2016/11/11.
 */
public class ModeFragment extends Fragment implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private View mHeaderView, mFootView;

    private List<HeaderImage> headerImageLists = new ArrayList<>();

    private MyLruCache mMyLruCache;

    private SQLiteDatabase db;

    private SQLiteUtils dbHelper;
    //private int currentPosition = 0;
    private int currentPosition = Integer.MAX_VALUE/2;


    public ModeFragment() {
        int MaxSize= (int) (Runtime.getRuntime().maxMemory()/8);
        mMyLruCache=new MyLruCache(MaxSize);
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case 168:
                    mHeaderLines.addAll((List<HeaderLines>) msg.obj);
                    mAdapter.notifyDataSetChanged();


                    if(mListViewPull.isRefreshing()){
                        //暂停刷新
                        mListViewPull.onRefreshComplete();

                        String time = DateUtils.formatDateTime(getContext(),
                                System.currentTimeMillis(),
                                DateUtils.FORMAT_ABBREV_TIME | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE);
                        mLoadingLayoutProxy.setLastUpdatedLabel(time);
                    }
                    break;
                case 169:
                    if(mListViewPull.isRefreshing()){
                        //暂停刷新
                        mListViewPull.onRefreshComplete();

                        String time = DateUtils.formatDateTime(getContext(),
                                System.currentTimeMillis(),
                                DateUtils.FORMAT_ABBREV_TIME | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE);
                        mLoadingLayoutProxy.setLastUpdatedLabel(time);
                    }

                    break;
                case 127:

                    headerImageLists = (List<HeaderImage>) msg.obj;

                    for (int i = 0; i < headerImageLists.size(); i++) {

                        final HeaderImage headerImage = headerImageLists.get(i);

                        Bitmap bitmap=getCachePic(headerImage.getImage());
                        ImageView imageView = new ImageView(getContext());
                        if (bitmap != null) {
                            imageView.setImageBitmap(bitmap);
                            headerImg.add(imageView);
                            imgAdapter.notifyDataSetChanged();
                        }else{
                            if(IsNetConntectedUtils.isNetConntected(getContext())){

                                new ImageAsyncTask(new ImageAsyncTask.ImageCallback() {
                                    @Override
                                    public void callback(byte[] bitmap) {
                                        Log.d("flag", "----------->得到的数据为callback:tupian" + bitmap.length);
                                        ImageView imageView = new ImageView(getContext());

                                        Bitmap bm = BitmapFactory.decodeByteArray(bitmap, 0, bitmap.length);
                                        imageView.setImageBitmap(bm);


                                        mMyLruCache.put(headerImage.getImage().replaceAll("/","").replaceAll(":",""),bm);

                                        String root=getContext().getExternalCacheDir().getAbsolutePath();

                                        String fileName=headerImage.getImage().replaceAll("/","").replaceAll(":","");

                                        SDUtils.saveFile(root,fileName,bitmap);

                                        headerImg.add(imageView);


                                          imgAdapter.notifyDataSetChanged();
                                    }
                                }).execute(headerImage.getImage());
                            }

                        }


                      //  imgAdapter.notifyDataSetChanged();
                    }

                    break;
                case 1:
                    if (this.hasMessages(1)) {
                        this.removeMessages(1);
                    }
                    currentPosition++;
                    mViewPager.setCurrentItem(currentPosition);
                    this.sendEmptyMessageDelayed(1,3000);
                   /* if (currentPosition > 2 ||currentPosition==0) {
                        currentPosition = 0;
                        mViewPager.setCurrentItem(currentPosition);
                        pageOne.setImageResource(R.mipmap.page_now);
                        pageTwo.setImageResource(R.mipmap.page);
                        pageThree.setImageResource(R.mipmap.page);
                    } else {
                        if(currentPosition==1){
                            mViewPager.setCurrentItem(currentPosition);
                            pageTwo.setImageResource(R.mipmap.page_now);
                            pageOne.setImageResource(R.mipmap.page);
                            pageThree.setImageResource(R.mipmap.page);
                        }else{
                            mViewPager.setCurrentItem(currentPosition);
                            pageThree.setImageResource(R.mipmap.page_now);
                            pageOne.setImageResource(R.mipmap.page);
                            pageTwo.setImageResource(R.mipmap.page);
                        }

                    }*/
                   // this.sendEmptyMessageDelayed(1, 3000);
                    break;
                case 2:

                    if(this.hasMessages(1)){
                        //移除message,自动切换就会停止
                        this.removeMessages(1);
                    }
                    break;
                case 3:

                    //手滑动的时候，页码变，需要重新赋值
                    currentPosition=msg.arg1;
                    this.sendEmptyMessageDelayed(1,3000);

                    break;

            }
        }


    };

    private Bitmap getCachePic(String image) {

        Bitmap bitmap=mMyLruCache.get(image.replaceAll("/","").replaceAll(":",""));

        if (bitmap != null) {

            return bitmap;

        }else{
            String root=getContext().getExternalCacheDir().getAbsolutePath();

            String fileName=image.replaceAll("/","").replaceAll(":","");

            byte[] bt=SDUtils.getByte(root+File.separator+fileName);
            if (bt != null) {

                return BitmapFactory.decodeByteArray(bt, 0, bt.length);
            }
        }
        return null;
    }

    private ViewPager mViewPager;

    private TextView more;

    private void initFootView(View footView) {

        more = (TextView) footView.findViewById(R.id.moreBottom);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = getArguments();
                mPager = bundle.getInt("pager");
                switch (mPager) {
                    case 0:
                        index++;
                        initData(Uri.HEADLINE_URL + Uri.HEADLINE_TYPE);
                        break;
                }
            }
        });


    }

    private int mPager;

    private void initRouces() {

        String root=getContext().getExternalCacheDir().getAbsolutePath();
        String fileName=Uri.HEADERIMAGE_URL.replaceAll("/","").replaceAll(":","");
        byte[] bt= getByte(root+File.separator+fileName);

        if (bt != null) {
            List<HeaderImage> headerImageList = JSONUtils.parseData(new String(bt));

            Message msg = Message.obtain();
            msg.what = 127;

            msg.obj = headerImageList;
            mHandler.sendMessage(msg);



        }else {
            Log.d("flag", "----------->得到的数据为initRouces:联网状态" +IsNetConntectedUtils.isNetConntected(getContext()));
            if (IsNetConntectedUtils.isNetConntected(getContext())) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        byte[] res = parseUriContent(Uri.HEADERIMAGE_URL);

                        String root = getContext().getExternalCacheDir().getAbsolutePath();
                        Log.d("flag", "----------->得到的数据为run:root" +root);
                        String fileName = Uri.HEADERIMAGE_URL.replaceAll("/", "").replaceAll(":", "");
                        Log.d("flag", "----------->得到的数据为run:fileName" +fileName);
                        //保存到SD卡
                        SDUtils.saveFile(root, fileName, res);


                        List<HeaderImage> headerImageList = JSONUtils.parseData(new String(res));

                        Message msg = Message.obtain();
                        msg.what = 127;

                        msg.obj = headerImageList;
                        mHandler.sendMessage(msg);

                    }
                }).start();


            }
        }
    }

    private PullToRefreshListView mListViewPull;
    private List<HeaderLines> mHeaderLines = new ArrayList<>();
    private BaseAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View ret = null;
        ret = LayoutInflater.from(getContext()).inflate(R.layout.fragment_item, container, false);

        initView(ret);


        return ret;
    }

    ListView mListView;
    private int index = 1;
    private ImageView backtop;

    private void initView(View ret) {

        backtop = (ImageView) ret.findViewById(R.id.backtop);
        backtop.setOnClickListener(this);

        mListViewPull = (PullToRefreshListView) ret.findViewById(R.id.listView);

        mListView = mListViewPull.getRefreshableView();

        Bundle bundle = getArguments();
        mPager = bundle.getInt("pager");
        Log.d("flag", "----------->得到的数据为initView:" + mPager);
        switch (mPager) {
            case 0:
                initRouces();
                initListView(mPager);
                initData(Uri.HEADLINE_URL + Uri.HEADLINE_TYPE);
                break;
            case 1:
                initListView(mPager);
                initData(Uri.BASE_URL + Uri.CYCLOPEDIA_TYPE);
                break;
            case 2:
                initListView(mPager);
                initData(Uri.BASE_URL + Uri.CONSULT_TYPE);
                break;
            case 3:
                initListView(mPager);
                initData(Uri.BASE_URL + Uri.OPERATE_TYPE);
                break;
            case 4:
                initListView(mPager);
                initData(Uri.BASE_URL + Uri.DATA_TYPE);
                break;
        }
    }

    private LoadingLayoutProxy mLoadingLayoutProxy;

    private void initLoadingLayout(PullToRefreshListView listViewPull) {
        mLoadingLayoutProxy = (LoadingLayoutProxy) listViewPull.getLoadingLayoutProxy();

        mLoadingLayoutProxy.setPullLabel("使劲拉……");

        mLoadingLayoutProxy.setReleaseLabel("快了……");

        mLoadingLayoutProxy.setRefreshingLabel("拼命加载中……");

        String time = DateUtils.formatDateTime(getContext(),
                System.currentTimeMillis(),
                DateUtils.FORMAT_ABBREV_TIME | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE);
        mLoadingLayoutProxy.setLastUpdatedLabel(time);
    }

    private void initListView(int pager) {
        if (pager == 0) {

            mHeaderView = LayoutInflater.from(getContext()).inflate(R.layout.header_item, mListView, false);

            initHeaderViewPager(mHeaderView);

            mListView.addHeaderView(mHeaderView);

            mFootView = LayoutInflater.from(getContext()).inflate(R.layout.foot_item, mListView, false);

            initFootView(mFootView);
            mListView.addFooterView(mFootView);
        }
        //TODO
        mAdapter = new ListViewAdapter(getContext(), mHeaderLines);

        mListView.setAdapter(mAdapter);
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setIcon(R.mipmap.icon_dialog);
                builder.setTitle("是否删除");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        TranslateAnimation animation=new TranslateAnimation(
                                Animation.RELATIVE_TO_SELF,0,
                                Animation.RELATIVE_TO_SELF,-1,//移动自身的宽度，参数二view
                                Animation.RELATIVE_TO_SELF,0,
                                Animation.RELATIVE_TO_SELF,0
                        );

                        animation.setDuration(2000);
                       // animation.setFillAfter(true);
                        view.startAnimation(animation);

                        animation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                               //if(mPager==0){

                                   mHeaderLines.remove(position-2);

                              // }else{
                                //   mHeaderLines.remove(position-1);
                                  // mAdapter.notifyDataSetChanged();
                            //  }

                                mAdapter.notifyDataSetChanged();
                                int count=mListView.getCount();
                                Log.d("flag", "----------->得到的数据为onAnimationEnd:listView lenth" +count);

                                TranslateAnimation translateAnimation =new TranslateAnimation(
                                        Animation.RELATIVE_TO_SELF,0,
                                        Animation.RELATIVE_TO_SELF,0,//移动自身的宽度，参数二view
                                        Animation.RELATIVE_TO_SELF,1,
                                        Animation.RELATIVE_TO_SELF,0);

                                translateAnimation.setDuration(2000);


                                int currentTop=view.getTop();
                                for (int i = 0; i < count; i++) {


                                    View itemView=mListView.getChildAt(i);
                              //      Log.d("flag", "----------->得到的数据为onAnimationEnd:" +itemView.toString());
                                    if(itemView!=null){

                                        if (itemView.getTop()>=currentTop){

                                            itemView.setAnimation(translateAnimation);

                                        }
                                    }




                                }


                                }



                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });



                    }
                });
                builder.setNegativeButton("取消", null);

                builder.show();


                return true;
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HeaderLines headerLines = mHeaderLines.get(position-2);
                dbHelper = new SQLiteUtils(getContext());
                db = dbHelper.getReadableDatabase();

                ContentValues values = new ContentValues();
                values.put("_id", headerLines.getId());
                values.put("title", headerLines.getTitle());
                values.put("source", headerLines.getSource());
                values.put("description", headerLines.getDescription());
                values.put("wap_thumb", headerLines.getWap_thumb());
                values.put("create_time", headerLines.getCreate_time());
                values.put("nickname", headerLines.getNickname());
                db.insert("info", null, values);
                db.close();


                Intent intent = new Intent(getContext(), DetailActivity.class);
                intent.putExtra("HeaderLines", headerLines);
                intent.putExtra("path", Uri.CONTENT_URL + headerLines.getId());

                getContext().startActivity(intent);
            }

        });

        initLoadingLayout(mListViewPull);
        mListViewPull.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mHandler.sendEmptyMessage(169);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                index++;
                initData(Uri.HEADLINE_URL + Uri.HEADLINE_TYPE);
            }
        });
        mListViewPull.setMode(PullToRefreshListView.Mode.BOTH);

    }


    private TextView textHeader;
    private List<ImageView> headerImg = new ArrayList<>();
    private PagerAdapter imgAdapter;

    private ImageView pageOne,pageTwo,pageThree;
    private void initHeaderViewPager(View headerView) {

        mViewPager = (ViewPager) headerView.findViewById(R.id.headerViewPager);
        pageOne= (ImageView) headerView.findViewById(R.id.pageOne);
        pageTwo= (ImageView) headerView.findViewById(R.id.pageTwo);
        pageThree= (ImageView) headerView.findViewById(R.id.pageThree);




        imgAdapter = new MyPagerAdapter(headerImg, getContext());

        mViewPager.setAdapter(imgAdapter);
        mViewPager.setCurrentItem(Integer.MAX_VALUE/2);
        mHandler.sendEmptyMessageDelayed(1, 3000);
        mViewPager.addOnPageChangeListener(this);

        textHeader = (TextView) headerView.findViewById(R.id.textHeader);

        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }



    private void initData(final String modePath) {

        String root=getContext().getExternalCacheDir().getAbsolutePath();
        String fileName=modePath.replaceAll("/","").replaceAll(":","");
        byte[] bt= getByte(root+ File.separator+fileName+index);
        if (bt != null) {

            try {
                JSONObject jsonObject = new JSONObject(new String(bt));

                JSONArray jsonArray = jsonObject.optJSONArray("data");

                List<HeaderLines> headerLines = JSON.parseArray(jsonArray.toString(), HeaderLines.class);
                mHandler.sendMessage(Message.obtain(mHandler, 168, headerLines));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }else {
            if (IsNetConntectedUtils.isNetConntected(getContext())) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        byte[] res = parseUriContent(modePath + index);

                        String root = getContext().getExternalCacheDir().getAbsolutePath();

                        String fileName = (modePath).replaceAll("/", "").replaceAll(":", "")+index;
                        SDUtils.saveFile(root, fileName, res);

                        try {
                            JSONObject jsonObject = new JSONObject(new String(res));

                            JSONArray jsonArray = jsonObject.optJSONArray("data");

                            List<HeaderLines> headerLines = JSON.parseArray(jsonArray.toString(), HeaderLines.class);
                            mHandler.sendMessage(Message.obtain(mHandler, 168, headerLines));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }).start();
            }
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backtop:
                mListView.setSelection(0);
                break;

        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        mHandler.sendMessage(Message.obtain(mHandler,3,position,0));
        if(headerImageLists!=null){

            textHeader.setText(headerImageLists.get(position%3).getTitle());
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {
            switch (state){

                case  ViewPager.SCROLL_STATE_DRAGGING://手动拖拽
                    mHandler.sendEmptyMessage(2);
                    break;
            }
    }
}
