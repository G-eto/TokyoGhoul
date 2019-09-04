package com.example.tokyoghoul.activity;

import android.app.SearchManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.SearchView;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.example.tokyoghoul.R;

import com.example.tokyoghoul.activity.dummy.DummyContent;
import com.example.tokyoghoul.database.DatabaseHelper;
import com.example.tokyoghoul.database.model.CDKs;
import com.example.tokyoghoul.database.model.CommunityManage;
import com.example.tokyoghoul.database.model.Psp;
import com.example.tokyoghoul.tool.DateUtil;
import com.example.tokyoghoul.tool.Gadget;
import com.example.tokyoghoul.tool.HtmlService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ItemListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
//    Typeface typeface = Typeface.createFromAsset(getAssets(), "fonnts/materialdesignicons-webfont.ttf");
//        tv_sz1.setTypeface(typeface);
    private boolean mTwoPane;
    private static String mark = "";
    private static int mark_id = -1;
    private String[] way = new String[]{"record", "psp", "play", "cdk", "feedback"};
    private String[] Title = new String[]{"我的手记", "玩家攻略", "活动记录", "兑换码", "反馈管理"};
    DatabaseHelper db;
    public static RecyclerView recyclerView;

    private static String jsondata = "";
    private static String jsondata_psp = "";
    private static String jsondata_play = "";
    private static String jsondata_cdk = "";
    private static String jsondata_feedback = "";

    private static List<DummyContent.DummyItem> cdkBuf= new ArrayList<>();
    private static List<DummyContent.DummyItem> pspBuf= new ArrayList<>();
    private static List<DummyContent.DummyItem> feedback = new ArrayList<>();

    private static ClipboardManager cm;
    private static ClipData mClipData;

    public static SimpleItemRecyclerViewAdapter adapter;

    private static SVProgressHUD tips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        getWindow().setStatusBarColor(Color.parseColor("#008577"));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //toolbar.setTitle(getTitle());
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            mark = extras.getString("way");
        }

        tips = new SVProgressHUD(this);
        //record
        if(mark.equals(way[0])){
            mark_id = 0;
        }
        //psp
        else if(mark.equals(way[1])){
            mark_id = 1;
        }
        //play
        else if(mark.equals(way[2])){
            mark_id = 2;
        }
        //cdk
        else if(mark.equals(way[3])){
            mark_id = 3;
        }
        //feedback
        else if(mark.equals(way[4])){
            mark_id = 4;
        }
        Log.i("markid"," "+mark_id);
        toolbar.setTitle(Title[mark_id]);


        //获取剪贴板管理器：
        cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        db = new DatabaseHelper(this);
