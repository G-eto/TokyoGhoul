package com.example.tokyoghoul.tool;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.example.tokyoghoul.activity.MainActivity;
import com.example.tokyoghoul.database.DatabaseHelper;
import com.example.tokyoghoul.database.model.Role;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Gadget {
    public static void showToast(String str, Context context){
        Toast toast =Toast.makeText(context, str, Toast.LENGTH_SHORT);
        //参数1：当前的上下文环境。可用getApplicationContext()或this
        //参数2：要显示的字符串。
        //参数3：显示的时间长短。Toast默认的有两个LENGTH_LONG(长)和LENGTH_SHORT(短)
        toast.setGravity(Gravity.CENTER, 0, 500);//设置提示框显示的位置
        toast.show();//显示消息
    }

    public static void copyStr(String str, Context context){
        ClipboardManager cm;
        ClipData mClipData;

        //获取剪贴板管理器：
        cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);

        //copy// 创建普通字符型ClipData
        mClipData = ClipData.newPlainText("Label", str);
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
        Gadget.showToast("复制成功", context);
    }

    //mysql
    public static Connection openConnection(String url, String user,
                                            String password) {
        Log.d("mysql1","first");
        java.sql.Connection conn = null;
        try {
            Log.d("mysql1","1.2");
            final String DRIVER_NAME = "com.mysql.jdbc.Driver";
            Log.d("mysql1","1.21");
            Class.forName(DRIVER_NAME);

            Log.d("mysql1","1.22");
            conn = DriverManager.getConnection(url, user, password);
            Log.d("mysql1","22");
        } catch (ClassNotFoundException e) {
            Log.d("mysql1","1.3");
            conn = null;
        } catch (SQLException e) {
            Log.d("mysql1","1.4");
            conn = null;
        }
        if(conn == null)
            Log.d("mysql1","33failed");
        return conn;
    }

    public static void query(Connection conn, String sql, Context context) {

        if (conn == null) {
            return;
        }
        Log.d("mysql1","44");

        Statement statement = null;
        ResultSet result = null;

        List<Role> roleList = new ArrayList<>();
        try {
            statement = conn.createStatement();
            result = statement.executeQuery(sql);
            if (result != null && result.first()) {
                int idColumnIndex = result.findColumn("id");
                int imgColumnIndex = result.findColumn("img");
                int kindColumnIndex = result.findColumn("kind");
                int nameColumnIndex = result.findColumn("name");
                int levelColumnIndex = result.findColumn("level");
                int stone_1ColumnIndex = result.findColumn("stone_1");
                int stone_2ColumnIndex = result.findColumn("stone_2");
                int rune_1_ColumnIndex = result.findColumn("rune_1");
                int rune_2_ColumnIndex = result.findColumn("rune_2");
                int rune_3_ColumnIndex = result.findColumn("rune_3");
                int rune_4_ColumnIndex = result.findColumn("rune_4");
                int rune_suitColumnIndex = result.findColumn("rune_suit");
                int introduceColumnIndex = result.findColumn("introduce");
                Log.d("mysql1",55+result.toString());
                System.out.println("id\t\t" + "name");
                while (!result.isAfterLast()) {
                    System.out.print(result.getString(kindColumnIndex) + "\t\t");
                    System.out.println(result.getString(nameColumnIndex));

                    roleList.add(new Role(result.getInt(idColumnIndex), result.getBytes(imgColumnIndex), result.getString(nameColumnIndex),
                            result.getString(kindColumnIndex), result.getString(levelColumnIndex),
                            result.getString(stone_1ColumnIndex), result.getString(stone_2ColumnIndex),
                            result.getString(rune_1_ColumnIndex), result.getString(rune_2_ColumnIndex),
                            result.getString(rune_3_ColumnIndex), result.getString(rune_4_ColumnIndex),
                            result.getString(rune_suitColumnIndex), result.getString(introduceColumnIndex),
                            "未获得"));

                    result.next();
                }
                Log.d("mysql1","66");
                DatabaseHelper db = new DatabaseHelper(context);
                db.deleteAllRoles();
                db.insertRoles(roleList);
                Log.d("mysql1","777");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (result != null) {
                    result.close();
                    result = null;
                }
                if (statement != null) {
                    statement.close();
                    statement = null;
                }

            } catch (SQLException sqle) {

            }
        }
    }

    public static boolean execSQL(Connection conn, String sql) {
        boolean execResult = false;
        if (conn == null) {
            return execResult;
        }

        Statement statement = null;

        try {
            statement = conn.createStatement();
            if (statement != null) {
                execResult = statement.execute(sql);
            }
        } catch (SQLException e) {
            execResult = false;
        }

        return execResult;
    }
}
