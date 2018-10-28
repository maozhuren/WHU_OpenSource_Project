package jack;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class Register {
	public static void register(String account, String pwd,HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		DefaultHttpClient httpClient = new DefaultHttpClient();
        String url = "https://api.netease.im/nimserver/user/create.action";
        HttpPost httpPost = new HttpPost(url);
        String appKey = "9ef05cdfc89ec423204b7dc88218cced";
        String appSecret = "083dc34b2cc7";
        String nonce =  "12345";
        String curTime = String.valueOf((new Date()).getTime() / 1000L);
        String checkSum = CheckSumBuilder.getCheckSum(appSecret, nonce ,curTime);//�ο� ����CheckSum��java����

        // ���������header
        httpPost.addHeader("AppKey", appKey);
        httpPost.addHeader("Nonce", nonce);
        httpPost.addHeader("CurTime", curTime);
        httpPost.addHeader("CheckSum", checkSum);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        // ��������Ĳ��� 
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("accid", account));
        nvps.add(new BasicNameValuePair("token", pwd)); 
        httpPost.setEntity(new UrlEncodedFormEntity(nvps));
        
        response.setContentType("textml");
        HttpResponse response1 = null;
        PrintWriter out = null;
        String s = null;
		try {
			response1 = httpClient.execute(httpPost);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    
		try {
			s = EntityUtils.toString(response1.getEntity());
		} catch (ParseException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			out = response.getWriter();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        out.println(s);
	}

}
