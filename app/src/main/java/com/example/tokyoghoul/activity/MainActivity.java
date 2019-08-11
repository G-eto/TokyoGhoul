package com.example.tokyoghoul.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.style.FontStyle;
import com.example.tokyoghoul.R;
import com.example.tokyoghoul.database.DatabaseHelper;
import com.example.tokyoghoul.database.model.CommunityManage;
import com.example.tokyoghoul.database.model.Logs;
import com.example.tokyoghoul.view.FirstFragment;
import com.example.tokyoghoul.view.LineChartDiamonds;
import com.example.tokyoghoul.view.MyAccountFragment;
import com.example.tokyoghoul.view.MyCommunityFragment;
import com.example.tokyoghoul.view.MyFragmentPagerAdapter;
import com.example.tokyoghoul.view.MyRoleFragment;
import com.example.tokyoghoul.view.SecondFragment;
import com.example.tokyoghoul.view.TabChange;
import com.example.tokyoghoul.view.ThirdFragment;
import com.github.mikephil.charting.charts.LineChart;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.tiagohm.markdownview.MarkdownView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private TabLayout.Tab tabAtOne;
    private TabLayout.Tab tabAttwo;
    private TabLayout.Tab tabAtthree;
    private TabLayout.Tab tabAtfour;
    private int[] unSelectTabRes = new int[]{R.drawable.ic_menu_camera
            , R.drawable.ic_menu_gallery,
            R.drawable.ic_menu_send,
            R.drawable.ic_menu_share};
    //选中的Tab图片
    private int[] selectTabRes = new int[]{R.drawable.ic_menu_manage,
            R.drawable.ic_menu_share
            , R.drawable.ic_menu_send, R.drawable.ic_menu_manage};
    //Tab标题
    private String[] title = new String[]{"首页", "娱乐", "游戏", "我的"};
    private String[] way = new String[]{"record", "psp", "play", "cdk"};

    private ViewPager viewPager_community;
    private TabLayout tabLayout_community;
    List<Fragment> fragments_community = new ArrayList<>();
    String[] tabTitles_community = new String[]{"角色速查", "社团管理", "我的小号"};

    private TextView today_time;
    private TabHost tabHost;
    private SmartTable table, table2;
    private LineChart lineChart;

    DatabaseHelper db;
    Context context;

    //钻石
    Button diamondAdd;
    EditText diamondInput;
    final List<Logs> list = new ArrayList<>();

    //社团
    Button communityBtn;
    EditText communityId;
    final List<CommunityManage> list_community = new ArrayList<>();

    //tool
    private RelativeLayout tool_record;
    private RelativeLayout tool_rune;
    private RelativeLayout tool_psp;
    private RelativeLayout tool_play;
    private RelativeLayout tool_story;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        db = new DatabaseHelper(this);
        context = this.getBaseContext();
        //today_time = findViewById(R.id.today_time);
        tabHost = (TabHost) findViewById(R.id.tabhost);
        tabHost.setup();
        LayoutInflater.from(this).inflate(R.layout.content_main,
                tabHost.getTabContentView(), false);


        // 添加第一个标签页
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("社团")
                .setContent(R.id.table_main));
        // 添加第二个标签页
        tabHost.addTab(tabHost
                .newTabSpec("tab2")
                // 在标签标题上放置图标
                .setIndicator("钻石日志",
                        ContextCompat.getDrawable(this, R.drawable.ic_menu_share))
                .setContent(R.id.table_diamonds));
        // 添加第三个标签页
        tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("记录")
                .setContent(R.id.table_tools));

        tabHost.setCurrentTab(1);
        /*设置标签切换监听器*/
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                //颜色全部重置
                Log.d("dgg",String.valueOf(tabHost.getCurrentTab()));
              }
        });

        list.addAll(db.getAllLogs("DESC"));
        list_community.addAll(db.getAllCommunity());
        //折线图
        lineChart = findViewById(R.id.spread_line_chart);
        final LineChartDiamonds diamondsshow = new LineChartDiamonds(lineChart, this, list);

        table = findViewById(R.id.table_record);
        table.getConfig().setShowXSequence(false);
        table.setData(list);
        table.getConfig().setContentStyle(new FontStyle(50, Color.BLACK));

