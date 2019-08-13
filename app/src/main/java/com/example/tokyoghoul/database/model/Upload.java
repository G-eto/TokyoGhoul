package com.example.tokyoghoul.database.model;

public class Upload {
    public static final String TABLE_NAME = "upload";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_TEXT = "text";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_KIND = "kind";

    public static final String CREATE_TABLE =
            " CREATE TABLE "+ TABLE_NAME + " ("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_TITLE + " TEXT,"
                    + COLUMN_TEXT + " TEXT,"
                    + COLUMN_PHONE + " TEXT,"
                    + COLUMN_KIND + " TEXT,"
                    + COLUMN_DATE + " DATETIME)";

    private int id;
    private String title;
    private String date;
    private String text;
    private String phone;
    private String kind;

    public Upload(){}

    public Upload(int id, String title, String text, String phone, String date, String kind){
        this.id = id;
        this.date = date;
        this.text = text;
        this.title = title;
        this.phone = phone;
        this.kind = kind;
    }

    public Upload(String title, String text, String phone, String date, String kind){
        this.date = date;
        this.text = text;
        this.title = title;
        this.phone = phone;
        this.kind = kind;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getPhone() {
        return phone;
    }

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }

    public String getKind() {
        return kind;
    }
}
