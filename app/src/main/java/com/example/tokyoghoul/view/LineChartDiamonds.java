package com.example.tokyoghoul.view;

import android.app.ActivityManager;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.example.tokyoghoul.R;
import com.example.tokyoghoul.activity.MainActivity;
import com.example.tokyoghoul.database.DatabaseHelper;
import com.example.tokyoghoul.database.model.Logs;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class LineChartDiamonds{
    private LineChart mLineChart;
    Typeface mTf; // 自定义显示字体
    Context mcontext;
    ArrayList<Entry> yValues = new ArrayList<Entry>();
    ArrayList<String> datelist = new ArrayList<>();
    List<Logs> list = new ArrayList<>();
    ArrayList<String> xValues = new ArrayList<>();
    DatabaseHelper db;
    int[] dateplus = new int[1500];

    public LineChartDiamonds(LineChart lineChart, Context context, List<Logs> list){
        db = new DatabaseHelper(context);

        this.list.addAll(db.getAllLogs("ASC"));
        mcontext = context;
        mLineChart = lineChart;
        drawTheChartByMPAndroid();
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.fade_blue);
        setChartFillDrawable(drawable);
        setMarkerView();
    }

    private void drawTheChartByMPAndroid() {
        LineData lineData = getLineData(list.size(), 1000);

        showChart(mLineChart, lineData, Color.rgb(255, 255, 255));
    }

    private void showChart(LineChart lineChart, LineData lineData, int color) {
        lineChart.setDrawBorders(false); //在折线图上添加边框
        //lineChart.setDescription(new Description()); //数据描述
        //lineChart.setNoDataTextDescription("You need to provide data for the chart.");
        Description description = new Description();
//        description.setText("需要展示的内容");
        description.setEnabled(false);
        lineChart.setDescription(description);
        lineChart.setNoDataText("暂无数据");

        lineChart.setDrawGridBackground(false); //表格颜色
        lineChart.setGridBackgroundColor(Color.BLACK & 0x70000000); //表格的颜色，设置一个透明度

        lineChart.setTouchEnabled(true); //可点击

        lineChart.setDragEnabled(true);  //可拖拽
        lineChart.setScaleEnabled(true);  //可缩放

        lineChart.setPinchZoom(false);

        lineChart.setBackgroundColor(color); //设置背景颜色

        lineChart.setData(lineData);  //填充数据

        Legend mLegend = lineChart.getLegend(); //设置标示，就是那个一组y的value的

        mLegend.setForm(Legend.LegendForm.LINE); //样式
        mLegend.setFormSize(6f); //字体
        mLegend.setTextColor(Color.BLACK); //颜色

        lineChart.setVisibleXRange(0, 7);   //x轴可显示的坐标范围
        XAxis xAxis = lineChart.getXAxis();  //x轴的标示
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //x轴位置
        xAxis.setTextColor(Color.BLACK);    //字体的颜色
        xAxis.setTextSize(10f); //字体大小
        xAxis.setGridColor(Color.BLACK);//网格线颜色
        xAxis.setDrawGridLines(false); //不显示网格线
        xAxis.setTypeface(mTf);
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xValues));

        YAxis axisLeft = lineChart.getAxisLeft(); //y轴左边标示
        YAxis axisRight = lineChart.getAxisRight(); //y轴右边标示
        axisLeft.setTextColor(Color.BLACK); //字体颜色
        axisLeft.setTextSize(10f); //字体大小
        //axisLeft.setAxisMaximum(100000); //最大值
        axisLeft.setAxisMinimum(0f);
        axisLeft.setLabelCount(8, true); //显示格数
        axisLeft.setGridColor(Color.BLACK); //网格线颜色
        axisLeft.setTypeface(mTf);

        axisRight.setDrawAxisLine(false);
        axisRight.setDrawGridLines(false);
        axisRight.setDrawLabels(false);



        lineChart.animateX(500);  //立即执行动画
    }

    private LineData getLineData(int count, float range) {
        for (int i = 0; i < count; i++) {
            // x轴显示的数据，这里默认使用数字下标显示
            xValues.add(list.get(i).getDate().substring(5, 10));
            datelist.add(list.get(i).getDate());
            dateplus[i] = list.get(i).getDiamondsIncome();
        }




        // y轴的数据
        yValues = new ArrayList<Entry>();
        for (int i = 0; i < count; i++) {
            float value = list.get(i).getDiamondsAll();
            yValues.add(new Entry(i, value));
        }
        // create a dataset and give it a type
        // y轴的数据集合
        LineDataSet lineDataSet = new LineDataSet(yValues, "钻石统计");
        // mLineDataSet.setFillAlpha(110);
        // mLineDataSet.setFillColor(Color.RED);

        //用y轴的集合来设置参数
//        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSet.setLineWidth(1.75f); // 线宽
        lineDataSet.setCircleRadius(2f);// 显示的圆形大小
        lineDataSet.setColor(Color.rgb(240,255,255));// 显示颜色
        lineDataSet.setCircleColor(Color.BLUE);// 圆形的颜色
        lineDataSet.setHighLightColor(Color.WHITE); // 高亮的线的颜色
        lineDataSet.setHighlightEnabled(true);
        lineDataSet.setValueTextColor(Color.BLACK); //数值显示的颜色
        lineDataSet.setValueTextSize(8f);     //数值显示的大小
        lineDataSet.setValueTypeface(mTf);

        lineDataSet.setDrawFilled(true);
        lineDataSet.setFormLineWidth(1f);
        lineDataSet.setFormSize(15.f);
        lineDataSet.setDrawCircles(false);

        ArrayList<LineDataSet> lineDataSets = new ArrayList<LineDataSet>();
        lineDataSets.add(lineDataSet); // 添加数据集合

        //创建lineData
        LineData lineData = new LineData(lineDataSet);

        return lineData;
    }

    /**
     * 设置线条填充背景颜色
     *
     * @param drawable
     */
    public void setChartFillDrawable(Drawable drawable) {
        if (mLineChart.getData() != null && mLineChart.getData().getDataSetCount() > 0) {
            LineDataSet lineDataSet = (LineDataSet) mLineChart.getData().getDataSetByIndex(0);
            //避免在 initLineDataSet()方法中 设置了 lineDataSet.setDrawFilled(false); 而无法实现效果
            lineDataSet.setDrawFilled(true);
            lineDataSet.setFillDrawable(drawable);
            mLineChart.invalidate();
        }
    }

    /**
     * 设置 可以显示X Y 轴自定义值的 MarkerView
     */
    public void setMarkerView() {
        LineChartMarkView mv = new LineChartMarkView(mcontext, datelist, dateplus);
        mv.setChartView(mLineChart);
        mLineChart.setMarker(mv);
        mLineChart.invalidate();
    }

    public void refresh(){
        list.clear();
        list.addAll(db.getAllLogs("ASC"));
        mLineChart.notifyDataSetChanged();
        drawTheChartByMPAndroid();
        Drawable drawable = ContextCompat.getDrawable(mcontext, R.drawable.fade_blue);
        setChartFillDrawable(drawable);
        setMarkerView();

        //mLineChart.invalidate(); // refresh
    }
}