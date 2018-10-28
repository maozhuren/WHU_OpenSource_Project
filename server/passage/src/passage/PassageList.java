package passage;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PassageList extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/**
	 * Constructor of the object.
	 */
	public PassageList() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		
		response.setContentType("text/html");
		doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		String driverName = "org.postgresql.Driver"; //Çý¶¯Ãû³Æ
        String DBUser = "postgres"; //mysqlÓÃ»§Ãû
        String DBPasswd = "123456"; //mysqlÃÜÂë
        String DBName = "file"; //Êý¾Ý¿âÃû

        String connUrl = "jdbc:postgresql://120.27.99.18:5432/" + DBName + "?user=" + DBUser + "&password=" + DBPasswd;
        
       try {
			Class.forName(driverName).newInstance();
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
        Connection conn = null;
		try {
			conn = DriverManager.getConnection(connUrl);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
//stmt.executeQuery("SET NAMES UTF8");
       // String accid = "1";
      //  String token = "2";
       // String account = "3";
      //  String password = "4";
        String select_sql = "select name from passage";
        // String insert_sql = "insert into User values('" + accid + "','" + token + "','" + account + "','" + password + "')";
        PrintWriter out = response.getWriter();
        
        try {
            ResultSet rs = stmt.executeQuery(select_sql);
           
           String r =fileToJson(rs);
            	 
            	out.println(r);
            System.out.println(r);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
        public String fileToJson(ResultSet rs) throws JSONException, SQLException {  
        	String file ="";//定义返回字符串
            JSONObject object = new JSONObject();//创建一个总的对象，这个对象对整个json串  
            JSONArray jsonarray = new JSONArray();//json数组，里面包含的内容为fileitem的所有对象
            while(rs.next()){
            String str =rs.getString("name");
            JSONObject jsonObj = new JSONObject();//1个对象，json形式  
            jsonObj.put("name", str);
            jsonarray.put(jsonObj);//
            } 
            object.put("passage", jsonarray);//向总对象里面添加包含pet的数组  
            file = object.toString();//生成返回字符串  
            return file;
	
	
	
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
