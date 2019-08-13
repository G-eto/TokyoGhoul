package com.example.tokyoghoul.database.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Blob;

public class Role {
    public static final String TABLE_NAME = "role";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_ROLE_IMG = "role_img";
    public static final String COLUMN_ROLE_NAME = "role_name";
    public static final String COLUMN_ROLE_KIND = "role_kind";
    public static final String COLUMN_ROLE_LEVEL = "role_level";
    public static final String COLUMN_ROLE_STONE_1 = "role_stone_1";
    public static final String COLUMN_ROLE_STONE_2 = "role_stone_2";
    public static final String COLUMN_ROLE_RUNE_1 = "role_rune_1";
    public static final String COLUMN_ROLE_RUNE_2 = "role_rune_2";
    public static final String COLUMN_ROLE_RUNE_3 = "role_rune_3";
    public static final String COLUMN_ROLE_RUNE_4 = "role_rune_4";
    public static final String COLUMN_ROLE_RUNE_SUIT_INTRODUCE = "role_rune_suit";
    public static final String COLUMN_ROLE_INTRODUCE = "role_introduce";
    public static final String COLUMN_ROLE_STATE = "role_state";

    public static final String CREATE_TABLE =
            " CREATE TABLE "+ TABLE_NAME + " ("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_ROLE_IMG + " BLOB,"
                    + COLUMN_ROLE_NAME + " TEXT,"
                    + COLUMN_ROLE_KIND + " TEXT,"
                    + COLUMN_ROLE_LEVEL + " TEXT,"
                    + COLUMN_ROLE_STONE_1 + " TEXT,"
                    + COLUMN_ROLE_STONE_2 + " TEXT,"
                    + COLUMN_ROLE_RUNE_1 + " TEXT,"
                    + COLUMN_ROLE_RUNE_2 + " TEXT,"
                    + COLUMN_ROLE_RUNE_3 + " TEXT,"
                    + COLUMN_ROLE_RUNE_4 + " TEXT,"
                    + COLUMN_ROLE_RUNE_SUIT_INTRODUCE + " TEXT,"
                    + COLUMN_ROLE_INTRODUCE + " TEXT,"
                    + COLUMN_ROLE_STATE + " TEXT )";

    private int id;
    private Bitmap role_img_show;//显示用的
    private byte[] role_img;
    private String role_name;
    private String role_kind;
    private String role_level;
    private String role_stone_1;
    private String role_stone_2;
    private String role_rune_1;
    private String role_rune_2;
    private String role_rune_3;
    private String role_rune_4;
    private String role_rune_suit;
    private String role_introduce;
    private String role_state;

    public Role(){
        this.role_state = null;
    }

    public Role(Bitmap bitmap, String role_name, String role_kind, String role_level,
                String role_stone_1, String role_stone_2,
                String role_rune_1, String role_rune_2, String role_rune_3, String role_rune_4,
                String role_rune_suit, String role_introduce, String role_state){

        this.role_img_show = bitmap;
        this.role_name = role_name;
        this.role_kind = role_kind;
        this.role_level = role_level;
        this.role_stone_1 = role_stone_1;
        this.role_stone_2 = role_stone_2;
        this.role_rune_1 = role_rune_1;
        this.role_rune_2 = role_rune_2;
        this.role_rune_3= role_rune_3;
        this.role_rune_4 = role_rune_4;
        this.role_rune_suit = role_rune_suit;
        this.role_introduce = role_introduce;
        this.role_state = role_state;
        this.role_img = bitmapToBytes(bitmap);
    }

    public Role(byte[] blob, String role_name, String role_kind, String role_level,
                String role_stone_1, String role_stone_2,
                String role_rune_1, String role_rune_2, String role_rune_3, String role_rune_4,
                String role_rune_suit, String role_introduce, String role_state){

        this.role_img = blob;
        this.role_name = role_name;
        this.role_kind = role_kind;
        this.role_level = role_level;
        this.role_stone_1 = role_stone_1;
        this.role_stone_2 = role_stone_2;
        this.role_rune_1 = role_rune_1;
        this.role_rune_2 = role_rune_2;
        this.role_rune_3= role_rune_3;
        this.role_rune_4 = role_rune_4;
        this.role_rune_suit = role_rune_suit;
        this.role_state = role_state;
        this.role_introduce = role_introduce;
        this.role_img_show = bytesToBitmap(blob);
    }

