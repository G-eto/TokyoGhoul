package com.example.tokyoghoul.database.model;

import com.example.tokyoghoul.activity.dummy.DummyContent;

/*
攻略 & 手记

 */
public class Psp {
    public static final String TABLE_NAME = "psp_record";

    public static final String COLUMN_PSP_ID = "id";
    public static final String COLUMN_PSP_TITLE = "psp_title";
    public static final String COLUMN_PSP_KIND = "psp_kind";
    public static final String COLUMN_PSP_TIME = "psp_time";
    public static final String COLUMN_PSP_AUTHOR = "psp_author";
    public static final String COLUMN_PSP_TEXT = "psp_text";
    public static final String COLUMN_PSP_WEB_ID = "psp_webid";
    public static final String COLUMN_PSP_WEB_EDITION = "psp_web_edition";

    public static final String CREATE_TABLE =
            " CREATE TABLE "+ TABLE_NAME + " ("
                    + COLUMN_PSP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_PSP_TITLE + " TEXT,"
                    + COLUMN_PSP_KIND + " TEXT,"
                    + COLUMN_PSP_TIME + " DATE DEFAULT ( datetime( 'now', 'localtime' ) ),"
                    + COLUMN_PSP_AUTHOR + " TEXT,"
                    + COLUMN_PSP_TEXT + " TEXT,"
                    + COLUMN_PSP_WEB_ID + " INTEGER,"
                    + COLUMN_PSP_WEB_EDITION + " INTEGER)";

    private int id;//
    private String psp_title;
    private String psp_kind;
    private String psp_time;
    private String psp_author;
    private String psp_text;
    private int psp_webid;
    private int psp_web_edition;

    public Psp(){ this.psp_webid = 0;psp_web_edition = 0;}
    public Psp(String psp_title, String psp_kind, String time, String author, String psp_text){
        this.psp_title = psp_title;
        this.psp_kind = psp_kind;
        this.psp_time = time;
        this.psp_author = author;
        this.psp_text = psp_text;
        this.psp_webid = 0;
        this.psp_web_edition = 0;
    }
    public Psp(String psp_title, String psp_kind, String time, String author,
               String psp_text, int psp_webid, int psp_web_edition){
        this.psp_title = psp_title;
        this.psp_kind = psp_kind;
        this.psp_time = time;
        this.psp_author = author;
        this.psp_text = psp_text;
        this.psp_webid = psp_webid;
        this.psp_web_edition = psp_web_edition;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPsp_author(String psp_author) {
        this.psp_author = psp_author;
    }

    public void setPsp_webid(int psp_webid) {
        this.psp_webid = psp_webid;
    }

    public void setPsp_web_edition(int psp_web_edition) {
        this.psp_web_edition = psp_web_edition;
    }

    public void setPsp_kind(String psp_kind) {
        this.psp_kind = psp_kind;
    }

    public void setPsp_text(String psp_text) {
        this.psp_text = psp_text;
    }

    public void setPsp_time(String psp_time) {
        this.psp_time = psp_time;
    }

    public void setPsp_title(String psp_title) {
        this.psp_title = psp_title;
    }

    public int getId() {
        return id;
    }

    public int getPsp_webid() {
        return psp_webid;
    }

    public int getPsp_web_edition() {
        return psp_web_edition;
    }

    public String getPsp_author() {
        return psp_author;
    }

    public String getPsp_kind() {
        return psp_kind;
    }

    public String getPsp_text() {
        return psp_text;
    }

    public String getPsp_time() {
        return psp_time;
    }

    public String getPsp_title() {
        return psp_title;
    }

    public DummyContent.DummyItem toDummyItem(){
        return new DummyContent.DummyItem(String.valueOf(this.id), this.psp_title,
                this.psp_kind, this.psp_author, this.psp_time,
                this.psp_text, this.psp_webid, this.psp_web_edition);
    }
}
