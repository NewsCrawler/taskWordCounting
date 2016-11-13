// 새로 만들기 위해 생성한 클래스.... 그러나 의미 없다 제대로 돌아가버렸다!
package net.p316.wordcounting.util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.Set;

public class newMySQLConnector {
   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
   static final String DB_URL = "jdbc:mysql://news.p316.net/news_crawler";
   //string형 변수 DB_URL에 DB연동을 할 주소(URL)저장
   
   static final String USER = "word";
   //String형 변수 USER에 아이디 crawler저장
   static final String PASS = "s:7Nq*?2AEEQ~h+%";
   //String형 변수 PASS에 비밀번호 저장
   
   //제목과 단어리스트를 맵에 등록하기 위한 변수
   public HashMap<Integer, String> titleMap = new HashMap<Integer, String>();
   public HashMap<Integer, String> wordMap = new HashMap<Integer, String>();
   
   private DriverManager driverManager;
   private Connection conn = null;
   
   public void MySQLConnector(){
	   try{
		   conn = driverManager.getConnection(DB_URL
				   + "?characterEncoding=utf8"
				   + "&user=" + USER + "&password=" + PASS);
	   } catch(Exception ex){
		   System.out.println("SQLException: " + ex.getMessage());
	   }
   }

   public void insertWordToDB(HashMap<Integer, String> titleMap2, HashMap<Integer, String> wordMap2, Integer integer) {
	   Statement stmt = null;
	   ResultSet rs = null;
	   try{
		   stmt.execute("INSERT INTO `nc_counter_title_word`"
		   		+ "(`idx`, `idx_title`, `idx_word`, `cnt`)"
		   		+ "VALUES (NULL,"
		   		+ "'" + titleMap2 + "',"
		   		+ "'" + wordMap2 + "',"
		   		+ "'" + integer + "'");
		   System.out.println(titleMap2 + " " + wordMap2 + " " + integer + " ");
	   } catch(Exception ex){
		   System.out.println("문제발생!");
	   } finally {
		   
	   }
   }

   public Map<Integer, String> getTitles(){
	   Map<Integer, String> Tmap = null;
	   Statement stmt = null;
	   ResultSet rs = null;
	   
	   try{
		   Class.forName("com.mysql.jdbc.Driver");
		   
		   conn = DriverManager.getConnection(DB_URL, USER, PASS);
		   
		   stmt = conn.createStatement();
		   String sql = "SELECT `title` FROM `nc_title`";
		   rs = stmt.executeQuery(sql);
		   while(rs.next()){
			   Tmap.put(rs.getRow(), rs.getString("title"));
			   System.out.println(Tmap.get(rs.getRow()));
		   }
		   
		   
	   } catch(Exception ex){
		   
	   }   
	   return Tmap;
   }
   
   public List<String> getWords(){	   
	   List<String> list = new ArrayList<String>();
	   Statement stmt = null;
	   ResultSet rs = null;
	   
	   try{
		   Class.forName("com.mysql.jdbc.Driver");
		   
		   conn = DriverManager.getConnection(DB_URL, USER, PASS);
		   
		   stmt = conn.createStatement();
		   // 데이터베이스 질의를 통해 원하는 테이블 스크랩 범위를 지정함
		   String sql = "SELECT idx, `word` FROM `nc_word_table`";
		   // 질의 실행
		   rs = stmt.executeQuery(sql);
		   // 데이터베이스를 모두 읽어서 String에 저장(현재는 title)
		   while(rs.next()){
			   wordMap.put(rs.getRow(), rs.getString("word"));
			   //list.add(rs.getString("title"));
			   System.out.println(wordMap.get(rs.getRow()));
		   }
		   	   
	   } catch(Exception ex){
		   
	   }
	   return list;
   }
}