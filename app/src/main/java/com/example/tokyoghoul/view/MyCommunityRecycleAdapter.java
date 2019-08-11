package com.example.tokyoghoul.view;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.contrarywind.view.WheelView;
import com.example.tokyoghoul.R;
import com.example.tokyoghoul.activity.MainActivity;
import com.example.tokyoghoul.database.DatabaseHelper;
import com.example.tokyoghoul.database.model.Account;
import com.example.tokyoghoul.database.model.CommunityManage;
import com.example.tokyoghoul.tool.DateUtil;
import com.example.tokyoghoul.tool.Gadget;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyCommunityRecycleAdapter extends RecyclerView.Adapter<MyCommunityRecycleAdapter.MyViewHolder>{

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public final TextView community_id;
        public final TextView community_name;
        public final TextView community_timestamp;
        public final TextView manager_port;
        public final TextView manager_manager;

        public MyViewHolder(View v) {
            super(v);
            community_id = (TextView) v.findViewById(R.id.community_item_id);
            community_name = (TextView) v.findViewById(R.id.community_item_name);
            community_timestamp = (TextView) v.findViewById(R.id.community_item_time);
            manager_port = (TextView) v.findViewById(R.id.community_item_port);
            manager_manager = (TextView) v.findViewById(R.id.community_item_manager);
        }
    }

    private List<CommunityManage> mdata = new ArrayList<>();
    private List<Account> mdata_account = new ArrayList<>();
    DatabaseHelper db;
    View mview;

    public MyCommunityRecycleAdapter(View view){
        db = new DatabaseHelper(view.getContext());
//        db.insertCommunity(1,"大撒孩子地方","08/05 13:14",12);
//        db.insertCommunity(2,"大傻子","08/06 13:14",1);
//        db.insertCommunity(7770,"哈子","08/07 13:14",2);
//        db.insertCommunity(3,"孩子","08/01 13:14",4);
//
//        db.insertAccount("QQ","焰灵姬","960789981","xxx");
//        db.insertAccount("微信","小号","9607899819","xxx");
//        db.insertAccount("华为","冷月","9607","xxx");
//        db.insertAccount("小米","test","xiaomi969981","xxx");

        mview = view;
        this.mdata = db.getAllCommunity();
        addAccounts();

    }
    public void addAccounts(){
        this.mdata_account.clear();
        for(int i = 0; i < mdata.size(); i++) {
            Log.d("hello :", "" + mdata.get(i).getManager_id());
            this.mdata_account.add(db.getAccount(mdata.get(i).getManager_id()));
        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int position) {
        myViewHolder.community_id.setText("id:"+mdata.get(position).getCommunity_id());
        myViewHolder.community_name.setText(mdata.get(position).getCommunity_name());
        myViewHolder.community_timestamp.setText(mdata.get(position).getDate());
        myViewHolder.manager_manager.setText(mdata_account.get(position).getAccount_role());
        myViewHolder.manager_port.setText(mdata_account.get(position).getAccount_port());

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //item 点击事件

                final AlertView dialog = new AlertView("选择操作",
                        "是否今日所在社团，此时为退出社团时间",
                        "取消",
                        new String[]{"删除"},
                        new String[]{"今天就决定是你了", "修改社团信息", "复制社团名", "复制社团id"},
                        v.getContext(),
                        AlertView.Style.ActionSheet,
                        new OnItemClickListener(){
                    @Override
                    public void onItemClick(Object o, int position_son) {
                        Toast.makeText(v.getContext(), "点击了第" + position_son + "个", Toast.LENGTH_SHORT).show();
                        final int op =  myViewHolder.getAdapterPosition();
                        switch (position_son){
                            case 1:
                                TimePickerView pvTime = new TimePickerBuilder(v.getContext(), new OnTimeSelectListener() {
                                    @Override
                                    public void onTimeSelect(Date date, View v) {
                                        CommunityManage communityManage_buf = mdata.get(op);
                                        communityManage_buf.setDate(DateUtil.getDateDate(date));
                                        db.updateCommunity(communityManage_buf);
                                        mdata.set(op, communityManage_buf);
                                        notifyItemChanged(op);
                                        //Toast.makeText(v.getContext(), "已更新最近时间", Toast.LENGTH_SHORT).show();
                                    }
                                }).setType(new boolean[]{false, false, false, true, true, true})// 默认全部显示
                                        .build();
                                pvTime.show();
                                break;
                            case 0:
                                //delete
                                db.deleteCommunity(mdata.get(op));
                                mdata.remove(op);
                                notifyItemRemoved(op);
                                Toast.makeText(v.getContext(), "已删除", Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                //编辑社团信息
                                showNoteDialog(true, mdata.get(op), mview, op);
                                Log.d("dssd",mdata.size()+"");
                                break;
                            case 3:
                                Gadget.copyStr(mdata.get(op).getCommunity_name(), v.getContext());
                                break;
                            case  4:
                                Gadget.copyStr(mdata.get(op).getCommunity_id()+"", v.getContext());
                                break;
                        }

                    }

                });
                dialog.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public void showNoteDialog(final boolean shouldUpdate, final CommunityManage community, final View _view, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from((_view.getContext()));
        final View view = layoutInflaterAndroid.inflate(R.layout.note_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(view.getContext());
        alertDialogBuilderUserInput.setView(view);

        final EditText inputId = view.findViewById(R.id.input_id);
        final EditText inputName = view.findViewById(R.id.input_name);
        final Spinner inputManagerId = view.findViewById(R.id.manager_spinner);
        final Button cancel = view.findViewById(R.id.dialog_cancel);
        final Button save = view.findViewById(R.id.dialog_save);


        final List<Account> accounts = new ArrayList<>();
        final DatabaseHelper database = new DatabaseHelper(view.getContext());
        accounts.add(new Account());
        accounts.addAll(database.getAllAccounts());
        MySpinnerAdapter mySpinnerAdapter = new MySpinnerAdapter(view.getContext(), accounts, community.getCommunity_id());
        inputManagerId.setAdapter(mySpinnerAdapter);

        int op_m_id = 0;
        for(int i = 1; i < accounts.size(); i++){
            if(accounts.get(i).getId() == community.getManager_id()){
                op_m_id = i;
                break;
            }
        }

//        TextView dialogTitle = view.findViewById(R.id.dialog_title);
        save.setHint(!shouldUpdate ? "保存" : "修改");

        if (shouldUpdate && community.getId() > 0) {
            inputId.setText(community.getCommunity_id()+"");
            inputName.setText(community.getCommunity_name());
            inputManagerId.setSelection(op_m_id);
        }
        alertDialogBuilderUserInput
                .setCancelable(false);

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommunityManage community_buf = community;
                community_buf.setCommunityid(Integer.parseInt(inputId.getText().toString()));
                community_buf.setColumnCommunityname(inputName.getText().toString());
                //Log.d("spinner",inputManagerId.getSelectedItemId()+"");
                community_buf.setManager_id(accounts.get((int)inputManagerId.getSelectedItemPosition()).getId());
                if(shouldUpdate){
                    database.updateCommunity(community_buf);
                    mdata.set(position, community_buf);
                    mdata_account.set(position, db.getAccount(community_buf.getManager_id()));
                    MyCommunityRecycleAdapter.this.notifyItemChanged(position);
                }
                else {
                    community_buf.setId((int)database.insertCommunity(community_buf.getCommunity_id(), community_buf.getCommunity_name(),
                            community_buf.getDate(), community_buf.getManager_id()));
                    mdata.add(0, community_buf);
                    mdata_account.add(0, db.getAccount(community_buf.getManager_id()));
//                    mdata.clear();
//                    mdata.addAll(db.getAllCommunity());
//                    mdata_account.clear();
//                    addAccounts();
                    MyCommunityRecycleAdapter.this.notifyItemInserted(0);
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

//        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Show toast message when no text is entered
//                if (TextUtils.isEmpty(inputName.getText().toString())) {
//                    Toast.makeText(view.getContext(), "Enter note!", Toast.LENGTH_SHORT).show();
//                    return;
//                } else {
//                    alertDialog.dismiss();
//                }
//            }
//        });
    }

//    public View.ViewPager.OnPageChangeListener(){
//
//    }
}
