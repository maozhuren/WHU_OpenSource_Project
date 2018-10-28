package jack;
import java.io.IOException;
import java.io.PrintWriter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebServlet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
/**
 * Servlet implementation class ServletForGETMethod
 */
@WebServlet("/ServletForGETMethod")
public class FileList extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FileList() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		doPost(request, response);
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");
        String driverName = "org.postgresql.Driver"; //Çý¶¯Ãû³Æ
        String DBUser = "postgres"; //mysqlÓÃ»§Ãû
        String DBPasswd = "123456"; //mysqlÃÜÂë
        String DBName = "file"; 

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
        String select_sql = "select name from fileinfo";
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
            object.put("file", jsonarray);//向总对象里面添加包含pet的数组  
            file = object.toString();//生成返回字符串  
            return file;
	
	
	}

}


