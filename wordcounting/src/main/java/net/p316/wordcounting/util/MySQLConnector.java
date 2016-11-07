package net.p316.wordcounting.util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLConnector {
   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
   static final String DB_URL = "jdbc:mysql://news.p316.net/news_crawler";
   //string형 변수 DB_URL에 DB연동을 할 주소(URL)저장
   
   static final String USER = "crawler";
   //String형 변수 USER에 아이디 crawler저장
   static final String PASS = "4X\"Zd@JaTs\\Yk<c]";
   //String형 변수 PASS에 비밀번호 저장
   
   private DriverManager driverManager;
   private Connection conn = null;
   
   public MySQLConnector(){
	   try{
		   conn = driverManager.getConnection(DB_URL
				   + "?characterEncoding=utf8"
				   + "&user=" + USER + "&password=" + PASS);
	   } catch(Exception ex){
		   System.out.println("SQLException: " + ex.getMessage());
	   }
   }
   
   public void simpleInsertTitle(String _href, String _title, String _comp, String _day){
	   Statement stmt = null;
	   ResultSet rs = null;
	   try{
		   stmt = conn.createStatement();
		   stmt.execute("INSERT INTO `nc_title`"
		   		+ "(`idx`, `idx_category`, `url`, `title`, `company`, `date`)"
		   		+ "VALUES (NULL,"
		   		+ "'1',"
		   		+ "'" + _href + "',"
		   		+ "'" + _title + "',"
		   		+ "'" + _comp + "',"
		   		+ "'" + _day + "')");
	   } catch(Exception ex){
		   
	   } finally {
		   
	   }
   }
   
   public void testConn(){
	   Statement stmt = null;
	   ResultSet rs = null;
	   try{
		   stmt = conn.createStatement();
		   stmt.execute("SELECT `title` FROM `nc_title`");
	   } catch(Exception ex){
		   
	   } finally {
		   
	   }
   }
   
   public List<String> getTitles(){
	   List<String> list = new ArrayList<String>();
	   Statement stmt = null;
	   ResultSet rs = null;
	   
	   try{
		   Class.forName("com.mysql.jdbc.Driver");
		   
		   conn = DriverManager.getConnection(DB_URL, USER, PASS);
		   
		   stmt = conn.createStatement();
		   // 데이터베이스 질의를 통해 원하는 테이블 스크랩 범위를 지정함
		   String sql = "SELECT `title` FROM `nc_title`";
		   // 질의 실행
		   rs = stmt.executeQuery(sql);
		   // 데이터베이스를 모두 읽어서 String에 저장(현재는 title)
		   while(rs.next()){
			   list.add(rs.getString("title"));
		   }
		   
		   
	   } catch(Exception ex){
		   
	   }
	   
	   return list;
   }
}