//        db.insertPsp(new Psp("手抄测试","类型","2019/8/9 22:29","monkey",
//                "zhehsiyegshk被打开了法国军队进攻和贷款机构和可见光会计法规和"));
        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        //recyclerView.setAdapter(adapter);
        setupRecyclerView((RecyclerView) recyclerView);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(mark_id == 0) {
            getMenuInflater().inflate(R.menu.main, menu);
// Get the SearchView and set the searchable configuration
//            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//            SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
//            // Assumes current activity is the searchable activity
//            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

            SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    DummyContent.ITEMS.clear();
                    List<Psp> list = db.selectPsps(s);
                    for(int i = 0; i < list.size(); i++) {
                        DummyContent.ITEMS.add(new DummyContent.DummyItem(
                                list.get(i).getId()+"", list.get(i).getPsp_title(),
                                list.get(i).getPsp_kind(), list.get(i).getPsp_time(),
                                list.get(i).getPsp_author(), list.get(i).getPsp_text()));
                    }
                    recyclerView.getAdapter().notifyDataSetChanged();
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    DummyContent.ITEMS.clear();
                    List<Psp> list = db.selectPsps(s);
                    for(int i = 0; i < list.size(); i++) {
                        DummyContent.ITEMS.add(new DummyContent.DummyItem(
                                list.get(i).getId()+"", list.get(i).getPsp_title(),
                                list.get(i).getPsp_kind(), list.get(i).getPsp_time(),
                                list.get(i).getPsp_author(), list.get(i).getPsp_text()));
                    }
                    recyclerView.getAdapter().notifyDataSetChanged();
                    return false;
                }
            });
            //add

        }
        if(mark_id == 3) {
            getMenuInflater().inflate(R.menu.main, menu);
            menu.findItem(R.id.action_search).setVisible(false);
            //menu.findItem(R.id.action_todo).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            //return true;
        }
        else if(id == android.R.id.home){
            finish();
        }
        else if(id == R.id.action_todo){
            //add
            if(mark_id == 0)
                showNoteDialog(false, new Psp(), recyclerView, 0);
            else if(mark_id == 3){
                showCDKDialog(false, new CDKs(), recyclerView, 0);
            }
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onResume() {
        super.onResume();
    }



    private void setupRecyclerView(@NonNull final RecyclerView recyclerView) {

        //record 本地
        if(mark.equals(way[0])){
            DummyContent.ITEMS.clear();
            mark_id = 0;
            List<Psp> list = db.getAllPsps();
            for(int i = 0; i < list.size(); i++) {
                DummyContent.ITEMS.add(new DummyContent.DummyItem(
                        list.get(i).getId()+"", list.get(i).getPsp_title(),
                        list.get(i).getPsp_kind(), list.get(i).getPsp_author(),
                        list.get(i).getPsp_time(), list.get(i).getPsp_text(),
                        list.get(i).getPsp_webid(), list.get(i).getPsp_web_edition()));
            }
            adapter = new SimpleItemRecyclerViewAdapter(this, DummyContent.ITEMS, mTwoPane, mark_id);
            //recyclerView.setAdapter(adapter);
            //adapter.notifyDataSetChanged();
            //tips.dismiss();
        }
        //psp json
        else{
            //tips.show();


            reFreshData();
            DummyContent.setVisitFlag(true);
            adapter = new SimpleItemRecyclerViewAdapter(this, DummyContent.ITEMS, mTwoPane, mark_id);
            //recyclerView.setAdapter(adapter);
//            while (DummyContent.isVisit()){
//            }
            //tips.dismiss();

        }


        adapter.setOnitemClickLintener(new SimpleItemRecyclerViewAdapter.OnitemClick() {
            @Override
            public void onItemClick(int position) {
                //Toast.makeText(ItemListActivity.this,"点击了一下"+position,Toast.LENGTH_SHORT).show();
                DummyContent.DummyItem item = DummyContent.ITEMS.get(position);

                if(mark_id == 3){
                    //copy// 创建普通字符型ClipData
                    mClipData = ClipData.newPlainText("Label", item.title);
                    Log.d("copy", item.detail);
                    // 将ClipData内容放到系统剪贴板里。
                    cm.setPrimaryClip(mClipData);
                    Gadget.showToast("复制成功", recyclerView.getContext());
//                    Gadget.showToast("复制成功"+item.title, ItemListActivity.this);
                }
                else {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(ItemDetailFragment.ARG_ITEM_ID, item.id);
                        ItemDetailFragment fragment = new ItemDetailFragment();
                        fragment.setArguments(arguments);
                        ItemListActivity.this.getSupportFragmentManager().beginTransaction()
                                .replace(R.id.item_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = recyclerView.getContext();
                        Intent intent = new Intent(recyclerView.getContext(), ItemDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("id", Integer.parseInt(item.id));
                        bundle.putString("title", item.toString());
                        bundle.putString("detail", item.detail);
                        boolean isUrl = false;
                        //约定版本号为零且为web者是url
                        if(DummyContent.ITEMS.get(position).web_edition == 0
                                && DummyContent.ITEMS.get(position).web_id > 0){
                            isUrl = true;
                        }
                        bundle.putBoolean("isUrl", isUrl);

                        intent.putExtras(bundle);

                        context.startActivity(intent);
                    }
                }
            }
        });

        adapter.setOnLongClickListener(new SimpleItemRecyclerViewAdapter.OnLongClick() {
            @Override
            public void onLongClick(final int position) {
                DummyContent.DummyItem item = DummyContent.ITEMS.get(position);
                //Toast.makeText(ItemListActivity.this,"长按了一下"+position, Toast.LENGTH_SHORT).show();
                if(mark_id == 3){
                    final AlertView dialog = new AlertView("选择操作",
                            "兑换码管理员",
                            "取消",
                            new String[]{"删除"},
                            new String[]{"修改"},
                            recyclerView.getContext(),
                            AlertView.Style.ActionSheet,
                            new OnItemClickListener(){
                                @Override
                                public void onItemClick(Object o, int position_son) {
                                    //Toast.makeText(v.getContext(), "点击了第" + position_son + "个", Toast.LENGTH_SHORT).show();
                                    final int op =  position;
                                    switch (position_son){
                                        case 0:
                                            //delete
                                            String sql = "delete from cdk where id = "+ DummyContent.ITEMS.get(op).id;
                                            mySqlCDK(sql);
                                            DummyContent.ITEMS.remove(op);
                                            adapter.notifyItemRemoved(op);
                                            break;
                                        case 1:
                                            //编辑社团信息
                                            CDKs cdK = DummyContent.ITEMS.get(op).toCDK();
                                            showCDKDialog(true, cdK, recyclerView, op);
                                            break;
                                    }
                                }
                            });
                    dialog.show();
                    //Gadget.showToast("操作完成", recyclerView.getContext());
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final ItemListActivity mParentActivity;
        //private final List<DummyContent.DummyItem> mValues;
        private final boolean mTwoPane;
        private final int mark_way;
        private OnitemClick onitemClick;   //定义点击事件接口
        private OnLongClick onLongClick;  //定义长按事件接口
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DummyContent.DummyItem item = (DummyContent.DummyItem) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(ItemDetailFragment.ARG_ITEM_ID, item.id);
                    ItemDetailFragment fragment = new ItemDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.item_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ItemDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", Integer.parseInt(item.id));
                    bundle.putString("title", item.toString());
                    bundle.putString("detail", item.detail);

                    intent.putExtras(bundle);

                    context.startActivity(intent);
                }
            }

        };

        //定义设置点击事件监听的方法
        public void setOnitemClickLintener (OnitemClick onitemClick) {
            this.onitemClick = onitemClick;
        }
        //定义设置长按事件监听的方法
        public void setOnLongClickListener (OnLongClick onLongClick) {
            this.onLongClick = onLongClick;
        }

        //定义一个点击事件的接口
        public interface OnitemClick {
            void onItemClick(int position);
        }
        //定义一个长按事件的接口
        public interface OnLongClick {
            void onLongClick(int position);
        }

        SimpleItemRecyclerViewAdapter(ItemListActivity parent,
                                      List<DummyContent.DummyItem> items,
                                      boolean twoPane, int way) {
            //mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
            mark_way = way;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.mTitle.setText(DummyContent.ITEMS.get(position).title);
            holder.mKind.setText(DummyContent.ITEMS.get(position).kind);
            holder.mText_1.setText(DummyContent.ITEMS.get(position).under_1);
            holder.mText_2.setText(DummyContent.ITEMS.get(position).under_2);

            if (onitemClick != null) {
                holder.item_relativelytout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //在TextView的地方进行监听点击事件，并且实现接口
                        onitemClick.onItemClick(holder.getAdapterPosition());
                    }
                });
            }

            if (onLongClick != null) {
                holder.item_relativelytout.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        //在TextView的地方进行长按事件的监听，并实现长按接口
                        onLongClick.onLongClick(holder.getAdapterPosition());
                        return true;
                    }
                });
            }

            if(mark_id == 0){
                holder.delete.setVisibility(View.VISIBLE);

                if(DummyContent.ITEMS.get(holder.getAdapterPosition()).web_id == 0 ) {
                    //holder.update.setVisibility(View.GONE);
                    holder.update.setVisibility(View.VISIBLE);
                }
                else{

                }
                    //holder.update.setVisibility(View.VISIBLE);
                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int op_id = holder.getAdapterPosition();
                        DatabaseHelper databaseHelper = new DatabaseHelper(recyclerView.getContext());
                        databaseHelper.deletePsp(Integer.parseInt(DummyContent.ITEMS.get(op_id).id));
                        Gadget.showToast("已删除", view.getContext());
                        DummyContent.ITEMS.remove(op_id);
                        recyclerView.getAdapter().notifyItemRemoved(op_id);
                    }
                });
                holder.update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int op_id = holder.getAdapterPosition();
                        DatabaseHelper databaseHelper = new DatabaseHelper(recyclerView.getContext());
                        showNoteDialog(true,
                                databaseHelper.getPsp(Integer.parseInt(DummyContent.ITEMS.get(op_id).id)),
                                view, position);
                    }
                });
            }
            else if(mark_id == 1){
                holder.star.setVisibility(View.VISIBLE);
                holder.star.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseHelper databaseHelper = new DatabaseHelper(view.getContext());
                        databaseHelper.insertPsp(DummyContent.ITEMS.get(holder.getAdapterPosition()).toPsp());
//                        ((CstSwipeDelMenu) holder.itemView).quickClose();
                        Gadget.showToast("已收藏到我的手记", view.getContext());
                        recyclerView.getAdapter().notifyDataSetChanged();
                    }
                });
            }
            else if(mark_id == 4){
                holder.star.setVisibility(View.VISIBLE);
                holder.star.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //todo update
                        String sql = "UPDATE `feedback` SET `state`='已处理' WHERE id = "+ DummyContent.ITEMS.get(holder.getAdapterPosition()).id;
                        mySqlCDK(sql);
                        Gadget.showToast("已处理", view.getContext());
                        recyclerView.getAdapter().notifyDataSetChanged();
                    }
                });
            }

            //holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }

        @Override
        public int getItemCount() {
            return DummyContent.ITEMS.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mTitle;
            final TextView mKind;
            final TextView mText_1;
            final TextView mText_2;
            final Button delete;
            final Button update;
            final Button star;
            final RelativeLayout item_relativelytout;

            ViewHolder(View view) {
                super(view);
                mTitle = (TextView) view.findViewById(R.id.item_title);
                mKind = (TextView) view.findViewById(R.id.item_kind);
                mText_1 = view.findViewById(R.id.item_under_1);
                mText_2 = view.findViewById(R.id.item_under_2);
                delete = view.findViewById(R.id.btnDelete);
                update = view.findViewById(R.id.btnUpdate);
                star = view.findViewById(R.id.btnStar);
                item_relativelytout = view.findViewById(R.id.item_relativelytout);
            }
        }
    }


    private static List<DummyContent.DummyItem> getJson(String jsonStr){
        JSONObject dataJson = null;
        List<DummyContent.DummyItem> list = new ArrayList<>();
        Log.d("sss:",jsonStr);
        try {
            dataJson = new JSONObject(jsonStr);
//            JSONObject response=dataJson.getJSONObject("");
            if(mark_id == 1){
                JSONArray data=dataJson.getJSONArray("Psp");
                for(int i = 0; i < data.length(); i++) {
                    JSONObject info = data.getJSONObject(i);
                    String title = info.getString("title");
                    String kind = info.getString("kind");
                    String time = info.getString("time");
                    String author = info.getString("author");
                    String content = info.getString("content");
                    int id = info.getInt("id");
                    int edition = info.getInt("edition");
                    System.out.println(title + time + kind + author + content);
                    list.add(new DummyContent.DummyItem(String.valueOf(id), title, kind, author, time, content, id, edition));
                }
            }
            //play
            else if(mark_id == 2){
                JSONArray data=dataJson.getJSONArray("Activity");
                for(int i = 0; i < data.length(); i++) {
                    JSONObject info = data.getJSONObject(i);
                    String title = info.getString("title");
                    String kind = info.getString("kind");
                    String time_s = info.getString("time_start");
                    String time_e = info.getString("time_end");
                    String content = info.getString("content");
                    int id = info.getInt("id");
                    System.out.println(title + time_s + kind + time_e + content);
                    list.add(new DummyContent.DummyItem(String.valueOf(id), title, kind, time_s, time_e, content, id, 0));
                }
            }
            else if(mark_id == 3){
                JSONArray data = dataJson.getJSONArray("CDK");
                for(int i = 0; i < data.length(); i++) {
                    JSONObject info = data.getJSONObject(i);
                    int id = info.getInt("id");
                    String cdk = info.getString("cdk");
                    String date = info.getString("date");
                    String content = info.getString("content");


//                    System.out.println(title + time + kind + author + content);
                    list.add(new DummyContent.DummyItem(String.valueOf(id), cdk, content, date));
                }
            }
            else if(mark_id == 4){
                JSONArray data = dataJson.getJSONArray("feedback");
                for(int i = 0; i < data.length(); i++) {
                    JSONObject info = data.getJSONObject(i);
                    String title = info.getString("summary");
                    String kind = info.getString("kind");
                    String time_s = info.getString("state");
                    String time_e = info.getString("timestamp");
                    String content = info.getString("content");
                    String ip = "ip:"+info.getString("ip") +"  \n";
                    String tel = "联系方式"+info.getString("tel") +"  \n";
                    int id = info.getInt("id");
                    System.out.println(title + time_s + kind + time_e + content);
                    list.add(new DummyContent.DummyItem(id+"", title, kind, time_s, time_e, ip+tel+content, id, 0));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }


    public static String run(String url){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void showNoteDialog(final boolean shouldUpdate, final Psp psp, final View _view, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from((_view.getContext()));
        final View view = layoutInflaterAndroid.inflate(R.layout.dialog_record, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(view.getContext());
        alertDialogBuilderUserInput.setView(view);

        final EditText inputTitle = view.findViewById(R.id.input_record_title);
        final EditText inputKind = view.findViewById(R.id.input_record_kind);
        final EditText inputText = view.findViewById(R.id.input_record_text);
        final Button cancel = view.findViewById(R.id.dialog_record_cancel);
        final Button save = view.findViewById(R.id.dialog_record_save);

        save.setHint(!shouldUpdate ? "保存" : "修改");

        if (shouldUpdate && psp.getId() > 0) {
            inputTitle.setText(psp.getPsp_title());
            inputKind.setText(psp.getPsp_kind());
            inputText.setText(psp.getPsp_text());
        }
        alertDialogBuilderUserInput
                .setCancelable(false);

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Psp psp_buf = psp;
                psp_buf.setPsp_title(inputTitle.getText().toString());
                psp_buf.setPsp_kind(inputKind.getText().toString());
                psp_buf.setPsp_text(inputText.getText().toString());
                psp_buf.setPsp_time(DateUtil.getDateTime());
                DatabaseHelper db = new DatabaseHelper(recyclerView.getContext());

                if(shouldUpdate){
                    db.updatePsp(psp_buf);
                    DummyContent.ITEMS.set(position, psp_buf.toDummyItem());

                    recyclerView.getAdapter().notifyItemChanged(position);
                }
                else {
                    psp_buf.setId((int)db.insertPsp(psp_buf));
                    Log.d("insert:",psp_buf.getId()+"");
                    DummyContent.ITEMS.add(0, psp_buf.toDummyItem());
                    recyclerView.getAdapter().notifyItemInserted(0);
                }
                alertDialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();

    }

    public static void showCDKDialog(final boolean shouldUpdate, final CDKs psp, final View _view, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from((_view.getContext()));
        final View view = layoutInflaterAndroid.inflate(R.layout.dialog_cdk, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(view.getContext());
        alertDialogBuilderUserInput.setView(view);

        final EditText inputTitle = view.findViewById(R.id.input_cdk);
        final EditText inputText = view.findViewById(R.id.input_cdk_text);
        final Button cancel = view.findViewById(R.id.dialog_cdk_cancel);
        final Button save = view.findViewById(R.id.dialog_cdk_save);

        save.setHint(!shouldUpdate ? "保存" : "修改");

        if (shouldUpdate && psp.getId() > 0) {
            inputTitle.setText(psp.getCdk());
            inputText.setText(psp.getCdk_text());
        }
        alertDialogBuilderUserInput
                .setCancelable(false);

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CDKs psp_buf = psp;
                psp_buf.setCdk(inputTitle.getText().toString());
                psp_buf.setCdk_text(inputText.getText().toString());
                psp_buf.setCdk_date(DateUtil.getDateMySql());
                //DatabaseHelper db = new DatabaseHelper(recyclerView.getContext());
                String sql = "";
                if(shouldUpdate){
                    sql = "update cdk set cdk =  '" + psp_buf.getCdk() +"',"
                            + " about = '" + psp_buf.getCdk_text()
                            + "' where id = " + psp.getId();
                    DummyContent.ITEMS.set(position, psp_buf.toDummyItem());

                    recyclerView.getAdapter().notifyItemChanged(position);
                }
                else {
                    sql = "insert into cdk (cdk, about, date) values(" +
                            "'"+psp_buf.getCdk()+"', '"
                            + psp_buf.getCdk_text()+"', '"
                            + psp_buf.getCdk_date() + "')";
                    Log.d("insert:",psp_buf.getId()+"");
                    DummyContent.ITEMS.add(0, psp_buf.toDummyItem());
                    recyclerView.getAdapter().notifyItemInserted(0);
                }
                mySqlCDK(sql);
                alertDialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();

    }

    public void reFreshData(){
        tips = new SVProgressHUD(this);
        tips.showWithStatus("加载中...");
        //Gadget.showToast(mark, this);
        DummyContent.ITEMS.clear();
        final Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "";
                if(mark.equals(way[1])){
                    mark_id = 1;
                    /*
                    url = "https://raw.githubusercontent.com/G-eto/TokyoGhoul/master/data_psp.json";
                    jsondata_psp = getJsondataFromWeb(url, jsondata_psp);
                    jsondata = jsondata_psp;
                    Log.d("urls:",url);

                    DummyContent.ITEMS.addAll(getJson(jsondata));
                    DummyContent.setVisitFlag(false);
                    */
                    refreshPSP("select * from psp order by time DESC");
                }
                //play
                else if(mark.equals(way[2])){
                    mark_id = 2;
                    url = "https://raw.githubusercontent.com/G-eto/TokyoGhoul/master/data_activity.json";
                    jsondata_play = getJsondataFromWeb(url, jsondata_play);
                    jsondata = jsondata_play;
                    Log.d("urls:",url);

                    DummyContent.ITEMS.addAll(getJson(jsondata));
                    DummyContent.setVisitFlag(false);
                }
                //cdk
                else if(mark.equals(way[3])){
                    mark_id = 3;
                    //url = "https://raw.githubusercontent.com/G-eto/TokyoGhoul/master/data_cdk.json";
                    //jsondata_cdk = getJsondataFromWeb(url, jsondata_cdk);
                    refreshCDK("select * from cdk order by date DESC");
                    //jsondata = jsondata_cdk;
                }
                else if(mark.equals(way[4])){
                    mark_id = 4;
                    refreshFeedback("select * from feedback order by timestamp DESC");
                }
                callUIrefresh();

            }

        });
        DummyContent.setVisitFlag(true);
        thread.start();
    }

    private String getJsondataFromWeb(String url, String s){
        int op = 0;
        if(!s.equals("")){
            return s;
        }
        String jsd = "";
        while (jsd.equals("")) {
            if(++op > 10){
                Log.d("请求失败","try 10 times");
                break;
            }
            try {
                jsd = HtmlService.getHtml(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return jsd;
    }

    private static void refreshCDK(final String sql) {
        //final String REMOTE_IP = "ghfuuto7.2392lan.dnstoo.com:3306";
        final String URL = "jdbc:mysql://ghfuuto7.2392.dnstoo.com:5504/wakof8" +
                "?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=UTC&rewriteBatchedStatements=true&autoReconnect=true";

        final String USER = "wakof8_f";
        final String PASSWORD = "n549tjkt";

        new Thread(new Runnable() {
            public void run() {
                System.out.println("bbbbbbb");
                Connection conn;
                conn = Gadget.openConnection(URL, USER, PASSWORD);
                System.out.println("All users info:");
                query(conn, sql);
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        conn = null;
                    } finally {
                        conn = null;
                    }
                }
            }
        }).start();

    }

    public static boolean query(Connection conn, String sql) {

        if (conn == null) {
            return false;
        }
        Log.d("mysql1","44");

        Statement statement = null;
        ResultSet result = null;

        //List<DummyContent.DummyItem> mlist = new ArrayList<>();
        try {
            statement = conn.createStatement();
            result = statement.executeQuery(sql);


            result.last();
            int count = result.getRow(); //获得ResultSet的总行数

            Log.d("mysql1",count+"个web");
            int count_local = cdkBuf.size();
            Log.d("mysql1",count+"个local");
            if(count <= count_local){
                DummyContent.ITEMS.clear();
                DummyContent.ITEMS.addAll(cdkBuf);
                callUIrefresh();
                return true;
            }

            if (result != null && result.first()) {
                int idColumnIndex = result.findColumn("id");
                int cdkColumnIndex = result.findColumn("cdk");
                int aboutColumnIndex = result.findColumn("about");
                int dateColumnIndex = result.findColumn("date");

                Log.d("mysql1",55+result.toString());
                System.out.println("id\t\t" + "name");
                while (!result.isAfterLast()) {
                    cdkBuf.add(new DummyContent.DummyItem(String.valueOf(result.getInt(idColumnIndex)),
                            result.getString(cdkColumnIndex), result.getString(aboutColumnIndex),
                            result.getString(dateColumnIndex)));

                    result.next();
                }
                Log.d("mysql1","66");
                DummyContent.ITEMS.clear();
                DummyContent.ITEMS.addAll(cdkBuf);
                callUIrefresh();
                Log.d("mysql1","777");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (result != null) {
                    result.close();
                    result = null;
                }
                if (statement != null) {
                    statement.close();
                    statement = null;
                }

            } catch (SQLException sqle) {

            }
        }
        return true;
    }

    private static void callUIrefresh(){
        recyclerView.post(new Runnable(){
            @Override
            public void run() {
                recyclerView.getAdapter().notifyDataSetChanged();
                tips.dismiss();
            }

        });
    }

    private static void mySqlCDK(final String sql) {
        //final String REMOTE_IP = "ghfuuto7.2392lan.dnstoo.com:3306";
        final String URL = "jdbc:mysql://ghfuuto7.2392.dnstoo.com:5504/wakof8" +
                "?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=UTC&rewriteBatchedStatements=true&autoReconnect=true";

        final String USER = "wakof8_f";
        final String PASSWORD = "n549tjkt";

        new Thread(new Runnable() {
            public void run() {
                System.out.println("bbbbbbb");
                Connection conn;
                conn = Gadget.openConnection(URL, USER, PASSWORD);
                System.out.println("All users info:");
                queryCDK(conn, sql);
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        conn = null;
                    } finally {
                        conn = null;
                    }
                }
            }
        }).start();
    }
    public static boolean queryCDK(Connection conn, String sql) {

        if (conn == null) {
            return false;
        }
        Log.d("mysql2","44");

        Statement statement = null;

        try {
            statement = conn.createStatement();
            //result = statement.executeQuery(sql);
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                    statement = null;
                }

            } catch (SQLException sqle) {

            }
        }
        return true;
    }

    private static void refreshPSP(final String sql) {
        //final String REMOTE_IP = "ghfuuto7.2392lan.dnstoo.com:3306";
        final String URL = "jdbc:mysql://ghfuuto7.2392.dnstoo.com:5504/wakof8" +
                "?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=UTC&rewriteBatchedStatements=true&autoReconnect=true";

        final String USER = "wakof8_f";
        final String PASSWORD = "n549tjkt";

        new Thread(new Runnable() {
            public void run() {
                System.out.println("bbbbbbb");
                Connection conn;
                conn = Gadget.openConnection(URL, USER, PASSWORD);
                System.out.println("All users info:");
                queryPSP(conn, sql);
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        conn = null;
                    } finally {
                        conn = null;
                    }
                }
            }
        }).start();

    }

    public static boolean queryPSP(Connection conn, String sql) {

        if (conn == null) {
            return false;
        }
        Log.d("mysql1","44");

        Statement statement = null;
        ResultSet result = null;

        //List<DummyContent.DummyItem> mlist = new ArrayList<>();
        try {
            statement = conn.createStatement();
            result = statement.executeQuery(sql);


            result.last();
            int count = result.getRow(); //获得ResultSet的总行数

            Log.d("mysql1",count+"个web");
            int count_local = pspBuf.size();
            Log.d("mysql1",count+"个local");
            if(count <= count_local){
                DummyContent.ITEMS.clear();
                DummyContent.ITEMS.addAll(pspBuf);
                callUIrefresh();
                return true;
            }

            if (result != null && result.first()) {
                int idColumnIndex = result.findColumn("id");
                int editionColumnIndex = result.findColumn("edition");
                int kindColumnIndex = result.findColumn("kind");
                int titleColumnIndex = result.findColumn("title");
                int authorColumnIndex = result.findColumn("author");
                int contentColumnIndex = result.findColumn("content");
                int timeColumnIndex = result.findColumn("time");

                Log.d("mysql1",55+result.toString());
                System.out.println("id\t\t" + "name");
                while (!result.isAfterLast()) {
                    pspBuf.add(new DummyContent.DummyItem(String.valueOf(result.getInt(idColumnIndex)),
                            result.getString(titleColumnIndex), result.getString(kindColumnIndex),
                            result.getString(authorColumnIndex), result.getString(timeColumnIndex),
                            result.getString(contentColumnIndex), result.getInt(idColumnIndex),
                            result.getInt(editionColumnIndex)));
                    //String.valueOf(id), title, kind, author, time, content, id, edition)
                    result.next();
                }
                Log.d("mysql1","66");
                DummyContent.ITEMS.clear();
                DummyContent.ITEMS.addAll(pspBuf);
                callUIrefresh();
                Log.d("mysql1","777");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (result != null) {
                    result.close();
                    result = null;
                }
                if (statement != null) {
                    statement.close();
                    statement = null;
                }

            } catch (SQLException sqle) {

            }
        }
        return true;
    }

    private static void refreshFeedback(final String sql) {
        //final String REMOTE_IP = "ghfuuto7.2392lan.dnstoo.com:3306";
        final String URL = "jdbc:mysql://ghfuuto7.2392.dnstoo.com:5504/wakof8" +
                "?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=UTC&rewriteBatchedStatements=true&autoReconnect=true";

        final String USER = "wakof8_f";
        final String PASSWORD = "n549tjkt";

        new Thread(new Runnable() {
            public void run() {
                System.out.println("bbbbbbb");
                Connection conn;
                conn = Gadget.openConnection(URL, USER, PASSWORD);
                System.out.println("All users info:");
                queryFeedback(conn, sql);
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        conn = null;
                    } finally {
                        conn = null;
                    }
                }
            }
        }).start();

    }

    public static boolean queryFeedback(Connection conn, String sql) {

        if (conn == null) {
            return false;
        }
        Log.d("mysql1","44");

        Statement statement = null;
        ResultSet result = null;

        //List<DummyContent.DummyItem> mlist = new ArrayList<>();
        try {
            statement = conn.createStatement();
            result = statement.executeQuery(sql);


            result.last();
            int count = result.getRow(); //获得ResultSet的总行数

            Log.d("mysql1",count+"个web");
            int count_local = feedback.size();
            Log.d("mysql1",count+"个local");
            if(count <= count_local){
                DummyContent.ITEMS.clear();
                DummyContent.ITEMS.addAll(feedback);
                callUIrefresh();
                return true;
            }

            if (result != null && result.first()) {
                int idColumnIndex = result.findColumn("id");
                int kindColumnIndex = result.findColumn("kind");
                int titleColumnIndex = result.findColumn("summary");
                int authorColumnIndex = result.findColumn("timestamp");
                int contentColumnIndex = result.findColumn("content");
                int timeColumnIndex = result.findColumn("state");
                int ipColumnIndex = result.findColumn("ip");
                int telColumnIndex = result.findColumn("tel");

                Log.d("mysql1",55+result.toString());
                System.out.println("id\t\t" + "name");
                while (!result.isAfterLast()) {
                    feedback.add(new DummyContent.DummyItem(String.valueOf(result.getInt(idColumnIndex)),
                            result.getString(titleColumnIndex), result.getString(kindColumnIndex),
                            result.getString(authorColumnIndex), result.getString(timeColumnIndex),
                            " 联系方式 "+result.getString(telColumnIndex)+" ip:"+result.getString(ipColumnIndex)+" "+result.getString(contentColumnIndex),
                            result.getInt(idColumnIndex), 0));
                    //String.valueOf(id), title, kind, author, time, content, id, edition)
                    result.next();
                }
                Log.d("mysql1","66");
                DummyContent.ITEMS.clear();
                DummyContent.ITEMS.addAll(feedback);
                callUIrefresh();
                Log.d("mysql1","777");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (result != null) {
                    result.close();
                    result = null;
                }
                if (statement != null) {
                    statement.close();
                    statement = null;
                }

            } catch (SQLException sqle) {

            }
        }
        return true;
    }

}
