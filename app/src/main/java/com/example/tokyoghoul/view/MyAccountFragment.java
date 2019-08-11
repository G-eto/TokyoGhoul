package com.example.tokyoghoul.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.tokyoghoul.R;
import com.example.tokyoghoul.database.model.Account;
import com.example.tokyoghoul.database.model.CommunityManage;

public class MyAccountFragment extends Fragment {

    RecyclerView recyclerView;
    View view;
    MyAccountRecycleAdapter myAccountRecycleAdapter;
    LinearLayout mlinearLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_account, null);
        myAccountRecycleAdapter = new MyAccountRecycleAdapter(view);
        InitView();
        return view;
    }

    private void InitView(){
        recyclerView = (RecyclerView) view.findViewById(R.id.account_recycleview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
//设置布局管理器
        recyclerView.setLayoutManager(layoutManager);
//设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper. VERTICAL);
//设置Adapter
        recyclerView.setAdapter(myAccountRecycleAdapter);
        //设置分隔线
//        recyclerView.addItemDecoration( new DividerGridItemDecoration(this ));
//设置增加或删除条目的动画
        recyclerView.setItemAnimator( new DefaultItemAnimator());

        mlinearLayout = view.findViewById(R.id.add_account);
        mlinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myAccountRecycleAdapter.showNoteDialog(false, new Account(), view, -1);
            }
        });

    }
//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser){
//        super.setUserVisibleHint(isVisibleToUser);
//        //recyclerView.getAdapter().notifyDataSetChanged();
//    }
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
            recyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //TODO give the signal that the fragment is invisible

    }
}