package com.leo.tea;

        import android.content.Intent;
        import android.os.Handler;
        import android.os.Message;
        import android.support.design.widget.TabLayout;
        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentPagerAdapter;
        import android.support.v4.view.ViewPager;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.TextView;
        import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
        import com.leo.tea.adapters.MainPagerAdapter;
        import com.leo.tea.fragments.ModeFragment;
        import java.util.ArrayList;
        import java.util.List;



public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        initView();
        initTab();
        initViewPager();
        initSlidingMenu();


    }
    private SlidingMenu mSlidingMenu;
    private Button search_btn;
    private EditText search_content;
    private TextView menu_collection;
    private TextView menu_history;
    private void initSlidingMenu() {
        mSlidingMenu=new SlidingMenu(this);
        mSlidingMenu.attachToActivity(this,SlidingMenu.SLIDING_CONTENT);
        mSlidingMenu.setMode(SlidingMenu.RIGHT);
        mSlidingMenu.setSecondaryMenu(R.layout.right_menu);
        //占多大屏幕
        mSlidingMenu.setBehindOffset(getResources().getDisplayMetrics().widthPixels/3);
        mSlidingMenu.setBehindScrollScale(1);
        mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);

        menu_collection= (TextView) mSlidingMenu.findViewById(R.id.menu_collection);

        menu_collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(MainActivity.this,CollectionActivity.class);

                MainActivity.this.startActivity(intent);

            }
        });

        menu_history= (TextView) mSlidingMenu.findViewById(R.id.menu_history);
        menu_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,HistoryActivity.class);

                MainActivity.this.startActivity(intent);
            }
        });

        search_btn= (Button) mSlidingMenu.findViewById(R.id.menu_search);
        search_content= (EditText) mSlidingMenu.findViewById(R.id.search_content);

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String content = search_content.getText().toString().trim();

                Intent intent=new Intent(MainActivity.this,SearchActivity.class);

                intent.putExtra("content",content);

                MainActivity.this.startActivity(intent);

            }
        });

        mImageView= (ImageView) mSlidingMenu.findViewById(R.id.toggle);

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSlidingMenu.isMenuShowing()){
                    mSlidingMenu.toggle();
                }
            }
        });

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mSlidingMenu.isMenuShowing()){
                    mSlidingMenu.toggle();
                }
            }
        });

    }

    List<Fragment> mFragmentList=new ArrayList<>();
    private void initViewPager() {

        for (int i = 0; i < titles.length; i++) {

            Fragment fragment=new ModeFragment();

            Bundle bundle=new Bundle();

            bundle.putInt("pager",i);

            fragment.setArguments(bundle);


            mFragmentList.add(fragment);



        }
        FragmentPagerAdapter adapter=new MainPagerAdapter(getSupportFragmentManager(),mFragmentList);

        mViewPager.setAdapter(adapter);
    }

    private String[] titles={"头条","百科","资讯","经营","数据"};
    private void initTab() {

        for (int i = 0; i <titles.length; i++) {
            TabLayout.Tab tab=mTabLayout.newTab();
            tab.setText(titles[i]);
            mTabLayout.addTab(tab);
        }
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        mTabLayout.setupWithViewPager(mViewPager);


    }

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private ImageView mImageView,more;
    private void initView() {
        more= (ImageView) findViewById(R.id.more);
        mTabLayout= (TabLayout) findViewById(R.id.tab);
        mViewPager= (ViewPager) findViewById(R.id.ViewPager);

    }
}
