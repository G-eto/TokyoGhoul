package com.example.tokyoghoul.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.example.tokyoghoul.R;
import com.example.tokyoghoul.database.DatabaseHelper;
import com.example.tokyoghoul.database.model.Account;
import com.example.tokyoghoul.database.model.Role;

import java.util.ArrayList;
import java.util.List;

public class MyRoleRecycleAdapter extends RecyclerView.Adapter<MyRoleRecycleAdapter.MyViewHolder> {

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public final ImageView role_img;
        public final TextView role_name;
        public final TextView role_kind;
        public final TextView role_level;
        public final TextView role_stone_1;
        public final TextView role_stone_2;
        public final TextView role_rune_1;
        public final TextView role_rune_2;
        public final TextView role_rune_3;
        public final TextView role_rune_4;
        public final TextView role_rune_suit;
        //public final TextView role_introduce;
        public final RelativeLayout mrelativeLayout;
        public boolean isClose = true;
        public LinearLayout mlinearLayout;

        public MyViewHolder(View v) {
            super(v);
            role_img = v.findViewById(R.id.img_role);
            role_name = (TextView) v.findViewById(R.id.role_name);
            role_kind = v.findViewById(R.id.role_kind);
            role_level = (TextView) v.findViewById(R.id.role_level);
            role_stone_1 = (TextView) v.findViewById(R.id.stone_1);
            role_stone_2 = (TextView) v.findViewById(R.id.stone_2);
            role_rune_1 = (TextView) v.findViewById(R.id.rune_1);
            role_rune_2 = (TextView) v.findViewById(R.id.rune_2);
            role_rune_3 = (TextView) v.findViewById(R.id.rune_3);
            role_rune_4 = (TextView) v.findViewById(R.id.rune_4);
            role_rune_suit = (TextView) v.findViewById(R.id.stone_suit_1_introduce);
            //role_introduce = (TextView) v.findViewById(R.id.stone_role_introduce);
            mrelativeLayout = v.findViewById(R.id.role_relativelytout);
            mlinearLayout = v.findViewById(R.id.role_gone);
        }
    }

    public List<Role> mdata = new ArrayList<>();
    DatabaseHelper db;
    View mview;

    public void setData(List<Role> data){
        this.mdata.clear();
        this.mdata = data;
    }

    public MyRoleRecycleAdapter(View view, List<Role> data) {
        db = new DatabaseHelper(view.getContext());
        mview = view;
//        this.mdata = db.getAllRoles();
        this.mdata = data;
        this.notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MyRoleRecycleAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_role_detail, parent, false);
        return new MyRoleRecycleAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyRoleRecycleAdapter.MyViewHolder myViewHolder, final int position) {
        //myViewHolder.img_port.set
        myViewHolder.mlinearLayout.setVisibility(View.GONE);
        myViewHolder.isClose = true;
        myViewHolder.role_img.setImageBitmap(mdata.get(position).getRole_img_show());
        myViewHolder.role_name.setText(mdata.get(position).getRole_name());
        myViewHolder.role_kind.setText(mdata.get(position).getRole_kind());
        myViewHolder.role_level.setText(mdata.get(position).getRole_level());
        myViewHolder.role_stone_1.setText(mdata.get(position).getRole_stone_1());
        myViewHolder.role_stone_2.setText(mdata.get(position).getRole_stone_2());
        myViewHolder.role_rune_1.setText(mdata.get(position).getRole_rune_1());
        myViewHolder.role_rune_2.setText(mdata.get(position).getRole_rune_2());
        myViewHolder.role_rune_3.setText(mdata.get(position).getRole_rune_3());
        myViewHolder.role_rune_4.setText(mdata.get(position).getRole_rune_4());
        myViewHolder.role_rune_suit.setText(mdata.get(position).getRole_rune_suit());
//        myViewHolder.role_introduce.setText(mdata.get(position).getRole_introduce());

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //item 点击事件
                Log.d("pic",mdata.get(position).getRole_img_show().toString()+"fg");
                Toast.makeText(v.getContext(), "点击了第" + myViewHolder.getAdapterPosition() + "个", Toast.LENGTH_SHORT).show();

            }
        });
        myViewHolder.mrelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(myViewHolder.isClose){
                    myViewHolder.mlinearLayout.setVisibility(View.VISIBLE);
                    myViewHolder.isClose = false;
                }
                else {
                    myViewHolder.mlinearLayout.setVisibility(View.GONE);
                    myViewHolder.isClose = true;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }
}