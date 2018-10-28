package passage;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Addpassage extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public Addpassage() {
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
		String name =request.getParameter("name");
		String url =request.getParameter("url");
        String insert_sql = "insert into  passage values( '"+name+"','"+url+"')";
        // String insert_sql = "insert into User values('" + accid + "','" + token + "','" + account + "','" + password + "')";
        PrintWriter out = response.getWriter();
        try {
			stmt.executeQuery(insert_sql);
			
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
