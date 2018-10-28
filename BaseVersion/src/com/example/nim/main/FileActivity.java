package com.example.nim.main;


import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nim.R;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class FileActivity extends AppCompatActivity {
    private String resultList = "";
    private String resultURL = "";
    private ListView fileList;
    private  String  fileName="";
    private ArrayList<String> fileItem = new ArrayList<String>();
    private Handler handler;

    static private int openfileDialogId = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);

        fileList = (ListView) findViewById(R.id.listView);
        new Thread(networkTask1).start();

        fileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                fileName = fileItem.get(position);
                new Thread(new Runnable() {
                    public void run() {
//                        String url = null;
                        try {
                            sendAndGetURL(fileName);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
//                            downloadFromUrl(url, fileName, Environment.getExternalStorageDirectory().getPath());
//                            Message m = handler.obtainMessage();
//                            m.what=2;
//                            handler.sendMessage(m);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();


            }
        });



        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 1:
                        Toast.makeText(FileActivity.this, "得到网址", Toast.LENGTH_LONG).show();
                        String url = msg.getData().getString("url");
                        download(url);


                        break;
                    case 2:
                        Toast.makeText(FileActivity.this, "下载成功在"+Environment.getExternalStorageDirectory().getPath(), Toast.LENGTH_LONG).show();
                        break;
                    case 3:
                        Toast.makeText(FileActivity.this, "上传成功", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        break;
                }

                // TODO
                // UI界面的更新等相关操作

            }
        };



    }

    Runnable networkTask1 = new Runnable() {

        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作
            try {
                showFile();

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    };
//    Runnable networkTask2 = new Runnable() {
//
//        @Override
//        public void run() {
//            // TODO
//            // 在这里进行 http request.网络请求相关操作
//            try {
//
//                downloadFromUrl(url, fileName, Environment.getExternalStorageDirectory().getPath());
//                Message m = handler.obtainMessage();
//                handler.sendMessage(m);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    };



    //192.168.191.2
    public ArrayList showFile() throws JSONException {
        String target = "http://120.27.99.18:8080/file/servlet/FileList";
        URL url;
        try {
            url = new URL(target);
            HttpURLConnection urlConn = (HttpURLConnection) url
                    .openConnection();
            InputStreamReader in = new InputStreamReader(
                    urlConn.getInputStream());
            BufferedReader buffer = new BufferedReader(in);
            String inputLine = null;

            while ((inputLine = buffer.readLine()) != null) {
                resultList += inputLine + "\n";
            }
            in.close();
            urlConn.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            JSONObject jsonObject = new JSONObject(resultList);
            JSONArray jsonArray = jsonObject.getJSONArray("file");
            int t = jsonArray.length();
            for (int i = 0; i < t; i++) {
                JSONObject jsonfile = jsonArray.getJSONObject(i);
                String name = jsonfile.getString("name");//获取对象的参数
                fileItem.add(name);
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        ArrayAdapter<String> fileAd = new ArrayAdapter<String>(FileActivity.this,
                                android.R.layout.simple_list_item_1, fileItem);
                        fileList.setAdapter(fileAd);
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return fileItem;


    }

    public void download(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    downloadFromUrl(url, fileName, Environment.getExternalStorageDirectory().getPath());
                    Message m = handler.obtainMessage();
                    m.what=2;
                    handler.sendMessage(m);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


    public void sendAndGetURL(final String name) throws IOException {

        new Thread(new Runnable() {
            public void run() {
                resultURL = "";
                String target = "http://120.27.99.18:8080/file/servlet/getURL?name=" + name;
                URL url;
                try {

                    url = new URL(target);
                    HttpURLConnection urlConn = (HttpURLConnection) url
                            .openConnection();
                    InputStreamReader in = new InputStreamReader(
                            urlConn.getInputStream());
                    BufferedReader buffer = new BufferedReader(in);
                    String inputLine = null;

                    while ((inputLine = buffer.readLine()) != null) {
                        resultURL += inputLine + "\n";
                    }
                    in.close();
                    urlConn.disconnect();
                    Bundle bundle = new Bundle();
                    bundle.putString("url", resultURL);
                    Message m = handler.obtainMessage();
                    m.what=1;
                    m.setData(bundle);
                    handler.sendMessage(m);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


//        return resultURL;
    }

    public static void downloadFromUrl( String url,  String name,  String dir) throws IOException {
        Log.d("Http", "url: " + url);
        URL httpUrl = new URL(url);
        File f = new File(dir + "/" + name);  //a=文件名，doc=类型应从数据库获得

        FileUtils.copyURLToFile(httpUrl, f);
    }
}