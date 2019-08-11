package com.example.tokyoghoul.database.model;

public class GamePlay {
    public static final String TABLE_NAME = "game_play";

    public static final String COLUMN_PLAY_ID = "id";
    public static final String COLUMN_PLAY_TITLE = "play_title";
    public static final String COLUMN_PLAY_KIND = "play_kind";
    public static final String COLUMN_PLAY_TIME_START = "play_time_start";
    public static final String COLUMN_PLAY_TIME_END = "play_time_end";
    public static final String COLUMN_PLAY_TEXT = "play_text";

    public static final String CREATE_TABLE =
            " CREATE TABLE "+ TABLE_NAME + " ("
                    + COLUMN_PLAY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_PLAY_TITLE + " TEXT,"
                    + COLUMN_PLAY_KIND + " TEXT,"
                    + COLUMN_PLAY_TIME_START + " DATE,"
                    + COLUMN_PLAY_TIME_END + " DATE,"
                    + COLUMN_PLAY_TEXT + " TEXT)";


    private int id;
    private String play_title;
    private String play_kind;
    private String play_time_start;
    private String play_time_end;
    private String play_text;

    public GamePlay(){}

    public GamePlay(int id, String play_title, String play_kind, String play_time_start,
                    String play_time_end, String play_text){
        this.id = id;
        this.play_title = play_title;
        this.play_kind = play_kind;
        this.play_time_start = play_time_start;
        this.play_time_end = play_time_end;
        this.play_text = play_text;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPlay_kind(String play_kind) {
        this.play_kind = play_kind;
    }

    public void setPlay_text(String play_text) {
        this.play_text = play_text;
    }

    public void setPlay_time_end(String play_time_end) {
        this.play_time_end = play_time_end;
    }

    public void setPlay_time_start(String play_time_start) {
        this.play_time_start = play_time_start;
    }

    public void setPlay_title(String play_title) {
        this.play_title = play_title;
    }

    public int getId() {
        return id;
    }

    public String getPlay_kind() {
        return play_kind;
    }

    public String getPlay_text() {
        return play_text;
    }

    public String getPlay_time_end() {
        return play_time_end;
    }

    public String getPlay_time_start() {
        return play_time_start;
    }

    public String getPlay_title() {
        return play_title;
    }
}