//        table2 = findViewById(R.id.table_community);
//        table2.getConfig().setShowXSequence(false);
//        table2.getConfig().setShowYSequence(false);
//        table2.setData(list_community);
//        table2.getConfig().setContentStyle(new FontStyle(50, Color.BLUE));

        diamondInput = findViewById(R.id.diamonds_input);
        diamondAdd = findViewById(R.id.diamonds_input_btn);
        diamondAdd.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(diamondInput.getText().toString().length() > 0) {
                    int input = Integer.parseInt(diamondInput.getText().toString());

                    Calendar c = Calendar.getInstance();
                    int year = c.get(Calendar.YEAR);   //获取年份
                    int month = c.get(Calendar.MONTH) + 1;  //获取月份
                    int day = c.get(Calendar.DAY_OF_MONTH);  //获取日期
                    int hour = c.get(Calendar.HOUR_OF_DAY);  //获取小时
                    int minute = c.get(Calendar.MINUTE); //获取分钟
                    int second = c.get(Calendar.SECOND);//秒
                    String date = year+"/"+String.format("%02d",month)+"/"+String.format("%02d",day);
                    int newid = db.getLogCount();
                    Logs bufLog = new Logs();
                    Logs bufLog1 = new Logs();
                    Logs log = new Logs();
                    log.setDiamondsAll(input);
                    log.setDate(date);
                    if(newid > 0) {
                        bufLog = db.getLog(newid);

                        if (bufLog.getDate().compareTo(date) < 0) {
                        //if (DateUtil.CompareDateString(bufLog.getDate(), date) < 0) {
                            log.setDiamondsIncome(input - bufLog.getDiamondsAll());
                            db.insertLog(log);
                        } else if (bufLog.getDate().compareTo(date) == 0) {
                            if (newid - 1 > 0) {
                                bufLog1 = db.getLog(newid - 1);
                                bufLog.setDiamondsIncome(input - bufLog1.getDiamondsAll());
                            } else {
                                bufLog.setDiamondsIncome(0);
                            }
                            bufLog.setDiamondsAll(input);
                            db.updateLog(bufLog);
                        } else {
                            //danchukuang
                        }
                    }
                    else{
                        log.setDiamondsIncome(0);
                        db.insertLog(log);
                    }
                }
                else{
                    //弹出提示
//                    new Thread(new Runnable() {
//                        public void run() {
//                                System.out.println("bbbbbbb");
//                                DatabaseHelper.initRoleTable(context);
//                        }
//                    }).start();

                }
                //Log.d("sfs",list.get(list.size()-1).getDiamondsAll()+" ");
                list.clear();
                //table.setData(list);
                list.addAll(db.getAllLogs("DESC"));
                table.setData(list);
                table.notifyDataChanged();
                table.setMinimumHeight(200);
                diamondsshow.refresh();
                lineChart.refreshDrawableState();
            }
        });

//        communityBtn = findViewById(R.id.today_quite_btn);
//        communityId = findViewById(R.id.today_community_id);
//        communityBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int input = -1;
//                if(communityId.getText().toString().length() > 0)
//                    input = Integer.parseInt(communityId.getText().toString());
//                int newid = db.getLogCount();
//                Logs bufLog = new Logs();
//                Logs log = new Logs();
//
//                Calendar c = Calendar.getInstance();
//                int year = c.get(Calendar.YEAR);   //获取年份
//                int month = c.get(Calendar.MONTH) + 1;  //获取月份
//                int day = c.get(Calendar.DAY_OF_MONTH);  //获取日期
//                int hour = c.get(Calendar.HOUR_OF_DAY);  //获取小时
//                int minute = c.get(Calendar.MINUTE); //获取分钟
//                int second = c.get(Calendar.SECOND);//秒
//                String date = year+"/"+month+"/"+day;
//                String time = String.format("%02d",hour)+":"
//                            + String.format("%02d",minute)+":"
//                            + String.format("%02d",second);
//
//                list.clear();
//                list.addAll(db.getAllLogs());
//                table2.notifyDataChanged();
//                table2.setMinimumHeight(200);
//            }
//        });

        //社团页
        fragments_community.add(new MyRoleFragment());
        fragments_community.add(new MyCommunityFragment());
        fragments_community.add(new MyAccountFragment());

        tabLayout_community = findViewById(R.id.community_tabs);
        viewPager_community = findViewById(R.id.community_content);
        //使用适配器将ViewPager与Fragment绑定在一起
        viewPager_community.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), tabTitles_community, fragments_community));
        //将TabLayout与ViewPager绑定
        tabLayout_community.setupWithViewPager(viewPager_community);
        TabChange community = new TabChange(tabTitles_community, selectTabRes, unSelectTabRes, tabLayout_community, viewPager_community);
        community.initView();
        community.initData();
        community.initListener(title);

        //记录页

        //getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        tool_record = findViewById(R.id.tool_layout_record);
        tool_psp = findViewById(R.id.tool_layout_psp);
        tool_play = findViewById(R.id.tool_layout_play);
        tool_story = findViewById(R.id.tool_layout_story);

        //手记
        tool_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentToListActivity(1);
            }
        });

        //网上大神攻略
        tool_psp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentToListActivity(2);
            }
        });
        //活动
        tool_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentToListActivity(3);
            }
        });
        //同人故事
        tool_story.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentToListActivity(4);
            }
        });
    }

    private void intentToListActivity(int mark){
        Intent intent = new Intent(MainActivity.this,ItemListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("way", "");
        switch(mark) {
            case 1: bundle.clear();
                    bundle.putString("way", way[0]);
                    break;
            case 2: bundle.clear();
                    bundle.putString("way", way[1]);
                    break;
            case 3: bundle.clear();
                    bundle.putString("way", way[2]);
                    break;
            case 4: bundle.clear();
                    bundle.putString("way", way[3]);
                    break;
            default:break;
        }
        intent.putExtras(bundle);
        //传入intent
        startActivity(intent);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float initx = 0, currentx = 0;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                initx = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                currentx = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                /*左右滑动事件处理*/
                if ((currentx - initx) > 25) {
                    if (tabHost.getCurrentTab() != 0) {
                        tabHost.setCurrentTab(tabHost.getCurrentTab() - 1);
                    }
                } else if ((currentx - initx) < -25) {
                    if (tabHost.getCurrentTab() != tabHost.getTabContentView().getChildCount()) {
                        tabHost.setCurrentTab(tabHost.getCurrentTab() + 1);
                    }
                }
                Log.d("dgg",String.valueOf(tabHost.getCurrentTab()));
                break;

        }

        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_you) {

        } else if (id == R.id.nav_protocol) {

        } else if (id == R.id.nav_advice) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_about) {

        }else if (id == R.id.nav_data) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
