package com.example.tokyoghoul.database.model;

import com.bin.david.form.annotation.SmartColumn;
import com.bin.david.form.annotation.SmartTable;

/*
钻石日志数据
 */
@SmartTable(name = "往日记录", count = false)
public class Logs {

    public static final String TABLE_NAME = "logs";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_ONLINETIME = "onlinetimes";
    public static final String COLUMN_DIAMONDSALL = "diamondsall";
    public static final String COLUMN_DIAMONDSINCOME = "diamondsincome";


    //钻石
    private int id;
    @SmartColumn(id = 1, name = "日期")
    private String date;
    //@SmartColumn(id = 1, name = "在线/min")
    private int onlineTime;//min
    @SmartColumn(id = 3, name = "钻石余量")
    private int diamondsAll;
    @SmartColumn(id = 2, name = "净收益")
    private int diamondsIncome;

    public static final String CREATE_TABLE =
            " CREATE TABLE "+ TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_DATE + " DATE DEFAULT ( datetime( 'now', 'localtime' ) ), "
            + COLUMN_ONLINETIME + " INTEGER, "
            + COLUMN_DIAMONDSALL + " INTEGER, "
            + COLUMN_DIAMONDSINCOME + " INTEGER) ";

    public Logs(){}

    public Logs(int id, String date, int onlineTime, int diamondsAll,
                int diamondsIncome){
        this.id = id;
        this.date = date;
        this.diamondsIncome = diamondsIncome;
        this.diamondsAll = diamondsAll;
        this.onlineTime = onlineTime;
    }

    public void setId(int id){ this.id = id; }
    public void setDate(String date){ this.date = date; }
    public void setDiamondsAll(int n){ this.diamondsAll = n; }
    public void setDiamondsIncome(int n){ this.diamondsIncome = n; }
    public void setOnlineTime(int n){ this.onlineTime = n; }

    public String getDate(){ return date; }
    public int getId(){ return id; }
    public int getDiamondsAll(){ return diamondsAll; }
    public int getDiamondsIncome(){ return diamondsIncome; }
    public int getOnlineTime(){ return onlineTime; }
}
