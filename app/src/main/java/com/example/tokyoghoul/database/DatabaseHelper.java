package com.example.tokyoghoul.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.tokyoghoul.database.model.Account;
import com.example.tokyoghoul.database.model.CDKs;
import com.example.tokyoghoul.database.model.CommunityManage;
import com.example.tokyoghoul.database.model.GamePlay;
import com.example.tokyoghoul.database.model.Logs;
import com.example.tokyoghoul.database.model.Psp;
import com.example.tokyoghoul.database.model.Role;
import com.example.tokyoghoul.database.model.Upload;
import com.example.tokyoghoul.tool.Gadget;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "tokyo_ghoul_db";

    private Context mcontext;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mcontext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Role.CREATE_TABLE);
        db.execSQL(CommunityManage.CREATE_TABLE);
        db.execSQL(Logs.CREATE_TABLE);
        db.execSQL(Account.CREATE_TABLE);
        db.execSQL(Psp.CREATE_TABLE);
        db.execSQL(Upload.CREATE_TABLE);
//        db.execSQL(GamePlay.CREATE_TABLE);
//        db.execSQL(CDKs.CREATE_TABLE);


        //initRoleTable(mcontext);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + Role.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CommunityManage.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Logs.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CommunityManage.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Psp.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Upload.TABLE_NAME);
