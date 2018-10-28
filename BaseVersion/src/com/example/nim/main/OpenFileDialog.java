package com.example.nim.main;

/**
 * Created by dell on 2016/7/23.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.nim.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class OpenFileDialog {
        public static String tag = "OpenFileDialog";
        static final public String sRoot = "/";
        static final public String sParent = "..";
        static final public String sFolder = ".";
        static final public String sEmpty = "";
        static final private String sOnErrorMsg = "No rights to access!";
        // 参数说明
        // context:上下文
        // dialogid:对话框ID
        // title:对话框标题
        // callback:一个传递Bundle参数的回调接口
        // suffix:需要选择的文件后缀，比如需要选择wav、mp3文件的时候设置为".wav;.mp3;"，注意最后需要一个分号(;)
        // images:用来根据后缀显示的图标资源ID。
        //  根目录图标的索引为sRoot;
        //  父目录的索引为sParent;
        //  文件夹的索引为sFolder;
        //  默认图标的索引为sEmpty;
        //  其他的直接根据后缀进行索引，比如.wav文件图标的索引为"wav"
        public static Dialog createDialog(int id, Context context, String title, CallbackBundle callback, String suffix, Map<String, Integer> images){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setView(new FileSelectView(context, id, callback, suffix, images));
            Dialog dialog = builder.create();
            //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setTitle(title);
            return dialog;
        }

        static class FileSelectView extends ListView implements OnItemClickListener {


            private CallbackBundle callback = null;
            private String path = sRoot;
            private List<Map<String, Object>> list = null;
            private int dialogid = 0;
            private String pt="";

            private String suffix = null;

            private Map<String, Integer> imagemap = null;

            public FileSelectView(Context context, int dialogid, CallbackBundle callback, String suffix, Map<String, Integer> images) {
                super(context);
                this.imagemap = images;
                this.suffix = suffix==null?"":suffix.toLowerCase();
                this.callback = callback;
                this.dialogid = dialogid;
                this.setOnItemClickListener(this);
                refreshFileList();
            }

            private String getSuffix(String filename){
                int dix = filename.lastIndexOf('.');
                if(dix<0){
                    return "";
                }
                else{
                    return filename.substring(dix+1);
                }
            }

            private int getImageId(String s){
                if(imagemap == null){
                    return 0;
                }
                else if(imagemap.containsKey(s)){
                    return imagemap.get(s);
                }
                else if(imagemap.containsKey(sEmpty)){
                    return imagemap.get(sEmpty);
                }
                else {
                    return 0;
                }
            }

            private int refreshFileList()
            {
                // 刷新文件列表
                File[] files = null;
                try{
                    files = new File(path).listFiles();
                }
                catch(Exception e){
                    files = null;
                }
                if(files==null){
                    // 访问出错
                    Toast.makeText(getContext(), sOnErrorMsg, Toast.LENGTH_SHORT).show();
                    return -1;
                }
                if(list != null){
                    list.clear();
                }
                else{
                    list = new ArrayList<Map<String, Object>>(files.length);
                }

                // 用来先保存文件夹和文件夹的两个列表
                ArrayList<Map<String, Object>> lfolders = new ArrayList<Map<String, Object>>();
                ArrayList<Map<String, Object>> lfiles = new ArrayList<Map<String, Object>>();

                if(!this.path.equals(sRoot)){
                    // 添加根目录 和 上一层目录
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("name", sRoot);
                    map.put("path", sRoot);
                    map.put("img", getImageId(sRoot));
                    list.add(map);

                    map = new HashMap<String, Object>();
                    map.put("name", sParent);
                    map.put("path", path);
                    map.put("img", getImageId(sParent));
                    list.add(map);
                }

                for(File file: files)
                {
                    if(file.isDirectory() && file.listFiles()!=null){
                        // 添加文件夹
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("name", file.getName());
                        map.put("path", file.getPath());
                        map.put("img", getImageId(sFolder));
                        lfolders.add(map);
                    }
                    else if(file.isFile()){
                        // 添加文件
                        String sf = getSuffix(file.getName()).toLowerCase();

                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("name", file.getName());
                            map.put("path", file.getPath());
                            map.put("img", getImageId(sf));
                            lfiles.add(map);

                    }
                }

                list.addAll(lfolders); // 先添加文件夹，确保文件夹显示在上面
                list.addAll(lfiles);    //再添加文件


                SimpleAdapter adapter = new SimpleAdapter(getContext(), list, R.layout.filedialogitem, new String[]{"img", "name", "path"}, new int[]{R.id.filedialogitem_img, R.id.filedialogitem_name, R.id.filedialogitem_path});
                this.setAdapter(adapter);
                return files.length;
            }
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // 条目选择
                pt = (String) list.get(position).get("path");
                final String fn = (String) list.get(position).get("name");
                if(fn.equals(sRoot) || fn.equals(sParent)){
                    // 如果是更目录或者上一层
                    File fl = new File(pt);
                    String ppt = fl.getParent();
                    if(ppt != null){
                        // 返回上一层
                        path = ppt;
                    }
                    else{
                        // 返回更目录
                        path = sRoot;
                    }
                }
                else{
                    File fl = new File(pt);
                    if(fl.isFile()){
                        // 如果是文件
                        ((Activity)getContext()).dismissDialog(this.dialogid); // 让文件夹对话框消失

                        // 设置回调的返回值
                        Bundle bundle = new Bundle();
                        bundle.putString("path", pt);
                        bundle.putString("name", fn);
                        // 调用事先设置的回调函数
                        this.callback.callback(bundle);


                        new Thread(new Runnable() {
                            @Override
                            public void run() {


                                DefaultHttpClient httpClient = new DefaultHttpClient();
                                String url = "https://api.netease.im/nimserver/msg/upload.action";
                                HttpPost httpPost = new HttpPost(url);
                                String appKey = "9ef05cdfc89ec423204b7dc88218cced";
                                String appSecret = "083dc34b2cc7";
                                String nonce = "12345";
                                String curTime = String.valueOf((new Date()).getTime() / 1000L);
                                String checkSum = CheckSumBuilder.getCheckSum(appSecret, nonce, curTime);
                                httpPost.addHeader("AppKey", appKey);

                                httpPost.addHeader("Nonce", nonce);
                                httpPost.addHeader("CurTime", curTime);
                                httpPost.addHeader("CheckSum", checkSum);
                                httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
                                List<NameValuePair> nvps = new ArrayList<NameValuePair>();

                                try {
                                    String s =GetImageStr(pt);
                                    nvps.add(new BasicNameValuePair("content", s));
                                } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                try {
                                    httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }

                                try {
                                    HttpResponse response1 = httpClient.execute(httpPost);


                                    HttpEntity entity = response1.getEntity();

                                    if (entity != null) {
                                        String retSrc = EntityUtils.toString(entity);
                                        // parsing JSON
                                        JSONObject result = new JSONObject(retSrc); //Convert String to JSON Object
                                    //120.27.99.18:5432/

                                        String url1 = result.getString("url");

                                        String target = "http://120.27.99.18:8080/file/servlet/AddFile?name=" + fn + "&url=" + url1;
                                        URL url2;

                                        try {
                                            url2 = new URL(target);
                                            HttpURLConnection urlConn = (HttpURLConnection) url2
                                                    .openConnection();
                                            InputStreamReader in = new InputStreamReader(
                                                    urlConn.getInputStream());
                                            BufferedReader buffer = new BufferedReader(in);


                                            in.close();
                                            urlConn.disconnect();
                                        } catch (MalformedURLException e) {
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }}).start();
                        return;
                    }
                    else if(fl.isDirectory()){
                        // 如果是文件夹
                        // 那么进入选中的文件夹
                        path = pt;
                    }
                }
                this.refreshFileList();
            }
        }



        public static String GetImageStr(String imgFile) {
            InputStream in = null;
            byte[] data = null;
            // 读取文件字节数组
            try {
                in = new FileInputStream(imgFile);
                data = new byte[in.available()];
                in.read(data);
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


            // 返回Base64编码过的字节数组字符串
            return Base64.encodeToString(data, Base64.DEFAULT);
        }
    }

