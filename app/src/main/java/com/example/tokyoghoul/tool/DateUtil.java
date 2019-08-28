package com.example.tokyoghoul.tool;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {

    public static String formatDate(String str) {
        SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sf2 = new SimpleDateFormat("MM-dd");
        String formatStr = "";
        try {
            formatStr = sf2.format(sf1.parse(str));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatStr;
    }

    public static String getTime(){
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"));
        int year = c.get(Calendar.YEAR);   //获取年份
        int month = c.get(Calendar.MONTH) + 1;  //获取月份
        int day = c.get(Calendar.DAY_OF_MONTH);  //获取日期
        int hour = c.get(Calendar.HOUR_OF_DAY);  //获取小时
        int minute = c.get(Calendar.MINUTE); //获取分钟
        int second = c.get(Calendar.SECOND);//秒
        String time = String.format("%02d",hour)+":"
                            + String.format("%02d",minute)+":"
                            + String.format("%02d",second);
        return time;
    }

    public static String getDate(){
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"));
        int year = c.get(Calendar.YEAR);   //获取年份
        int month = c.get(Calendar.MONTH) + 1;  //获取月份
        int day = c.get(Calendar.DAY_OF_MONTH);  //获取日期
        int hour = c.get(Calendar.HOUR_OF_DAY);  //获取小时
        int minute = c.get(Calendar.MINUTE); //获取分钟
        int second = c.get(Calendar.SECOND);//秒
        String date = String.format("%04d",year)+"/"
                + String.format("%02d",month)+"/"
                + String.format("%02d",day);
        return date;
    }

    public static String getDateMySql(){
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"));
        int year = c.get(Calendar.YEAR);   //获取年份
        int month = c.get(Calendar.MONTH) + 1;  //获取月份
        int day = c.get(Calendar.DAY_OF_MONTH);  //获取日期
        int hour = c.get(Calendar.HOUR_OF_DAY);  //获取小时
        int minute = c.get(Calendar.MINUTE); //获取分钟
        int second = c.get(Calendar.SECOND);//秒
        String date = String.format("%04d",year)+"-"
                + String.format("%02d",month)+"-"
                + String.format("%02d",day);
        return date;
    }

    public static String getDateTime(){
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"));
        int year = c.get(Calendar.YEAR);   //获取年份
        int month = c.get(Calendar.MONTH) + 1;  //获取月份
        int day = c.get(Calendar.DAY_OF_MONTH);  //获取日期
        int hour = c.get(Calendar.HOUR_OF_DAY);  //获取小时
        int minute = c.get(Calendar.MINUTE); //获取分钟
        int second = c.get(Calendar.SECOND);//秒
        String date = String.format("%04d",year)+"/"
                + String.format("%02d",month)+"/"
                + String.format("%02d",day);

        String time = String.format("%02d",hour)+":"
                + String.format("%02d",minute);
//                +":"
//                + String.format("%02d",second);
        return date + " "+time;
    }

    public static String getDateDate(Date date){
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy/MM/dd HH:mm:ss" );
        String str = sdf.format(date);
        return str;
    }

    public static String getDateToDate(Date date){
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy/MM/dd" );
        String str = sdf.format(date);
        return str;
    }


    public static int CompareDateString(String a, String now){
        Log.d("date",a);
        Log.d("date",now);

        a = a + "/";
        now = now + "/";
        int oldday = 0, newday = 0;
        for(int i = 0; i < a.length() && i < now.length(); i++){
            Log.d("fuck","a:"+oldday+" c:"+a.charAt(i)+"  now:"+newday+" c:"+now.charAt(i));
            if(a.charAt(i) == '/' && now.charAt(i) != '/'){
                Log.d("fuck","a");
                if(now.charAt(i) >= '0' && now.charAt(i) <= '9'){
                    Log.d("fuck","b");
                    return -1;
                }
                else{
                    Log.d("fuck","error");
                    //error
                }
            }
            else if(now.charAt(i) == '/' && a.charAt(i) == '/'){
                oldday = 10*oldday + a.charAt(i) - '0';
                newday = 10*newday + now.charAt(i) - '0';
                int day = oldday - newday;
                if(day > 0)
                    return 1;
                else if(day < 0)
                    return -1;
                else{
                    oldday = 0;
                    newday = 0;
                }
            }
            else if(now.charAt(i) == '/' && a.charAt(i) != '/'){
                if(a.charAt(i) >= '0' && a.charAt(i) <= '9'){
                    return 1;
                }
            }
            else{
                oldday = 10*oldday + a.charAt(i) - '0';
                newday = 10*newday + now.charAt(i) - '0';
            }
        }

        Log.d("fuck","aaaa");
        int day = oldday - newday;
        if(day > 0)
            return 1;
        else if(day < 0)
            return -1;
        else
            return 0;
    }
}