    public Role(int id, byte[] blob, String role_name, String role_kind, String role_level,
                String role_stone_1, String role_stone_2,
                String role_rune_1, String role_rune_2, String role_rune_3, String role_rune_4,
                String role_rune_suit, String role_state){
        this.id = id;
        this.role_img = blob;
        this.role_name = role_name;
        this.role_kind = role_kind;
        this.role_level = role_level;
        this.role_stone_1 = role_stone_1;
        this.role_stone_2 = role_stone_2;
        this.role_rune_1 = role_rune_1;
        this.role_rune_2 = role_rune_2;
        this.role_rune_3= role_rune_3;
        this.role_rune_4 = role_rune_4;
        this.role_rune_suit = role_rune_suit;
        this.role_state = role_state;
        this.role_introduce = "";
        this.role_img_show = bytesToBitmap(blob);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRole_img_show(Bitmap role_img_show) {
        this.role_img_show = role_img_show;
    }

    public void setRole_img(byte[] role_img) {
        this.role_img = role_img;
        this.role_img_show = bytesToBitmap(role_img);
    }

    public void setRole_introduce(String role_introduce) {
        this.role_introduce = role_introduce;
    }

    public void setRole_kind(String role_kind) {
        this.role_kind = role_kind;
    }

    public void setRole_level(String role_level) {
        this.role_level = role_level;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }

    public void setRole_rune_1(String role_rune_1) {
        this.role_rune_1 = role_rune_1;
    }

    public void setRole_rune_2(String role_rune_2) {
        this.role_rune_2 = role_rune_2;
    }

    public void setRole_rune_3(String role_rune_3) {
        this.role_rune_3 = role_rune_3;
    }

    public void setRole_rune_4(String role_rune_4) {
        this.role_rune_4 = role_rune_4;
    }

    public void setRole_rune_suit(String role_rune_suit) {
        this.role_rune_suit = role_rune_suit;
    }

    public void setRole_state(String role_state) {
        this.role_state = role_state;
    }

    public void setRole_stone_1(String role_stone_1) {
        this.role_stone_1 = role_stone_1;
    }

    public void setRole_stone_2(String role_stone_2) {
        this.role_stone_2 = role_stone_2;
    }

    //get


    public String getRole_kind() {
        return role_kind;
    }

    public int getId() {
        return id;
    }

    public Bitmap getRole_img_show() {

        return role_img_show;
    }

    public byte[] getRole_img() {
        return role_img;
    }

    public String getRole_introduce() {
        return role_introduce;
    }

    public String getRole_level() {
        return role_level;
    }

    public String getRole_name() {
        return role_name;
    }

    public String getRole_rune_1() {
        return role_rune_1;
    }

    public String getRole_rune_2() {
        return role_rune_2;
    }

    public String getRole_rune_3() {
        return role_rune_3;
    }

    public String getRole_rune_4() {
        return role_rune_4;
    }

    public String getRole_rune_suit() {
        return role_rune_suit;
    }

    public String getRole_state() {
        return role_state;
    }

    public String getRole_stone_1() {
        return role_stone_1;
    }

    public String getRole_stone_2() {
        return role_stone_2;
    }

    public byte[] bitmapToBytes(Bitmap bitmap){
        //将图片转化为位图
        byte[] imagedata = null;
        if(bitmap == null) {
            Log.d("dssd",role_name);
            return imagedata;
        }
        int size = bitmap.getWidth() * bitmap.getHeight() * 4;
        //创建一个字节数组输出流,流的大小为size
        ByteArrayOutputStream baos= new ByteArrayOutputStream(size);
        try {
            //设置位图的压缩格式，质量为100%，并放入字节数组输出流中
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            //将字节数组输出流转化为字节数组byte[]
            imagedata = baos.toByteArray();
            Log.d("bssb",role_name);
            return imagedata;
        }catch (Exception e){
        }finally {
            try {
                //bitmap.recycle();
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.d("pssd",role_name);
        return new byte[0];
    }
    public Bitmap bytesToBitmap(byte[] blob){
        return BitmapFactory.decodeByteArray(blob,0,blob.length);
    }
}
