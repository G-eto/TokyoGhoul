package com.example.tokyoghoul.view;

import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.tokyoghoul.R;
import com.example.tokyoghoul.database.DatabaseHelper;
import com.example.tokyoghoul.database.model.Account;
import com.example.tokyoghoul.database.model.Account;
import com.example.tokyoghoul.tool.Gadget;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyAccountRecycleAdapter extends RecyclerView.Adapter<MyAccountRecycleAdapter.MyViewHolder>{

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public final ImageView img_port;
        public final TextView account_port;
        public final TextView account_role;
        public final TextView account_number;
        public final TextView account_password;

        public MyViewHolder(View v) {
            super(v);
            img_port = v.findViewById(R.id.img_port_account);
            account_role = (TextView) v.findViewById(R.id.role_account);
            account_number = (TextView) v.findViewById(R.id.number_account);
            account_password = (TextView) v.findViewById(R.id.password_account);
            account_port = (TextView) v.findViewById(R.id.port_account);
        }
    }

    private List<Account> mdata = new ArrayList<>();
    DatabaseHelper db;
    View mview;

    public MyAccountRecycleAdapter(View view){
        db = new DatabaseHelper(view.getContext());
//        db.insertCommunity(1,"大撒孩子地方","08/05 13:14",12);
//        db.insertCommunity(2,"大傻子","08/06 13:14",1);
//        db.insertCommunity(7770,"哈子","08/07 13:14",2);
//        db.insertCommunity(3,"孩子","08/01 13:14",4);
////
//        db.insertAccount("QQ","焰灵姬","960789981","xxx");
//        db.insertAccount("微信","小号","9607899819","xxx");
//        db.insertAccount("华为","冷月","9607","xxx");
//        db.insertAccount("小米","test","xiaomi969981","xxx");

        mview = view;
        this.mdata = db.getAllAccounts();

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int position) {
        //myViewHolder.img_port.set
        myViewHolder.account_port.setText(mdata.get(position).getAccount_port());
        myViewHolder.account_role.setText(mdata.get(position).getAccount_role());
        myViewHolder.account_number.setText(mdata.get(position).getAccount_number());
        myViewHolder.account_password.setText(mdata.get(position).getAccount_password());

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //item 点击事件
                //Toast.makeText(v.getContext(), "点击了第" + myViewHolder.getAdapterPosition() + "个", Toast.LENGTH_SHORT).show();
                final AlertView dialog = new AlertView("选择操作",
                        "小号怎么了",
                        "取消",
                        new String[]{"删除"},
                        new String[]{"复制账号", "复制密码", "修改账号信息"},
                        v.getContext(),
                        AlertView.Style.ActionSheet,
                        new OnItemClickListener(){
                            @Override
                            public void onItemClick(Object o, int position_son) {
                                int op =  myViewHolder.getAdapterPosition();
                                if(position_son == 0) {
                                    //delete
                                    db.deleteAccount(mdata.get(op));
                                    mdata.remove(op);
                                    //mdata.remove();
                                    notifyItemRemoved(op);

                                    Toast.makeText(v.getContext(), "已删除", Toast.LENGTH_SHORT).show();

                                }
                                else if(position_son == 1){
                                    //copy number
                                    Gadget.copyStr(mdata.get(op).getAccount_number(), v.getContext());
                                }
                                else if(position_son == 2){
                                    //copy psd
                                    Gadget.copyStr(mdata.get(op).getAccount_password(), v.getContext());
                                }
                                else if(position_son == 3){
                                    //update
                                    showNoteDialog(true, mdata.get(op), mview, op);
                                }
//                                Log.d("delete",""+position+" "+mdata.get(position).getAccount_role());
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

    public void showNoteDialog(final boolean shouldUpdate, final Account community, final View _view, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from((_view.getContext()));
        final View view = layoutInflaterAndroid.inflate(R.layout.account_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(view.getContext());
        alertDialogBuilderUserInput.setView(view);

        final EditText inputRole = view.findViewById(R.id.account_input_role);
        final EditText inputNumber = view.findViewById(R.id.account_input_number);
        final Spinner port = view.findViewById(R.id.account_port_spinner);
        final EditText inputPsw = view.findViewById(R.id.account_input_password);
        final Button cancel = view.findViewById(R.id.dialog_account_cancel);
        final Button save = view.findViewById(R.id.dialog_account_save);


        final List<Account> accounts = new ArrayList<>();
        final DatabaseHelper database = new DatabaseHelper(view.getContext());
        accounts.add(new Account());
        accounts.addAll(database.getAllAccounts());

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                view.getContext(), R.array.port_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        port.setAdapter(adapter);


        final String[] portText = new String[]{"QQ", "微信", "小米", "华为", "苹果", "魅族", "oppo","vivo",
                                        "b站", "其他"};
        int op_port_index = 9;
        for(int i = 0; i < portText.length; i++){
            if(portText[i].equals(community.getAccount_port())){
                op_port_index = i;
                break;
            }
        }

//        TextView dialogTitle = view.findViewById(R.id.dialog_title);
        save.setHint(!shouldUpdate ? "保存" : "修改");

        if (shouldUpdate && community.getId() > 0) {
            inputRole.setText(community.getAccount_role());
            inputNumber.setText(community.getAccount_number());
            port.setSelection(op_port_index);
            inputPsw.setText(community.getAccount_password());
        }
        alertDialogBuilderUserInput
                .setCancelable(false);

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Account community_buf = community;
                community_buf.setAccount_port(portText[port.getSelectedItemPosition()]);

                community_buf.setAccount_role(inputRole.getText().toString());
                community_buf.setAccount_number(inputNumber.getText().toString());
                community_buf.setAccount_password(inputPsw.getText().toString());

                if(shouldUpdate){
                    database.updateAccount(community_buf);
                    mdata.set(position, community_buf);
                    MyAccountRecycleAdapter.this.notifyItemChanged(position);

//                    database.updateCommunity(community_buf);
//                    mdata.set(position, community_buf);
//                    mdata_account.set(position, db.getAccount(community_buf.getManager_id()));
//                    MyCommunityRecycleAdapter.this.notifyItemChanged(position);
                }
                else {
                    community_buf.setId((int)database.insertAccount(
                            community_buf.getAccount_port(),
                            community_buf.getAccount_role(),
                            community_buf.getAccount_number(),
                            community_buf.getAccount_password()));
                    mdata.add(0, community_buf);

                    MyAccountRecycleAdapter.this.notifyItemInserted(0);

//                    mdata.clear();
//                    mdata.addAll(db.getAllCommunity());
//                    mdata_account.clear();
//                    addAccounts();
//                    MyAccountRecycleAdapter.this.notifyDataSetChanged();
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
}