//        db.execSQL("DROP TABLE IF EXISTS " + CDKs.TABLE_NAME);
//        db.execSQL("DROP TABLE IF EXISTS " + GamePlay.TABLE_NAME);

        onCreate(db);
    }

    /*
    钻石日志
     */

    /*
    add a diamond log
     */
    public long insertLog(Logs diamondLog){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Logs.COLUMN_DATE, diamondLog.getDate());
        values.put(Logs.COLUMN_ONLINETIME, diamondLog.getOnlineTime());
        values.put(Logs.COLUMN_DIAMONDSALL, diamondLog.getDiamondsAll());
        values.put(Logs.COLUMN_DIAMONDSINCOME, diamondLog.getDiamondsIncome());

        long id = db.insert(Logs.TABLE_NAME, null, values);
        db.close();

        updateLog(getLog((int)id));

        return id;
    }

    /*
    delete not be allowed
     */
    protected void deleteLog(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Logs.TABLE_NAME, Logs.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }
    /*
    update a log
     */
    public void updateLog(Logs diamondLog){
        int add = 0;
        //更新自己和后面一个的add
        List<Logs> list = getAllLogs("DESC");
        Logs after = new Logs();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        for(int i = 0; i < list.size(); i++){
            if(list.get(i).getDate().equals(diamondLog.getDate())){
                if(i - 1 >= 0){
                    after = list.get(i - 1);
                    after.setDiamondsIncome(after.getDiamondsAll() - diamondLog.getDiamondsAll());
                    ContentValues values2 = new ContentValues();
                    values2.put(Logs.COLUMN_DATE, after.getDate());
                    values2.put(Logs.COLUMN_ONLINETIME, after.getOnlineTime());
                    values2.put(Logs.COLUMN_DIAMONDSALL, after.getDiamondsAll());
                    values2.put(Logs.COLUMN_DIAMONDSINCOME, after.getDiamondsIncome());

                    db.update(Logs.TABLE_NAME, values2, Logs.COLUMN_ID + " = ?",
                            new String[]{String.valueOf(after.getId())});
                }

                if(i + 1 < list.size()){
                    diamondLog.setDiamondsIncome(diamondLog.getDiamondsAll() - list.get(i + 1).getDiamondsAll());
                }
                break;
            }

        }
        values.put(Logs.COLUMN_DATE, diamondLog.getDate());
        values.put(Logs.COLUMN_ONLINETIME, diamondLog.getOnlineTime());
        values.put(Logs.COLUMN_DIAMONDSALL, diamondLog.getDiamondsAll());
        values.put(Logs.COLUMN_DIAMONDSINCOME, diamondLog.getDiamondsIncome());

        db.update(Logs.TABLE_NAME, values, Logs.COLUMN_ID + " = ?",
                new String[]{String.valueOf(diamondLog.getId())});
        db.close();
    }

    /*
    select a log
     */
    public Logs getLog(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Logs.TABLE_NAME,
                new String[]{Logs.COLUMN_ID, Logs.COLUMN_DATE, Logs.COLUMN_ONLINETIME,
                            Logs.COLUMN_DIAMONDSALL, Logs.COLUMN_DIAMONDSINCOME },
                   Logs.COLUMN_ID + " =?",
                            new String[]{String.valueOf(id)},
        null, null, null, null);

        if(cursor.getCount() > 0){
            cursor.moveToFirst();
        }
        else
            return new Logs(-1,null,0,0,0);
        Logs log = new Logs(
                cursor.getInt(cursor.getColumnIndex(Logs.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Logs.COLUMN_DATE)),
                cursor.getInt(cursor.getColumnIndex(Logs.COLUMN_ONLINETIME)),
                cursor.getInt(cursor.getColumnIndex(Logs.COLUMN_DIAMONDSALL)),
                cursor.getInt(cursor.getColumnIndex(Logs.COLUMN_DIAMONDSINCOME)));
        cursor.close();
        db.close();
        return log;
    }

    public void getTodayLog(String date, int number){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Logs.TABLE_NAME,
                new String[]{Logs.COLUMN_ID, Logs.COLUMN_DATE, Logs.COLUMN_ONLINETIME,
                        Logs.COLUMN_DIAMONDSALL, Logs.COLUMN_DIAMONDSINCOME },
                Logs.COLUMN_DATE + " =?",
                new String[]{date},
                null, null, null, null);

        Logs log = new Logs();
        if(cursor.moveToFirst()){
            Logs obj = new Logs(
                    cursor.getInt(cursor.getColumnIndex(Logs.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(Logs.COLUMN_DATE)),
                    cursor.getInt(cursor.getColumnIndex(Logs.COLUMN_ONLINETIME)),
                    cursor.getInt(cursor.getColumnIndex(Logs.COLUMN_DIAMONDSALL)),
                    cursor.getInt(cursor.getColumnIndex(Logs.COLUMN_DIAMONDSINCOME)));
            log = obj;
            cursor.close();
            db.close();
            log.setDiamondsIncome(obj.getDiamondsIncome()+number-obj.getDiamondsAll());
            log.setDiamondsAll(number);
            log.setDate(date);

            updateLog(log);
        }
        else{
            cursor.close();
            db.close();
            log.setDiamondsAll(number);
            log.setDate(date);
            insertLog(log);
        }


    }

    /*
    get all logs
     */
    public List<Logs> getAllLogs(String sort){
        String sql = "SELECT * FROM " + Logs.TABLE_NAME + " ORDER BY " + Logs.COLUMN_DATE + " " + sort;
        List<Logs> logs = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        if(cursor.moveToFirst()){
            do{
                Logs log = new Logs();
                log.setId(cursor.getInt(cursor.getColumnIndex(Logs.COLUMN_ID)));
                log.setDate(cursor.getString(cursor.getColumnIndex(Logs.COLUMN_DATE)));
                log.setOnlineTime(cursor.getInt(cursor.getColumnIndex(Logs.COLUMN_ONLINETIME)));
                log.setDiamondsAll(cursor.getInt(cursor.getColumnIndex(Logs.COLUMN_DIAMONDSALL)));
                log.setDiamondsIncome(cursor.getInt(cursor.getColumnIndex(Logs.COLUMN_DIAMONDSINCOME)));
                logs.add(log);
            }while (cursor.moveToNext());
        }
        db.close();
        return logs;
    }

    /*
    get 7 logs
     */
    public List<Logs> get7Logs(String sort){
        String sql = "SELECT * FROM " + Logs.TABLE_NAME + " ORDER BY " + Logs.COLUMN_DATE + " " + sort;
        List<Logs> logs = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        int op = 0;
        if(cursor.moveToLast()){
            do{
                Logs log = new Logs();
                log.setId(cursor.getInt(cursor.getColumnIndex(Logs.COLUMN_ID)));
                log.setDate(cursor.getString(cursor.getColumnIndex(Logs.COLUMN_DATE)));
                log.setOnlineTime(cursor.getInt(cursor.getColumnIndex(Logs.COLUMN_ONLINETIME)));
                log.setDiamondsAll(cursor.getInt(cursor.getColumnIndex(Logs.COLUMN_DIAMONDSALL)));
                log.setDiamondsIncome(cursor.getInt(cursor.getColumnIndex(Logs.COLUMN_DIAMONDSINCOME)));
                logs.add(log);
                op++;
            }while (cursor.moveToPrevious() && op < 7);
        }
        db.close();
        List<Logs> logs1 = new ArrayList<>();
        for(int i = logs.size() - 1; i >= 0; i--){
            logs1.add(logs.get(i));
        }

        return logs1;
    }

    /*
    get number of logs
     */
    public int getLogCount(){
        String countQuery = "SELECT  * FROM " + Logs.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();
        db.close();

        return count;
    }

    /*
    社团
     */

    //add
    public long insertCommunity(int c_id, String name, String date, int m_id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CommunityManage.COLUMN_COMMUNITY_ID, c_id);
        values.put(CommunityManage.COLUMN_COMMUNITY_NAME, name);
        values.put(CommunityManage.COLUMN_DATE, date);
        values.put(CommunityManage.COLUMN_MANAGER_ID, m_id);

        long id = db.insert(CommunityManage.TABLE_NAME, null, values);
        db.close();
        return id;
    }

    //update
    public void updateCommunity(CommunityManage obj){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CommunityManage.COLUMN_COMMUNITY_ID, obj.getCommunity_id());
        values.put(CommunityManage.COLUMN_COMMUNITY_NAME, obj.getCommunity_name());
        values.put(CommunityManage.COLUMN_DATE, obj.getDate());
        values.put(CommunityManage.COLUMN_MANAGER_ID, obj.getManager_id());

        db.update(CommunityManage.TABLE_NAME, values, CommunityManage.COLUMN_ID + " = ?",
                new String[]{String.valueOf(obj.getId())});
        db.close();
    }

    //get one
    public CommunityManage getCommunity(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(CommunityManage.TABLE_NAME,
                new String[]{CommunityManage.COLUMN_ID, CommunityManage.COLUMN_COMMUNITY_ID,
                        CommunityManage.COLUMN_COMMUNITY_NAME,
                        CommunityManage.COLUMN_DATE, CommunityManage.COLUMN_MANAGER_ID },
                CommunityManage.COLUMN_ID + " =?",
                new String[]{String.valueOf(id)},
                null, null, null, null);

        if(cursor.getCount() > 0){
            cursor.moveToFirst();
        }
        else
            return new CommunityManage(-1,0,null,null,'\0');
        CommunityManage log = new CommunityManage(
                cursor.getInt(cursor.getColumnIndex(CommunityManage.COLUMN_ID)),
                cursor.getInt(cursor.getColumnIndex(CommunityManage.COLUMN_COMMUNITY_ID)),
                cursor.getString(cursor.getColumnIndex(CommunityManage.COLUMN_COMMUNITY_NAME)),
                cursor.getString(cursor.getColumnIndex(CommunityManage.COLUMN_DATE)),
                cursor.getInt(cursor.getColumnIndex(CommunityManage.COLUMN_MANAGER_ID)));
        cursor.close();
        db.close();
        return log;
    }

    //get all
    public List<CommunityManage> getAllCommunity(){
        String sql = "SELECT * FROM " + CommunityManage.TABLE_NAME + " ORDER BY " + CommunityManage.COLUMN_DATE ;
        List<CommunityManage> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        if(cursor.moveToFirst()){
            do{
                CommunityManage log = new CommunityManage();
                log.setId(cursor.getInt(cursor.getColumnIndex(CommunityManage.COLUMN_ID)));
                log.setCommunityid(cursor.getInt(cursor.getColumnIndex(CommunityManage.COLUMN_COMMUNITY_ID)));
                log.setColumnCommunityname(cursor.getString(cursor.getColumnIndex(CommunityManage.COLUMN_COMMUNITY_NAME)));
                log.setDate(cursor.getString(cursor.getColumnIndex(CommunityManage.COLUMN_DATE)));
                log.setManager_id(cursor.getInt(cursor.getColumnIndex(CommunityManage.COLUMN_MANAGER_ID)));

                list.add(log);
            }while (cursor.moveToNext());
        }
        db.close();
        return list;
    }
    //delete
    public void deleteCommunity(CommunityManage community){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CommunityManage.TABLE_NAME, CommunityManage.COLUMN_ID + " = ?",
                new String[]{String.valueOf(community.getId())});
        db.close();
    }

    /*
    小号管理
     */
    //add
    public long insertAccount(String port, String role, String number, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Account.COLUMN_ACCOUNT_PORT, port);
        values.put(Account.COLUMN_ACCOUNT_ROLE, role);
        values.put(Account.COLUMN_ACCOUNT_NUMBER, number);
        values.put(Account.COLUMN_ACCOUNT_PASSWORD, password);

        long id = db.insert(Account.TABLE_NAME, null, values);
        db.close();
        return id;
    }

    //update
    public void updateAccount(Account obj){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Account.COLUMN_ACCOUNT_PORT, obj.getAccount_port());
        values.put(Account.COLUMN_ACCOUNT_ROLE, obj.getAccount_role());
        values.put(Account.COLUMN_ACCOUNT_NUMBER, obj.getAccount_number());
        values.put(Account.COLUMN_ACCOUNT_PASSWORD, obj.getAccount_password());

        db.update(Account.TABLE_NAME, values, Account.COLUMN_ID + " = ?",
                new String[]{String.valueOf(obj.getId())});
        db.close();
    }

    //get all
    public List<Account> getAllAccounts(){
        String sql = "SELECT * FROM " + Account.TABLE_NAME ;
        List<Account> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        if(cursor.moveToFirst()){
            do{
                Account log = new Account();
                log.setId(cursor.getInt(cursor.getColumnIndex(Account.COLUMN_ID)));
                log.setAccount_port(cursor.getString(cursor.getColumnIndex(Account.COLUMN_ACCOUNT_PORT)));
                log.setAccount_role(cursor.getString(cursor.getColumnIndex(Account.COLUMN_ACCOUNT_ROLE)));
                log.setAccount_number(cursor.getString(cursor.getColumnIndex(Account.COLUMN_ACCOUNT_NUMBER)));
                log.setAccount_password(cursor.getString(cursor.getColumnIndex(Account.COLUMN_ACCOUNT_PASSWORD)));

                list.add(log);
            }while (cursor.moveToNext());
        }
        db.close();
        return list;
    }

    //get account
    public Account getAccount(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Account.TABLE_NAME,
                new String[]{Account.COLUMN_ID, Account.COLUMN_ACCOUNT_PORT,
                        Account.COLUMN_ACCOUNT_ROLE,
                        Account.COLUMN_ACCOUNT_NUMBER, Account.COLUMN_ACCOUNT_PASSWORD },
                Account.COLUMN_ID + " =?",
                new String[]{String.valueOf(id)},
                null, null, null, null);

        if(cursor.getCount() > 0){
            cursor.moveToFirst();
        }
        else
            return new Account();
        Account log = new Account(
                cursor.getInt(cursor.getColumnIndex(Account.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Account.COLUMN_ACCOUNT_PORT)),
                cursor.getString(cursor.getColumnIndex(Account.COLUMN_ACCOUNT_ROLE)),
                cursor.getString(cursor.getColumnIndex(Account.COLUMN_ACCOUNT_NUMBER)),
                cursor.getString(cursor.getColumnIndex(Account.COLUMN_ACCOUNT_PASSWORD)));
        cursor.close();
        db.close();
        return log;
    }

    //delete
    public void deleteAccount(Account account){

        CommunityManage community = new CommunityManage();
        community = getCommunity(account.getId());
        community.setManager_id(-1);
        updateCommunity(community);

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Account.TABLE_NAME, Account.COLUMN_ID + " = ?",
                new String[]{String.valueOf(account.getId())});
        db.close();
    }

    /*
    role
     */
    //add []
    public void insertRoles(List<Role> roleList){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        for(int i = 0; i < roleList.size(); i++){
            values.put(Role.COLUMN_ID, roleList.get(i).getId());
            values.put(Role.COLUMN_ROLE_IMG, roleList.get(i).getRole_img());
            values.put(Role.COLUMN_ROLE_NAME, roleList.get(i).getRole_name());
            values.put(Role.COLUMN_ROLE_KIND, roleList.get(i).getRole_kind());
            values.put(Role.COLUMN_ROLE_LEVEL, roleList.get(i).getRole_level());
            values.put(Role.COLUMN_ROLE_STONE_1, roleList.get(i).getRole_stone_1());
            values.put(Role.COLUMN_ROLE_STONE_2, roleList.get(i).getRole_stone_2());
            values.put(Role.COLUMN_ROLE_RUNE_1, roleList.get(i).getRole_rune_1());
            values.put(Role.COLUMN_ROLE_RUNE_2, roleList.get(i).getRole_rune_2());
            values.put(Role.COLUMN_ROLE_RUNE_3, roleList.get(i).getRole_rune_3());
            values.put(Role.COLUMN_ROLE_RUNE_4, roleList.get(i).getRole_rune_4());
            values.put(Role.COLUMN_ROLE_RUNE_SUIT_INTRODUCE, roleList.get(i).getRole_rune_suit());
            values.put(Role.COLUMN_ROLE_INTRODUCE, roleList.get(i).getRole_introduce());
            values.put(Role.COLUMN_ROLE_STATE, roleList.get(i).getRole_state());

            db.insert(Role.TABLE_NAME, null, values);
        }

        db.close();
    }
    public long insertRole(Role role){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
            //values.put(Role.COLUMN_ID, role.getId());
            values.put(Role.COLUMN_ROLE_IMG, role.getRole_img());
            values.put(Role.COLUMN_ROLE_NAME, role.getRole_name());
            values.put(Role.COLUMN_ROLE_KIND, role.getRole_kind());
            values.put(Role.COLUMN_ROLE_LEVEL, role.getRole_level());
            values.put(Role.COLUMN_ROLE_STONE_1, role.getRole_stone_1());
            values.put(Role.COLUMN_ROLE_STONE_2, role.getRole_stone_2());
            values.put(Role.COLUMN_ROLE_RUNE_1, role.getRole_rune_1());
            values.put(Role.COLUMN_ROLE_RUNE_2, role.getRole_rune_2());
            values.put(Role.COLUMN_ROLE_RUNE_3, role.getRole_rune_3());
            values.put(Role.COLUMN_ROLE_RUNE_4, role.getRole_rune_4());
            values.put(Role.COLUMN_ROLE_RUNE_SUIT_INTRODUCE, role.getRole_rune_suit());
            values.put(Role.COLUMN_ROLE_INTRODUCE, role.getRole_introduce());
            values.put(Role.COLUMN_ROLE_STATE, role.getRole_state());
        long id = db.insert(Role.TABLE_NAME, null, values);
        db.close();
        return id;
    }
    //update
    public void updataRole(Role role){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Role.COLUMN_ROLE_IMG, role.getRole_img());
        values.put(Role.COLUMN_ROLE_NAME, role.getRole_name());
        values.put(Role.COLUMN_ROLE_KIND, role.getRole_kind());
        values.put(Role.COLUMN_ROLE_LEVEL, role.getRole_level());
        values.put(Role.COLUMN_ROLE_STONE_1, role.getRole_stone_1());
        values.put(Role.COLUMN_ROLE_STONE_2, role.getRole_stone_2());
        values.put(Role.COLUMN_ROLE_RUNE_1, role.getRole_rune_1());
        values.put(Role.COLUMN_ROLE_RUNE_2, role.getRole_rune_2());
        values.put(Role.COLUMN_ROLE_RUNE_3, role.getRole_rune_3());
        values.put(Role.COLUMN_ROLE_RUNE_4, role.getRole_rune_4());
        values.put(Role.COLUMN_ROLE_RUNE_SUIT_INTRODUCE, role.getRole_rune_suit());
        values.put(Role.COLUMN_ROLE_INTRODUCE, role.getRole_introduce());
        values.put(Role.COLUMN_ROLE_STATE, role.getRole_state());

        db.update(Role.TABLE_NAME, values, Account.COLUMN_ID + " = ?",
                new String[]{String.valueOf(role.getId())});
        db.close();
    }

    //delete
    public void deleteRole(Role role){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Role.TABLE_NAME, Role.COLUMN_ID + " = ?",
                new String[]{String.valueOf(role.getId())});
        db.close();
    }

    public void deleteAllRoles(){
        String sql = " DELETE FROM " + Role.TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(sql);
        db.close();
    }

    //get role
    //get all
    public List<Role> getAllRoles(){
        String sql = "SELECT * FROM " + Role.TABLE_NAME ;
        List<Role> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        if(cursor.moveToFirst()){
            do{
                Role log = new Role();
                log.setId(cursor.getInt(cursor.getColumnIndex(Role.COLUMN_ID)));
                log.setRole_img(cursor.getBlob(cursor.getColumnIndex(Role.COLUMN_ROLE_IMG)));
                log.setRole_name(cursor.getString(cursor.getColumnIndex(Role.COLUMN_ROLE_NAME)));
                log.setRole_kind(cursor.getString(cursor.getColumnIndex(Role.COLUMN_ROLE_KIND)));
                log.setRole_level(cursor.getString(cursor.getColumnIndex(Role.COLUMN_ROLE_LEVEL)));
                log.setRole_stone_1(cursor.getString(cursor.getColumnIndex(Role.COLUMN_ROLE_STONE_1)));
                log.setRole_stone_2(cursor.getString(cursor.getColumnIndex(Role.COLUMN_ROLE_STONE_2)));
                log.setRole_rune_1(cursor.getString(cursor.getColumnIndex(Role.COLUMN_ROLE_RUNE_1)));
                log.setRole_rune_2(cursor.getString(cursor.getColumnIndex(Role.COLUMN_ROLE_RUNE_2)));
                log.setRole_rune_3(cursor.getString(cursor.getColumnIndex(Role.COLUMN_ROLE_RUNE_3)));
                log.setRole_rune_4(cursor.getString(cursor.getColumnIndex(Role.COLUMN_ROLE_RUNE_4)));
                log.setRole_rune_suit(cursor.getString(cursor.getColumnIndex(Role.COLUMN_ROLE_RUNE_SUIT_INTRODUCE)));
                log.setRole_introduce(cursor.getString(cursor.getColumnIndex(Role.COLUMN_ROLE_INTRODUCE)));
                log.setRole_state(cursor.getString(cursor.getColumnIndex(Role.COLUMN_ROLE_STATE)));

                list.add(log);
            }while (cursor.moveToNext());
        }
        db.close();
        return list;
    }

    public int getRoleCount(){
        String countQuery = "SELECT  * FROM " + Role.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();
        db.close();

        return count;
    }

    public List<Role> selectRoles(String name){
        String sql = "SELECT * FROM " + Role.TABLE_NAME
                    + " WHERE " + Role.COLUMN_ROLE_NAME + " LIKE '%" + name + "%'";
        List<Role> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        if(cursor.moveToFirst()){
            do{
                Role log = new Role();
                log.setId(cursor.getInt(cursor.getColumnIndex(Role.COLUMN_ID)));
                log.setRole_img(cursor.getBlob(cursor.getColumnIndex(Role.COLUMN_ROLE_IMG)));
                log.setRole_name(cursor.getString(cursor.getColumnIndex(Role.COLUMN_ROLE_NAME)));
                log.setRole_kind(cursor.getString(cursor.getColumnIndex(Role.COLUMN_ROLE_KIND)));
                log.setRole_level(cursor.getString(cursor.getColumnIndex(Role.COLUMN_ROLE_LEVEL)));
                log.setRole_stone_1(cursor.getString(cursor.getColumnIndex(Role.COLUMN_ROLE_STONE_1)));
                log.setRole_stone_2(cursor.getString(cursor.getColumnIndex(Role.COLUMN_ROLE_STONE_2)));
                log.setRole_rune_1(cursor.getString(cursor.getColumnIndex(Role.COLUMN_ROLE_RUNE_1)));
                log.setRole_rune_2(cursor.getString(cursor.getColumnIndex(Role.COLUMN_ROLE_RUNE_2)));
                log.setRole_rune_3(cursor.getString(cursor.getColumnIndex(Role.COLUMN_ROLE_RUNE_3)));
                log.setRole_rune_4(cursor.getString(cursor.getColumnIndex(Role.COLUMN_ROLE_RUNE_4)));
                log.setRole_rune_suit(cursor.getString(cursor.getColumnIndex(Role.COLUMN_ROLE_RUNE_SUIT_INTRODUCE)));
                log.setRole_introduce(cursor.getString(cursor.getColumnIndex(Role.COLUMN_ROLE_INTRODUCE)));
                log.setRole_state(cursor.getString(cursor.getColumnIndex(Role.COLUMN_ROLE_STATE)));

                list.add(log);
            }while (cursor.moveToNext());
        }
        db.close();
        return list;
    }

    /*
    psp -> & record add update delete select
    download from website or json
     */

    //get


    //insert
    public long insertPsp(Psp psp){
        SQLiteDatabase db = this.getWritableDatabase();
//        String sql = "SELECE * FROM " + Psp.TABLE_NAME
//                + " WHERE " + Psp.COLUMN_PSP_WEB_ID + " = "
//                + psp.getPsp_webid();
        if(psp.getPsp_webid() > 0){
            Cursor cursor = db.query(Psp.TABLE_NAME,
                    new String[]{Psp.COLUMN_PSP_ID, Psp.COLUMN_PSP_TITLE,
                            Psp.COLUMN_PSP_KIND, Psp.COLUMN_PSP_TIME,
                            Psp.COLUMN_PSP_AUTHOR, Psp.COLUMN_PSP_TEXT,
                            Psp.COLUMN_PSP_WEB_ID},
                    Psp.COLUMN_PSP_WEB_ID + " =?",
                    new String[]{String.valueOf(psp.getPsp_webid())},
                    null, null, null, null);
            if(cursor.getCount() > 0){
//            return cursor.getInt(cursor.getColumnIndex(Psp.COLUMN_PSP_WEB_ID));
                db.close();
                cursor.moveToFirst();
                psp.setId(cursor.getInt(cursor.getColumnIndex(Psp.COLUMN_PSP_ID)));
                updatePsp(psp);
                return 0;
            }
        }

        ContentValues values = new ContentValues();
        values.put(Psp.COLUMN_PSP_TITLE, psp.getPsp_title());
        values.put(Psp.COLUMN_PSP_KIND, psp.getPsp_kind());
        values.put(Psp.COLUMN_PSP_TIME, psp.getPsp_time());
        values.put(Psp.COLUMN_PSP_AUTHOR, psp.getPsp_author());
        values.put(Psp.COLUMN_PSP_TEXT, psp.getPsp_text());
        values.put(Psp.COLUMN_PSP_WEB_ID, psp.getPsp_webid());
        values.put(Psp.COLUMN_PSP_WEB_EDITION, psp.getPsp_web_edition());

        long id = db.insert(Psp.TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public int[] insertPsps(List<Psp> list){
        int[] result = new int[list.size()];
        int number = 0;
        for(int i = 0; i < list.size(); i++){
            if(insertPsp(list.get(i)) == 0){
                result[++number] = i;
            }
        }
        result[0] = number;
        return result;
    }

    public long updatePsp(Psp psp){
        SQLiteDatabase db = this.getWritableDatabase();
//        String sql = "SELECE * FROM " + Psp.TABLE_NAME
//                + " WHERE " + Psp.COLUMN_PSP_WEB_ID + " = "
//                + psp.getPsp_webid();
        if(psp.getPsp_webid() > 0) {
            Cursor cursor = db.query(Psp.TABLE_NAME,
                    new String[]{Psp.COLUMN_PSP_ID, Psp.COLUMN_PSP_TITLE,
                            Psp.COLUMN_PSP_KIND, Psp.COLUMN_PSP_TIME,
                            Psp.COLUMN_PSP_AUTHOR, Psp.COLUMN_PSP_TEXT,
                            Psp.COLUMN_PSP_WEB_ID, Psp.COLUMN_PSP_WEB_EDITION},
                    Psp.COLUMN_PSP_WEB_ID + " =?",
                    new String[]{String.valueOf(psp.getPsp_webid())},
                    null, null, null, null);
            if (cursor.getCount() > 0) {

                cursor.moveToFirst();
                Log.d("数据库", cursor.getString(cursor.getColumnIndex(Psp.COLUMN_PSP_WEB_EDITION)) + "");
                if (cursor.getInt(cursor.getColumnIndex(Psp.COLUMN_PSP_WEB_EDITION)) >= psp.getPsp_web_edition()) {
                    Log.d("hiho",cursor.getInt(cursor.getColumnIndex(Psp.COLUMN_PSP_WEB_EDITION)) + " "+ psp.getPsp_web_edition());
                    db.close();
                    return 0;
                }
            }
        }
        ContentValues values = new ContentValues();
        values.put(Psp.COLUMN_PSP_TITLE, psp.getPsp_title());
        values.put(Psp.COLUMN_PSP_KIND, psp.getPsp_kind());
        values.put(Psp.COLUMN_PSP_TIME, psp.getPsp_time());
        values.put(Psp.COLUMN_PSP_AUTHOR, psp.getPsp_author());
        values.put(Psp.COLUMN_PSP_TEXT, psp.getPsp_text());
        values.put(Psp.COLUMN_PSP_WEB_ID, psp.getPsp_webid());
        values.put(Psp.COLUMN_PSP_WEB_EDITION, psp.getPsp_web_edition());

        db.update(Psp.TABLE_NAME, values, Psp.COLUMN_PSP_ID + " = ?",
                new String[]{String.valueOf(psp.getId())});
        db.close();
        return psp.getId();
    }

    //get
    public Psp getPsp(int id) {
        String sql = "SELECT * FROM " + Psp.TABLE_NAME + " WHERE " + Psp.COLUMN_PSP_ID + " = " + id;
        Psp log = new Psp();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            log.setId(cursor.getInt(cursor.getColumnIndex(Psp.COLUMN_PSP_ID)));
            log.setPsp_title(cursor.getString(cursor.getColumnIndex(Psp.COLUMN_PSP_TITLE)));
            log.setPsp_kind(cursor.getString(cursor.getColumnIndex(Psp.COLUMN_PSP_KIND)));
            log.setPsp_time(cursor.getString(cursor.getColumnIndex(Psp.COLUMN_PSP_TIME)));
            log.setPsp_author(cursor.getString(cursor.getColumnIndex(Psp.COLUMN_PSP_AUTHOR)));
            log.setPsp_text(cursor.getString(cursor.getColumnIndex(Psp.COLUMN_PSP_TEXT)));
            log.setPsp_webid(cursor.getInt(cursor.getColumnIndex(Psp.COLUMN_PSP_WEB_ID)));
            log.setPsp_web_edition(cursor.getInt(cursor.getColumnIndex(Psp.COLUMN_PSP_WEB_EDITION)));
        }
        db.close();
        return log;
    }
    //get all
    public List<Psp> getAllPsps(){
        String sql = "SELECT * FROM " + Psp.TABLE_NAME ;
        List<Psp> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        if(cursor.moveToFirst()){
            do{
                Psp log = new Psp();
                log.setId(cursor.getInt(cursor.getColumnIndex(Psp.COLUMN_PSP_ID)));
                log.setPsp_title(cursor.getString(cursor.getColumnIndex(Psp.COLUMN_PSP_TITLE)));
                log.setPsp_kind(cursor.getString(cursor.getColumnIndex(Psp.COLUMN_PSP_KIND)));
                log.setPsp_time(cursor.getString(cursor.getColumnIndex(Psp.COLUMN_PSP_TIME)));
                log.setPsp_author(cursor.getString(cursor.getColumnIndex(Psp.COLUMN_PSP_AUTHOR)));
                log.setPsp_text(cursor.getString(cursor.getColumnIndex(Psp.COLUMN_PSP_TEXT)));
                log.setPsp_webid(cursor.getInt(cursor.getColumnIndex(Psp.COLUMN_PSP_WEB_ID)));
                log.setPsp_web_edition(cursor.getInt(cursor.getColumnIndex(Psp.COLUMN_PSP_WEB_EDITION)));
                list.add(log);
            }while (cursor.moveToNext());
        }
        db.close();
        return list;
    }
    public List<Psp> selectPsps(String str){
        String sql = "SELECT * FROM " + Psp.TABLE_NAME
                + " WHERE " + Psp.COLUMN_PSP_TITLE +" LIKE "
                + "'%" + str + "%'"
                + " OR " + Psp.COLUMN_PSP_TEXT +" LIKE "
                + "'%" + str + "%'"
                + " OR " + Psp.COLUMN_PSP_AUTHOR +" LIKE "
                + "'%" + str + "%'"
                + " ORDER BY "+ Psp.COLUMN_PSP_TIME;
        List<Psp> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        if(cursor.moveToFirst()){
            do{
                Psp log = new Psp();
                log.setId(cursor.getInt(cursor.getColumnIndex(Psp.COLUMN_PSP_ID)));
                log.setPsp_title(cursor.getString(cursor.getColumnIndex(Psp.COLUMN_PSP_TITLE)));
                log.setPsp_kind(cursor.getString(cursor.getColumnIndex(Psp.COLUMN_PSP_KIND)));
                log.setPsp_time(cursor.getString(cursor.getColumnIndex(Psp.COLUMN_PSP_TIME)));
                log.setPsp_author(cursor.getString(cursor.getColumnIndex(Psp.COLUMN_PSP_AUTHOR)));
                log.setPsp_text(cursor.getString(cursor.getColumnIndex(Psp.COLUMN_PSP_TEXT)));
                log.setPsp_webid(cursor.getInt(cursor.getColumnIndex(Psp.COLUMN_PSP_WEB_ID)));
                log.setPsp_web_edition(cursor.getInt(cursor.getColumnIndex(Psp.COLUMN_PSP_WEB_EDITION)));

                list.add(log);
            }while (cursor.moveToNext());
        }
        db.close();
        return list;
    }

    //delete
    public void deletePsp(Psp psp){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Psp.TABLE_NAME, Psp.COLUMN_PSP_ID + " = ?",
                new String[]{String.valueOf(psp.getId())});
        db.close();
    }
    public void deletePsp(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Psp.TABLE_NAME, Psp.COLUMN_PSP_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }


    /*
    Game just reload/add select if isExist
    json
     */

    /*
    cdk reload just like game play
    json
     */

    /*
    upload insert delete update
     */
    public long insertUpload(Upload obj){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Upload.COLUMN_TITLE, obj.getTitle());
        values.put(Upload.COLUMN_PHONE, obj.getPhone());
        values.put(Upload.COLUMN_TEXT, obj.getText());
        values.put(Upload.COLUMN_DATE, obj.getDate());
        values.put(Upload.COLUMN_KIND, obj.getKind());

        long id = db.insert(Upload.TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public void updateUpload(Upload obj){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Upload.COLUMN_TITLE, obj.getTitle());
        values.put(Upload.COLUMN_PHONE, obj.getPhone());
        values.put(Upload.COLUMN_TEXT, obj.getText());
        values.put(Upload.COLUMN_DATE, obj.getDate());
        values.put(Upload.COLUMN_KIND, obj.getKind());
        db.update(Upload.TABLE_NAME, values, Upload.COLUMN_ID + " = ?",
                new String[]{String.valueOf(obj.getId())});
        db.close();
    }

    public void deleteUpload(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Upload.TABLE_NAME, Upload.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    public List<Upload> getAllUploads(){
        String sql = "SELECT * FROM " + Upload.TABLE_NAME ;
        List<Upload> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        if(cursor.moveToFirst()){
            do{
                Upload log = new Upload();
                log.setId(cursor.getInt(cursor.getColumnIndex(Upload.COLUMN_ID)));
                log.setTitle(cursor.getString(cursor.getColumnIndex(Upload.COLUMN_TITLE)));
                log.setDate(cursor.getString(cursor.getColumnIndex(Upload.COLUMN_DATE)));
                log.setText(cursor.getString(cursor.getColumnIndex(Upload.COLUMN_TEXT)));
                log.setPhone(cursor.getString(cursor.getColumnIndex(Upload.COLUMN_PHONE)));
                log.setKind(cursor.getString(cursor.getColumnIndex(Upload.COLUMN_KIND)));
                list.add(log);
            }while (cursor.moveToNext());
        }
        db.close();
        return list;
    }


    public static void initRoleTable(final Context context, final String sql) {
        //final String REMOTE_IP = "ghfuuto7.2392lan.dnstoo.com:3306";
        final String URL = "jdbc:mysql://ghfuuto7.2392.dnstoo.com:5504/wakof8" +
                "?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=UTC&rewriteBatchedStatements=true&autoReconnect=true";

        final String USER = "wakof8_f";
        final String PASSWORD = "n549tjkt";

        new Thread(new Runnable() {
            public void run() {
                System.out.println("bbbbbbb");
                Connection conn;
                conn = Gadget.openConnection(URL, USER, PASSWORD);
                System.out.println("All users info:");
                if(sql.equals("select * from role"))
                    Gadget.query(conn, sql, context);
                else
                    Gadget.queryUpload(conn, sql, context);
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        conn = null;
                    } finally {
                        conn = null;
                    }
                }
            }
        }).start();

    }

}
