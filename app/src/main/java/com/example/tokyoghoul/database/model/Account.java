package com.example.tokyoghoul.database.model;

public class Account {
    public static final String TABLE_NAME = "account_manage";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_ACCOUNT_PORT = "account_port";
    public static final String COLUMN_ACCOUNT_ROLE = "account_role";
    public static final String COLUMN_ACCOUNT_NUMBER = "account_number";
    public static final String COLUMN_ACCOUNT_PASSWORD = "account_password";

    public static final String CREATE_TABLE =
            " CREATE TABLE "+ TABLE_NAME + " ("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_ACCOUNT_PORT + " TEXT, "
                    + COLUMN_ACCOUNT_ROLE + " TEXT, "
                    + COLUMN_ACCOUNT_NUMBER + " TEXT,"
                    + COLUMN_ACCOUNT_PASSWORD + " TEXT )";

    private int id;
    private String account_port;
    private String account_role;
    private String account_number;
    private String account_password;

    public Account(){
        this.id = 0;
        this.account_port = "无";
        this.account_role = "无";
        this.account_number = "无";
        this.account_password = "";
    }

    public Account(int id, String port, String role, String number, String password){
        this.id = id;
        this.account_port = port;
        this.account_role = role;
        this.account_number = number;
        this.account_password = password;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setAccount_port(String account_port) {
        this.account_port = account_port;
    }

    public void setAccount_role(String account_role) {
        this.account_role = account_role;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public void setAccount_password(String account_password) {
        this.account_password = account_password;
    }

    public int getId() {
        return id;
    }

    public String getAccount_port() {
        return account_port;
    }

    public String getAccount_role() {
        return account_role;
    }

    public String getAccount_number() {
        return account_number;
    }

    public String getAccount_password() {
        return account_password;
    }
}
