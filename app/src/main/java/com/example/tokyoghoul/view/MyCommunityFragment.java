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
import com.example.tokyoghoul.database.DatabaseHelper;
import com.example.tokyoghoul.database.model.CommunityManage;

public class MyCommunityFragment extends Fragment {

    public RecyclerView recyclerView;
    public static View view_community;
    public static MyCommunityRecycleAdapter myCommunityRecycleAdapter;
    LinearLayout mlinearLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view_community = inflater.inflate(R.layout.fragment_mycommunity, null);

 //       db = new DatabaseHelper(view.getContext());
        //test
//        db.insertCommunity(1,"大撒孩子","08/05 13:14",12);
//        db.insertCommunity(2,"大傻子","08/06 13:14",1);
//        db.insertCommunity(5,"哈子","08/07 13:14",2);
//        db.insertCommunity(3,"孩子","08/01 13:14",4);

        myCommunityRecycleAdapter = new MyCommunityRecycleAdapter(view_community);
        InitView();

        return view_community;
    }

    private void InitView(){
        recyclerView = (RecyclerView) view_community.findViewById(R.id.mycommunity_recycleview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view_community.getContext());
//设置布局管理器
        recyclerView.setLayoutManager(layoutManager);
//设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper. VERTICAL);
//设置Adapter
        recyclerView.setAdapter(myCommunityRecycleAdapter);
        //设置分隔线
//        recyclerView.addItemDecoration( new DividerGridItemDecoration(this ));
//设置增加或删除条目的动画
        recyclerView.setItemAnimator( new DefaultItemAnimator());

//        mlinearLayout = view_community.findViewById(R.id.add_community);
//        mlinearLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//               myCommunityRecycleAdapter.showNoteDialog(false, new CommunityManage(), view_community, -1);
//            }
//        });

    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if ((isVisibleToUser && isResumed())) {
            this.myCommunityRecycleAdapter.addAccounts();
            this.myCommunityRecycleAdapter.notifyDataSetChanged();
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