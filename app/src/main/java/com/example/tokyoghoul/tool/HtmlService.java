package com.example.tokyoghoul.tool;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import com.example.tokyoghoul.database.model.Logs;
import com.mysql.jdbc.log.LogUtils;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Enumeration;

public class HtmlService {

    public static String getHtml(String path) throws Exception {
        //TODO url server address
        String str = "http://undera.cn/Tokyo_php/" + path;
        Log.i("phpurl",str);
        URL url = new URL(str);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5 * 1000);
        InputStream inStream = conn.getInputStream();//通过输入流获取html数据
        byte[] data = readInputStream(inStream);//得到html的二进制数据
        String html = new String(data, "UTF-8");
        Log.i("html",html);
        return html;
    }

    public static byte[] readInputStream(InputStream inStream) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while( (len=inStream.read(buffer)) != -1 ){
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }

    public static String getJsondataFromWeb(String url, String s){
        int op = 0;
        if(!s.equals("")){
            return s;
        }
        String jsd = "";
        while (jsd.equals("")) {
            if(++op > 10){
                Log.d("请求失败","try 10 times");
                break;
            }
            try {
                jsd = HtmlService.getHtml(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return jsd;
    }

    public static String uploadFeedback(String path, JSONObject jsonObject) throws Exception{
        String str = "http://undera.cn/Tokyo_php/" + path;
        Log.i("phpurl",str);
        HttpURLConnection con = null;
        URL url = null;
        String json = null;

        try {
            url = new URL(str);

            String content=String.valueOf(jsonObject);  //json串转string类型

            HttpURLConnection conn=(HttpURLConnection) url.openConnection(); //开启连接
            conn.setConnectTimeout(5000);

            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("ser-Agent", "Fiddler");
            conn.setRequestProperty("Content-Type","application/json");
            //写输出流，将要转的参数写入流里
            OutputStream os=conn.getOutputStream();
            os.write(content.getBytes()); //字符串写进二进流
            os.close();

            int code=conn.getResponseCode();
            Log.d("jfgd",code+"");
            if(code==200){   //与后台交互成功返回 200
                //读取返回的json数据
                InputStream inputStream=conn.getInputStream();
                // 调用自己写的NetUtils() 将流转成string类型
                json = NetUtils.readString(inputStream);

            }else{
                Log.i("sd","失败");
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return json;
    }

    public static void addFeedback(final String url, final JSONObject obj){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int op = 0;
                String jsd = "";
                while (jsd.equals("")) {
                    if(++op > 10){
                        Log.d("请求失败","try 10 times");
                        break;
                    }
                    try {
                        Log.i("sdf",op+"ci");
                        jsd = HtmlService.uploadFeedback(url, obj);
                        Log.i("result",jsd+"");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();

    }

    public static String getIP(){

        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)
                {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address))
                    {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        }
        catch (SocketException ex){
            ex.printStackTrace();
        }
        return null;
    }

}

class NetUtils {

    public static byte[] readBytes(InputStream is){
        try {
            byte[] buffer = new byte[1024];
            int len = -1 ;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while((len = is.read(buffer)) != -1){
                baos.write(buffer, 0, len);
            }
            baos.close();
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null ;
    }
    public static String readString(InputStream is){

        return new String(readBytes(is));
    }

}
