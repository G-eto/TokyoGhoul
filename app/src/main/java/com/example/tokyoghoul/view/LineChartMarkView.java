package com.example.tokyoghoul.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.example.tokyoghoul.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class LineChartMarkView extends MarkerView {

    private TextView tvDate;
    private TextView tvValue;
    private TextView tvPlus;
    private List<String> datelist;
    private int[] plus;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss" );

    public LineChartMarkView(Context context, List<String> list, int[] plus) {
        super(context, R.layout.layout_markview);
        this.datelist = list;
        this.plus = plus;
        tvDate = findViewById(R.id.tv_date);
        tvValue = findViewById(R.id.tv_value);
        tvPlus = findViewById(R.id.tv_plus);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        //展示自定义X轴值 后的X轴内容
        tvDate.setText(datelist.get((int) e.getX()));
        tvValue.setText("钻石余额：" + (int)e.getY());
        tvPlus.setText("今日收益:"+plus[(int)e.getX()]+"");
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }

}