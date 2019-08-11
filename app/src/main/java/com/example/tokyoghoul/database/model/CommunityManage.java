package com.example.tokyoghoul.database.model;

import com.bin.david.form.annotation.SmartColumn;
import com.bin.david.form.annotation.SmartTable;

/*
社团与boss管理
 */
@SmartTable(name="社团与boss时间管理")
public class CommunityManage {
    public static final String TABLE_NAME = "community_manage";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_COMMUNITY_ID = "community_id";
    public static final String COLUMN_COMMUNITY_NAME = "community_name";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_MANAGER_ID = "manager_id";

    public static final String CREATE_TABLE =
            " CREATE TABLE "+ TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_COMMUNITY_ID + " INTEGER UNIQUE, "
                    + COLUMN_COMMUNITY_NAME + " TEXT, "
                    + COLUMN_DATE + " DATETIME UNIQUE,"
                    + COLUMN_MANAGER_ID + " INTEGER )";

    private int id;
    @SmartColumn(id = 0, name = "社团编号")
    private int community_id;
    @SmartColumn(id = 1, name = "社团")
    private String community_name;
    @SmartColumn(id = 2, name = "最后进入时间")
    private String date;
    @SmartColumn(id = 3, name = "管理员")
    private int manager_id;
    @SmartColumn(id = 4, name = "今日翻牌")
    private boolean click;

    public CommunityManage(){
        this.click = false;
        this.id = 0;
        this.community_id = -1;
        this.community_name = "未加入社团";
        this.date = null;
        this.manager_id = '\0';
    }

    public CommunityManage(int id, int c_id, String name, String date, int manager_id){
        this.id = id;
        this.community_id = c_id;
        this.community_name = name;
        this.date = date;
        this.manager_id = manager_id;
        this.click = false;
    }

    public void setId(int id) { this.id = id; }
    public void setCommunityid(int id){ this.community_id = id; }
    public void setColumnCommunityname(String name){ this.community_name = name; }
    public void setDate(String date){ this.date = date; }
    public void setClick(boolean b){ this.click = b; }
    public void setManager_id(int id){ this.manager_id = id; }

    public int getId(){ return id; }
    public int getCommunity_id(){ return community_id; }
    public String getCommunity_name(){ return community_name; }
    public String getDate(){ return date; }
    public boolean getClick(){ return click; }
    public int getManager_id(){return manager_id; }

}
