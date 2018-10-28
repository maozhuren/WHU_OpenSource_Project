package article2.dummy;

import android.os.Environment;
import android.util.Log;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {
    private static String resultList1 = "";
    private static String resultList2 = "";
    public static ArrayList<String> fileItem = new ArrayList<String>();
    private static ArrayList<String> urlItem = new ArrayList<String>();

    /**
     * An array of sample (dummy) items.
     */
    public static List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    private static final int COUNT = 1;

    static {
        // Add some sample items.
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    showPassage();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    getUrl();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    download();
                    int total= fileItem.size();

                    for (int i = 0; i < total; i++) {
                        addItem(createDummyItem(i));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }}).start();


    }

   public static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }
public static void download() throws IOException {
    int t =urlItem.size();
    for(int i=0;i<t;i++){
        downloadFromUrl(urlItem.get(i), fileItem.get(i), Environment.getExternalStorageDirectory().getPath()+"/downloadhtml");
    }

}
    public static DummyItem createDummyItem(int position) {
        return new DummyItem(String.valueOf(position), fileItem.get(position), makeDetails(position));
    }
    public static void downloadFromUrl( final String url,  final String name,  final String dir) throws IOException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("Http", "url: " + url);
                URL httpUrl = null;
                try {
                    httpUrl = new URL(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                File f = new File(dir + "/" + name);  //a=文件名，doc=类型应从数据库获得

                try {
                    FileUtils.copyURLToFile(httpUrl, f);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }
    public static void showPassage() throws JSONException {

                String target = "http://120.27.99.18:8080/passage/servlet/PassageList";
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
                        resultList1 += inputLine + "\n";
                    }
                    in.close();
                    urlConn.disconnect();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    JSONObject jsonObject = new JSONObject(resultList1);

                    JSONArray jsonArray = jsonObject.getJSONArray("passage");
                    int t = jsonArray.length();
                    for (int i = 0; i < t; i++) {
                        JSONObject jsonfile = jsonArray.getJSONObject(i);
                        String name = jsonfile.getString("name");//获取对象的参数
                        fileItem.add(name);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }





    }
    public static void getUrl() throws JSONException {


                String target = "http://192.168.191.1:8080/passage/servlet/UrlList";
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
                        resultList2 += inputLine + "\n";
                    }
                    in.close();
                    urlConn.disconnect();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    JSONObject jsonObject = new JSONObject(resultList2);

                    JSONArray jsonArray = jsonObject.getJSONArray("passage");
                    int t = jsonArray.length();
                    for (int i = 0; i < t; i++) {
                        JSONObject jsonfile = jsonArray.getJSONObject(i);
                        String name = jsonfile.getString("url");//获取对象的参数
                        urlItem.add(name);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }





    }
    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {




        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public String id;
        public String content;
        public String details;

        public DummyItem(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
