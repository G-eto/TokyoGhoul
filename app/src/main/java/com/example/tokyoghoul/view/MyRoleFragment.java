package com.example.tokyoghoul.view;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.tokyoghoul.R;
import com.example.tokyoghoul.database.DatabaseHelper;
import com.example.tokyoghoul.database.model.Role;

import java.util.ArrayList;
import java.util.List;

import static com.example.tokyoghoul.activity.MainActivity.searchView;


public class MyRoleFragment extends Fragment {
    RecyclerView recyclerView;
    View view;
    MyRoleRecycleAdapter myRoleRecycleAdapter;
    //SearchView searchView;
    DatabaseHelper db;
    List<Role> mlist;
    public static int opopSearch = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_myrole, null);

        //Bitmap bitmap = BitmapFactory.decodeResource(container.getResources(), R.drawable.ic_menu_share);

//        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.mipmap.click_icon);
//        //Bitmap bmp = (((BitmapDrawable)tmp.image).getBitmap());
//        Log.d("pssd",bitmap1.toString()+"dfg");
//
//        DatabaseHelper db = new DatabaseHelper(view.getContext());
//
//        Role role = new Role(bitmap1, "有马贵将", "攻", "SSS", "果敢", "极致",
//                "奥义1", "奥义2", "奥义3", "奥义4", "很流弊，吸血\n八级套,\n九级套：计划v的",
//                "有马天下第一", "未获得");
//        db.insertRole(role);
//
//
//        bitmap1.recycle();
        db = new DatabaseHelper(view.getContext());
        mlist = db.getAllRoles();
        myRoleRecycleAdapter = new MyRoleRecycleAdapter(view, mlist);
        InitView();
        return view;
    }

    private void InitView(){
        recyclerView = (RecyclerView) view.findViewById(R.id.myrole_recycleview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
//设置布局管理器
        recyclerView.setLayoutManager(layoutManager);
//设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper. VERTICAL);
//设置Adapter
        recyclerView.setAdapter(myRoleRecycleAdapter);
        //设置分隔线
//        recyclerView.addItemDecoration( new DividerGridItemDecoration(this ));
//设置增加或删除条目的动画
        recyclerView.setItemAnimator( new DefaultItemAnimator());

        //searchView = view.findViewById(R.id.myrole_search);

        ((SearchView)searchView.getActionView()).setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //myRoleRecycleAdapter.setData(db.selectRoles(s));
                myRoleRecycleAdapter.mdata.clear();
                myRoleRecycleAdapter.mdata.addAll(db.selectRoles(s));
                myRoleRecycleAdapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                myRoleRecycleAdapter.mdata.clear();
                myRoleRecycleAdapter.mdata.addAll(db.selectRoles(s));
                myRoleRecycleAdapter.notifyDataSetChanged();
                return false;
            }

        });

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if ((isVisibleToUser && isResumed())) {
            onResume();
        } else if (!isVisibleToUser) {
            //Timber.i("On Pause on %s Fragment Invisble", getClass().getSimpleName());
            onPause();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {

            //Timber.i("On Resume on %s Fragment Visible", getClass().getSimpleName());
            //TODO give the signal that the fragment is visible
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //TODO give the signal that the fragment is invisible


    }
}

