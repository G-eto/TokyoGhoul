package com.example.tokyoghoul.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.contrarywind.listener.OnItemSelectedListener;
import com.contrarywind.view.WheelView;
import com.example.tokyoghoul.R;
import com.example.tokyoghoul.database.model.Account;

import java.util.ArrayList;
import java.util.List;

public class MySpinnerAdapter extends BaseAdapter {

    private List<Account> mList;
    private Context mContext;
    private int m_id;

    //端口图片[]
    private int[] iconPic = new int[]{R.drawable.qq, R.drawable.weixin, R.drawable.xiaomi,
            R.drawable.huawei, R.drawable.pingguo, R.drawable.meizu,R.drawable.oppo,
            R.drawable.vivo ,R.drawable.bilibili, R.drawable.zhanghao};
    private String[] portText = new String[]{"QQ", "微信", "小米", "华为", "苹果", "魅族", "oppo","vivo",
            "b站", "无"};

    public MySpinnerAdapter(Context context, List<Account> list, int m_id){
        this.mContext = context;
        this.mList = list;
        this.m_id = m_id;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return mList.get(i).getId();
    }

//    @Override
//    public View getView(int i, View view, ViewGroup viewGroup) {
//
//        return null;
//    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        LayoutInflater _LayoutInflater=LayoutInflater.from(mContext);
        convertView = _LayoutInflater.inflate(R.layout.spinner_item_custom, null);
        ImageView img_port = convertView.findViewById(R.id.img_port);
        TextView port = convertView.findViewById(R.id.tv_port);
        TextView role = convertView.findViewById(R.id.tv_role);
        TextView number = convertView.findViewById(R.id.tv_number);

        for(int i = 0; i < portText.length; i++){
            if(mList.get(position).getAccount_port().equals(portText[i])){
                img_port.setImageResource(iconPic[i]);
                break;
            }
        }


        port.setText(mList.get(position).getAccount_port());
        role.setText(mList.get(position).getAccount_role());
        number.setText(mList.get(position).getAccount_number());

        return convertView;
    }
}
