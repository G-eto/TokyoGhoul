package com.example.tokyoghoul.database.model;

public class CDKs {
    public static final String TABLE_NAME = "cdks";

    public static final String COLUMN_CDK_ID = "id";
    public static final String COLUMN_CDK = "cdk";
    public static final String COLUMN_CDK_DATE = "cdk_date";
    public static final String COLUMN_CDK_TEXT = "cdk_text";

    public static final String CREATE_TABLE =
            " CREATE TABLE "+ TABLE_NAME + " ("
                    + COLUMN_CDK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_CDK + " TEXT,"
                    + COLUMN_CDK_TEXT + " TEXT,"
                    + COLUMN_CDK_DATE + " DATE)";

    private int id;
    private String cdk;
    private String cdk_date;
    private String cdk_text;

    public CDKs(){}

    public CDKs(int id, String cdk, String cdk_date, String cdk_text){
        this.id = id;
        this.cdk = cdk;
        this.cdk_date = cdk_date;
        this.cdk_text = cdk_text;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCdk(String cdk) {
        this.cdk = cdk;
    }

    public void setCdk_date(String cdk_date) {
        this.cdk_date = cdk_date;
    }

    public void setCdk_text(String cdk_text) {
        this.cdk_text = cdk_text;
    }

    public int getId() {
        return id;
    }

    public String getCdk() {
        return cdk;
    }

    public String getCdk_date() {
        return cdk_date;
    }

    public String getCdk_text() {
        return cdk_text;
    }
}
